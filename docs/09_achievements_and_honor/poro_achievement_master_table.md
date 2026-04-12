# 포로 서버 업적 마스터 표 초안

## 문서 목적
이 문서는 포로 서버 업적을 실제 구현 가능한 수준으로 정리한 1차 마스터 표 초안이다.

핵심 목표:
- 업적을 카테고리별로 정리한다.
- 조건과 보상 방향을 seed/config 형태로 옮기기 쉽게 만든다.
- 전투/탐험/생활/영지/특별 업적의 균형을 맞춘다.

---

## 1. 전투 / 보스 업적

| 업적 ID | 업적명 | 조건 | 보상 방향 |
|---|---|---|---|
| ach_clear_first_duel | 첫 결전 | 아무 결전 보스 1회 클리어 | 칭호 조각, 기록 |
| ach_clear_ragnes | 종화를 넘은 자 | 라그네스 클리어 | 지역 칭호 |
| ach_clear_morvain | 백빙을 헤친 자 | 모르바인 클리어 | 지역 칭호 |
| ach_clear_hazard | 폭풍의 틈을 본 자 | 하자드 클리어 | 지역 칭호 |
| ach_clear_serkain | 판결을 넘은 자 | 세르카인 클리어 | 지역 칭호 |
| ach_no_death_clear | 무사망 돌파 | 보스전 무사망 클리어 | 뱃지 |
| ach_zero_deathcount_clear | 최후 생존 | 데카 0 상태 생존 클리어 | 기록 문구 |
| ach_extreme_first_clear | 전승의 문을 연 자 | 아무 극상위 첫 클리어 | 전승 칭호 |

---

## 2. 탐험 업적

| 업적 ID | 업적명 | 조건 | 보상 방향 |
|---|---|---|---|
| ach_discover_hidden_route | 숨겨진 길 | 비밀 루트 1회 발견 | 탐험 뱃지 |
| ach_east_core_discover | 숲의 심부 발견 | 동부 핵심 구역 발견 | 지역 기록 |
| ach_west_core_discover | 폭풍핵 관측 | 서부 핵심 구역 발견 | 지역 기록 |
| ach_south_core_discover | 화맥 심부 발견 | 남부 핵심 구역 발견 | 지역 기록 |
| ach_north_core_discover | 백빙 성소 발견 | 북부 핵심 구역 발견 | 지역 기록 |
| ach_sahar_truth_discover | 진실의 모래 | 사하르 핵심 구역 발견 | 지역 기록 |
| ach_arkanon_core_discover | 공명 심부 접근 | 아르카논 핵심 구역 발견 | 지역 기록 |

---

## 3. 생활 업적

| 업적 ID | 업적명 | 조건 | 보상 방향 |
|---|---|---|---|
| ach_first_craft | 첫 제작 | 아무 제작 1회 성공 | 생활 입문 칭호 |
| ach_first_feast | 첫 만찬 | 만찬 1회 제작 | 프로필 뱃지 |
| ach_first_potion | 첫 연금 | 물약 1회 제작 | 기록 |
| ach_life_lv10 | 숙련의 시작 | 아무 생활 레벨 10 달성 | 생활 뱃지 |
| ach_life_lv20 | 장인 | 아무 생활 레벨 20 달성 | 칭호 |
| ach_first_harvest | 첫 수확 | 영지 첫 수확 | 영지 기록 |

---

## 4. 영지 업적

| 업적 ID | 업적명 | 조건 | 보상 방향 |
|---|---|---|---|
| ach_estate_unlock | 나만의 거점 | 영지 해금 | 영지 칭호 |
| ach_facility_lv3 | 숙련된 관리 | 시설 Lv3 1회 달성 | 영지 뱃지 |
| ach_theme_facility_install | 테마 개척 | 테마 시설 1개 설치 | 전시 아이템 |
| ach_trophy_place | 전시의 시작 | 영지 트로피 1개 배치 | 기록 문구 |

---

## 5. 특별 / 극상위 업적

| 업적 ID | 업적명 | 조건 | 보상 방향 |
|---|---|---|---|
| ach_clear_agner | 불멸화룡의 종화를 넘은 자 | 아그네르 첫 클리어 | 전승 칭호 |
| ach_clear_eldheim | 백설 장막의 종결자 | 엘드하임 첫 클리어 | 후광/뱃지 |
| ach_clear_setra | 기만의 막을 찢은 자 | 세트라 첫 클리어 | 전승 칭호 |
| ach_clear_carmen | 최종 공명 붕괴 생존자 | 카르멘 첫 클리어 | 대표 전승 칭호 |
| ach_extreme_no_death | 완전 정복 | 극상위 무사망 클리어 | 전용 휘장 |
| ach_server_first | 선발대 | 서버 최초 클리어 | 명예의 전당 표시 |

---

## 6. 한 줄 요약

**업적은 전투/탐험/생활/영지/특별 카테고리로 나누고, 각 업적의 조건과 보상 방향을 먼저 고정해두면 나중에 seed/config와 UI, 칭호 시스템으로 연결하기가 훨씬 쉬워진다.**
