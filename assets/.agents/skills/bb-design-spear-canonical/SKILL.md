---
name: bb-design-spear-canonical
description: Spear 전용 design canonical. Shaft 중심 장병기 구조.
version: v12.0
---

# Spear Canonical

## 설계 철학
창은 blade보다 shaft의 균형과 spearhead/collar 연결부가 핵심이다. 대검식 넓은 blade 구조를 쓰지 않는다.

## 범위
```txt
Category: spear
Primary axis: Y
Total Y: 48~56px
Shaft width 2D: 2~4px
Spearhead width 2D: 8~11px
Depth 2D: 2~4px
Cube budget: target 45~100 / hard ceiling 130
Texture: 256×256 per-face UV
Pivot: bottom of butt cap
```

## 필수 파트
```txt
head_group: spear_tip, spearhead_body
collar_group: collar_center, optional_side_lugs
shaft_group: shaft_core, shaft_bands
butt_group: butt_cap
```

## 옵션 feature
```txt
tassel: collar 아래 장식
side_lug: spearhead 옆 날개형 보조날, wing이 아님
shaft_wrap: band/ring 반복
core_gem: collar 또는 spearhead 중앙
```

## 좌표 규칙
```txt
Shaft는 전체 길이의 45% 이상.
Head는 위쪽에 집중하고 shaft와 collar가 반드시 접촉.
Side lug가 있어도 wing_step1~5 구조 사용 금지.
```

## 금지
```txt
blade_lower/mid/upper/tip 대검 구조 필수화 금지
guard/pommel 중심 설계 금지
wing analysis 필수 요구 금지
shaft 없이 spearhead만 있는 구조 금지
```
