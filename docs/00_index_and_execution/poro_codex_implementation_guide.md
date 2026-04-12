# 포로 서버 Codex 구현 가이드 초안

## 문서 목적
이 문서는 지금까지 정리한 포로 서버 기획/구조 문서를 바탕으로,
**Codex가 실제로 코드를 작성할 수 있도록 구현 목표, 모듈 구조, 우선순위, 산출물 형식**을 더 구체적으로 정리한 문서다.

이 문서는 “기획 설명문”이 아니라, **Codex에게 넘길 구현 지시서**에 가깝다.

---

## 1. 최우선 전제

### 1-1. 기술 스택
- 게임 서버: **Paper 1.21.10**
- Java: **Java 21**
- 빌드: **Gradle Kotlin DSL**
- 운영 API: **Spring Boot**
- DB: 초기 **SQLite**
- 디스코드 봇: Java 또는 Kotlin 가능하지만, 가능하면 현재 백엔드와 쉽게 맞물리도록 **Java 기준** 추천

### 1-2. 프로젝트 목표
이 프로젝트는 단순한 보스 플러그인 1개가 아니다.

핵심 목표는 아래 4개다.
- 마인크래프트 RPG 서버용 **커스텀 보스/전투 시스템**
- 운영자가 직접 수치를 보고 통제할 수 있는 **운영 데이터 수집 구조**
- 웹 대시보드/디스코드/관리자 명령이 같은 데이터를 보는 **운영 API 구조**
- 이후 확장 가능한 **멀티모듈 구조**

### 1-3. 절대 원칙
**운영자가 직접 보고 통제할 수 없는 핵심 수치는 존재하면 안 된다.**

따라서 Codex가 구현할 때도 아래 원칙을 지켜야 한다.
- 로그를 남길 수 있는 구조로 설계
- 설정값은 하드코딩보다 config / DB 설정 테이블 / enum 기반으로 분리
- 웹/봇/게임 명령이 서로 다른 기준값을 보지 않게 설계
- 공용 패턴/공용 DTO/공용 enum 재사용

---

## 2. Codex에게 기대하는 산출물

Codex는 한 번에 “완성본”을 만들려고 하기보다, 아래 순서대로 점진적으로 만들어야 한다.

### 2-1. 1차 산출물
- Gradle 멀티모듈 프로젝트 뼈대
- `poro-core`
- `poro-plugin`
- `poro-admin-api`
- `poro-discord-bot`
- 공용 enum / DTO / 기본 클래스
- 최소 실행 가능한 플러그인/스프링 앱/디스코드 앱 부트스트랩

### 2-2. 2차 산출물
- 보스룸 / 보스 전투 엔진 / 패턴 모듈 인터페이스
- 관리자 테스트 명령 최소 세트
- SQLite 연결 및 최소 테이블 생성
- 운영 API 최소 엔드포인트

### 2-3. 3차 산출물
- 결전 보스 1차 구현 (남부 → 북부 → 서부 우선)
- 운영 대시보드용 summary API
- 로그 적재 및 집계 배치 뼈대

---

## 3. 반드시 먼저 읽어야 할 문서 목록

Codex는 아래 md 파일들을 먼저 읽고, 그 기준을 우선해야 한다.

### 핵심 기획 문서
- `poro_final_bosses_design.md`
- `poro_boss_rewards_detail.md`
- `poro_boss_ui_alerts.md`
- `poro_boss_dev_priority.md`
- `poro_boss_reward_names.md`
- `poro_boss_ui_texts.md`

### 운영/데이터 문서
- `poro_db_web_discord_metrics.md`
- `poro_balance_control_sheet.md`
- `poro_db_table_design.md`
- `poro_operator_dashboard_layout.md`
- `poro_operator_dashboard_mvp.md`
- `poro_api_bot_response_formats.md`
- `poro_api_dto_response_classes.md`
- `poro_spring_package_file_tree.md`

Codex는 위 문서들 기준을 서로 충돌 없이 최대한 보존해야 한다.

---

## 4. 구현 우선순위

### 4-1. 제일 먼저 구현할 것
1. Gradle 멀티모듈 구조
2. 공용 core 모듈
3. plugin / admin-api / discord-bot 부트스트랩
4. SQLite 연결 및 기본 repository 계층
5. 최소 관리자 명령
6. 최소 운영 API

### 4-2. 보스 구현 순서
결전 보스 먼저 간다.

추천 순서:
1. 남부 결전 - 라그네스
2. 북부 결전 - 모르바인
3. 서부 결전 - 하자드
4. 수도 결전 - 세르카인
5. 동부 결전 - 카실레아
6. 아르카논 결전 - 아르케논
7. 사하르 결전 - 아자켈

극상위는 후반에 간다.

---

## 5. 구현 단위별 Codex 작업 지시

아래는 Codex에게 직접 줄 수 있는 작업 단위 예시다.

### 작업 단위 A. Gradle 멀티모듈 뼈대 생성
목표:
- `poro-core`
- `poro-plugin`
- `poro-admin-api`
- `poro-discord-bot`
멀티모듈 프로젝트 생성

요구사항:
- Gradle Kotlin DSL 사용
- Java 21 설정
- 루트 `settings.gradle.kts`에서 모듈 include
- 공통 버전 관리는 루트에서
- `poro-core`는 다른 모든 모듈이 참조 가능
- `poro-plugin`은 Paper API 의존
- `poro-admin-api`는 Spring Boot 의존
- `poro-discord-bot`은 Discord 라이브러리 의존

산출물:
- 실제 폴더 구조
- 루트 gradle 파일
- 각 모듈 gradle 파일
- 최소 부트스트랩 클래스

---

### 작업 단위 B. core 모듈 구현
목표:
공용 enum / DTO / 기본 응답 클래스 추가

반드시 포함:
- `BossTier`
- `ThemeType`
- `ClassType`
- `CurrencyType`
- `AlertLevel`
- `RecordType`
- `AdminApiResponse`
- `TimeRangeDto`
- `AlertDto`

요구사항:
- record 사용 가능하면 record 우선
- DTO는 불변 구조 선호
- enum은 문자열 매핑이 흔들리지 않게 명확한 이름 사용

---

### 작업 단위 C. plugin 최소 전투 뼈대
목표:
실제 보스 구현 전, 공용 전투 엔진 뼈대 만들기

반드시 포함:
- `PoroPlugin`
- `BossBattleEngine`
- `BossRoomManager`
- `BossPhaseController`
- `BossPattern` 인터페이스
- `BossUiService`
- `BossAdminCommand`

요구사항:
- 실제 세부 패턴 로직은 비워도 됨
- 대신 클래스 책임과 호출 흐름이 보여야 함
- 나중에 결전/극상위 보스가 붙을 수 있도록 확장성 있게 작성

---

### 작업 단위 D. SQLite 및 DB 테이블 뼈대
목표:
최소 운영용 데이터 저장 구조 구현

1차 테이블 우선:
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

요구사항:
- Flyway 또는 직접 schema.sql 중 하나 선택
- 초기엔 단순해도 되지만, 나중에 확장 가능한 키/컬럼 네이밍 유지
- raw log / summary / config 성격 분리

---

### 작업 단위 E. admin-api 최소 구현
목표:
운영 메인 / 경제 / 보스 / 전투 밸런스 최소 API 제공

우선 엔드포인트:
- `/api/admin/overview`
- `/api/admin/economy/summary`
- `/api/admin/bosses/summary`
- `/api/admin/combat/class-balance`
- `/api/admin/alerts`

요구사항:
- 응답은 `AdminApiResponse` 구조
- summary / details / alerts 구조 유지
- 아직 데이터가 적어도 mock 또는 placeholder 가능
- 단, DTO 구조는 문서 기준 유지

---

### 작업 단위 F. 관리자 명령 최소 구현
목표:
QA 생산성 확보

최소 필요 명령:
- `/boss test start <boss_id>`
- `/boss phase set <n>`
- `/boss pattern cast <pattern_id>`
- `/boss state hp <value>`
- `/boss debug state`
- `/boss reset`

요구사항:
- 명령 파싱 구조 분리
- 이후 확장 가능한 subcommand 구조
- 관리자 권한 체크 포함

---

## 6. 코딩 스타일 요구사항

### 6-1. 클래스 책임 분리
Codex는 한 클래스에 모든 걸 몰아넣지 말아야 한다.

예:
- Controller: 요청/응답
- Service: 비즈니스 로직
- Repository: DB 접근
- Mapper: entity ↔ dto 변환
- Domain/Engine: 실제 전투 규칙

### 6-2. 하드코딩 최소화
아래는 하드코딩하지 않는 게 좋다.
- 보스 체력
- 방어력
- 패턴 쿨타임
- 드랍량
- 무력화 시간
- 발악 타이머

가능하면:
- config 파일
- DB config table
- enum + registry
로 분리

### 6-3. 로그 친화적 구현
주요 이벤트마다 로그 수집 지점이 있어야 한다.

예:
- 보스 입장
- 보스 클리어
- 전멸
- 패턴 사망
- 강화 성공/실패
- 큐브 사용
- 골드 생성/소모

---

## 7. Codex에게 주면 좋은 프롬프트 스타일

아래처럼 주는 게 좋다.

### 예시 1
“첨부한 md 문서들을 기준으로 Java 21 + Gradle Kotlin DSL 멀티모듈 프로젝트를 생성해줘.  
모듈은 `poro-core`, `poro-plugin`, `poro-admin-api`, `poro-discord-bot`이고,  
먼저 실행 가능한 최소 뼈대와 공용 enum/DTO부터 만들어줘.  
DTO/enum 이름은 문서 기준을 최대한 유지해줘.”

### 예시 2
“`poro_db_table_design.md`와 `poro_api_dto_response_classes.md` 기준으로  
SQLite용 schema와 Spring Boot admin-api의 entity/repository/service/controller 뼈대를 작성해줘.  
`/api/admin/overview`, `/api/admin/economy/summary`, `/api/admin/bosses/summary`가 동작하는 최소 코드부터 만들어줘.”

### 예시 3
“`poro_boss_dev_priority.md`와 `poro_final_bosses_design.md` 기준으로  
남부 결전 보스 라그네스를 먼저 구현할 수 있는 보스 엔진 뼈대와 패턴 인터페이스 구조를 작성해줘.  
실제 패턴 수치보다 구조/확장성을 우선해줘.”

---

## 8. Codex에게 하지 말아야 할 요구

아래는 한 번에 던지지 않는 게 좋다.

- “전체 서버를 전부 완성해줘”
- “보스, 웹, 디코, DB, UI를 한 번에 다 만들어줘”
- “세부 수치까지 완벽하게 확정해서 구현해줘”

이런 식으로 가면 결과가 엉키기 쉽다.

대신:
- 멀티모듈 뼈대
- core DTO/enum
- DB schema
- admin-api 최소 엔드포인트
- plugin 전투 엔진 뼈대
- 결전 보스 1마리
순으로 쪼개는 게 좋다.

---

## 9. 추천 실제 Codex 작업 순서

### 1차
- Gradle 멀티모듈 생성
- core enum/DTO 생성
- plugin/admin-api/discord-bot 부트스트랩

### 2차
- DB schema + repository
- admin-api 최소 summary 엔드포인트
- 관리자 명령 최소 세트

### 3차
- 보스룸 + 보스 전투 엔진
- 패턴 인터페이스 + 공용 패턴 모듈
- 라그네스 구현

### 4차
- 모르바인 / 하자드
- 로그 적재
- 일일 집계 배치

### 5차
- 대시보드 / 디코봇 연동
- 결전 보스 확대
- 극상위 후반 구현

---

## 10. 최종 한 줄 요약

**Codex에게는 “완성본”을 한 번에 시키지 말고, 지금까지 만든 md 문서들을 기준으로 멀티모듈 뼈대 → 공용 타입 → DB/API → 보스 엔진 → 결전 보스 순서로 단계적으로 구현시키는 게 가장 안정적이다.**
