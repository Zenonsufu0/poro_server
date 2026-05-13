# spear_tempest_poro_02 — Finalized

**Status:** Finalized (2026-05-12)
**Category:** 03_spear (창계열 치장무기)

---

## Identity

| 항목 | 값 |
|---|---|
| Model ID | `spear_tempest_poro_02` |
| Model path | `poro:item/weapons/spear_tempest_poro_02` |
| CMD | `100304` |
| Base item | `minecraft:trident` |
| Registry file | `assets/export/resourcepack/assets/minecraft/items/trident.json` |

---

## Export paths

| 파일 | 경로 |
|---|---|
| bbmodel (source) | `assets/source/items/weapons/_season1_2_5d/03_spear/spear_tempest_poro_02/source/spear_tempest_poro_02.bbmodel` |
| model JSON | `assets/export/resourcepack/assets/poro/models/item/weapons/spear_tempest_poro_02.json` |
| blade texture | `assets/export/resourcepack/assets/poro/textures/item/weapons/spear_tempest_poro_02_blade_alpha.png` |
| energy texture | `assets/export/resourcepack/assets/poro/textures/item/weapons/spear_tempest_poro_02_energy_alpha.png` |
| darkmetal texture | `assets/export/resourcepack/assets/poro/textures/item/weapons/spear_tempest_poro_02_darkmetal.png` |
| shaft texture | `assets/export/resourcepack/assets/poro/textures/item/weapons/spear_tempest_poro_02_shaft.png` |

---

## Element structure (14 elements)

| Index | Name | From | To | Active faces |
|---|---|---|---|---|
| 0 | pommel_tip | [7.5,-6,7.5] | [8.5,-5,8.5] | 6면 |
| 1 | pommel_body | [7,-5,7] | [9,-2,9] | 6면 |
| 2 | shaft_main | [7.5,-2,7.5] | [8.5,11,8.5] | 6면 |
| 3 | shaft_collar | [7,10.5,7] | [9,11.5,9] | 6면 |
| 4 | core_arm_left | [2,11,7] | [5.5,14,9] | 6면 |
| 5 | core_arm_right | [10.5,11,7] | [14,14,9] | 6면 |
| 6 | core_main | [5.5,11,6.5] | [10.5,15,9.5] | 6면 |
| 7 | core_gem | [7.5,12,6] | [8.5,14,6.5] | 6면 |
| 8 | core_collar_top | [6.5,15,7] | [9.5,16,9] | 6면 |
| 9 | blade_back_plate | [5,16,8] | [11,32,8.125] | north/south only |
| 10 | fin_left_back_plate | [2,12,8] | [8,22,8.125] | 6면 |
| 11 | fin_right_back_plate | [8,12,8] | [14,22,8.125] | 6면 |
| 12 | fin_left_back_plate (소형 우) | [8,6,7.97] | [12,11,8.0] | north/south only |
| 13 | fin_left_back_plate (소형 좌) | [4,6,7.97] | [8,11,8.0] | north/south only |

---

## Design decisions

- **blade_back_plate**: north/south face만 활성화 (east/west/up/down 제거). 두께 0.125u, blade_alpha 텍스처 사용.
  - side face를 유지하면 텍스처 col 8의 순수 백색 픽셀이 흰 심(seam)으로 렌더링됨 → face null 처리로 해결.
- **상위 fin 두 개 (10/11)**: 6면 유지, 0.125u 두께, energy_alpha 텍스처.
- **소형 청색 날개 두 개 (12/13)**: bbmodel 내부 -180° 회전 → Minecraft 비호환. 좌표 베이킹으로 회전 제거.
  - 12(우): from=[8,6,7.97] to=[12,11,8.0], x=8 기준 우측
  - 13(좌): from=[4,6,7.97] to=[8,11,8.0], x=8 기준 좌측 대칭 미러
  - UV: U-flip 적용으로 좌우 대칭 텍스처
- **pommel/shaft/socket/core**: 2.5D 큐브 구조, 수정 없음.
- **display**: Blockbench에서 수동 조정 완료, AI 자동 수정 금지.

---

## Validation

- `bad_uv_count`: 0
- export elements: 14 (source와 일치)
- rotation key in export: 없음 (모든 rotation 베이킹 완료)
- CMD 100304: trident.json 등록 완료

---

## Do not

- 블레이드/핀을 chunky 큐브로 되돌리지 말 것
- display 자동 덮어쓰기 금지
- 소형 날개(12/13)에 회전 값 재적용 금지 (베이킹 완료 상태)
- east/west face를 blade_back_plate에 재추가하면 흰 심 재발함

---

## Pipeline pattern (후속 spear 참고)

창계열 치장무기의 표준 구조:
- 블레이드/에너지핀: 2D alpha plate (north/south-only, 두께 0.03~0.125u)
- 소켓/코어/손잡이: 2.5D 큐브 디테일
- Minecraft 비호환 rotation은 반드시 베이킹(좌표 직접 계산)으로 처리
