---
name: bb-build-phase-spear
description: Spear 전용 Blockbench build phase. Shaft 중심 구조와 spearhead/collar 연결 검증.
version: v12.0
---

# Spear Build Phase

## Phase 0 — Init
```txt
Groups: head_group, collar_group, shaft_group, butt_group, optional_tassel_group
Texture: 256×256 per-face UV
```

## Phase 1 — Shaft first
```txt
Create shaft_core first.
Shaft must occupy at least 45% of total Y.
Create butt_cap.
```

## Phase 2 — Spearhead and collar
```txt
Create spearhead_body and spear_tip.
Create collar_center connecting head and shaft.
All head/collar cubes must touch shaft/collar anchor.
```

## Phase 3 — Detail
```txt
Add shaft_bands.
Add side_lugs/tassel only if ref has them.
Add gem/core at collar if ref has it.
No wing_step system.
```

## Phase 4 — UV
```txt
Shaft UV long strip.
Head edge UV separated.
Collar/bands with contrast UV.
```

## Phase 5 — Texture
```txt
Run /bb-texture-paint spear section.
Focus: spearhead edge highlight, shaft banding, collar contrast.
```

## Exit checks
```txt
- shaft present and long enough
- head/collar/shaft physically connected
- no greatsword blade segmentation required
```
