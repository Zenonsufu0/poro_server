# 포로 서버 Codex 프롬프트 - 2단계 대화 UI 렌더와 Citizens 연결

첨부한 `poro_server_docs_structured_latest_v4.zip`을 설계 기준 문서로 삼아라.

현재 repo는 DialogueRegistry, DialogueSessionService 같은 기반 구조가 이미 들어간 상태라고 가정한다.

이번 작업 목표:
Citizens NPC 클릭 시 EmpireRPG 커스텀 대화 UI가 열리고, 선택지 진행이 가능한 **실제 렌더/UI 연결 구조**를 구현하라.

## 구현할 것
1. DialogueUiRenderer
2. DialogueUiService
3. CitizensNpcClickDialogueAdapter
4. DialogueNextActionHandler
5. DialogueCloseActionHandler
6. 수도 레오니드 클릭 시 대화 시작 연결
7. 영지 관리관 클릭 시 대화 시작 연결

## UI 요구사항
- NPC 이름 표시
- 대사 본문 표시
- 선택지 1~3개
- 다음 / 닫기 버튼
- 향후 초상화/아이콘 붙이기 쉬운 구조

## 제약
- 퀘스트 상태 저장은 여전히 EmpireRPG service에서 처리
- BetonQuest 재도입 금지
- 하드코딩 대사보다 seed 참조 우선

## 산출물
- 패키지 구조
- 렌더/UI 핵심 클래스 코드
- 레오니드/영지 관리관 클릭 시 대화가 열리는 흐름 설명
- 디버그 로그 또는 테스트 방법 설명
