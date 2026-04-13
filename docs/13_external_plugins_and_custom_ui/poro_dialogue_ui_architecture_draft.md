# 포로 서버 커스텀 대화 UI 구조 초안

## 목표
Citizens NPC 클릭 시 채팅형 대화가 아니라 EmpireRPG 커스텀 UI가 열리도록 전환한다.

## 핵심 원칙
- Citizens는 NPC 실체와 클릭 진입점만 담당
- EmpireRPG가 대화 UI, 선택지, 트리거, 퀘스트 시작을 담당
- 대화는 seed 기반으로 관리
- 대화 종료 후 quest / estate / boss entry 같은 trigger를 EmpireRPG가 직접 실행

## 추천 구성
- `ui.dialogue`
- `ui.framework`
- `dialogue.seed`
- `dialogue.session`
- `dialogue.trigger`

## 추천 핵심 클래스
- DialogueUiService
- DialogueSessionService
- DialogueRegistry
- DialogueNode
- DialogueChoice
- DialogueTriggerResolver
- DialogueUiStateStore

## 추천 seed 필드
- dialogue_id
- npc_seed_id
- node_id
- speaker_name
- portrait_id
- text
- next_node_id
- choice_text
- choice_next_node_id
- finish_trigger_type
- finish_trigger_value

## 1차 구현 우선순위
- 수도 레오니드 대화
- 영지 관리관 대화
- 보스 입장 NPC 대화

## 한 줄 요약
**대화는 Citizens 클릭 → EmpireRPG Dialogue UI open → 선택지 진행 → finish trigger 실행 구조로 통일하는 것이 가장 안정적이다.**
