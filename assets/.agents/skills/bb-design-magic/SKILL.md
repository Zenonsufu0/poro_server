---
name: bb-design-magic
description: Magic weapon design. staff/orb/arcane_core를 category_route.json 기준으로 설계한다.
version: v12.0-router-compatible
---

# bb-design-magic

## 입력
```txt
refs/category_route.json
refs/p1~p4
```

## category
```txt
staff: Y 56~64, shaft 3~4, head 12~16
orb: diameter 16~20, depth 6~10, hand-floating focus
arcane_core: diameter 16~20, depth 4~6, flat chest reactor
```

## staff 필수
```txt
shaft_group
head_group
grip/butt_group
crystal/core optional
```

## orb 필수
```txt
outer_frame
inner_frame
core
floating display hook
```

## arcane_core 필수
```txt
outer_ring
inner_ring
central_core
front detail heavy
back simple
chest_display hook
flat disc Z depth <=6
```

## 금지
```txt
arcane_core를 막대기/구체로 만들기 금지
orb에 shaft/barrel 넣기 금지
staff를 총기처럼 Z축으로 만들기 금지
```
