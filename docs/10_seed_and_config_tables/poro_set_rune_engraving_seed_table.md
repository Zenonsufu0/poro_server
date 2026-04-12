# 포로 서버 세트효과 / 룬 / 각인 seed 표 초안

## 문서 목적
이 문서는 T2 세트효과, 룬, 각인 시스템을 seed/config 형태로 관리하기 위한 초안이다.

핵심 목표:
- 세트효과 5종을 데이터화한다.
- 룬과 각인을 별도 마스터로 분리한다.
- Codex가 set bonus resolver, rune registry, engraving registry를 만들 수 있게 한다.

---

## 1. 세트효과 seed 추천 필드

- `set_id`
- `set_name`
- `piece_count`
- `effect_order`
- `effect_type`
- `value_type`
- `value_amount`
- `notes`

### 예시
| set_id | set_name | piece_count | effect_order | effect_type | value_type | value_amount |
|---|---|---:|---:|---|---|---:|
| glory_of_imperial | 황성의 위광 | 3 | 1 | GENERAL_DAMAGE_INCREASE | PERCENT | 6 |
| glory_of_imperial | 황성의 위광 | 4 | 1 | CORE_SKILL_BOOST | PERCENT | 8 |
| glory_of_imperial | 황성의 위광 | 7 | 1 | PLAYSTYLE_COMPLETION | FLAG | 1 |

---

## 2. 룬 seed 추천 필드

- `rune_id`
- `rune_name`
- `rune_grade`
- `effect_type`
- `value_type`
- `value_amount`
- `slot_filter`
- `notes`

### 예시
| rune_id | rune_name | grade | effect_type | value_type | value_amount |
|---|---|---|---|---|---:|
| rune_resonance_minor | 공명의 소룬 | rare | RESONANCE_EFFECT_UP | PERCENT | 4 |
| rune_pressure_minor | 압박의 소룬 | rare | PRESSURE_EFFICIENCY | PERCENT | 4 |
| rune_core_major | 핵심의 대룬 | unique | CORE_SKILL_DAMAGE | PERCENT | 6 |

---

## 3. 각인 seed 추천 필드

- `engraving_id`
- `engraving_name`
- `engraving_type`  // class/common
- `max_level`
- `effect_type`
- `value_type`
- `value_per_level`
- `notes`

### 예시
| engraving_id | engraving_name | type | max_level | effect_type | value_type | value_per_level |
|---|---|---|---:|---|---|---:|
| common_recovery_01 | 회복 강화 | common | 5 | RECOVERY_EFFICIENCY | PERCENT | 2 |
| common_combat_01 | 전투 집중 | common | 5 | GENERAL_DAMAGE_INCREASE | PERCENT | 1.5 |
| warrior_gs_crack_01 | 파쇄 심화 | class | 1 | CRACK_EFFICIENCY | PERCENT | 12 |

---

## 4. 한 줄 요약

**세트효과, 룬, 각인은 각각 별도 seed/config 마스터로 관리하고, set bonus는 piece count 기준, 룬은 effect 기준, 각인은 level 기반 effect 기준으로 분리하는 것이 가장 안정적이다.**
