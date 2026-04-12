# 포로 서버 요리 / 만찬 / 물약 아이템 seed 표 초안

## 문서 목적
이 문서는 음식 / 만찬 / 물약을 실제 seed 데이터나 registry로 넣기 위한 **아이템 seed 표** 초안이다.

핵심 목표:
- 아이템 id, 이름, 타입, 등급, 주요 효과를 seed 수준으로 고정한다.
- Codex가 consumable item master를 만들 수 있게 한다.

---

## 1. 음식 seed

| consumable_id | 이름 | 타입 | 주요 효과 | 지속시간 |
|---|---|---|---|---:|
| healing_soup | 회복 수프 | FOOD | 최대 체력 +3% | 1800 |
| gatherer_lunchbox | 채집꾼의 도시락 | FOOD | 채집 효율 +8% | 1800 |
| frost_meal | 설혼 방한식 | FOOD | 냉기 저항 +10% | 1800 |
| flame_meal | 홍염 내열식 | FOOD | 화상 저항 +10% | 1800 |
| survival_meal | 생존 전투식 | FOOD | 회복 효율 +8% | 1800 |
| scout_meal | 정찰병 식사 | FOOD | 이동속도 +3% | 1800 |

---

## 2. 만찬 seed

| consumable_id | 이름 | 타입 | 주요 효과 | 지속시간 |
|---|---|---|---|---:|
| capital_defense_feast | 제국 수비 만찬 | FEAST | 최대 체력 +5%, 회복 효율 +6% | 2700 |
| forest_spirit_feast | 정령 숲 만찬 | FEAST | 상태이상 저항 +10%, 이동속도 +2% | 2700 |
| frost_guard_feast | 설원의 방한 만찬 | FEAST | 냉기 저항 +12%, 최대 체력 +4% | 2700 |
| flame_guard_feast | 홍염 대비 만찬 | FEAST | 화상 저항 +12%, 회복 효율 +6% | 2700 |
| explorer_feast | 탐험가의 성찬 | FEAST | 최대 체력 +4%, 상태이상 저항 +8% | 2700 |

---

## 3. 회복 물약 seed

| consumable_id | 이름 | 타입 | 등급 | 주요 효과 | 쿨다운 |
|---|---|---|---|---|---:|
| basic_healing_potion | 일반 회복 물약 | POTION_HEAL | NORMAL | 체력 15% 회복 | 30 |
| advanced_healing_potion | 고급 회복 물약 | POTION_HEAL | ADVANCED | 체력 25% 회복 | 35 |
| rare_healing_potion | 희귀 회복 물약 | POTION_HEAL | RARE | 체력 35% 회복 + 피해감소 5% 5초 | 40 |

---

## 4. 상태 대응 물약 seed

| consumable_id | 이름 | 타입 | 주요 효과 | 지속시간 |
|---|---|---|---|---:|
| frost_relief_potion | 냉기 완화 물약 | POTION_RESIST | 냉기 대응 +15% | 90 |
| flame_resist_potion | 화상 저항 물약 | POTION_RESIST | 화상 저항 +15% | 90 |
| poison_relief_potion | 독 완화 물약 | POTION_RESIST | 독 지속 완화 +20% | 90 |
| bleed_relief_potion | 출혈 완화 물약 | POTION_RESIST | 출혈 피해 완화 +15% | 90 |
| erosion_relief_agent | 침식 완화제 | POTION_RESIST | 침식 대응 +15% | 90 |

---

## 5. 전투 보조 물약 seed

| consumable_id | 이름 | 타입 | 주요 효과 | 지속시간 |
|---|---|---|---|---:|
| survival_tonic | 생존 강화 약제 | POTION_COMBAT | 받는 피해 8% 감소 | 30 |
| focus_extract | 집중 추출액 | POTION_COMBAT | 상태이상 저항 +10%, 이동속도 +3% | 20 |
| emergency_regen_tonic | 응급 재생 약제 | POTION_COMBAT | 2초마다 체력 3% 회복 | 10 |
| mobility_tonic | 긴급 기동 약제 | POTION_COMBAT | 이동속도 +8% | 15 |

---

## 6. 한 줄 요약

**이 seed 표는 음식 6종, 만찬 5종, 회복 물약 3종, 상태 대응 물약 5종, 전투 보조 물약 4종을 1차 기준으로 고정해 Codex가 consumable item master와 effect master seed를 만들 수 있게 하는 문서다.**
