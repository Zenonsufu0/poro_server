# 포로 서버 Codex 투입용 모듈별 프롬프트 묶음 최종판 초안

## 문서 목적
이 문서는 지금까지 만든 문서를 기반으로,
Codex에 실제 구현을 맡길 때 바로 복붙해서 쓸 수 있는 모듈별 프롬프트 묶음을 정리한 최종판 초안이다.

핵심 목표:
- 구현 단위를 충분히 잘게 쪼갠다.
- 문서 참조 범위를 명확히 한다.
- 한 번에 너무 큰 범위를 던지지 않고, 성공률 높은 작업 단위로 나눈다.

---

## 1. 공용 기반 프롬프트

첨부한 공용 인덱스, DB 개요, seed handoff 문서를 기준으로
Java 21 + Gradle + Paper 1.21.10 프로젝트의 공용 기반 구조를 구현해줘.

필수 포함:
- common package
- enum/code registry
- config loader
- seed loader abstraction
- result/response wrapper
- base entity / audit fields
- id generation policy

조건:
- 도메인별 패키지 분리
- 서비스/레지스트리/저장소 계층 분리
- 하드코딩 enum 남발보다 registry 참조 선호

---

## 2. 전투 엔진 프롬프트

첨부한 전투 계산식, 상태 코드, 스킬 seed 확장 문서를 기준으로
전투 엔진 핵심 구조를 구현해줘.

필수 포함:
- damage formula resolver
- state registry
- buff/debuff applier
- resource handler
- tag damage resolver
- conditional damage resolver
- critical / defense hooks

조건:
- 일반 피해 → 태그 피해 → 조건부 피해 → 방어 → 치명 → 피해감소 순서 유지
- 상태 부여와 소비 순서를 문서 기준으로 맞춰줘
- 확장 가능한 구조로 작성

---

## 3. 보스 엔진 프롬프트

첨부한 보스 패턴 스크립트, 패턴 seed, 입장/기록 구조 문서를 기준으로
보스 엔진을 구현해줘.

필수 포함:
- boss run creation
- phase controller
- pattern scheduler
- stagger success/fail branch
- berserk entry
- deathcount logic
- result summary builder
- reward resolver hook

조건:
- daily/weekly 제한은 기본 null
- run_id 기반 기록 저장
- 결전/극상위 모두 공통 구조에서 확장 가능해야 함

---

## 4. 성장 시스템 프롬프트

첨부한 장비/강화/잠재/세트/룬/각인 문서를 기준으로
성장 시스템을 구현해줘.

필수 포함:
- item master loader
- equip/unequip service
- enhancement service
- potential reroll/upgrade service
- set bonus resolver
- rune slot service
- engraving slot service

조건:
- 강화 실패 시 파괴/하락 없음
- 잠재 3줄과 이탈 잠재 규칙 반영
- T1/T2 구조 분리

---

## 5. 생활/영지 프롬프트

첨부한 생활/영지/제작식/시설 업그레이드 문서를 기준으로
생활 시스템을 구현해줘.

필수 포함:
- resource gather
- craft recipe resolver
- life skill exp progression
- estate unlock
- facility install/upgrade
- harvest logic
- estate storage snapshot

조건:
- 필드 채집과 영지 생산을 구분
- 영지는 기본 재료 안정 수급, 필드는 희귀 부산물 우위
- 시설 Lv3에서 희귀 부산물 해금 가능 구조 반영

---

## 6. 퀘스트/업적 프롬프트

첨부한 퀘스트 seed, objective type, reward seed, 업적 seed 문서를 기준으로
퀘스트/업적 시스템을 구현해줘.

필수 포함:
- quest state service
- objective evaluator
- reward resolver
- quest chain next unlock
- achievement tracker
- title/badge unlock service

조건:
- objective type은 표준 master 기준 사용
- 메인/서브/일일/생활/영지 퀘스트 모두 같은 구조 안에서 처리
- 업적 보상은 명예형 중심

---

## 7. 운영/조회 프롬프트

첨부한 대시보드 KPI, 웹 페이지, 디코 명령 문서를 기준으로
운영 API와 디코 봇 조회 레이어를 구현해줘.

필수 포함:
- admin dashboard summary endpoints
- player detail query endpoints
- boss statistics endpoints
- market statistics endpoints
- discord snapshot commands
- hall of fame query

조건:
- 웹은 운영자 상세 조회 중심
- 디코는 유저 빠른 조회 중심
- DTO는 응답용 전용 projection 선호

---

## 8. 한 줄 요약

**Codex에는 공용 기반, 전투 엔진, 보스 엔진, 성장 시스템, 생활/영지, 퀘스트/업적, 운영/조회 일곱 묶음으로 나눠 순서대로 투입하는 것이 가장 안정적이다.**
