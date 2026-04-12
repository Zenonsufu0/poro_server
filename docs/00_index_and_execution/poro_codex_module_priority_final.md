# 포로 서버 Codex 모듈별 구현 우선순위 최종판 초안

## 문서 목적
이 문서는 Codex로 실제 구현을 시작할 때
**어떤 모듈부터 어떤 순서로 만드는 게 가장 안정적인지** 정리한 최종 우선순위 초안이다.

핵심 목표:
- 구조가 엉키지 않게 선후관계를 잡는다.
- 가장 먼저 필요한 공용 기반을 놓치지 않게 한다.
- 보스/전투/생활/UI/API를 병렬로 개발하더라도 기준 순서를 제공한다.

---

## 1단계: 공용 기반
- core/common module
- enum / code registry
- base entity / audit / ids
- config loader
- result wrapper / dto 공통 구조

## 2단계: 데이터 레이어
- item master
- skill master
- boss master
- quest master
- achievement master
- region/town/npc master
- seed loader

## 3단계: 전투 기반
- 상태 registry
- 전투 계산기
- 스킬 실행기
- 리소스(검세/잔심/압박/공명 등) 처리
- 버프/디버프 처리기

## 4단계: 보스 엔진
- phase controller
- pattern scheduler
- boss run record
- deathcount / revive / fail logic
- result screen

## 5단계: 성장 시스템
- 장비 장착
- 강화
- 잠재
- 세트효과
- 룬
- 각인

## 6단계: 생활 / 영지
- 자원 수집
- 제작식
- 생활 숙련도
- 영지 해금
- 시설 설치 / 업그레이드 / 수확

## 7단계: 퀘스트 / 업적
- objective evaluator
- reward resolver
- 퀘스트 진행
- 업적 추적
- 칭호/뱃지 해금

## 8단계: 운영 / 조회
- admin api
- dashboard summary
- Discord bot
- web 조회 페이지
- 명예의 전당 / 시즌 기록

---

## 9. 한 줄 요약

**Codex 구현은 공용 기반 → 데이터 마스터 → 전투 기반 → 보스 엔진 → 성장 시스템 → 생활/영지 → 퀘스트/업적 → 운영/조회 순으로 가는 것이 가장 안정적이다.**
