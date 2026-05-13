---
name: bb-feature-library
description: Category-neutral reusable Blockbench feature modules. Wing/gem/core 등은 기본 구조가 아니라 옵션 feature로만 사용한다.
version: v12.0-module-library
---

# Feature Library

## 전역
```txt
CENTER_X = 8
CENTER_Z = 8
BB_X = round(2D_X × 0.75 × 2) / 2
BB_D = 2D_D × 0.5
from.z = 8 - BB_D/2
to.z = 8 + BB_D/2
```

---

## 사용 원칙

- Feature는 canonical의 기본 구조를 대체하지 않는다.
- Feature는 `refs/category_route.json.special_features` 또는 design.md §2에서 요청될 때만 사용한다.
- front/back 대칭이 필요한 feature는 반드시 쌍으로 배치한다.
- Wing feature는 기본적으로 greatsword 전용이다. spear의 side_lug와 혼동하지 않는다.

---

## CROSS_COLUMN
```txt
용도: grip, pommel, shaft segment 등 중심 기둥
입력: Yf, Yt, CW=4 권장
center: [7,Yf,7] -> [9,Yt,9]
front:  [7,Yf,6] -> [9,Yt,7]
back:   [7,Yf,9] -> [9,Yt,10]
left:   [6,Yf,7] -> [7,Yt,9]
right:  [9,Yf,7] -> [10,Yt,9]
```

## GEM_SQUARE_RECESSED
```txt
용도: guard, pommel, tsuba, collar, staff head, reactor ring
필수: socket + gem_body
front/back가 보이는 무기는 front/back 쌍 필수
chest/back invisible arcane_core는 front detail 우선, back simple 허용
```

## CHANNEL_BLADE_CORE
```txt
용도: greatsword blade core, katana thin glow line optional
기본: front/back 쌍
주의: dagger는 texture line 우선, 큐브 core 과다 금지
```

## BLADE_BEVEL
```txt
용도: greatsword, katana edge depth
front/back + left/right 균형
작은 dagger에는 보통 texture로 처리
```

## GUARD_RAISE
```txt
용도: greatsword guard 중앙 볼록
katana tsuba에는 기본 적용 금지
front/back 쌍 필수
```

## WING_ANGEL
```txt
용도: greatsword holy/celestial wing only
Step1 = guard side base feather
Step5 = outer tip feather
Y_RISE default [0,2,4,6,8]
Wing Reach = sum(2D_W) - (step_count-1)×1 overlap
Z default 7->9
```

## SIDE_LUG_SPEAR
```txt
용도: spearhead 옆 보조날/장식
wing step system 사용 금지
collar 또는 head_body에 반드시 접촉
```

## SHAFT_BAND
```txt
용도: spear/staff shaft wrap
thin ring/band cube or texture band
반복 간격은 ref_spec 기준
```

## TSUBA_RING
```txt
용도: katana tsuba
얇고 작게 유지
blade보다 넓지만 greatsword guard처럼 과장 금지
```

## DAGGER_GUARD_COMPACT
```txt
용도: dagger small guard/collar
높이/폭 최소화, GUI readability 우선
```

## ORB_RING / REACTOR_RING
```txt
용도: orb/arcane_core
orb: spherical/circular frame
arcane_core: flat front disc ring, Z depth 4~6
```

---

## 금지
```txt
front-only gem/core/raise when back face visible
wing feature without wing route flag
greatsword feature forced onto katana/spear/dagger
floating decoration with no shared face/contact
```
