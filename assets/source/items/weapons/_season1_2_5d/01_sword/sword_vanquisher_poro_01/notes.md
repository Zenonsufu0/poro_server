# sword_vanquisher_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 zip 수령 (Vanquisher_Sword-4fll7y.zip)
- [x] 00_incoming_external/ 배치
- [x] original/ 압축 해제 복사
- [x] 모델 구조 분석 (.json 파일 확인)
- [x] 텍스처 레이아웃 분석 (.png 파일 확인)
- [x] display 설정 확인 (firstperson/thirdperson/gui/ground)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록
- [x] 인게임 테스트 (손/인벤토리/드롭/땅)
- [x] CMD 100101 최종 확인

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | sword_vanquisher_poro_01 |
| display_name | 정복자의 검 |
| CMD | 100101 |
| material | NETHERITE_SWORD |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | optional |

## 인게임 테스트 결과 (2026-05-08)

- 리소스팩 적용 성공
- `/give` 명령어(`custom_model_data={strings:["100101"]}`)로 지급 성공
- 인벤토리 아이콘 정상 (gui display 적용 확인)
- 3인칭 손持 모델 정상 (thirdperson display 적용 확인)
- **1차 검 치장 후보로 사용 가능** — 포로 서버 2D~2.5D 치장 무기 파이프라인 첫 번째 성공 사례

## 이슈 로그

(없음)
