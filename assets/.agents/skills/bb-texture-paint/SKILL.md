---
name: bb-texture-paint
description: category-aware texture automation. UV map에서 base texture와 shading guide를 생성하고 카테고리별 페인팅 규칙을 제공한다.
version: v12.0-category-aware
---

# Texture Paint — Phase 5

## 목표
기존 flat color 수준에서 끝내지 않고, 최소한의 자동 음영 가이드와 카테고리별 재질 규칙까지 생성한다.

---

## 입력
```txt
design.md §4 palette
uv_map.json
refs/category_route.json
refs/ref_spec.json
```

---

## 자동 출력
```txt
source/textures/<variant>_base.png
source/textures/<variant>_paint_guide.json
source/textures/<variant>_palette.json
```

`base.png`는 flat color + 기본 명암 block-in까지 허용한다.
최종 세밀 수정은 Aseprite/Photoshop에서 진행할 수 있게 guide를 만든다.

---

## 공통 페인팅 규칙
```txt
Light source: upper-left
Highlight: Base보다 Value +30, Hue warm/cyan shift
Base: palette 원색
Shadow: Value -20
Deep shadow: Value -40
Alpha: 0 또는 255만 허용
No pillow shading, no banding, no random noise
```

---

## Face brightness 기본
```txt
front/north: base
back/south: base ×0.9
side/east/west: base ×0.8
up: highlight
down: shadow
```

---

## 카테고리별 규칙

### greatsword
```txt
blade front: center core/glow 1~2px, edge deep shadow
blade side: Primary×0.8
guard front: Secondary center highlight, edge shadow
wing: step1 Base+0, step2 +10, step3 +15, step4 +20, step5 +30
grip: dark diagonal wrap
pommel/gem: radial highlight + dark rim
```

### katana
```txt
blade: thin polish line, optional hamon-like subtle wave as texture only
tsuba: small high-contrast rim
grip: diagonal wrap bands, alternating dark/accent
avoid noisy large gradients
```

### spear
```txt
spearhead: edge highlight and central ridge
shaft: low-saturation wood/metal banding or wrap
collar: high contrast separating head from shaft
tassel if present: simple 2-tone strands
```

### dagger
```txt
strong contrast over fine detail
blade edge: 1px highlight + 1px shadow
gem max clarity, no tiny unreadable patterns
GUI icon readability first
```

### twin_blades
```txt
right/left share visual language
accent distribution balanced
avoid making one blade visually heavier unless intentional
```

### ranged
```txt
barrel top highlight, underside shadow
muzzle/emitter glow if present
grip darker with wrap/texture
scope lens gem/glow gradient
```

### magic
```txt
staff: shaft bands + head crystal glow
orb: outer ring shadow, core glow center
arcane_core: concentric rings, bright core, flat-disc depth cue
```

---

## Guide JSON schema
```json
{
  "category": "greatsword",
  "light_source": "upper-left",
  "palette": {},
  "face_rules": {},
  "category_rules": {},
  "transparency": [],
  "manual_pass_checklist": []
}
```

---

## 최종 체크
```txt
- texture has >=3 distinct colors
- palette outside colors 없음 except allowed highlight variants
- alpha only 0/255
- category-specific key material visible
- gui screenshot readable
```
