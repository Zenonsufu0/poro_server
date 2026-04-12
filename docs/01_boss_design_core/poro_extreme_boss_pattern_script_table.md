# 포로 서버 극상위 보스 패턴 스크립트 표 초안

## 문서 목적
이 문서는 포로 서버 극상위 보스를 실제로 구현하기 위해 필요한 **극상위 전용 패턴 운용 흐름**을 표 중심으로 정리한 초안이다.

중요:
- 이 문서는 최종 피해량/쿨타임표가 아니라 **운용 구조와 페이즈 흐름**을 고정하는 문서다.
- 극상위는 결전보다 “공정하지만 매우 어려운” 방향을 유지해야 한다.
- 반복 파밍보다 도전/업적/연습의 성격이 강하므로, 패턴의 개성과 기억점이 중요하다.

---

## 1. 극상위 공통 원칙

### 1-1. 극상위의 핵심
- 패턴 수 자체보다 **패턴 결합 방식**이 중요하다.
- 한 번 본 사람은 “어디서 죽었는지”를 이해할 수 있어야 한다.
- 랜덤성은 있되, 판별 불가능한 수준의 억까는 금지한다.

### 1-2. 페이즈 구조 권장
- 1페이즈: 테마 핵심 룰 학습
- 2페이즈: 룰 강화 + 공간 압박
- 3페이즈: 무력화/저지/판정형 기믹
- 4페이즈: 보스 정체성 완성
- 0% 발악: 마지막 확인전

### 1-3. 패턴 분류
- `BASIC`
- `CORE`
- `PHASE`
- `STAGGER`
- `BERSERK`
- `TRANSITION`
- `ULTIMATE`  // 극상위만의 대표 전용 패턴

---

## 2. 수도 극상위 — 아우렐

### 보스 의도
- 황성/법/판결의 완성형
- 수도 결전보다 훨씬 강한 규칙 통제
- “금지 영역 + 판결 누적 + 최종 심판”의 압박

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | imperial_blade | BASIC | 기본 | LOW | 1회 | 기본 견제 |
| 1 | decree_line | BASIC | 기본 | MID | 1회 | 직선 판정 |
| 1 | crime_mark | CORE | 대상 지정 가능 시 | HIGH | 2회 금지 | 죄목 표식 |
| 2 | forbidden_sector | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 금지 영역 확대 |
| 2 | sentence_accumulation | CORE | 일정 시간마다 | HIGH | 2회 금지 | 판결 누적 |
| 2 | confiscation_chain | PHASE | 표식 대상 존재 시 | HIGH | 2회 금지 | 몰수/속박 |
| 3 | tribunal_stagger | STAGGER | 전환 시 | FORCED | 해당 없음 | 재판 저지전 |
| 3 | verdict_split_field | ULTIMATE | 무력화 전후 | HIGH | 2회 금지 | 전장 분할 |
| 4 | execution_square | PHASE | 후반부 | HIGH | 2회 금지 | 처형 구역 |
| 4 | final_sentence_mark | CORE | 후반부 대상 지정 | HIGH | 2회 금지 | 강화 죄목 |
| BERSERK | last_judgment | BERSERK | 발악 진입 | FORCED | 해당 없음 | 최종 판결 |
| BERSERK | execution_square_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 전장 봉쇄 강화 |

### 운용 메모
- 수도 극상위는 “룰 이해력”이 가장 중요한 보스여야 한다.
- 죄목, 금지 구역, 판결 누적이 계속 연결되도록 만든다.
- 발악은 판결 저지 실패 시 즉시 전멸처럼 명확한 끝이 좋다.

---

## 3. 서부 극상위 — 네브카

### 보스 의도
- 서부의 공간 기만과 이동 압박 완성형
- 폭풍 장벽, 가짜 안전지대, 사막 붕괴의 강화판

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | sand_cleave | BASIC | 기본 | LOW | 1회 | 기본 견제 |
| 1 | storm_slice | BASIC | 기본 | MID | 1회 | 장거리 견제 |
| 1 | shifting_oasis | CORE | 일정 시간마다 | HIGH | 2회 금지 | 이동형 안전지대 |
| 2 | false_oasis_prime | ULTIMATE | 2페이즈 진입 후 | HIGH | 2회 금지 | 가짜 안전지대 강화 |
| 2 | desert_collapse_ring | PHASE | 전장 조건 충족 | HIGH | 2회 금지 | 외곽 붕괴 |
| 2 | storm_wall_shift_plus | CORE | 일정 간격 | HIGH | 2회 금지 | 장벽 강화 |
| 3 | sand_core_stagger | STAGGER | 전환 시 | FORCED | 해당 없음 | 모래핵 저지 |
| 3 | mirage_route_lock | PHASE | 무력화 전후 | HIGH | 2회 금지 | 이동 루트 봉쇄 |
| 4 | double_false_oasis | ULTIMATE | 후반부 | HIGH | 2회 금지 | 이중 가짜 안전지대 |
| 4 | sandstorm_prison | PHASE | 후반부 | HIGH | 2회 금지 | 폭풍 감옥 |
| BERSERK | desert_heart_break | BERSERK | 발악 진입 | FORCED | 해당 없음 | 모래 심핵 폭주 |
| BERSERK | mirage_route_lock_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 길 봉쇄 강화 |

### 운용 메모
- 네브카는 “안전해 보이는 곳이 안전하지 않다”를 가장 잘 보여주는 보스
- 단, 판별 단서는 결전보다 더 정교하지만 여전히 읽을 수 있어야 한다
- 발악은 길찾기 + 저지의 결합으로 가면 좋다

---

## 4. 동부 극상위 — 엘테론

### 보스 의도
- 본체 판별, 숲 침식, 분신 교란의 완성형
- 결전보다 훨씬 강한 판별/억압 구조이되 단서를 충분히 제공

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | root_blade | BASIC | 기본 | LOW | 1회 | 기본 견제 |
| 1 | spirit_clone | CORE | 기본 순환 | HIGH | 2회 금지 | 분신 생성 |
| 1 | true_body_hint | PHASE | 분신 이후 | MID | 1회 | 본체 단서 |
| 2 | overgrowth_field | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 전장 침식 |
| 2 | erosion_brand | CORE | 대상 지정 시 | HIGH | 2회 금지 | 침식 표식 |
| 2 | clone_route_cut | ULTIMATE | 분신 활성 다수 | HIGH | 2회 금지 | 이동 동선 차단 |
| 3 | world_tree_stagger | STAGGER | 전환 시 | FORCED | 해당 없음 | 세계수 저지 |
| 3 | true_body_reversal | ULTIMATE | 무력화 전후 | HIGH | 2회 금지 | 단서 역전 |
| 4 | forest_of_falsehood | PHASE | 후반부 | HIGH | 2회 금지 | 거짓 숲 전장 |
| 4 | erosion_brand_plus | CORE | 후반부 | HIGH | 2회 금지 | 침식 강화 |
| BERSERK | spirit_core_bloom_plus | BERSERK | 발악 진입 | FORCED | 해당 없음 | 심핵 개화 강화 |
| BERSERK | clone_route_cut_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 동선 봉쇄 강화 |

### 운용 메모
- 엘테론은 분신 억까가 아니라 “정보량 과부하를 버티는 싸움”
- 본체 단서는 어렵더라도 존재해야 한다
- 발악은 본체 판별 + 침식 회피 + 심핵 저지의 복합 구조 추천

---

## 5. 북부 극상위 — 엘드하임

### 보스 의도
- 냉기/안개/낙인/빙결 공간 통제의 완성형
- 북부 테마 최종 보스다운 압박과 장엄함

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | frost_lance | BASIC | 기본 | LOW | 1회 | 원거리 견제 |
| 1 | blizzard_mark | CORE | 대상 지정 시 | HIGH | 2회 금지 | 낙인 |
| 1 | cold_ring | BASIC | 기본 | MID | 1회 | 범위 압박 |
| 2 | whiteout_field | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 시야 차단 |
| 2 | frozen_step_prison | PHASE | 안개 상태 시 | HIGH | 2회 금지 | 이동 제약 |
| 2 | blizzard_mark_plus | CORE | 후속 연계 시 | HIGH | 2회 금지 | 강화 낙인 |
| 3 | frozen_altar_stagger | STAGGER | 전환 시 | FORCED | 해당 없음 | 제단 저지 |
| 3 | outer_frost_collapse | ULTIMATE | 무력화 전후 | HIGH | 2회 금지 | 외곽 혹한 붕괴 |
| 4 | snowstorm_maze | PHASE | 후반부 | HIGH | 2회 금지 | 설혼 미로 |
| 4 | ice_brand_chain | CORE | 후반부 | HIGH | 2회 금지 | 낙인 연쇄 |
| BERSERK | frost_heart_last_exposure | BERSERK | 발악 진입 | FORCED | 해당 없음 | 마지막 심핵 노출 |
| BERSERK | whiteout_field_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 백색 장막 강화 |

### 운용 메모
- 엘드하임은 북부 최종답게 장엄하고 느리지만 치명적인 압박이 중요
- 시야 차단은 핵심 패턴을 완전히 숨기면 안 된다
- 발악은 “마지막 심핵 파괴 실패 시 전멸”이 가장 잘 어울린다

---

## 6. 남부 극상위 — 아그네르

### 보스 의도
- 화산/과열/연쇄폭발/심핵 저지의 완성형
- 네가 특히 공을 들여야 한다고 본 대표 극상위

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | inferno_claw | BASIC | 기본 | LOW | 1회 | 기본 견제 |
| 1 | magma_arc_breath | BASIC | 기본 | MID | 1회 | 광역 브레스 |
| 1 | core_brand | CORE | 대상 지정 시 | HIGH | 2회 금지 | 심핵 낙인 |
| 2 | overheat_floor_rise | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 바닥 과열 |
| 2 | molten_chain_burst | CORE | 낙인 대상 존재 | HIGH | 2회 금지 | 연쇄폭발 |
| 2 | magma_fall_cross | PHASE | 전장 조건 충족 | HIGH | 2회 금지 | 십자 낙하 |
| 3 | molten_heart_stagger | STAGGER | 전환 시 | FORCED | 해당 없음 | 심핵 저지 |
| 3 | overheat_floor_rise_plus | ULTIMATE | 무력화 전후 | HIGH | 2회 금지 | 과열 강화 |
| 4 | eruption_maze | PHASE | 후반부 | HIGH | 2회 금지 | 분화 미로 |
| 4 | core_brand_chain_plus | CORE | 후반부 | HIGH | 2회 금지 | 낙인 연쇄 강화 |
| BERSERK | molten_heart_last_exposure | BERSERK | 발악 진입 | FORCED | 해당 없음 | 마지막 심핵 노출 |
| BERSERK | inferno_collapse | BERSERK | 발악 중 | HIGH | 2회 금지 | 종화 폭주 |

### 운용 메모
- 아그네르는 “보이는 공포”의 정점이어야 한다
- 과열, 낙인, 분화가 계속 연결되면서도 읽히게 설계
- 발악은 심핵 파괴 실패 시 전멸이 가장 잘 어울린다

---

## 7. 사하르 극상위 — 세트라

### 보스 의도
- 기만/환영/가짜 클리어/진실 페이즈의 정점
- 네가 말한 “깬 줄 알았는데 한 페이즈 더” 컨셉 반영 가능

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | mirage_cut_plus | BASIC | 기본 | LOW | 1회 | 기본 견제 |
| 1 | false_oasis_prime | CORE | 기본 순환 | HIGH | 2회 금지 | 가짜 안전지대 |
| 1 | mirage_brand | CORE | 대상 지정 시 | HIGH | 2회 금지 | 환영 표식 |
| 2 | illusion_market_shift | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 시장 전장 교란 |
| 2 | caravan_of_lies | ULTIMATE | 특정 시간 | HIGH | 2회 금지 | 환영 행렬 |
| 2 | mirage_brand_plus | CORE | 연계 가능 시 | HIGH | 2회 금지 | 표식 강화 |
| 3 | truth_stagger_plus | STAGGER | 전환 시 | FORCED | 해당 없음 | 진실 노출 저지 |
| 3 | fake_clear_transition | TRANSITION | 0% 도달 또는 특정 조건 | FORCED | 해당 없음 | 가짜 클리어 연출 |
| 4 | true_desert_realm | PHASE | 진실 페이즈 진입 | HIGH | 2회 금지 | 진실 전장 |
| 4 | no_oasis_verdict | ULTIMATE | 진실 페이즈 | HIGH | 2회 금지 | 진짜 안전지대 판별 |
| BERSERK | mirage_heart_break | BERSERK | 발악 진입 | FORCED | 해당 없음 | 핵 붕괴 |
| BERSERK | caravan_of_lies_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 기만 극대화 |

### 운용 메모
- 세트라는 “속였지만 납득은 되는” 방향이 중요
- 가짜 클리어 연출은 한 번은 강하게 먹히되, 반복 도전 시 너무 길면 피로하니 축약 가능
- 진실 페이즈는 완전히 다른 맵/분위기 연출이 어울린다

---

## 8. 아르카논 극상위 — 카르멘

### 보스 의도
- 공명/붕괴/구획/레이저/계산형 패턴의 정점
- 아르카논 테마의 대표 최종 보스

| 페이즈 | 패턴명 | 분류 | 사용 조건 | 우선순위 | 연속 사용 제한 | 비고 |
|---|---|---|---|---|---|---|
| 1 | beam_slice_plus | BASIC | 기본 | LOW | 1회 | 레이저 절단 |
| 1 | resonance_node_mark | CORE | 기본 순환 | HIGH | 2회 금지 | 공명 표식 |
| 1 | sector_shift_plus | CORE | 일정 시간 | HIGH | 2회 금지 | 구획 변화 |
| 2 | safe_cell_relocate_plus | PHASE | 2페이즈 진입 후 | HIGH | 2회 금지 | 안전 구역 재배치 |
| 2 | resonance_link_plus | CORE | 오브젝트 활성 시 | HIGH | 2회 금지 | 연결선 패턴 |
| 2 | beam_grid_lock | PHASE | 일정 시간 | HIGH | 2회 금지 | 격자 봉쇄 |
| 3 | sync_core_stagger_plus | STAGGER | 전환 시 | FORCED | 해당 없음 | 동기화 저지 |
| 3 | resonance_collapse | ULTIMATE | 무력화 전후 | HIGH | 2회 금지 | 공명 붕괴 |
| 4 | sector_annihilation | PHASE | 후반부 | HIGH | 2회 금지 | 구획 소거 |
| 4 | node_mark_chain | CORE | 후반부 | HIGH | 2회 금지 | 표식 연쇄 |
| BERSERK | final_resonance_break | BERSERK | 발악 진입 | FORCED | 해당 없음 | 최종 공명 붕괴 |
| BERSERK | beam_grid_lock_plus | BERSERK | 발악 중 | HIGH | 2회 금지 | 레이저 강화 |

### 운용 메모
- 카르멘은 “정확한 규칙을 이해했는가”를 끝까지 묻는 보스
- ULTIMATE 패턴인 공명 붕괴는 이 보스의 대표 얼굴이어야 한다
- 발악은 계산/판독/순간이동의 총합 느낌이 좋다

---

## 9. 공통 구현 메모

### 9-1. 전환 연출
극상위는 TRANSITION 패턴이 중요하다.
- 단순 컷신이 아니라
- 룰이 바뀌는 시점
- 전장이 재정렬되는 시점
으로 느껴져야 한다.

### 9-2. ULTIMATE 패턴
각 극상위는 최소 1개의 대표 ULTIMATE 패턴이 있어야 한다.
그 패턴이 곧 보스의 얼굴이 된다.

### 9-3. 발악
발악은 길고 귀찮은 구간보다
짧고 강하게 마지막 확인전 역할을 하게 만드는 게 낫다.

---

## 10. 다음으로 가기 좋은 것

1. **보스별 보상 드랍 테이블 상세**
2. **생활 숙련도 경험치표 / 레벨 보정표**
3. **관리자 명령 최종 목록표**
4. **요리/만찬/물약 아이템 seed 표**
5. **최신 zip 갱신**

---

## 11. 한 줄 요약

**극상위 보스 스크립트 표는 각 테마의 최종 완성형 보스가 어떤 핵심 패턴과 전환, 발악 구조를 갖는지 고정하는 문서이며, 특히 ULTIMATE 패턴과 발악의 역할을 분명하게 잡아두면 Codex가 극상위 보스를 훨씬 안정적으로 구현할 수 있다.**
