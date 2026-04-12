# 포로 서버 API DTO / 응답 클래스 초안

## 문서 목적
이 문서는 포로 서버 운영용 웹 API, 디스코드 봇, 관리자 명령이 공통으로 참조할 수 있는
**Java 기준 DTO / 응답 클래스 구조**를 정리한 초안이다.

핵심 목표:
- 필드명을 먼저 고정한다.
- enum과 응답 구조를 통일한다.
- 웹/봇/관리자 명령이 같은 서비스 계층을 바라보게 한다.
- 이후 Spring + Java 구현으로 자연스럽게 옮길 수 있게 한다.

---

## 1. 기본 철학

### 1-1. API 응답은 얇고, 서비스 계층이 두껍게
DTO는 “전달용 객체”다.

즉:
- 계산 로직은 서비스 계층
- DTO는 결과를 담는 그릇
- 컨트롤러는 DTO를 그대로 반환

---

### 1-2. 공통 응답 래퍼를 둔다
운영 API는 대부분 아래 구조를 공유한다.

- 성공 여부
- 생성 시각
- 시간 범위
- summary
- details
- alerts

그래서 공통 래퍼를 먼저 두는 게 좋다.

---

### 1-3. enum을 먼저 고정한다
나중에 문자열이 제각각이면
- DB 값
- Java 코드
- API 응답
- 디스코드 명령
이 다 어긋난다.

그래서 중요한 분류값은 enum으로 고정하는 게 좋다.

---

## 2. 공통 패키지 구조 추천

```text
com.poro.server.api.admin.dto
com.poro.server.api.admin.enums
com.poro.server.api.admin.response
com.poro.server.api.admin.request
com.poro.server.domain.*
```

### 추천 세부 구조
```text
admin/dto/common
admin/dto/economy
admin/dto/boss
admin/dto/combat
admin/dto/growth
admin/dto/season
admin/enums
admin/response
```

---

## 3. 공통 응답 클래스

## 3-1. AdminApiResponse<TSummary, TDetails>

```java
public record AdminApiResponse<TSummary, TDetails>(
    boolean ok,
    OffsetDateTime generatedAt,
    TimeRangeDto timeRange,
    TSummary summary,
    TDetails details,
    List<AlertDto> alerts
) {}
```

### 역할
모든 운영 API 공통 래퍼

---

## 3-2. TimeRangeDto

```java
public record TimeRangeDto(
    OffsetDateTime from,
    OffsetDateTime to,
    String label
) {}
```

### label 예시
- today
- last_7_days
- last_30_days
- season_current

---

## 3-3. AlertDto

```java
public record AlertDto(
    AlertLevel level,
    String code,
    String message
) {}
```

---

## 3-4. AlertLevel enum

```java
public enum AlertLevel {
    INFO,
    WARNING,
    CRITICAL
}
```

---

## 4. 공통 enum 초안

## 4-1. BossTier

```java
public enum BossTier {
    FIELD,
    STORY,
    APEX,
    DEPTH,
    FINAL,
    EXTREME
}
```

---

## 4-2. ThemeType

```java
public enum ThemeType {
    CAPITAL,
    WEST,
    EAST,
    NORTH,
    SOUTH,
    SAHAR,
    ARKANON
}
```

---

## 4-3. ClassType

```java
public enum ClassType {
    WARRIOR,
    ASSASSIN,
    MAGE
}
```

---

## 4-4. CurrencyType

```java
public enum CurrencyType {
    GOLD,
    CUBE,
    ENHANCEMENT_STONE,
    SMUGGLE_COIN,
    MAGIC_ESSENCE
}
```

---

## 4-5. TransactionDirection

```java
public enum TransactionDirection {
    INFLOW,
    OUTFLOW
}
```

---

## 4-6. RunType

```java
public enum RunType {
    NORMAL,
    MATCH,
    DIRECT,
    TEST_ADMIN
}
```

---

## 4-7. RecordType

```java
public enum RecordType {
    FIRST_CLEAR,
    BEST_TIME,
    NO_DEATH,
    SOLO_CLEAR,
    EXTREME_CLEAR
}
```

---

## 5. 경제 DTO

## 5-1. EconomySummaryDto

```java
public record EconomySummaryDto(
    long totalGold,
    long goldInflow,
    long goldOutflow,
    long goldNet,
    long tradeVolume
) {}
```

---

## 5-2. EconomySourceAmountDto

```java
public record EconomySourceAmountDto(
    String source,
    long amount
) {}
```

---

## 5-3. EconomyDetailsDto

```java
public record EconomyDetailsDto(
    List<EconomySourceAmountDto> topInflowSources,
    List<EconomySourceAmountDto> topOutflowSources
) {}
```

---

## 6. 보스 DTO

## 6-1. BossSummaryDto

```java
public record BossSummaryDto(
    long attemptCount,
    long clearCount,
    double clearRate,
    Double avgClearTimeSeconds,
    double berserkReachRate,
    double berserkClearRate
) {}
```

---

## 6-2. BossListItemDto

```java
public record BossListItemDto(
    String bossId,
    String bossName,
    BossTier tier,
    ThemeType theme,
    long attemptCount,
    long clearCount,
    double clearRate,
    Double avgClearTimeSeconds,
    String mostDangerousPattern
) {}
```

---

## 6-3. BossDetailsDto

```java
public record BossDetailsDto(
    List<BossListItemDto> bosses
) {}
```

---

## 6-4. BossPartySizeBreakdownDto

```java
public record BossPartySizeBreakdownDto(
    int partySize,
    long attemptCount,
    long clearCount
) {}
```

---

## 6-5. BossPhaseReachDto

```java
public record BossPhaseReachDto(
    String phase,
    double reachRate
) {}
```

---

## 6-6. DangerousPatternDto

```java
public record DangerousPatternDto(
    String patternId,
    long deathCount
) {}
```

---

## 6-7. BossDetailSummaryDto

```java
public record BossDetailSummaryDto(
    String bossId,
    String bossName,
    BossTier tier,
    ThemeType theme,
    long attemptCount7d,
    long clearCount7d,
    double clearRate7d,
    Double avgClearTimeSeconds7d
) {}
```

---

## 6-8. BossDetailDetailsDto

```java
public record BossDetailDetailsDto(
    List<BossPartySizeBreakdownDto> partySizeBreakdown,
    List<BossPhaseReachDto> phaseReachRates,
    List<DangerousPatternDto> dangerousPatterns
) {}
```

---

## 7. 전투 밸런스 DTO

## 7-1. CombatBalanceSummaryDto

```java
public record CombatBalanceSummaryDto(
    double maxAvgDps,
    double minAvgDps,
    double spreadPercent
) {}
```

---

## 7-2. ClassBalanceItemDto

```java
public record ClassBalanceItemDto(
    ClassType classType,
    double avgDps,
    double survivalRate
) {}
```

---

## 7-3. WeaponBalanceItemDto

```java
public record WeaponBalanceItemDto(
    String weaponId,
    String weaponName,
    double avgDps
) {}
```

---

## 7-4. CombatBalanceDetailsDto

```java
public record CombatBalanceDetailsDto(
    List<ClassBalanceItemDto> classes,
    List<WeaponBalanceItemDto> weapons
) {}
```

---

## 8. 성장 DTO

## 8-1. GrowthSummaryDto

```java
public record GrowthSummaryDto(
    double t1UserRatio,
    double t2UserRatio,
    double avgEnhanceLevel,
    long usersAt18Plus
) {}
```

---

## 8-2. EnhancementDistributionDto

```java
public record EnhancementDistributionDto(
    int level,
    long count
) {}
```

---

## 8-3. PotentialGradeDistributionDto

```java
public record PotentialGradeDistributionDto(
    String grade,
    long count
) {}
```

---

## 8-4. GrowthDetailsDto

```java
public record GrowthDetailsDto(
    List<EnhancementDistributionDto> enhancementDistribution,
    List<PotentialGradeDistributionDto> potentialGrades
) {}
```

---

## 9. 시즌 / 명예의 전당 DTO

## 9-1. SeasonHallOfFameSummaryDto

```java
public record SeasonHallOfFameSummaryDto(
    String seasonId,
    long finalFirstClearCount,
    long extremeFirstClearCount,
    long extremeUnclearedCount
) {}
```

---

## 9-2. HallOfFameRecordDto

```java
public record HallOfFameRecordDto(
    String bossId,
    String bossName,
    RecordType recordType,
    List<String> partyNames,
    OffsetDateTime clearAt
) {}
```

---

## 9-3. SeasonHallOfFameDetailsDto

```java
public record SeasonHallOfFameDetailsDto(
    List<HallOfFameRecordDto> records
) {}
```

---

## 10. 운영 메인 대시보드 DTO

## 10-1. OverviewSummaryDto

```java
public record OverviewSummaryDto(
    long totalGold,
    long goldNetToday,
    long finalClearCountToday,
    long extremeAttemptCountToday,
    boolean firstClearOccurredToday,
    double bossClearRateDeltaPercent,
    double classDpsSpreadPercent
) {}
```

---

## 10-2. OverviewWarningItemDto

```java
public record OverviewWarningItemDto(
    AlertLevel level,
    String code,
    String title,
    String message,
    String targetPath
) {}
```

---

## 10-3. OverviewDetailsDto

```java
public record OverviewDetailsDto(
    List<OverviewWarningItemDto> warnings
) {}
```

---

## 11. 디스코드 봇 응답용 DTO

디스코드는 최종적으로 문자열 렌더링이 많지만,
중간 DTO를 하나 두면 편하다.

## 11-1. DiscordCommandResponseDto

```java
public record DiscordCommandResponseDto(
    String title,
    List<String> lines,
    List<AlertDto> alerts
) {}
```

### 예시 용도
- 운영 요약
- 보스 요약
- 경제 요약
- 극상위 현황

---

## 12. 관리자 명령 응답용 DTO

게임 안 관리자 명령도 공통 포맷을 두면 좋다.

## 12-1. AdminCommandResponseDto

```java
public record AdminCommandResponseDto(
    String header,
    List<String> bodyLines,
    List<String> warningLines
) {}
```

---

## 13. 요청 DTO 초안

## 13-1. RangeQueryRequest

```java
public record RangeQueryRequest(
    String range,
    OffsetDateTime from,
    OffsetDateTime to
) {}
```

### 메모
- range 기반 빠른 조회
- 필요 시 from/to 직접 지정

---

## 13-2. BossDetailQueryRequest

```java
public record BossDetailQueryRequest(
    String bossId,
    String range
) {}
```

---

## 14. 서비스 계층 반환 구조 추천

컨트롤러가 직접 DB를 다루지 않게 하려면
서비스 계층도 DTO 친화적으로 정리하는 게 좋다.

### 예시
```java
public interface AdminEconomyService {
    AdminApiResponse<EconomySummaryDto, EconomyDetailsDto> getEconomySummary(RangeQueryRequest request);
}
```

```java
public interface AdminBossService {
    AdminApiResponse<BossSummaryDto, BossDetailsDto> getBossSummary(RangeQueryRequest request);
    AdminApiResponse<BossDetailSummaryDto, BossDetailDetailsDto> getBossDetail(BossDetailQueryRequest request);
}
```

---

## 15. 필드명 규칙 추천

### 숫자
- 비율은 `Rate`
- 편차는 `SpreadPercent`
- 시간은 `Seconds`
- 수량은 `Count`
- 총합은 `Total`
- 평균은 `Avg`

### 예시
- `clearRate`
- `avgClearTimeSeconds`
- `attemptCount`
- `goldNet`
- `spreadPercent`

이렇게 통일하면 좋다.

---

## 16. enum 추가 후보

나중에 필요할 가능성이 높은 enum

### PatternCategory
```java
public enum PatternCategory {
    CONE,
    LINE_AOE,
    GROUND_AOE,
    SAFE_ZONE,
    SUMMON,
    SPLIT_ARENA,
    MARK_EXPLODE,
    STAGGER,
    BERSERK
}
```

### MaterialCategory
```java
public enum MaterialCategory {
    BOSS_CORE,
    COMMON_CRAFT,
    DEPTH_MATERIAL,
    SYMBOLIC_EXTREME,
    MARKET_TOKEN
}
```

### FeatureFlagType
```java
public enum FeatureFlagType {
    EXTREME_BOSS_REWARDS_ENABLED,
    HALL_OF_FAME_ENABLED,
    BOSS_DEBUG_LOGS_ENABLED
}
```

---

## 17. 추천 실제 구현 순서

### 1단계
공통 응답
- `AdminApiResponse`
- `TimeRangeDto`
- `AlertDto`
- `AlertLevel`

### 2단계
핵심 운영 DTO
- `OverviewSummaryDto`
- `EconomySummaryDto`
- `BossSummaryDto`
- `CombatBalanceSummaryDto`

### 3단계
상세 DTO
- 보스 상세
- 성장 상세
- 시즌/명예 상세

### 4단계
디스코드/관리자 전용 응답 DTO

---

## 18. 한 줄 요약

### DTO 철학
**API는 공통 래퍼 + 요약(summary) + 상세(details) + 경고(alerts) 구조로 통일한다.**

### 구현 철학
**웹/디스코드/관리자 명령은 서로 다른 렌더링을 하더라도, 같은 서비스 계층 DTO를 기반으로 움직인다.**

---

## 19. 다음으로 가야 할 것

### 1순위
테마별 보이스 / 사운드 방향 정리
- 패턴 대표 사운드
- 발악 패턴 사운드
- 클리어 / 실패 사운드

### 2순위
운영자 대시보드 화면별 컴포넌트 목록 정리
- 카드
- 표
- 그래프
- 경고 위젯

### 3순위
Spring 기준 실제 패키지 / 클래스 파일 트리 초안
- dto
- response
- controller
- service
- repository
