# 포로 서버 직업 스킬 seed / config 표 초안

## 문서 목적
이 문서는 전사/암살자/마법사 스킬을 실제 seed/config 데이터로 관리하기 위한 초안이다.

핵심 목표:
- 스킬을 클래스별 공통 포맷으로 저장한다.
- 태그, 계수, 쿨타임, 자원 소모, 부가효과를 분리해서 관리한다.
- Codex가 skill registry와 combat resolver를 쉽게 구현할 수 있게 한다.

---

## 1. 추천 필드

- `skill_id`
- `class_id`
- `weapon_id`
- `skill_name`
- `role_type`
- `tag_1`
- `tag_2`
- `base_coefficient`
- `cooldown_seconds`
- `resource_type`
- `resource_gain`
- `resource_cost`
- `secondary_effect_type`
- `secondary_effect_value`
- `notes`

---

## 2. 예시

| skill_id | class | weapon | name | role | tag1 | tag2 | coeff | cd | resource_type | gain | cost | effect | value |
|---|---|---|---|---|---|---|---:|---:|---|---:|---:|---|---:|
| gs_break_slash | warrior | greatsword | 파쇄참 | builder | 핵심 | - | 120 | 6 | crack | 1 | 0 | NONE | 0 |
| katana_draw_plus | warrior | katana | 강화 발도 | spender | 정밀 | 핵심 | 240 | 12 | sword_intent | 0 | 3 | NONE | 0 |
| spear_gwancheon | warrior | spear | 관천격 | finisher | 정밀 | 핵심 | 330 | 18 | pressure | 0 | 5 | NONE | 0 |
| dagger_heart_cut | assassin | dagger | 심장 가르기 | finisher | 정밀 | 핵심 | 325 | 18 | shadow_mark | 0 | 3 | BONUS_VS_MARK | 1 |
| twinmoon_annihilate | assassin | dual_blade | 쌍월 절멸 | finisher | 연계 | 핵심 | 335 | 18 | combo_point | 0 | 5 | NONE | 0 |
| staff_thunder_break | mage | staff | 천뢰 붕괴 | finisher | 범위 | 핵심 | 340 | 19 | none | 0 | 0 | BONUS_VS_STATUS | 1 |
| wand_dimension_break | mage | wand | 차원 붕괴 | finisher | 정밀 | 핵심 | 330 | 18 | resonance | 0 | 4 | NONE | 0 |
| rune_seal_fall | mage | rune | 봉성 붕락 | finisher | 범위 | 핵심 | 350 | 20 | rune_piece | 0 | 5 | BONUS_ON_FIELD | 1 |

---

## 3. 한 줄 요약

**직업 스킬은 class/weapon/tag/coefficient/cooldown/resource/effect를 공통 필드로 가진 seed/config 표로 관리하면 전사·암살자·마법사를 같은 구조 안에서 일관되게 다룰 수 있다.**
