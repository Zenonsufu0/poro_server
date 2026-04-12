# 포로 서버 업적 / 칭호 / 명예 seed 예시 초안

## 문서 목적
이 문서는 업적, 칭호, 명예 요소를 실제 seed 예시 형태로 묶어본 초안이다.

핵심 목표:
- achievement/title/honor data가 어떻게 연결되는지 예시를 만든다.
- Codex가 title registry, badge registry, hall-of-fame linkage를 만들기 쉽게 한다.

---

## 1. title seed 예시

| title_id | title_name | source_type | source_id | rarity |
|---|---|---|---|---|
| title_ragnes_clear | 종화를 넘은 자 | achievement | ach_clear_ragnes | rare |
| title_estate_unlock | 나만의 거점 | achievement | ach_estate_unlock | common |
| title_carmen_final | 최종 공명 붕괴 생존자 | achievement | ach_clear_carmen | legendary |

---

## 2. badge seed 예시

| badge_id | badge_name | source_type | source_id | rarity |
|---|---|---|---|---|
| badge_first_craft | 첫 손길 | achievement | ach_first_craft | common |
| badge_no_death_clear | 무사망 돌파 | achievement | ach_no_death_clear | rare |
| badge_extreme_perfect | 완전 정복 | achievement | ach_extreme_no_death | legendary |

---

## 3. hall of fame linkage 예시

| honor_id | honor_type | trigger_type | trigger_target | display_type |
|---|---|---|---|---|
| hof_server_first_any | server_first | SERVER_FIRST_CLEAR | any_extreme | permanent |
| hof_ragnes_no_death | boss_special | BOSS_NO_DEATH | ragnes | seasonal |
| hof_carmen_first | server_first | SERVER_FIRST_CLEAR | carmen | permanent |

---

## 4. 한 줄 요약

**업적/칭호/명예 데이터는 achievement seed를 중심으로 title seed, badge seed, hall-of-fame linkage를 따로 분리해 연결하는 것이 가장 관리하기 쉽다.**
