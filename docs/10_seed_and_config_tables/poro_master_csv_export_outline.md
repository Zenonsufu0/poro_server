# 포로 서버 전체 마스터 CSV 내보내기 구조 초안

## 문서 목적
이 문서는 보스, 장비, 스킬, 퀘스트, NPC 같은 마스터 데이터를 CSV로 관리하거나 내보낼 때의 구조를 정리한 초안이다.

핵심 목표:
- seed/config를 CSV로도 관리 가능하게 한다.
- 엑셀/스프레드시트 기반 검수와 수정이 쉬워지게 한다.
- 나중에 CSV → seed import가 가능하게 한다.

---

## 1. 추천 CSV 묶음

- item_master.csv
- skill_master.csv
- boss_master.csv
- pattern_master.csv
- quest_master.csv
- achievement_master.csv
- npc_master.csv
- town_master.csv
- region_master.csv

## 2. 장점
- 대량 검수 쉬움
- 밸런스 수정 쉬움
- 운영자/기획자와 공유 쉬움

## 3. 주의점
- enum/code 값은 엄격히 맞춰야 함
- 참조 무결성 검증기가 필요함

## 4. 한 줄 요약

**마스터 데이터는 CSV로도 내보낼 수 있게 구조를 잡아두면, 나중에 검수와 밸런스 조정이 훨씬 편해진다.**
