"""
공통 명령어 모듈 — 특정 게임(RPG/Zenon Mon)에 종속되지 않는 봇 전역 기능.

- `/핑`   : 헬스체크(응답 지연).
- `/도움말`: 사용 가능한 명령어 안내. 등록된 app command 트리를 순회해 도메인별로
            그룹핑하고, 운영 권한 명령(`requires_permission`)은 호출자가 해당 권한을
            보유한 경우에만 노출한다(공통 명령은 항상 표시). 명령 이름/설명은 실제
            트리에서 읽으므로 별도 카탈로그 없이 자동 동기화된다.
"""
from __future__ import annotations

import discord
from discord import app_commands
from discord.ext import commands

from core import permissions

# 모듈 경로 최상위 세그먼트(modules.<domain>.*) → 표시 그룹명
_DOMAIN_LABELS: dict[str, str] = {
    "common": "공통",
    "roles": "역할",
    "community": "커뮤니티",
    "support": "지원",
    "onboarding": "온보딩",
    "notify": "알림",
    "server_lifecycle": "서버 운영",
    "moderation": "모더레이션",
    "zenon_mon": "Zenon Mon",
    "event": "이벤트",
    "admin": "관리자",
    "rpg": "RPG",
}

# 임베드 필드 표시 순서(위 라벨 + 미분류 "기타")
_GROUP_ORDER: tuple[str, ...] = (
    "공통", "역할", "커뮤니티", "지원", "온보딩", "알림",
    "서버 운영", "모더레이션", "Zenon Mon", "이벤트", "관리자", "RPG", "기타",
)

_FIELD_VALUE_LIMIT = 1024  # discord 임베드 필드 value 최대 길이


def _group_for(command: app_commands.Command) -> str:
    module = getattr(command, "module", "") or ""
    parts = module.split(".")
    if len(parts) >= 2 and parts[0] == "modules":
        return _DOMAIN_LABELS.get(parts[1], "기타")
    return "기타"


def _perm_keys_for(command: app_commands.Command) -> tuple[str, ...] | None:
    """명령이 요구하는 권한 키(없으면 None = 공통 명령)."""
    callback = getattr(command, "callback", None)
    return getattr(callback, "_perm_keys", None)


def _chunk_lines(lines: list[str], limit: int = _FIELD_VALUE_LIMIT) -> list[str]:
    """줄 목록을 필드 value 길이 제한 이하 청크로 나눈다."""
    chunks: list[str] = []
    buf: list[str] = []
    length = 0
    for line in lines:
        add = len(line) + (1 if buf else 0)
        if length + add > limit and buf:
            chunks.append("\n".join(buf))
            buf, length = [], 0
            add = len(line)
        buf.append(line)
        length += add
    if buf:
        chunks.append("\n".join(buf))
    return chunks


class CommonCog(commands.Cog):
    def __init__(self, bot: commands.Bot) -> None:
        self.bot = bot

    @app_commands.command(name="핑", description="봇 응답 지연(latency)을 확인합니다.")
    async def ping(self, interaction: discord.Interaction) -> None:
        latency_ms = round(self.bot.latency * 1000)
        await interaction.response.send_message(
            f"🏓 퐁! 지연: **{latency_ms}ms**", ephemeral=True
        )

    @app_commands.command(name="도움말", description="사용 가능한 명령어를 안내합니다.")
    async def help_command(self, interaction: discord.Interaction) -> None:
        member = (
            interaction.guild.get_member(interaction.user.id)
            if interaction.guild
            else None
        )

        # 도메인 그룹 → [(명령경로, 설명)]
        groups: dict[str, list[tuple[str, str]]] = {}
        for cmd in self.bot.tree.walk_commands():
            if not isinstance(cmd, app_commands.Command):
                continue  # 그룹(app_commands.Group) 자체는 건너뜀
            keys = _perm_keys_for(cmd)
            if keys:  # 운영 권한 명령 — 보유자에게만 노출
                if member is None or not permissions.member_has_permission(member, *keys):
                    continue
            group = _group_for(cmd)
            groups.setdefault(group, []).append(
                (cmd.qualified_name, cmd.description or "")
            )

        embed = discord.Embed(
            title="📖 YUKI-01 명령어 안내",
            description="사용할 수 있는 명령어입니다. (보유 권한에 따라 표시가 달라집니다)",
            color=discord.Color.from_rgb(137, 207, 240),  # 하늘색 테마
        )
        for group in _GROUP_ORDER:
            cmds = groups.get(group)
            if not cmds:
                continue
            lines = [f"`/{name}` — {desc}" for name, desc in sorted(cmds)]
            for i, chunk in enumerate(_chunk_lines(lines)):
                title = f"— {group} —" if i == 0 else f"— {group} (계속) —"
                embed.add_field(name=title, value=chunk, inline=False)

        if not embed.fields:
            embed.description = "표시할 수 있는 명령어가 없습니다."

        await interaction.response.send_message(embed=embed, ephemeral=True)


async def setup(bot: commands.Bot) -> None:
    await bot.add_cog(CommonCog(bot))
