# 포로 서버 보스 패턴 seed / config 표 초안

## 문서 목적
이 문서는 보스 패턴을 실제 데이터 기반으로 관리하기 위한 seed/config 표 초안이다.

핵심 목표:
- 보스 패턴을 하드코딩 덩어리가 아니라 데이터 조합형으로 다룬다.
- 페이즈, 우선순위, 쿨타임, 연속 사용 제한, 성공/실패 분기를 표로 정리한다.
- Codex가 BossPatternScheduler와 BossPhaseController를 만들 때 바로 참고할 수 있게 한다.

---

## 1. 추천 필드

- `boss_id`
- `phase_no`
- `pattern_id`
- `pattern_group`
- `priority`
- `unlock_hp_threshold`
- `cooldown_seconds`
- `max_consecutive_use`
- `is_forced`
- `condition_type`
- `condition_value`
- `success_branch_pattern_id`
- `failure_branch_pattern_id`
- `notes`

---

## 2. 라그네스 예시

| boss_id | phase | pattern_id | group | priority | hp_threshold | cooldown | max_consecutive | forced | condition_type | condition_value | success_branch | failure_branch |
|---|---:|---|---|---|---:|---:|---:|---|---|---|---|---|
| ragnes | 1 | magma_claw | BASIC | LOW | 100 | 6 | 1 | false | NONE | - | - | - |
| ragnes | 1 | eruption_line | CORE | HIGH | 100 | 18 | 1 | false | TIME_ELAPSED | 10 | - | - |
| ragnes | 2 | overheat_zone_expand | PHASE | HIGH | 75 | 22 | 1 | true | PHASE_ENTER | 2 | - | - |
| ragnes | 3 | core_exposure_stagger | STAGGER | FORCED | 50 | 999 | 1 | true | PHASE_ENTER | 3 | eruption_cross | overheat_zone_expand |
| ragnes | 4 | overheat_collapse | PHASE | HIGH | 30 | 24 | 1 | true | PHASE_ENTER | 4 | - | - |
| ragnes | 0 | molten_heart_last_exposure | BERSERK | FORCED | 0 | 999 | 1 | true | HP_ZERO | true | boss_clear | battle_fail |

---

## 3. 카르멘 예시

| boss_id | phase | pattern_id | group | priority | hp_threshold | cooldown | max_consecutive | forced | condition_type | condition_value | success_branch | failure_branch |
|---|---:|---|---|---|---:|---:|---:|---|---|---|---|---|
| carmen | 1 | sector_shift_plus | CORE | HIGH | 100 | 18 | 1 | false | TIME_ELAPSED | 12 | - | - |
| carmen | 2 | safe_cell_relocate_plus | PHASE | HIGH | 82 | 20 | 1 | true | PHASE_ENTER | 2 | - | - |
| carmen | 3 | sync_core_stagger_plus | STAGGER | FORCED | 62 | 999 | 1 | true | PHASE_ENTER | 3 | resonance_collapse | beam_grid_lock |
| carmen | 4 | sector_annihilation | ULTIMATE | HIGH | 40 | 24 | 1 | false | TIME_ELAPSED | 20 | - | - |
| carmen | 0 | final_resonance_break | BERSERK | FORCED | 0 | 999 | 1 | true | HP_ZERO | true | boss_clear | battle_fail |

---

## 4. 한 줄 요약

**보스 패턴은 boss_id, phase, pattern_group, priority, cooldown, max_consecutive, condition, success/failure branch를 가진 seed/config 표로 관리하는 것이 가장 안정적이다.**
