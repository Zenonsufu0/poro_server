# 포로 서버 음식 / 만찬 / 물약 DB · 아이템 구조 초안

## 문서 목적
이 문서는 포로 서버 생활 시스템에서 사용하는 **음식 / 만찬 / 물약**을
실제 DB, 아이템, 레시피, 버프 슬롯, 서비스 구조 기준으로 정리한 초안이다.

핵심 목표:
- 효과표를 실제 구현 가능한 데이터 구조로 내린다.
- 음식/만찬/물약의 아이템 분류와 DB 저장 기준을 고정한다.
- Codex가 recipe, item, effect, buff slot 구조를 바로 만들 수 있게 한다.

---

## 1. 기본 철학

### 1-1. 음식 / 만찬 / 물약은 역할이 다르다
- **음식**: 개인 장기 버프
- **만찬**: 파티 장기 버프
- **물약**: 즉시 회복 또는 단기 대응

이 역할 차이는
- 아이템 분류
- 제작식
- 적용 규칙
- 버프 슬롯
전부에 반영되어야 한다.

---

### 1-2. 효과는 아이템 텍스트가 아니라 데이터로 관리한다
구현할 때 효과를 설명문으로만 두면 안 된다.

최소한 아래는 데이터화해야 한다.
- 버프 카테고리
- 적용 대상
- 지속시간
- 수치
- 중복 규칙
- 재사용 대기시간

즉:
**아이템 자체 + 효과 데이터 + 적용 규칙**을 분리해서 관리하는 게 좋다.

---

## 2. 추천 분류 구조

## 2-1. 아이템 분류
추천 상위 분류는 이렇다.

- `FOOD`
- `FEAST`
- `POTION_HEAL`
- `POTION_RESIST`
- `POTION_COMBAT`

### 의미
- `FOOD`: 일반 개인 음식
- `FEAST`: 파티 전체 만찬
- `POTION_HEAL`: 회복 물약
- `POTION_RESIST`: 상태 대응 물약
- `POTION_COMBAT`: 짧은 전투 보조 물약

---

## 2-2. 버프 슬롯 분류
추천 버프 슬롯은 이렇다.

- `MEAL_SLOT`
- `RESIST_POTION_SLOT`
- `COMBAT_POTION_SLOT`

### 규칙
- 음식/만찬은 모두 `MEAL_SLOT` 공유
- 상태 대응 물약은 `RESIST_POTION_SLOT`
- 전투 보조 물약은 `COMBAT_POTION_SLOT`
- 회복 물약은 즉시형이므로 버프 슬롯보다 **쿨타임 관리형**으로 처리 가능

즉:
- 음식과 만찬은 같은 슬롯을 공유
- 물약은 별도 계열 슬롯
- 회복 물약은 버프보단 소비/쿨타임성 아이템

---

## 3. DB 테이블 추천

## 3-1. consumable_items
음식 / 만찬 / 물약 마스터

### 컬럼 예시
- `consumable_id` TEXT PK
- `consumable_name` TEXT
- `consumable_type` TEXT
- `grade` TEXT NULL
- `item_rarity` TEXT NULL
- `required_life_skill_type` TEXT
- `required_life_skill_level` INTEGER
- `recipe_id` TEXT NULL
- `is_tradeable` INTEGER
- `is_active` INTEGER

### consumable_type 예시
- FOOD
- FEAST
- POTION_HEAL
- POTION_RESIST
- POTION_COMBAT

### grade 예시
- NORMAL
- ADVANCED
- RARE

---

## 3-2. consumable_effects
소비 아이템 효과 마스터

### 컬럼 예시
- `effect_id` TEXT PK
- `consumable_id` TEXT
- `effect_type` TEXT
- `target_type` TEXT
- `buff_slot_type` TEXT NULL
- `duration_seconds` INTEGER NULL
- `cooldown_seconds` INTEGER NULL
- `value_type` TEXT
- `value_amount` REAL
- `secondary_effect_type` TEXT NULL
- `secondary_value_amount` REAL NULL
- `is_replace_existing` INTEGER

### effect_type 예시
- MAX_HP_PERCENT
- HEAL_PERCENT
- STATUS_RESIST
- MOVE_SPEED_PERCENT
- RECOVERY_EFFICIENCY
- DAMAGE_REDUCTION
- DOT_REDUCTION
- STATUS_ACCUMULATION_REDUCTION

### target_type 예시
- SELF
- PARTY

### value_type 예시
- PERCENT
- FLAT
- SECONDS

---

## 3-3. consumable_cooldowns
유저 개별 쿨타임 상태 저장이 필요하면 쓰는 테이블

### 컬럼 예시
- `user_id` TEXT
- `consumable_type` TEXT
- `cooldown_ends_at` DATETIME

### PK 추천
- `(user_id, consumable_type)`

### 비고
회복 물약처럼 카테고리 쿨다운을 관리할 때 유용하다.

---

## 3-4. user_active_consumable_effects
유저 현재 활성화 버프 상태

### 컬럼 예시
- `user_id` TEXT
- `buff_slot_type` TEXT
- `consumable_id` TEXT
- `applied_at` DATETIME
- `expires_at` DATETIME
- `source_user_id` TEXT NULL
- `source_context` TEXT NULL

### PK 추천
- `(user_id, buff_slot_type)`

### source_context 예시
- self_food
- party_feast
- self_potion

### 비고
음식/만찬 대체 규칙 구현이 쉬워진다.

---

## 3-5. consumable_use_logs
소비 아이템 사용 로그

### 컬럼 예시
- `use_log_id` TEXT PK
- `user_id` TEXT
- `consumable_id` TEXT
- `consumable_type` TEXT
- `target_type` TEXT
- `used_at` DATETIME
- `applied_count` INTEGER

### 역할
- 음식/만찬/물약 사용량 추적
- 거래 가치 높은 품목 소비량 분석
- 운영자가 메타 파악 가능

---

## 3-6. feast_party_apply_logs
만찬 파티 적용 로그가 필요하면 별도 분리 가능

### 컬럼 예시
- `apply_log_id` TEXT PK
- `source_user_id` TEXT
- `target_user_id` TEXT
- `consumable_id` TEXT
- `applied_at` DATETIME
- `expires_at` DATETIME

### 역할
- 만찬이 누구에게 적용됐는지 추적
- 파티 적용 문제 디버깅 가능

---

## 4. 아이템/효과 구조 예시

## 4-1. 음식 예시
### 아이템
- `consumable_id`: `healing_soup`
- `consumable_type`: `FOOD`

### 효과
- `effect_type`: `MAX_HP_PERCENT`
- `target_type`: `SELF`
- `buff_slot_type`: `MEAL_SLOT`
- `duration_seconds`: `1800`
- `value_type`: `PERCENT`
- `value_amount`: `3`

### 적용 규칙
- 기존 `MEAL_SLOT` 버프 제거
- 새 음식 적용
- 30분 지속

---

## 4-2. 만찬 예시
### 아이템
- `consumable_id`: `capital_defense_feast`
- `consumable_type`: `FEAST`

### 효과 1
- `effect_type`: `MAX_HP_PERCENT`
- `target_type`: `PARTY`
- `buff_slot_type`: `MEAL_SLOT`
- `duration_seconds`: `2700`
- `value_amount`: `5`

### 효과 2
- `effect_type`: `RECOVERY_EFFICIENCY`
- `target_type`: `PARTY`
- `buff_slot_type`: `MEAL_SLOT`
- `duration_seconds`: `2700`
- `value_amount`: `6`

### 적용 규칙
- 대상 파티원 전원에게 적용
- 각 유저의 `MEAL_SLOT` 기존 버프 제거 후 새 만찬 등록

---

## 4-3. 일반 회복 물약 예시
### 아이템
- `consumable_id`: `basic_healing_potion`
- `consumable_type`: `POTION_HEAL`
- `grade`: `NORMAL`

### 효과
- `effect_type`: `HEAL_PERCENT`
- `target_type`: `SELF`
- `duration_seconds`: NULL
- `cooldown_seconds`: `30`
- `value_amount`: `15`

### 적용 규칙
- 즉시 최대 체력의 15% 회복
- 회복 물약 계열 쿨다운 30초 시작

---

## 4-4. 상태 대응 물약 예시
### 아이템
- `consumable_id`: `frost_relief_potion`
- `consumable_type`: `POTION_RESIST`

### 효과
- `effect_type`: `STATUS_ACCUMULATION_REDUCTION`
- `target_type`: `SELF`
- `buff_slot_type`: `RESIST_POTION_SLOT`
- `duration_seconds`: `90`
- `value_amount`: `15`

### 적용 규칙
- 기존 `RESIST_POTION_SLOT` 버프 제거 후 대체
- 90초 동안 냉기 대응 효과 제공

---

## 4-5. 전투 보조 물약 예시
### 아이템
- `consumable_id`: `survival_tonic`
- `consumable_type`: `POTION_COMBAT`

### 효과
- `effect_type`: `DAMAGE_REDUCTION`
- `target_type`: `SELF`
- `buff_slot_type`: `COMBAT_POTION_SLOT`
- `duration_seconds`: `30`
- `value_amount`: `8`

### 적용 규칙
- 기존 `COMBAT_POTION_SLOT` 버프 제거 후 대체
- 30초 동안 받는 피해 8% 감소

---

## 5. recipe 구조와 연결

소비 아이템도 결국 recipe 기반 제작으로 연결하는 게 좋다.

## 5-1. life_crafting_recipes와 연결
### 예시
- `craft_category`: COOKING / FEAST / ALCHEMY
- `result_item_id`: consumable item code
- `required_skill_type`: COOKING 또는 ALCHEMY

---

## 5-2. 추천 제작 분류
- `COOKING`: 개인 음식
- `FEAST`: 만찬
- `ALCHEMY_HEAL`: 회복 물약
- `ALCHEMY_RESIST`: 상태 대응 물약
- `ALCHEMY_COMBAT`: 전투 보조 물약

이렇게 나누면 나중에 UI나 정렬도 편하다.

---

## 6. 서비스 구조 추천

## 6-1. 추천 서비스 클래스
- `ConsumableUseService`
- `MealBuffService`
- `FeastApplyService`
- `PotionCooldownService`
- `ConsumableEffectResolver`
- `ConsumableLogService`

---

## 6-2. 역할 분리
### `ConsumableUseService`
- 아이템 사용 진입점
- 타입별 분기 호출

### `MealBuffService`
- 음식/만찬 대체 규칙 처리
- `MEAL_SLOT` 관리

### `FeastApplyService`
- 파티 대상 탐색
- 만찬 효과 일괄 적용

### `PotionCooldownService`
- 회복 물약/전투 물약 쿨타임 검사 및 등록

### `ConsumableEffectResolver`
- 효과 수치 조회
- 복합 효과 조합

### `ConsumableLogService`
- 사용 로그 기록

---

## 7. 구현용 중복 규칙 요약

### 음식 / 만찬
- 최종 `MEAL_SLOT` 하나만 유지
- 새 음식/만찬 사용 시 기존 제거 후 새 효과 적용
- 지속시간은 새 효과 기준으로 초기화

### 상태 대응 물약
- `RESIST_POTION_SLOT` 하나만 유지
- 새 대응 물약 사용 시 기존 제거 후 대체

### 전투 보조 물약
- `COMBAT_POTION_SLOT` 하나만 유지
- 새 보조 물약 사용 시 기존 제거 후 대체

### 회복 물약
- 즉시형
- 버프 슬롯보다 쿨다운 중심 관리
- 같은 카테고리 쿨다운 공유 가능

---

## 8. 등급 구조 추천

## 8-1. 회복 물약
- NORMAL
- ADVANCED
- RARE

### 권장 회복량
- NORMAL: 15%
- ADVANCED: 25%
- RARE: 35%

---

## 8-2. 음식 / 만찬
1차는 굳이 등급을 깊게 넣지 않아도 된다.
대신 필요하면:
- BASIC
- SPECIAL
- FEAST
정도로만 나눌 수 있다.

처음엔 **레시피/카테고리 기준 분리**가 더 낫다.

---

## 9. 운영자가 봐야 하는 통계 포인트

이 시스템도 운영자가 통제할 수 있어야 한다.

### 봐야 하는 것
- 음식별 사용 횟수
- 만찬별 사용 횟수
- 회복 물약 사용 횟수
- 상태 대응 물약 사용 횟수
- 전투 보조 물약 사용 횟수
- 보스별 사용량 차이
- 특정 소모품 의존도

### 경고 예시
- 특정 만찬 사용률이 지나치게 높음
- 회복 물약 사용량이 과도함
- 특정 상태 대응 물약이 필수품처럼 굳어짐

---

## 10. Codex에 바로 줄 수 있는 구현 지시 포인트

### 작업 단위 A
- consumable item master / effect master schema 작성

### 작업 단위 B
- 음식/만찬/물약 type enum 및 DTO 작성

### 작업 단위 C
- `MEAL_SLOT`, `RESIST_POTION_SLOT`, `COMBAT_POTION_SLOT` 기반 버프 적용 서비스 작성

### 작업 단위 D
- 회복 물약 쿨다운 서비스 작성

### 작업 단위 E
- recipe 결과물을 consumable item으로 연결하는 제작 로직 작성

---

## 11. 한 줄 요약

**음식/만찬/물약은 consumable item master + effect master + active buff slot + use log 구조로 분리하고, 음식/만찬은 `MEAL_SLOT` 1개를 공유하며 물약은 대응 슬롯과 전투 슬롯, 회복용 즉시 쿨다운 구조로 나누는 것이 포로 서버에 가장 적합하다.**
