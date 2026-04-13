# 포로 서버 Codex 프롬프트 - BetonQuest 연출 패키지 구조 생성

첨부한 `poro_server_docs_structured_final.zip`을 설계 기준 문서로 삼아라.

현재 서버는 EmpireRPG가 퀘스트 상태/보상/해금의 핵심을 담당한다.
이번 작업은 BetonQuest를 **대사 / 연출 / 분위기 보조 레이어**로 붙이는 범위만 구현하라.

## 목표
수도, 지역 진입, 생활 적응, 영지 해금, 보스 직전 연출용 BetonQuest 패키지 구조를 만든다.

## 이번 작업에서 꼭 구현할 것
1. BetonQuest packages 폴더 구조 제안 및 샘플 생성
   - 00_common
   - 01_capital
   - 02_regions_east
   - 03_regions_west
   - 04_regions_south
   - 05_regions_north
   - 06_sahar
   - 07_arkanon
   - 08_boss_preludes
   - 09_life_estate
   - 10_debug

2. 샘플 conversation 5종
   - 수도 인트로 대화 1개
   - 지역 진입 대화 1개
   - 생활 적응 대화 1개
   - 영지 해금 대화 1개
   - 보스 직전 대화 1개

3. EmpireRPG 연동 hook 구조
   - 대화 종료 후 명령/이벤트 trigger
   - quest start hook
   - estate unlock intro hook
   - boss entry prelude hook

4. 명확한 역할 분리
   - BetonQuest는 대사만
   - EmpireRPG는 상태/보상/해금

## 제약
- BetonQuest가 퀘스트 핵심 상태를 저장하지 말 것
- BetonQuest가 보상을 직접 지급하지 말 것
- 연출용 패키지와 디버그용 패키지를 분리할 것
- 실제 운영용 대사는 placeholder여도 되지만 연결 구조는 명확히 할 것

## 산출물
- packages 폴더 트리
- 샘플 yml 파일
- EmpireRPG hook 연결 설명
- Citizens 클릭 → BetonQuest 대화 → EmpireRPG trigger 흐름 설명

## 참조 문서
- poro_betonquest_package_structure_draft.md
- poro_story_quest_system_outline.md
- poro_spawn_to_capital_intro_flow.md
- poro_life_adaptation_side_quest_outline.md
- poro_estate_unlock_quest_line.md
- poro_region_main_quest_line_table.md
- poro_extreme_boss_unlock_line.md
