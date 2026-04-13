# 포로 서버 외부 플러그인 역할 매트릭스 최종판

| 플러그인 | 유지 여부 | 핵심 역할 | 하지 말아야 할 역할 |
|---|---|---|---|
| EmpireRPG | 유지 | 서버 핵심 시스템 전부 | 없음 |
| Citizens | 유지 | NPC 배치/외형/클릭 진입점 | 퀘스트 상태/보상 핵심 처리 |
| BetonQuest | 유지 | 대사/연출/대화 분기 보조 | 퀘스트 상태/보상/해금 핵심 처리 |
| MythicMobs | 유지 | 몹/보스 외형/스폰/연출 | 결전/극상위 clear/fail 핵심 판정 |
| Vault | 유지 | 경제 연결 | 내부 핵심 성장 로직 대체 |
| LuckPerms | 유지 | 권한 | 게임 데이터 로직 |
| WorldGuard | 유지 | 구역 보호 | 핵심 전투/퀘스트 데이터 |
| WorldEdit | 유지 | 빌드/편집 | 게임 로직 |
| Multiverse-Core | 유지 | 월드 분리 | 게임 데이터 판정 |
| EssentialsX | 조건부 유지 | 테스트/운영 유틸 | 탐험 동선 무력화 수준의 자유 이동 |
| AuraSkills | 제거 | 없음 | 성장/생활/전투 progression 중복 |

## 한 줄 요약
**EmpireRPG가 모든 핵심 판정을 쥐고, Citizens/BetonQuest/MythicMobs는 외형·대사·연출 보조 레이어로만 쓰는 것이 가장 안전하다.**
