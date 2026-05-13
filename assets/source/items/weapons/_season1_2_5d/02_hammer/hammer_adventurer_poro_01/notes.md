# hammer_adventurer_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 zip 수령 (adventurer_tools_v8.zip — crossbow 폴더 공유)
- [x] original/ 참조 (zip 원본은 crossbow/original/에 보관)
- [x] 모델 구조 분석 (adventurer_axe.json — 9 elements)
- [x] 텍스처 분석 (adventurer_axe.png, 정적 523B)
- [x] display 설정 확인 (thirdperson/firstperson/gui/ground/head/fixed)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록
- [x] 인게임 테스트 통과
- [x] status → active

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | hammer_adventurer_poro_01 |
| display_name | 모험가의 손도끼 (임시) |
| CMD | 100202 |
| material | MACE |
| rarity | starter |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | 없음 (정적 단일 PNG) |
| 원본 소스 | adventurer_tools_v8 (akaleaf / adventurer_axe) |
| mcmodels 키 | 제거 완료 |

## 모델 구조

- 9 elements (도끼 헤드 + 날 + 자루 구조)
- texture_size [64, 64]
- gui_light: front (평면 투영 방식)
- 텍스처 키 `"1"` + `"particle"` (Adventurer 세트 표준)

## 표시 설정 요약

| 뷰 | rotation | translation | scale | 비고 |
|---|---|---|---|---|
| firstperson_right | [20, -90, 0] | [2, 3.25, 0] | [0.6, 0.6, 0.6] | Y=3.25 — 도끼 헤드 위치 자연스러움 |
| thirdperson_right | [-10, -90, 0] | [0, 3, 0.25] | [0.6, 0.6, 0.6] | 검과 동일한 측면 거치 |
| ground | [0, 0, -45] | [0, 3, 0] | [0.35, 0.35, 0.35] | 소형 바닥 드랍 ✅ |
| gui | [0, 0, -45] | [2, 0.35, 0] | [0.635, 0.635, 0.635] | 45° 도끼 아이콘 ✅ |
| fixed | [0, -180, -45] | [-1.75, 0.5, 0.75] | [0.575, 0.575, 0.575] | ItemDisplay 활용 가능 |

## MACE fallback 검토 기록

- **1차**: MACE / CMD 100202 테스트
- **MACE 어색 기준**: 1인칭 스윙 시 타격 모션이 도끼형 무기와 크게 어긋나 시각적으로 부자연스럽다고 판단될 때
- **fallback**: NETHERITE_AXE로 material 변경
  - CMD 100202 번호는 유지
  - spec.yaml material 변경 + netherite_axe.json 신규 생성 필요
  - mace.json의 100202 case 제거 후 netherite_axe.json에 재등록
- **이번 단계에서 fallback 전환하지 않음** — 인게임 테스트 결과 확인 후 판단

## 세트 포지션

- sword_adventurer_poro_01 (100102, NETHERITE_SWORD) — starter 검 ✅ active
- **hammer_adventurer_poro_01 (100202, MACE)** — starter 전사 무기 후보
- crossbow_adventurer_poro_01 (100401, CROSSBOW) — starter 원거리 ✅ active

## 인게임 테스트 결과

- 리소스팩 적용 성공
- `/minecraft:give` 명령어로 지급 성공
- 인벤토리 아이콘 정상
- 3인칭 손持 모델 정상
- MACE 기반 표시 정상
- 순수 망치보다는 손도끼/전투도끼 인상이 강함
- 하지만 starter/common 전사 무기 후보로 사용 가능
- display_name은 임시로 "모험가의 손도끼" 유지
- Carnivoret 망치보다 수수해서 초기 지급용으로 적합

## 이슈 로그

(없음)
