# 포로 서버 퀘스트 seed / config 표 초안

## 문서 목적
이 문서는 퀘스트 시스템을 데이터 기반으로 관리하기 위한 seed/config 표 초안이다.

핵심 목표:
- 메인/서브/반복/생활/영지 해금 퀘스트를 동일한 구조로 관리한다.
- Codex가 quest registry / condition / reward resolver를 만들 수 있게 한다.

---

## 1. 추천 필드

- `quest_id`
- `quest_type`
- `quest_name`
- `region_code`
- `giver_npc_id`
- `unlock_condition_type`
- `unlock_condition_value`
- `objective_type`
- `objective_target_id`
- `objective_amount`
- `reward_type`
- `reward_target_id`
- `reward_amount`
- `next_quest_id`

---

## 2. 예시

| quest_id | quest_type | quest_name | region | giver_npc | objective_type | objective_target | amount | reward_type | reward_target | reward_amount | next |
|---|---|---|---|---|---|---|---:|---|---|---:|---|
| q_spawn_intro_01 | MAIN | 제국의 부름 | capital | npc_leonid | talk | npc_leonid | 1 | gold | gold | 300 | q_spawn_intro_02 |
| q_life_intro_01 | SIDE | 약초의 쓰임 | capital | npc_supply_01 | gather | herb_basic | 3 | item | refined_herb | 1 | q_life_intro_02 |
| q_estate_unlock_01 | SIDE | 개인 거점 허가 | capital | npc_estate_mgr | collect | monster_residue | 5 | unlock | estate_access | 1 | q_estate_unlock_02 |
| q_daily_east_01 | DAILY | 침식 개체 처리 | east | npc_east_board | kill | east_corrupted_mob | 10 | gold | gold | 700 | NONE |

---

## 3. 한 줄 요약

**퀘스트는 seed/config 테이블로 관리해 quest_id, quest_type, giver NPC, 목표, 보상, 다음 퀘스트를 연결하면 메인/서브/일일/생활/영지 라인을 모두 일관되게 다룰 수 있다.**
