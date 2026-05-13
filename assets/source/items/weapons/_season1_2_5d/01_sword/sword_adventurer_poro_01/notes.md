# sword_adventurer_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 zip 수령 (adventurer_tools_v8.zip — crossbow 폴더 공유)
- [x] original/ 참조 (zip 원본은 crossbow/original/에 보관)
- [x] 모델 구조 분석 (adventurer_sword.json — 5 elements)
- [x] 텍스처 분석 (adventurer_sword.png, 정적 461B)
- [x] display 설정 확인 (thirdperson/firstperson/gui/ground/head/fixed)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록
- [x] 인게임 테스트 통과
- [x] status → active

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | sword_adventurer_poro_01 |
| display_name | 모험가의 검 |
| CMD | 100102 |
| material | NETHERITE_SWORD |
| rarity | starter |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | 없음 (정적 단일 PNG) |
| 원본 소스 | adventurer_tools_v8 (akaleaf / adventurer_tools) |
| mcmodels 키 | 제거 완료 |

## 모델 구조

- 5 elements (핸들 2 + 가드 1 + 날 1 + 폼멜 1)
- texture_size [64, 64]
- gui_light: front (평면 투영 방식)
- 텍스처 키 `"1"` + `"particle"` (Adventurer 세트 표준)

## 표시 설정 요약

- **ground**: rotation [-45°Z], scale [0.35, 0.35, 0.35] → 소형 바닥 드랍 ✅
- **gui**: rotation [-45°Z], scale [0.62, 0.62, 0.62] → 45° 검 아이콘 ✅
- **firstperson_righthand**: rotation [20, -90, 0], scale [0.6, 0.6, 0.6]
- **fixed**: rotation [0, -180, -45], scale [0.5, 0.5, 0.5] → ItemDisplay 활용 가능

## 후속 검토 항목

### Adventurer 시리즈 세트 완성 계획

- sword_adventurer_poro_01: **100102 등록 완료** ✅
- axe_adventurer_poro_01: 100202 예정 (MACE 매핑, 보류 중)
- spear_adventurer: GPT img2 또는 별도 에셋 검토 필요
- staff_adventurer: GPT img2 또는 별도 에셋 검토 필요
- crossbow_adventurer_poro_01: 100401 기등록 ✅

## 인게임 테스트 결과

- 리소스팩 적용 성공
- `/minecraft:give` 명령어로 지급 성공
- 인벤토리 아이콘 정상
- 3인칭 손持 모델 정상
- starter/common 검으로 사용 가능
- Vanquisher 검보다 수수해서 초기 지급용으로 적합
- crossbow_adventurer_poro_01과 함께 Adventurer starter 세트 후보로 분류

## 이슈 로그

(없음)
