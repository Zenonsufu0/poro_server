# 포로 서버 Codex 단계별 작업 프롬프트 세트 확장판 v2

## 문서 목적
이 문서는 지금까지 만든 포로 서버 문서를 기반으로,
Codex에 실제 구현을 시킬 때 바로 복붙할 수 있는 **확장 프롬프트 세트**를 정리한 문서다.

핵심 목표:
- 보스, 생활, 운영, 소비 아이템, 결과창, 데스카운트까지 포함한 실전 프롬프트를 만든다.
- 작업 단위를 더 잘게 쪼개 Codex 실패 확률을 낮춘다.

---

## 1. 보스 엔진 공용 구조 프롬프트

첨부한 보스 관련 md 문서를 기준으로 Paper 플러그인에서 보스 엔진 공용 구조를 구현해줘.

필수 포함:
- BossBattleEngine
- BossPhaseController
- BossPatternScheduler
- BossDeathCountService
- BossReviveService
- BossBattleFailService
- BossDeathUiService
- BossResultScreenService

조건:
- 데스카운트는 1인 3 / 2인 4 / 3인 5
- 데카 남아 있으면 부활 가능
- 데카 0 상태에서 전원 사망 시 실패
- 결과창 DTO는 좌측 요약 + 우측 기여도 구조
- 서비스 계층과 UI 계층을 분리해줘

---

## 2. 라그네스 / 아그네르 상세 구현 프롬프트

첨부한 라그네스/아그네르 상세 타임라인 문서를 기준으로
결전 보스 라그네스와 극상위 보스 아그네르의 상세 패턴 운용 구조를 구현해줘.

필수 포함:
- phase transition
- stagger success/fail branch
- berserk entry
- pattern cooldown and priority handling
- max consecutive pattern rule

조건:
- 숫자는 config/seed로 분리 가능하게
- pattern data를 JSON/YAML/registry 중 하나로 관리
- 하드코딩 switch문 남발보다 data-driven 구조 선호

---

## 3. 보스 결과창 / 기여도 표 프롬프트

첨부한 결과창/기여도 문서를 기준으로
보스전 종료 시 성공/실패 결과창과 파티 기여도 표를 구현해줘.

필수 포함:
- clearSuccess / failureReason
- clearTime / phaseReached / berserkReached
- participant totalDamage / damageShare / deathCount / staggerContribution
- left summary / right contribution structure
- MVP badge placeholder

조건:
- Minecraft UI 한계에 맞춰 inventory GUI + chat summary 혼합형 가능
- 데이터는 run summary 테이블에서 가져오게 구조화
- 로깅 구조도 같이 제안해줘

---

## 4. 생활 시스템 공용 구조 프롬프트

첨부한 생활 문서를 기준으로 생활 시스템 공용 구조를 구현해줘.

필수 포함:
- life resource master
- user life skill progression
- field gather
- recipe crafting
- estate blueprint unlock
- estate facility install
- estate production / harvest
- life resource transactions

조건:
- 구조 구현 우선
- 수치는 seed/config로 분리
- field와 estate source를 명확히 구분

---

## 5. 음식 / 만찬 / 물약 프롬프트

첨부한 음식/만찬/물약 문서를 기준으로
consumable item master와 effect master, buff slot 구조를 구현해줘.

필수 포함:
- FOOD / FEAST / POTION_HEAL / POTION_RESIST / POTION_COMBAT
- MEAL_SLOT / RESIST_POTION_SLOT / COMBAT_POTION_SLOT
- user_active_consumable_effects
- consumable_use_logs
- feast party apply flow
- heal potion cooldown flow

조건:
- 음식과 만찬은 MEAL_SLOT 공유
- 음식/만찬은 통합 1슬롯
- 물약은 별도 계열 슬롯
- 회복 물약은 즉시형 + 쿨다운 중심

---

## 6. 운영 대시보드 / 경고 프롬프트

첨부한 운영 대시보드와 경고 임계값 문서를 기준으로
admin-api의 운영 대시보드 summary endpoint와 alert evaluator를 구현해줘.

필수 포함:
- economy summary
- boss summary
- combat balance summary
- life summary
- alerts evaluator

조건:
- 최근 7일 평균 기반 WARNING / CRITICAL 판정
- DTO는 기존 AdminApiResponse 구조 사용
- placeholder 데이터 가능하지만 필드 구조는 문서 기준 유지

---

## 7. 한 줄 요약

**이 문서는 지금까지 만든 포로 서버 md 문서를 기반으로 Codex에 바로 복붙해서 쓸 수 있는 확장 작업 프롬프트 세트이며, 보스/생활/소비 아이템/운영 대시보드까지 한 단계 더 실전적으로 쪼개놓은 문서다.**
