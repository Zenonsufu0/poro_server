# 포로 서버 장비 / 강화 / 잠재 seed / config 표 초안

## 문서 목적
이 문서는 장비, 강화, 잠재 시스템을 실제 seed/config 형태로 관리하기 위한 초안이다.

핵심 목표:
- T1/T2 장비 구조를 데이터화한다.
- 강화 확률표, 잠재 등급, 이탈 잠재 구조를 seed/config로 옮긴다.
- Codex가 item/enhance/potential registry를 만들 수 있게 한다.

---

## 1. 장비 마스터 추천 필드
- `item_id`
- `item_tier`
- `slot_type`
- `item_name`
- `base_stat_type`
- `base_stat_value`
- `option_pool_id`
- `set_id`
- `is_tradeable`
- `notes`

## 2. 강화 seed 추천 필드
- `enhance_level`
- `success_rate`
- `gold_cost`
- `stone_cost`
- `break_on_fail`
- `downgrade_on_fail`

### 예시
| level | success_rate | gold_cost | stone_cost | break | downgrade |
|---:|---:|---:|---:|---|---|
| 1 | 100 | 100 | 1 | false | false |
| 10 | 75 | 1200 | 5 | false | false |
| 15 | 50 | 3000 | 10 | false | false |
| 20 | 5 | 12000 | 25 | false | false |
| 25 | 0.5 | 50000 | 60 | false | false |

## 3. 잠재 seed 추천 필드
- `potential_grade`
- `line_no`
- `pool_id`
- `option_code`
- `value_min`
- `value_max`
- `is_prime_like_escape`

## 4. 한 줄 요약

**장비/강화/잠재는 item master, enhance rate table, potential option pool을 분리한 seed/config 구조로 관리하면 T1/T2, 강화 확률, 이탈 잠재까지 일관되게 구현할 수 있다.**
