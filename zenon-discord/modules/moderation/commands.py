"""
모더레이션 (T15) — moderation.md §1 구현.

기록계(경고):
  - 🟢 /경고        (유저, 사유)  — warnings 추가 + DM + **통합 누적 경고수**(디코+마크) 안내
    + **자동 제재**(누적 임계: WARN_TIMEOUT_THRESHOLD→타임아웃, WARN_BAN_THRESHOLD→차단, §2).
  - 🟢 /경고목록    (유저)        — 경고 이력(활성/철회) 조회
  - 🟢 /경고취소    (경고_id)     — warnings.active=0(철회, 이력 보존)

통합 카운트(§2): 누적 경고 = 디코 경고(봇 DB) + 마크 경고(ZenonMonCore 조회, 연동 Discord ID
기준·베스트에포트). 마크 API 미설정/미연동/실패 시 디코 기준만. 자동 제재는 이 통합 카운트로
판정하며, **디스코드 경고 시점**(/경고·패널)에 계산·실행된다(마크 경고만 쌓인 경우는 다음 디코
경고나 조회 때 반영). 자동 제재 주체 = 봇(operator=bot), 봇 권한/위계 부족 시 안내만.

상태변경 제재(디스코드 멤버 상태 변경 — 사용자 명시 승인하 구현, 2026-06-09):
  - 🟢 /타임아웃     (유저, 기간, 단위, 사유) — 디스코드 timeout(최대 28일) + 사전 DM
  - 🟢 /타임아웃해제 (유저)                   — timeout 해제
  - 🟢 /추방         (유저, 사유)             — kick + 사전 DM
  - 🟢 /차단         (유저, 사유)             — ban + 사전 DM
  - 🟢 /차단해제     (유저_id)               — unban(대상은 길드에 없음 → id 입력)

공통 규약:
  - 권한: 경고·타임아웃 = admin·support / 추방·차단·차단해제 = admin. Owner 항상 통과.
  - 대상 보호(§1b): 봇·자기 자신·서버 소유자·operator와 같거나 상위 권한자 거부
    + 디스코드 역할 위계상 봇보다 상위면 거부(_bot_hierarchy_reject).
  - DM: 추방/차단은 길드 제거 전 발송(이후 공유 서버 없어 DM 불가). 모두 베스트에포트.
  - 적재: core.mod_log.record() 만 사용(raw INSERT 금지). 응답 ephemeral.
"""
from __future__ import annotations

import logging
from datetime import timedelta

import discord
from discord import app_commands
from discord.ext import commands

from core import config, mod_log, permissions, servers, warnings
from core.permissions import requires_permission
from integrations.zenon_mon_api import ZenonMonAdminError, ZenonMonApiClient

# 타임아웃 단위 → 분 환산. 디스코드 timeout 상한 = 28일.
_UNIT_MINUTES = {"m": 1, "h": 60, "d": 1440}
_TIMEOUT_MAX_MINUTES = 28 * 1440

log = logging.getLogger(__name__)

# 경고/철회를 다룰 수 있는 권한 키
_MOD_ROLES = ("admin", "support")
_SANCTION_LABELS = {
    "warn": "경고",
    "warn_revoke": "경고취소",
    "timeout": "타임아웃",
    "timeout_clear": "타임아웃해제",
    "kick": "추방",
    "ban": "차단",
    "unban": "차단해제",
}


def _target_reject_reason(
    operator: discord.Member, target: discord.Member, bot_user: discord.abc.User | None
) -> str | None:
    """제재 대상 보호 판정(§1b). 거부 사유 문자열, 통과면 None."""
    if target.bot:
        return "봇은 제재 대상이 아닙니다."
    if bot_user is not None and target.id == bot_user.id:
        return "봇은 제재 대상이 아닙니다."
    if target.id == operator.id:
        return "자기 자신은 제재할 수 없습니다."
    if target.id == target.guild.owner_id:
        return "서버 소유자는 제재할 수 없습니다."
    t_rank = permissions.permission_rank(target)
    if t_rank and t_rank >= permissions.permission_rank(operator):
        return "대상이 본인과 같거나 상위 권한자입니다. 제재할 수 없습니다."
    return None


def _bot_hierarchy_reject(guild: discord.Guild, target: discord.Member) -> str | None:
    """디스코드 역할 위계상 봇이 대상에 조치 가능한지(§1b). 불가면 사유.

    봇의 최상위 역할이 대상보다 높아야 timeout/kick/ban 이 가능하다(디스코드 제약).
    """
    me = guild.me
    if me is None:
        return "봇 멤버 정보를 찾을 수 없습니다."
    if target.top_role >= me.top_role:
        return "대상의 역할이 봇과 같거나 높아 조치할 수 없습니다(봇 역할을 상위로 올려주세요)."
    return None


class SanctionPanelView(discord.ui.View):
    """제재내역 채널 고정 패널."""

    def __init__(self, cog: "ModerationCog") -> None:
        super().__init__(timeout=None)
        self.cog = cog

        mine = discord.ui.Button(
            label="내 제재 확인",
            style=discord.ButtonStyle.secondary,
            custom_id="sanction_panel_mine",
        )
        mine.callback = self._on_mine
        self.add_item(mine)

        lookup = discord.ui.Button(
            label="유저 조회",
            style=discord.ButtonStyle.secondary,
            custom_id="sanction_panel_lookup",
        )
        lookup.callback = self._on_lookup
        self.add_item(lookup)

        warn = discord.ui.Button(
            label="경고 부여",
            style=discord.ButtonStyle.danger,
            custom_id="sanction_panel_warn",
        )
        warn.callback = self._on_warn
        self.add_item(warn)

        revoke = discord.ui.Button(
            label="경고 취소",
            style=discord.ButtonStyle.secondary,
            custom_id="sanction_panel_revoke",
        )
        revoke.callback = self._on_revoke
        self.add_item(revoke)

    async def _on_mine(self, interaction: discord.Interaction) -> None:
        await self.cog.send_sanction_summary(interaction, interaction.user.id)

    async def _on_lookup(self, interaction: discord.Interaction) -> None:
        if not self.cog._can_moderate(interaction):
            await interaction.response.send_message("운영진만 조회할 수 있습니다.", ephemeral=True)
            return
        await interaction.response.send_modal(SanctionLookupModal(self.cog))

    async def _on_warn(self, interaction: discord.Interaction) -> None:
        if not self.cog._can_moderate(interaction):
            await interaction.response.send_message("경고 권한이 없습니다.", ephemeral=True)
            return
        await interaction.response.send_message(
            "경고할 유저를 선택하세요.",
            view=SanctionWarnSelectView(self.cog),
            ephemeral=True,
        )

    async def _on_revoke(self, interaction: discord.Interaction) -> None:
        if not self.cog._can_moderate(interaction):
            await interaction.response.send_message("경고 취소 권한이 없습니다.", ephemeral=True)
            return
        await interaction.response.send_modal(SanctionRevokeModal(self.cog))


class SanctionLookupModal(discord.ui.Modal, title="유저 제재 조회"):
    user_id = discord.ui.TextInput(
        label="Discord 유저 ID",
        placeholder="숫자 ID",
        min_length=17,
        max_length=20,
    )

    def __init__(self, cog: "ModerationCog") -> None:
        super().__init__()
        self.cog = cog

    async def on_submit(self, interaction: discord.Interaction) -> None:
        try:
            target_id = int(str(self.user_id.value).strip())
        except ValueError:
            await interaction.response.send_message("유저 ID는 숫자여야 합니다.", ephemeral=True)
            return
        await self.cog.send_sanction_summary(interaction, target_id)


class SanctionWarnModal(discord.ui.Modal, title="경고 부여"):
    """사유 입력 모달. 대상은 SanctionWarnSelectView 에서 이름으로 고른 뒤 넘어온다."""
    reason = discord.ui.TextInput(
        label="사유",
        style=discord.TextStyle.paragraph,
        min_length=1,
        max_length=1000,
    )

    def __init__(self, cog: "ModerationCog", target_id: int) -> None:
        super().__init__()
        self.cog = cog
        self.target_id = target_id

    async def on_submit(self, interaction: discord.Interaction) -> None:
        await self.cog.issue_warning_by_id(interaction, self.target_id, str(self.reason.value))


class SanctionWarnSelectView(discord.ui.View):
    """경고 대상 유저를 이름으로 선택. 모달엔 멤버 선택기를 못 넣어 UserSelect 로 고른 뒤
    사유 모달을 띄운다(패널에서 숫자 ID 입력 없이 이름으로 경고)."""

    def __init__(self, cog: "ModerationCog") -> None:
        super().__init__(timeout=120)
        self.cog = cog
        self.select = discord.ui.UserSelect(
            placeholder="경고할 유저를 선택하세요", min_values=1, max_values=1
        )
        self.select.callback = self._on_select
        self.add_item(self.select)

    async def _on_select(self, interaction: discord.Interaction) -> None:
        member = self.select.values[0]
        await interaction.response.send_modal(SanctionWarnModal(self.cog, member.id))


class SanctionRevokeModal(discord.ui.Modal, title="경고 취소"):
    warning_id = discord.ui.TextInput(
        label="경고 ID",
        placeholder="/경고목록 또는 내 제재 확인에 표시된 #번호",
        min_length=1,
        max_length=12,
    )

    def __init__(self, cog: "ModerationCog") -> None:
        super().__init__()
        self.cog = cog

    async def on_submit(self, interaction: discord.Interaction) -> None:
        try:
            warning_id = int(str(self.warning_id.value).strip().lstrip("#"))
        except ValueError:
            await interaction.response.send_message("경고 ID는 숫자여야 합니다.", ephemeral=True)
            return
        await self.cog.revoke_warning_by_id(interaction, warning_id)


class ModerationCog(commands.Cog):
    def __init__(self, bot: commands.Bot) -> None:
        self.bot = bot
        # 통합 경고 카운트용 마크 제재 조회 클라이언트(세션은 지연 생성).
        self._mon_api = ZenonMonApiClient()

    async def cog_unload(self) -> None:
        await self._mon_api.close()

    @property
    def db(self):
        return self.bot.db  # type: ignore[attr-defined]

    @commands.Cog.listener()
    async def on_ready(self) -> None:
        self.bot.add_view(SanctionPanelView(self))

    # ─── 통합 경고 카운트 / 자동 제재 (moderation.md §2) ──────────────
    async def _combined_warn_count(self, target_id: int) -> tuple[int, int, int, str]:
        """(디코 활성 경고, 마크 경고, 합계, 안내주석) 반환.

        마크 경고는 ZenonMonCore 조회(연동 Discord ID 기준, 베스트에포트). API 미설정·
        실패·미연동이면 마크 0 으로 폴백(합계=디코만). 통합 카운트 = 디코 + 마크.
        """
        dcount = await warnings.count_active(self.db, target_id)
        mcount = 0
        note = ""
        if config.ZENON_MON_API_URL and config.ZENON_MON_API_KEY:
            try:
                result = await self._mon_api.list_minecraft_sanctions(str(target_id))
                if result.get("ok"):
                    items = result.get("sanctions") or result.get("items") or []
                    if isinstance(items, list):
                        mcount = sum(
                            1 for it in items
                            if isinstance(it, dict) and it.get("action") == "warn"
                            and it.get("active", True)
                        )
            except ZenonMonAdminError:
                note = " (마크 경고 조회 실패 — 디코 기준만)"
                log.warning("통합 경고 카운트: 마크 조회 실패 target=%s", target_id, exc_info=True)
        return dcount, mcount, dcount + mcount, note

    async def _auto_escalate(
        self, guild: discord.Guild, target: discord.Member, total: int
    ) -> str:
        """누적 경고 임계 자동 제재. 취한 조치 안내문(없으면 "")을 반환.

        임계는 config(WARN_BAN_THRESHOLD > WARN_TIMEOUT_THRESHOLD). 각 0 = 비활성.
        조치 주체 = 봇(operator_id = bot). 봇 권한/위계 부족 시 안내만.
        """
        ban_at = config.WARN_BAN_THRESHOLD
        to_at = config.WARN_TIMEOUT_THRESHOLD
        to_min = config.WARN_TIMEOUT_MINUTES
        bot_id = self.bot.user.id if self.bot.user else 0
        try:
            if ban_at and total >= ban_at:
                await self._dm(target, f"🔨 누적 경고 {total}회로 자동 차단되었습니다.")
                await guild.ban(
                    target, reason=f"자동 제재: 누적 경고 {total}회(임계 {ban_at})",
                    delete_message_seconds=0,
                )
                log_id = await mod_log.record(
                    self.bot, action="ban", operator_id=bot_id, target_id=target.id,
                    reason=f"자동 제재(누적 경고 {total}회)", detail={"auto": True, "total": total},
                )
                await self._post_public_sanction(
                    "ban", log_id, bot_id, target.id, f"자동 제재(누적 {total}회)"
                )
                return f"\n🔨 **자동 차단** 실행(누적 {total}회 ≥ {ban_at})."
            if to_at and total >= to_at:
                if target.is_timed_out():
                    return ""  # 이미 타임아웃 중 — 중복 적용 안 함
                await self._dm(
                    target, f"⏲ 누적 경고 {total}회로 {to_min}분 자동 타임아웃되었습니다."
                )
                await target.timeout(
                    timedelta(minutes=to_min),
                    reason=f"자동 제재: 누적 경고 {total}회(임계 {to_at})",
                )
                log_id = await mod_log.record(
                    self.bot, action="timeout", operator_id=bot_id, target_id=target.id,
                    reason=f"자동 제재(누적 경고 {total}회)",
                    detail={"auto": True, "total": total, "minutes": to_min},
                )
                await self._post_public_sanction(
                    "timeout", log_id, bot_id, target.id, f"자동 제재(누적 {total}회)",
                    {"minutes": to_min},
                )
                return f"\n⏲ **자동 타임아웃 {to_min}분** 실행(누적 {total}회 ≥ {to_at})."
        except discord.Forbidden:
            return "\n⚠ 자동 제재 실패(봇 권한/위계 부족 — 수동 조치 필요)."
        except discord.HTTPException:
            log.warning("자동 제재 처리 오류 target=%s", target.id, exc_info=True)
            return "\n⚠ 자동 제재 처리 중 오류(수동 조치 필요)."
        return ""

    async def _process_warning(
        self, interaction: discord.Interaction, target: discord.Member, reason: str
    ) -> None:
        """경고 부여 공용 처리(슬래시 /경고 · 패널 공용). 대상 보호는 호출부에서 선검증.

        통합 카운트(디코+마크) 산출에 외부 조회가 있어 먼저 defer 한다.
        """
        operator = interaction.user
        await interaction.response.defer(ephemeral=True)

        wid = await warnings.add_warning(
            self.db, user_id=target.id, reason=reason, operator_id=operator.id
        )
        dcount, mcount, total, mark_note = await self._combined_warn_count(target.id)
        breakdown = f"(디코 {dcount}" + (f" + 마크 {mcount}" if mcount else "") + ")"

        dm_ok = await self._dm(
            target,
            f"⚠ 경고를 받았습니다.\n**사유:** {reason}\n현재 누적 경고: **{total}회** {breakdown}",
        )
        log_id = await mod_log.record(
            self.bot, action="warn", operator_id=operator.id, target_id=target.id, reason=reason,
            detail={"warning_id": wid, "discord_active": dcount, "mark_warns": mcount, "total": total},
        )
        await self._post_public_sanction(
            "warn", log_id, operator.id, target.id, reason,
            {"warning_id": wid, "active_count": total},
        )

        auto_note = ""
        if interaction.guild is not None:
            auto_note = await self._auto_escalate(interaction.guild, target, total)

        dm_note = "" if dm_ok else " (DM 전송 실패)"
        await interaction.followup.send(
            f"✅ {target.mention} 경고 등록(`#{wid}`). 누적 경고 **{total}회** {breakdown}."
            f"{mark_note}{dm_note}{auto_note}",
            ephemeral=True,
            allowed_mentions=discord.AllowedMentions.none(),
        )

    def _can_moderate(self, interaction: discord.Interaction) -> bool:
        member = interaction.guild.get_member(interaction.user.id) if interaction.guild else None
        return isinstance(member, discord.Member) and permissions.member_has_permission(
            member, *_MOD_ROLES
        )

    # ─── /경고 ────────────────────────────────────────────────────
    @app_commands.command(name="경고", description="유저에게 경고를 부여합니다(기록 + DM 통보).")
    @app_commands.describe(유저="경고 대상", 사유="경고 사유")
    @requires_permission(*_MOD_ROLES)
    async def warn(
        self, interaction: discord.Interaction, 유저: discord.Member, 사유: str
    ) -> None:
        reject = _target_reject_reason(interaction.user, 유저, self.bot.user)  # type: ignore[arg-type]
        if reject:
            await interaction.response.send_message(reject, ephemeral=True)
            return
        await self._process_warning(interaction, 유저, 사유)

    # ─── /경고목록 ────────────────────────────────────────────────
    @app_commands.command(name="경고목록", description="유저의 경고 이력(활성/철회)을 조회합니다.")
    @app_commands.describe(유저="조회 대상")
    @requires_permission(*_MOD_ROLES)
    async def warn_list(
        self, interaction: discord.Interaction, 유저: discord.Member
    ) -> None:
        rows = await warnings.list_warnings(self.db, 유저.id)
        if not rows:
            await interaction.response.send_message(
                f"{유저.mention} 의 경고 이력이 없습니다.",
                ephemeral=True,
                allowed_mentions=discord.AllowedMentions.none(),
            )
            return
        active_n = sum(1 for r in rows if r["active"])
        lines = []
        for r in rows:
            mark = "🟥" if r["active"] else "⬜"
            ts = f"<t:{r['created_at']}:d>"
            lines.append(
                f"{mark} `#{r['id']}` {ts} — {r['reason'] or '(사유 없음)'} "
                f"· 처리 <@{r['operator_id']}>"
            )
        embed = discord.Embed(
            title=f"경고 이력 — {유저.display_name}",
            description="\n".join(lines)[:4000],
            color=discord.Color.orange(),
        )
        embed.set_footer(text=f"전체 {len(rows)}건 · 활성 {active_n}건")
        await interaction.response.send_message(
            embed=embed, ephemeral=True, allowed_mentions=discord.AllowedMentions.none()
        )

    # ─── /경고취소 ────────────────────────────────────────────────
    @app_commands.command(name="경고취소", description="경고를 철회합니다(이력은 보존).")
    @app_commands.describe(경고_id="철회할 경고 ID(/경고목록 의 #번호)")
    @requires_permission(*_MOD_ROLES)
    async def warn_revoke(self, interaction: discord.Interaction, 경고_id: int) -> None:
        row = await warnings.get_warning(self.db, 경고_id)
        if row is None:
            await interaction.response.send_message(
                f"경고 `#{경고_id}` 를 찾을 수 없습니다.", ephemeral=True
            )
            return
        if not row["active"]:
            await interaction.response.send_message(
                f"경고 `#{경고_id}` 는 이미 철회된 상태입니다.", ephemeral=True
            )
            return
        await self._revoke_warning(interaction.user.id, row, 경고_id)
        await interaction.response.send_message(
            f"↩ 경고 `#{경고_id}` 철회 완료(<@{row['discord_user_id']}>).",
            ephemeral=True,
            allowed_mentions=discord.AllowedMentions.none(),
        )

    async def _revoke_warning(self, operator_id: int, row, warning_id: int) -> None:
        await warnings.revoke_warning(self.db, warning_id)
        log_id = await mod_log.record(
            self.bot,
            action="warn_revoke",
            operator_id=operator_id,
            target_id=row["discord_user_id"],
            detail={"warning_id": warning_id},
        )
        await self._post_public_sanction(
            "warn_revoke", log_id, operator_id, row["discord_user_id"], None,
            {"warning_id": warning_id},
        )

    # ─── /타임아웃 ────────────────────────────────────────────────
    @app_commands.command(name="타임아웃", description="유저를 일정 시간 타임아웃합니다(최대 28일).")
    @app_commands.describe(유저="대상", 기간="숫자", 단위="시간 단위", 사유="사유")
    @app_commands.choices(단위=[
        app_commands.Choice(name="분", value="m"),
        app_commands.Choice(name="시간", value="h"),
        app_commands.Choice(name="일", value="d"),
    ])
    @requires_permission("admin", "support")
    async def timeout(
        self,
        interaction: discord.Interaction,
        유저: discord.Member,
        기간: app_commands.Range[int, 1, 40320],
        단위: app_commands.Choice[str],
        사유: str,
    ) -> None:
        reject = self._guard(interaction, 유저)
        if reject:
            await interaction.response.send_message(reject, ephemeral=True)
            return
        minutes = 기간 * _UNIT_MINUTES[단위.value]
        if minutes > _TIMEOUT_MAX_MINUTES:
            await interaction.response.send_message(
                "타임아웃은 최대 28일까지만 가능합니다.", ephemeral=True
            )
            return

        # 사전 DM(베스트에포트) → 적용
        await self._dm(
            유저, f"⏲ {minutes}분 동안 타임아웃되었습니다.\n**사유:** {사유}"
        )
        try:
            await 유저.timeout(timedelta(minutes=minutes), reason=f"{interaction.user}: {사유}")
        except discord.Forbidden:
            await interaction.response.send_message(
                "봇 권한 부족(Moderate Members) 또는 위계 문제로 타임아웃 실패.", ephemeral=True
            )
            return
        except discord.HTTPException:
            await interaction.response.send_message("타임아웃 처리 중 오류가 발생했습니다.", ephemeral=True)
            return

        log_id = await mod_log.record(
            self.bot, action="timeout", operator_id=interaction.user.id,
            target_id=유저.id, reason=사유, detail={"minutes": minutes},
        )
        await self._post_public_sanction(
            "timeout", log_id, interaction.user.id, 유저.id, 사유, {"minutes": minutes},
        )
        await interaction.response.send_message(
            f"⏲ {유저.mention} {minutes}분 타임아웃 완료. 사유: {사유}",
            ephemeral=True, allowed_mentions=discord.AllowedMentions.none(),
        )

    # ─── /타임아웃해제 ────────────────────────────────────────────
    @app_commands.command(name="타임아웃해제", description="유저의 타임아웃을 해제합니다.")
    @app_commands.describe(유저="대상")
    @requires_permission("admin", "support")
    async def timeout_clear(self, interaction: discord.Interaction, 유저: discord.Member) -> None:
        if 유저.timed_out_until is None:
            await interaction.response.send_message("해당 유저는 타임아웃 상태가 아닙니다.", ephemeral=True)
            return
        try:
            await 유저.timeout(None, reason=f"{interaction.user}: 타임아웃 해제")
        except discord.Forbidden:
            await interaction.response.send_message("봇 권한/위계 문제로 해제 실패.", ephemeral=True)
            return
        except discord.HTTPException:
            await interaction.response.send_message("처리 중 오류가 발생했습니다.", ephemeral=True)
            return
        log_id = await mod_log.record(
            self.bot, action="timeout_clear", operator_id=interaction.user.id, target_id=유저.id,
        )
        await self._post_public_sanction("timeout_clear", log_id, interaction.user.id, 유저.id)
        await interaction.response.send_message(
            f"⏲ {유저.mention} 타임아웃 해제 완료.",
            ephemeral=True, allowed_mentions=discord.AllowedMentions.none(),
        )

    # ─── /추방 ────────────────────────────────────────────────────
    @app_commands.command(name="추방", description="유저를 서버에서 추방(kick)합니다.")
    @app_commands.describe(유저="대상", 사유="사유")
    @requires_permission("admin")
    async def kick(self, interaction: discord.Interaction, 유저: discord.Member, 사유: str) -> None:
        reject = self._guard(interaction, 유저)
        if reject:
            await interaction.response.send_message(reject, ephemeral=True)
            return
        # 추방 전 DM(이후 공유 서버 없어 DM 불가)
        await self._dm(유저, f"👢 서버에서 추방되었습니다.\n**사유:** {사유}")
        try:
            await 유저.kick(reason=f"{interaction.user}: {사유}")
        except discord.Forbidden:
            await interaction.response.send_message(
                "봇 권한 부족(Kick Members) 또는 위계 문제로 추방 실패.", ephemeral=True
            )
            return
        except discord.HTTPException:
            await interaction.response.send_message("추방 처리 중 오류가 발생했습니다.", ephemeral=True)
            return
        log_id = await mod_log.record(
            self.bot, action="kick", operator_id=interaction.user.id, target_id=유저.id, reason=사유,
        )
        await self._post_public_sanction("kick", log_id, interaction.user.id, 유저.id, 사유)
        await interaction.response.send_message(
            f"👢 {유저} 추방 완료. 사유: {사유}", ephemeral=True,
        )

    # ─── /차단 ────────────────────────────────────────────────────
    @app_commands.command(name="차단", description="유저를 서버에서 차단(ban)합니다.")
    @app_commands.describe(유저="대상", 사유="사유")
    @requires_permission("admin")
    async def ban(self, interaction: discord.Interaction, 유저: discord.Member, 사유: str) -> None:
        reject = self._guard(interaction, 유저)
        if reject:
            await interaction.response.send_message(reject, ephemeral=True)
            return
        await self._dm(유저, f"🔨 서버에서 차단되었습니다.\n**사유:** {사유}")
        try:
            await interaction.guild.ban(
                유저, reason=f"{interaction.user}: {사유}", delete_message_seconds=0
            )
        except discord.Forbidden:
            await interaction.response.send_message(
                "봇 권한 부족(Ban Members) 또는 위계 문제로 차단 실패.", ephemeral=True
            )
            return
        except discord.HTTPException:
            await interaction.response.send_message("차단 처리 중 오류가 발생했습니다.", ephemeral=True)
            return
        log_id = await mod_log.record(
            self.bot, action="ban", operator_id=interaction.user.id, target_id=유저.id, reason=사유,
        )
        await self._post_public_sanction("ban", log_id, interaction.user.id, 유저.id, 사유)
        await interaction.response.send_message(
            f"🔨 {유저} 차단 완료. 사유: {사유}", ephemeral=True,
        )

    # ─── /차단해제 ────────────────────────────────────────────────
    @app_commands.command(name="차단해제", description="차단된 유저를 해제(unban)합니다.")
    @app_commands.describe(유저_id="차단 해제할 유저의 디스코드 ID")
    @requires_permission("admin")
    async def unban(self, interaction: discord.Interaction, 유저_id: str) -> None:
        try:
            uid = int(유저_id)
        except ValueError:
            await interaction.response.send_message("유저 ID는 숫자여야 합니다.", ephemeral=True)
            return
        try:
            await interaction.guild.unban(discord.Object(id=uid), reason=f"{interaction.user}: 차단해제")
        except discord.NotFound:
            await interaction.response.send_message("차단 목록에 없는 유저입니다.", ephemeral=True)
            return
        except discord.Forbidden:
            await interaction.response.send_message("봇 권한 부족(Ban Members)으로 해제 실패.", ephemeral=True)
            return
        except discord.HTTPException:
            await interaction.response.send_message("처리 중 오류가 발생했습니다.", ephemeral=True)
            return
        log_id = await mod_log.record(
            self.bot, action="unban", operator_id=interaction.user.id, target_id=uid,
        )
        await self._post_public_sanction("unban", log_id, interaction.user.id, uid)
        await interaction.response.send_message(f"🔓 `{uid}` 차단 해제 완료.", ephemeral=True)

    @app_commands.command(name="제재패널", description="제재내역 채널에 자기 조회/운영 제재 패널을 게시합니다.")
    @requires_permission("admin", "support")
    async def sanction_panel(self, interaction: discord.Interaction) -> None:
        guild = interaction.guild
        if guild is None:
            await interaction.response.send_message("길드에서만 사용할 수 있습니다.", ephemeral=True)
            return
        channel = await self._active_log_channel(guild, "제재내역")
        if channel is None:
            await interaction.response.send_message(
                "활성 서버의 로그/제재내역 채널을 찾지 못했습니다.", ephemeral=True
            )
            return
        embed = discord.Embed(
            title="제재 내역",
            description=(
                "본인 제재 기록은 본인에게만 표시됩니다. 운영진은 유저 ID로 경고를 부여하거나 "
                "경고를 취소할 수 있습니다."
            ),
            color=discord.Color.orange(),
        )
        embed.add_field(name="유저", value="내 제재 확인", inline=True)
        embed.add_field(name="운영진", value="유저 조회 · 경고 부여 · 경고 취소", inline=True)
        await channel.send(embed=embed, view=SanctionPanelView(self))
        await interaction.response.send_message(
            f"제재 패널을 게시했습니다: {channel.mention}", ephemeral=True
        )

    # ─── 내부 헬퍼 ────────────────────────────────────────────────
    def _guard(self, interaction: discord.Interaction, target: discord.Member) -> str | None:
        """제재 공통 가드: 대상 보호(§1b) + 봇 위계. 거부 사유, 통과면 None."""
        return (
            _target_reject_reason(interaction.user, target, self.bot.user)  # type: ignore[arg-type]
            or _bot_hierarchy_reject(interaction.guild, target)
        )

    @staticmethod
    async def _dm(member: discord.Member, content: str) -> bool:
        """대상에게 DM(베스트에포트). 성공 여부 반환(차단/실패=False)."""
        try:
            await member.send(content)
            return True
        except discord.HTTPException:
            return False

    async def _active_log_channel(
        self, guild: discord.Guild, name: str
    ) -> discord.TextChannel | None:
        active = await servers.get_any_active(self.db)
        if active is None:
            return None
        category_id = None
        for row in await servers.get_categories(self.db, active["id"]):
            if row["group_key"] == "logs":
                category_id = row["category_id"]
                break
        category = guild.get_channel(category_id) if category_id else None
        if not isinstance(category, discord.CategoryChannel):
            return None
        for channel in category.text_channels:
            if channel.name == name:
                return channel
        return None

    async def _post_public_sanction(
        self,
        action: str,
        log_id: int,
        operator_id: int,
        target_id: int | None,
        reason: str | None = None,
        detail: dict | None = None,
    ) -> None:
        guild = self.bot.get_guild(config.GUILD_ID) if hasattr(config, "GUILD_ID") else None
        if guild is None:
            return
        channel = await self._active_log_channel(guild, "제재내역")
        if channel is None:
            return
        label = _SANCTION_LABELS.get(action, action)
        embed = discord.Embed(
            title=f"제재 기록 - {label}",
            color=discord.Color.orange(),
            timestamp=discord.utils.utcnow(),
        )
        embed.add_field(name="처리자", value=f"<@{operator_id}>", inline=True)
        if target_id is not None:
            embed.add_field(name="대상", value=f"<@{target_id}>", inline=True)
        if reason:
            embed.add_field(name="사유", value=reason[:1024], inline=False)
        if detail:
            detail_str = ", ".join(f"`{k}`={v}" for k, v in detail.items())
            embed.add_field(name="상세", value=detail_str[:1024], inline=False)
        embed.set_footer(text=f"mod_log #{log_id}")
        try:
            await channel.send(embed=embed, allowed_mentions=discord.AllowedMentions.none())
        except discord.HTTPException:
            log.warning("제재내역 게시 실패(mod_log #%s)", log_id, exc_info=True)

    async def send_sanction_summary(
        self, interaction: discord.Interaction, target_id: int
    ) -> None:
        await interaction.response.defer(ephemeral=True)
        warn_rows = await warnings.list_warnings(self.db, target_id)
        log_rows = await mod_log.list_for_target(self.db, target_id, limit=10)
        dcount, mcount, total, mark_note = await self._combined_warn_count(target_id)
        embed = discord.Embed(
            title=f"제재 조회 - {target_id}",
            description=f"누적 활성 경고 **{total}회** (디코 {dcount}"
                       + (f" + 마크 {mcount}" if mcount else "")
                       + f").{mark_note}",
            color=discord.Color.orange(),
        )
        if warn_rows:
            lines = []
            for row in warn_rows[:10]:
                mark = "활성" if row["active"] else "철회"
                lines.append(
                    f"`#{row['id']}` {mark} <t:{row['created_at']}:d> "
                    f"{(row['reason'] or '(사유 없음)')[:80]}"
                )
            embed.add_field(name="경고", value="\n".join(lines)[:1024], inline=False)
        else:
            embed.add_field(name="경고", value="기록 없음", inline=False)

        if log_rows:
            lines = []
            for row in log_rows:
                label = _SANCTION_LABELS.get(row["action"], row["action"])
                reason = f" - {row['reason'][:80]}" if row["reason"] else ""
                lines.append(f"`#{row['id']}` {label} <t:{row['created_at']}:d>{reason}")
            embed.add_field(name="최근 제재 로그", value="\n".join(lines)[:1024], inline=False)
        else:
            embed.add_field(name="최근 제재 로그", value="기록 없음", inline=False)
        await interaction.followup.send(
            embed=embed, ephemeral=True, allowed_mentions=discord.AllowedMentions.none()
        )

    async def issue_warning_by_id(
        self, interaction: discord.Interaction, target_id: int, reason: str
    ) -> None:
        guild = interaction.guild
        operator = interaction.user
        if guild is None or not isinstance(operator, discord.Member):
            await interaction.response.send_message("길드에서만 사용할 수 있습니다.", ephemeral=True)
            return
        target = guild.get_member(target_id)
        if target is None:
            try:
                target = await guild.fetch_member(target_id)
            except discord.HTTPException:
                target = None
        if target is None:
            await interaction.response.send_message("서버 멤버를 찾지 못했습니다.", ephemeral=True)
            return
        reject = _target_reject_reason(operator, target, self.bot.user)  # type: ignore[arg-type]
        if reject:
            await interaction.response.send_message(reject, ephemeral=True)
            return
        await self._process_warning(interaction, target, reason)

    async def revoke_warning_by_id(self, interaction: discord.Interaction, warning_id: int) -> None:
        row = await warnings.get_warning(self.db, warning_id)
        if row is None:
            await interaction.response.send_message(
                f"경고 `#{warning_id}` 를 찾을 수 없습니다.", ephemeral=True
            )
            return
        if not row["active"]:
            await interaction.response.send_message(
                f"경고 `#{warning_id}` 는 이미 철회된 상태입니다.", ephemeral=True
            )
            return
        await self._revoke_warning(interaction.user.id, row, warning_id)
        await interaction.response.send_message(
            f"경고 `#{warning_id}` 철회 완료(<@{row['discord_user_id']}>).",
            ephemeral=True,
            allowed_mentions=discord.AllowedMentions.none(),
        )


async def setup(bot: commands.Bot) -> None:
    await bot.add_cog(ModerationCog(bot))
