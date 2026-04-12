# 포로 서버 보스 입장 / 도전 / 기록 저장 구조 초안

## 문서 목적
이 문서는 보스 입장과 도전, 기록 저장을
**제한은 최소화하면서도 운영은 안정적이게** 관리하기 위한 구조 초안이다.

핵심 목표:
- 결전/극상위 보스에 과한 주간 제한을 걸지 않는다.
- 대신 입장 조건, 현재 방 상태, 기록 저장을 분리해 관리한다.
- 결과창/명예의 전당/대시보드와 기록을 연결한다.

---

## 1. 입장 철학

### 추천
- 결전: 자유 입장 중심
- 극상위: 자유 도전 중심
- 최초 보상/전승 보상만 1회성
- 기록은 매번 저장

### 비추천
- 주간 1회, 2회 같은 강한 락
- 미클리어 시도 자체를 막는 방식
- 원하는 날 몰아치기 불가 구조

---

## 2. 추천 필드

### boss_entry_rule
- `boss_id`
- `requires_unlock_quest`
- `requires_party_size_min`
- `requires_party_size_max`
- `requires_key_item`
- `daily_limit`
- `weekly_limit`
- `notes`

추천 기본값:
- `daily_limit = null`
- `weekly_limit = null`

### boss_run_records
- `run_id`
- `boss_id`
- `party_size`
- `entered_at`
- `ended_at`
- `clear_success`
- `phase_reached`
- `remaining_death_count`
- `clear_time_seconds`
- `failure_reason_code`

---

## 3. 한 줄 요약

**보스 입장 구조는 daily/weekly limit를 기본적으로 비워두고, 해금 조건과 run 기록 저장만 명확히 관리하는 방식이 포로 서버의 제한 최소화 철학에 가장 잘 맞는다.**
