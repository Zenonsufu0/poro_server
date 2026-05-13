---
name: bb-cube-detail-pass
description: 텍스처가 아니라 실제 cube form 디테일을 강화하는 Blockbench 무기 리워크 스킬. 가드/날개/보석/사면/링/래핑을 큐브 레이어로 살린다.
version: v13.0
---

# bb-cube-detail-pass

## 목적
현재 모델이 평면적이거나 단순한 blockout처럼 보일 때, 텍스처 이전에 cube geometry 자체를 강화한다.

이 스킬은 **텍스처 패스가 아니다.**
큐브 수, 레이어, 깊이, 단차, 실루엣을 수정한다.

---

## 선행 조건

```txt
/bb-silhouette-pass PASS 또는 MINOR REWORK 이하
refs/ref_spec.json
refs/art_spec.json
source/*.bbmodel
```

`/bb-silhouette-pass`가 MAJOR REWORK 또는 REBUILD이면 이 스킬을 실행하지 말고 큰 덩어리부터 고친다.

---

## 공통 원칙

```txt
1. 큰 형태를 먼저 살리고, 작은 장식은 그 다음이다.
2. 디테일 큐브는 읽혀야 한다. 보이지 않는 tiny cube 남발 금지.
3. 전면/후면/측면 모두에서 레이어가 보여야 한다.
4. 같은 색 덩어리가 하나로 뭉치면 z-offset, bevel, ring, socket으로 분리한다.
5. 텍스처로 때울 수 없는 형태 문제는 큐브로 해결한다.
```

---

## Detail Budget

`refs/art_spec.json.detail_budget`을 읽는다.

없으면 기본값:

```txt
simple: 35~70 cubes
standard: 70~120 cubes
heroic: 90~180 cubes
legendary: 140~260 cubes
```

`heroic` 이상인데 전체 큐브 수가 60 미만이고 모델이 밋밋하면 **UNDER-DETAILED FAIL**.

---

## greatsword + holy wing 필수 큐브 디테일

### 1. Blade form detail

```txt
- blade_tip: point shaping cube/cut, 막대기 끝 금지
- blade_upper/mid/lower: width rhythm visible
- blade_core_slab: front/back center glow slab 또는 shallow channel
- blade_bevel: 좌우 외곽 1-cube-deep bevel detail
- lower blade near guard: guard 쪽으로 힘이 모이는 형태
```

### 2. Guard layered bulge

```txt
- guard_core: main gold body
- guard_ring_top: raised gold ring layer
- gem_socket: dark/secondary border layer
- gem_body: front readable gem, minimum 2x2 visual
- guard_shoulder L/R: wing으로 넘어가는 2-step stair
- wing_attach: gold/neutral connector, wing과 guard 사이 접합부
```

Guard가 금색 한 덩어리처럼 보이면 FAIL.

### 3. Wing feather detail

```txt
- wing_step1~5 각각 따로 읽히게 분리
- 각 step마다 height/rise 차이를 geometry로 표현
- outer tip shaping: alpha가 아니라 cube silhouette로 우선 표현
- step overlap은 1px 정도로 읽히게 배치
- left/right mirror 유지
```

날개가 흰 계단 3개처럼 보이면 FAIL.

### 4. Grip detail

```txt
- CROSS_COLUMN 유지
- diagonal wrap은 텍스처만이 아니라 얇은 band cube 또는 face offset으로 암시 가능
- handle_top은 guard depth와 자연스럽게 연결
```

### 5. Pommel detail

```txt
- pommel_core
- pommel_ring
- pommel_gem_socket
- pommel_gem_body
- round/octagonal read using step cubes
```

폼멜이 단순 사각 금색 블록이면 FAIL.

---

## 카테고리별 최소 디테일 구조

### katana

```txt
blade polish ridge, thin spine, tsuba layered plate, grip wrap bands, end cap.
큰 가드/날개식 장식 금지.
```

### spear

```txt
spearhead ridge, collar rings, shaft bands, butt cap, optional tassel connector.
shaft가 단순 막대 하나면 FAIL.
```

### dagger

```txt
bold blade bevel, handle separation, pommel cap, readable guard nub.
작아도 silhouette와 부품 분리가 보여야 함.
```

### twin_blades

```txt
one master blade detail, mirrored copy consistency, pair spacing, dual accent balance.
```

---

## 리워크 명령 방식

수정할 때 반드시 part 이름으로 지시한다.

```txt
Add / adjust:
- blade_bevel_l_front
- blade_bevel_r_front
- blade_core_slab_front
- blade_core_slab_back
- guard_ring_top_front
- guard_ring_top_back
- gem_socket_front
- gem_body_front
- wing_r_step1~5
- wing_l_step1~5
- pommel_ring_front
- pommel_gem_body_front
```

이름 없는 큐브를 많이 만들지 않는다.

---

## 검증 기준

```txt
Cube Detail Review: PASS / NEEDS REWORK / UNDER-DETAILED FAIL

Check:
- silhouette detail improved without breaking total_y/pivot/center_z
- focal point no longer reads as blob
- wing step count visible
- blade no longer reads as pole
- pommel no longer reads as square block
- detail cubes named and grouped
- no floating cubes
- front/back pairs where required
```

---

## 출력 형식

```txt
Cube Detail Pass: PASS / NEEDS REWORK / UNDER-DETAILED FAIL
Cube count: current / target range
Main cube-detail weaknesses:
1.
2.
3.
Required cube edits:
- group/part: action
Next skill: /bb-mcp-verify then /bb-art-review
```
