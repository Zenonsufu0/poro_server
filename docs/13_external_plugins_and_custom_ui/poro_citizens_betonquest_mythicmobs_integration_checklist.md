# 포로 서버 Citizens / BetonQuest / MythicMobs 연동 체크리스트

## 핵심 목표
- 역할 충돌 방지
- 실제 설정 순서 정리
- EmpireRPG 중심 구조 유지

## Citizens 체크리스트
- [ ] NPC 역할표 확정
- [ ] 수도 핵심 NPC 배치 seed 작성
- [ ] 지역 안내 NPC 배치 seed 작성
- [ ] 영지/생활 NPC 배치 seed 작성
- [ ] 보스 입장 NPC 배치 seed 작성
- [ ] 인게임 수동 생성보다 seed 자동 생성 우선
- [ ] 클릭 이벤트 중복 방지
- [ ] 시스템형 NPC는 EmpireRPG interaction profile만 연결
- [ ] 연출형 NPC만 BetonQuest conversation 연결

## BetonQuest 체크리스트
- [ ] 대사 패키지 구조 생성
- [ ] 수도 인트로 대사 작성
- [ ] 지역 진입 대사 작성
- [ ] 보스 직전 대사 작성
- [ ] 퀘스트 상태 저장 금지
- [ ] 보상 지급 금지
- [ ] 대화 종료 후 EmpireRPG trigger 호출 구조 확인

## MythicMobs 체크리스트
- [ ] 일반 필드 몹 정의
- [ ] 정예몹 정의
- [ ] 결전 보스 외형 정의
- [ ] 극상위 보스 외형 정의
- [ ] 장판/투사체/소환 연출 스킬 정의
- [ ] clear/fail 판정은 EmpireRPG만 하도록 역할 분리
- [ ] 보스 death event 처리 중복 여부 점검
- [ ] 보스 ID 매핑표 작성

## EssentialsX 체크리스트
- [ ] 테스트 단계 사용 범위 확인
- [ ] /home 제한 여부 결정
- [ ] /back 제한 여부 결정
- [ ] /tpa 제한 여부 결정
- [ ] /warp 제한 여부 결정

## WorldGuard / Multiverse 체크리스트
- [ ] 운영 월드
- [ ] 개발 월드
- [ ] 보스 테스트 월드
- [ ] 수도 보호 구역
- [ ] 보스방 보호 구역
- [ ] 던전/영지 구역 보호

## 한 줄 요약
**Citizens는 NPC 껍데기, BetonQuest는 대사 연출, MythicMobs는 몹/보스 외형과 연출, EmpireRPG는 핵심 판정이라는 역할 분리를 유지하는 것이 가장 중요하다.**
