# 포로 서버 Citizens NPC seed 구조 초안

## 문서 목적
이 문서는 Citizens NPC를 인게임에서 하나씩 수동 생성하지 않고,
**seed 기반으로 자동 생성/갱신**하기 위한 구조 초안이다.

## 핵심 목표
- 수도, 지역, 보스 입장, 생활, 영지, 상점 NPC를 파일 기반으로 관리
- EmpireRPG가 Citizens API를 통해 NPC를 자동 생성/수정
- 이름, 위치, 스킨, 역할, 상호작용 연결을 한 구조에서 관리

## 추천 seed 필드
- npc_seed_id
- npc_master_id
- region_code
- town_id
- world_name
- x / y / z
- yaw / pitch
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
- notes

## 예시 seed
| npc_seed_id | npc_master_id | region | town | world | x | y | z | yaw | entity_type | display_name | role_type | interaction_profile_id | quest_start_id | beton_conversation_id |
|---|---|---|---|---|---:|---:|---:|---:|---|---|---|---|---|---|
| npc_capital_leonid_main | npc_capital_leonid | capital | capital_main | world_main | 120 | 65 | -40 | 180 | PLAYER | 레오니드 | main_story_npc | capital_main_hub | q_spawn_intro_01 | conv_capital_intro |
| npc_capital_estate_mgr | npc_capital_estate_mgr | capital | capital_main | world_main | 128 | 65 | -36 | 90 | PLAYER | 영지 관리관 | estate_unlock_npc | estate_unlock_profile | q_estate_unlock_01 | conv_estate_intro |
| npc_south_boss_gate | npc_south_gatekeeper | south | south_m_01 | world_main | 820 | 72 | 144 | 0 | PLAYER | 남부 결전 안내관 | boss_entry_npc | boss_entry_south | NONE | conv_south_boss_gate |

## 동기화 규칙 추천
- 서버 시작 시 seed 읽기
- 기존 Citizens NPC 조회
- 없으면 생성
- 있으면 이름/위치/스킨/trait 갱신
- should_spawn=false면 비활성 또는 제거

## 추천 trait 연결
- protection
- lookclose
- click command hook
- BetonQuest conversation trigger
- EmpireRPG interaction profile hook

## 한 줄 요약
**Citizens NPC는 위치, 이름, 스킨, 역할, 상호작용 연결을 담은 seed 구조로 관리하고, EmpireRPG가 서버 시작 시 자동 생성/갱신하게 만드는 것이 가장 효율적이다.**
