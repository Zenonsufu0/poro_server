# 포로 서버 Codex 프롬프트 - Citizens NPC 자동 생성 / 동기화

첨부한 `poro_server_docs_structured_final.zip`을 설계 기준 문서로 삼아라.

현재 repo의 EmpireRPG 위에,
Citizens NPC를 수동 배치 대신 seed 기반으로 자동 생성/동기화하는 시스템을 구현해라.

## 목표
수도/지역/생활/영지/보스 입장 NPC를
seed 파일 기준으로 Citizens API를 통해 자동 생성/수정/동기화한다.

## 이번 작업에서 꼭 구현할 것
1. NpcSeedRecord 또는 CitizensNpcSeed DTO
2. CitizensNpcSeedLoader
3. CitizensNpcSyncService
   - seed 로드
   - 기존 NPC 조회
   - 없으면 생성
   - 있으면 이름/위치/스킨/trait 갱신
4. CitizensNpcTraitBinder
   - protection
   - lookclose
   - interaction profile hook
   - BetonQuest conversation hook
5. NpcSyncBootstrap
   - plugin enable 이후 sync 진입점

## 추천 seed 필드
- npc_seed_id
- npc_master_id
- region_code
- town_id
- world_name
- x
- y
- z
- yaw
- pitch
- entity_type
- display_name
- skin_type
- skin_value
- role_type
- interaction_profile_id
- quest_start_id
- beton_conversation_id
- is_protected
- should_spawn

## 역할 분리 규칙
- Citizens는 NPC 실체만 담당
- EmpireRPG는 NPC 정의와 interaction profile 기준 담당
- BetonQuest는 conversation 연결만 담당
- 퀘스트 상태 저장/보상 지급은 Citizens 또는 BetonQuest에서 처리하지 말 것

## 제약
- 운영용 NPC를 수동 저장 파일 편집에 의존하지 말 것
- seed 기준 재동기화 가능 구조로 만들 것
- orphan NPC 탐지 로그 제공 가능하면 좋음
- 클릭 이벤트 중복 실행 방지 고려

## 산출물
- 패키지 구조
- 핵심 클래스 코드
- sample npc seed 파일
- 수도 NPC 2개, 지역 NPC 1개 예시 동기화 설명
- 기존 repo에서 어디에 붙였는지 설명

## 참조 문서
- poro_citizens_npc_seed_structure_draft.md
- poro_npc_region_town_master_tables.md
- poro_shared_npc_function_matrix.md
- poro_story_quest_system_outline.md
