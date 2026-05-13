# crossbow_adventurer_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 zip 수령 (adventurer_tools_v8.zip)
- [x] original/ 배치
- [x] 모델 구조 분석 (6종 JSON 확인)
- [x] 텍스처 분석 (crossbow_adventurer_poro_01.png, 정적)
- [x] display 설정 확인 (firstperson/thirdperson/gui/ground/head/fixed)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록 (정적 모델 1단계)
- [x] 인게임 테스트 (손/인벤토리/3인칭)
- [x] CMD 100401 최종 확인
- [ ] pulling/charged 상태 모델 연결 (2차 단계)

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | crossbow_adventurer_poro_01 |
| display_name | 모험가의 석궁 |
| CMD | 100401 |
| material | CROSSBOW |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | 없음 (정적 단일 PNG) |
| 원본 제작자 | akaleaf (adventurer_tools_v8) |

## 인게임 테스트 결과 (2026-05-08)

- 리소스팩 적용 성공
- `/minecraft:give @s crossbow[custom_model_data={strings:["100401"]}]` 명령어로 지급 성공
- 인벤토리 아이콘 정상
- 3인칭 손持 모델 정상
- 초보자/기본 석궁 치장 후보로 사용 가능
- 대표 고급 치장보다는 초반 보상용으로 적합

## 이슈 로그

(없음)
