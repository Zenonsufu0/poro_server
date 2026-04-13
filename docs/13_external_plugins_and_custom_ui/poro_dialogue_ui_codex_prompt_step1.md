# 포로 서버 Codex 프롬프트 - 1단계 대화 UI 기반 구조

첨부한 `poro_server_docs_structured_latest_v4.zip`을 설계 기준 문서로 삼아라.

현재 방향:
- BetonQuest 제거
- Citizens는 NPC 실체/배치만 담당
- EmpireRPG가 대화 UI를 직접 담당

이번 작업 목표:
커스텀 대화 UI의 **기반 구조와 seed/registry/session 구조**만 먼저 구현하라.

## 구현할 것
1. DialogueRegistry
2. DialogueSeedLoader
3. DialogueNode / DialogueChoice record
4. DialogueSessionService
5. DialogueTriggerResolver
6. DialogueUiStateStore
7. 샘플 dialogue seed 3종
   - 수도 레오니드
   - 영지 관리관
   - 보스 입장 안내관

## 제약
- 실제 강화/잠재/보스 입장 UI까지 확장하지 말 것
- UI 프레임워크 전체보다 대화 세션 구조 우선
- Citizens 클릭 연동은 hook 수준만 두고, 완전 연결은 다음 단계에서 처리

## 산출물
- 패키지 구조
- 핵심 클래스 코드
- 샘플 seed 파일
- 수도/영지/보스 NPC용 dialogue_id 예시
- 다음 단계(UI 렌더)로 어떻게 이어질지 설명
