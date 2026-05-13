# prop_chest_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 rar 수령 (Chests_Pack-p7uqtu.rar)
- [x] original/ 배치
- [x] 모델 구조 분석 (normal_chest.json — 12 elements)
- [x] 텍스처 분석 (prop_chest_poro_01.png, 정적)
- [x] display 설정 확인 (firstperson/thirdperson/gui/ground/head/fixed)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록
- [x] 인게임 테스트 통과 (인벤토리/손/바닥 드랍)
- [x] CMD 200102 최종 확인

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | prop_chest_poro_01 |
| display_name | 낡은 보관 상자 |
| CMD | 200102 |
| material | PAPER |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | 없음 (정적 단일 PNG) |
| 원본 소스 | Chests_Pack-p7uqtu / normal_chest |
| mcmodels 키 | 없음 (제거 불필요) |

## 표시 설정 요약

- **ground**: translation [0, 0.25, 0] + scale [0.5, 0.5, 0.5] → 바닥 밀착, 소품 크기 ✅
- **gui**: rotation [33, 139, 0] → 등각 투시 상자 아이콘 ✅
- **firstperson**: scale 없음 (기본 1,1,1) → 손持 시 크게 보일 수 있음 △
- **fixed**: rotation [-90, 0, 0], scale [3, 3, 3] → ItemDisplay 향후 활용 (눕혀진 방향 주의)

## 동일 팩 보상 상자 등급 후보 (미등록)

| 자산 | CMD 예정 | 비고 |
|---|---|---|
| medium_chest | 200103 | 중급 보상 상자 후보 |
| premium_chest | 200104 | 고급 보상 상자 후보 |

> 1차 테스트 통과 후 보상 상자 등급 시스템과 연계 검토. 현재는 후보로만 기록.

## 후속 검토 항목

### firstperson scale 조정 (2차 단계)

- 현재 firstperson scale 없음 → 손持 시 상자가 크게 보일 수 있음
- 1차 테스트 후 실제 크기 확인, 필요 시 `firstperson.scale [0.5, 0.5, 0.5]` 추가 검토

### fixed rotation 방향 (ItemDisplay 설치형, 2차 단계)

- `fixed.rotation [-90, 0, 0]` → ItemDisplay 배치 시 상자가 눕혀진 방향으로 보일 수 있음
- 설치형 소품 구현 전 실제 방향 확인 및 rotation 조정 필요

### 저장고/보상 상자 기능 (2차 단계)

- 우클릭 열기, 영지 권한 체크, 아이템 저장 → EmpireRPG 연동
- 실제 블럭 설치, ItemDisplay 배치 → 소품 플러그인 또는 EmpireRPG 연동

## 이슈 로그

(없음)
