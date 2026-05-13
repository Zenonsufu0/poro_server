# prop_barrel_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 zip 수령 (BarrelKnight_1.1.zip)
- [x] original/ 배치
- [x] 모델 구조 분석 (barrel.json — 18 elements)
- [x] 텍스처 분석 (prop_barrel_poro_01.png, 정적)
- [x] display 설정 확인 (firstperson/thirdperson/gui/ground/head/fixed)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록
- [x] 인게임 테스트 실시 (인벤토리/손/바닥 드랍)
- [~] CMD 200101 — 소품 용도 기각, 몬스터 장비 재분류 대기

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | prop_barrel_poro_01 |
| display_name | 낡은 공방 나무통 |
| CMD | 200101 |
| material | PAPER |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | 없음 (정적 단일 PNG) |
| 원본 제작자 | BarrelKnight_1.1 (HMCCosmetics 전용) |
| 원본 설계 용도 | head cosmetics (플레이어 머리 코스튬) |

## 표시 설정 주의 사항

- **ground**: scale 없음 (기본 1,1,1) + `translation [0, 22, 0]` → 바닥 드랍 시 약간 부유 가능
- **thirdperson**: scale 없음 (기본 1,1,1) → 3인칭 손持 시 나무통이 크게 보일 수 있음
- **fixed (ItemDisplay)**: scale [2, 2, 2] → 추후 설치형 소품 활용 가능한 값

## 후속 검토 항목

### ground display 조정 (2차 단계)

- 바닥 드랍 테스트 후 오브젝트 부유 여부 확인
- 필요 시 `ground.translation` 값 낮추거나 `ground.scale` 추가 조정
- 또는 `barrel_thirdperson.json` 활용 검토 (원저자가 3인칭 전용 모델로 별도 제공)

### ItemDisplay 설치형 소품 (2차 단계)

- `fixed` display scale [2, 2, 2] 기준으로 ItemDisplay 엔티티 배치 검토
- 영지 권한 체크, 설치/제거 상호작용, 우클릭 배치 → EmpireRPG 또는 별도 소품 플러그인 연동 필요
- 실제 블럭 설치 구현은 이 단계에서 제외

### PAPER material 특성

- PAPER는 소모성 아이템이나 우클릭 상호작용 없음 → 소품 아이템으로 가장 안전한 재료 선택
- 인챈팅, 제작 레시피 등 충돌 가능성 낮음
- 추후 다수 소품 CMD 등록 시 동일 paper.json에 cases 항목 append 방식으로 확장

## 인게임 테스트 결과 (2026-05-08) — 소품 용도 기각

- 소품 barrel(낡은 공방 나무통) 용도로는 부적합
- 인게임에서 나무 갑옷 / 배럴 나이트 장비 / 목제 기사 헬멧처럼 보임
- 영지 바닥 오브젝트 느낌이 아닌 장착 장비 인상이 강함
- 몬스터 장비 / 훈련장 장식 / 목제 기사 연출용으로는 적합
- status: rejected_candidate → 몬스터 장비 재분류 대기
- export/resourcepack 파일은 삭제하지 않고 유지 (재활용 예정)

## 재분류 확정 (2026-05-08)

- player 뒤태에서 barrel backpack(등짐형 나무통)처럼 보임
- skeleton 장착 시 barrel knight 몬스터로 자연스럽게 연출됨
- prop(바닥 오브젝트)보다는 mob cosmetic(몬스터 장비)으로 적합
- **확정 asset_id**: `mob_barrelknight_body_01`
- **구현 슬롯**: HELMET (head slot) — Minecraft 장비 슬롯 제약
- **콘텐츠 분류**: body armor / backpack / barrel armor (시각적 역할)
  - 슬롯명(HELMET)과 콘텐츠 분류(body armor)를 분리 기록
- 관련 CMD: 200101 재사용
- mob_equipment 레지스트리에 재등록 예정

## 이슈 로그

(없음)
