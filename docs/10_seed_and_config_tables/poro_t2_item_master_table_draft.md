# 포로 서버 T2 장비 부위별 아이템 마스터 표 초안

## 문서 목적
이 문서는 T2 장비를 부위별 아이템 마스터 형태로 정리한 초안이다.

핵심 목표:
- 무기/방어구/장신구를 item master 관점에서 정리한다.
- 지역/보스/세트와 어떤 식으로 연결되는지 구조를 잡는다.
- Codex가 item registry를 만들 수 있게 한다.

---

## 1. 추천 필드

- `item_id`
- `item_name`
- `tier`
- `slot_type`
- `region_theme`
- `boss_source`
- `set_id`
- `base_stat_type`
- `base_stat_value`
- `is_tradeable`
- `notes`

---

## 2. 예시

| item_id | item_name | tier | slot | region_theme | boss_source | set_id | base_stat_type | base_stat_value | tradeable |
|---|---|---|---|---|---|---|---|---:|---|
| t2_gs_ragnes | 화맥 대검 | T2 | weapon | south | ragnes | crimson_brand | attack | 245 | false |
| t2_armor_morvain | 설혼 갑주 | T2 | armor | north | morvain | snow_silence | defense | 188 | false |
| t2_ring_hazard | 폭풍 반지 | T2 | accessory | west | hazard | wild_frontier | attack | 72 | false |
| t2_staff_arkanon | 공명 지팡이 | T2 | weapon | arkanon | arkhanon | imperial_glory | attack | 238 | false |

---

## 3. 한 줄 요약

**T2 장비는 item_id, slot, 지역 테마, 보스 출처, 세트 연결, 기본 수치를 가진 item master 표로 관리하는 것이 가장 안정적이다.**
