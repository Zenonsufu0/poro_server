# 포로 서버 업적 seed / config 표 초안

## 문서 목적
이 문서는 업적 시스템을 실제 데이터 기반으로 구현하기 위한 seed/config 표 초안이다.

핵심 목표:
- 업적 조건과 보상 방향을 코드가 아니라 데이터로 관리한다.
- 카테고리, 조건 타입, 보상 타입을 표준화한다.
- Codex가 achievement tracker와 reward linker를 만들 수 있게 한다.

---

## 1. 추천 필드

- `achievement_id`
- `category`
- `achievement_name`
- `condition_type`
- `condition_target_id`
- `condition_amount`
- `reward_type`
- `reward_target_id`
- `reward_amount`
- `is_hidden`
- `is_repeatable`
- `notes`

---

## 2. 예시

| achievement_id | category | name | condition_type | target | amount | reward_type | reward_target | reward_amount | hidden | repeatable |
|---|---|---|---|---|---:|---|---|---:|---|---|
| ach_clear_ragnes | boss | 종화를 넘은 자 | BOSS_CLEAR | ragnes | 1 | TITLE | title_ragnes_clear | 1 | false | false |
| ach_first_craft | life | 첫 제작 | CRAFT_ANY | any | 1 | BADGE | badge_first_craft | 1 | false | false |
| ach_estate_unlock | estate | 나만의 거점 | ESTATE_UNLOCK | estate | 1 | TITLE | title_estate_unlock | 1 | false | false |
| ach_server_first | special | 선발대 | SERVER_FIRST_CLEAR | any_extreme | 1 | RECORD | hall_of_fame_entry | 1 | true | false |

---

## 3. 한 줄 요약

**업적은 category, condition_type, reward_type 중심의 seed/config 표로 관리하면 추적과 보상 연결이 훨씬 단순해진다.**
