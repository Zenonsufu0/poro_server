# 포로 서버 NPC 상호작용 구조 초안

## 문서 목적
이 문서는 NPC가 단순히 대사만 하는 객체가 아니라,
퀘스트/상점/이동/보상/기록 기능을 수행하는 상호작용 허브가 되도록 구조를 정리한 초안이다.

핵심 목표:
- 같은 NPC가 여러 기능을 가질 수 있게 한다.
- 지역 특화 표현은 다르게 하되 시스템 구조는 통일한다.
- Codex가 npc interaction handler를 만들 수 있게 한다.

---

## 1. 추천 상호작용 타입
- TALK
- QUEST_GIVE
- QUEST_TURN_IN
- SHOP_OPEN
- STORAGE_OPEN
- TRANSPORT
- EXCHANGE
- BOSS_UNLOCK
- RECORD_VIEW
- ACHIEVEMENT_VIEW

---

## 2. 예시

### 수도 특사 레오니드
- TALK
- QUEST_GIVE
- QUEST_TURN_IN
- REGION_BRANCH_SELECT

### 화맥 감시관 마르코스
- TALK
- QUEST_GIVE
- REGION_INFO
- BOSS_UNLOCK

### 계약 중개인 자파르
- TALK
- QUEST_GIVE
- EXCHANGE
- REGION_INFO

---

## 3. 한 줄 요약

**NPC는 기능별 상호작용 타입을 조합하는 구조로 설계하면, 지역마다 표현은 다르게 하면서도 시스템적으로는 일관된 허브로 운영할 수 있다.**
