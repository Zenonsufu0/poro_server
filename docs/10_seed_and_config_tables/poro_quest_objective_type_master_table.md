# 포로 서버 퀘스트 objective type 마스터 표 초안

## 문서 목적
이 문서는 퀘스트 단계에서 사용할 objective type을 표준화하기 위한 초안이다.

핵심 목표:
- 메인/서브/생활/영지/일일 퀘스트가 같은 목표 타입 체계를 공유하게 한다.
- Codex가 objective evaluator를 구현하기 쉽게 만든다.
- 퀘스트 작성 시 타입이 들쭉날쭉해지는 것을 방지한다.

---

## 1. 기본 철학

objective는 가능한 한 적은 타입으로 넓게 커버하는 게 좋다.

추천 방향:
- 대화
- 처치
- 수집
- 채집
- 제작
- 상호작용
- 지역 진입
- 보스 클리어
- 기록 확인
정도를 표준으로 둔다.

---

## 2. objective type 목록

| objective_type | 설명 | 예시 |
|---|---|---|
| TALK | NPC와 대화 | 레오니드와 대화 |
| KILL | 특정 몬스터 처치 | 들개 10마리 처치 |
| COLLECT | 특정 드랍 재료 수집 | 들개 이빨 5개 수집 |
| GATHER | 생활 자원 채집 | 설혼초 3개 채집 |
| CRAFT | 제작 성공 | 회복 수프 1개 제작 |
| DELIVER | 특정 아이템 전달 | 기록 문서 전달 |
| INTERACT | 오브젝트 상호작용 | 제단 조사 |
| ENTER_REGION | 특정 지역 진입 | 심층 숲 진입 |
| ENTER_DUNGEON | 던전 진입 | 폭풍 협곡 심부 진입 |
| CLEAR_BOSS | 보스 클리어 | 라그네스 처치 |
| VIEW_UI | UI/메뉴 확인 | 영지 메뉴 확인 |
| INSTALL_FACILITY | 시설 설치 | 약초 재배지 설치 |
| HARVEST | 영지 수확 | 첫 수확 완료 |
| EQUIP_ITEM | 아이템 장착 | 초반 무기 장착 |
| ENHANCE_ITEM | 강화 수행 | 장비 1회 강화 |

---

## 3. 추천 추가 필드

- `objective_type`
- `target_id`
- `target_amount`
- `region_scope`
- `allow_party_share`
- `notes`

### allow_party_share 감각
- 메인 퀘스트 보스 처치: true 가능
- 생활 채집/제작: false 권장
- 일반 처치/수집: 일부 true 가능

---

## 4. 한 줄 요약

**퀘스트 objective는 TALK, KILL, COLLECT, GATHER, CRAFT, INTERACT, ENTER, CLEAR_BOSS 같은 표준 타입으로 묶어두는 것이 가장 구현하기 쉽고 확장성도 좋다.**
