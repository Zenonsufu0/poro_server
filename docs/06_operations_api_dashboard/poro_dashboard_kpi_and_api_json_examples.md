# 포로 서버 대시보드 KPI / 통계 API 예시 JSON 초안

## 문서 목적
이 문서는 운영 대시보드와 admin-api에서 사용할 KPI와 응답 JSON 예시를 정리한 초안이다.

핵심 목표:
- 운영자가 실제로 어떤 수치를 봐야 하는지 한눈에 정리한다.
- API 응답 형태를 미리 고정해 프론트/백/봇 연동을 쉽게 만든다.

---

## 1. 핵심 KPI 추천

- 일일 골드 순증
- 결전 클리어율
- 극상위 클리어율
- 직업별 평균 DPS
- 생활 자원 공급량
- 만찬 사용량
- 희귀 물약 사용량
- 18강 도달률
- 레전더리 잠재 비율

---

## 2. overview 예시 JSON
```json
{
  "date": "2026-04-12",
  "dailyGoldNet": 1820000,
  "duelBossClearRate": 0.47,
  "extremeBossClearRate": 0.03,
  "topClassAvgDps": 128400,
  "bottomClassAvgDps": 111900,
  "alertCount": 2
}
```

## 3. life summary 예시 JSON
```json
{
  "date": "2026-04-12",
  "topResources": [
    {"resourceId": "herb_basic", "supply": 840},
    {"resourceId": "ore_basic", "supply": 620}
  ],
  "topConsumables": [
    {"itemId": "capital_defense_feast", "uses": 94},
    {"itemId": "basic_healing_potion", "uses": 410}
  ]
}
```

## 4. boss summary 예시 JSON
```json
{
  "date": "2026-04-12",
  "bosses": [
    {
      "bossId": "ragnes",
      "attempts": 88,
      "clearRate": 0.54,
      "avgClearTimeSeconds": 611,
      "avgDeaths": 3.2
    },
    {
      "bossId": "carmen",
      "attempts": 14,
      "clearRate": 0.07,
      "avgClearTimeSeconds": 994,
      "avgDeaths": 6.8
    }
  ]
}
```

## 5. 한 줄 요약

**대시보드 KPI는 경제·보스·전투·생활·성장 카테고리로 나누고, overview/life/boss 같은 summary API 응답 JSON 형태를 미리 잡아두면 운영 도구 구현이 훨씬 쉬워진다.**
