# 포로 서버 DB 테이블 통합 개요 초안

## 문서 목적
이 문서는 지금까지 흩어져 있던 DB 관련 구조를
**도메인별 테이블 묶음 기준으로 한 번에 보는 개요**로 정리한 초안이다.

핵심 목표:
- 어떤 테이블이 어떤 도메인에 속하는지 빠르게 파악한다.
- DB 설계를 통합적으로 볼 수 있게 한다.
- 실제 schema 작업 전 큰 구조를 고정한다.

---

## 1. 플레이어 / 계정
- users
- user_profiles
- user_settings
- user_titles
- user_badges

## 2. 장비 / 성장
- user_items
- user_equipment_slots
- item_potential_lines
- enhancement_logs
- rune_slots
- engraving_slots

## 3. 전투 / 보스
- boss_run_records
- boss_participant_records
- boss_reward_logs
- boss_pattern_logs
- hall_of_fame_records

## 4. 생활 / 영지
- user_life_skills
- user_life_inventory
- user_estates
- user_estate_facilities
- life_craft_logs
- estate_harvest_logs

## 5. 퀘스트 / 업적 / 명예
- user_quest_states
- user_quest_objective_progress
- user_achievement_states
- user_achievement_rewards
- title_unlock_logs

## 6. 마스터 데이터
- item_master
- skill_master
- boss_master
- pattern_master
- region_master
- town_master
- npc_master
- quest_master
- achievement_master

## 7. 경제 / 거래
- market_listings
- market_trade_logs
- gold_change_logs
- item_exchange_logs

## 8. 운영 / 통계
- daily_metric_snapshots
- alert_logs
- balance_stat_snapshots
- api_access_logs

---

## 9. 한 줄 요약

**DB는 플레이어, 성장, 보스, 생활/영지, 퀘스트/업적, 마스터 데이터, 경제, 운영 통계 여덟 묶음으로 나누어 보면 가장 설계하기 쉽다.**
