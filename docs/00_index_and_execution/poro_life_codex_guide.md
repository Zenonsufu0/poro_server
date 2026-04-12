# 포로 서버 생활 시스템 Codex 구현 지시서 초안

## 문서 목적
이 문서는 지금까지 정리한 포로 서버 생활 시스템 문서들을 바탕으로,
**Codex가 실제 코드 작성을 시작할 수 있도록 구현 단위와 요구사항을 구체화한 지시서**다.

이 문서는 생활 시스템 전체를 한 번에 완성하라는 문서가 아니라,
Codex가 **뼈대 → DB → 서비스 → UI → 경제 연결** 순서로 안정적으로 구현할 수 있도록 쪼개는 문서다.

---

## 1. Codex가 먼저 읽어야 하는 문서

Codex는 아래 문서를 먼저 읽고, 여기 적힌 기준을 우선해야 한다.

### 생활 시스템 문서
- `poro_life_system_details.md`
- `poro_life_resource_output_table.md`
- `poro_estate_production_structure.md`
- `poro_life_economy_trade_structure.md`

### 공용 구조 문서
- `poro_db_table_design.md`
- `poro_api_dto_response_classes.md`
- `poro_spring_package_file_tree.md`
- `poro_codex_implementation_guide.md`

---

## 2. 생활 시스템 구현 최우선 원칙

### 2-1. 생활은 메인 성장 루트가 아니다
Codex는 생활 시스템을 구현할 때,
생활이 전투/보스/던전/퀘스트를 밀어내는 구조로 만들면 안 된다.

즉:
- 최종 장비 핵심 재료를 생활 전용으로 두지 말 것
- 생활만으로 성장 메타가 완성되지 않게 할 것
- 생활은 보조 재료 / 소모품 / 거래 루트 중심으로 갈 것

---

### 2-2. 필드 채집과 영지 생산은 역할이 달라야 한다
- 필드 채집 = 즉시 수급 / 희귀 부산물 / 적극 플레이 보상
- 영지 생산 = 기본 재료 안정 수급 / 편의성 / 장기 누적

Codex는 영지 생산이 필드 채집을 완전히 대체하지 않게 구조를 잡아야 한다.

---

### 2-3. 생활도 운영 통제 대상이다
생활 시스템 구현 시에도 반드시
- 로그
- 현재 상태
- 집계 가능성
을 고려해야 한다.

최소한 아래는 추후 통계화 가능해야 한다.
- 재료 획득량
- 재료 소비량
- 영지 생산량
- 수확량
- 음식/물약 제작량
- 거래량

---

## 3. 1차 구현 목표

생활 시스템은 처음부터 전부 구현하지 말고, 1차 범위를 명확히 잡는다.

### 1차 구현 범위
#### 채집 계열
- 채집
- 채광
- 벌목
- 낚시

#### 제작 계열
- 요리
- 연금
- 제련/가공

#### 영지 계열
- 청사진 해금
- 생산 시설 설치
- 실시간 누적 생산
- 수확
- Lv1~Lv3 업그레이드

#### 경제 계열
- 재료 거래 가능 구조
- 가공 재료 / 음식 / 물약 거래 가능 구조

---

## 4. Codex에게 요구하는 작업 순서

### 작업 1. 생활 공용 enum / 식별자 / 분류 추가
`poro-core` 또는 공용 모듈에 먼저 넣어야 한다.

#### 최소 enum 후보
- `LifeSkillType`
- `LifeResourceTier`
- `LifeSourceType`
- `EstateFacilityType`
- `CraftCategory`
- `ResourceUsageCategory`

### 예시
```java
public enum LifeSkillType {
    GATHERING,
    MINING,
    LOGGING,
    FISHING,
    COOKING,
    ALCHEMY,
    REFINING
}
```

```java
public enum LifeResourceTier {
    BASIC,
    RARE_BYPRODUCT,
    PROCESSED,
    CONSUMABLE
}
```

```java
public enum LifeSourceType {
    FIELD,
    ESTATE,
    CRAFT,
    BOSS,
    EXCHANGE
}
```

---

### 작업 2. DB 테이블 추가
생활 시스템용 최소 테이블을 먼저 만든다.

#### 최소 필요 테이블
- `life_resources`
- `user_life_skills`
- `user_life_resource_balances`
- `life_resource_transactions`
- `estate_blueprints`
- `user_estate_blueprints`
- `estate_facilities`
- `user_estate_facilities`
- `estate_production_logs`
- `estate_harvest_logs`
- `life_crafting_recipes`
- `life_crafting_logs`

---

## 5. 추천 DB 테이블 초안

## 5-1. life_resources
생활 자원 마스터

### 컬럼 예시
- `resource_id` TEXT PK
- `resource_name` TEXT
- `life_skill_type` TEXT
- `resource_tier` TEXT
- `theme_type` TEXT NULL
- `is_field_only` INTEGER
- `is_estate_producible` INTEGER
- `is_rare_byproduct` INTEGER
- `usage_category` TEXT
- `is_tradeable` INTEGER

---

## 5-2. user_life_skills
유저 생활 숙련도

### 컬럼 예시
- `user_id` TEXT
- `life_skill_type` TEXT
- `level` INTEGER
- `exp` INTEGER
- `updated_at` DATETIME

### PK 추천
- `(user_id, life_skill_type)`

---

## 5-3. user_life_resource_balances
유저 생활 자원 보유량

### 컬럼 예시
- `user_id` TEXT
- `resource_id` TEXT
- `amount` INTEGER
- `updated_at` DATETIME

### PK 추천
- `(user_id, resource_id)`

---

## 5-4. life_resource_transactions
생활 자원 획득/소모 로그

### 컬럼 예시
- `tx_id` TEXT PK
- `user_id` TEXT
- `resource_id` TEXT
- `amount` INTEGER
- `direction` TEXT
- `source_type` TEXT
- `source_detail` TEXT
- `created_at` DATETIME

### source_type 예시
- field_gather
- estate_production
- craft_use
- craft_result
- npc_exchange
- trade

---

## 5-5. estate_blueprints
영지 청사진 마스터

### 컬럼 예시
- `blueprint_id` TEXT PK
- `blueprint_name` TEXT
- `facility_type` TEXT
- `theme_type` TEXT NULL
- `required_estate_level` INTEGER
- `is_active` INTEGER

---

## 5-6. user_estate_blueprints
유저 청사진 해금 상태

### 컬럼 예시
- `user_id` TEXT
- `blueprint_id` TEXT
- `unlocked_at` DATETIME

### PK 추천
- `(user_id, blueprint_id)`

---

## 5-7. estate_facilities
시설 마스터

### 컬럼 예시
- `facility_id` TEXT PK
- `facility_name` TEXT
- `facility_type` TEXT
- `theme_type` TEXT NULL
- `base_cycle_seconds` INTEGER
- `base_storage_limit` INTEGER
- `max_level` INTEGER

---

## 5-8. user_estate_facilities
유저 영지 설치 시설 상태

### 컬럼 예시
- `user_facility_id` TEXT PK
- `user_id` TEXT
- `facility_id` TEXT
- `level` INTEGER
- `slot_index` INTEGER
- `stored_amount` INTEGER
- `last_produced_at` DATETIME
- `last_harvested_at` DATETIME
- `created_at` DATETIME
- `updated_at` DATETIME

---

## 5-9. estate_production_logs
영지 생산 로그

### 컬럼 예시
- `production_log_id` TEXT PK
- `user_id` TEXT
- `user_facility_id` TEXT
- `resource_id` TEXT
- `amount_produced` INTEGER
- `is_rare_byproduct` INTEGER
- `created_at` DATETIME

---

## 5-10. estate_harvest_logs
영지 수확 로그

### 컬럼 예시
- `harvest_log_id` TEXT PK
- `user_id` TEXT
- `user_facility_id` TEXT
- `resource_id` TEXT
- `amount_harvested` INTEGER
- `created_at` DATETIME

---

## 5-11. life_crafting_recipes
생활 제작식

### 컬럼 예시
- `recipe_id` TEXT PK
- `recipe_name` TEXT
- `craft_category` TEXT
- `result_resource_id` TEXT NULL
- `result_item_id` TEXT NULL
- `result_amount` INTEGER
- `required_skill_type` TEXT
- `required_skill_level` INTEGER
- `is_active` INTEGER

---

## 5-12. life_crafting_logs
생활 제작 로그

### 컬럼 예시
- `craft_log_id` TEXT PK
- `user_id` TEXT
- `recipe_id` TEXT
- `craft_category` TEXT
- `result_code` TEXT
- `result_amount` INTEGER
- `created_at` DATETIME

---

## 6. 패키지 구조 추천

### core
```text
poro-core/
 └─ enums/
     ├─ LifeSkillType.java
     ├─ LifeResourceTier.java
     ├─ LifeSourceType.java
     ├─ EstateFacilityType.java
     └─ CraftCategory.java
```

### plugin
```text
poro-plugin/
 └─ life/
    ├─ gather/
    ├─ craft/
    ├─ estate/
    ├─ command/
    ├─ ui/
    ├─ service/
    ├─ repository/
    └─ log/
```

### 더 세부 추천
```text
life/
 ├─ gather/
 │   ├─ GatherNodeService.java
 │   ├─ GatherRewardResolver.java
 │   └─ GatherExpService.java
 ├─ craft/
 │   ├─ LifeCraftService.java
 │   ├─ RecipeResolver.java
 │   └─ CraftValidationService.java
 ├─ estate/
 │   ├─ EstateBlueprintService.java
 │   ├─ EstateFacilityService.java
 │   ├─ EstateProductionService.java
 │   ├─ EstateHarvestService.java
 │   └─ EstateUpgradeService.java
 ├─ ui/
 │   ├─ LifeMenuService.java
 │   ├─ EstateMenuService.java
 │   └─ BlueprintUnlockUiService.java
 ├─ command/
 │   ├─ LifeAdminCommand.java
 │   └─ EstateAdminCommand.java
 └─ log/
     ├─ LifeResourceLogger.java
     ├─ EstateProductionLogger.java
     └─ LifeCraftLogger.java
```

---

## 7. Codex 작업 단위 세분화

### 작업 단위 A. 생활 enum / DTO / 마스터 구조 생성
목표:
- 생활 시스템의 공용 타입 추가

Codex에게 요구:
- enum 생성
- resource/facility/blueprint용 기본 domain class 또는 entity 초안
- 이름은 문서 기준 유지

---

### 작업 단위 B. 생활 DB schema 생성
목표:
- 최소 생활 관련 테이블 생성

Codex에게 요구:
- SQLite schema.sql 또는 migration 작성
- primary key / foreign key / index 기본 설계
- 나중에 집계 확장 가능한 네이밍 유지

---

### 작업 단위 C. 필드 채집 뼈대 구현
목표:
- 채집/채광/벌목/낚시의 공통 구조 생성

최소 포함:
- 채집 노드 인터페이스 또는 추상 클래스
- 자원 지급 로직
- 희귀 부산물 확률 처리
- 생활 숙련도 경험치 지급

주의:
- 실제 맵 배치까지 완성하려 하지 말고,
- 공용 구조와 지급 흐름을 먼저 만들 것

---

### 작업 단위 D. 생활 제작 뼈대 구현
목표:
- 요리/연금/제련의 공통 제작식 구조 생성

최소 포함:
- recipe lookup
- 재료 차감
- 결과 지급
- 제작 로그 저장

주의:
- UI를 너무 깊게 만들기보다 서비스 계층 먼저 만들 것

---

### 작업 단위 E. 청사진 / 영지 생산 뼈대 구현
목표:
- 청사진 해금 → 시설 설치 → 시간 누적 생산 → 수확 구조 구현

최소 포함:
- 청사진 해금 서비스
- 시설 설치 서비스
- 생산 누적 계산 서비스
- 수확 서비스
- Lv1~Lv3 업그레이드 서비스

가장 중요한 점:
- 영지 생산은 실시간 누적 구조
- 저장량 가득 차면 생산 멈춤
- 기본 재료 위주
- 희귀 부산물은 낮은 확률

---

### 작업 단위 F. 생활 로그 / 통계 적재 포인트 구현
목표:
- 운영 통제 가능성 확보

최소 로그:
- 필드 채집 획득 로그
- 생활 제작 로그
- 영지 생산 로그
- 영지 수확 로그
- 생활 자원 거래 로그

---

## 8. 영지 생산 로직 구현 규칙

### 8-1. 실시간 누적 방식
Codex는 영지 생산을
“틱마다 생산”보다
**마지막 생산 시각 기준 차이 계산 방식**으로 구현하는 걸 우선 추천한다.

이유:
- 서버 부하 감소
- 오프라인 누적 처리 쉬움
- 수확 시 계산 가능

### 권장 방식
- `lastProducedAt`
- `baseCycleSeconds`
- `storageLimit`
- 현재 시각과 비교
- 누적 생산 가능량 계산
- 상한 이상은 잘라냄

---

### 8-2. 저장량 상한 필수
영지 생산은 무한 누적되면 안 된다.

최소 포함:
- 기본 24시간치 저장량
- 업그레이드 시 저장량 증가 가능
- 가득 차면 생산 정지

---

### 8-3. 희귀 부산물 처리
희귀 부산물은
- 필드 채집 우위
- 영지 생산은 Lv3부터 낮은 확률
구조를 유지해야 한다.

Codex가 영지 희귀 부산물 효율을 너무 높게 잡으면 안 된다.

---

## 9. 생활 제작 로직 구현 규칙

### 9-1. recipe 기반으로 구현
요리/연금/제련 모두 하드코딩 분기보다
**recipe 데이터 기반**으로 구현하는 게 좋다.

최소 요소:
- 입력 재료 목록
- 출력 결과
- 요구 숙련도
- 제작 카테고리

---

### 9-2. 가공 재료 중심 구조
생활 경제는 기본 재료보다
가공 재료와 소모품이 핵심이므로,
Codex는 제작 시스템을 아래처럼 우선 구현하는 게 좋다.

우선순위:
1. 광석 → 주괴
2. 약초 → 정제 약초
3. 목재 → 가공 목재
4. 생선/약초 → 음식
5. 약초/광물 → 물약

---

## 10. UI 구현 우선순위

Codex가 생활 UI까지 바로 손댄다면,
다음 순서를 권장한다.

### 1차
- 단순 명령 기반 또는 기본 GUI
- 청사진 해금 확인
- 시설 설치
- 수확
- 제작

### 2차
- 생활 메인 메뉴
- 영지 생산 메뉴
- 수확량/저장량 표시
- 업그레이드 표시

### 3차
- 보기 좋은 전용 UI
- 테마별 아이콘/분류
- 상세 설명 툴팁

즉 처음부터 화려한 UI보다 **서비스 로직과 데이터 구조가 먼저**다.

---

## 11. 운영/통계 연결 포인트

생활 시스템도 운영자가 봐야 한다.

Codex는 아래 로그가 나중에 집계 가능하도록 저장 구조를 맞춰야 한다.

### 최소 집계 가능 항목
- 재료별 획득량
- 재료별 소비량
- 필드 vs 영지 생산 비중
- 희귀 부산물 획득량
- 음식/물약 제작량
- 거래량
- 영지 시설별 생산량

---

## 12. Codex에게 추천하는 실제 프롬프트 예시

### 예시 1
“첨부한 생활 시스템 md 문서들을 기준으로,
Java 21 + Paper 플러그인 구조에서 생활 시스템 공용 enum, DB schema, 서비스 뼈대를 먼저 작성해줘.
먼저 `life_resources`, `estate_blueprints`, `user_estate_facilities`, `life_crafting_recipes` 중심으로 구현해줘.”

### 예시 2
“`poro_life_system_details.md`, `poro_estate_production_structure.md`, `poro_life_economy_trade_structure.md` 기준으로
청사진 해금 → 영지 시설 설치 → 실시간 누적 생산 → 수확까지 동작하는 최소 서비스 로직을 작성해줘.
UI는 단순 명령 기반으로 두고, 서비스 계층과 DB 구조를 우선해줘.”

### 예시 3
“생활 시스템에서 채집/채광/벌목/낚시 공통 구조를 먼저 구현해줘.
기본 재료 + 희귀 부산물 지급, 숙련도 경험치 획득, 로그 저장까지 포함해줘.
실제 자원 종류는 enum/registry로 분리해줘.”

---

## 13. Codex에게 주면 안 되는 요구

아래는 한 번에 시키지 않는 게 좋다.

- “생활 시스템 전부 완성해줘”
- “필드 배치부터 UI까지 한 번에 다 해줘”
- “생활 밸런스 수치까지 다 완성해줘”

이렇게 하면 결과가 엉킬 가능성이 높다.

대신 아래처럼 쪼개는 게 좋다.
1. enum / schema
2. 채집 구조
3. 제작 구조
4. 영지 생산 구조
5. 로그/통계 연결
6. UI/명령

---

## 14. 추천 실제 구현 순서

### 1차
- enum / DTO / DB schema
- 생활 자원 마스터
- 청사진/시설 마스터

### 2차
- 채집 공통 구조
- 숙련도 경험치
- 로그 저장

### 3차
- 제작식 기반 요리/연금/제련
- 재료 차감/결과 지급

### 4차
- 청사진 해금
- 시설 설치
- 누적 생산
- 수확

### 5차
- 시설 업그레이드
- 거래 연결
- 관리자 명령
- 운영 통계 적재

---

## 15. 한 줄 요약

**Codex에게 생활 시스템을 시킬 때는 “채집-제작-영지-거래” 전체를 한 번에 완성시키려 하지 말고, 공용 타입과 DB → 채집 → 제작 → 영지 생산 → 로그/거래 연결 순서로 단계적으로 구현시키는 게 가장 안정적이다.**
