---
name: bb-design-ranged
description: Ranged weapon design. bb-reference-router 결과를 받아 pistol/sniper/shotgun Z-axis 설계를 완성한다.
version: v12.0-router-compatible
---

# bb-design-ranged

## 핵심
총기류는 반드시 Z축이 길이/총구 방향이다. 세로로 세우면 HARD FAIL.

## 입력
```txt
refs/category_route.json
refs/p1~p4
```

## category
```txt
pistol: Z 22~26, Y 14~16, X 4~5
sniper: Z 56~64, Y 6~10, X 5~9
shotgun: Z 28~36, Y 14~20, X 6~20
```

## 필수 파트
```txt
barrel_group
frame_group
grip_group
trigger_group
sight_or_scope_group optional
muzzle_or_emitter optional
```

## design.md 작성 규칙
```txt
- Primary axis: Z
- Z=0 stock/grip side, Z increases toward muzzle
- Barrel/frame must be longer on Z than Y
- p4 Z-depth/layer profile 반영
- texture guide includes barrel highlight, muzzle glow, grip dark wrap
```

## 금지
```txt
Y축 총기 금지
arcane_core/orb와 혼동 금지
barrel 없는 총기 금지
```
