# 포로 서버 상태이상 / 제어 / 표식 코드 마스터 표 초안

## 문서 목적
이 문서는 이미 정한 상태 체계를 실제 코드/데이터 마스터 형태로 정리한 초안이다.

핵심 목표:
- 버프 / 디버프 / 상태이상 / 제어 / 표식류를 코드 레벨에서 통일한다.
- 전투, 보스 패턴, 스킬, 소비 아이템, 장비 옵션이 같은 상태 코드를 참조하게 한다.
- Codex가 state registry를 만들기 쉽게 한다.

---

## 1. 상위 분류

- `BUFF`
- `DEBUFF_STATUS`
- `DEBUFF_CONTROL`
- `DEBUFF_MARK`
- `SPECIAL`

---

## 2. 상태이상 코드

| state_code | 분류 | 설명 |
|---|---|---|
| bleed | DEBUFF_STATUS | 출혈 |
| poison | DEBUFF_STATUS | 독 |
| burn | DEBUFF_STATUS | 화상 |
| frost_erosion | DEBUFF_STATUS | 냉기 침식 |

---

## 3. 제어 코드

| state_code | 분류 | 설명 |
|---|---|---|
| slow | DEBUFF_CONTROL | 둔화 |
| bind | DEBUFF_CONTROL | 속박 |
| stagger | DEBUFF_CONTROL | 경직 |
| freeze | DEBUFF_CONTROL | 빙결 |

---

## 4. 표식류 코드

| state_code | 분류 | 설명 |
|---|---|---|
| target_mark | DEBUFF_MARK | 표적 |
| crack | DEBUFF_MARK | 균열 |
| exposed_weakness | DEBUFF_MARK | 약점 노출 |
| stigma | DEBUFF_MARK | 낙인 |
| shadow_mark | DEBUFF_MARK | 암영 표식 |

---

## 5. 버프 코드 예시

| state_code | 분류 | 설명 |
|---|---|---|
| recovery_boost | BUFF | 회복 효율 증가 |
| damage_reduction | BUFF | 피해 감소 |
| movement_boost | BUFF | 이동 속도 증가 |
| shield_active | BUFF | 보호막 활성 |

---

## 6. 추천 필드

- `state_code`
- `state_group`
- `display_name`
- `is_stackable`
- `max_stack`
- `duration_default`
- `notes`

---

## 7. 한 줄 요약

**상태 체계는 bleed/poison/burn/frost_erosion 같은 상태이상, slow/bind/stagger/freeze 같은 제어, target_mark/crack/exposed_weakness/stigma/shadow_mark 같은 표식류를 공통 state registry로 통일하는 것이 가장 안정적이다.**
