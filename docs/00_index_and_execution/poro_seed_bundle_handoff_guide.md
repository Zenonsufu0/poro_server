# 포로 서버 seed/config 묶음 전달 가이드 초안

## 문서 목적
이 문서는 지금까지 만든 seed/config 관련 문서를
Codex나 실제 구현 파이프라인에 넘길 때 어떻게 묶어 전달하면 좋은지 정리한 초안이다.

핵심 목표:
- seed 문서를 한 번에 넘기기 좋은 묶음으로 정리한다.
- 구현 단위별 필요한 seed를 같이 주게 한다.
- 누락 없이 전달하게 한다.

---

## 1. 보스 묶음
- boss master
- pattern seed
- reward seed
- entry/record structure
- symbolic/legacy rewards

## 2. 전투/성장 묶음
- skill seed
- skill expansion seed
- status master
- class engraving
- common engraving
- set/rune/engraving seed
- equipment/potential/enhancement seed

## 3. 생활/영지 묶음
- life resource/facility
- recipe seed
- consumable effect seed
- estate upgrade/effect
- market price/economy review

## 4. 퀘스트/명예 묶음
- quest seed
- quest reward seed
- objective master
- achievement seed
- title/badge/hall of fame linkage

## 5. 운영/조회 묶음
- dashboard KPI/API examples
- web page models
- discord command spec
- discord response examples
- admin command list

## 6. 한 줄 요약

**seed/config 문서는 보스, 전투/성장, 생활/영지, 퀘스트/명예, 운영/조회 다섯 묶음으로 나눠 전달하면 Codex가 구현 단위별로 받아 처리하기 가장 쉽다.**
