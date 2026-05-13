# hammer_carnivoret_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 zip 수령 (Carnivoret Weapon Set Volume 1.zip)
- [x] original/ 배치
- [x] 모델 구조 분석 (hammer.json — 72 elements)
- [x] 텍스처 분석 (hammer_carnivoret_poro_01.png, 정적)
- [x] display 설정 확인 (firstperson/thirdperson/gui/ground/head/fixed)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록
- [x] 인게임 테스트 (손/인벤토리/3인칭)
- [x] CMD 100201 최종 확인

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | hammer_carnivoret_poro_01 |
| display_name | 포식자의 망치 |
| CMD | 100201 |
| material | MACE |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | 없음 (정적 단일 PNG) |
| 원본 제작자 | elitecreatures (Carnivoret Weapon Set) |

## 인게임 테스트 결과 (2026-05-08)

- 리소스팩 적용 성공
- `/minecraft:give @s mace[custom_model_data={strings:["100201"]}]` 명령어로 지급 성공
- 인벤토리 아이콘 정상
- 3인칭 손持 모델 정상
- 망치 치장으로 가독성과 존재감이 좋음
- 포로 서버 중급~고급 망치 치장 후보로 사용 가능

## 이슈 로그

(없음)
