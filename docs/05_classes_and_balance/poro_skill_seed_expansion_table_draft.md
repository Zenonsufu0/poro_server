# 포로 서버 스킬 seed 확장표 초안

## 문서 목적
이 문서는 기존 스킬 seed/config 표를 한 단계 더 확장해,
실제 구현에서 필요한 세부 필드를 더 정리한 초안이다.

핵심 목표:
- 단순 계수/쿨타임 외에도 캐스팅, 범위, 타수, 조건부 보정까지 구조화한다.
- Codex가 combat resolver와 skill executor를 더 쉽게 만들게 한다.

---

## 1. 추천 추가 필드

- `cast_time_ms`
- `animation_lock_ms`
- `hit_count`
- `aoe_radius`
- `range_distance`
- `movement_distance`
- `can_cancel`
- `condition_bonus_type`
- `condition_bonus_value`
- `applied_state_code`
- `state_apply_chance`

---

## 2. 예시

| skill_id | cast_ms | lock_ms | hit_count | radius | range | move_dist | bonus_type | bonus_val | state_code | state_chance |
|---|---:|---:|---:|---:|---:|---:|---|---:|---|---:|
| gs_break_slash | 300 | 450 | 1 | 2.5 | 3.0 | 0 | NONE | 0 | crack | 100 |
| dagger_heart_cut | 250 | 400 | 1 | 1.5 | 2.0 | 1.0 | BONUS_VS_MARK | 12 | shadow_mark_burst | 100 |
| staff_flame_flood | 500 | 650 | 4 | 4.0 | 8.0 | 0 | BONUS_ON_FIELD | 8 | burn | 100 |
| rune_seal_fall | 600 | 700 | 3 | 4.5 | 9.0 | 0 | BONUS_ON_RUNE_FIELD | 15 | none | 0 |

---

## 3. 한 줄 요약

**스킬 seed는 계수/쿨타임뿐 아니라 캐스팅 시간, 타수, 범위, 이동 거리, 상태 부여, 조건부 보정까지 확장해서 관리하는 것이 실제 구현에 가장 유리하다.**
