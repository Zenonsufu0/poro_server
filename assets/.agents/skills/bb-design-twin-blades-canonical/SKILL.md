---
name: bb-design-twin-blades-canonical
description: Twin blades 전용 design canonical. 오른손 master 한 자루를 만들고 Phase 5.5에서 왼손 mirror를 생성한다.
version: v12.0
---

# Twin Blades Canonical

## 설계 철학
쌍검은 한 자루의 완성도와 두 자루의 세트감을 동시에 만족해야 한다. 기본은 right master 1개를 만든 뒤 left mirror를 생성한다.

## 범위
```txt
Category: twin_blades
Primary axis: Y
One blade Total Y: 16~18px
Width/Depth: 4×4px 권장
Cube budget: target 25~50 per blade / hard ceiling 65 per blade
Texture: 256×256 per-face UV
Pivot: bottom of grip
Output: <variant>_right.bbmodel + <variant>_left.bbmodel
```

## 필수 파트
```txt
blade_group: blade_body, blade_tip
guard_or_collar_group: compact_guard
grip_group: grip_core
pommel_group: pommel_cap
mirror_plan: Phase 5.5 left-hand mirror
```

## 옵션 feature
```txt
paired_accent: 한쪽/양쪽 동일 포인트 장식
small_gem: guard or pommel
edge_core: texture line
```

## 좌표 규칙
```txt
Right master를 먼저 완성한다.
Left는 X mirror로 생성한다.
Y/Z 좌표는 동일, X 좌표만 mirrored.
두 자루 모두 같은 texture를 공유할 수 있게 UV 정리.
```

## 금지
```txt
두 자루를 한 bbmodel 안에 강제 배치 금지
대검식 guard/wing 금지
한 자루가 18px 초과 금지
Phase 5.5 mirror 계획 누락 금지
```
