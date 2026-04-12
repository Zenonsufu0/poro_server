# 포로 서버 보스 보상 seed / config 표 초안

## 문서 목적
이 문서는 보스 보상 시스템을 실제 seed/config 데이터로 넣기 위해 필요한 표준 형태를 정리한 초안이다.

핵심 목표:
- 결전/극상위 보상을 코드 하드코딩이 아니라 데이터 기반으로 관리한다.
- 기본보상, 추가보상, 최초 클리어 보상, 상징 보상을 한 구조 안에 넣는다.
- Codex가 reward resolver와 distributor를 쉽게 만들 수 있게 한다.

---

## 1. 추천 필드

- `boss_id`
- `reward_group`
- `reward_type`
- `reward_target_id`
- `amount_min`
- `amount_max`
- `drop_chance`
- `is_first_clear_only`
- `is_account_bound`
- `is_tradeable`
- `notes`

---

## 2. reward_group 추천

- `BASE`
- `BONUS`
- `FIRST_CLEAR`
- `SYMBOLIC`
- `HONOR`
- `COSMETIC`

---

## 3. 라그네스 예시

| boss_id | reward_group | reward_type | reward_target_id | amount_min | amount_max | drop_chance | first_clear_only | 비고 |
|---|---|---|---|---:|---:|---:|---|---|
| ragnes | BASE | gold | gold | 9000 | 9000 | 100 | false | 기본 골드 |
| ragnes | BASE | item | enhancement_stone | 12 | 12 | 100 | false | 강화석 |
| ragnes | BASE | item | cube | 5 | 5 | 100 | false | 큐브 |
| ragnes | BASE | item | duel_material | 3 | 3 | 100 | false | 결전 재료 |
| ragnes | BASE | item | southern_theme_material | 3 | 3 | 100 | false | 테마 재료 |
| ragnes | BONUS | item | lava_core | 1 | 1 | 15 | false | 희귀 재료 |
| ragnes | BONUS | item | flame_powder_bundle | 1 | 1 | 20 | false | 생활/가공 보조 |
| ragnes | SYMBOLIC | title | title_ragnes_clear | 1 | 1 | 100 | true | 첫 칭호 |
| ragnes | SYMBOLIC | trophy | trophy_ragnes_core | 1 | 1 | 100 | true | 영지 트로피 |

---

## 4. 아그네르 예시

| boss_id | reward_group | reward_type | reward_target_id | amount_min | amount_max | drop_chance | first_clear_only | 비고 |
|---|---|---|---|---:|---:|---:|---|---|
| agner | BASE | gold | gold | 4000 | 4000 | 100 | false | 소량 골드 |
| agner | BASE | item | extreme_token | 1 | 1 | 100 | false | 극상위 증표 |
| agner | BASE | item | symbolic_currency | 2 | 2 | 100 | false | 상징 재화 |
| agner | FIRST_CLEAR | title | title_agner_legend | 1 | 1 | 100 | true | 전승 칭호 |
| agner | FIRST_CLEAR | cosmetic | aura_agner_flame | 1 | 1 | 100 | true | 후광 |
| agner | FIRST_CLEAR | trophy | trophy_agner_heart | 1 | 1 | 100 | true | 영지 트로피 |
| agner | FIRST_CLEAR | relic | relic_agner_heart | 1 | 1 | 100 | true | 전승 유물 |
| agner | HONOR | record | hall_of_fame_entry | 1 | 1 | 100 | true | 명예 기록 |

---

## 5. 카르멘 예시

| boss_id | reward_group | reward_type | reward_target_id | amount_min | amount_max | drop_chance | first_clear_only | 비고 |
|---|---|---|---|---:|---:|---:|---|---|
| carmen | BASE | gold | gold | 5000 | 5000 | 100 | false | 소량 골드 |
| carmen | BASE | item | extreme_token | 1 | 1 | 100 | false | 극상위 증표 |
| carmen | BASE | item | symbolic_currency | 3 | 3 | 100 | false | 상징 재화 |
| carmen | FIRST_CLEAR | title | title_carmen_final | 1 | 1 | 100 | true | 최종 칭호 |
| carmen | FIRST_CLEAR | cosmetic | skin_carmen_resonance | 1 | 1 | 100 | true | 무기 스킨 |
| carmen | FIRST_CLEAR | trophy | trophy_carmen_core | 1 | 1 | 100 | true | 최종 코어 트로피 |
| carmen | FIRST_CLEAR | relic | relic_carmen_core | 1 | 1 | 100 | true | 전승 유물 |
| carmen | HONOR | record | hall_of_fame_entry | 1 | 1 | 100 | true | 명예 기록 |

---

## 6. 운영 메모

- 결전은 `BASE + BONUS + SYMBOLIC(선택)` 감각
- 극상위는 `BASE + FIRST_CLEAR + HONOR + COSMETIC` 감각
- 전승 유물은 tradeable false, account bound true가 자연스럽다

---

## 7. 한 줄 요약

**보스 보상은 seed/config 테이블로 관리하는 것이 가장 좋고, 결전은 BASE/BONUS 중심, 극상위는 FIRST_CLEAR/HONOR/COSMETIC 중심으로 분리하면 각 보스의 상징 보상과 전승 유물을 안정적으로 관리할 수 있다.**
