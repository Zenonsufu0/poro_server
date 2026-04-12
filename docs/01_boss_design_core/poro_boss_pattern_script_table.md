# 포로 서버 보스별 실제 패턴 스크립트 표 초안

## 문서 목적
이 문서는 지금까지 정리한 보스 기획을 바탕으로,
Codex가 실제 보스 로직을 구현할 때 참고할 수 있도록 **보스별 패턴 운용 흐름**을 표 중심으로 정리한 초안이다.

중요:
- 이 문서는 **최종 수치 확정본이 아니라 스크립트 구조 초안**이다.
- 핵심은 “어떤 페이즈에서 어떤 패턴을 어떤 우선순위로, 어떤 조건에서 쓰는가”를 고정하는 것이다.
- 정확 쿨타임과 피해량은 테스트로 조정할 수 있게 열어둔다.

---

## 1. 공통 규칙

### 1-1. 스크립트 표의 목적
보스 스크립트 표는 아래를 명확히 하기 위한 것이다.

- 페이즈별 패턴 풀
- 패턴 우선순위
- 연속 사용 금지 규칙
- 특정 체력/기믹 구간 전용 패턴
- 무력화/발악 진입 시점
- 안전지대/분신/오브젝트 패턴 등 전장 제어 흐름

---

### 1-2. 패턴 분류
각 보스는 아래 정도의 패턴 그룹으로 나누는 게 좋다.

- `BASIC`: 평상시 기본 패턴
- `CORE`: 보스 정체성을 드러내는 핵심 패턴
- `PHASE`: 특정 페이즈 전용 패턴
- `STAGGER`: 무력화/저지 관련 패턴
- `BERSERK`: 발악 패턴
- `TRANSITION`: 페이즈 전환 연출/기믹

---

### 1-3. 공통 운용 원칙
- 같은 핵심 패턴 연속 2회 이상은 가급적 금지
- 전환 직후 즉사성 패턴 연속 배치 금지
- 무력화 실패 직후 즉사 패턴 연타 금지
- 전장 패턴과 표식 패턴은 교차 배치 추천
- 발악 전에는 보스 정체성 패턴을 한 번 더 강조하는 흐름이 좋음

---

## 2. 표 읽는 법

각 표의 열은 아래 의미다.

- `페이즈`: 패턴이 주로 쓰이는 구간
- `패턴명`: 구현용 식별 패턴
- `분류`: BASIC / CORE / PHASE / STAGGER / BERSERK / TRANSITION
- `사용 조건`: 체력, 페이즈, 전장 상태, 내부 스택 등
- `우선순위`: LOW / MID / HIGH / FORCED
- `연속 사용 제한`: 같은 패턴 재사용 제한
- `비고`: 개발 메모

---

## 3. 결전 보스 스크립트 표

## 3-1. 남부 결전 — 라그네스

### 보스 의도
- 남부 화산 테마 입문 결전
- “보이는 위험”과 과열 압박
- 분화선 / 화상 / 연쇄 폭발 감각 학습

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | magma_claw | BASIC | 기본 | LOW | 1회 | 근접 견제 |
| 1 | flame_breath_arc | BASIC | 기본 | MID | 1회 | 전방 광역 |
| 1 | eruption_line | CORE | 일정 시간마다 | HIGH | 2회 금지 | 분화선 패턴 |
| 1 | heat_mark_single | CORE | 대상 지정 가능 시 | HIGH | 2회 금지 | 낙인 시작점 |
| 2 | overheat_zone_expand | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 과열 구역 확장 |
| 2 | flame_brand_chain | CORE | 낙인 대상 1명 이상 | HIGH | 2회 금지 | 연쇄 폭발 핵심 |
| 2 | magma_wave | BASIC | 기본 | MID | 1회 | 파동형 압박 |
| 3 | core_exposure_stagger | STAGGER | 체력 기준 또는 고정 순환 | FORCED | 해당 없음 | 무력화/딜타임 |
| 3 | eruption_cross | PHASE | core_exposure 이후 | HIGH | 2회 금지 | 십자 분화 |
| 4 | overheat_collapse | PHASE | 체력 하강 / 전장 압박 누적 | HIGH | 2회 금지 | 외곽 압박 강화 |
| BERSERK | heart_meltdown | BERSERK | 발악 진입 | FORCED | 해당 없음 | 심핵 폭주 저지 |
| BERSERK | flame_brand_chain_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 강화 연쇄폭발 |

### 운용 메모
- 1페이즈는 분화선과 화상 대응 학습용
- 2페이즈부터 낙인 연쇄가 본격 핵심
- 3페이즈 무력화 성공 시 짧은 딜타임 제공
- 발악은 “심핵 저지 + 연쇄폭발 회피”의 복합 구조가 좋다

---

## 3-2. 북부 결전 — 모르바인

### 보스 의도
- 시야/냉기/표식 관리 학습
- 북부 테마의 상태 대응 감각 제공
- “안 보이는 위험”과 “표식 폭발” 중심

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | frost_shard_volley | BASIC | 기본 | LOW | 1회 | 원거리 견제 |
| 1 | white_frost_mark | CORE | 대상 지정 가능 시 | HIGH | 2회 금지 | 백빙 낙인 |
| 1 | cold_wave_sweep | BASIC | 기본 | MID | 1회 | 부채꼴 휩쓸기 |
| 2 | fog_field | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 설혼 안개 |
| 2 | frost_prison_spike | CORE | 안개 상태일 때 | HIGH | 2회 금지 | 시야 제한 + 구속 |
| 2 | icy_ring_expand | BASIC | 기본 | MID | 1회 | 범위 회피 |
| 3 | altar_stagger | STAGGER | 체력/시간 조건 | FORCED | 해당 없음 | 제단 저지/무력화 |
| 3 | white_frost_mark_plus | CORE | 무력화 후 또는 실패 후 | HIGH | 2회 금지 | 강화 낙인 |
| 4 | blizzard_outer_field | PHASE | 후반부 | HIGH | 2회 금지 | 외곽 혹한 압박 |
| BERSERK | frozen_core_exposure | BERSERK | 발악 진입 | FORCED | 해당 없음 | 백빙 심핵 노출 |
| BERSERK | blizzard_mark_burst | BERSERK | 발악 중 | HIGH | 2회 금지 | 낙인 폭발 강화 |

### 운용 메모
- 모르바인은 “안개 + 표식 + 외곽 압박” 3축 유지가 중요
- 3페이즈 무력화 기믹은 북부 결전 핵심 포인트
- 발악은 냉기 대응 준비 여부가 체감되게 만들기 좋다

---

## 3-3. 서부 결전 — 하자드

### 보스 의도
- 이동, 위치선정, 유사 안전지대 판별
- 사막/폭풍 테마의 공간 압박 전달

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | sand_slash | BASIC | 기본 | LOW | 1회 | 근접 베기 |
| 1 | dust_arc | BASIC | 기본 | MID | 1회 | 전방 부채꼴 |
| 1 | storm_wall_shift | CORE | 일정 시간마다 | HIGH | 2회 금지 | 폭풍 장벽 |
| 2 | fake_safe_zone | CORE | 장벽 이후 | HIGH | 2회 금지 | 유사 안전지대 |
| 2 | sinkhole_drop | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 함몰지대 |
| 2 | desert_charge | BASIC | 기본 | MID | 1회 | 돌진형 |
| 3 | storm_core_stagger | STAGGER | 체력/전환 조건 | FORCED | 해당 없음 | 폭풍핵 무력화 |
| 3 | mirage_shift | CORE | 무력화 전후 | HIGH | 2회 금지 | 위치 혼란 |
| 4 | storm_wall_double | PHASE | 후반부 | HIGH | 2회 금지 | 장벽 강화 |
| BERSERK | storm_core_meltdown | BERSERK | 발악 진입 | FORCED | 해당 없음 | 폭풍핵 폭주 |
| BERSERK | fake_safe_zone_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 가짜 안전지대 강화 |

### 운용 메모
- 하자드는 안전지대 판별 스트레스를 너무 과하게 주면 안 된다
- “유사 안전지대”는 단서가 분명해야 억까를 피할 수 있다
- 발악은 폭풍핵 + 공간 제어를 동시에 압축하는 방향이 좋다

---

## 3-4. 수도 결전 — 세르카인

### 보스 의도
- 규칙 위반 / 판결 / 구역 통제
- 수도권 결전의 “심판” 감각

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | verdict_slash | BASIC | 기본 | LOW | 1회 | 기본 공격 |
| 1 | law_line | BASIC | 기본 | MID | 1회 | 직선 판정 |
| 1 | sentence_mark | CORE | 대상 지정 시 | HIGH | 2회 금지 | 죄목 표식 |
| 2 | forbidden_zone | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 금지 구역 생성 |
| 2 | judgment_stack | CORE | 일정 간격 | HIGH | 2회 금지 | 판결 스택 누적 |
| 2 | chain_of_guilt | BASIC | 기본 | MID | 1회 | 속박/제어 |
| 3 | trial_stagger | STAGGER | 전환 시 | FORCED | 해당 없음 | 재판 저지 |
| 3 | sentence_mark_plus | CORE | 저지 실패/성공 후 | HIGH | 2회 금지 | 강화 죄목 |
| 4 | execution_field | PHASE | 후반부 | HIGH | 2회 금지 | 처형 구역 |
| BERSERK | final_verdict | BERSERK | 발악 진입 | FORCED | 해당 없음 | 최종 판결 |
| BERSERK | judgment_stack_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 누적 강화 |

### 운용 메모
- 수도는 “룰을 이해하면 피할 수 있음”이 핵심
- 금지 구역과 죄목 표식은 정보 전달이 명확해야 한다
- 발악은 최종 판결 저지전으로 연출하기 좋다

---

## 3-5. 동부 결전 — 카실레아

### 보스 의도
- 분신, 자연 침식, 본체 판별
- 동부 숲 특유의 교란 감각 제공

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | vine_lash | BASIC | 기본 | LOW | 1회 | 근접 견제 |
| 1 | spirit_bloom | BASIC | 기본 | MID | 1회 | 범위 꽃피움 |
| 1 | false_body_split | CORE | 일정 시간 | HIGH | 2회 금지 | 분신 기초 |
| 2 | mist_root_zone | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 안개+뿌리 |
| 2 | nature_erosion_mark | CORE | 대상 표식 가능 시 | HIGH | 2회 금지 | 침식 표식 |
| 2 | blooming_trap | BASIC | 기본 | MID | 1회 | 바닥 함정 |
| 3 | spirit_tree_stagger | STAGGER | 전환 시 | FORCED | 해당 없음 | 세계수 저지 |
| 3 | true_body_reveal | PHASE | 무력화 전후 | HIGH | 2회 금지 | 본체 판별 유도 |
| 4 | forest_overgrowth | PHASE | 후반부 | HIGH | 2회 금지 | 전장 장악 강화 |
| BERSERK | spirit_core_bloom | BERSERK | 발악 진입 | FORCED | 해당 없음 | 심핵 개화 |
| BERSERK | nature_erosion_mark_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 침식 강화 |

### 운용 메모
- 동부는 “틀릴 수는 있지만 읽을 수는 있어야” 한다
- 본체 판별 단서는 충분히 줘야 한다
- 분신 테마를 억까가 아니라 학습형으로 설계하는 게 핵심

---

## 3-6. 아르카논 결전 — 아르케논

### 보스 의도
- 시스템형 보스 입문
- 레이저 구획 / 안전지대 재배치 / 정확한 규칙성

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | beam_slice | BASIC | 기본 | LOW | 1회 | 레이저 절단 |
| 1 | rune_burst | BASIC | 기본 | MID | 1회 | 원형 폭발 |
| 1 | sector_shift | CORE | 일정 시간 | HIGH | 2회 금지 | 구획 이동 |
| 2 | safe_cell_relocate | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 안전 구역 재배치 |
| 2 | resonance_link | CORE | 전장 오브젝트 활성 시 | HIGH | 2회 금지 | 연결선 패턴 |
| 2 | pulse_field | BASIC | 기본 | MID | 1회 | 주기장 |
| 3 | core_sync_stagger | STAGGER | 전환 시 | FORCED | 해당 없음 | 공명 핵 동기화 저지 |
| 3 | sector_shift_plus | CORE | 무력화 이후 | HIGH | 2회 금지 | 강화 구획 이동 |
| 4 | beam_grid_lock | PHASE | 후반부 | HIGH | 2회 금지 | 격자 레이저 |
| BERSERK | resonance_breakdown | BERSERK | 발악 진입 | FORCED | 해당 없음 | 공명 붕괴 |
| BERSERK | safe_cell_relocate_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 재배치 강화 |

### 운용 메모
- 아르카논은 “정확하고 일관된 규칙성”이 가장 중요
- 잘 보이는 패턴보다 “잘 읽히는 패턴”이 더 중요하다
- 발악은 공명 붕괴 저지전으로 깔끔하다

---

## 3-7. 사하르 결전 — 아자켈

### 보스 의도
- 기만/환영/가짜 안전지대 입문
- 극상위 사하르보다 덜 가혹한 버전

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | mirage_cut | BASIC | 기본 | LOW | 1회 | 기본 견제 |
| 1 | illusion_orb | BASIC | 기본 | MID | 1회 | 구체 투사 |
| 1 | false_oasis | CORE | 일정 시간 | HIGH | 2회 금지 | 가짜 안전지대 입문 |
| 2 | caravan_decoy | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 환영 상단 |
| 2 | mirage_brand | CORE | 대상 지정 가능 시 | HIGH | 2회 금지 | 환영 표식 |
| 2 | sand_trick_field | BASIC | 기본 | MID | 1회 | 전장 기만 |
| 3 | truth_stagger | STAGGER | 전환 시 | FORCED | 해당 없음 | 진실 노출 저지 |
| 3 | false_oasis_plus | CORE | 무력화 이후 | HIGH | 2회 금지 | 강화 가짜 안전지대 |
| 4 | market_illusion_collapse | PHASE | 후반부 | HIGH | 2회 금지 | 시장 붕괴 |
| BERSERK | mirage_core_exposure | BERSERK | 발악 진입 | FORCED | 해당 없음 | 핵 노출 |
| BERSERK | mirage_brand_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 환영 표식 강화 |

### 운용 메모
- 사하르 결전은 기만 테마의 입문형이어야 한다
- 가짜 안전지대도 단서가 분명해야 한다
- 사하르 극상위로 가기 전에 학습용 역할이 중요하다

---

## 4. 스크립트 공통 구현 규칙

## 4-1. 패턴 쿨다운 추천 구조
정확 수치는 아직 확정 전이므로, 구조만 고정한다.

- BASIC: 짧은 쿨타임
- CORE: 중간 쿨타임
- PHASE: 길거나 조건부
- STAGGER: 강제
- BERSERK: 발악 전용

---

## 4-2. 우선순위 추천 규칙
- FORCED: 반드시 실행
- HIGH: 가능하면 우선
- MID: 일반 순환
- LOW: 빈 칸 메우기

---

## 4-3. 연속 사용 제한 추천
- BASIC: 1회 정도 허용
- CORE: 2회 연속 금지
- PHASE: 2회 연속 금지
- BERSERK: 강화 패턴이라도 반복 도배 금지

---

## 5. Codex 구현용 추천 데이터 구조

### 추천 JSON/YAML/DB 형태 필드
- `boss_id`
- `phase_no`
- `pattern_id`
- `pattern_group`
- `priority`
- `condition_type`
- `condition_value`
- `cooldown_seconds`
- `max_consecutive_use`
- `is_forced`
- `notes`

이렇게 두면 스크립트 데이터화가 쉽다.

---

## 6. 지금 이후 다음으로 좋은 것

이 문서 다음엔 아래가 자연스럽다.

1. **보스 보상 수치표 초안**
2. **생활 제작식 전체표 초안**
3. **운영 경고 임계값 표 초안**

---

## 7. 한 줄 요약

**보스별 실제 패턴 스크립트 표는 수치보다 운용 흐름을 먼저 고정하는 문서이며, 각 결전 보스가 어떤 페이즈에서 어떤 핵심 패턴을 어떤 조건으로 쓰는지 명확히 잡아두면 Codex가 보스 로직을 훨씬 안정적으로 구현할 수 있다.**
