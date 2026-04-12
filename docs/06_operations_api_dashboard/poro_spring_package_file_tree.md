# 포로 서버 Spring 기준 패키지 / 클래스 파일 트리 초안

## 문서 목적
이 문서는 포로 서버의 웹/운영/게임 서버 구현을 실제 코드 구조로 옮기기 위한
**Spring 기준 패키지 / 클래스 파일 트리 초안**이다.

핵심 목표:
- 지금까지 정리한 초안 문서들을 실제 클래스 구조로 연결한다.
- 어떤 기능이 어떤 패키지에 들어갈지 먼저 고정한다.
- 이후 Java + Gradle + Spring 기반 구현에서 폴더 구조가 흔들리지 않게 한다.
- 운영 데이터, API, 보스 시스템, 통계/집계를 분리한다.

---

## 1. 큰 구조 원칙

### 1-1. 게임 서버 로직과 운영 웹 로직을 분리한다
포로 서버는 결국 아래 2축이 있다.

- **게임 서버 플러그인(Paper)**  
  실제 보스전, 전투, 아이템, 강화, 드랍, 입장 처리
- **운영 웹/관리 서버(Spring)**  
  대시보드, API, 통계, 디스코드 봇 연동, 기록 조회

이 둘은 한 저장소 안에 있더라도 **모듈 또는 패키지 레벨에서 명확히 분리**하는 게 좋다.

---

### 1-2. 추천 모듈 구조
가장 추천하는 건 멀티모듈이다.

```text
poro-server/
 ├─ poro-plugin/        # Paper 플러그인
 ├─ poro-admin-api/     # Spring 운영 API / 대시보드 백엔드
 ├─ poro-discord-bot/   # 디스코드 봇
 ├─ poro-core/          # 공용 도메인 / enum / DTO / 유틸
 └─ poro-build-logic/   # 선택 사항
```

처음엔 단일 모듈로도 가능하지만,
지금 네가 만들고 싶은 범위를 보면 **멀티모듈 쪽이 훨씬 낫다.**

---

## 2. 추천 저장소 루트 구조

```text
poro-server/
 ├─ build.gradle.kts
 ├─ settings.gradle.kts
 ├─ README.md
 ├─ docs/
 │   ├─ bosses/
 │   ├─ economy/
 │   ├─ admin/
 │   └─ architecture/
 ├─ poro-core/
 ├─ poro-plugin/
 ├─ poro-admin-api/
 └─ poro-discord-bot/
```

---

## 3. poro-core 구조

`poro-core`는 공용 모듈이다.

역할:
- enum
- 공용 DTO
- 공용 상수
- 공용 식별자 타입
- 공용 유틸
- 도메인 기본 모델

### 추천 구조
```text
poro-core/
 └─ src/main/java/com/poro/core/
    ├─ enums/
    │   ├─ BossTier.java
    │   ├─ ThemeType.java
    │   ├─ ClassType.java
    │   ├─ CurrencyType.java
    │   ├─ PatternCategory.java
    │   ├─ RecordType.java
    │   └─ AlertLevel.java
    ├─ ids/
    │   ├─ BossId.java
    │   ├─ UserId.java
    │   ├─ ItemId.java
    │   └─ PatternId.java
    ├─ dto/
    │   ├─ common/
    │   ├─ economy/
    │   ├─ boss/
    │   ├─ combat/
    │   ├─ growth/
    │   └─ season/
    ├─ constants/
    │   ├─ BossConstants.java
    │   ├─ EconomyConstants.java
    │   └─ TimeConstants.java
    └─ util/
        ├─ TimeRangeUtils.java
        ├─ MathUtils.java
        └─ JsonUtils.java
```

---

## 4. poro-plugin 구조

이게 실제 마인크래프트 서버 플러그인이다.

### 역할
- 직업/스킬/전투
- 보스 AI
- 보스룸
- 아이템/강화/잠재/제작
- 인게임 로그 적재
- 관리자 테스트 명령
- DB 이벤트 송신/저장

### 추천 구조
```text
poro-plugin/
 └─ src/main/java/com/poro/plugin/
    ├─ PoroPlugin.java
    ├─ bootstrap/
    │   ├─ PluginBootstrap.java
    │   ├─ ServiceRegistry.java
    │   └─ ConfigLoader.java
    ├─ config/
    │   ├─ PluginConfig.java
    │   ├─ BalanceConfig.java
    │   ├─ BossConfig.java
    │   └─ RewardConfig.java
    ├─ command/
    │   ├─ admin/
    │   │   ├─ BossAdminCommand.java
    │   │   ├─ EconomyAdminCommand.java
    │   │   └─ DebugAdminCommand.java
    │   └─ player/
    │       ├─ ClassCommand.java
    │       └─ BossEntryCommand.java
    ├─ boss/
    │   ├─ room/
    │   │   ├─ BossRoomManager.java
    │   │   ├─ BossInstance.java
    │   │   ├─ BossRunContext.java
    │   │   └─ BossRoomResetService.java
    │   ├─ engine/
    │   │   ├─ BossBattleEngine.java
    │   │   ├─ BossPhaseController.java
    │   │   ├─ BossPatternScheduler.java
    │   │   └─ BossStateController.java
    │   ├─ pattern/
    │   │   ├─ BossPattern.java
    │   │   ├─ BaseBossPattern.java
    │   │   ├─ cone/
    │   │   ├─ line/
    │   │   ├─ ground/
    │   │   ├─ safezone/
    │   │   ├─ summon/
    │   │   ├─ split/
    │   │   ├─ berserk/
    │   │   └─ special/
    │   ├─ impl/
    │   │   ├─ capital/
    │   │   ├─ west/
    │   │   ├─ east/
    │   │   ├─ north/
    │   │   ├─ south/
    │   │   ├─ sahar/
    │   │   └─ arkanon/
    │   ├─ reward/
    │   │   ├─ BossRewardService.java
    │   │   ├─ BossDropResolver.java
    │   │   └─ BossRewardDistributor.java
    │   └─ ui/
    │       ├─ BossUiService.java
    │       ├─ BossBarManager.java
    │       ├─ TitleMessenger.java
    │       ├─ ActionBarMessenger.java
    │       └─ GroundIndicatorService.java
    ├─ combat/
    │   ├─ engine/
    │   ├─ formula/
    │   ├─ damage/
    │   ├─ state/
    │   ├─ classsystem/
    │   ├─ weapon/
    │   └─ log/
    ├─ item/
    │   ├─ equipment/
    │   ├─ enhance/
    │   ├─ potential/
    │   ├─ crafting/
    │   ├─ transfer/
    │   └─ setbonus/
    ├─ economy/
    │   ├─ wallet/
    │   ├─ transaction/
    │   ├─ material/
    │   └─ trade/
    ├─ persistence/
    │   ├─ repository/
    │   ├─ entity/
    │   ├─ mapper/
    │   └─ transaction/
    ├─ analytics/
    │   ├─ collector/
    │   ├─ logger/
    │   └─ snapshot/
    └─ qa/
        ├─ command/
        ├─ preset/
        └─ simulator/
```

---

## 5. 보스 패키지 더 세부 추천

### 5-1. boss.impl 구조
테마별 / 보스별로 나누는 방식 추천

```text
boss/impl/
 ├─ capital/
 │   ├─ SerkainBoss.java
 │   └─ AurelBoss.java
 ├─ west/
 │   ├─ HazardBoss.java
 │   └─ NebkaBoss.java
 ├─ east/
 │   ├─ CasileaBoss.java
 │   └─ ElteronBoss.java
 ├─ north/
 │   ├─ MorgvainBoss.java
 │   └─ EldheimBoss.java
 ├─ south/
 │   ├─ RagnesBoss.java
 │   └─ AgnerBoss.java
 ├─ sahar/
 │   ├─ AzakelBoss.java
 │   └─ SetraBoss.java
 └─ arkanon/
     ├─ ArkenonBoss.java
     └─ CarmenBoss.java
```

### 5-2. 보스별 클래스 구성 추천
보스 하나당 전부 한 파일에 몰지 말고,
필요하면 아래처럼 나눠도 좋다.

```text
north/
 ├─ morgvain/
 │   ├─ MorgvainBoss.java
 │   ├─ MorgvainPatterns.java
 │   ├─ MorgvainPhaseScript.java
 │   └─ MorgvainUiTexts.java
```

처음엔 1파일 시작 가능하지만,
복잡해지면 위 구조가 유지보수에 좋다.

---

## 6. poro-admin-api 구조

이게 운영 웹/API 서버다.

### 역할
- 운영자 대시보드
- 관리자용 API
- 집계 조회
- 경고 조회
- 명예의 전당
- 경제/보스/전투 통계 조회

### 추천 구조
```text
poro-admin-api/
 └─ src/main/java/com/poro/admin/
    ├─ PoroAdminApiApplication.java
    ├─ config/
    │   ├─ WebSecurityConfig.java
    │   ├─ JacksonConfig.java
    │   ├─ CorsConfig.java
    │   └─ TimezoneConfig.java
    ├─ controller/
    │   ├─ AdminOverviewController.java
    │   ├─ AdminEconomyController.java
    │   ├─ AdminBossController.java
    │   ├─ AdminGrowthController.java
    │   ├─ AdminCombatController.java
    │   ├─ AdminSeasonController.java
    │   └─ AdminAlertController.java
    ├─ service/
    │   ├─ overview/
    │   ├─ economy/
    │   ├─ boss/
    │   ├─ growth/
    │   ├─ combat/
    │   ├─ season/
    │   └─ alert/
    ├─ dto/
    │   ├─ common/
    │   ├─ economy/
    │   ├─ boss/
    │   ├─ growth/
    │   ├─ combat/
    │   ├─ season/
    │   └─ overview/
    ├─ repository/
    │   ├─ raw/
    │   ├─ summary/
    │   └─ config/
    ├─ entity/
    │   ├─ log/
    │   ├─ summary/
    │   ├─ config/
    │   └─ master/
    ├─ mapper/
    ├─ facade/
    ├─ validator/
    └─ support/
        ├─ query/
        ├─ pagination/
        └─ response/
```

---

## 7. controller 구성 추천

### 예시
```text
controller/
 ├─ AdminOverviewController.java
 ├─ AdminEconomyController.java
 ├─ AdminBossController.java
 ├─ AdminCombatController.java
 ├─ AdminGrowthController.java
 ├─ AdminSeasonController.java
 └─ AdminAlertController.java
```

### 역할
- 각 도메인별 API endpoint만 담당
- 요청 파라미터 해석
- 서비스 호출
- DTO 반환

컨트롤러 안에 계산 로직 넣지 않는 걸 강하게 추천

---

## 8. service 구성 추천

### 예시
```text
service/
 ├─ overview/
 │   ├─ AdminOverviewService.java
 │   └─ AdminOverviewServiceImpl.java
 ├─ economy/
 │   ├─ AdminEconomyService.java
 │   └─ AdminEconomyServiceImpl.java
 ├─ boss/
 │   ├─ AdminBossService.java
 │   ├─ AdminBossSummaryService.java
 │   └─ AdminBossDetailService.java
 ├─ combat/
 ├─ growth/
 ├─ season/
 └─ alert/
```

### 역할
- 비즈니스 로직
- 집계 조합
- 경고 생성
- DTO 조립

---

## 9. repository / entity 구성 추천

### log entity
```text
entity/log/
 ├─ GoldTransactionEntity.java
 ├─ BossRunEntity.java
 ├─ BossRunParticipantEntity.java
 ├─ BossDeathEventEntity.java
 ├─ EnhancementLogEntity.java
 ├─ CubeLogEntity.java
 └─ CombatDamageLogEntity.java
```

### summary entity
```text
entity/summary/
 ├─ DailyEconomySummaryEntity.java
 ├─ DailyBossSummaryEntity.java
 ├─ DailyClassDpsSummaryEntity.java
 ├─ DailyEnhancementSummaryEntity.java
 └─ SeasonBossRecordEntity.java
```

### config entity
```text
entity/config/
 ├─ BossBalanceConfigEntity.java
 ├─ BossPartyScalingConfigEntity.java
 ├─ PatternBalanceConfigEntity.java
 └─ RewardDropConfigEntity.java
```

### repository
```text
repository/raw/
repository/summary/
repository/config/
```

---

## 10. poro-discord-bot 구조

### 역할
- 운영 요약 명령
- 경제 요약 명령
- 보스 통계 명령
- 극상위 현황
- 명예의 전당 조회

### 추천 구조
```text
poro-discord-bot/
 └─ src/main/java/com/poro/discord/
    ├─ PoroDiscordBotApplication.java
    ├─ command/
    │   ├─ overview/
    │   ├─ economy/
    │   ├─ boss/
    │   ├─ season/
    │   └─ admin/
    ├─ client/
    │   └─ AdminApiClient.java
    ├─ renderer/
    │   ├─ OverviewMessageRenderer.java
    │   ├─ EconomyMessageRenderer.java
    │   ├─ BossMessageRenderer.java
    │   └─ SeasonMessageRenderer.java
    ├─ dto/
    └─ config/
```

### 핵심
디코 봇은 직접 DB를 때리기보다  
**admin-api를 호출해서 렌더링만 하는 구조**가 좋다.

---

## 11. 추천 클래스 이름 예시

### 운영 API 응답
- `AdminApiResponse`
- `OverviewSummaryDto`
- `EconomySummaryDto`
- `BossDetailSummaryDto`
- `CombatBalanceDetailsDto`

### 플러그인 서비스
- `BossBattleEngine`
- `BossPhaseController`
- `BossPatternScheduler`
- `BossRewardDistributor`
- `BossUiService`
- `EnhancementService`
- `PotentialService`

### 분석 / 로그
- `BossRunLogger`
- `CombatAnalyticsCollector`
- `EconomySnapshotService`
- `DailySummaryBatchService`

---

## 12. 배치 / 스케줄 패키지 추천

운영 통계는 배치성 집계가 필요할 수 있다.

### admin-api 또는 별도 batch 패키지
```text
batch/
 ├─ DailyEconomySummaryJob.java
 ├─ DailyBossSummaryJob.java
 ├─ DailyCombatSummaryJob.java
 └─ AlertEvaluationJob.java
```

### 역할
- 일일 집계 생성
- 경고 조건 평가
- 대시보드용 요약 테이블 업데이트

---

## 13. 추천 패키지 트리 전체 예시

```text
poro-server/
 ├─ docs/
 ├─ poro-core/
 │   └─ src/main/java/com/poro/core/...
 ├─ poro-plugin/
 │   └─ src/main/java/com/poro/plugin/...
 ├─ poro-admin-api/
 │   └─ src/main/java/com/poro/admin/...
 └─ poro-discord-bot/
     └─ src/main/java/com/poro/discord/...
```

---

## 14. 구현 시작 시 우선 생성 추천 파일

처음부터 전부 만들지 말고, 이 정도부터 시작하는 걸 추천

### poro-core
- `BossTier.java`
- `ThemeType.java`
- `AlertLevel.java`
- `AdminApiResponse.java`
- `TimeRangeDto.java`
- `AlertDto.java`

### poro-plugin
- `PoroPlugin.java`
- `BossBattleEngine.java`
- `BossRoomManager.java`
- `BossPattern.java`
- `BossUiService.java`
- `EnhancementService.java`

### poro-admin-api
- `PoroAdminApiApplication.java`
- `AdminOverviewController.java`
- `AdminEconomyController.java`
- `AdminBossController.java`
- `AdminOverviewService.java`
- `AdminEconomyService.java`
- `AdminBossService.java`

### poro-discord-bot
- `PoroDiscordBotApplication.java`
- `AdminApiClient.java`
- `OverviewMessageRenderer.java`

---

## 15. 한 줄 요약

### 구조 철학
**게임 서버(플러그인), 운영 API(Spring), 디스코드 봇, 공용 코어 모듈을 분리하고, 공통 enum/DTO는 core로 모은다.**

### 구현 철학
**플러그인은 게임 로직, admin-api는 조회/집계/API, discord-bot은 렌더링, core는 공통 타입을 담당한다.**

---

## 16. 다음으로 가야 할 것

### 1순위
운영자 대시보드 화면별 컴포넌트 목록 정리
- 카드
- 표
- 그래프
- 경고 위젯
- 필터

### 2순위
테마별 보이스 / 사운드 방향 정리
- 패턴 대표 사운드
- 발악 패턴 사운드
- 클리어 / 실패 사운드

### 3순위
Gradle 멀티모듈 구조 초안
- settings.gradle.kts
- 각 모듈 의존관계
- 공용 라이브러리 배치
