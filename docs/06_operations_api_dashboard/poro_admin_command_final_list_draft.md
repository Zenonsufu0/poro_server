# 포로 서버 관리자 명령 최종 목록표 초안

## 문서 목적
이 문서는 포로 서버에서 QA, 운영, 디버그에 필요한 **관리자 명령 전체 목록의 1차 초안**을 정리한 문서다.

핵심 목표:
- 보스 / 생활 / 경제 / 운영 테스트에 필요한 명령을 한 번에 정리한다.
- Codex가 command 패키지를 구현할 때 기준으로 삼게 한다.
- 추후 권한 단계별로 잘라낼 수 있게 카테고리화를 한다.

---

## 1. 보스 관련 명령

### 보스 테스트
- `/boss test start <boss_id>`
- `/boss reset`
- `/boss state hp <value>`
- `/boss phase set <n>`
- `/boss pattern cast <pattern_id>`
- `/boss debug state`

### 데스카운트 / 결과
- `/boss deathcount set <value>`
- `/boss revive <player>`
- `/boss fail <reason>`
- `/boss clear`
- `/boss result show`

---

## 2. 생활 관련 명령

### 자원/숙련도
- `/life admin give <resource> <amount>`
- `/life admin take <resource> <amount>`
- `/life admin skill <type> <level>`
- `/life admin exp <type> <amount>`

### 청사진/영지
- `/life admin unlock-blueprint <blueprintId>`
- `/life admin install-facility <facilityId>`
- `/life admin set-facility-level <userFacilityId> <level>`
- `/life admin refresh-estate`
- `/life admin harvest <userFacilityId>`

### 제작
- `/life admin craft <recipeId>`
- `/life admin recipe unlock <recipeId>`

---

## 3. 경제 관련 명령

- `/eco give-gold <player> <amount>`
- `/eco take-gold <player> <amount>`
- `/eco give-cube <player> <amount>`
- `/eco give-stone <player> <amount>`
- `/eco summary`
- `/eco reset-test-values`

---

## 4. 장비/성장 관련 명령

- `/item give <itemId>`
- `/item enhance <level>`
- `/item potential reroll`
- `/item set-potential-grade <grade>`
- `/item bind`
- `/item unlock`

---

## 5. 운영/디버그 관련 명령

- `/admin metrics overview`
- `/admin alerts show`
- `/admin reload-config`
- `/admin test-ui <uiId>`
- `/admin log-tail <category>`
- `/admin set-flag <feature> <true/false>`

---

## 6. 권한 단계 초안

- `DEV_ADMIN`: 전부 가능
- `OP_ADMIN`: 운영/보상/경고/경제
- `QA_ADMIN`: 테스트/보스/생활 검증
- `GM`: 제한된 조회/보조 명령

---

## 7. 구현 메모

- 보스/생활/경제/운영 명령은 서브커맨드 구조로 분리 추천
- 명령은 서비스 계층을 호출하게 하고, 직접 로직을 길게 넣지 않는다
- 테스트 전용 명령은 프로덕션에서 감추거나 권한 제한 필수

---

## 8. 한 줄 요약

**관리자 명령은 보스·생활·경제·성장·운영 카테고리로 나누고, QA와 디버그에 필요한 최소 서브커맨드를 먼저 구현한 뒤 권한 단계별로 노출을 조정하는 구조가 가장 안정적이다.**
