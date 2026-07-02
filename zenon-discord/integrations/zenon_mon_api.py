"""
Zenon Mon(모드 서버) 연동 클라이언트.

RPG 와 Zenon Mon은 코드 공유가 거의 없으므로 봇 내부에서도 통합 경로를 분리한다.
RPG 연동은 integrations/rpg_api.py(ZenonRpgApiClient)가 담당하고,
Zenon Mon 연동은 이 모듈이 담당한다.

현재 구현된 것:
  - verify_code: 디스코드 인증 코드 검증 (ZenonMonCore /auth/verify 계약).
    봇·MC 동일 호스트 전제 → 베이스 URL 은 127.0.0.1 루프백(ZENON_MON_AUTH_URL),
    API 키는 secret(ZENON_MON_AUTH_KEY) 으로만 로드한다.

스텁(미구현 — 엔드포인트 미확정):
  - get_server_status / get_player_summary
"""
from __future__ import annotations

import logging
from urllib.parse import quote

import aiohttp

from core import config
from integrations.common import VerifyError

log = logging.getLogger(__name__)


class ZenonMonAuthError(VerifyError):
    """Zenon Mon 인증 API 호출 중 운영자 확인이 필요한 오류(VerifyError 하위).

    키 불일치(401)·네트워크 실패·예상치 못한 응답 등 *사용자 잘못이 아닌*
    오류에 사용한다. 호출부는 이 예외를 잡아 운영자 로그 + 사용자에겐
    일반 오류 안내로 처리한다. (코드 만료/없음(404)은 예외가 아니라
    verify_code 의 정상 반환값으로 구분한다.)
    """


class ZenonMonAdminError(RuntimeError):
    """Zenon Mon 운영 API 호출 실패."""


class ZenonMonApiClient:
    """Zenon Mon 서버 조회/인증용 클라이언트.

    RPG 클라이언트(ZenonRpgApiClient)와 동일하게 aiohttp 세션을 지연 생성하고
    API 키 헤더를 붙인다.
    """

    def __init__(self, session: aiohttp.ClientSession | None = None) -> None:
        self._session = session

    def _get_session(self) -> aiohttp.ClientSession:
        if self._session is None or self._session.closed:
            self._session = aiohttp.ClientSession()
        return self._session

    @staticmethod
    def _headers(api_key: str) -> dict[str, str]:
        return {
            "X-Api-Key": api_key,
            "Content-Type": "application/json",
        }

    async def close(self) -> None:
        if self._session and not self._session.closed:
            await self._session.close()

    # ─── 인증 ────────────────────────────────────────────────────

    async def verify_code(self, code: str, discord_id: int | str) -> dict:
        """POST {base}/auth/verify — 디스코드 인증 코드 검증.

        헤더 `X-API-Key`, 바디 `{"code", "discordId"}` (ZenonMonCore 계약).

        반환 (계약 = RPG AuthApiHandler DL-138 레퍼런스와 동일):
          {"ok": True,  "uuid": <MC UUID 또는 None>, "name": <MC 닉 또는 None>}
                                                       — 200 인증 성공
          {"ok": False, "reason": "not_found"}         — 404 코드 만료/없음
          {"ok": False, "reason": "rate_limited"}      — 429 시도 과다(잠시 후 재시도)

        예외:
          ZenonMonAuthError — 401(키 불일치)·네트워크 실패·예상치 못한 응답.
                             (운영자 확인 필요, 사용자에겐 일반 오류로 안내)
        """
        if not config.ZENON_MON_AUTH_KEY:
            raise ZenonMonAuthError("ZENON_MON_AUTH_KEY 미설정 — Zenon Mon 인증 비활성")

        url = f"{config.ZENON_MON_AUTH_URL}/auth/verify"
        headers = self._headers(config.ZENON_MON_AUTH_KEY)
        payload = {"code": code, "discordId": str(discord_id)}

        try:
            async with self._get_session().post(url, json=payload, headers=headers) as resp:
                if resp.status == 200:
                    data = await resp.json()
                    return {"ok": True, "uuid": data.get("uuid"), "name": data.get("name")}
                if resp.status == 404:
                    return {"ok": False, "reason": "not_found"}
                if resp.status == 429:
                    return {"ok": False, "reason": "rate_limited"}
                if resp.status == 401:
                    raise ZenonMonAuthError("Zenon Mon 인증 API 키 불일치(401)")
                text = await resp.text()
                raise ZenonMonAuthError(f"예상치 못한 응답 {resp.status}: {text[:200]}")
        except aiohttp.ClientError as e:
            raise ZenonMonAuthError(f"Zenon Mon 인증 API 네트워크 오류: {e}") from e

    # ─── 운영 제재 API ────────────────────────────────────────────

    async def minecraft_sanction(
        self,
        *,
        action: str,
        target: str,
        reason: str | None,
        operator_discord_id: int | str,
    ) -> dict:
        """POST /admin/sanctions/{action} — 마크 서버 제재 요청.

        action: warn|kick|ban|unban.
        대상 식별은 ZenonMonCore 권위다. 봇은 Discord 계정과 MC 계정 매핑을 저장하지 않고,
        target 문자열(닉/UUID/연동키)을 그대로 서버에 전달한다.
        """
        if action not in {"warn", "kick", "ban", "unban"}:
            raise ValueError(f"unsupported minecraft sanction action: {action}")
        if not config.ZENON_MON_API_KEY:
            raise ZenonMonAdminError("ZENON_MON_API_KEY 미설정 — Zenon Mon 운영 API 비활성")

        url = f"{config.ZENON_MON_API_URL}/admin/sanctions/{action}"
        payload = {
            "target": target,
            "reason": reason or "",
            "operatorDiscordId": str(operator_discord_id),
        }
        try:
            async with self._get_session().post(
                url, json=payload, headers=self._headers(config.ZENON_MON_API_KEY)
            ) as resp:
                if resp.status in (200, 201, 204):
                    data = await _safe_json(resp)
                    return {"ok": True, **data}
                if resp.status == 404:
                    data = await _safe_json(resp)
                    return {"ok": False, "reason": data.get("reason") or "not_found"}
                if resp.status == 409:
                    data = await _safe_json(resp)
                    return {"ok": False, "reason": data.get("reason") or "conflict"}
                if resp.status == 501:
                    return {"ok": False, "reason": "not_implemented"}
                if resp.status == 401:
                    raise ZenonMonAdminError("Zenon Mon 운영 API 키 불일치(401)")
                text = await resp.text()
                raise ZenonMonAdminError(f"예상치 못한 응답 {resp.status}: {text[:200]}")
        except aiohttp.ClientError as e:
            raise ZenonMonAdminError(f"Zenon Mon 운영 API 네트워크 오류: {e}") from e

    async def list_minecraft_sanctions(self, target: str) -> dict:
        """GET /admin/sanctions?target=... — 마크 제재 이력 조회."""
        if not config.ZENON_MON_API_KEY:
            raise ZenonMonAdminError("ZENON_MON_API_KEY 미설정 — Zenon Mon 운영 API 비활성")
        url = f"{config.ZENON_MON_API_URL}/admin/sanctions?target={quote(target)}"
        try:
            async with self._get_session().get(
                url, headers=self._headers(config.ZENON_MON_API_KEY)
            ) as resp:
                if resp.status == 200:
                    data = await _safe_json(resp)
                    return {"ok": True, **data}
                if resp.status == 404:
                    return {"ok": False, "reason": "not_found"}
                if resp.status == 501:
                    return {"ok": False, "reason": "not_implemented"}
                if resp.status == 401:
                    raise ZenonMonAdminError("Zenon Mon 운영 API 키 불일치(401)")
                text = await resp.text()
                raise ZenonMonAdminError(f"예상치 못한 응답 {resp.status}: {text[:200]}")
        except aiohttp.ClientError as e:
            raise ZenonMonAdminError(f"Zenon Mon 운영 API 네트워크 오류: {e}") from e

    # ─── 조회 (스텁 — 엔드포인트 미확정) ──────────────────────────────

    async def get_server_status(self) -> dict:
        """Zenon Mon 모드 서버 상태(온라인 인원·TPS 등) 조회."""
        raise NotImplementedError("zenon_mon_api: get_server_status 미구현")

    async def get_player_summary(self, nick: str) -> dict:
        """Zenon Mon 플레이어 요약 정보 조회."""
        raise NotImplementedError("zenon_mon_api: get_player_summary 미구현")


async def _safe_json(resp: aiohttp.ClientResponse) -> dict:
    if resp.status == 204:
        return {}
    try:
        data = await resp.json()
    except (aiohttp.ContentTypeError, ValueError):
        return {}
    return data if isinstance(data, dict) else {}
