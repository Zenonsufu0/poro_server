# 포로 서버 NPC / 지역 / 마을 master 표 초안

## 문서 목적
이 문서는 NPC, 지역, 마을을 각각 master 데이터로 관리하기 위한 초안이다.

핵심 목표:
- 지역과 마을, NPC를 하드코딩하지 않고 마스터 테이블로 분리한다.
- 자작령/백작령/후작령 구조와 지역 테마를 데이터로 관리한다.
- Codex가 region/town/npc registry를 만들 수 있게 한다.

---

## 1. region master 추천 필드
- `region_code`
- `region_name`
- `theme_type`
- `is_capital_related`
- `notes`

## 2. town master 추천 필드
- `town_id`
- `region_code`
- `town_grade`  // viscount/count/marquis
- `town_name`
- `has_storage`
- `has_blacksmith`
- `has_life_support`
- `has_boss_unlock_npc`

## 3. npc master 추천 필드
- `npc_id`
- `region_code`
- `town_id`
- `npc_name`
- `npc_role_type`
- `interaction_profile_id`
- `is_main_story_npc`
- `notes`

## 4. 예시
| region_code | region_name | theme_type |
|---|---|---|
| south | 남부 | volcano |
| north | 북부 | frost |
| west | 서부 | desert_storm |
| east | 동부 | forest_spirit |
| arkanon | 아르카논 | magitech |

| town_id | region_code | town_grade | town_name | storage | life_support | boss_unlock |
|---|---|---|---|---|---|---|
| south_v_01 | south | viscount | 화맥 외곽 전초 | true | false | false |
| south_c_01 | south | count | 화맥 정련 거점 | true | true | false |
| south_m_01 | south | marquis | 남부 중앙 성채 | true | true | true |

| npc_id | region_code | town_id | npc_name | npc_role_type |
|---|---|---|---|---|
| npc_south_marcos | south | south_c_01 | 마르코스 | magma_watcher |
| npc_capital_leonid | capital | capital_main | 레오니드 | imperial_envoy |
| npc_sahar_zafar | sahar | sahar_m_01 | 자파르 | contract_broker |

## 5. 한 줄 요약

**지역/마을/NPC를 각각 master 테이블로 분리하면 자작령/백작령/후작령 구조, 지역 테마, NPC 기능을 데이터 기반으로 안정적으로 관리할 수 있다.**
