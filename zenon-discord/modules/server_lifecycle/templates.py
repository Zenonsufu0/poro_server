"""
서버 카테고리 템플릿 전개 (T17) — server_lifecycle.md §3 구현.

"서버 생성"은 **새 디스코드 길드가 아니라 기존 허브 길드 안에 게임/시즌 세트**
(온보딩 3역할 + 프리픽스 카테고리 그룹 + 채널들)를 자동 생성하는 것이다(task.md §11).

구조(2026-06-09 결정 — B안: 프리픽스 카테고리, 중첩 시즌/서버 미운영):
  디스코드는 카테고리 중첩 불가 → 한 게임/시즌 = `<표시명> · <그룹>` 카테고리 여러 개가
  사이드바에 나란히 붙어 그룹처럼 보임. domain당 active 1(중첩 미운영)이라 충돌 없음.

역할(온보딩 3역할 상태머신 — task.md §5):
  접근(access)  → 약관 채널만   → 약관 동의 시
  인증전(pending) → 인증 채널만   → 인증 완료 시
  플레이어(player) → 정보·커뮤니티·지원·음성·로그 일부 전체(약관·인증은 이제 안 보임)

전개 모델(§1·§3):
  - 신설(prep) = 카테고리·채널·역할 생성하되 **전부 비공개**(@everyone·3역할 모두 view=False).
  - /서버시작(active) = apply_visibility(visible=True): 약관→접근 / 인증→인증전 / 그 외→플레이어 공개.
  - /서버종료(ended) = apply_visibility(visible=False, archive=True): 전부 숨김 + `[종료]` 프리픽스.
  ※ 역할 부여(접근→인증전→플레이어 전이)는 온보딩 흐름(T7/T9, 후속)이 담당. 여기선 구조·권한만.

실패/중복 시 cleanup() 으로 생성분(역할·카테고리·채널)을 롤백.

온보딩 패널(약관동의 버튼·인증 모달) 게시 = `/온보딩패널`(panels.py)이 active 서버의 온보딩
카테고리 약관/인증 채널을 찾아 게시(DB 기반). verify 라우팅 = panels._verifiers(도메인별).
"""
from __future__ import annotations

import logging

import discord

from core import config

log = logging.getLogger(__name__)

_REASON = "T17 서버 템플릿 신설"

# 온보딩 3역할: (키, 역할명 접미). 키 = 가시성 대상 식별자.
_ROLE_SPEC: list[tuple[str, str]] = [
    ("access",  "접근"),
    ("pending", "인증전"),
    ("player",  "플레이어"),
]

# 카테고리 그룹: 각 원소 = 카테고리 1개.
#   key       = server_categories.group_key (레지스트리 키)
#   suffix    = 카테고리명 접미("<표시명> · <suffix>")
#   audience  = active 시 가시성 대상.
#               "player"=플레이어 공개(read_only=True 면 읽기 전용) / "onboarding"=
#               채널별(_ONBOARDING_AUDIENCE) / "logs"=제재내역은 플레이어 읽기전용,
#               경고는 운영자 전용 / "staff"=운영자 전용 카테고리(문의 티켓 컨테이너 —
#               개별 채널은 tickets.py 가 개설자에게만 공개)
#   read_only = (선택) audience="player" 인데 플레이어가 읽기만 가능(정보 카테고리).
#   channels  = [(이름, "text"|"voice")] — 생성 순서 = 배치 순서
_TEMPLATE_GROUPS: list[dict] = [
    {
        # 온보딩 카테고리("<표시명> · 임시") — 약관+인증. 인증 완료(플레이어) 시 사라짐.
        "key": "onboarding", "suffix": "임시", "audience": "onboarding",
        "channels": [("약관", "text"), ("인증", "text")],
    },
    {
        # 정보 = 운영자만 게시, 플레이어는 읽기 전용(공지·접속정보·가이드·FAQ).
        "key": "info", "suffix": "정보", "audience": "player", "read_only": True,
        # 접속정보 = 서버 IP·실시간 상태 게시 자리(T18 후속)
        "channels": [("공지", "text"), ("접속정보", "text"), ("가이드", "text"), ("FAQ", "text")],
    },
    {
        "key": "community", "suffix": "커뮤니티", "audience": "player",
        "channels": [("자유채팅", "text"), ("스크린샷", "text"), ("파티모집", "text")],
    },
    {
        "key": "support", "suffix": "지원·음성", "audience": "player",
        # 건의·일반문의·버그제보 통합(T16) / 임시음성 허브(T13, 카테고리별 다중)
        "channels": [("건의-문의-버그제보", "text"), ("➕ 음성방 만들기", "voice")],
    },
    {
        # 문의 = 열린 1:1 티켓 컨테이너. 채널은 tickets.py 가 /문의 시 동적 생성.
        # 운영자 전용 가시(개별 티켓은 개설자 overwrite 로 공개). 사전 채널 없음.
        "key": "tickets", "suffix": "문의", "audience": "staff",
        "channels": [],
    },
    {
        # 문의 보관 = 종료된 티켓 아카이브. 종료 시 tickets.py 가 채널을 여기로 이동
        # (개설자 읽기전용 유지). 운영자 전용 가시. 사전 채널 없음.
        "key": "tickets_closed", "suffix": "문의 보관", "audience": "staff",
        "channels": [],
    },
    {
        "key": "logs", "suffix": "로그", "audience": "logs",
        # 제재내역 = 플레이어 읽기전용 / 경고 = 운영자 전용 이상징후 알림 채널
        "channels": [("제재내역", "text"), ("경고", "text")],
    },
]

# 온보딩 카테고리의 채널별 가시성 대상(role key). 채널 이름으로 매칭.
_ONBOARDING_AUDIENCE: dict[str, str] = {"약관": "access", "인증": "pending"}


def _operator_roles(guild: discord.Guild) -> list[discord.Role]:
    """운영 권한 역할은 준비/비공개 상태에서도 카테고리를 볼 수 있어야 한다."""
    roles: list[discord.Role] = []
    for role_id in config.PERMISSION_ROLE_IDS.values():
        role = guild.get_role(role_id or 0)
        if role is not None and role not in roles:
            roles.append(role)
    return roles


def channel_count() -> int:
    return sum(len(g["channels"]) for g in _TEMPLATE_GROUPS)


def group_count() -> int:
    return len(_TEMPLATE_GROUPS)


# ─── 전개 ─────────────────────────────────────────────────────────────

async def provision(
    guild: discord.Guild, *, display_name: str
) -> tuple[dict[str, discord.Role], dict[str, discord.CategoryChannel]]:
    """온보딩 3역할 + 프리픽스 카테고리 그룹 + 채널을 prep(비공개) 상태로 생성.

    반환 = (roles{key→Role}, categories{group_key→CategoryChannel}).
    실패 시 생성분 롤백 후 예외(discord.Forbidden / discord.HTTPException) 재전파.
    """
    roles: dict[str, discord.Role] = {}
    created_roles: list[discord.Role] = []
    created_channels: list[discord.abc.GuildChannel] = []
    try:
        for key, suffix in _ROLE_SPEC:
            r = await guild.create_role(name=f"{display_name} {suffix}", reason=_REASON)
            roles[key] = r
            created_roles.append(r)

        # prep = 비공개: @everyone·3역할 모두 view=False. /서버시작에서 역할별 공개.
        base_overwrites: dict = {guild.default_role: discord.PermissionOverwrite(view_channel=False)}
        for r in created_roles:
            base_overwrites[r] = discord.PermissionOverwrite(view_channel=False)
        if guild.me is not None:
            base_overwrites[guild.me] = discord.PermissionOverwrite(
                view_channel=True,
                send_messages=True,
                connect=True,
                speak=True,
                move_members=True,
                manage_channels=True,
            )
        for r in _operator_roles(guild):
            base_overwrites[r] = discord.PermissionOverwrite(
                view_channel=True,
                send_messages=True,
                connect=True,
            )

        categories: dict[str, discord.CategoryChannel] = {}
        for g in _TEMPLATE_GROUPS:
            cat = await guild.create_category(
                f"{display_name} · {g['suffix']}", overwrites=dict(base_overwrites), reason=_REASON
            )
            categories[g["key"]] = cat
            created_channels.append(cat)
            for name, kind in g["channels"]:
                if kind == "voice":
                    ch = await guild.create_voice_channel(
                        name,
                        category=cat,
                        user_limit=1 if name == "➕ 음성방 만들기" else 0,
                        reason=_REASON,
                    )
                else:
                    ch = await guild.create_text_channel(name, category=cat, reason=_REASON)
                created_channels.append(ch)
    except discord.HTTPException:
        await _delete_all(created_channels)
        for r in created_roles:
            try:
                await r.delete(reason=f"{_REASON} 롤백")
            except discord.HTTPException:
                log.warning("역할 롤백 실패: %s", r.id, exc_info=True)
        raise
    return roles, categories


async def cleanup(
    roles: dict[str, discord.Role] | None,
    categories: dict[str, discord.CategoryChannel] | None,
) -> None:
    """레지스트리 기록 실패(중복 등) 시 생성 자산 정리(베스트에포트)."""
    if categories:
        for cat in categories.values():
            await _delete_all(list(cat.channels))
            try:
                await cat.delete(reason=f"{_REASON} 롤백")
            except discord.HTTPException:
                log.warning("카테고리 롤백 실패: %s", cat.id, exc_info=True)
    if roles:
        for r in roles.values():
            try:
                await r.delete(reason=f"{_REASON} 롤백")
            except discord.HTTPException:
                log.warning("역할 롤백 실패: %s", r.id, exc_info=True)


async def provision_missing(
    guild: discord.Guild,
    *,
    display_name: str,
    existing_group_keys: set[str],
    role_ids: dict[str, int],
) -> dict[str, discord.CategoryChannel]:
    """템플릿 그룹 중 아직 없는 카테고리만 prep(비공개)로 소급 생성. {group_key: cat} 반환.

    템플릿이 새 카테고리(예: 문의)를 추가했을 때 이미 준비/활성 중인 시즌에 반영하기
    위한 것. 기존 3역할(role_ids)·운영자·봇만 가시인 prep 권한으로 만든다. 가시성 공개는
    호출부가 이어서 `apply_visibility(visible=True)` 로 처리. 실패 시 생성분 롤백 후 재전파.
    """
    missing = [g for g in _TEMPLATE_GROUPS if g["key"] not in existing_group_keys]
    if not missing:
        return {}

    base_overwrites: dict = {guild.default_role: discord.PermissionOverwrite(view_channel=False)}
    for key in ("access", "pending", "player"):
        r = guild.get_role(role_ids.get(key, 0))
        if r is not None:
            base_overwrites[r] = discord.PermissionOverwrite(view_channel=False)
    if guild.me is not None:
        base_overwrites[guild.me] = discord.PermissionOverwrite(
            view_channel=True, send_messages=True, connect=True,
            speak=True, move_members=True, manage_channels=True,
        )
    for r in _operator_roles(guild):
        base_overwrites[r] = discord.PermissionOverwrite(
            view_channel=True, send_messages=True, connect=True,
        )

    created: dict[str, discord.CategoryChannel] = {}
    created_channels: list[discord.abc.GuildChannel] = []
    try:
        for g in missing:
            cat = await guild.create_category(
                f"{display_name} · {g['suffix']}", overwrites=dict(base_overwrites), reason=_REASON
            )
            created[g["key"]] = cat
            created_channels.append(cat)
            for name, kind in g["channels"]:
                if kind == "voice":
                    ch = await guild.create_voice_channel(
                        name,
                        category=cat,
                        user_limit=1 if name == "➕ 음성방 만들기" else 0,
                        reason=_REASON,
                    )
                else:
                    ch = await guild.create_text_channel(name, category=cat, reason=_REASON)
                created_channels.append(ch)
    except discord.HTTPException:
        await _delete_all(created_channels)
        raise
    return created


# ─── 가시성 전이 (시작/종료) ──────────────────────────────────────────

async def apply_visibility(
    guild: discord.Guild,
    *,
    category_ids: dict[str, int],
    role_ids: dict[str, int],
    visible: bool,
    archive: bool = False,
) -> str:
    """그룹별 카테고리 가시성을 역할 기준으로 토글. 사용자 안내용 짧은 문구 반환.

    category_ids: {group_key: category_id}, role_ids: {"access"/"pending"/"player": role_id}.
    visible=True(시작): 약관→접근 / 인증→인증전 / 그 외 카테고리→플레이어 공개.
    visible=False(종료/prep): 동일 대상 view=False. archive=True 면 카테고리에 [종료] 프리픽스.
    """
    touched = 0
    forbidden = False
    operator_roles = _operator_roles(guild)
    bot_overwrite = discord.PermissionOverwrite(
        view_channel=True,
        send_messages=True,
        connect=True,
        speak=True,
        move_members=True,
        manage_channels=True,
    )
    operator_overwrite = discord.PermissionOverwrite(
        view_channel=True,
        send_messages=True,
        connect=True,
        speak=True,
        move_members=True,
        manage_channels=True,
    )
    text_overwrite = discord.PermissionOverwrite(
        view_channel=visible,
        send_messages=visible,
        read_message_history=visible,
    )
    voice_overwrite = discord.PermissionOverwrite(
        view_channel=visible,
        connect=visible,
        speak=visible,
    )
    # 읽기 전용: 보고 히스토리는 읽되 글은 못 씀(정보·로그·제재내역·온보딩 안내).
    # send_messages 를 명시적으로 deny 하지 않으면 @everyone 기본 Send 권한이 살아
    # 플레이어가 글을 쓸 수 있으므로 스레드 글쓰기까지 명시 차단한다.
    read_overwrite = discord.PermissionOverwrite(
        view_channel=visible,
        read_message_history=visible,
        send_messages=False if visible else None,
        send_messages_in_threads=False if visible else None,
        create_public_threads=False if visible else None,
        create_private_threads=False if visible else None,
    )
    deny_view = discord.PermissionOverwrite(view_channel=False)

    def _allow_for_channel(ch: discord.abc.GuildChannel) -> discord.PermissionOverwrite:
        if isinstance(ch, discord.VoiceChannel):
            return voice_overwrite
        return text_overwrite

    async def _set_staff(ch: discord.abc.GuildChannel) -> None:
        if guild.me is not None:
            await ch.set_permissions(guild.me, overwrite=bot_overwrite, reason=_REASON)
        for operator_role in operator_roles:
            await ch.set_permissions(operator_role, overwrite=operator_overwrite, reason=_REASON)

    for g in _TEMPLATE_GROUPS:
        cid = category_ids.get(g["key"])
        cat = guild.get_channel(cid) if cid else None
        if not isinstance(cat, discord.CategoryChannel):
            continue
        try:
            await _set_staff(cat)
            if g["audience"] == "onboarding":
                for role_key in ("player",):
                    role = guild.get_role(role_ids.get(role_key, 0))
                    if role is not None:
                        await cat.set_permissions(role, overwrite=deny_view, reason=_REASON)
                for ch in cat.channels:
                    await _set_staff(ch)
                    aud = _ONBOARDING_AUDIENCE.get(ch.name)
                    for role_key in ("access", "pending", "player"):
                        role = guild.get_role(role_ids.get(role_key, 0))
                        if role is None:
                            continue
                        overwrite = read_overwrite if role_key == aud else deny_view
                        await ch.set_permissions(role, overwrite=overwrite, reason=_REASON)
                        touched += 1
            elif g["audience"] == "logs":
                role = guild.get_role(role_ids.get("player", 0))
                if role is not None:
                    await cat.set_permissions(role, overwrite=read_overwrite, reason=_REASON)
                    touched += 1
                for role_key in ("access", "pending"):
                    restricted = guild.get_role(role_ids.get(role_key, 0))
                    if restricted is not None:
                        await cat.set_permissions(restricted, overwrite=deny_view, reason=_REASON)
                for ch in cat.channels:
                    await _set_staff(ch)
                    if role is not None:
                        overwrite = read_overwrite if ch.name == "제재내역" else deny_view
                        await ch.set_permissions(role, overwrite=overwrite, reason=_REASON)
                        touched += 1
                    for role_key in ("access", "pending"):
                        restricted = guild.get_role(role_ids.get(role_key, 0))
                        if restricted is not None:
                            await ch.set_permissions(restricted, overwrite=deny_view, reason=_REASON)
            elif g["audience"] == "staff":
                # 운영자 전용 카테고리(문의 티켓 컨테이너). _set_staff(cat) 로 운영/봇은
                # 이미 가시. 플레이어·온보딩 역할만 카테고리 미가시로 막는다. 개별 티켓
                # 채널은 tickets.py 가 개설자 overwrite 를 얹어 공개한다.
                for role_key in ("access", "pending", "player"):
                    restricted = guild.get_role(role_ids.get(role_key, 0))
                    if restricted is not None:
                        await cat.set_permissions(restricted, overwrite=deny_view, reason=_REASON)
                for ch in cat.channels:
                    await _set_staff(ch)
                touched += 1
            else:  # "player" (read_only=True 면 플레이어는 읽기 전용)
                read_only = g.get("read_only", False)
                role = guild.get_role(role_ids.get("player", 0))
                if role is not None:
                    cat_ow = read_overwrite if read_only else _allow_for_channel(cat)
                    await cat.set_permissions(role, overwrite=cat_ow, reason=_REASON)
                    touched += 1
                for role_key in ("access", "pending"):
                    restricted = guild.get_role(role_ids.get(role_key, 0))
                    if restricted is not None:
                        await cat.set_permissions(restricted, overwrite=deny_view, reason=_REASON)
                for ch in cat.channels:
                    await _set_staff(ch)
                    if role is not None:
                        ch_ow = read_overwrite if read_only else _allow_for_channel(ch)
                        await ch.set_permissions(role, overwrite=ch_ow, reason=_REASON)
                        touched += 1
                    for role_key in ("access", "pending"):
                        restricted = guild.get_role(role_ids.get(role_key, 0))
                        if restricted is not None:
                            await ch.set_permissions(restricted, overwrite=deny_view, reason=_REASON)
            if archive and not visible and not cat.name.startswith("[종료]"):
                await cat.edit(name=f"[종료] {cat.name}", reason="서버 종료 아카이브")
        except discord.Forbidden:
            forbidden = True
    if forbidden:
        return "⚠ 봇 권한 부족(Manage Channels/Roles) — 일부 가시성 미반영"
    if touched == 0:
        return "카테고리/역할 미연결(상태만 전이)"
    return f"카테고리 가시성 갱신({touched}건)"


async def _delete_all(channels: list) -> None:
    # 역순 삭제(자식 채널 먼저). 카테고리 삭제는 자식 채널을 지우지 않으므로 명시 삭제 필요.
    for ch in reversed(channels):
        try:
            await ch.delete(reason=f"{_REASON} 롤백")
        except discord.HTTPException:
            log.warning("채널 롤백 실패: %s", getattr(ch, "id", "?"), exc_info=True)
