# 포로 서버 실제 DB 테이블 설계 초안

## 문서 목적
이 문서는 포로 서버의 핵심 운영 철학인 **“운영자가 직접 보고 통제할 수 없는 핵심 수치는 존재하면 안 된다”**를 기준으로,
실제 저장 구조를 **로그 테이블 / 집계 테이블 / 설정 테이블 / 마스터 테이블**로 나눠 설계한 초안이다.

이 문서의 목적은 다음과 같다.

- 구현 전에 저장 구조를 먼저 고정한다.
- 웹 / 디스코드 / 관리자 명령이 같은 원본 데이터를 보게 한다.
- 운영자가 보고 싶은 수치가 **사후 계산 불가** 상태가 되지 않게 한다.
- 초기 SQLite 기준으로 시작하되, 이후 확장 가능한 구조를 잡는다.

---

## 1. 설계 원칙

### 1-1. 원본 로그와 집계는 분리한다
같은 데이터를 한 테이블에서 다 해결하려고 하면 나중에 꼬인다.

그래서 최소한 아래처럼 나눈다.

- **원본 로그 테이블**: 실제 발생한 사건 저장
- **집계 테이블**: 웹/디코/대시보드용 요약 데이터
- **설정 테이블**: 운영자가 조정하는 수치
- **마스터 테이블**: 보스/아이템/직업 등 기준 정보

---

### 1-2. “현재 상태”와 “이벤트 기록”을 같이 가져간다
예를 들면 골드는 둘 다 필요하다.

- 현재 유저 골드
- 언제 어디서 얼마를 얻고 썼는지 로그

장비도 마찬가지다.

- 현재 장착/보유 상태
- 강화/큐브/전승 시도 로그

---

### 1-3. 운영자가 궁금해할 질문을 기준으로 테이블을 만든다
예:
- 서버 전체 골드 총량이 얼마지?
- 오늘 골드는 어디서 가장 많이 생겼지?
- 라그네스 평균 클리어 타임이 며칠째 증가 중이지?
- 카르멘 발악 성공률이 왜 0%지?
- 태도 평균 딜량이 창보다 왜 이렇게 높지?

이 질문에 답할 수 있어야 한다.

---

## 2. 테이블 큰 분류

### A. 마스터 테이블
- 유저
- 직업
- 무기
- 보스
- 보스 패턴
- 아이템
- 세트
- 재료
- 재화
- 테마

### B. 현재 상태 테이블
- 유저 현재 골드
- 유저 현재 장비
- 유저 현재 강화 상태
- 유저 현재 잠재 상태
- 유저 현재 재료 보유량
- 유저 현재 세트 활성화 상태

### C. 이벤트 로그 테이블
- 골드 획득/소모 로그
- 강화 로그
- 큐브 로그
- 제작 로그
- 보스 입장/클리어/실패 로그
- 보스 패턴 사망 로그
- 전투 딜량 로그
- 재료 획득/소모 로그
- 거래 로그

### D. 집계 테이블
- 일일 경제 집계
- 일일 보스 집계
- 일일 직업 딜량 집계
- 일일 강화/큐브 집계
- 시즌 기록 집계
- 명예의 전당 집계

### E. 설정 테이블
- 보스 체력 배수
- 보스 방어력
- 패턴 피해 배수
- 상태이상 시간
- 무력화 시간
- 발악 타이머
- 파티 규모 보정
- 드랍률/보상 테이블

---

## 3. 마스터 테이블

## 3-1. users
유저 기본 식별 정보

### 컬럼 예시
- `user_id` TEXT PK
- `nickname` TEXT
- `created_at` DATETIME
- `last_login_at` DATETIME
- `account_status` TEXT

### 메모
SQLite 기준이면 UUID 문자열로 가는 게 편하다.

---

## 3-2. themes
테마 마스터

### 컬럼 예시
- `theme_id` TEXT PK
- `theme_name` TEXT
- `display_order` INTEGER
- `is_active` INTEGER

### 예시
- capital
- west
- east
- north
- south
- sahar
- arkanon

---

## 3-3. bosses
보스 마스터

### 컬럼 예시
- `boss_id` TEXT PK
- `theme_id` TEXT FK
- `boss_name` TEXT
- `boss_tier` TEXT
- `boss_type` TEXT
- `is_party_content` INTEGER
- `has_final_berserk` INTEGER
- `is_extreme` INTEGER
- `display_order` INTEGER

### boss_tier 예시
- field
- story
- apex
- depth
- final
- extreme

---

## 3-4. boss_patterns
보스 패턴 마스터

### 컬럼 예시
- `pattern_id` TEXT PK
- `boss_id` TEXT FK
- `pattern_name` TEXT
- `pattern_category` TEXT
- `is_signature` INTEGER
- `is_berserk_pattern` INTEGER
- `phase_min` INTEGER
- `phase_max` INTEGER

### pattern_category 예시
- cone
- line_aoe
- ground_aoe
- safe_zone
- summon
- split_arena
- mark_explode
- stagger
- berserk

---

## 3-5. classes
직업 마스터

### 컬럼 예시
- `class_id` TEXT PK
- `class_name` TEXT
- `is_active` INTEGER

### 예시
- warrior
- assassin
- mage

---

## 3-6. weapons
무기 마스터

### 컬럼 예시
- `weapon_id` TEXT PK
- `class_id` TEXT FK
- `weapon_name` TEXT
- `weapon_group` TEXT
- `is_active` INTEGER

### 예시
- greatsword
- katana
- spear
- dagger
- gun
- staff
- wand
- rune

---

## 3-7. items
아이템 마스터

### 컬럼 예시
- `item_id` TEXT PK
- `item_name` TEXT
- `item_type` TEXT
- `item_tier` TEXT
- `item_slot` TEXT
- `theme_id` TEXT NULL
- `base_attack` REAL NULL
- `base_defense` REAL NULL
- `is_tradeable` INTEGER

---

## 3-8. item_sets
세트 마스터

### 컬럼 예시
- `set_id` TEXT PK
- `set_name` TEXT
- `theme_id` TEXT NULL
- `required_count_3` INTEGER
- `required_count_4` INTEGER
- `required_count_7` INTEGER

---

## 3-9. materials
재료 마스터

### 컬럼 예시
- `material_id` TEXT PK
- `material_name` TEXT
- `material_category` TEXT
- `theme_id` TEXT NULL
- `is_tradeable` INTEGER

### 예시 category
- boss_core
- common_craft
- depth_material
- symbolic_extreme
- market_token

---

## 3-10. currencies
재화 마스터

### 컬럼 예시
- `currency_id` TEXT PK
- `currency_name` TEXT
- `currency_type` TEXT

### 예시
- gold
- cube
- enhancement_stone
- smuggle_coin
- magic_essence

---

## 4. 현재 상태 테이블

## 4-1. user_wallets
유저 현재 재화 보유량

### 컬럼 예시
- `user_id` TEXT
- `currency_id` TEXT
- `amount` INTEGER
- `updated_at` DATETIME

### PK 추천
- `(user_id, currency_id)`

---

## 4-2. user_equipment
유저 장착 장비 상태

### 컬럼 예시
- `equipment_instance_id` TEXT PK
- `user_id` TEXT
- `item_id` TEXT
- `slot_type` TEXT
- `enhance_level` INTEGER
- `potential_grade` TEXT
- `is_equipped` INTEGER
- `is_locked` INTEGER
- `created_at` DATETIME
- `updated_at` DATETIME

### 메모
같은 아이템 마스터라도 실제 장비 인스턴스는 따로 둬야 한다.

---

## 4-3. user_equipment_options
장비 옵션 / 잠재 상세

### 컬럼 예시
- `equipment_instance_id` TEXT
- `option_group` TEXT
- `line_no` INTEGER
- `option_code` TEXT
- `option_value` REAL
- `grade` TEXT
- `is_bonus_line` INTEGER

### option_group 예시
- fixed
- option
- potential

---

## 4-4. user_set_status
유저 세트 활성 상태

### 컬럼 예시
- `user_id` TEXT
- `set_id` TEXT
- `equipped_count` INTEGER
- `active_bonus_stage` INTEGER
- `updated_at` DATETIME

---

## 4-5. user_class_builds
유저 현재 직업/무기 빌드 상태

### 컬럼 예시
- `user_id` TEXT PK
- `class_id` TEXT
- `main_weapon_id` TEXT
- `secondary_info` TEXT NULL
- `power_score` REAL
- `item_level` REAL
- `updated_at` DATETIME

---

## 4-6. user_material_balances
유저 재료 보유량

### 컬럼 예시
- `user_id` TEXT
- `material_id` TEXT
- `amount` INTEGER
- `updated_at` DATETIME

### PK 추천
- `(user_id, material_id)`

---

## 5. 경제 / 성장 로그 테이블

## 5-1. gold_transactions
골드 흐름 원본 로그

### 컬럼 예시
- `tx_id` TEXT PK
- `user_id` TEXT
- `amount` INTEGER
- `direction` TEXT
- `source_type` TEXT
- `source_detail` TEXT
- `related_boss_id` TEXT NULL
- `related_item_id` TEXT NULL
- `created_at` DATETIME

### direction 예시
- inflow
- outflow

### source_type 예시
- boss_reward
- shop
- craft
- enhancement
- cube
- trade
- admin

---

## 5-2. currency_transactions
골드 외 재화 로그

### 컬럼 예시
- `tx_id` TEXT PK
- `user_id` TEXT
- `currency_id` TEXT
- `amount` INTEGER
- `direction` TEXT
- `source_type` TEXT
- `source_detail` TEXT
- `created_at` DATETIME

---

## 5-3. material_transactions
재료 생성/소모 로그

### 컬럼 예시
- `tx_id` TEXT PK
- `user_id` TEXT
- `material_id` TEXT
- `amount` INTEGER
- `direction` TEXT
- `source_type` TEXT
- `source_detail` TEXT
- `related_boss_id` TEXT NULL
- `created_at` DATETIME

---

## 5-4. enhancement_logs
강화 시도 로그

### 컬럼 예시
- `enhance_log_id` TEXT PK
- `user_id` TEXT
- `equipment_instance_id` TEXT
- `before_level` INTEGER
- `after_level` INTEGER
- `success` INTEGER
- `gold_cost` INTEGER
- `stone_cost` INTEGER
- `created_at` DATETIME

---

## 5-5. cube_logs
큐브 사용 로그

### 컬럼 예시
- `cube_log_id` TEXT PK
- `user_id` TEXT
- `equipment_instance_id` TEXT
- `before_grade` TEXT
- `after_grade` TEXT
- `upgrade_success` INTEGER
- `before_snapshot_json` TEXT
- `after_snapshot_json` TEXT
- `created_at` DATETIME

### 메모
전/후 선택형이면 snapshot 저장이 특히 중요하다.

---

## 5-6. crafting_logs
제작 / 전승 / 세트 제작 로그

### 컬럼 예시
- `craft_log_id` TEXT PK
- `user_id` TEXT
- `craft_type` TEXT
- `result_item_id` TEXT
- `result_equipment_instance_id` TEXT NULL
- `is_unique_result` INTEGER
- `gold_cost` INTEGER
- `created_at` DATETIME

### craft_type 예시
- set_craft
- transfer
- normal_craft
- symbol_craft

---

## 5-7. trade_logs
유저 간 거래 / 거래소 로그

### 컬럼 예시
- `trade_log_id` TEXT PK
- `seller_user_id` TEXT
- `buyer_user_id` TEXT
- `trade_type` TEXT
- `target_type` TEXT
- `target_id` TEXT
- `price_gold` INTEGER
- `quantity` INTEGER
- `created_at` DATETIME

---

## 6. 보스 / 전투 로그 테이블

## 6-1. boss_runs
보스 1회 도전 단위 로그

### 컬럼 예시
- `run_id` TEXT PK
- `boss_id` TEXT
- `party_size` INTEGER
- `run_type` TEXT
- `started_at` DATETIME
- `ended_at` DATETIME
- `clear_success` INTEGER
- `clear_time_seconds` INTEGER NULL
- `phase_reached` INTEGER
- `berserk_reached` INTEGER
- `berserk_cleared` INTEGER
- `wipe_reason_code` TEXT NULL
- `server_build_version` TEXT

### run_type 예시
- normal
- match
- direct
- test_admin

---

## 6-2. boss_run_participants
도전 참가자 정보

### 컬럼 예시
- `run_id` TEXT
- `user_id` TEXT
- `class_id` TEXT
- `weapon_id` TEXT
- `item_level` REAL
- `power_score` REAL
- `death_count` INTEGER
- `total_damage` REAL
- `support_score` REAL NULL

### PK 추천
- `(run_id, user_id)`

---

## 6-3. boss_phase_logs
페이즈 전환 로그

### 컬럼 예시
- `phase_log_id` TEXT PK
- `run_id` TEXT
- `phase_no` INTEGER
- `entered_at` DATETIME
- `boss_hp_percent` REAL
- `trigger_type` TEXT

---

## 6-4. boss_pattern_logs
패턴 발동 로그

### 컬럼 예시
- `pattern_log_id` TEXT PK
- `run_id` TEXT
- `pattern_id` TEXT
- `phase_no` INTEGER
- `cast_started_at` DATETIME
- `cast_finished_at` DATETIME NULL
- `target_mode` TEXT
- `hit_count` INTEGER
- `fail_condition_triggered` INTEGER

---

## 6-5. boss_death_events
사망 이벤트 로그

### 컬럼 예시
- `death_event_id` TEXT PK
- `run_id` TEXT
- `user_id` TEXT
- `phase_no` INTEGER
- `killer_pattern_id` TEXT
- `damage_type` TEXT
- `had_mark_debuff` INTEGER
- `had_zone_debuff` INTEGER
- `created_at` DATETIME

### 왜 중요하냐
가장 많이 사람 죽이는 패턴 TOP 5를 만들 수 있다.

---

## 6-6. boss_berserk_logs
발악 패턴 로그

### 컬럼 예시
- `berserk_log_id` TEXT PK
- `run_id` TEXT
- `boss_id` TEXT
- `entered_at` DATETIME
- `duration_seconds` INTEGER
- `clear_success` INTEGER
- `fail_reason_code` TEXT NULL

---

## 6-7. combat_damage_logs
전투 피해 로그

### 컬럼 예시
- `damage_log_id` TEXT PK
- `run_id` TEXT
- `user_id` TEXT
- `class_id` TEXT
- `weapon_id` TEXT
- `source_skill_code` TEXT
- `damage_amount` REAL
- `is_critical` INTEGER
- `is_core_skill` INTEGER
- `tag_snapshot` TEXT
- `created_at` DATETIME

### 메모
원본 로그는 너무 커질 수 있으니, 필요 시 샘플링/버퍼링/배치 집계 고려.

---

## 6-8. combat_state_logs
상태이상 / 무력화 기여 로그

### 컬럼 예시
- `state_log_id` TEXT PK
- `run_id` TEXT
- `user_id` TEXT
- `state_code` TEXT
- `applied_count` INTEGER
- `uptime_seconds` REAL
- `stagger_contribution` REAL NULL
- `created_at` DATETIME

---

## 7. 집계 테이블

## 7-1. daily_economy_summary
일일 경제 집계

### 컬럼 예시
- `summary_date` DATE PK
- `total_gold` INTEGER
- `gold_inflow` INTEGER
- `gold_outflow` INTEGER
- `cube_inflow` INTEGER
- `cube_outflow` INTEGER
- `stone_inflow` INTEGER
- `stone_outflow` INTEGER

---

## 7-2. daily_material_summary
일일 재료 집계

### 컬럼 예시
- `summary_date` DATE
- `material_id` TEXT
- `total_amount` INTEGER
- `inflow` INTEGER
- `outflow` INTEGER

### PK 추천
- `(summary_date, material_id)`

---

## 7-3. daily_boss_summary
일일 보스 집계

### 컬럼 예시
- `summary_date` DATE
- `boss_id` TEXT
- `attempt_count` INTEGER
- `clear_count` INTEGER
- `clear_rate` REAL
- `avg_clear_time_seconds` REAL
- `berserk_reach_rate` REAL
- `berserk_clear_rate` REAL

### PK 추천
- `(summary_date, boss_id)`

---

## 7-4. daily_boss_party_summary
일일 보스 + 파티 규모 집계

### 컬럼 예시
- `summary_date` DATE
- `boss_id` TEXT
- `party_size` INTEGER
- `attempt_count` INTEGER
- `clear_count` INTEGER
- `avg_clear_time_seconds` REAL

### PK 추천
- `(summary_date, boss_id, party_size)`

---

## 7-5. daily_class_dps_summary
일일 직업 평균 딜량 집계

### 컬럼 예시
- `summary_date` DATE
- `class_id` TEXT
- `weapon_id` TEXT NULL
- `avg_dps` REAL
- `median_dps` REAL
- `avg_total_damage` REAL
- `sample_count` INTEGER

---

## 7-6. daily_enhancement_summary
일일 강화 집계

### 컬럼 예시
- `summary_date` DATE
- `target_level` INTEGER
- `attempt_count` INTEGER
- `success_count` INTEGER
- `success_rate` REAL

---

## 7-7. daily_potential_summary
일일 잠재 집계

### 컬럼 예시
- `summary_date` DATE
- `grade` TEXT
- `equipment_count` INTEGER
- `cube_use_count` INTEGER
- `upgrade_success_count` INTEGER

---

## 7-8. season_boss_records
시즌 기록 집계

### 컬럼 예시
- `season_id` TEXT
- `boss_id` TEXT
- `first_clear_run_id` TEXT NULL
- `first_clear_at` DATETIME NULL
- `best_clear_time_seconds` INTEGER NULL
- `best_clear_run_id` TEXT NULL
- `no_death_best_run_id` TEXT NULL

---

## 7-9. hall_of_fame_records
명예의 전당용 기록

### 컬럼 예시
- `record_id` TEXT PK
- `season_id` TEXT
- `boss_id` TEXT
- `record_type` TEXT
- `run_id` TEXT
- `display_title` TEXT
- `created_at` DATETIME

### record_type 예시
- first_clear
- best_time
- no_death
- solo_clear
- extreme_clear

---

## 8. 설정 테이블

## 8-1. boss_balance_configs
보스 기본 밸런스 설정

### 컬럼 예시
- `boss_id` TEXT PK
- `hp_multiplier` REAL
- `defense_value` REAL
- `damage_multiplier` REAL
- `status_duration_multiplier` REAL
- `stagger_window_seconds` REAL
- `berserk_timer_seconds` INTEGER
- `updated_at` DATETIME

---

## 8-2. boss_party_scaling_configs
파티 규모 보정

### 컬럼 예시
- `boss_id` TEXT
- `party_size` INTEGER
- `hp_multiplier` REAL
- `damage_multiplier` REAL
- `status_multiplier` REAL

### PK 추천
- `(boss_id, party_size)`

---

## 8-3. pattern_balance_configs
패턴별 세부 수치 설정

### 컬럼 예시
- `pattern_id` TEXT PK
- `cooldown_seconds` REAL
- `damage_multiplier` REAL
- `warning_seconds` REAL
- `duration_seconds` REAL
- `safezone_size_multiplier` REAL NULL
- `mark_duration_seconds` REAL NULL

---

## 8-4. reward_drop_configs
보상/드랍 설정

### 컬럼 예시
- `boss_id` TEXT
- `reward_type` TEXT
- `reward_target_id` TEXT
- `min_amount` INTEGER
- `max_amount` INTEGER
- `drop_rate` REAL

### PK 추천
- `(boss_id, reward_type, reward_target_id)`

---

## 8-5. system_feature_flags
기능 온오프

### 컬럼 예시
- `feature_key` TEXT PK
- `is_enabled` INTEGER
- `note` TEXT
- `updated_at` DATETIME

### 예시
- extreme_boss_rewards_enabled
- hall_of_fame_enabled
- boss_debug_logs_enabled

---

## 9. 웹 / 디코 / 관리자 명령이 보는 구조

## 웹 대시보드
주로 집계 테이블을 본다.
- `daily_economy_summary`
- `daily_boss_summary`
- `daily_class_dps_summary`
- `season_boss_records`
- `hall_of_fame_records`

필요 시 drill-down으로 원본 로그 참조:
- `gold_transactions`
- `boss_runs`
- `boss_death_events`

---

## 디스코드 봇
주로 집계 + 최근 기록을 본다.
- 일일 집계
- 최근 7일 집계
- 시즌 기록
- 최초 클리어/최고 기록

---

## 관리자 명령
원본 상태 + 최근 로그를 같이 본다.
- 현재 보스 밸런스 설정
- 현재 서버 전체 골드
- 오늘 생성/소멸
- 보스별 최근 성공률
- 직업별 최근 평균 딜량

---

## 10. 추천 인덱스

SQLite라도 최소한 아래는 신경 써야 한다.

### 추천 인덱스
- `gold_transactions(user_id, created_at)`
- `gold_transactions(source_type, created_at)`
- `boss_runs(boss_id, started_at)`
- `boss_runs(clear_success, started_at)`
- `boss_run_participants(user_id, run_id)`
- `boss_death_events(killer_pattern_id, created_at)`
- `combat_damage_logs(run_id, user_id)`
- `daily_boss_summary(summary_date, boss_id)`
- `daily_class_dps_summary(summary_date, class_id, weapon_id)`

---

## 11. 로그 보존 정책 추천

모든 원본 로그를 영원히 들고 있으면 무거워질 수 있다.

### 추천
- 원본 상세 로그:
  - 최근 30~90일 유지
- 일일 집계:
  - 시즌 전체 보존
- 시즌 기록 / 명예의 전당:
  - 영구 보존

### 특히 무거운 로그
- `combat_damage_logs`
- `combat_state_logs`

이 둘은 나중에
- 배치 집계 후 원본 압축
- 샘플링 저장
도 고려 가능하다.

---

## 12. 초기 SQLite 기준 현실적인 최소 테이블 세트

처음부터 너무 다 만들면 무거우니, 최소 MVP 기준도 잡는다.

### 꼭 먼저 만들 것
- `users`
- `bosses`
- `boss_runs`
- `boss_run_participants`
- `boss_death_events`
- `gold_transactions`
- `currency_transactions`
- `enhancement_logs`
- `cube_logs`
- `user_wallets`
- `user_equipment`
- `daily_economy_summary`
- `daily_boss_summary`
- `daily_class_dps_summary`
- `boss_balance_configs`
- `pattern_balance_configs`

이 정도면
- 운영 통계
- 웹 조회
- 디코 요약
- 밸런스 조정
까지 기본은 된다.

---

## 13. 중기 확장 추천

나중에 추가
- `trade_logs`
- `material_transactions`
- `daily_material_summary`
- `season_boss_records`
- `hall_of_fame_records`
- `user_set_status`
- `reward_drop_configs`

이렇게.

---

## 14. 한 줄 요약

### DB 철학
**로그는 원본을 남기고, 운영은 집계로 보고, 조정은 설정 테이블로 한다.**

### 구현 철학
**웹 / 디스코드 / 관리자 명령은 같은 DB 원본과 집계값을 봐야 한다.**

---

## 15. 다음으로 가야 할 것

### 1순위
운영자 대시보드 화면 구성 초안
- 어떤 카드가 메인에 와야 하는지
- 어떤 그래프가 필요한지
- 어떤 경고를 가장 먼저 띄울지

### 2순위
테마별 보이스 / 사운드 방향 정리
- 패턴 대표 사운드
- 발악 패턴 사운드
- 클리어 / 실패 사운드

### 3순위
실제 API / 봇 명령 응답 포맷 초안
- 웹 API 응답
- 디스코드 명령 응답 구조
- 관리자 명령 출력 형식
