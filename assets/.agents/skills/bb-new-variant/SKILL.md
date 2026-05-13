---
name: bb-new-variant
description: Create a new weapon variant folder. Category can be explicit or auto-detected later by bb-design from p1~p4 refs.
version: v12.0-auto-compatible
---

# New Weapon Variant

## Goal
Create one variant folder under `assets/source/items/weapons/`.

---

## Inputs

권장 입력:
```txt
1. Variant name (snake_case)
2. Numeric prefix (e.g. 003)
3. Weapon category or auto
4. Set name/theme
5. Folder override optional
```

카테고리를 모르면 `category: auto`로 생성 가능하다. 이 경우 `/bb-design`이 p1~p4를 읽고 category를 확정한다.

---

## 11 categories

### Melee
```txt
greatsword: 48~52 Y, wide blade, guard, optional wing
katana: 62~64 Y, thin long blade, tsuba, grip wrap
spear: 48~56 Y, shaft/head/collar
 dagger: 12~14 Y, compact readable silhouette
twin_blades: 16~18 Y per blade, right master + left mirror
```

### Ranged
```txt
pistol: 22~26 Z, side-view horizontal
sniper: 56~64 Z, long barrel/scope
shotgun: 28~36 Z, thick body/barrel
```

### Magic
```txt
staff: 56~64 Y, shaft + head
orb: diameter 16~20, floating focus
arcane_core: diameter 16~20, Z depth 4~6, flat chest reactor
```

---

## Folder structure

```txt
<prefix>_<variant>/
  design.md
  task.md
  refs/
    <variant>_p1.png
    <variant>_p2.png
    <variant>_p3.png
    <variant>_p4.png
    category_route.json      # created by bb-reference-router
    ref_analysis.md          # created by design ref skill
    ref_spec.json            # created by design ref skill
  source/
    textures/
  exports/
```

p1~p4 4장은 필수다. 3장 기준은 폐기한다.

---

## design.md template

```md
# <prefix>_<variant> Design Spec

Category: <category or auto>
Set: <set or TBD>
Route: [TBD]

## 1. Overall Proportions
[TBD]

## 2. Part List
[TBD]

## 3. Coordinates and Dimensions
[TBD]

## 4. Color Palette
[TBD]

## 5. Thickness / Z-Depth Guide
[TBD]

## 6. Construction Phases
[TBD]

## 7. Must-Not-Break Constraints
[TBD]

## 8. Technical Constraints

### 8.0 Fixed at creation
- Format: java_block_item
- Category: <category or auto>
- Primary axis: <auto>
- Size: <auto>
- Cube budget: <auto>
- Texture: 256×256 per-face UV default
- Animation: static unless category requires
- Special: <auto>

### 8.1 Cube budget
[TBD]

### 8.2 Dimensions and coordinates
[TBD]

### 8.3 Texture strategy
- UV mode: per-face
- Texture size: 256×256
- Format: PNG square
- Forbidden: 1×1 solid, non-square static texture, colors outside §4 except derived highlights

### 8.4 Coordinate rules and mirror
[TBD]

### 8.5 Physical attachment graph
[TBD]

## 9. Display / Effect Hooks
[TBD]

## 10. Texture Painting Guide
[TBD]

## 11. Validation Checklist
[TBD]
```

---

## After creation
```txt
1. p1~p4 refs 넣기
2. Run /bb-design
3. bb-design이 category_route.json 생성
4. Run /bb-pre-build-audit
5. Run /bb-build-phase
```
