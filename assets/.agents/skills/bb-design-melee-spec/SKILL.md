---
name: bb-design-melee-spec
version: v12.0-category-aware
description: category canonical + ref_spec.json을 합쳐 design.md를 작성한다.
---

# bb-design-melee-spec

## 목표
카테고리별 canonical을 기준으로 `design.md`를 빌드 가능한 설계서로 완성한다.

---

## 입력

```txt
refs/category_route.json
refs/ref_spec.json
<category canonical skill>
design.md template
```

---

## 절차

1. `category_route.json.category` 확인.
2. `category_route.json.canonical_skill` 로드.
3. `ref_spec.json`의 수치를 canonical 템플릿에 채운다.
4. category별 금지 요소가 섞였는지 제거한다.
5. `design.md` 저장.

---

## design.md 필수 섹션

```md
# <variant> Design Spec
Category:
Set:
Route:

## 1. Overall Proportions
## 2. Part List
## 3. Coordinates and Dimensions
## 4. Color Palette
## 5. Thickness / Z-Depth Guide
## 6. Construction Phases
## 7. Must-Not-Break Constraints
## 8. Technical Constraints
  ### 8.0 Fixed at design
  ### 8.1 Cube budget
  ### 8.2 Dimensions and coordinates
  ### 8.3 Texture strategy
  ### 8.4 Coordinate rules and mirror
  ### 8.5 Physical attachment graph
## 9. Display / Effect Hooks
## 10. Texture Painting Guide
## 11. Validation Checklist
```

---

## 공통 규칙

- `[TBD]` 남기지 않는다.
- 카테고리 범위 밖 수치는 `WARN`로 기록하고 가능한 가장 가까운 합리값으로 보정한다.
- p4 Z-Depth Profile은 §5와 §8.2에 반드시 반영한다.
- 팔레트는 p2의 6색을 그대로 사용한다. 임의 색 추가 금지. Gem Highlight #FFFFFF는 보조 허용.
- Texture strategy는 256×256, per-face UV, PNG, square를 기본값으로 한다.
- feature는 `optional`/`required`를 명시한다.

---

## feature 처리

```txt
wing           -> category가 greatsword이고 route에 wing 있을 때만 required
blade_core     -> route에 blade_core 있을 때 optional/required 명시
bevel          -> greatsword/katana에서만 권장
socket_gem     -> guard/pommel/head/core 등 위치 명시
scope          -> ranged만
reactor_ring   -> arcane_core만
shaft_wrap     -> spear/staff만
```

---

## 출력 전 Self-check

- [ ] category canonical과 일치
- [ ] p1~p4 수치 반영
- [ ] p4 Z-depth 반영
- [ ] category-specific build skill 명시
- [ ] feature-library 위임 파트와 직접 좌표 파트 구분
- [ ] `[TBD]` 없음
- [ ] `Next: /bb-pre-build-audit` 명시
