# 포로 서버 문서 인덱스 / 목차 초안

## 문서 목적
이 문서는 지금까지 만든 포로 서버 md 문서를 **주제별로 묶고, 어떤 순서로 읽고, 어떤 순서로 Codex에 넘기면 좋은지** 정리한 인덱스 문서다.

핵심 목표:
- 문서가 많아져도 길을 잃지 않게 한다.
- 기획 문서 / 운영 문서 / 생활 문서 / Codex 문서를 분리한다.
- 실제 구현 전 어떤 문서를 먼저 보는지 순서를 고정한다.

---

## 1. 가장 먼저 읽을 핵심 문서

처음 보는 사람은 아래 4개부터 읽으면 된다.

1. `poro_codex_implementation_guide.md`
2. `poro_final_bosses_design.md`
3. `poro_db_table_design.md`
4. `poro_spring_package_file_tree.md`

이 4개를 먼저 보면
- 프로젝트 방향
- 보스/콘텐츠 축
- 데이터 저장 구조
- 실제 코드 구조
를 한 번에 잡을 수 있다.

---

## 2. 보스 / 전투 관련 문서

### 핵심 보스 기획
- `poro_final_bosses_design.md`
- `poro_boss_patterns_design.md`
- `poro_boss_dev_priority.md`

### 보상 / 연출 / UI
- `poro_boss_rewards_detail.md`
- `poro_boss_reward_names.md`
- `poro_boss_ui_alerts.md`
- `poro_boss_ui_texts.md`

### QA / 관리자 도구
- `poro_boss_admin_test_commands.md`

### 추천 읽는 순서
1. `poro_final_bosses_design.md`
2. `poro_boss_patterns_design.md`
3. `poro_boss_dev_priority.md`
4. `poro_boss_rewards_detail.md`
5. `poro_boss_ui_alerts.md`
6. `poro_boss_admin_test_commands.md`

---

## 3. 운영 / 경제 / 밸런스 / 대시보드 관련 문서

### 운영 철학 / 지표
- `poro_db_web_discord_metrics.md`
- `poro_balance_control_sheet.md`

### DB / API / DTO / 패키지 구조
- `poro_db_table_design.md`
- `poro_api_bot_response_formats.md`
- `poro_api_dto_response_classes.md`
- `poro_spring_package_file_tree.md`

### 대시보드
- `poro_operator_dashboard_layout.md`
- `poro_operator_dashboard_mvp.md`

### 추천 읽는 순서
1. `poro_db_web_discord_metrics.md`
2. `poro_balance_control_sheet.md`
3. `poro_db_table_design.md`
4. `poro_api_bot_response_formats.md`
5. `poro_api_dto_response_classes.md`
6. `poro_operator_dashboard_layout.md`
7. `poro_operator_dashboard_mvp.md`

---

## 4. 생활 시스템 관련 문서

### 생활 큰 틀 / 세부 구조
- `poro_life_system_details.md`

### 재료 / 산출물 / 영지 / 경제
- `poro_life_resource_output_table.md`
- `poro_estate_production_structure.md`
- `poro_life_economy_trade_structure.md`

### 생활 DB / UI / Codex 지시
- `poro_life_db_table_design.md`
- `poro_life_ui_structure.md`
- `poro_life_codex_guide.md`
- `poro_life_codex_prompt_set.md`

### 추천 읽는 순서
1. `poro_life_system_details.md`
2. `poro_life_resource_output_table.md`
3. `poro_estate_production_structure.md`
4. `poro_life_economy_trade_structure.md`
5. `poro_life_db_table_design.md`
6. `poro_life_ui_structure.md`
7. `poro_life_codex_guide.md`
8. `poro_life_codex_prompt_set.md`

---

## 5. Codex에 직접 넘기기 좋은 문서

### 최우선
- `poro_codex_implementation_guide.md`

### 구현 주제별
- `poro_api_dto_response_classes.md`
- `poro_db_table_design.md`
- `poro_spring_package_file_tree.md`
- `poro_boss_dev_priority.md`
- `poro_life_codex_guide.md`
- `poro_life_codex_prompt_set.md`

### Codex 전달 추천 방식
#### 공통 뼈대 작업
- `poro_codex_implementation_guide.md`
- `poro_db_table_design.md`
- `poro_api_dto_response_classes.md`
- `poro_spring_package_file_tree.md`

#### 보스 작업
- `poro_final_bosses_design.md`
- `poro_boss_patterns_design.md`
- `poro_boss_dev_priority.md`
- `poro_boss_ui_alerts.md`

#### 생활 작업
- `poro_life_system_details.md`
- `poro_estate_production_structure.md`
- `poro_life_db_table_design.md`
- `poro_life_codex_prompt_set.md`

---

## 6. 지금 문서 단계의 의미

현재 문서들은 대부분 **초안 문서화 단계**다.

즉:
- 큰 방향
- 시스템 구조
- 역할 분리
- 구현 순서
- DB / API / UI 뼈대
는 많이 잡혀 있다.

하지만 아직 완전히 확정되지 않은 것도 있다.
- 세부 밸런스 수치
- 정확한 드랍량
- 확률표 일부
- 생활 제작식 세부 재료 수량
- 영지 생산량 세부값
- 보스별 패턴 타임라인 수치

그래서 현재 문서는
**구조 구현에는 충분하고, 최종 밸런스 고정에는 일부 추가 명세가 더 필요한 상태**다.

---

## 7. 지금 시점 추천 읽기 루트

### 너 혼자 전체 흐름 볼 때
1. `poro_codex_implementation_guide.md`
2. `poro_final_bosses_design.md`
3. `poro_db_web_discord_metrics.md`
4. `poro_db_table_design.md`
5. `poro_life_system_details.md`

### Codex에게 공통 뼈대 시킬 때
1. `poro_codex_implementation_guide.md`
2. `poro_db_table_design.md`
3. `poro_api_dto_response_classes.md`
4. `poro_spring_package_file_tree.md`

### Codex에게 생활 시킬 때
1. `poro_life_system_details.md`
2. `poro_estate_production_structure.md`
3. `poro_life_db_table_design.md`
4. `poro_life_codex_prompt_set.md`

### Codex에게 보스 시킬 때
1. `poro_final_bosses_design.md`
2. `poro_boss_patterns_design.md`
3. `poro_boss_dev_priority.md`
4. `poro_boss_admin_test_commands.md`

---

## 8. 한 줄 요약

**이 인덱스 문서는 지금까지 만든 포로 서버 md 문서들을 길잡이처럼 묶은 문서이며, 보스/운영/생활/Codex 구현 문서를 주제별로 나눠 어떤 순서로 읽고 구현에 반영하면 되는지 정리한 목차다.**
