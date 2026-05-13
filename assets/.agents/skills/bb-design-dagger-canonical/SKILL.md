---
name: bb-design-dagger-canonical
description: Dagger 전용 design canonical. 짧고 굵은 GUI 가독성 중심.
version: v12.0
---

# Dagger Canonical

## 설계 철학
단검은 작은 크기에서 읽히는 굵은 실루엣과 강한 대비가 핵심이다. 과한 세그먼트와 복잡한 장식은 오히려 나쁘다.

## 범위
```txt
Category: dagger
Primary axis: Y
Total Y: 12~14px
Blade width 2D: 3~4px
Depth 2D: 3~4px
Cube budget: target 15~35 / hard ceiling 45
Texture: 256×256 per-face UV 또는 64×64 export 가능
Pivot: bottom of pommel
```

## 필수 파트
```txt
blade_group: blade_body, blade_tip
guard_or_collar_group: small_guard or collar
grip_group: grip_core
pommel_group: pommel_cap
```

## 옵션 feature
```txt
gem: pommel 또는 guard에 1개만 권장
edge_highlight: texture 우선
small_core: blade center 1px texture line
```

## 좌표 규칙
```txt
한눈에 blade/grip/pommel이 구분되어야 함.
큐브 수보다 silhouette readability 우선.
GUI 축소 뷰에서 카테고리가 읽혀야 함.
```

## 금지
```txt
Total Y=50 강제 금지
wing_group 금지
blade 4세그먼트 강제 금지
큐브 예산 45 초과 금지
미세 장식 과다 금지
```
