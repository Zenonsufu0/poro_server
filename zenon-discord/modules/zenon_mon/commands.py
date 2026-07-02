"""
Zenon Mon 명령어 모듈.

디스코드 제재와 마크 서버 제재는 분리한다.
이 모듈의 `마크*` 명령은 ZenonMonCore 운영 API에 요청하고, 성공한 결과만
마크 제재 기록(`poromon.minecraft_sanction`)으로 `로그/제재내역`에 게시한다.
"""
from __future__ import annotations

import logging

import discord
from discord import app_commands
from discord.ext import commands

from core import mod_log, notifier
from core.permissions import requires_permission
from integrations.zenon_mon_api import ZenonMonAdminError, ZenonMonApiClient

log = logging.getLogger(__name__)

_MC_ACTION_LABELS = {
    "warn": "마크 경고",
    "kick": "마크 킥",
    "ban": "마크 밴",
    "unban": "마크 밴해제",
}


class MinecraftSanctionPanelView(discord.ui.View):
    """마크 제재 패널(영구 뷰)."""

    def __init__(self, cog: "ZenonMonCog") -> None:
        super().__init__(timeout=None)
        self.cog = cog
        for label, custom_id, action, style in (
            ("마크 제재 조회", "mc_sanction_lookup", "lookup", discord.ButtonStyle.secondary),
            ("마크 경고", "mc_sanction_warn", "warn", discord.ButtonStyle.secondary),
            ("마크 킥", "mc_sanction_kick", "kick", discord.ButtonStyle.danger),
            ("마크 밴", "mc_sanction_ban", "ban", discord.ButtonStyle.danger),
            ("마크 밴해제", "mc_sanction_unban", "unban", discord.ButtonStyle.secondary),
        ):
            button = discord.ui.Button(label=label, custom_id=custom_id, style=style)
            button.callback = self._callback_for(action)
            self.add_item(button)

    def _callback_for(self, action: str):
        async def _callback(interaction: discord.Interaction) -> None:
            if action == "lookup":
                await interaction.response.send_modal(MinecraftSanctionLookupModal(self.cog))
            else:
                await interaction.response.send_modal(MinecraftSanctionModal(self.cog, action))

        return _callback


class MinecraftSanctionLookupModal(discord.ui.Modal, title="마크 제재 조회"):
    target = discord.ui.TextInput(
        label="마크 닉네임 또는 UUID",
        min_length=1,
        max_length=80,
    )

    def __init__(self, cog: "ZenonMonCog") -> None:
        super().__init__()
        self.cog = cog

    async def on_submit(self, interaction: discord.Interaction) -> None:
        await self.cog.lookup_minecraft_sanctions(interaction, str(self.target.value))


class MinecraftSanctionModal(discord.ui.Modal):
    target = discord.ui.TextInput(
        label="마크 닉네임 또는 UUID",
        min_length=1,
        max_length=80,
    )
    reason = discord.ui.TextInput(
        label="사유",
        style=discord.TextStyle.paragraph,
        required=False,
        max_length=1000,
    )

    def __init__(self, cog: "ZenonMonCog", action: str) -> None:
        super().__init__(title=_MC_ACTION_LABELS.get(action, action))
        self.cog = cog
        self.action = action

    async def on_submit(self, interaction: discord.Interaction) -> None:
        await self.cog.apply_minecraft_sanction(
            interaction,
            self.action,
            str(self.target.value),
            str(self.reason.value or ""),
        )


class ZenonMonCog(commands.Cog):
    def __init__(self, bot: commands.Bot) -> None:
        self.bot = bot
        self.api = ZenonMonApiClient()

    async def cog_unload(self) -> None:
        await self.api.close()

    @commands.Cog.listener()
    async def on_ready(self) -> None:
        self.bot.add_view(MinecraftSanctionPanelView(self))

    @property
    def db(self):
        return self.bot.db  # type: ignore[attr-defined]

    @app_commands.command(name="마크제재패널", description="제재내역 채널에 마크 제재 패널을 게시합니다.")
    @requires_permission("admin", "zenon_mon_manager")
    async def minecraft_sanction_panel(self, interaction: discord.Interaction) -> None:
        guild = interaction.guild
        if guild is None:
            await interaction.response.send_message("길드에서만 사용할 수 있습니다.", ephemeral=True)
            return
        channel = await self._active_sanction_channel(guild)
        if channel is None:
            await interaction.response.send_message(
                "활성 서버의 로그/제재내역 채널을 찾지 못했습니다.", ephemeral=True
            )
            return
        embed = self._panel_embed()
        await channel.send(embed=embed, view=MinecraftSanctionPanelView(self))
        await interaction.response.send_message(
            f"마크 제재 패널을 게시했습니다: {channel.mention}", ephemeral=True
        )

    @app_commands.command(name="마크경고", description="마크 서버 유저에게 경고를 부여합니다.")
    @app_commands.describe(대상="마크 닉네임 또는 UUID", 사유="경고 사유")
    @requires_permission("admin", "zenon_mon_manager")
    async def minecraft_warn(self, interaction: discord.Interaction, 대상: str, 사유: str) -> None:
        await self.apply_minecraft_sanction(interaction, "warn", 대상, 사유)

    @app_commands.command(name="마크킥", description="마크 서버 유저를 킥합니다.")
    @app_commands.describe(대상="마크 닉네임 또는 UUID", 사유="킥 사유")
    @requires_permission("admin", "zenon_mon_manager")
    async def minecraft_kick(self, interaction: discord.Interaction, 대상: str, 사유: str) -> None:
        await self.apply_minecraft_sanction(interaction, "kick", 대상, 사유)

    @app_commands.command(name="마크밴", description="마크 서버 유저를 밴합니다.")
    @app_commands.describe(대상="마크 닉네임 또는 UUID", 사유="밴 사유")
    @requires_permission("admin", "zenon_mon_manager")
    async def minecraft_ban(self, interaction: discord.Interaction, 대상: str, 사유: str) -> None:
        await self.apply_minecraft_sanction(interaction, "ban", 대상, 사유)

    @app_commands.command(name="마크밴해제", description="마크 서버 유저의 밴을 해제합니다.")
    @app_commands.describe(대상="마크 닉네임 또는 UUID", 사유="해제 사유")
    @requires_permission("admin", "zenon_mon_manager")
    async def minecraft_unban(
        self, interaction: discord.Interaction, 대상: str, 사유: str = ""
    ) -> None:
        await self.apply_minecraft_sanction(interaction, "unban", 대상, 사유)

    @app_commands.command(name="마크제재조회", description="마크 서버 제재 이력을 조회합니다.")
    @app_commands.describe(대상="마크 닉네임 또는 UUID")
    @requires_permission("admin", "zenon_mon_manager", "support")
    async def minecraft_sanction_lookup(
        self, interaction: discord.Interaction, 대상: str
    ) -> None:
        await self.lookup_minecraft_sanctions(interaction, 대상)

    async def apply_minecraft_sanction(
        self, interaction: discord.Interaction, action: str, target: str, reason: str
    ) -> None:
        target = target.strip()
        reason = reason.strip()
        if not target:
            await interaction.response.send_message("대상을 입력해주세요.", ephemeral=True)
            return

        await interaction.response.defer(ephemeral=True)
        try:
            result = await self.api.minecraft_sanction(
                action=action,
                target=target,
                reason=reason,
                operator_discord_id=interaction.user.id,
            )
        except ZenonMonAdminError as exc:
            await interaction.followup.send(
                f"Zenon Mon 운영 API 호출 실패: {exc}", ephemeral=True
            )
            return

        if not result.get("ok"):
            await interaction.followup.send(
                f"마크 제재 실패: {self._reason_message(result.get('reason'))}", ephemeral=True
            )
            return

        await self._record_success(interaction, action, target, reason, result)
        label = _MC_ACTION_LABELS.get(action, action)
        await interaction.followup.send(f"{label} 완료: `{target}`", ephemeral=True)

    async def lookup_minecraft_sanctions(
        self, interaction: discord.Interaction, target: str
    ) -> None:
        target = target.strip()
        if not target:
            await interaction.response.send_message("대상을 입력해주세요.", ephemeral=True)
            return
        await interaction.response.defer(ephemeral=True)
        try:
            result = await self.api.list_minecraft_sanctions(target)
        except ZenonMonAdminError as exc:
            await interaction.followup.send(
                f"Zenon Mon 운영 API 호출 실패: {exc}", ephemeral=True
            )
            return
        if not result.get("ok"):
            await interaction.followup.send(
                f"마크 제재 조회 실패: {self._reason_message(result.get('reason'))}",
                ephemeral=True,
            )
            return

        sanctions = result.get("sanctions") or result.get("items") or []
        lines = []
        if isinstance(sanctions, list):
            for item in sanctions[:10]:
                if isinstance(item, dict):
                    action = item.get("action", "sanction")
                    reason = item.get("reason") or "(사유 없음)"
                    created = item.get("createdAt") or item.get("created_at") or ""
                    lines.append(f"`{action}` {created} - {str(reason)[:80]}")
        embed = discord.Embed(
            title=f"마크 제재 조회 - {target}",
            description="\n".join(lines)[:4000] if lines else "기록 없음",
            color=discord.Color.orange(),
        )
        await interaction.followup.send(embed=embed, ephemeral=True)

    async def _record_success(
        self,
        interaction: discord.Interaction,
        action: str,
        target: str,
        reason: str,
        result: dict,
    ) -> None:
        await mod_log.record(
            self.bot,
            action=f"mc_{action}",
            operator_id=interaction.user.id,
            reason=reason or None,
            detail={
                "target": target,
                "sanction_id": result.get("id") or result.get("sanctionId"),
                "source": "discord",
            },
        )
        await notifier.dispatch(
            self.bot,
            "poromon",
            "minecraft_sanction",
            {
                "action": action,
                "target": target,
                "player": result.get("player") or result.get("name"),
                "uuid": result.get("uuid"),
                "operator": f"<@{interaction.user.id}>",
                "reason": reason,
                "source": "discord",
                "sanction_id": result.get("id") or result.get("sanctionId"),
            },
        )

    @staticmethod
    def _reason_message(reason: object) -> str:
        return {
            "not_found": "대상을 찾지 못했습니다.",
            "conflict": "현재 상태와 충돌합니다.",
            "not_implemented": "ZenonMonCore 운영 API가 아직 구현되지 않았습니다.",
        }.get(str(reason), str(reason or "알 수 없는 오류"))

    @staticmethod
    def _panel_embed() -> discord.Embed:
        embed = discord.Embed(
            title="마크 제재",
            description=(
                "마크 서버 제재는 디스코드 제재와 분리됩니다. "
                "요청은 ZenonMonCore 운영 API 성공 후에만 기록됩니다."
            ),
            color=discord.Color.orange(),
        )
        embed.add_field(name="조회", value="마크 닉네임 또는 UUID 기준", inline=True)
        embed.add_field(name="조치", value="경고 · 킥 · 밴 · 밴해제", inline=True)
        return embed

    async def _active_sanction_channel(
        self, guild: discord.Guild
    ) -> discord.TextChannel | None:
        from core import servers

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
            if channel.name == "제재내역":
                return channel
        return None


async def setup(bot: commands.Bot) -> None:
    await bot.add_cog(ZenonMonCog(bot))
