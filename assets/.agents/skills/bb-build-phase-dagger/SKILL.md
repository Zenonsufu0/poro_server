---
name: bb-build-phase-dagger
description: Dagger 전용 Blockbench build phase. 짧은 무기 GUI 가독성 우선.
version: v12.0
---

# Dagger Build Phase

## Phase 0 — Init
```txt
Groups: blade_group, guard_or_collar_group, grip_group, pommel_group
Texture: 256×256 per-face UV or compact export
```

## Phase 1 — Bold silhouette
```txt
Create blade_body, blade_tip, grip_core, pommel_cap.
Total Y 12~14.
Make blade/grip/pommel readable at small scale.
```

## Phase 2 — Guard/collar
```txt
Add small guard or collar.
Avoid oversized crossguard.
```

## Phase 3 — Minimal detail
```txt
Add 1 gem max if ref has it.
Add edge highlight via texture more than cubes.
Keep cube count low.
```

## Phase 4 — UV
```txt
Large UV islands for readability.
Avoid tiny unreadable UV fragments.
```

## Phase 5 — Texture
```txt
Run /bb-texture-paint dagger section.
Focus: high contrast and GUI icon clarity.
```

## Exit checks
```txt
- Total Y 12~14
- cube count <= hard ceiling
- no wing, no greatsword structure
- GUI 64px view readable
```
