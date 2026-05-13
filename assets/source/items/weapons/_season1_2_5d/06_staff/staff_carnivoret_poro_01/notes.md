# staff_carnivoret_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 zip 수령 (Carnivoret Weapon Set Volume 1.zip)
- [x] original/ 배치
- [x] 모델 구조 분석 (staff.json — 32 elements)
- [x] 텍스처 분석 (staff_carnivoret_poro_01.png, 정적)
- [x] display 설정 확인 (firstperson/thirdperson/gui/ground/head/fixed)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록
- [x] 인게임 테스트 (손/인벤토리/3인칭)
- [x] CMD 100601 최종 확인

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | staff_carnivoret_poro_01 |
| display_name | 포식자의 스태프 (임시) |
| CMD | 100601 |
| material | BLAZE_ROD |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | 없음 (정적 단일 PNG) |
| 원본 제작자 | elitecreatures (Carnivoret Weapon Set) |
| 텍스처 키 | `"2"` 단독 (hammer/spear/scythe의 `"1"+"particle"` 패턴과 다름) |

## 후속 검토 항목

### BLAZE_ROD 아이템 동작 (2차 단계)

- BLAZE_ROD는 용광로 연료로 사용 가능한 소모성 아이템
- 치장 무기로 운용 시 우클릭 직접 상호작용은 없으나, 연료 슬롯에 삽입되어 소모될 수 있음
- 실제 서버 운영 시 EmpireRPG에서 아이템 소모 이벤트 intercept 또는 커스텀 material 대체 방안 검토 필요
- 검토 시점: 스태프 전투 스킬 구현 단계 또는 2차 스태프 파이프라인 작업 시

## 인게임 테스트 결과 (2026-05-08)

- 리소스팩 적용 성공
- `/minecraft:give @s blaze_rod[custom_model_data={strings:["100601"]}]` 명령어로 지급 성공
- 인벤토리 아이콘 정상
- 3인칭 손持 모델 정상
- 스태프 치장으로 실루엣과 가독성이 좋음
- 중급~고급 스태프 치장 후보로 사용 가능
- BLAZE_ROD 기본 아이템 사용감/상호작용은 후속 검토 항목으로 유지

## 이슈 로그

(없음)
