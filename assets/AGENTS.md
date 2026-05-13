# Poro Server Assets Workflow

## Blockbench weapon workflow

When working on Minecraft Java Edition Blockbench voxel weapons, prefer the installed skills under `.agents/skills`.

Primary flow:

1. Use `bb-reference-router` or `bb-design` first.
2. If reference images p1~p4 and `ref_spec.json` exist, treat `ref_spec.json` as the source of truth.
3. Route by category:
   - greatsword -> `bb-design-greatsword-canonical`, then `bb-build-phase-greatsword`
   - katana -> `bb-design-katana-canonical`, then `bb-build-phase-katana`
   - spear -> `bb-design-spear-canonical`, then `bb-build-phase-spear`
   - dagger -> `bb-design-dagger-canonical`, then `bb-build-phase-dagger`
   - twin_blades -> `bb-design-twin-blades-canonical`, then `bb-build-phase-twin-blades`
4. Run `bb-pre-build-audit` before building.
5. Run `bb-mcp-verify` after each major build phase.
6. Run `bb-art-review` before final texture pass.
7. Run `bb-texture-paint` for texture planning and painting instructions.

## Reference rule

For weapon references, use ref-memory-v4.3-auto:

- p1~p4 PNG files are human-readable design sheets.
- `ref_spec.json` is the machine-readable source of truth.
- Do not infer exact dimensions from image text if `ref_spec.json` provides them.
- `Depth (X-axis thickness)` is invalid wording. Use `Z-Depth / Front-Back Thickness`.