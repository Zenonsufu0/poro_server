# 포로 서버 음식 / 만찬 / 물약 효과 seed / config 표 초안

## 문서 목적
이 문서는 음식, 만찬, 물약 효과를 실제 effect seed/config 데이터로 넣기 위한 초안이다.

핵심 목표:
- item master와 effect master를 분리해 관리한다.
- 하나의 소비 아이템에 여러 효과를 묶을 수 있게 한다.
- 버프 슬롯, 적용 대상, 지속시간, 수치 타입을 데이터화한다.

---

## 1. 추천 필드

- `consumable_id`
- `effect_order`
- `effect_type`
- `target_type`
- `buff_slot_type`
- `duration_seconds`
- `cooldown_seconds`
- `value_type`
- `value_amount`
- `secondary_effect_type`
- `secondary_value_amount`
- `replace_existing`

---

## 2. 제국 수비 만찬 예시

| consumable_id | order | effect_type | target | slot | duration | cooldown | value_type | value |
|---|---:|---|---|---|---:|---:|---|---:|
| capital_defense_feast | 1 | MAX_HP_PERCENT | PARTY | MEAL_SLOT | 2700 | 0 | PERCENT | 5 |
| capital_defense_feast | 2 | RECOVERY_EFFICIENCY | PARTY | MEAL_SLOT | 2700 | 0 | PERCENT | 6 |

---

## 3. 희귀 회복 물약 예시

| consumable_id | order | effect_type | target | slot | duration | cooldown | value_type | value |
|---|---:|---|---|---|---:|---:|---|---:|
| rare_healing_potion | 1 | HEAL_PERCENT | SELF | NONE | 0 | 40 | PERCENT | 35 |
| rare_healing_potion | 2 | DAMAGE_REDUCTION | SELF | NONE | 5 | 40 | PERCENT | 5 |

---

## 4. 침식 완화제 예시

| consumable_id | order | effect_type | target | slot | duration | cooldown | value_type | value |
|---|---:|---|---|---|---:|---:|---|---:|
| erosion_relief_agent | 1 | STATUS_ACCUMULATION_REDUCTION | SELF | RESIST_POTION_SLOT | 90 | 0 | PERCENT | 15 |

---

## 5. 한 줄 요약

**소비 아이템 효과는 seed/config로 분리해 한 아이템에 여러 효과를 묶고, MEAL_SLOT / RESIST_POTION_SLOT / COMBAT_POTION_SLOT과 연결하면 가장 안정적으로 확장할 수 있다.**
