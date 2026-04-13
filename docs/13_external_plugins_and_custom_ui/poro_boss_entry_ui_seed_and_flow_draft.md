# 포로 서버 보스 입장 UI seed / flow 초안

## 1. 테마 허브 seed 추천 필드
- theme_id
- theme_name
- theme_icon
- theme_description
- display_order
- is_enabled

예시:
| theme_id | theme_name | theme_icon | display_order |
|---|---|---|---:|
| south | 남부 화맥 | fire_core | 1 |
| north | 북부 설혼 | frost_core | 2 |
| west | 서부 황야 | sand_storm | 3 |
| east | 동부 침식 숲 | spirit_leaf | 4 |
| sahar | 사하르 | mirage_eye | 5 |
| arkanon | 아르카논 | resonance_cube | 6 |
| capital | 수도 황성 | imperial_seal | 7 |

## 2. 보스 카드 seed 추천 필드
- boss_id
- theme_id
- boss_name
- boss_tier
- unlock_condition_type
- unlock_condition_value
- entry_ui_profile_id
- loot_theme_id
- is_first_clear_reward
- is_legacy_linked
- display_order

## 3. 보스 상세 profile 추천 필드
- profile_id
- boss_id
- title_text
- flavor_text
- recommended_party_text
- entry_requirement_text
- reward_summary_text
- warning_text
- confirm_button_text

## 4. UI 흐름
1. NPC 클릭
2. theme hub UI open
3. theme select
4. boss list UI open
5. boss card click
6. boss detail UI open
7. entry validate
8. success -> boss run start
9. fail -> reason popup

## 5. 한 줄 요약
**보스 입장 UI는 theme hub, boss card, detail profile 세 층을 seed로 관리하면 테마 중심 구조와 난이도 필터를 동시에 살릴 수 있다.**
