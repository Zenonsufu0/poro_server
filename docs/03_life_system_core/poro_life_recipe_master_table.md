# 포로 서버 생활 제작식 전체표 초안

## 문서 목적
이 문서는 포로 서버 생활 시스템의 **1차 제작식 마스터 표**를 정리한 초안이다.

핵심 목표:
- 요리 / 만찬 / 연금 / 제련/가공의 1차 레시피 풀을 정리한다.
- Codex가 recipe seed 데이터나 registry를 만들 수 있게 한다.
- 생활 재료와 산출물의 연결을 고정한다.

주의:
- 재료 개수와 수치는 1차 권장안이다.
- 실제 테스트에서 조정 가능하다.

---

## 1. 제련 / 가공 제작식

| recipe_id | 분류 | 결과물 | 필요 재료 | 권장 수량 | 요구 숙련도 |
|---|---|---|---|---|---:|
| refine_herb_basic | REFINING | 정제 약초 | 약초 x3 | 1개 제작 | 1 |
| refine_ore_ingot | REFINING | 주괴 | 광석 x4 | 1개 제작 | 1 |
| refine_lumber_basic | REFINING | 가공 목재 | 목재 x3 | 1개 제작 | 1 |
| refine_resonance_powder | REFINING | 공명 분말 | 공명 광석 x3, 정제 약초 x1 | 1개 제작 | 5 |
| refine_frost_dust | REFINING | 빙결 가루 | 냉기 광석 x3, 설혼초 x1 | 1개 제작 | 5 |
| refine_flame_powder | REFINING | 화맥 분말 | 화맥석 x3, 홍염초 x1 | 1개 제작 | 5 |

---

## 2. 요리 제작식

| recipe_id | 분류 | 결과물 | 필요 재료 | 권장 수량 | 요구 숙련도 |
|---|---|---|---|---|---:|
| cook_healing_soup | COOKING | 회복 수프 | 생선 x2, 약초 x2 | 1개 제작 | 1 |
| cook_gatherer_lunchbox | COOKING | 채집꾼의 도시락 | 버섯 x2, 약초 x1, 생선 x1 | 1개 제작 | 2 |
| cook_frost_meal | COOKING | 설혼 방한식 | 설혼초 x2, 생선 x2, 정제 약초 x1 | 1개 제작 | 4 |
| cook_flame_meal | COOKING | 홍염 내열식 | 홍염초 x2, 생선 x2, 정제 약초 x1 | 1개 제작 | 4 |
| cook_scout_meal | COOKING | 정찰병 식사 | 생선 x1, 버섯 x2, 꽃 x1 | 1개 제작 | 3 |
| cook_survival_meal | COOKING | 생존 전투식 | 생선 x2, 버섯 x1, 정제 약초 x1 | 1개 제작 | 3 |

---

## 3. 만찬 제작식

| recipe_id | 분류 | 결과물 | 필요 재료 | 권장 수량 | 요구 숙련도 |
|---|---|---|---|---|---:|
| feast_capital_defense | FEAST | 제국 수비 만찬 | 생선 x4, 약초 x4, 정제 약초 x2, 주괴 x1 | 1회 | 8 |
| feast_forest_spirit | FEAST | 정령 숲 만찬 | 정령초 x4, 버섯 x3, 생선 x3, 정령 수액 x1 | 1회 | 10 |
| feast_frost_guard | FEAST | 설원의 방한 만찬 | 설혼초 x4, 생선 x4, 빙결 가루 x1, 빙결 핵편 x1 | 1회 | 10 |
| feast_flame_guard | FEAST | 홍염 대비 만찬 | 홍염초 x4, 생선 x4, 화맥 분말 x1, 용암 결정핵 x1 | 1회 | 10 |
| feast_explorer | FEAST | 탐험가의 성찬 | 생선 x5, 버섯 x3, 약초 x3, 진주 x1 | 1회 | 9 |

---

## 4. 회복 물약 제작식

| recipe_id | 분류 | 결과물 | 필요 재료 | 권장 수량 | 요구 숙련도 |
|---|---|---|---|---|---:|
| potion_heal_normal | ALCHEMY_HEAL | 일반 회복 물약 | 약초 x2, 정제 약초 x1 | 1개 제작 | 1 |
| potion_heal_advanced | ALCHEMY_HEAL | 고급 회복 물약 | 약초 x3, 정제 약초 x2, 광물 분말 x1 | 1개 제작 | 4 |
| potion_heal_rare | ALCHEMY_HEAL | 희귀 회복 물약 | 약초 x4, 정제 약초 x2, 정수 x1, 광물 분말 x1 | 1개 제작 | 8 |

---

## 5. 상태 대응 물약 제작식

| recipe_id | 분류 | 결과물 | 필요 재료 | 권장 수량 | 요구 숙련도 |
|---|---|---|---|---|---:|
| potion_frost_relief | ALCHEMY_RESIST | 냉기 완화 물약 | 설혼초 x2, 정제 약초 x1 | 1개 제작 | 4 |
| potion_flame_resist | ALCHEMY_RESIST | 화상 저항 물약 | 홍염초 x2, 정제 약초 x1 | 1개 제작 | 4 |
| potion_poison_relief | ALCHEMY_RESIST | 독 완화 물약 | 버섯 x2, 정제 약초 x1 | 1개 제작 | 4 |
| potion_bleed_relief | ALCHEMY_RESIST | 출혈 완화 물약 | 꽃 x2, 정제 약초 x1 | 1개 제작 | 4 |
| potion_erosion_relief | ALCHEMY_RESIST | 침식 완화제 | 정령초 x2, 정제 약초 x1, 정령 수액 x1 | 1개 제작 | 6 |

---

## 6. 전투 보조 물약 제작식

| recipe_id | 분류 | 결과물 | 필요 재료 | 권장 수량 | 요구 숙련도 |
|---|---|---|---|---|---:|
| potion_survival_tonic | ALCHEMY_COMBAT | 생존 강화 약제 | 약초 x2, 광물 분말 x1, 정제 약초 x1 | 1개 제작 | 5 |
| potion_focus_extract | ALCHEMY_COMBAT | 집중 추출액 | 정령초 x1, 공명 분말 x1, 정제 약초 x1 | 1개 제작 | 7 |
| potion_emergency_regen | ALCHEMY_COMBAT | 응급 재생 약제 | 약초 x2, 정수 x1, 정제 약초 x1 | 1개 제작 | 6 |
| potion_mobility_tonic | ALCHEMY_COMBAT | 긴급 기동 약제 | 꽃 x2, 정제 약초 x1, 진주 가루 x1 | 1개 제작 | 6 |

---

## 7. 제작식 운영 메모

### 제련/가공
- 기본 재료를 중간재로 올려 거래 가치를 만든다.

### 요리
- 개인 장기 버프
- 생존/저항/편의 중심

### 만찬
- 파티 준비용 고급 음식
- 요구 재료가 일반 음식보다 확실히 무거워야 한다.

### 연금
- 회복/상태 대응/전투 보조를 분리
- 1차는 딜 폭증보다 생존/대응 중심

---

## 8. 한 줄 요약

**1차 생활 제작식은 제련 6종, 요리 6종, 만찬 5종, 회복 물약 3종, 상태 대응 물약 5종, 전투 보조 물약 4종 정도로 시작하는 것이 가장 안정적이며, 일반 제작식은 가볍게, 만찬과 희귀 물약은 확실히 무겁게 설계하는 것이 좋다.**
