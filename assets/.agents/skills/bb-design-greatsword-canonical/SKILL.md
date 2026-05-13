---
name: bb-design-greatsword-canonical
description: Greatsword 전용 design canonical. holy_wing 포함 대검형 무기 설계 기준.
version: v12.0
---

# Greatsword Canonical

## 설계 철학
대검은 넓은 blade, 뚜렷한 guard, 강한 실루엣이 핵심이다. Wing/Blade Core/Gem은 옵션 feature다.

## 범위
```txt
Category: greatsword
Primary axis: Y
Total Y: 48~52px
Blade width 2D: 5~8px
Guard width 2D: blade_W보다 크고 blade_W×1.5 이하
Grip width: CROSS_COLUMN 4px 권장
Cube budget: target 90~180 / hard ceiling 250
Texture: 256×256 per-face UV
Pivot: bottom of pommel
```

## 필수 파트
```txt
blade_group: blade_lower, blade_mid, blade_upper, blade_tip
guard_group: guard_center
grip_group: grip_cross_column
pommel_group: pommel_cross_column or pommel_block
```

## 옵션 feature
```txt
wing: wing_r_group + wing_l_group, step1~5, Wing Reach 검산 필수
blade_core: front/back 쌍
bevel: mid/upper edge bevel 권장
guard_raise: front/back 쌍
socket_gem: socket + body, front/back 쌍
pommel_ring: detail slab
```

## 좌표 규칙
```txt
CENTER_X=8 / CENTER_Z=8
BB_X = round(2D_X × 0.75 × 2) / 2
BB_D = 2D_D × 0.5
from.z = 8 - BB_D/2
to.z = 8 + BB_D/2
blade_upper W = blade_mid W
blade_tip may taper to W=2
```

## Wing 규칙
```txt
Wing은 route.special_features에 wing이 있을 때만 required.
Step1 = guard 옆 base feather
Step5 = outer tip feather
Wing Reach = sum(2D_W) - (step_count-1)×1px overlap
BB Wing Reach = 2D Wing Reach ×0.5
Angel/Holy sweep: Y_RISE [0,2,4,6,8]
step1 floor: max(guard.from.y, GCY-floor(H1/2))
```

## 금지
```txt
wing 없는 대검에 wing_group required 금지
front gem만 배치 금지 — front/back 쌍
세그먼트별 Z-depth 무시 금지
```
