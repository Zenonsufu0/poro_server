# 포로 서버 Citizens 자동생성 + BetonQuest 연출 동시 진행 순서표

## 목표
Citizens NPC 자동 생성과 BetonQuest 대사 패키지를 동시에 진행하되,
서로 꼬이지 않게 병렬로 붙이는 순서를 정한다.

## 병렬 진행 원칙
- Citizens는 NPC 실체와 위치를 먼저 잡는다
- BetonQuest는 대사 패키지를 먼저 잡는다
- EmpireRPG가 둘을 연결한다
- 퀘스트 핵심 상태는 끝까지 EmpireRPG만 담당한다

## 추천 순서

### 1단계: seed / package 골격 동시 생성
- Citizens NPC seed 구조 생성
- BetonQuest packages 구조 생성

### 2단계: 수도 구간부터 연결
- 레오니드 NPC 자동 생성
- 수도 인트로 대화 연결
- 대화 종료 후 EmpireRPG의 퀘스트 시작 trigger 연결

### 3단계: 생활/영지 구간 연결
- 생활 적응 NPC 자동 생성
- 영지 관리관 NPC 자동 생성
- 생활 적응 대사 / 영지 해금 대사 연결
- 대화 종료 후 EmpireRPG hook 연결

### 4단계: 지역 진입 구간 연결
- 동/서/남/북 대표 안내 NPC 자동 생성
- 지역 진입 대사 연결
- 필요 시 지역 안내 profile 연결

### 5단계: 보스 입장 구간 연결
- 결전 입장 NPC 자동 생성
- 보스 직전 대사 연결
- 대화 후 EmpireRPG boss entry validate 호출

## 테스트 우선순위
1. 수도 NPC 2개
2. 영지/생활 NPC 2개
3. 지역 진입 NPC 1개
4. 보스 입장 NPC 1개

## 성공 기준
- 서버 시작 시 NPC 자동 생성
- NPC 클릭 시 대화 정상 시작
- 대화 종료 후 EmpireRPG trigger 정상 호출
- 퀘스트 상태/보상/해금은 BetonQuest가 아닌 EmpireRPG에서 처리

## 한 줄 요약
**Citizens 자동생성과 BetonQuest 연출은 수도 → 생활/영지 → 지역 진입 → 보스 입장 순서로 병렬 연결하면 가장 안전하고 빠르다.**
