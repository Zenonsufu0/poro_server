# 포로 서버 API / 디스코드 봇 / 관리자 명령 응답 포맷 초안

## 문서 목적
이 문서는 포로 서버의 운영 데이터가 **웹 API / 디스코드 봇 / 게임 내 관리자 명령**에서
어떤 형식으로 응답되어야 하는지 정리한 초안이다.

핵심 목표:
- 같은 원본 데이터를 서로 다른 채널에서 일관되게 본다.
- 사람이 읽기 쉬운 형식과 기계가 다루기 쉬운 형식을 분리한다.
- 운영자가 빠르게 이상 징후를 파악할 수 있게 한다.
- 나중에 웹/봇/관리자 명령을 붙일 때 형식이 흔들리지 않게 한다.

---

## 1. 기본 철학

### 1-1. 원본 구조는 하나, 표현만 다르게
핵심 원칙은 이거다.

- **DB 원본은 하나**
- **API 응답 구조도 하나**
- 디스코드 봇과 관리자 명령은 그걸 사람이 읽기 쉽게 변환

즉:
- API = 정규 구조
- 디스코드 = 요약형 렌더링
- 관리자 명령 = 빠른 텍스트형 렌더링

---

### 1-2. 모든 응답은 3층으로 나누면 좋다
추천 구조:

1. `summary`
2. `details`
3. `alerts`

예:
- `summary`: 핵심 수치
- `details`: 세부 분해
- `alerts`: 이상 징후 / 주의 사항

이 구조는 거의 모든 운영 응답에 재사용 가능하다.

---

### 1-3. 시간 범위는 항상 명시한다
운영 데이터는 언제 기준인지가 중요하다.

모든 응답에는 가능하면 아래가 있어야 한다.
- `time_range`
- `generated_at`
- `timezone`

추천:
- 서버 표준 시간대 1개 고정
- 웹/봇/관리자 모두 같은 기준 사용

---

## 2. 공통 응답 기본 구조

모든 API 응답 기본 틀 추천:

```json
{
  "ok": true,
  "generated_at": "2026-04-12T18:20:00+09:00",
  "time_range": {
    "from": "2026-04-12T00:00:00+09:00",
    "to": "2026-04-12T18:20:00+09:00",
    "label": "today"
  },
  "summary": {},
  "details": {},
  "alerts": []
}
```

### 필드 의미
- `ok`: 성공 여부
- `generated_at`: 응답 생성 시각
- `time_range`: 집계 대상 시간 범위
- `summary`: 핵심 카드/요약용
- `details`: 세부 표/그래프용
- `alerts`: 경고 목록

---

## 3. 경제 요약 API 포맷

### API 예시
`GET /api/admin/economy/summary?range=today`

### 응답 예시
```json
{
  "ok": true,
  "generated_at": "2026-04-12T18:20:00+09:00",
  "time_range": {
    "from": "2026-04-12T00:00:00+09:00",
    "to": "2026-04-12T18:20:00+09:00",
    "label": "today"
  },
  "summary": {
    "total_gold": 123400000,
    "gold_inflow": 5400000,
    "gold_outflow": 4160000,
    "gold_net": 1240000,
    "trade_volume": 1820000
  },
  "details": {
    "top_inflow_sources": [
      {"source": "boss_reward", "amount": 2400000},
      {"source": "trade", "amount": 1700000}
    ],
    "top_outflow_sources": [
      {"source": "enhancement", "amount": 2100000},
      {"source": "craft", "amount": 1100000}
    ]
  },
  "alerts": [
    {
      "level": "warning",
      "code": "GOLD_NET_SPIKE",
      "message": "골드 순증이 최근 7일 평균 대비 180%입니다."
    }
  ]
}
```

---

## 4. 보스 요약 API 포맷

### API 예시
`GET /api/admin/bosses/summary?range=7d`

### 응답 예시
```json
{
  "ok": true,
  "generated_at": "2026-04-12T18:20:00+09:00",
  "time_range": {
    "from": "2026-04-06T00:00:00+09:00",
    "to": "2026-04-12T18:20:00+09:00",
    "label": "last_7_days"
  },
  "summary": {
    "attempt_count": 421,
    "clear_count": 199,
    "clear_rate": 0.472,
    "avg_clear_time_seconds": 673,
    "berserk_reach_rate": 0.38,
    "berserk_clear_rate": 0.14
  },
  "details": {
    "bosses": [
      {
        "boss_id": "morgvain",
        "boss_name": "봉인의 백빙 사제 모르바인",
        "attempt_count": 71,
        "clear_count": 44,
        "clear_rate": 0.619,
        "avg_clear_time_seconds": 641,
        "most_dangerous_pattern": "white_frost_mark"
      },
      {
        "boss_id": "carmen",
        "boss_name": "심연룡 카르멘",
        "attempt_count": 12,
        "clear_count": 0,
        "clear_rate": 0.0,
        "avg_clear_time_seconds": null,
        "most_dangerous_pattern": "resonance_collapse"
      }
    ]
  },
  "alerts": [
    {
      "level": "critical",
      "code": "BOSS_CLEAR_ZERO",
      "message": "심연룡 카르멘의 최근 7일 클리어율이 0%입니다."
    }
  ]
}
```

---

## 5. 보스 상세 API 포맷

### API 예시
`GET /api/admin/bosses/{boss_id}`

### 응답 예시
```json
{
  "ok": true,
  "generated_at": "2026-04-12T18:20:00+09:00",
  "summary": {
    "boss_id": "agner",
    "boss_name": "불멸화룡 아그네르",
    "tier": "extreme",
    "theme": "south",
    "attempt_count_7d": 18,
    "clear_count_7d": 2,
    "clear_rate_7d": 0.111,
    "avg_clear_time_seconds_7d": 801
  },
  "details": {
    "party_size_breakdown": [
      {"party_size": 1, "attempt_count": 2, "clear_count": 0},
      {"party_size": 2, "attempt_count": 4, "clear_count": 0},
      {"party_size": 3, "attempt_count": 12, "clear_count": 2}
    ],
    "phase_reach_rates": [
      {"phase": 1, "reach_rate": 1.0},
      {"phase": 2, "reach_rate": 0.78},
      {"phase": 3, "reach_rate": 0.41},
      {"phase": 4, "reach_rate": 0.17},
      {"phase": "berserk", "reach_rate": 0.11}
    ],
    "dangerous_patterns": [
      {"pattern_id": "flare_brand_chain", "death_count": 18},
      {"pattern_id": "core_meltdown", "death_count": 11}
    ]
  },
  "alerts": [
    {
      "level": "warning",
      "code": "BERSERK_FAIL_HIGH",
      "message": "발악 진입 후 클리어 성공률이 매우 낮습니다."
    }
  ]
}
```

---

## 6. 직업 / 무기 밸런스 API 포맷

### API 예시
`GET /api/admin/combat/class-balance?range=7d`

### 응답 예시
```json
{
  "ok": true,
  "generated_at": "2026-04-12T18:20:00+09:00",
  "time_range": {
    "from": "2026-04-06T00:00:00+09:00",
    "to": "2026-04-12T18:20:00+09:00",
    "label": "last_7_days"
  },
  "summary": {
    "max_avg_dps": 151230,
    "min_avg_dps": 126540,
    "spread_percent": 19.5
  },
  "details": {
    "classes": [
      {"class_id": "warrior", "avg_dps": 139200, "survival_rate": 0.74},
      {"class_id": "assassin", "avg_dps": 151230, "survival_rate": 0.62},
      {"class_id": "mage", "avg_dps": 126540, "survival_rate": 0.69}
    ],
    "weapons": [
      {"weapon_id": "katana", "avg_dps": 155000},
      {"weapon_id": "greatsword", "avg_dps": 134000},
      {"weapon_id": "spear", "avg_dps": 129500}
    ]
  },
  "alerts": [
    {
      "level": "warning",
      "code": "DPS_SPREAD_HIGH",
      "message": "직업 평균 DPS 편차가 15%를 초과합니다."
    }
  ]
}
```

---

## 7. 성장 / 강화 API 포맷

### API 예시
`GET /api/admin/growth/summary?range=30d`

### 응답 예시
```json
{
  "ok": true,
  "generated_at": "2026-04-12T18:20:00+09:00",
  "time_range": {
    "from": "2026-03-14T00:00:00+09:00",
    "to": "2026-04-12T18:20:00+09:00",
    "label": "last_30_days"
  },
  "summary": {
    "t1_user_ratio": 0.42,
    "t2_user_ratio": 0.58,
    "avg_enhance_level": 15.8,
    "users_at_18_plus": 37
  },
  "details": {
    "enhancement_distribution": [
      {"level": 10, "count": 92},
      {"level": 15, "count": 74},
      {"level": 18, "count": 37},
      {"level": 20, "count": 8}
    ],
    "potential_grades": [
      {"grade": "rare", "count": 311},
      {"grade": "epic", "count": 204},
      {"grade": "unique", "count": 57},
      {"grade": "legendary", "count": 9}
    ]
  },
  "alerts": []
}
```

---

## 8. 시즌 / 명예의 전당 API 포맷

### API 예시
`GET /api/admin/season/hall-of-fame?season_id=s1`

### 응답 예시
```json
{
  "ok": true,
  "generated_at": "2026-04-12T18:20:00+09:00",
  "summary": {
    "season_id": "s1",
    "final_first_clear_count": 7,
    "extreme_first_clear_count": 3,
    "extreme_uncleared_count": 4
  },
  "details": {
    "records": [
      {
        "boss_id": "eldheim",
        "boss_name": "백빙룡 엘드하임",
        "record_type": "first_clear",
        "party_names": ["A", "B", "C"],
        "clear_at": "2026-04-09T22:13:00+09:00"
      }
    ]
  },
  "alerts": []
}
```

---

## 9. 디스코드 봇 응답 원칙

디코는 API 원본을 그대로 보여주지 말고,  
**짧은 운영 요약 카드형 문장**으로 바꾼다.

### 9-1. 운영 요약 응답 예시
명령:
`/운영 요약`

응답 예시:
```text
[운영 요약 - 오늘]
전체 골드: 123,400,000
골드 순증감: +1,240,000
결전 클리어 수: 84
극상위 도전 수: 12
가장 위험한 경고: 카르멘 최근 7일 클리어율 0%
직업 DPS 편차: 19.5%
```

---

### 9-2. 보스 요약 응답 예시
명령:
`/보스 통계 카르멘`

응답 예시:
```text
[심연룡 카르멘 - 최근 7일]
도전: 12
클리어: 0
클리어율: 0.0%
발악 진입률: 16.7%
발악 성공률: 0.0%
가장 위험한 패턴: 공명 붕괴
경고: 발악 진입 후 성공 사례 없음
```

---

### 9-3. 경제 요약 응답 예시
명령:
`/골드 현황`

응답 예시:
```text
[경제 요약 - 오늘]
전체 골드: 123,400,000
생성: 5,400,000
소멸: 4,160,000
순증감: +1,240,000
생성처 TOP1: 보스 보상
소모처 TOP1: 강화
경고: 최근 평균 대비 순증 180%
```

---

### 9-4. 극상위 현황 응답 예시
명령:
`/극상위 현황`

응답 예시:
```text
[극상위 현황]
클리어 완료: 엘드하임, 아그네르, 네브카
미클리어: 아우렐, 엘테론, 세트라, 카르멘
최근 가장 많이 도전한 보스: 카르멘 (12회)
최근 가장 높은 페이즈 도달: 세트라 진실 페이즈
```

---

## 10. 관리자 명령 응답 원칙

게임 안 관리자 명령은  
**짧고 빠르게 읽히는 단문 텍스트**가 좋다.

### 10-1. 경제 응답 예시
명령:
`/admin eco summary`

응답 예시:
```text
[경제]
총 골드 123,400,000
오늘 생성 5,400,000 / 소멸 4,160,000 / 순증 +1,240,000
경고: 골드 순증 급등
```

---

### 10-2. 보스 응답 예시
명령:
`/admin boss summary carmen`

응답 예시:
```text
[심연룡 카르멘]
최근 7일 도전 12 / 클리어 0 / 클리어율 0.0%
발악 진입률 16.7% / 성공률 0.0%
위험 패턴: 공명 붕괴
```

---

### 10-3. 직업 밸런스 응답 예시
명령:
`/admin class dps`

응답 예시:
```text
[직업 평균 DPS - 최근 7일]
전사 139,200
암살자 151,230
마법사 126,540
편차 19.5% (경고)
```

---

### 10-4. 재료 응답 예시
명령:
`/admin material flow`

응답 예시:
```text
[재료 현황 - 오늘]
가장 부족한 재료: 설혼 결정 (-18%)
가장 과잉인 재료: 폭풍 모래핵 (+41%)
주의: 심핵 파편 공급량 급감
```

---

## 11. API 설계 공통 규칙 추천

### 11-1. 숫자 단위
- 원본은 정수/실수 그대로
- 프론트/봇/명령에서 포맷팅
- `%`는 소수값으로 저장하고 렌더링 때 표시

예:
- API: `0.472`
- 화면: `47.2%`

---

### 11-2. 코드 + 표시 문자열 분리
예:
- `boss_id`: `carmen`
- `boss_name`: `심연룡 카르멘`

이렇게 둘 다 내려주면 좋다.

---

### 11-3. alerts는 항상 배열
경고가 없을 수도 있으니 빈 배열 유지

예:
```json
"alerts": []
```

---

### 11-4. 집계 기준을 항상 명시
- `today`
- `last_7_days`
- `last_30_days`
- `season_current`

같은 label을 같이 주는 게 좋다.

---

## 12. 추천 엔드포인트 목록

### 운영 요약
- `GET /api/admin/overview`

### 경제
- `GET /api/admin/economy/summary`
- `GET /api/admin/economy/gold-flow`
- `GET /api/admin/economy/currency-flow`

### 성장
- `GET /api/admin/growth/summary`
- `GET /api/admin/growth/enhancement`
- `GET /api/admin/growth/potential`
- `GET /api/admin/growth/sets`

### 보스
- `GET /api/admin/bosses/summary`
- `GET /api/admin/bosses/{boss_id}`
- `GET /api/admin/bosses/{boss_id}/patterns`
- `GET /api/admin/bosses/{boss_id}/phases`

### 전투 밸런스
- `GET /api/admin/combat/class-balance`
- `GET /api/admin/combat/weapon-balance`

### 시즌 / 명예
- `GET /api/admin/season/hall-of-fame`
- `GET /api/admin/season/extreme-progress`

### 경고
- `GET /api/admin/alerts`

---

## 13. 응답 심각도 단계 추천

경고는 일관된 단계가 있어야 한다.

### 추천 값
- `info`
- `warning`
- `critical`

### 예시
```json
{
  "level": "critical",
  "code": "BOSS_CLEAR_ZERO",
  "message": "심연룡 카르멘의 최근 7일 클리어율이 0%입니다."
}
```

---

## 14. 한 줄 요약

### 설계 원칙
**API는 정규 구조로, 디스코드는 요약 카드형으로, 관리자 명령은 빠른 단문형으로 응답한다.**

### 가장 중요한 것
**어느 채널에서 보든 같은 원본 데이터를 같은 집계 기준으로 봐야 한다.**

---

## 15. 다음으로 가야 할 것

### 1순위
운영자 대시보드 MVP 범위 정리
- 출시 전에 꼭 필요한 카드/그래프
- 출시 후 확장 가능한 항목

### 2순위
테마별 보이스 / 사운드 방향 정리
- 패턴 대표 사운드
- 발악 패턴 사운드
- 클리어 / 실패 사운드

### 3순위
실제 API DTO / 응답 클래스 초안
- Java 객체 기준 설계
- 필드명 표준화
- enum 정리
