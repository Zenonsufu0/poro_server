"""
출석 — T14 community_level.md §9.

`/출석` → 하루 1회(KST) 체크 → 연속 streak·누적 total 갱신.
커뮤니티 레벨/XP는 비활성 상태라 XP 보상은 지급하지 않는다.
"""
from __future__ import annotations

import logging

import discord
from discord import app_commands
from discord.ext import commands

from core import attendance

log = logging.getLogger(__name__)


class AttendanceCog(commands.Cog):
    def __init__(self, bot: commands.Bot) -> None:
        self.bot = bot

    @property
    def db(self):
        return self.bot.db  # type: ignore[attr-defined]

    @app_commands.command(name="출석", description="하루 1회 출석합니다.")
    async def attend(self, interaction: discord.Interaction) -> None:
        result = await attendance.check_in(self.db, interaction.user.id)

        if result["already_checked"]:
            await interaction.response.send_message(
                f"오늘은 이미 출석했습니다. (연속 {result['streak']}일 · 누적 {result['total']}일)",
                ephemeral=True,
            )
            return

        streak, total = result["streak"], result["total"]
        embed = discord.Embed(
            title="📅 출석 완료!",
            description=f"{interaction.user.mention} 님, 출석이 기록되었습니다.",
            color=discord.Color.green(),
        )
        embed.add_field(name="연속 출석", value=f"🔥 {streak}일", inline=True)
        embed.add_field(name="누적 출석", value=f"{total}일", inline=True)
        await interaction.response.send_message(embed=embed)


async def setup(bot: commands.Bot) -> None:
    await bot.add_cog(AttendanceCog(bot))
