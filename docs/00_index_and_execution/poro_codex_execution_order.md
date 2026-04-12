# 포로 서버 Codex 실행 순서표 초안

## 문서 목적
이 문서는 지금까지 만든 포로 서버 문서를 바탕으로,
**Codex에 어떤 순서로 무엇을 시켜야 하는지**를 실전 작업 순서로 정리한 문서다.

핵심 목표:
- Codex에게 한 번에 너무 큰 작업을 던지지 않는다.
- 구조 → DB → 공용 타입 → 서비스 뼈대 → 실제 콘텐츠 순으로 안정적으로 진행한다.
- 보스, 운영, 생활을 한 줄로 엮은 실행 순서를 만든다.

---

## 1. 최상위 원칙

### 원칙 1
한 번에 “전체 완성”을 시키지 않는다.

### 원칙 2
먼저 공용 구조를 만든 뒤, 그 위에 보스/생활/운영을 올린다.

### 원칙 3
수치 확정이 덜 된 시스템은 **구조 우선 구현**을 시킨다.

### 원칙 4
UI보다 서비스/DB/로그 구조를 먼저 만든다.

---

## 2. 0단계 — Codex에게 먼저 같이 줄 문서

가장 먼저 같이 넘길 공통 문서:
- `poro_codex_implementation_guide.md`
- `poro_docs_index.md`
- `poro_db_table_design.md`
- `poro_api_dto_response_classes.md`
- `poro_spring_package_file_tree.md`

이 5개는 거의 공통 스타터 문서다.

---

## 3. 1단계 — 프로젝트 공용 뼈대

### 목표
멀티모듈과 공용 enum/DTO/기본 클래스부터 만든다.

### 같이 줄 문서
- `poro_codex_implementation_guide.md`
- `poro_spring_package_file_tree.md`
- `poro_api_dto_response_classes.md`
- `poro_db_table_design.md`

### Codex에게 시킬 작업
1. Gradle 멀티모듈 뼈대 생성
2. `poro-core`, `poro-plugin`, `poro-admin-api`, `poro-discord-bot` 생성
3. 공용 enum/DTO 생성
4. 최소 부트스트랩 클래스 생성

### 기대 산출물
- 실행 가능한 프로젝트 뼈대
- core enum/DTO
- 최소 plugin/admin-api/bot 앱

---

## 4. 2단계 — DB / API / 운영 기초

### 목표
운영자가 볼 수 있는 최소 통계 구조와 API 뼈대 생성

### 같이 줄 문서
- `poro_db_web_discord_metrics.md`
- `poro_db_table_design.md`
- `poro_api_bot_response_formats.md`
- `poro_api_dto_response_classes.md`
- `poro_operator_dashboard_mvp.md`

### Codex에게 시킬 작업
1. SQLite schema 1차 생성
2. log / summary / config 테이블 분리
3. admin-api 기본 repository/service/controller 생성
4. 최소 엔드포인트 구현
   - `/api/admin/overview`
   - `/api/admin/economy/summary`
   - `/api/admin/bosses/summary`
   - `/api/admin/combat/class-balance`
   - `/api/admin/alerts`

### 기대 산출물
- DB schema
- admin-api 최소 작동
- 운영 요약 API

---

## 5. 3단계 — 보스 엔진 공용 구조

### 목표
실제 보스별 구현 전에 공용 전투 엔진과 QA 구조 먼저 만든다.

### 같이 줄 문서
- `poro_final_bosses_design.md`
- `poro_boss_patterns_design.md`
- `poro_boss_dev_priority.md`
- `poro_boss_admin_test_commands.md`
- `poro_boss_ui_alerts.md`

### Codex에게 시킬 작업
1. 보스룸 구조
2. 보스 전투 엔진
3. 패턴 인터페이스
4. 페이즈 컨트롤러
5. 보스 UI 서비스
6. QA 관리자 명령 최소 세트

### 기대 산출물
- 보스 공용 엔진
- 패턴 모듈 구조
- 관리자 테스트 명령

---

## 6. 4단계 — 결전 보스 1차 구현

### 목표
결전 보스를 쉬운 순서부터 붙인다.

### 추천 구현 순서
1. 라그네스
2. 모르바인
3. 하자드
4. 세르카인
5. 카실레아
6. 아르케논
7. 아자켈

### 같이 줄 문서
- `poro_final_bosses_design.md`
- `poro_boss_dev_priority.md`
- `poro_boss_ui_texts.md`
- `poro_boss_rewards_detail.md`

### Codex에게 시킬 작업
- 라그네스부터 보스 클래스 / 패턴 스크립트 뼈대 / UI 문구 연동
- 이후 동일 패턴으로 다른 결전 보스 확장

### 기대 산출물
- 결전 보스 1~3종 구현
- 패턴 재사용성 검증 가능 상태

---

## 7. 5단계 — 생활 시스템 공용 구조

### 목표
생활 시스템도 공용 타입/DB/서비스 뼈대부터 구현

### 같이 줄 문서
- `poro_life_system_details.md`
- `poro_life_db_table_design.md`
- `poro_life_codex_guide.md`
- `poro_life_codex_prompt_set.md`

### Codex에게 시킬 작업
1. 생활 enum / resource master 구조
2. 생활 DB schema
3. 채집 공통 구조
4. 제작 공통 구조
5. 청사진 / 영지 생산 공통 구조

### 기대 산출물
- 생활 시스템 core + DB + service 뼈대

---

## 8. 6단계 — 생활 채집 / 제작 / 영지 생산

### 목표
생활을 실제 플레이 가능한 수준으로 올린다.

### 같이 줄 문서
- `poro_life_resource_output_table.md`
- `poro_estate_production_structure.md`
- `poro_life_economy_trade_structure.md`
- `poro_life_ui_structure.md`
- `poro_cooking_alchemy_details.md`
- `poro_life_numbers_draft.md`

### Codex에게 시킬 작업
1. 채집/채광/벌목/낚시
2. 요리/연금/제련
3. 청사진 해금
4. 영지 생산/수확/업그레이드
5. 생활 UI 최소 구현

### 기대 산출물
- 생활 1차 플레이 가능
- 거래소 연결 가능한 기본 산출물 확보

---

## 9. 7단계 — 운영 고도화

### 목표
대시보드, 경고, 통계, 디스코드 봇을 실전 운영 수준으로 올린다.

### 같이 줄 문서
- `poro_operator_dashboard_layout.md`
- `poro_operator_dashboard_mvp.md`
- `poro_balance_control_sheet.md`
- `poro_db_web_discord_metrics.md`

### Codex에게 시킬 작업
1. 운영 대시보드 MVP
2. 경고 로직
3. 일일 집계 배치
4. 디스코드 봇 요약 명령
5. 관리자 명령 출력 포맷 정리

### 기대 산출물
- 운영자가 실제로 통제 가능한 상태

---

## 10. 8단계 — 극상위 보스 / 명예 요소 / 후반 확장

### 목표
후반 고난도 콘텐츠와 기록/명예 구조 붙이기

### 같이 줄 문서
- `poro_final_bosses_design.md`
- `poro_boss_rewards_detail.md`
- `poro_boss_reward_names.md`
- `poro_boss_ui_texts.md`

### Codex에게 시킬 작업
1. 극상위 보스 구현
2. 발악 패턴 연출
3. 명예의 전당 / 시즌 기록
4. 클리어 연출 / 기록 노출

---

## 11. 지금 당장 가장 현실적인 Codex 실행 순서

### 바로 시키기 좋은 순서
1. 멀티모듈 뼈대
2. core enum/DTO
3. DB schema 1차
4. admin-api 최소 엔드포인트
5. 보스 엔진 공용 구조
6. 생활 시스템 공용 구조
7. 결전 보스 1차
8. 생활 채집/제작/영지 1차
9. 운영 대시보드 MVP
10. 극상위/명예/후반 확장

---

## 12. 한 줄 요약

**Codex는 공용 구조 → DB/API → 보스 엔진 → 결전 보스 → 생활 공용 구조 → 생활 구현 → 운영 고도화 → 극상위 순서로 시키는 게 가장 안정적이다.**
