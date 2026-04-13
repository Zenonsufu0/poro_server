# 포로 서버 BetonQuest 패키지 구조 초안

## 문서 목적
이 문서는 BetonQuest를 퀘스트 핵심 시스템이 아니라
**대사 / 연출 / 분위기 보조 레이어**로 사용할 때의 패키지 구조를 정리한 초안이다.

## 핵심 원칙
- 퀘스트 상태 저장은 EmpireRPG가 담당
- 보상 지급은 EmpireRPG가 담당
- 다음 퀘스트 해금 기준도 EmpireRPG가 담당
- BetonQuest는 대사, 연출, 선택지, 분위기 전달만 담당

## 추천 패키지 묶음
- `00_common`
  - 공통 변수
  - 공통 이벤트 래퍼
  - 공통 태그/조건 템플릿
- `01_capital`
  - 수도 인트로
  - 수도 허브 대사
  - 수도 메인 허브 연출
- `02_regions_east`
  - 동부 진입 대사
  - 숲/침식 분위기 연출
  - 결전 직전 대사
- `03_regions_west`
  - 서부 진입 대사
  - 폭풍/길 상실 연출
- `04_regions_south`
  - 남부 진입 대사
  - 화맥/과열 분위기 연출
- `05_regions_north`
  - 북부 진입 대사
  - 설혼/백빙 분위기 연출
- `06_sahar`
  - 계약/환영 대사
- `07_arkanon`
  - 공명/구획/붕괴 대사
- `08_boss_preludes`
  - 결전 직전 대사
  - 극상위 도전 전 대사
- `09_life_estate`
  - 생활 적응 대사
  - 영지 해금 대사
- `10_debug`
  - 테스트용 짧은 대화
  - 대사 연결 확인용 패키지

## 추천 파일 감각
각 패키지 안에:
- `conversations.yml`
- `events.yml`
- `conditions.yml`
- `objectives.yml` (필요 최소만)
- `package.yml`
정도로 두는 게 무난하다.

## 대사 연결 규칙
- Citizens NPC 클릭
- BetonQuest 대화 시작
- 대화 종료 시 EmpireRPG command / trigger 호출
- EmpireRPG가 quest start, reward, unlock 처리

## 절대 하지 말 것
- BetonQuest가 퀘스트 완료 상태를 진짜 기준으로 저장
- BetonQuest가 보상 지급을 단독 처리
- BetonQuest가 다음 퀘스트 해금을 최종 판정

## 한 줄 요약
**BetonQuest는 지역/보스/생활/영지 대사의 패키지를 파일 구조로 나누고, 대화 종료 후 EmpireRPG 트리거를 호출하는 연출 전용 레이어로 쓰는 것이 가장 안전하다.**
