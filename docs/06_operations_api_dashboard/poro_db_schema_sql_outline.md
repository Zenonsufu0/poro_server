# 포로 서버 DB 스키마 SQL 초안 가이드

## 문서 목적
이 문서는 실제 SQL 스키마를 만들기 전에,
어떤 테이블을 어떤 컬럼 감각으로 생성할지 정리한 초안이다.

핵심 목표:
- SQLite 기준으로 먼저 무리 없이 설계한다.
- 추후 확장 가능한 형태로 만든다.
- Codex가 CREATE TABLE SQL을 생성하기 쉽게 한다.

---

## 1. users

추천 컬럼:
- user_id TEXT PRIMARY KEY
- nickname TEXT NOT NULL
- class_id TEXT
- weapon_id TEXT
- created_at TEXT NOT NULL
- updated_at TEXT NOT NULL

## 2. user_items

추천 컬럼:
- user_item_id TEXT PRIMARY KEY
- user_id TEXT NOT NULL
- item_id TEXT NOT NULL
- enhance_level INTEGER NOT NULL DEFAULT 0
- potential_grade TEXT
- is_equipped INTEGER NOT NULL DEFAULT 0
- created_at TEXT NOT NULL

## 3. boss_run_records

추천 컬럼:
- run_id TEXT PRIMARY KEY
- boss_id TEXT NOT NULL
- party_size INTEGER NOT NULL
- clear_success INTEGER NOT NULL
- phase_reached INTEGER
- remaining_death_count INTEGER
- clear_time_seconds INTEGER
- failure_reason_code TEXT
- entered_at TEXT NOT NULL
- ended_at TEXT

## 4. user_quest_states

추천 컬럼:
- user_id TEXT NOT NULL
- quest_id TEXT NOT NULL
- state_code TEXT NOT NULL
- started_at TEXT
- completed_at TEXT
- PRIMARY KEY (user_id, quest_id)

## 5. user_estate_facilities

추천 컬럼:
- user_facility_id TEXT PRIMARY KEY
- user_id TEXT NOT NULL
- facility_id TEXT NOT NULL
- level INTEGER NOT NULL
- last_harvested_at TEXT
- stored_amount INTEGER NOT NULL DEFAULT 0

## 6. market_listings

추천 컬럼:
- listing_id TEXT PRIMARY KEY
- seller_user_id TEXT NOT NULL
- item_id TEXT NOT NULL
- unit_price INTEGER NOT NULL
- quantity INTEGER NOT NULL
- status_code TEXT NOT NULL
- created_at TEXT NOT NULL
- sold_at TEXT

## 7. 한 줄 요약

**실제 SQL은 users, user_items, boss_run_records, user_quest_states, user_estate_facilities, market_listings 같은 핵심 테이블부터 먼저 만들고 나머지를 점진적으로 붙이는 방식이 가장 안정적이다.**
