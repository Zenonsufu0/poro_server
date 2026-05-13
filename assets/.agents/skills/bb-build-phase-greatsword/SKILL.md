---
name: bb-build-phase-greatsword
description: Greatsword 전용 Blockbench build phase. Wing/holy_wing 구조는 feature flag가 있을 때만 생성한다.
version: v12.0
---

# Greatsword Build Phase

## Phase 0 — Init
```txt
create_project java_block_item
texture 256×256 PNG
Groups: blade_group, guard_group, grip_group, pommel_group
If wing feature: wing_r_group, wing_l_group
If gem feature: gem_group or nested gem cubes
```

## Phase 1 — Main silhouette
```txt
Create: blade_lower, blade_mid, blade_upper, blade_tip
Create: guard_center, grip_core/rough, pommel_core/rough
Use design.md §5 Z-depth exactly.
Check Total Y 48~52.
```

## Phase 2 — Structure / optional features
```txt
If wing: create wing_step1~5 right and left; apply floor constraint and sweep.
If guard_shoulder: create transition brackets.
If blade_core: create front/back core slabs.
MCP verify phase 2 before continuing.
```

## Phase 3 — Detail
```txt
Replace grip/pommel with CROSS_COLUMN if required.
Add socket_gem front/back pairs only if feature exists.
Add guard_raise front/back only if feature exists.
Add blade_bevel front/back left/right if feature exists.
Add pommel_ring/guard_ring_top if design requires.
```

## Phase 4 — UV
```txt
per-face UV, box_uv=false
UV zones: blade / guard / wing / grip / pommel / gem
Export uv_map.json.
```

## Phase 5 — Texture
```txt
Run /bb-texture-paint
Generate base texture + shading_guide.json.
Confirm 45도/gui/handheld screenshots.
```

## Exit checks
```txt
- guard_W > blade_W and <= blade_W×1.5
- wing only if route.special_features has wing
- front/back pairs for gem/core/raise/bevel
- no floaters
- p4 Z-depth implemented
```
