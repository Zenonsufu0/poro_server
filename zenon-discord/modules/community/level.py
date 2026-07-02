"""수동 칭호 시스템.

레벨/XP 자동 성장과 레벨 임계 칭호 지급은 비활성화했다.
칭호는 운영자가 생성·부여·회수하고, 유저는 보유 칭호 중 1개를 장착한다.
"""
from __future__ import annotations

import logging
import re

import discord
from discord import app_commands
from discord.ext import commands

from core import mod_log, titles
from core.permissions import requires_permission

log = logging.getLogger(__name__)

_TITLE_PREFIX_RE = re.compile(r"^「[^」]{1,24}」\s*")
_NICK_LIMIT = 32
_NICK_TITLE_LIMIT = 12


class _TitleSelect(discord.ui.Select):
    """보유 칭호 중 1개 장착(ephemeral — 호출자만 조작)."""

    def __init__(self, cog: "CommunityLevelCog", owned: list) -> None:
        self.cog = cog
        options = [
            discord.SelectOption(
                label=t["display_name"][:100], value=str(t["id"]), default=bool(t["equipped"])
            )
            for t in owned
        ]
        super().__init__(placeholder="장착할 칭호를 선택하세요", min_values=1, max_values=1, options=options)

    async def callback(self, interaction: discord.Interaction) -> None:
        ok = await titles.equip(self.cog.db, interaction.user.id, int(self.values[0]))
        nick_msg = ""
        if ok and isinstance(interaction.user, discord.Member):
            nick_msg = await self.cog.sync_title_nickname(interaction.user)
        await interaction.response.edit_message(
            content=(
                f"✅ 칭호를 장착했습니다.{nick_msg}"
                if ok else "보유하지 않은 칭호입니다."
            ),
            view=None,
        )


class CommunityLevelCog(commands.Cog):
    def __init__(self, bot: commands.Bot) -> None:
        self.bot = bot

    @property
    def db(self):
        return self.bot.db  # type: ignore[attr-defined]

    async def sync_title_nickname(self, member: discord.Member) -> str:
        """장착 칭호를 서버 닉네임 prefix로 반영한다."""
        if member.guild.owner_id == member.id:
            return " 다만 서버 소유자는 Discord 제한으로 봇이 닉네임을 바꿀 수 없습니다."
        if member.guild.me is not None and member.top_role >= member.guild.me.top_role:
            return " 다만 대상 역할이 봇보다 높거나 같아서 닉네임 반영은 실패했습니다."

        equipped = await titles.equipped_title(self.db, member.id)
        base = _TITLE_PREFIX_RE.sub("", member.nick or member.display_name).strip()
        if not base:
            base = member.name

        if equipped:
            title = " ".join(str(equipped).split())[:_NICK_TITLE_LIMIT]
            prefix = f"「{title}」 "
            target = f"{prefix}{base[:max(1, _NICK_LIMIT - len(prefix))]}"
        else:
            target = base[:_NICK_LIMIT]

        target_nick = None if not equipped and target == member.name else target
        if member.nick == target_nick:
            return ""

        try:
            await member.edit(nick=target_nick, reason="칭호 닉네임 동기화")
        except discord.Forbidden:
            log.warning("칭호 닉네임 반영 권한 부족: user=%s", member.id)
            return " 다만 봇 권한/역할 위계 때문에 닉네임 반영은 실패했습니다."
        except discord.HTTPException:
            log.warning("칭호 닉네임 반영 실패: user=%s", member.id, exc_info=True)
            return " 다만 디스코드 오류로 닉네임 반영은 실패했습니다."
        return " 닉네임에도 반영했습니다."

    # ─── 명령어 ───────────────────────────────────────────────────
    @app_commands.command(name="칭호", description="보유 칭호를 확인하고 1개를 장착합니다.")
    async def title_cmd(self, interaction: discord.Interaction) -> None:
        owned = await titles.owned_titles(self.db, interaction.user.id)
        if not owned:
            await interaction.response.send_message(
                "보유한 칭호가 없습니다. 칭호는 운영진이 이벤트/보상으로 부여합니다.",
                ephemeral=True,
            )
            return
        view = discord.ui.View(timeout=60)
        view.add_item(_TitleSelect(self, owned[:25]))
        await interaction.response.send_message(
            "장착할 칭호를 선택하세요:", view=view, ephemeral=True
        )

    @app_commands.command(name="칭호목록", description="부여 가능한 칭호 목록을 확인합니다(운영).")
    @requires_permission("admin")
    async def title_list(self, interaction: discord.Interaction) -> None:
        rows = await titles.list_catalog(self.db)
        if not rows:
            await interaction.response.send_message("등록된 칭호가 없습니다.", ephemeral=True)
            return
        lines = [
            f"`#{row['id']}` {row['display_name']}"
            for row in rows[:50]
        ]
        if len(rows) > 50:
            lines.append(f"... 외 {len(rows) - 50}개")
        await interaction.response.send_message("\n".join(lines), ephemeral=True)

    @app_commands.command(name="칭호생성", description="운영자 부여용 칭호를 생성합니다(운영).")
    @app_commands.describe(이름="칭호 표시명")
    @requires_permission("admin")
    async def title_create(self, interaction: discord.Interaction, 이름: str) -> None:
        display_name = 이름.strip()
        if not display_name or len(display_name) > 80:
            await interaction.response.send_message(
                "칭호 이름은 1~80자여야 합니다.", ephemeral=True
            )
            return
        title_id = await titles.create_title(self.db, display_name)
        await mod_log.record(
            self.bot,
            action="title_create",
            operator_id=interaction.user.id,
            detail={"title_id": title_id, "display_name": display_name},
        )
        await interaction.response.send_message(
            f"✅ 칭호 생성: `#{title_id}` **{display_name}**", ephemeral=True
        )

    @app_commands.command(name="칭호부여", description="유저에게 칭호를 부여합니다(운영).")
    @app_commands.describe(유저="대상", 칭호="칭호 번호")
    @requires_permission("admin")
    async def title_grant(
        self, interaction: discord.Interaction, 유저: discord.Member, 칭호: int
    ) -> None:
        row = await titles.get_title(self.db, 칭호)
        if row is None:
            await interaction.response.send_message(
                f"칭호 `#{칭호}` 를 찾을 수 없습니다.", ephemeral=True
            )
            return
        await titles.grant_title(self.db, 유저.id, 칭호)
        await titles.equip(self.db, 유저.id, 칭호)
        nick_msg = await self.sync_title_nickname(유저)
        await mod_log.record(
            self.bot,
            action="title_grant",
            operator_id=interaction.user.id,
            target_id=유저.id,
            detail={"title_id": 칭호, "display_name": row["display_name"]},
        )
        await interaction.response.send_message(
            f"✅ {유저.mention} 에게 칭호 **{row['display_name']}** 부여.{nick_msg}",
            ephemeral=True, allowed_mentions=discord.AllowedMentions.none(),
        )

    @app_commands.command(name="칭호회수", description="유저에게서 칭호를 회수합니다(운영).")
    @app_commands.describe(유저="대상", 칭호="칭호 번호")
    @requires_permission("admin")
    async def title_revoke(
        self, interaction: discord.Interaction, 유저: discord.Member, 칭호: int
    ) -> None:
        row = await titles.get_title(self.db, 칭호)
        if row is None:
            await interaction.response.send_message(
                f"칭호 `#{칭호}` 를 찾을 수 없습니다.", ephemeral=True
            )
            return
        equipped_before = await titles.equipped_title(self.db, 유저.id)
        ok = await titles.revoke_title(self.db, 유저.id, 칭호)
        if not ok:
            await interaction.response.send_message(
                f"{유저.mention} 님은 칭호 **{row['display_name']}** 을 보유하고 있지 않습니다.",
                ephemeral=True, allowed_mentions=discord.AllowedMentions.none(),
            )
            return
        nick_msg = ""
        if equipped_before == row["display_name"]:
            nick_msg = await self.sync_title_nickname(유저)
        await mod_log.record(
            self.bot,
            action="title_revoke",
            operator_id=interaction.user.id,
            target_id=유저.id,
            detail={"title_id": 칭호, "display_name": row["display_name"]},
        )
        await interaction.response.send_message(
            f"↩ {유저.mention} 의 칭호 **{row['display_name']}** 회수.{nick_msg}",
            ephemeral=True, allowed_mentions=discord.AllowedMentions.none(),
        )

    @title_grant.autocomplete("칭호")
    @title_revoke.autocomplete("칭호")
    async def _title_autocomplete(
        self, interaction: discord.Interaction, current: str
    ) -> list[app_commands.Choice[int]]:
        rows = await titles.list_catalog(self.db)
        choices: list[app_commands.Choice[int]] = []
        for row in rows:
            name = f"#{row['id']} {row['display_name']}"[:100]
            if current and current not in name and current != str(row["id"]):
                continue
            choices.append(app_commands.Choice(name=name, value=row["id"]))
            if len(choices) >= 25:
                break
        return choices


async def setup(bot: commands.Bot) -> None:
    await bot.add_cog(CommunityLevelCog(bot))
