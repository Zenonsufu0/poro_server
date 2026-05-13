# Blockbench asset rules

## WSL source-of-truth rule (HIGHEST PRIORITY, 2026-05-10 정책 변경)

- **2026-05-10부터**: `.bbmodel` 작업 중 유일 원본은 **WSL source**다.
  - 작업 원본: `~/dev/poro-server/assets/source/items/weapons/_season1_2_5d/`
  - Blockbench에서 UNC 경로(`\\wsl.localhost\Ubuntu\home\zenonsufu1\dev\poro-server\assets\source\...`)로 직접 Save As.
  - 최종 완료 후 백업/보관 전용: `C:\Users\User\Project\poro-assets-work\_season1_2_5d`
- **rsync 불필요.** UNC 경로로 WSL에 직접 저장하므로 동기화 단계 없음.
- C 드라이브 `.bbmodel`은 최종 완료 후 보관 전용이며 직접 수정 금지.
- WSL source `.bbmodel` ↔ export JSON은 항상 동시에 갱신한다.

---

- This subtree is for Blockbench asset work only.
- Work only inside `assets/source` unless explicitly allowed otherwise.
- For `.bbmodel` tasks, use this order:
  1. inspect on-disk files first
  2. inspect all provided reference images and notes
  3. create or update `asset_brief.md` and `ref_spec.json`
  4. use Blockbench MCP for model creation, live visual confirmation, screenshots, and direct manipulation
  5. if Blockbench MCP is unavailable, stop and report the exact blocker
  6. do not create `.bbmodel` by direct JSON fallback unless the user explicitly says: `direct .bbmodel JSON fallback allowed`

- If MCP fails:
  - You may still create or edit `.md`, `.json`, and planning files.
  - You may not generate final `.bbmodel` files unless direct `.bbmodel` JSON fallback is explicitly allowed.
  - If fallback is allowed, the output must still satisfy the blockout quality gate below.
  - Do not ask to proceed with low-quality fallback. Report the limitation clearly.

- Keep edits minimal and targeted after a model already exists.
- During first creation, prioritize reference similarity over edit convenience.
- Minecraft-style readability must remain high.
- Never write exported Minecraft JSON or final packed resource files back into `assets/source`.
- `.bbmodel`, source textures, notes, refs, and work-in-progress files may stay under `assets/source`.

## Reference priority

- If a primary reference is provided, analyze it before creating geometry.
- Do not replace reference-based design with a generic Minecraft sword silhouette.
- Similarity priority:
  1. overall silhouette
  2. weapon length and proportions
  3. blade shape
  4. guard / wing / core shape
  5. handle and pommel shape
  6. texture and color mood

- For fan/reference-inspired work, create an original Poro-server variant.
- Do not copy exact logos, text, symbols, or copyrighted markings.
- Preserve the recognizable design direction, not the exact asset.

## Part structure

Prefer separate parts and groups such as:

- `blade_core`
- `blade_edge`
- `blade_trim`
- `blade_tip`
- `guard_core`
- `wing_guard`
- `handle`
- `pommel`
- `gems`
- `accent_parts`

Floating blocks are forbidden unless they are intentionally designed as magical floating parts and are clearly listed in `ref_spec.json`.

## Blockout quality gate

A blockout is not allowed to be a simple placeholder stick.

A valid blockout must satisfy:

- It should be recognizable from silhouette alone.
- It must show the main blade shape, not a plain rectangle.
- It must show the guard / wing / core identity.
- It must show handle and pommel separation.
- It must avoid large featureless slabs unless the reference is also slab-like.
- It must be useful for visual approval before texture work.
- For complex fantasy weapons, use enough geometry to communicate the shape.

For complex greatswords:

- Simple 8–15 cube blockouts are not acceptable.
- Use roughly 30–80 cubes if needed for silhouette.
- Long blades must include tapering, stepped edges, tip shaping, and asymmetric or curved silhouette if present in the reference.
- Guards must not be a single horizontal bar unless the reference is that simple.
- Important silhouette features must be geometry first, not texture-only.

Bad blockout examples:

- plain cyan rectangle blade
- default Minecraft sword shape
- generic crossguard sword
- 10-cube placeholder with no reference identity
- flat stick with color labels only

Good blockout examples:

- textureless but recognizable weapon silhouette
- blade taper and tip readable
- guard/wing/core readable
- pommel readable
- proportions match the reference

## Texture rules

- 색상 모자이크 배열 금지.
- Texture should support geometry, not replace missing geometry.
- 뼈대는 블럭으로, 질감/색상은 작은 픽셀 단위로.
- Use pixel-level texture only after blockout approval.
- Do not start final texture work during blockout.
- Avoid noisy random pixels.
- Avoid pillow shading.
- Use clear Minecraft-style contrast.

## Workflow

Use this phased workflow:

1. `bb-ref-curator`
   - organize references only
   - no model creation

2. `bb-asset-brief`
   - create `asset_brief.md`
   - create `ref_spec.json`
   - no `.bbmodel`

3. `bb-build-blockout`
   - create geometry-only `.bbmodel`
   - no final textures
   - no UV detail pass
   - must pass blockout quality gate

4. `bb-audit-fix`
   - fix only 1–5 targeted problems
   - do not rebuild everything unless explicitly requested

5. `bb-texture-pass`
   - apply colors and texture after blockout approval
   - do not radically change geometry

6. `bb-export-register`
   - final cleanup and export notes
   - do not place exported Minecraft JSON inside `assets/source`

## MCP requirement

- If Blockbench MCP is required but not connected, stop and report:
  - what MCP resources/tools are visible
  - whether Blockbench MCP is missing from Claude
  - what task cannot be completed without MCP
- Do not pretend visual inspection was performed if MCP screenshots or renders were not produced.
- If the user manually opens the `.bbmodel` and provides screenshots, use those screenshots for visual critique and targeted fixes.

## Greatsword-specific rules

For greatsword variants, also follow:

- `assets/source/items/weapons/01_greatsword/CLAUDE.md`

Greatsword blockouts must include:

- long readable blade body
- shaped blade tip
- blade edge or trim layer
- guard core
- left/right guard or wing feature
- handle
- pommel
- optional gem/core if present in reference

For greatswords, a blockout that looks like a flat rectangular paddle must be rejected and rebuilt.