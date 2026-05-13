# spear_carnivoret_poro_01 — 작업 노트

## 진행 상태 체크리스트

- [x] 원본 zip 수령 (Carnivoret Weapon Set Volume 1.zip)
- [x] original/ 배치
- [x] 모델 구조 분석 (spear.json — 30 elements)
- [x] 텍스처 분석 (spear_carnivoret_poro_01.png, 정적)
- [x] display 설정 확인 (firstperson/thirdperson/gui/ground/head/fixed)
- [x] mapping_plan.md 완성
- [x] 텍스처 준비 (poro namespace 경로 대응)
- [x] export/resourcepack 등록
- [x] 인게임 테스트 (손/인벤토리/3인칭)
- [x] CMD 100301 최종 확인

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | spear_carnivoret_poro_01 |
| display_name | 포식자의 창 |
| CMD | 100301 |
| material | TRIDENT |
| pipeline | existing_json_png_import |
| Blockbench 필요 | 불필요 (원본 json 직접 사용) |
| 텍스처 수정 | 없음 (정적 단일 PNG) |
| 원본 제작자 | elitecreatures (Carnivoret Weapon Set) |

## 후속 검토 항목

### TRIDENT 투척 모션 (2차 단계)

- TRIDENT material은 우클릭 시 투척 → 별도 throwing 애니메이션 트리거
- 현재 등록된 모델은 정적 단일 모델이므로, 투척 중 시각적 어색함 발생 가능
- Minecraft 1.21.4+ item definition 포맷에서 `minecraft:throwing` 조건 지원 여부 확인 필요
- 검토 시점: 2차 창 치장 파이프라인 또는 TRIDENT 전용 상태 모델 작업 시

## 인게임 테스트 결과 (2026-05-08)

- 리소스팩 적용 성공
- `/minecraft:give @s trident[custom_model_data={strings:["100301"]}]` 명령어로 지급 성공
- 인벤토리 아이콘 정상
- 3인칭 손持 모델 정상
- 순수한 창보다는 폴암/할버드형 장병기 인상이 있음
- 포로 서버 창 카테고리 치장으로 사용 가능
- 중급~고급 창 치장 후보로 적합
- 기본 표시명은 임시로 "포식자의 창" 유지
- 추후 유저 커스텀 이름/치장 이름 변경 기능 검토 가능
- TRIDENT 투척/특수 모션은 후속 검토 항목으로 유지

## 이슈 로그

(없음)
