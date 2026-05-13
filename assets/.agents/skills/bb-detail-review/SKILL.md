---
name: bb-detail-review
description: 기술 상세 검토 + 필요 시 bb-art-review로 미감 검토 연결.
version: v12.0
---

# Detail Review

## 역할
1. hard spec violation 먼저 확인
2. 기술적으로 깨끗하면 `/bb-art-review` 실행을 권장 또는 수행

---

## Step 1 — Hard spec violations
```txt
- category_route.json과 design.md 불일치
- texture strategy 위반
- cube count 위반
- mirror pair 깨짐
- floating decorations
- origin/pivot 위반
- palette 위반
- category-specific hard fail
```

HARD FAIL 있으면 미감 평가 금지. 먼저 수정 phase로 되돌린다.

---

## Step 2 — Category-specific detail
```txt
greatsword: wing/core/gem/bevel front-back pair, guard depth
katana: tsuba compactness, grip wrap, thin blade
spear: shaft bands, collar attachment, spearhead clarity
dagger: GUI readability, over-detail suppression
twin_blades: mirror consistency and pair identity
guns: Z-axis barrel, muzzle, grip separation
orb: circular silhouette
arcane_core: flat disc, front ring depth
```

---

## Step 3 — Art review
기술 문제가 없으면 `/bb-art-review`를 실행하거나 동일 기준으로 평가한다.

---

## 출력
```txt
Hard violations: none/list
Technical detail notes: ...
Art review summary: ...
Next pass: one concrete action
```
