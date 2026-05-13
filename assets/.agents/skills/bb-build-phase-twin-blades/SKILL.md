---
name: bb-build-phase-twin-blades
description: Twin blades 전용 Blockbench build phase. right master 제작 후 Phase 5.5에서 left mirror 생성.
version: v12.0
---

# Twin Blades Build Phase

## Phase 0 — Init
```txt
Create right master project only.
Groups: blade_group, guard_or_collar_group, grip_group, pommel_group
Texture: 256×256 per-face UV
```

## Phase 1 — One blade silhouette
```txt
Create blade_body, blade_tip, grip_core, pommel_cap.
One blade Total Y 16~18.
```

## Phase 2 — Pair-aware balance
```txt
Create compact guard/collar.
Ensure silhouette will still read when mirrored.
```

## Phase 3 — Detail
```txt
Add paired accent/gem if ref has it.
Keep detail consistent and not too heavy.
```

## Phase 4 — UV
```txt
UV must be mirror-safe.
Texture can be shared between right and left.
```

## Phase 5 — Texture
```txt
Run /bb-texture-paint twin_blades section.
```

## Phase 5.5 — Mirror output
```txt
Duplicate right master to <variant>_left.bbmodel.
Mirror X coordinates around X=8.
Y/Z coordinates identical.
Run /bb-mcp-verify phase 5.5 mirror audit.
```

## Exit checks
```txt
- right and left files exist
- cube counts match
- X mirror correct
- shared texture valid
```
