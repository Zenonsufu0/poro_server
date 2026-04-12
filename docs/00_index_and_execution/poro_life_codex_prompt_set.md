# 포로 서버 생활 시스템 Codex 프롬프트 세트 초안

## 문서 목적
이 문서는 생활 시스템을 Codex에 단계적으로 구현시키기 위한 **실전 프롬프트 모음**이다.

원칙:
- 한 번에 전부 시키지 않는다.
- 작은 단위로 끊어서 구현시킨다.
- 먼저 구조와 뼈대, 그다음 DB, 그다음 서비스, 그다음 UI 순서로 간다.

---

## 0. 사용 전 주의

### 현재 문서에 이미 정해진 것
- 생활은 메인 성장 루트가 아니라 보조 성장 루트
- 필드 채집 = 즉시 수급 / 희귀 부산물 / 적극 플레이 보상
- 영지 생산 = 청사진 해금 후 기본 재료 안정 수급
- 생활은 거래/가공/소모품/보조 제작 중심
- 최종 장비 핵심 재료는 생활 전용으로 두지 않음

### 아직 완전히 안 정해진 것
- 재료별 정확한 드랍량
- 시설별 최종 생산량 수치
- 제작식 정확한 재료 개수
- 생활 숙련도 경험치 테이블
- 희귀 부산물 정확한 확률
- 거래 기준가/가격 범위

즉, Codex에게는 **수치 확정본이 아니라 구조 우선 구현**을 시키는 게 맞다.

---

## 1. 프롬프트 세트 A — 생활 enum / schema 뼈대

### 목적
생활 시스템 공용 enum, DB schema, 기본 엔티티 구조 생성

### 프롬프트
첨부한 생활 시스템 md 문서들을 기준으로 Java 21 + Gradle Kotlin DSL 프로젝트에서 생활 시스템 공용 타입과 DB schema 뼈대를 작성해줘.

우선 구현 범위:
- `LifeSkillType`
- `LifeResourceTier`
- `LifeSourceType`
- `EstateFacilityType`
- `CraftCategory`
- `ResourceUsageCategory`

그리고 SQLite 기준으로 아래 테이블 schema 초안을 작성해줘:
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
- `life_crafting_recipe_inputs`
- `life_crafting_logs`

조건:
- 구조와 컬럼 설계를 우선하고, 밸런스 수치는 placeholder 가능
- raw log / current state / master table 역할을 분리해줘
- 나중에 Spring과 Paper 양쪽에서 재사용 가능하게 네이밍 통일해줘

산출물:
- enum 코드
- schema.sql 또는 migration
- 최소 entity 또는 domain class 초안

---

## 2. 프롬프트 세트 B — 생활 자원 마스터 / seed 데이터

### 목적
생활 자원, 청사진, 시설, 제작식용 seed 데이터 뼈대 생성

### 프롬프트
첨부한 생활 문서 기준으로 생활 자원 마스터와 영지 청사진/시설/제작식 seed 데이터 구조를 작성해줘.

필수 포함:
- 테마별 기본 자원 예시
- basic / rare_byproduct / processed / consumable 구분
- field 전용 여부
- estate 생산 가능 여부
- usage_category

우선 예시 데이터는 아래 테마 자원 중심으로 넣어줘:
- 동부: 정령초, 세계수 수피, 안개 버섯
- 북부: 설혼초, 냉기 광석, 빙결 결정
- 남부: 홍염초, 화맥석, 용암 결정
- 사하르: 신기루 수정, 금빛 모래 광물
- 아르카논: 공명 광석, 폭주 결정편

조건:
- 수치는 정확 확정이 아니라 seed 예시 수준으로 둬도 됨
- 이름, category, source_type, usage_category가 흔들리지 않게 설계해줘
- Java enum/registry 또는 JSON/YAML seed 중 구현하기 좋은 구조를 선택해줘

산출물:
- seed 데이터 파일
- 자원/청사진/시설/레시피 registry 구조
- 초기 마스터 로딩 코드 초안

---

## 3. 프롬프트 세트 C — 필드 채집 공통 구조

### 목적
채집/채광/벌목/낚시 공통 서비스 구조 생성

### 프롬프트
생활 시스템 문서를 기준으로 Paper 플러그인에서 필드 채집 공통 구조를 먼저 구현해줘.

필수 기능:
- 채집 노드 추상 구조 또는 인터페이스
- 플레이어 상호작용 처리
- 기본 재료 지급
- 희귀 부산물 확률 지급
- 생활 숙련도 경험치 지급
- `life_resource_transactions` 로그 저장

대상 생활:
- 채집
- 채광
- 벌목
- 낚시

조건:
- 실제 월드 배치 로직보다 공용 서비스 구조를 우선
- 희귀 부산물 확률은 placeholder 상수로 두되, 나중에 config/DB로 옮길 수 있게 설계
- field_gather source_type이 로그에 남아야 함
- 클래스 책임을 나눠서 구현해줘

추천 클래스 예시:
- `GatherNodeService`
- `GatherRewardResolver`
- `GatherExpService`
- `LifeResourceGrantService`
- `LifeResourceLogger`

산출물:
- 인터페이스/추상 클래스
- 기본 구현 클래스
- 관리자 테스트용 간단한 명령 또는 테스트 훅

---

## 4. 프롬프트 세트 D — 생활 제작 공통 구조

### 목적
요리/연금/제련/가공 recipe 기반 제작 구조 생성

### 프롬프트
첨부한 생활 문서 기준으로 recipe 기반 생활 제작 시스템 공용 구조를 구현해줘.

우선 구현 카테고리:
- 요리
- 연금
- 제련/가공

필수 기능:
- recipe 조회
- 요구 숙련도 검증
- 재료 보유량 검증
- 재료 차감
- 결과 지급
- `life_crafting_logs` 저장
- `life_resource_transactions`에 craft_use / craft_result 기록

우선순위 예시 recipe:
1. 광석 → 주괴
2. 약초 → 정제 약초
3. 목재 → 가공 목재
4. 생선 + 약초 → 음식
5. 약초 + 광물 → 물약

조건:
- 하드코딩 switch보다 recipe 데이터 기반 구조로 만들어줘
- UI는 최소화하고 서비스 계층을 먼저 구현해줘
- 결과물은 resource 또는 item 둘 다 대응 가능한 구조면 좋음

추천 클래스 예시:
- `LifeCraftService`
- `RecipeResolver`
- `CraftValidationService`
- `CraftExecutionService`
- `LifeCraftLogger`

산출물:
- recipe domain/entity
- service 구현
- 최소 테스트 코드

---

## 5. 프롬프트 세트 E — 청사진 해금 / 영지 생산 구조

### 목적
청사진 → 시설 설치 → 누적 생산 → 수확 구조 구현

### 프롬프트
생활 시스템 문서를 기준으로 영지 생산 시스템의 최소 기능을 구현해줘.

필수 흐름:
1. 청사진 해금
2. 시설 설치
3. 실시간 누적 생산
4. 저장량 상한 체크
5. 수확
6. Lv1~Lv3 업그레이드

필수 규칙:
- 영지 생산은 기본 재료 중심
- 희귀 부산물은 Lv3부터 낮은 확률
- 저장량이 가득 차면 생산 멈춤
- 생산은 틱 누적보다 `lastProducedAt` 차이 계산 방식 우선
- 생산 로그와 수확 로그를 분리 저장

기본 시설:
- 약초 재배지
- 광물 추출장
- 벌목장
- 양식장

테마 시설 예시 대응 가능 구조:
- 설혼 온실
- 화맥 배양기
- 공명 추출장
- 신기루 정제대
- 세계수 묘포

추천 클래스 예시:
- `EstateBlueprintService`
- `EstateFacilityService`
- `EstateProductionService`
- `EstateHarvestService`
- `EstateUpgradeService`
- `EstateProductionCalculator`

산출물:
- 서비스 계층
- repository/entity 초안
- 생산량 계산 로직
- 수확 처리 로직
- 간단한 관리자 테스트 명령

---

## 6. 프롬프트 세트 F — 생활 UI 최소 버전

### 목적
생활 메인창 / 제작창 / 청사진창 / 영지 생산창의 최소 GUI 또는 명령 UI 구현

### 프롬프트
생활 시스템 문서를 기준으로 생활 UI 최소 버전을 구현해줘.

1차 목표:
- 화려한 UI보다 기능이 되는 최소 GUI
- 생활 메인창을 허브로 두고 하위 메뉴 이동 가능
- 제작 / 청사진 해금 / 영지 생산 / 수확 / 업그레이드 가능

필수 UI:
- 생활 메인창
- 제작창
- 청사진 해금창
- 영지 생산창

꼭 보여야 하는 정보:
- 생활별 숙련도 레벨
- 제작 가능 여부
- 미사용 청사진 여부
- 수확 가능한 시설 수
- 시설 저장량
- 생산 정지 상태
- 업그레이드 가능 여부

조건:
- GUI 라이브러리가 있다면 사용 가능
- 없다면 Bukkit/Paper inventory GUI 기반으로 최소 구현
- UI 로직과 서비스 로직은 분리해줘

추천 클래스 예시:
- `LifeMenuService`
- `LifeCraftMenu`
- `BlueprintUnlockMenu`
- `EstateProductionMenu`

산출물:
- 메뉴 클래스
- 클릭 핸들러
- 서비스 연동 구조

---

## 7. 프롬프트 세트 G — 생활 운영 로그 / 관리자 명령

### 목적
운영 통제와 QA를 위한 로그/명령 구조 구현

### 프롬프트
생활 시스템 문서를 기준으로 운영자가 생활 시스템을 통제하고 테스트할 수 있는 최소 관리자 명령과 로그 구조를 추가해줘.

필수 관리자 기능:
- 특정 생활 자원 지급
- 특정 청사진 해금
- 특정 시설 설치
- 특정 시설 레벨 변경
- 생산 강제 갱신
- 수확 강제 실행
- 생활 숙련도 조정
- 생활 로그 요약 조회

추천 명령 예시:
- `/life admin give <resource> <amount>`
- `/life admin unlock-blueprint <blueprintId>`
- `/life admin install-facility <facilityId>`
- `/life admin set-facility-level <userFacilityId> <level>`
- `/life admin refresh-estate`
- `/life admin skill <type> <level>`

필수 로그:
- 필드 채집 획득
- 제작
- 영지 생산
- 영지 수확
- 거래 이동

산출물:
- 관리자 명령 파서
- 서비스 호출
- 로그 저장 지점

---

## 8. 수치에 대한 현재 상태

### 이미 들어간 권장 수치/범위
현재 문서에 들어가 있는 건 “권장 범위” 수준이다.

예:
- 영지 기본 생산 주기: **4시간 추천**
- 영지 저장량: **24시간치 추천**
- 영지 업그레이드: **Lv1~Lv3 우선**
- 영지 희귀 부산물: **Lv3부터 낮은 확률**
- 생활 숙련도 최대: **20~30 추천**
- 테마당 1차 자원 수: **8~10종 내외 추천**

### 아직 확정 안 된 것
아래는 아직 “정확 수치 확정본”이 아니다.
- 자원별 정확 드랍량
- 희귀 부산물 확률 %
- recipe 재료 개수
- 영지 시설별 생산량
- 업그레이드 비용
- 숙련도 레벨업 경험치표
- 거래 기준가

즉 지금 단계에서 Codex에는
**수치 완성본이 아니라 구조 우선 구현**을 시키는 게 맞다.

---

## 9. 추천 사용 순서

Codex에는 아래 순서로 던지는 걸 추천한다.

1. 프롬프트 세트 A
2. 프롬프트 세트 B
3. 프롬프트 세트 C
4. 프롬프트 세트 D
5. 프롬프트 세트 E
6. 프롬프트 세트 G
7. 프롬프트 세트 F

UI는 제일 나중이 더 안정적이다.

---

## 10. 한 줄 요약

**현재 생활 문서에는 구조를 짤 수 있을 정도의 권장 범위와 역할 정의는 들어가 있지만, 최종 밸런스용 세부 수치표까지 완전히 확정된 상태는 아니므로 Codex에는 구조 우선 구현을 단계적으로 시키는 게 맞다.**
