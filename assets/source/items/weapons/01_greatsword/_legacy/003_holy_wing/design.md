# 003_holy_wing Design Spec

Reference source: `refs/holy_wing.png`. `task.md` is empty and no `source/*.bbmodel` file was present when this spec was written, so this document defines the first Blockbench build target rather than revising an existing model.

## 1. Overall Proportions

- Identity: holy wing greatsword with a pale cathedral blade, raised golden sacred core, winged gold crossguard, blue gem accents, dark navy grip, and chunky gold pommel.
- Use the front/back views as the primary silhouette. The side view is only a thickness guide.
- Full model reads as a long, heavy greatsword, not a thin rapier. Blade height should be about 68-72% of the total sword height.
- Blade silhouette is tall, straight, and mostly parallel through the middle, with a stepped arched point at the top and a broad socket at the base.
- Crossguard is the widest area of the sword. Its total span should be about 1.6-1.8x the blade's widest lower section.
- Wings sit on both sides of the guard, with white feather stacks stepping outward and slightly upward at the outer tips.
- Grip is short and dark compared with the blade, roughly 18-22% of total height, capped by a gold pommel wider than the grip.
- Keep the side profile slim: blade and wings are compressed, while guard center and pommel remain visibly bulky.

## 2. Part List

Use these names as Blockbench outliner groups or part prefixes:

- `blade_core`: raised golden vertical strip on the centerline, running from the lower blade socket to near the top motif.
- `blade_core_glow`: bright yellow and orange highlight blocks around the core, including the top flame/diamond shape and lower cross flare.
- `blade_face_l` / `blade_face_r`: white/light gray main blade planes on each side of the core.
- `blade_edge_l` / `blade_edge_r`: silver-gray stepped bevel strips that define the outer blade outline.
- `blade_tip`: centered stepped white/silver cap forming the cathedral-like point.
- `blade_shadow_l` / `blade_shadow_r`: controlled gray depth bands visible near the lower and outer blade edges.
- `blade_base_socket`: white/silver block cluster where the blade locks into the guard.
- `guard_center_gold`: thick square/rectangular gold body around the main gem.
- `guard_center_gem`: bright cyan-blue square gem on front and back faces.
- `guard_halo`: gold arch above the center guard, framing the small vertical blue gem.
- `guard_halo_gem`: small vertical blue gem inside the halo.
- `guard_bar_l` / `guard_bar_r`: horizontal gold arms extending from center guard to wing joints.
- `wing_joint_l` / `wing_joint_r`: deep blue connector blocks between guard bars and white wings.
- `wing_guard_l` / `wing_guard_r`: mirrored white feather groups, built from stepped connected blocks.
- `lower_talon_l` / `lower_talon_r`: downward gold claw accents below each side of the guard.
- `handle_grip`: dark navy rectangular grip.
- `handle_ribbing`: raised blue/navy grip bands, evenly spaced.
- `pommel_gold`: chunky gold pommel, round-block/octagonal in front view.
- `pommel_gem`: small cyan-blue gem centered on the front and back of the pommel.

## 3. Symmetry Rules

- Front and back should be effectively identical for silhouette, core, gems, halo, guard, grip, and pommel.
- Left/right symmetry is strict for blade edges, wing feathers, guard arms, talons, grip, and pommel.
- The blade centerline must stay uninterrupted from pommel through grip, guard gem, blade core, and tip.
- Wing feathers must mirror in count, height steps, and block thickness. Do not let one side read heavier than the other.
- Side view may simplify fine face detail, but it must preserve the raised blade core, blade bevel, guard bulk, wing thickness, grip bands, and pommel volume.
- Blue gems should stay centered except for the two blue wing joints and optional tiny side gems on the pommel.

## 4. Color Palette

- White/light gray blade and feathers: `#F2F2F2`
- Silver gray bevels and soft shadows: `#C8CCD4`
- Golden yellow sacred core: `#FFD54D`
- Warm orange core edge/glow: `#FF9E1A`
- Main gold guard and pommel: `#E0B347`
- Dark gold underside/shadow: `#9C6B16`
- Deep blue grip and wing joints: `#1E3A8A`
- Bright blue gem face: `#3DBBFF`
- Dark navy grip base: `#0D234A`

Palette placement:

- Blade and feathers should read mostly white from a distance, with gray used only as bevel/shadow structure.
- Gold should dominate the guard, pommel, halo, talons, and center blade core.
- Orange should be a thin glow edge around the core, not a broad fill color.
- Blue should be concentrated on gems, wing joints, and grip ribbing.
- Do not use random color mosaic arrays. Use deliberate bands, bevel strips, gem highlights, and stepped shadows.

## 5. Thickness Guide

- `blade_tip`: 1-2 blocks thick, centered and narrow.
- Upper blade: 2-3 blocks thick, with a raised gold core visible above the white blade face.
- Mid blade: 3-5 blocks thick. Use a shallow diamond/oval cross-section: raised core, pale side planes, gray outer bevels.
- Lower blade: 5-6 blocks thick near the socket and lower cross flare.
- `blade_base_socket`: 6-8 blocks thick so the blade visibly plugs into the guard.
- `guard_center_gold`: 9-12 blocks thick, the thickest structural point.
- `guard_halo`: 3-5 blocks thick, connected into the guard center and not floating behind it.
- `guard_bar_l/r`: 4-6 blocks thick, tapering slightly toward the wing joints.
- `wing_guard_l/r`: 3-5 blocks thick near the joints, stepping down to 1-2 blocks at feather tips.
- `lower_talon_l/r`: 3-4 blocks thick, connected to the underside of the guard.
- `handle_grip`: 4-5 blocks thick, slimmer than guard and pommel.
- `pommel_gold`: 8-10 blocks thick, wider than grip and visually weighted enough to balance the guard.

## 6. Construction Phases

1. Establish centerline and block out the complete silhouette: full blade height, guard span, grip length, and pommel size.
2. Build the blade mass with `blade_face_l/r`, `blade_edge_l/r`, `blade_tip`, and `blade_base_socket` using only white and gray.
3. Add `blade_core` and `blade_core_glow`: vertical strip, top flame/diamond motif, and lower cross flare. Keep every glow block connected to the blade surface.
4. Build `guard_center_gold`, `guard_center_gem`, `guard_halo`, and `guard_halo_gem` as one connected central assembly.
5. Extend `guard_bar_l/r`, attach `wing_joint_l/r`, then construct `wing_guard_l/r` feather stacks from the joints outward.
6. Add `lower_talon_l/r` beneath the guard, keeping them gold, mirrored, and physically attached.
7. Build `handle_grip`, `handle_ribbing`, `pommel_gold`, and `pommel_gem`. Keep the grip straight and the pommel centered.
8. Final pass: add gray bevel bands, dark gold underside blocks, gem highlights, side-profile cleanup, and front/back symmetry checks.

## 7. Must-Not-Break Constraints

- Do not model from this document until a build phase is explicitly requested.
- Stay inside `003_holy_wing` for this variant.
- Do not use references from sibling variant folders.
- Do not write exported JSON, generated textures, or final pack outputs into `assets/source`.
- Do not create floating blocks. Feathers, halo, glow pieces, talons, gems, and blade details must visibly connect to the main structure.
- Preserve Minecraft-style block readability at low resolution: large silhouette first, controlled pixel detail second.
- Preserve the holy wing identity: pale blade, golden sacred core, blue gems, winged crossguard, dark navy grip, chunky gold pommel.
- Keep color detail intentional and grouped. No noisy color mosaics or scattered single-pixel speckles.
- Avoid over-rounding. Curves in the reference should be translated into stepped block arcs and stair-stepped outlines.
