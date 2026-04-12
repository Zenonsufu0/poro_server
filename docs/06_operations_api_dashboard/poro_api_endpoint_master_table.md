# 포로 서버 API 엔드포인트 마스터 표 초안

## 문서 목적
이 문서는 운영 웹과 디코 봇, 내부 서비스가 사용할 API 엔드포인트를 정리한 초안이다.

핵심 목표:
- 어떤 조회/통계/관리 API가 필요한지 빠르게 파악한다.
- 운영자용과 유저용을 구분한다.
- DTO와 페이지 구조를 API 기준으로 연결한다.

---

## 1. 운영자용 API

| Method | Endpoint | 설명 |
|---|---|---|
| GET | /admin/dashboard/overview | 대시보드 요약 |
| GET | /admin/dashboard/bosses | 보스 통계 |
| GET | /admin/dashboard/economy | 경제 통계 |
| GET | /admin/dashboard/life | 생활 통계 |
| GET | /admin/players/{userId} | 플레이어 상세 |
| GET | /admin/alerts | 현재 경고 목록 |

---

## 2. 유저 조회 API

| Method | Endpoint | 설명 |
|---|---|---|
| GET | /public/player/{userId}/snapshot | 유저 스냅샷 |
| GET | /public/player/{userId}/equipment | 장비 요약 |
| GET | /public/player/{userId}/boss-records | 보스 기록 |
| GET | /public/player/{userId}/life | 생활 요약 |
| GET | /public/market/item/{itemId} | 시세 조회 |
| GET | /public/hall-of-fame | 명예의 전당 |

---

## 3. 내부 서비스 API/핸들러 감각

- reward resolution
- quest progress update
- achievement trigger
- boss run persist
- market price snapshot save

---

## 4. 한 줄 요약

**API 엔드포인트는 운영자용 admin/dashboard 계열과 유저용 public snapshot 계열로 나누는 것이 가장 깔끔하다.**
