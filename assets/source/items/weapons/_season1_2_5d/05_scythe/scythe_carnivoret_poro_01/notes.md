# scythe_carnivoret_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 zip 수령 (Carnivoret Weapon Set Volume 1.zip)
- [x] original/ 배치
- [x] 모델 구조 분석 (scythe.json — 36 elements)
- [x] 텍스처 분석 (scythe_carnivoret_poro_01.png, 정적)
- [x] display 설정 확인 (firstperson/thirdperson/gui/ground/head/fixed)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록
- [x] 인게임 테스트 (손/인벤토리/3인칭)
- [x] CMD 100501 최종 확인

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | scythe_carnivoret_poro_01 |
| display_name | 포식자의 낫 (임시) |
| CMD | 100501 |
| material | NETHERITE_HOE |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | 없음 (정적 단일 PNG) |
| 원본 제작자 | elitecreatures (Carnivoret Weapon Set) |

## 후속 검토 항목

### NETHERITE_HOE 우클릭 상호작용 (2차 단계)

- NETHERITE_HOE material은 흙 블록 우클릭 시 경작지 변환 동작 발생
- 1차 테스트에서는 정적 표시 확인만 진행하며 상호작용은 무시
- 실제 서버 운영 시 EmpireRPG에서 right-click 이벤트를 intercept하거나 커스텀 material로 대체하는 방안 검토 필요
- 검토 시점: 낫 전투 스킬 구현 단계 또는 2차 낫 파이프라인 작업 시

## 인게임 테스트 결과 (2026-05-08)

- 리소스팩 적용 성공
- `/minecraft:give @s netherite_hoe[custom_model_data={strings:["100501"]}]` 명령어로 지급 성공
- 인벤토리 아이콘 정상
- 3인칭 손持 모델 정상
- 낫 치장으로 실루엣과 가독성이 좋음
- 중급~고급 낫 치장 후보로 사용 가능
- 추후 GPT img2로 대표 희귀 낫을 별도 제작하는 방향 유지
- NETHERITE_HOE 우클릭 상호작용은 후속 검토 항목으로 유지

## 이슈 로그

(없음)
