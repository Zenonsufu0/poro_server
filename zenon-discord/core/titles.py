"""
칭호 데이터 접근 (T13) — community_level.md §3.2, data_model.md §2.3·2.3b.

칭호 = 디스코드 역할이 **아님**(순수 표시 데이터/코스메틱).
운영자가 카탈로그 칭호를 생성하고 유저에게 직접 부여/회수한다.
유저는 보유 칭호 중 1개를 장착한다(`/칭호`).
"""
from __future__ import annotations

import uuid

from core.db import Database


async def list_catalog(db: Database) -> list:
    return await db.fetchall(
        "SELECT * FROM titles WHERE required_level IS NULL ORDER BY id"
    )


async def get_title(db: Database, title_id: int):
    return await db.fetchone(
        "SELECT * FROM titles WHERE id = ? AND required_level IS NULL", (title_id,)
    )


async def create_title(db: Database, display_name: str) -> int:
    key = f"manual_{uuid.uuid4().hex[:12]}"
    return await db.execute(
        "INSERT INTO titles(key, display_name, required_level) VALUES(?, ?, NULL)",
        (key, display_name),
    )


async def owned_titles(db: Database, user_id: int) -> list:
    """보유 칭호(장착 여부 포함), 임계 낮은 순."""
    return await db.fetchall(
        "SELECT t.id, t.display_name, t.required_level, ut.equipped "
        "FROM user_titles ut JOIN titles t ON t.id = ut.title_id "
        "WHERE ut.discord_user_id = ? ORDER BY t.required_level, t.id",
        (user_id,),
    )


async def equipped_title(db: Database, user_id: int) -> str | None:
    """장착 중인 칭호 display_name(없으면 None)."""
    row = await db.fetchone(
        "SELECT t.display_name FROM user_titles ut JOIN titles t ON t.id = ut.title_id "
        "WHERE ut.discord_user_id = ? AND ut.equipped = 1",
        (user_id,),
    )
    return row["display_name"] if row else None


async def grant_title(db: Database, user_id: int, title_id: int) -> None:
    """보유 추가(이미 있으면 무시)."""
    await db.execute(
        "INSERT OR IGNORE INTO user_titles(discord_user_id, title_id, acquired_at, equipped) "
        "VALUES(?, ?, strftime('%s','now'), 0)",
        (user_id, title_id),
    )


async def revoke_title(db: Database, user_id: int, title_id: int) -> bool:
    """보유 칭호 회수. 회수된 행이 있으면 True."""
    before = await db.fetchone(
        "SELECT 1 FROM user_titles WHERE discord_user_id = ? AND title_id = ?",
        (user_id, title_id),
    )
    if before is None:
        return False
    await db.execute(
        "DELETE FROM user_titles WHERE discord_user_id = ? AND title_id = ?",
        (user_id, title_id),
    )
    return True


async def equip(db: Database, user_id: int, title_id: int) -> bool:
    """보유 칭호 1개 장착(나머지 해제). 미보유면 False."""
    owned = await db.fetchone(
        "SELECT 1 FROM user_titles WHERE discord_user_id = ? AND title_id = ?",
        (user_id, title_id),
    )
    if owned is None:
        return False
    await db.execute(
        "UPDATE user_titles SET equipped = 0 WHERE discord_user_id = ?", (user_id,)
    )
    await db.execute(
        "UPDATE user_titles SET equipped = 1 WHERE discord_user_id = ? AND title_id = ?",
        (user_id, title_id),
    )
    return True
