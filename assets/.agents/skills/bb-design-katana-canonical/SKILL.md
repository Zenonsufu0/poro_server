---
name: bb-design-katana-canonical
description: Katana 전용 design canonical. 얇고 긴 칼날, 츠바, 손잡이 래핑 중심.
version: v12.0
---

# Katana Canonical

## 설계 철학
카타나는 대검처럼 두껍고 화려한 덩어리가 아니라, 길고 얇은 선형 실루엣과 정제감이 핵심이다.

## 범위
```txt
Category: katana
Primary axis: Y
Total Y: 62~64px
Blade width 2D: 3~4px
Blade depth 2D: 3~4px
Tsuba width 2D: 5~7px 권장
Grip width: 3~4px
Cube budget: target 55~120 / hard ceiling 150
Texture: 256×256 per-face UV
Pivot: bottom of handle/pommel
```

## 필수 파트
```txt
blade_group: blade_main, blade_tip, optional_blade_edge/highlight_line
tsuba_group: tsuba_center, optional_tsuba_ring
grip_group: grip_core, wrap_bands
pommel_group: end_cap
```

## 옵션 feature
```txt
hamon_line: texture guide로 표현 우선, 큐브 강제 아님
blade_core: 매우 얇은 glow line만 허용
gem: tsuba 또는 pommel에 작은 소켓만
sheath_hint: 장식으로만, 본체 아님
```

## 좌표 규칙
```txt
CENTER_X=8 / CENTER_Z=8
Blade는 가능한 1~2개의 긴 큐브 + tip으로 간결하게.
두꺼운 crossguard 금지.
Tsuba는 blade보다 넓지만 guard처럼 과장하지 않는다.
Grip wrap은 texture 또는 얇은 band cube로 표현.
```

## 금지
```txt
wing_group required 금지
guard_shoulder required 금지
greatsword식 blade_lower/mid/upper/tip 4분할 강제 금지
blade width 5px 이상 금지
guard_W > blade_W×2 금지
```
