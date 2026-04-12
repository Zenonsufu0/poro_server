# 포로 서버 보스 결과창 표시 문구 테이블 초안

## 문서 목적
이 문서는 보스 결과창, 실패창, 기여도 화면에서 사용할 대표 문구를 정리한 초안이다.

핵심 목표:
- 결과창 문구 톤을 통일한다.
- 성공/실패/발악/무사망/최초 클리어 같은 상황에서 일관된 표현을 제공한다.
- UI 구현 시 하드코딩 문구를 줄인다.

---

## 1. 성공 문구 예시

| code | 문구 |
|---|---|
| clear_success | 전투 승리 |
| clear_flawless | 무사망 돌파 |
| clear_last_stand | 최후까지 생존 |
| clear_berserk | 발악 돌파 성공 |
| clear_first_time | 첫 결전 돌파 |
| clear_extreme_first | 전승의 문을 열었다 |

---

## 2. 실패 문구 예시

| code | 문구 |
|---|---|
| fail_deathcount_zero | 데스카운트 소진 |
| fail_party_wipe | 파티 전멸 |
| fail_berserk_timeout | 발악 저지 실패 |
| fail_stagger_fail | 핵심 저지 실패 |
| fail_timeout | 전투 시간 초과 |

---

## 3. 기여도 문구 예시

| code | 문구 |
|---|---|
| mvp_damage | 최대 기여 |
| mvp_stagger | 저지 핵심 |
| mvp_survival | 최후 생존 |
| contribution_damage | 누적 피해 |
| contribution_share | 피해 점유율 |
| contribution_deaths | 사망 횟수 |

---

## 4. 보상 문구 예시

| code | 문구 |
|---|---|
| reward_gold | 획득 골드 |
| reward_stone | 획득 강화석 |
| reward_cube | 획득 큐브 |
| reward_material | 획득 재료 |
| reward_symbolic | 상징 보상 |
| reward_title | 칭호 해금 |
| reward_relic | 전승 유물 획득 |

---

## 5. 한 줄 요약

**보스 결과창 문구는 성공/실패/기여도/보상 영역별 코드 테이블로 관리하면 톤을 통일하기 쉽고 UI 하드코딩도 줄일 수 있다.**
