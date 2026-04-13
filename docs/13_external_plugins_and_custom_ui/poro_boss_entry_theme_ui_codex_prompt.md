# 포로 서버 Codex 프롬프트 - 테마 허브형 보스 입장 커스텀 UI

첨부한 `poro_server_docs_structured_final.zip`을 설계 기준 문서로 삼아라.

현재 방향:
- BetonQuest 제거
- Citizens는 NPC 실체/배치만 담당
- EmpireRPG가 모든 상호작용 UI를 담당
- 보스 입장도 EmpireRPG 커스텀 UI로 처리

이번 작업 목표:
보스 입장 UI를 **난이도별 허브가 아니라 테마별 허브 구조**로 구현해라.

## 반드시 반영할 원칙
- 1차 허브는 테마 선택
- 2차 화면은 해당 테마의 보스 카드 목록
- 3차 화면은 개별 보스 상세/입장 화면
- 난이도는 탭이나 필터로 보조 표시
- 테마성이 보스 입장 경험의 중심이 되어야 함

## 구현할 것
1. BossEntryThemeHubUiService
2. BossEntryBossListUiService
3. BossEntryDetailUiService
4. BossEntryUiStateStore
5. BossEntryThemeRegistry / BossEntryUiProfileRegistry
6. Citizens NPC 클릭 연동

## UI 구조
- 1단계: 테마 허브
  - 남부 / 북부 / 서부 / 동부 / 사하르 / 아르카논 / 수도
- 2단계: 보스 카드 목록
  - 필드 / 심층 / 결전 / 극상위 필터
- 3단계: 보스 상세
  - 설명 / 조건 / 권장 파티 / 보상 / 입장 버튼

## 제약
- 기존 BossEntryValidator, BossRunService와 연동할 것
- 테마 허브 중심 구조를 유지할 것
- 하드코딩보다 seed/profile/registry 기반 우선
- UI는 향후 리소스팩/커스텀 아이콘 붙이기 쉽게 설계할 것
- 현재 단계에서는 남부/아르카논 2테마만 샘플로 구현해도 됨

## 샘플 구현 범위
- 테마 허브 2개 이상
- 남부 보스 카드 2개 이상
- 아르카논 보스 카드 1개 이상
- 상세 화면 2개 이상
- 입장 버튼 -> 기존 boss entry validate 연동 예시

## 산출물
- 패키지 구조
- 핵심 클래스 코드
- 샘플 seed 파일
- Citizens 클릭 -> theme hub -> boss list -> detail -> validate 흐름 설명
- 향후 강화/잠재/전승 UI와 공통 UI 프레임으로 묶을 수 있는 확장 포인트 설명

## 참조 문서
- poro_boss_entry_theme_hub_ui_draft.md
- poro_boss_entry_ui_seed_and_flow_draft.md
- poro_boss_entry_and_record_structure.md
- poro_boss_material_to_t2_mapping_table.md
- poro_hall_of_fame_ui_structure.md
- poro_open_access_and_low_restriction_principles.md
