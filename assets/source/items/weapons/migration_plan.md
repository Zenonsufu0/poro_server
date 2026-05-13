# Weapons 폴더 Legacy 이동 계획

> 작성일: 2026-05-08
> 상태: **계획만 작성 — 실제 이동 미실행**
> 이동 시점: `_season1_2_5d/` 파이프라인이 안정화된 후 별도 결정

---

## 배경

기존 폴더 분류(01_greatsword ~ 09_arks)는 무기 실루엣 기준으로 설계됨.
1차 시즌 확정 무기 클래스(sword/hammer/spear/crossbow/scythe/staff)와 불일치.
당장 이동하지 않고 `_season1_2_5d/`를 병행 운영 후 이전 여부를 결정한다.

---

## 이동 대상 분류

### A. 즉시 이동 가능 — 빈 폴더, 1차 시즌 미사용 클래스

| 현재 경로 | 이동 대상 | 사유 |
|---|---|---|
| `04_shortsword/` | `_legacy_old_categories/04_shortsword/` | 내용 없음. 1차 시즌 미사용 클래스 |
| `05_doublesword/` | `_legacy_old_categories/05_doublesword/` | 내용 없음. 1차 시즌 미사용 클래스 |
| `06_gun/` | `_legacy_old_categories/06_gun/` | 내용 없음. crossbow와 별개 클래스 |
| `08_orbs/` | `_legacy_old_categories/08_orbs/` | 내용 없음. 1차 시즌 미사용 클래스 |
| `09_arks/` | `_legacy_old_categories/09_arks/` | 내용 없음. 1차 시즌 미사용 클래스 |

### B. 내용 확인 후 이동 — 자산 있음, 재분류 검토 필요

| 현재 경로 | 이동 대상 | 내부 자산 | 재분류 검토 |
|---|---|---|---|
| `01_greatsword/` | `_legacy_old_categories/01_greatsword/` | flame_guardian_sword, holy_wing, jinhsi | sword 클래스로 `_season1_2_5d/01_sword/`에 이관 가능. 별도 결정 필요 |
| `02_katana/` | `_legacy_old_categories/02_katana/` | changli_blazing_brilliance | sword 클래스로 이관 가능. 별도 결정 필요 |

### C. 유지 또는 통합 — 공용 폴더

| 현재 경로 | 처리 방안 |
|---|---|
| `03_spear/` | 빈 폴더. `_season1_2_5d/03_spear/`로 대체됨. 원본 삭제 가능 |
| `07_staff/` | 빈 폴더. `_season1_2_5d/06_staff/`로 대체됨. 원본 삭제 가능 |
| `_factory_output/` | 유지. flame_guardian_sword 테스트팩 보존 |
| `_reference_library/` | 유지. 레퍼런스 라이브러리 공용 사용 |
| `_concept_art/` | 유지. katana 컨셉 보존 |
| `_templates_25d/` | 유지. katana 템플릿 보존 |

---

## 이동 실행 전 체크리스트

- [ ] `_season1_2_5d/` 파이프라인에서 첫 자산(sword_vanquisher_poro_01) 인게임 테스트 통과
- [ ] `01_greatsword` 내 자산들의 `_season1_2_5d/01_sword/` 재분류 여부 결정
- [ ] `02_katana` 내 자산 재분류 여부 결정
- [ ] `assets/source/CLAUDE.md` 및 `01_greatsword/CLAUDE.md` greatsword-specific rules 재검토
- [ ] 이동 시 반드시 `git mv` 사용 (히스토리 보존)

---

## 이동 명령 예시 (실행하지 마라, 참고용)

```bash
# 빈 폴더 이동 예시
git mv assets/source/items/weapons/04_shortsword \
       assets/source/items/weapons/_legacy_old_categories/04_shortsword

# 자산 있는 폴더 이동 예시
git mv assets/source/items/weapons/01_greatsword \
       assets/source/items/weapons/_legacy_old_categories/01_greatsword
```
