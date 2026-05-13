# CustomModelData 범위 정의 — Season 1 치장 무기

## 개요

- base material: `NETHERITE_SWORD`
- namespace: `poro`
- 관리 파일: `weapon_cosmetic_registry.yml` (이 파일에서만 CMD 할당)

## 범위 테이블

| 클래스 | 범위 | 베이스(예약) | 사용 가능 시작 | 용량 |
|---|---|---|---|---|
| sword | 100100–100199 | 100100 | 100101 | 99 |
| hammer | 100200–100299 | 100200 | 100201 | 99 |
| spear | 100300–100399 | 100300 | 100301 | 99 |
| crossbow | 100400–100499 | 100400 | 100401 | 99 |
| scythe | 100500–100599 | 100500 | 100501 | 99 |
| staff | 100600–100699 | 100600 | 100601 | 99 |

## 규칙

1. **xx00 슬롯은 클래스 플레이스홀더로 예약** — 실제 자산 미할당.
2. **첫 자산은 xx01부터** (예: sword 첫 번째 = 100101).
3. **한번 할당된 CMD는 재사용 금지** — 자산 제거 후에도 해당 번호 보존.
4. 신규 자산 추가 시 반드시 `weapon_cosmetic_registry.yml`에 먼저 등록.
5. CMD 할당 순서는 등록 순이며, 무기 품질/희귀도와 무관.
6. export/resourcepack의 JSON과 이 레지스트리가 일치하는지 정기 검증.

## 현재 할당 현황

| CMD | asset_id | 클래스 | 상태 |
|---|---|---|---|
| 100101 | sword_vanquisher_poro_01 | sword | planned |
