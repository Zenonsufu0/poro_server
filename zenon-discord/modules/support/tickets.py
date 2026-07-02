"""
티켓 (1:1 문의) — T16 support.md §2.

`/문의` → 비공개 채널 생성(개설자 + Support·admin 가시) → tickets INSERT(open).
채널은 "문의" 카테고리에 생성. 채널 내 운영진과 대화(개설자는 열린 동안 채팅 가능) →
`/티켓종료` 또는 [티켓 종료] 버튼 → 잠금·아카이브.

권한: 개설 = 공통(유저, 동시 1개 제한). 종료 = 개설자 또는 admin·support.
모든 개설/종료 = mod_log 기록. 종료 = 삭제 대신 [종료] 프리픽스 + **개설자 읽기전용 전환**
(view+history, send 차단) + **"문의 보관" 카테고리로 이동**(운영자 전체 열람, 기록 보존).
"""
from __future__ import annotations

import logging

import discord
from discord import app_commands
from discord.ext import commands

from core import config, mod_log, permissions, servers, tickets
from core.permissions import requires_permission

log = logging.getLogger(__name__)

# 티켓을 묶을 카테고리 이름(자동 탐색/생성용). env CATEGORY_티켓_ID 가 있으면 그게 우선.
_TICKET_CATEGORY_NAME = "문의"
# 종료된 티켓을 보관할 아카이브 카테고리 이름.
_ARCHIVE_CATEGORY_NAME = "문의 보관"


class TicketCloseView(discord.ui.View):
    """티켓 채널 [종료] 버튼 (영구 뷰 — custom_id 고정)."""

    def __init__(self, cog: "TicketCog") -> None:
        super().__init__(timeout=None)
        self.cog = cog
        btn = discord.ui.Button(
            label="티켓 종료", style=discord.ButtonStyle.red, custom_id="ticket_close"
        )
        btn.callback = self._on_click
        self.add_item(btn)

    async def _on_click(self, interaction: discord.Interaction) -> None:
        await self.cog.close_here(interaction)


class SupportPanelView(discord.ui.View):
    """지원 채널 고정 패널(문의·FAQ·버그제보 안내)."""

    def __init__(self, cog: "TicketCog") -> None:
        super().__init__(timeout=None)
        self.cog = cog

        ticket = discord.ui.Button(
            label="문의하기",
            style=discord.ButtonStyle.blurple,
            emoji="📨",
            custom_id="support_panel_ticket",
        )
        ticket.callback = self._on_ticket
        self.add_item(ticket)

        faq = discord.ui.Button(
            label="FAQ 보기",
            style=discord.ButtonStyle.secondary,
            emoji="❓",
            custom_id="support_panel_faq",
        )
        faq.callback = self._on_faq
        self.add_item(faq)

        bug = discord.ui.Button(
            label="버그제보 안내",
            style=discord.ButtonStyle.secondary,
            emoji="🐞",
            custom_id="support_panel_bug",
        )
        bug.callback = self._on_bug
        self.add_item(bug)

    async def _on_ticket(self, interaction: discord.Interaction) -> None:
        await self.cog.open_ticket_for(interaction)

    async def _on_faq(self, interaction: discord.Interaction) -> None:
        faq_cog = self.cog.bot.get_cog("FaqCog")
        if faq_cog is None:
            await interaction.response.send_message(
                "FAQ 기능을 사용할 수 없습니다. `/문의`를 이용해주세요.",
                ephemeral=True,
            )
            return
        await faq_cog.send_faq_panel(interaction)  # type: ignore[attr-defined]

    async def _on_bug(self, interaction: discord.Interaction) -> None:
        if not config.CHANNEL_BUGREPORT_ID:
            await interaction.response.send_message(
                "버그제보 채널이 아직 설정되지 않았습니다. 지금은 `/문의`로 접수해주세요.",
                ephemeral=True,
            )
            return
        await interaction.response.send_message(
            "버그는 `/버그제보 대상 심각도` 명령으로 접수해주세요.",
            ephemeral=True,
        )


class TicketCog(commands.Cog):
    def __init__(self, bot: commands.Bot) -> None:
        self.bot = bot

    @property
    def db(self):
        return self.bot.db  # type: ignore[attr-defined]

    @commands.Cog.listener()
    async def on_ready(self) -> None:
        self.bot.add_view(TicketCloseView(self))  # 재시작 후 종료 버튼 동작
        self.bot.add_view(SupportPanelView(self))  # 재시작 후 지원 패널 버튼 동작

    def _staff_overwrites(self, guild: discord.Guild, opener: discord.Member) -> dict:
        """티켓 채널 권한: @everyone 숨김 + 개설자·운영(admin/support)·봇 가시."""
        allow = discord.PermissionOverwrite(view_channel=True, send_messages=True)
        ow: dict = {
            guild.default_role: discord.PermissionOverwrite(view_channel=False),
            opener: allow,
        }
        if guild.me is not None:
            ow[guild.me] = allow
        for key in ("admin", "support"):
            role = guild.get_role(config.PERMISSION_ROLE_IDS.get(key, 0))
            if role is not None:
                ow[role] = allow
        return ow

    def _staff_category_overwrites(self, guild: discord.Guild) -> dict:
        """티켓 카테고리(문의/문의 보관) 자동 생성 권한: @everyone 숨김 + 운영/봇 가시.

        개별 티켓 채널이 개설자에게만 공개되므로 카테고리 자체는 @everyone 숨김.
        """
        overwrites: dict = {
            guild.default_role: discord.PermissionOverwrite(view_channel=False)
        }
        if guild.me is not None:
            overwrites[guild.me] = discord.PermissionOverwrite(
                view_channel=True, manage_channels=True
            )
        for key in ("admin", "support"):
            role = guild.get_role(config.PERMISSION_ROLE_IDS.get(key, 0))
            if role is not None:
                overwrites[role] = discord.PermissionOverwrite(
                    view_channel=True, send_messages=True
                )
        return overwrites

    async def _resolve_ticket_category(
        self, guild: discord.Guild, *, group_key: str, name: str, env_id: int = 0
    ) -> discord.CategoryChannel | None:
        """티켓 카테고리를 찾거나 만든다.

        우선순위: ① 활성 시즌 템플릿 카테고리(server_categories group_key, `/서버신설`
        자동전개) → ② env_id(있으면) → ③ 이름 일치 카테고리 → ④ 없으면 자동 생성
        (운영자 전용 가시). 실패 시 None(폴백=카테고리 없음).
        """
        active = await servers.get_any_active(self.db)
        if active is not None:
            for row in await servers.get_categories(self.db, active["id"]):
                if row["group_key"] == group_key:
                    cat = guild.get_channel(row["category_id"])
                    if isinstance(cat, discord.CategoryChannel):
                        return cat
                    break
        if env_id:
            cat = guild.get_channel(env_id)
            if isinstance(cat, discord.CategoryChannel):
                return cat
        for cat in guild.categories:
            if cat.name == name:
                return cat
        try:
            return await guild.create_category(
                name, overwrites=self._staff_category_overwrites(guild),
                reason=f"{name} 카테고리 자동 생성",
            )
        except discord.Forbidden:
            log.warning("%s 카테고리 자동 생성 권한 부족(Manage Channels)", name)
            return None

    async def _ticket_category(
        self, guild: discord.Guild
    ) -> discord.CategoryChannel | None:
        """열린 티켓을 묶을 카테고리(문의)."""
        return await self._resolve_ticket_category(
            guild, group_key="tickets", name=_TICKET_CATEGORY_NAME,
            env_id=config.CATEGORY_티켓_ID,
        )

    async def _ticket_archive_category(
        self, guild: discord.Guild
    ) -> discord.CategoryChannel | None:
        """종료된 티켓을 보관할 카테고리(문의 보관)."""
        return await self._resolve_ticket_category(
            guild, group_key="tickets_closed", name=_ARCHIVE_CATEGORY_NAME,
        )

    async def _active_support_channel(
        self, guild: discord.Guild
    ) -> discord.TextChannel | None:
        active = await servers.get_any_active(self.db)
        if active is None:
            return None

        category_id = None
        for row in await servers.get_categories(self.db, active["id"]):
            if row["group_key"] == "support":
                category_id = row["category_id"]
                break
        if not category_id:
            return None

        category = guild.get_channel(category_id)
        if not isinstance(category, discord.CategoryChannel):
            return None

        for channel in category.text_channels:
            if channel.name == "건의-문의-버그제보":
                return channel
        return None

    @app_commands.command(name="지원패널", description="지원 채널에 문의/FAQ/버그제보 안내 패널을 게시합니다.")
    @requires_permission("admin", "support")
    async def support_panel(self, interaction: discord.Interaction) -> None:
        guild = interaction.guild
        if guild is None:
            await interaction.response.send_message("길드에서만 사용할 수 있습니다.", ephemeral=True)
            return

        channel = await self._active_support_channel(guild)
        if channel is None:
            await interaction.response.send_message(
                "활성 서버의 지원 채널(건의-문의-버그제보)을 찾지 못했습니다.",
                ephemeral=True,
            )
            return

        embed = discord.Embed(
            title="지원 센터",
            description=(
                "문의는 비공개 티켓으로 열리고, FAQ는 본인에게만 표시됩니다.\n"
                "버그제보는 대상과 심각도를 선택해 별도 양식으로 접수합니다."
            ),
            color=discord.Color.blurple(),
        )
        embed.add_field(name="문의", value="운영진과 1:1 비공개 채널을 엽니다.", inline=False)
        embed.add_field(name="FAQ", value="등록된 자주 묻는 질문을 확인합니다.", inline=False)
        embed.add_field(
            name="버그제보",
            value="`/버그제보 대상 심각도` 명령으로 접수합니다.",
            inline=False,
        )

        try:
            await channel.send(embed=embed, view=SupportPanelView(self))
        except discord.Forbidden:
            await interaction.response.send_message(
                "봇 권한 부족으로 지원 채널에 패널을 게시할 수 없습니다.",
                ephemeral=True,
            )
            return

        await interaction.response.send_message(
            f"지원 패널을 게시했습니다: {channel.mention}", ephemeral=True
        )

    @app_commands.command(name="문의", description="운영진과 1:1 비공개 문의 채널을 엽니다.")
    async def open_ticket(self, interaction: discord.Interaction) -> None:
        await self.open_ticket_for(interaction)

    async def open_ticket_for(self, interaction: discord.Interaction) -> None:
        """티켓 개설 본체(슬래시·FAQ 폴백 버튼 공용). 응답 미소비 상태 전제."""
        guild = interaction.guild
        member = interaction.user
        if guild is None or not isinstance(member, discord.Member):
            await interaction.response.send_message("길드에서만 사용할 수 있습니다.", ephemeral=True)
            return

        existing = await tickets.get_open_by_opener(self.db, member.id)
        if existing is not None:
            await interaction.response.send_message(
                f"이미 열린 문의가 있습니다: <#{existing['channel_id']}>. 먼저 종료해주세요.",
                ephemeral=True,
            )
            return

        await interaction.response.defer(ephemeral=True)
        category = await self._ticket_category(guild)
        try:
            channel = await guild.create_text_channel(
                f"문의-{member.name}"[:100],
                category=category,
                overwrites=self._staff_overwrites(guild, member),
                reason=f"티켓 개설 — {member}",
            )
        except discord.Forbidden:
            await interaction.followup.send(
                "봇 권한 부족(Manage Channels)으로 문의 채널을 만들 수 없습니다.", ephemeral=True
            )
            return

        tid = await tickets.create_ticket(self.db, channel.id, member.id)
        await mod_log.record(
            self.bot, action="ticket_open", operator_id=member.id,
            detail={"ticket_id": tid, "channel_id": channel.id},
        )
        embed = discord.Embed(
            title=f"문의 #{tid}",
            description=(
                f"{member.mention} 님의 문의 채널입니다. 운영진이 곧 확인합니다.\n"
                "문의 내용을 남겨주세요. 완료되면 아래 **티켓 종료** 버튼을 눌러주세요."
            ),
            color=discord.Color.blurple(),
        )
        await channel.send(embed=embed, view=TicketCloseView(self))
        await interaction.followup.send(f"문의 채널을 열었습니다: {channel.mention}", ephemeral=True)

    @app_commands.command(name="티켓종료", description="현재 문의 채널을 종료합니다.")
    async def close_ticket_cmd(self, interaction: discord.Interaction) -> None:
        await self.close_here(interaction)

    async def close_here(self, interaction: discord.Interaction) -> None:
        """현재 채널의 티켓을 종료(개설자 또는 운영). 버튼·명령 공용."""
        channel = interaction.channel
        guild = interaction.guild
        if guild is None or not isinstance(channel, discord.TextChannel):
            await interaction.response.send_message("문의 채널에서만 사용할 수 있습니다.", ephemeral=True)
            return
        row = await tickets.get_by_channel(self.db, channel.id)
        if row is None:
            await interaction.response.send_message("이 채널은 문의 채널이 아닙니다.", ephemeral=True)
            return
        if row["state"] == "closed":
            await interaction.response.send_message("이미 종료된 문의입니다.", ephemeral=True)
            return

        member = interaction.user
        is_staff = isinstance(member, discord.Member) and permissions.member_has_permission(
            member, "admin", "support"
        )
        if member.id != row["opener_id"] and not is_staff:
            await interaction.response.send_message(
                "본인 또는 운영진만 종료할 수 있습니다.", ephemeral=True
            )
            return

        await tickets.close_ticket(self.db, row["id"], member.id)
        await mod_log.record(
            self.bot, action="ticket_close", operator_id=member.id, target_id=row["opener_id"],
            detail={"ticket_id": row["id"], "channel_id": channel.id},
        )
        # 잠금·아카이브: 개설자 읽기전용 전환 + [종료] 프리픽스 + 보관 카테고리로 이동.
        try:
            opener = guild.get_member(row["opener_id"])
            if opener is not None:
                # 개설자는 종료 후에도 자기 문의 내역을 읽을 수 있으나 글은 못 씀.
                await channel.set_permissions(
                    opener,
                    overwrite=discord.PermissionOverwrite(
                        view_channel=True, read_message_history=True, send_messages=False
                    ),
                    reason="티켓 종료(개설자 읽기전용)",
                )
            archive = await self._ticket_archive_category(guild)
            new_name = channel.name if channel.name.startswith("[종료]") else f"[종료]-{channel.name}"[:100]
            edit_kwargs: dict = {"name": new_name, "reason": "티켓 종료 아카이브"}
            if archive is not None and channel.category_id != archive.id:
                edit_kwargs["category"] = archive
                edit_kwargs["sync_permissions"] = False  # 채널 자체 overwrite 보존
            await channel.edit(**edit_kwargs)
        except discord.HTTPException:
            log.warning("티켓 종료 채널 정리 실패: #%s", row["id"], exc_info=True)
        await interaction.response.send_message(
            f"✅ 문의 #{row['id']} 를 종료했습니다.", allowed_mentions=discord.AllowedMentions.none()
        )


async def setup(bot: commands.Bot) -> None:
    await bot.add_cog(TicketCog(bot))
