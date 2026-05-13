---
name: bb-art-director
description: Blockbench 무기 제작 전에 ref_spec과 레퍼런스를 읽어 art_spec.json을 생성한다. 정확한 수치 재현보다 실루엣, 과장, 큐브 디테일 우선순위를 정하는 아트 디렉션 스킬.
version: v13.0
---

# bb-art-director

## 목적
`ref_spec.json`은 기술 명세서다. 이 스킬은 디자이너의 암묵지를 별도 파일로 만든다.

출력 파일:

```txt
refs/art_spec.json
refs/art_direction.md
```

`art_spec.json`은 다음 단계에서 반드시 읽어야 한다.

```txt
/bb-design
/bb-silhouette-pass
/bb-cube-detail-pass
/bb-build-phase-<category>
/bb-art-review
```

---

## 입력

필수:

```txt
refs/ref_spec.json
refs/*_p1.png
refs/*_p2.png
refs/*_p3.png
refs/*_p4.png
```

선택:

```txt
exports/screenshots/front.png
exports/screenshots/side.png
exports/screenshots/iso.png
exports/screenshots/gui_64.png
source/*.bbmodel
```

---

## 핵심 원칙

기술 고정값과 미감 보정값을 분리한다.

### 절대 고정값

```txt
asset_id
category
main_axis
pivot
center_z
total_y
segments_y
z_depth center rule
alpha 0/255
```

### 아트 보정 허용값

```txt
wing visual reach +1~2 px allowed if silhouette is weak
guard shoulder +0.5~1 px allowed if center reads flat
gem body minimum visible size 2x2 front pixels
pommel ring +0.5 px inflate allowed
blade bevel decorative cubes allowed even if not in main dimensions
layer offsets allowed to prevent gold blob / flat slab look
```

절대 고정값을 깨지 않으면서, 아트 보정 허용값으로 보기 좋은 모델을 만든다.

---

## art_spec.json 필수 구조

아래 구조로 작성한다.

```json
{
  "schema_version": "art-spec-v1.0",
  "asset_id": "...",
  "category": "greatsword",
  "style_goal": ["holy", "heroic", "ceremonial", "celestial"],
  "visual_priority": [
    "wing silhouette",
    "center gem readability",
    "blade core line",
    "guard layered bulge",
    "pommel gem"
  ],
  "cube_detail_priority": [
    "layered guard instead of gold blob",
    "individual wing feathers",
    "blade bevel and core slab",
    "pommel ring and socket",
    "grip cross-column and wrap cubes"
  ],
  "exaggeration_rules": {
    "wing_visual_reach_extra_px_allowed": 1,
    "guard_shoulder_extra_px_allowed": 1,
    "gem_min_front_size": "2x2",
    "pommel_ring_inflate_allowed": 0.5,
    "blade_bevel_detail_allowed": true
  },
  "sacrifice_rules": [
    "If crowded, simplify pommel before reducing wing silhouette.",
    "Preserve wing and center gem before minor bevel details.",
    "Prefer readable large forms over exact tiny decorative cubes."
  ],
  "fail_conditions": [
    "looks like plain sword with small wings",
    "guard reads as one gold blob",
    "blade reads as one straight white pole",
    "wing steps are not individually readable",
    "gem is not visible at 64px GUI"
  ],
  "target_scores": {
    "silhouette_strength": 8,
    "cube_detail_richness": 8,
    "visual_identity": 8,
    "focal_point_readability": 8,
    "texture_richness": 7
  },
  "detail_budget": {
    "level": "heroic",
    "min_total_cubes": 90,
    "max_total_cubes": 180,
    "min_detail_cubes": 35,
    "notes": "Do not stop at a low cube count if the model still looks blockout-level."
  }
}
```

---

## 카테고리별 기본 art direction

### greatsword

```txt
시선 우선순위: guard/wing 중심부 -> blade core -> pommel.
형태는 의식용 성검처럼 과장 가능.
날개가 feature면 작게 붙은 장식이 아니라 전체 정체성이어야 한다.
블레이드는 막대기가 아니라 tip/upper/mid/lower 단계가 읽혀야 한다.
```

### katana

```txt
시선 우선순위: 긴 칼선 -> tsuba -> grip wrap.
과장보다 정제감 우선. guard 과대 금지.
```

### spear

```txt
시선 우선순위: spearhead -> collar -> shaft rhythm.
shaft가 빈약하면 실패. 머리 장식과 손잡이 균형을 본다.
```

### dagger

```txt
시선 우선순위: 큰 실루엣 -> blade/grip separation -> pommel.
작은 무기라 디테일보다 굵은 형태와 대비가 중요하다.
```

### twin_blades

```txt
시선 우선순위: pair identity -> one-blade readability -> mirrored silhouette.
한 자루가 너무 복잡하면 세트로 봤을 때 뭉개진다.
```

---

## 출력 규칙

반드시 아래를 출력한다.

```txt
Art direction route: <category> / <style_goal>
Visual priorities: 1~5
Allowed exaggerations: list
Fail conditions: list
Next required skill: /bb-silhouette-pass
```

`refs/art_spec.json`이 없으면 `/bb-build-phase`를 바로 진행하지 말고 먼저 이 스킬을 실행한다.
