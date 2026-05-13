---
name: bb-design-melee-ref
version: v12.0-category-aware
description: 근접무기 p1~p4 레퍼런스 분석. 카테고리별로 필요한 수치만 추출하고 ref_spec.json을 생성한다.
---

# bb-design-melee-ref

## 목표
레퍼런스를 읽어 `refs/ref_analysis.md`와 `refs/ref_spec.json`을 만든다.

- `ref_analysis.md`: 사람이 보기 쉬운 요약
- `ref_spec.json`: 후속 스킬이 신뢰하는 수치 원본

이미지보다 p3 Dimension Summary / p4 Z-Depth Profile의 명시 수치를 우선한다.

---

## 입력

```txt
refs/category_route.json
refs/p1.png 또는 refs/<variant>_p1.png
refs/p2.png 또는 refs/<variant>_p2.png
refs/p3.png 또는 refs/<variant>_p3.png
refs/p4.png 또는 refs/<variant>_p4.png
```

기존 `design.md`는 읽어도 되지만, 수치 판단의 1차 근거로 삼지 않는다.

---

## 공통 추출 필드

`refs/ref_spec.json` 기본 구조:

```json
{
  "category": "katana",
  "set": "Lunar",
  "variant_name": "unknown",
  "primary_axis": "Y",
  "total_length_2d": 64,
  "palette": {
    "Primary": "#0D1B4A",
    "Secondary": "#E0B347",
    "Core_Glow": "#FFF9C4",
    "Gem": "#FFD700",
    "Dark": "#050D1F",
    "Neutral": "#37474F"
  },
  "parts": {},
  "features": [],
  "z_depth_profile": {},
  "texture_notes": {},
  "validation": []
}
```

---

## 카테고리별 추출

### greatsword
필수:

```txt
total_y
blade segment H: tip/upper/mid/lower
blade segment W: tip/upper/mid/lower
blade main 2D_D per segment
guard W/H/2D_D
grip H/W
pommel H/W/2D_D
wing_steps if wing feature present: step, 2D_W, BB_W, H, 2D_D, BB_D, Y_RISE
wing_reach_2d/bb
guard_shoulder if wing present
blade_core range if present
bevel/guard_raise yes/no
```

### katana
필수:

```txt
total_y 62~64
blade_length / blade_width_2d / blade_depth_2d
tip H
habaki/collar H optional
tsuba W/H/D
grip H/W/D
pommel/endcap H/W/D
wrap bands count/spacing
optional core/hamon/glow line
```

### spear
필수:

```txt
total_y 48~56
spearhead H/W/D
head_tip/head_body split if present
collar H/W/D
shaft H/W/D
butt H/W/D
side_lug/tassel/ornament optional
shaft band positions
```

### dagger
필수:

```txt
total_y 12~14
blade H/W/D
guard or collar W/H/D
grip H/W/D
pommel H/W/D
GUI readability note
```

### twin_blades
필수:

```txt
one_blade_total_y 16~18
blade H/W/D
guard/collar W/H/D
grip H/W/D
pommel H/W/D
pair_mirror_axis X=8
right_master true
left_generated_phase 5.5
```

---

## 출력: refs/ref_analysis.md

짧고 밀도 있게 작성한다.

```md
# Ref Analysis
Category: <category> / confidence <x>
Set: <set>
Total: <value>
Primary axis: Y
Features: wing, blade_core, gem

## Dimensions
...

## Z-depth
...

## Palette
...

## Build notes
...

## Validation
- H sum = PASS
- category range = PASS
- p4 present = PASS
```

---

## 출력: refs/ref_spec.json

숫자는 가능하면 number로 저장한다. 모르면 null, 추정이면 `estimated: true`와 evidence를 붙인다.

```json
{
  "value": 50,
  "estimated": false,
  "source": "p3 Dimension Summary"
}
```

---

## 검산

- [ ] p4 Z-Depth Profile 반영
- [ ] p2 팔레트 6색 + Neutral 포함
- [ ] total length가 category 범위 안
- [ ] 카테고리별 필수 파트 존재
- [ ] wing feature가 있을 때만 wing_steps 추출
- [ ] p3/p4 수치가 p1 이미지 추정보다 우선
