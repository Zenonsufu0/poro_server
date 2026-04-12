# 포로 서버 웹 조회 페이지 데이터 모델 초안

## 문서 목적
이 문서는 운영자용 웹 조회 페이지와 유저용 디코 봇 조회 기능을 구분해서,
각각 어떤 데이터를 보여주는 게 좋은지 정리한 초안이다.

핵심 목표:
- 웹은 운영자/관리자 조회 중심
- 디코 봇은 유저 조회 중심
- 같은 원본 데이터를 서로 다른 표현으로 제공

---

## 1. 기본 방향

### 웹
- 운영자/관리자용
- 상세 통계, 로그, 분포, 경고, 기록 조회
- 표/그래프/필터 중심

### 디코 봇
- 유저용
- 내 장비, 내 강화, 내 잠재, 내 기록, 보스 클리어 기록
- 짧고 빠른 조회 중심

---

## 2. 웹 페이지 추천

- 플레이어 상세 조회
- 장비/강화/잠재 조회
- 보스 기록 대시보드
- 경제/골드/재료 흐름 대시보드
- 생활 자원/거래량 대시보드
- 직업 평균 딜량/클리어율 대시보드

## 3. 디코 봇 추천 명령 감각

- 내 장비 보기
- 내 강화 보기
- 내 잠재 보기
- 내 보스 클리어 기록
- 내 생활 레벨
- 내 영지 상태
- 특정 보스 클리어 기록 보기

---

## 4. 예시 데이터 모델

### 웹용 PlayerDetailView
- user_id
- nickname
- class_id
- weapon_id
- equipped_items
- enhancement_summary
- potential_summary
- recent_boss_records
- life_skill_summary

### 디코용 PlayerSnapshot
- nickname
- class_id
- weapon_id
- total_enhancement
- key_item_summary
- latest_boss_clear
- life_level_summary

---

## 5. 한 줄 요약

**웹 조회는 운영자용 상세 통계와 로그 중심으로, 디코 봇은 유저가 자기 정보와 기록을 빠르게 확인하는 스냅샷 중심으로 두는 것이 가장 적합하다.**
