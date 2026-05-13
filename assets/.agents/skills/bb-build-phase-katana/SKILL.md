---
name: bb-build-phase-katana
description: Katana 전용 Blockbench build phase. 얇고 긴 실루엣과 츠바/손잡이 래핑 중심.
version: v12.0
---

# Katana Build Phase

## Phase 0 — Init
```txt
Groups: blade_group, tsuba_group, grip_group, pommel_group, optional_gem_group
Texture: 256×256 per-face UV
```

## Phase 1 — Long blade silhouette
```txt
Create blade_main and blade_tip first.
Total Y 62~64.
Blade width 3~4px range.
Do not split into greatsword blade_lower/mid/upper unless ref explicitly demands.
```

## Phase 2 — Tsuba and handle balance
```txt
Create tsuba_center thin guard.
Create grip_core.
Create end_cap.
Check blade dominates length and tsuba remains compact.
```

## Phase 3 — Wrap/detail
```txt
Add grip wrap bands or texture guide.
Add thin blade highlight/core only if ref has it.
Add small gem only if ref has it.
No wing_group.
```

## Phase 4 — UV
```txt
Blade gets long UV strip.
Grip wrap gets repeated diagonal/stripe UV area.
Tsuba has front/back distinct UV.
```

## Phase 5 — Texture
```txt
Run /bb-texture-paint katana section.
Focus: blade polish line, subtle hamon optional, wrap contrast.
```

## Exit checks
```txt
- total Y 62~64
- blade remains thin
- no greatsword wing/shoulder pollution
- tsuba readable but not oversized
```
