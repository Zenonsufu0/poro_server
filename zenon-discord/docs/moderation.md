# 모더레이션 / 운영 제재 설계 (T15)

> **[STATUS: PARTIAL]** — 디스코드 측 제재(경고·타임아웃·추방·차단) + 운영/감사 로그 구현.
> 저장 = [`data_model.md`](data_model.md) §2.4 `warnings`·§2.5 `mod_log`. 권한 = [`roles_and_permissions.md`](roles_and_permissions.md) §B.

## 0. 경계
- **디스코드 측 제재만.** 게임 내 제재(인게임 밴·아이템 회수 등)는 게임서버 API(A-3) 별개 — 본 모듈 범위 아님.
- Zenon Mon 마크 제재는 `modules/zenon_mon/commands.py`의 `마크*` 명령과 ZenonMonCore 운영 API가 담당한다. 마크 **제재 실행**은 디스코드와 동기화하지 않으나, **경고 카운트는 통합**한다(2026-07-03 결정, §2): 누적 경고 판정 시 디코 경고 + 마크 경고를 합산한다.
- 자동 키워드/스팸 차단은 **디스코드 네이티브 AutoMod**(봇 외부, §9.2). 본 모듈은 **수동 운영 제재**만.
- 모든 제재는 `mod_log` 적재 + `#운영로그` 채널 게시(누가·언제·무엇·왜) + active 서버 `로그/제재내역` 공개 기록을 남긴다.

## 1. 명령어 (모두 `requires_permission`)

| 명령 | 입력 | 효과 | 권한 |
|---|---|---|---|
| `/경고` 🟢 | `유저, 사유` | `warnings` 추가 + DM + 통합 누적 경고수 안내 + 임계 자동제재(§2) | admin·support |
| `/경고목록` 🟢 | `유저` | 경고 이력(활성/철회) 조회 | admin·support |
| `/경고취소` 🟢 | `경고_id` | `warnings.active=0` | admin·support |
| `/타임아웃` 🟢 | `유저, 기간, 단위, 사유` | 디스코드 timeout(최대 28일) + DM | admin·support |
| `/타임아웃해제` 🟢 | `유저` | timeout 해제 | admin·support |
| `/추방` 🟢 | `유저, 사유` | kick + DM(사전 발송) | admin |
| `/차단` 🟢 | `유저, 사유` | ban + DM(사전 발송) | admin |
| `/차단해제` 🟢 | `유저_id` | unban | admin |
| `/제재패널` 🟢 | — | active 서버 `로그/제재내역`에 자기 조회/운영 경고 패널 게시 | admin·support |

- Owner 는 항상 통과. 권한 역할 전부 미설정이면 보수적 차단(roles_and_permissions §B).
- 명령 응답은 ephemeral, 공개 기록은 `#운영로그` 및 active 서버 `로그/제재내역`.

### 1a. 제재내역 패널
- `/제재패널`은 active 서버 `로그/제재내역` 채널에 영구 버튼 패널을 게시한다.
- 유저: **내 제재 확인** 버튼으로 본인의 `warnings`와 최근 `mod_log` 제재 이력을 ephemeral 조회.
- 운영진(admin·support): **유저 조회**·**경고 부여**·**경고 취소** 버튼 사용. 강한 제재(타임아웃·추방·차단)는 기존 slash 명령을 사용한다.

### 1b. 대상 보호 (필수 가드)
- **operator보다 같거나 높은 권한 보유자·Owner는 제재 대상에서 제외.** admin이 다른 admin/owner를 추방·차단·타임아웃하는 것을 코드에서 차단.
- 🟢 **구현(2026-06-09): `permissions.permission_rank(member)`**(owner100·admin80·매니저50·support40·미보유0) + `modules/moderation/_target_reject_reason()` — 봇·자기자신·`target_rank >= operator_rank` 거부. 경고계에 부착(제재 명령도 동일 헬퍼 재사용 예정).
- 봇은 **자기 역할보다 상위 멤버를 제재 불가**(디스코드 제약) → 사전 검사 후 정중히 거부.
- 자기 자신·봇 대상 거부.

### 1c. 봇 길드 권한 (배포 T8 구성)
- 제재용: **Moderate Members(timeout) · Kick Members · Ban Members**. (admin.md의 Channels/Roles/Nicknames에 추가)
- 🟢 **제재 구현(2026-06-09): `/타임아웃`·`/타임아웃해제`·`/추방`·`/차단`·`/차단해제`**(`modules/moderation/commands.py`). 사용자 명시 승인하 진행(디스코드 멤버 상태변경, CLAUDE.md §4). 공통 가드 `_guard()` = `_target_reject_reason`(봇·자기자신·서버소유자·동급이상) + `_bot_hierarchy_reject`(봇 역할 위계). 추방/차단은 길드 제거 전 DM. `discord.Forbidden`(권한/위계 부족)·`HTTPException` graceful 안내. 모든 액션 `mod_log.record()` 적재. 봇 길드 권한(위 3종)은 실제 동작 전 **배포(T8)에서 부여 필요** — 미부여 시 명령은 Forbidden 안내로 안전 실패.

## 2. 경고 누적 / 에스컬레이션 (🟢 구현, 2026-07-03 결정)

**통합 누적 카운트** = 디코 경고(봇 DB `warnings` 활성) + 마크 경고(ZenonMonCore 조회, 연동
Discord ID 기준·베스트에포트). 마크 API 미설정/미연동/실패 시 디코 기준만(graceful).
- 구현: `modules/moderation/commands.py` `_combined_warn_count()`(디코, 마크, 합계, 안내).
  마크는 `integrations/zenon_mon_api.list_minecraft_sanctions(discord_id)` 에서 `action="warn"`·
  `active` 만 집계.

**자동 에스컬레이션** = 통합 누적 경고가 임계 도달 시 봇이 자동 조치(`_auto_escalate()`):
- 기본값: **WARN_TIMEOUT_THRESHOLD=3 → 타임아웃(WARN_TIMEOUT_MINUTES=60분)**,
  **WARN_BAN_THRESHOLD=5 → 차단**. 각 값 `0` = 해당 조치 비활성(`config`/`.env`).
- 판정·실행 시점 = **디스코드 경고 부여 시**(`/경고`·패널). 마크 경고만 쌓인 경우는 다음 디코
  경고나 조회(`_combined_warn_count`) 시 반영된다(마크 측 이벤트 콜백은 미도입).
- 조치 주체 = 봇(operator=bot). 봇 권한/위계 부족 시 실행 실패를 안내만 하고 수동 조치에 맡긴다.
  이미 타임아웃 중이면 중복 적용하지 않는다. 자동 조치도 `mod_log`+`#운영로그`+제재내역에 기록.

## 3. 운영 / 감사 로그
- 모든 액션(경고·타임아웃·추방·차단·서버 생애주기·XP보정 등) → `mod_log`:
  `{action, target_id, operator_id, reason, detail, created_at}`.
- 동시에 `#운영로그`(`CHANNEL_MODLOG_ID`, 신규 `.env`) 임베드 게시.
- 제재 액션(`warn`·`warn_revoke`·`timeout`·`timeout_clear`·`kick`·`ban`·`unban`)은 active 서버 `로그/제재내역`에도 공개 임베드로 게시한다.
- 🟢 **인프라 구현(2026-06-09): `core/mod_log.py` `record(bot, *, action, operator_id, target_id, reason, detail)`** — DB 적재 보장 + `#운영로그` 게시 best-effort(채널 미설정/실패여도 적재). 모더레이션 명령은 이 헬퍼만 호출(raw INSERT 금지). action 코드 라벨 매핑 = `_ACTION_META`(경고=`warn`·타임아웃=`timeout`·추방=`kick`·차단=`ban`·서버전이=`server_*` 등).
- 봇이 멤버 역할/상태 변경 시 항상 `reason` 기록(roles_and_permissions §보안메모).

## 4. DM 통보
- 제재 대상에게 사유 DM(베스트에포트). **추방/차단은 실행 전 DM 발송**(차단 후엔 공유 서버 없어 DM 불가).
- DM 차단된 유저는 무시(로깅).

## 5. 미확정
- ~~경고 임계·자동 에스컬레이션~~ → 🟢 확정·구현(§2, 2026-07-03: 3=타임아웃 1h·5=차단, 통합 카운트).
- 마크 경고 발생 시 **즉시** 자동제재(마크→디코 이벤트 콜백/인바운드) 도입 여부 — 현재는 디코 경고 시점 판정.
- 제재 권한 세분(경고=support 허용 / 차단=admin 전용) 최종 확정.
- 디스코드 차단과 게임 화이트리스트 연동 여부(현재 별개 — 게임 제재는 A-3).
- DM 문구 템플릿.
</content>
