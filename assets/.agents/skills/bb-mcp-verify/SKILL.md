---
name: bb-mcp-verify
description: Live Blockbench verification via MCP. category_route.json 기반 공용/카테고리별 검증.
version: v12.0-category-aware
---

# Blockbench MCP Verify

## 원칙
- MCP query/screenshot 기반으로만 검증한다.
- HARD FAIL은 다음 phase로 진행 불가.
- category_route.json의 category/build_skill을 먼저 확인한다.

---

## Generic checks
```txt
- Total cube count vs design.md §8.1
- Texture resolution/square/non-1x1
- UV mode / box_uv=false when required
- Zero-volume cubes HARD FAIL
- Floaters / unattached decorative cubes HARD FAIL
- Group names match design.md §2
- Mirror pairs correct where required
- Palette hexes within §4
- Pivot/origin matches category
```

---

## Phase 1 silhouette audit

### Y-axis categories
```txt
greatsword/katana/spear/dagger/twin_blades/staff:
- max_y - min_y equals category total
- centered around X=8/Z=8 unless off-center explicitly specified
- silhouette matches category range
```

### Z-axis guns
```txt
pistol/sniper/shotgun:
- max_z - min_z equals category length
- max_z-min_z > max_y-min_y
- barrel points Z, not Y
```

### Magic special
```txt
orb: circular/spherical, diameter 16~20
arcane_core: flat disc, Z depth <= 6, front face detail
```

---

## Category-specific audits

### greatsword
```txt
- blade_lower/mid/upper/tip present
- guard_W > blade_W and <= blade_W×1.5
- if wing feature: wing_r/l step1~5 exist
- angel wing: step5 higher than step1, delta >=4
- wing reach matches ref_spec
- gem/core/raise/bevel front/back pairs
```

### katana
```txt
- blade thin and long
- tsuba present and compact
- no required wing_group/guard_shoulder
- grip wrap or texture plan visible
- blade width not greatsword-like
```

### spear
```txt
- shaft present and >=45% total length
- spearhead and collar touch shaft/head
- no wing_step system unless explicitly decorative but not named wing
- side_lug/tassel attached if present
```

### dagger
```txt
- total Y 12~14
- blade/grip/pommel visible
- cube count below ceiling
- no large wing/crossguard pollution
- GUI screenshot readable
```

### twin_blades
```txt
- right master valid
- Phase 5.5: left file exists
- cube counts match
- X mirror around X=8
- Y/Z identical
```

---

## Output
```txt
MCP Verify: PASS/FAIL
Category: <category>
Phase: <phase>
BLOCKERS:
- expected | actual | fix
WARNINGS:
- ...
Next: stay current phase or proceed
```

## 금지
```txt
"mostly okay" 금지
HARD FAIL 있는데 PASS 금지
카테고리 확인 없이 greatsword 기준 검증 금지
```
