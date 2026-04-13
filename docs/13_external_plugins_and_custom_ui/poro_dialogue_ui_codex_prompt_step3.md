# 포로 서버 Codex 프롬프트 - 3단계 대화 UI 트리거 / 퀘스트 / 보스 입장 연결

첨부한 `poro_server_docs_structured_latest_v4.zip`을 설계 기준 문서로 삼아라.

현재 repo는 대화 UI가 열리고 선택지 진행까지 되는 상태라고 가정한다.

이번 작업 목표:
대화 종료 또는 특정 선택지에서 EmpireRPG trigger가 실행되도록 연결하라.
이 단계에서 퀘스트 시작, 영지 해금 인트로, 보스 입장 UI 오픈 트리거까지 연결한다.

## 구현할 것
1. DialogueFinishTriggerService
2. DialogueChoiceTriggerService
3. QuestStartDialogueHook
4. EstateUnlockDialogueHook
5. BossEntryDialogueHook
6. 레오니드 대화 종료 -> 첫 메인 퀘스트 시작
7. 영지 관리관 대화 종료 -> 영지 해금 인트로 처리
8. 보스 입장 안내관 선택 -> 테마 허브형 보스 입장 UI open

## 제약
- trigger는 EmpireRPG service만 호출
- Citizens/BetonQuest에 다시 의존하지 말 것
- 기존 BossEntryValidator, QuestStateService와 자연스럽게 연결할 것

## 산출물
- 수정 파일 목록
- trigger 연결 흐름 설명
- 레오니드 / 영지 / 보스 안내관 각각의 finish trigger 예시
- 다음 단계로 강화 UI, 잠재 UI에 재사용 가능한 공통 UI 구조 설명
