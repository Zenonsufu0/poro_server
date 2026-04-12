# 포로 서버 T2 장비 제작 recipe seed 표 초안

## 문서 목적
이 문서는 T2 장비 제작을 실제 recipe seed 데이터로 관리하기 위한 초안이다.

핵심 목표:
- T2 무기/방어구/장신구 제작식을 표준화한다.
- 보스 고유 재료, 공용 결전 재료, 골드, 보조 재료를 조합한다.
- Codex가 crafting recipe registry를 만들 수 있게 한다.

---

## 1. 추천 필드

- `recipe_id`
- `result_item_id`
- `recipe_category`
- `boss_material_id`
- `boss_material_amount`
- `common_duel_material_amount`
- `gold_cost`
- `support_material_id`
- `support_material_amount`
- `is_tradeable_result`
- `notes`

---

## 2. 예시

| recipe_id | result_item_id | category | boss_material_id | boss_amt | common_amt | gold_cost | support_material_id | support_amt |
|---|---|---|---|---:|---:|---:|---|---:|
| t2_gs_ragnes_01 | t2_greatsword_ragnes | weapon | lava_core | 4 | 12 | 32000 | flame_powder | 2 |
| t2_armor_morvain_01 | t2_armor_morvain | armor | frost_core_piece | 3 | 9 | 20000 | frost_dust | 1 |
| t2_ring_hazard_01 | t2_ring_hazard | accessory | storm_crystal | 2 | 6 | 12000 | sand_resin | 1 |

---

## 3. 한 줄 요약

**T2 제작식은 boss material + common duel material + gold + support material 조합으로 관리하고, 무기는 무겁게, 방어구는 중간, 장신구는 비교적 가볍게 두는 recipe seed 구조가 가장 적절하다.**
