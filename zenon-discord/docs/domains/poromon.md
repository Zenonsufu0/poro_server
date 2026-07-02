# Zenon Mon 도메인 — 명세

> **[도메인: Zenon Mon]** **[STATUS: PARTIAL]** — 온보딩 인증과 마크 제재 API는 구현 완료.
> 조회 명령어·게임서버 push 알림은 아직 스텁/TODO.
> 코드: `modules/zenon_mon/commands.py`, `integrations/zenon_mon_api.py`(인증/마크 제재 구현 + 조회 스텁).

## 원칙

- RPG 도메인과 **완전 분리**한다. RPG 코드/클라이언트를 import 하지 않는다.
- Zenon Mon 서버 연동은 `integrations/zenon_mon_api.py` 를 통해서만 한다.
- 인증 외 추가 API 연동은 사용자가 명시적으로 요청할 때만 구현한다.

## 연동 방식 (DL-133)

- **ZenonMonCore(커스텀 모드)가 RPG와 동일한 HTTP API 패턴을 노출**한다. `zenon_mon_api` 가 그 클라이언트.
- 봇 관여 경계는 RPG와 동일 — 게임 상태 권위는 Zenon Mon 서버, 봇은 API 클라이언트(DL-133).
- 이벤트 알림은 **Zenon Mon 서버 → 봇 push**(공유 시크릿/HMAC 보호) → `core/notifier` 게시.
- 온보딩은 전역 active 서버 모델을 따른다 — Zenon Mon active 서버 → 약관동의 →
  Zenon Mon 인게임 `/인증` 코드 발급 → 봇 인증 버튼/모달 검증 → `포로몬플레이어`([`../roles_and_permissions.md`] §D).

## 인증 / 온보딩

| 항목 | 상태 | 코드/설정 |
|---|---|---|
| 봇 측 인증 클라이언트 | 🟢 구현 | `integrations/zenon_mon_api.py` `verify_code` |
| 공통 온보딩 라우팅 | 🟢 구현 | `modules/onboarding/panels.py` `_verifiers["poromon"]` |
| 인증 URL/키 | 🟢 구현 | `ZENON_MON_AUTH_URL`, `ZENON_MON_AUTH_KEY` |
| 마크 제재 API 클라이언트 | 🟢 구현 | `/마크경고`·`/마크킥`·`/마크밴`·`/마크밴해제`·`/마크제재조회`, ZenonMonCore `/admin/sanctions/*` |
| 조회/도감 API | 🟡 스텁 | `get_server_status`, `get_player_summary` |

## 명령어 후보 (TODO)

| 명령어 | 설명 | 선행 조건 |
|---|---|---|
| `/포로몬현황` | 모드 서버 상태(접속 인원·TPS) | `zenon_mon_api.get_server_status` 구현 |
| `/포로몬도감` | 도감/보유 현황 조회 | `zenon_mon_api` + Zenon Mon DB/API 확정 |
| `/마크경고`·`/마크킥`·`/마크밴`·`/마크밴해제` | 디스코드 제재와 분리된 마크 서버 제재 | 구현 완료, 실서버 e2e 필요 |
| `/마크제재조회`·`/마크제재패널` | 마크 제재 이력 조회/패널 게시 | 구현 완료, 실서버 e2e 필요 |

## 알림 후보 (TODO)

- Zenon Mon 이벤트/공지 → `@포로몬알림` (→ [`../notifications.md`]).
- 경제/아이템 이상징후 → `poromon.anomaly` push → active 서버 `로그/경고`.
  - 근거 지표 후보: PoroMonCore `EconomyStats`의 골드 유입/유출, 아이템 판매/구매 카운터, `AuditLog`.
  - 산출 기준(임계값·기간·심각도)은 ZenonMonCore 쪽에서 확정 필요.
- 마크 서버 제재 기록 → `poromon.minecraft_sanction` push → active 서버 `로그/제재내역`.
  - 인게임 op/콘솔 제재도 ZenonMonCore가 AuditLog 후 push하면 디코에서 확인 가능.

## 미확정

- 연결 방식은 HTTP API로 확정(DL-135). 남은 것: 조회 API/이벤트 push 스키마와 조회 가능 데이터 범위.
  ZenonMonCore(`../../zenon-mon/docs/03_zenonmoncore/`) 설계 확정과 함께 결정.
- 마크 제재 API는 닉네임/UUID/연동 Discord ID를 지원한다. 남은 것은 실서버 e2e 검증이다.
