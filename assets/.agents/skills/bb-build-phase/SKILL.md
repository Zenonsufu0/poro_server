---
name: bb-build-phase
description: Blockbench MCP build entry. category_route.json을 읽고 카테고리별 build-phase로 자동 분기한다.
version: v12.0-router
---

# bb-build-phase — Build Router

## 역할
`design.md`와 `refs/category_route.json`을 읽고 실제 Blockbench 빌드 스킬을 자동 선택한다.

이 파일은 직접 대검/날개 Phase를 실행하지 않는다.

---

## 사전 조건

```txt
- design.md에 [TBD] 없음
- refs/category_route.json 존재
- /bb-pre-build-audit PASS
```

`category_route.json`이 없으면 `/bb-reference-router`를 먼저 실행한다.
`pre-build-audit`가 PASS되지 않았으면 STOP.

---

## 자동 분기

```txt
greatsword   -> /bb-build-phase-greatsword
katana       -> /bb-build-phase-katana
spear        -> /bb-build-phase-spear
dagger       -> /bb-build-phase-dagger
twin_blades  -> /bb-build-phase-twin-blades
pistol       -> /bb-design-ranged + ranged build rules
sniper       -> /bb-design-ranged + ranged build rules
shotgun      -> /bb-design-ranged + ranged build rules
staff        -> /bb-design-magic + magic build rules
orb          -> /bb-design-magic + magic build rules
arcane_core  -> /bb-design-magic + magic build rules
```

---

## 공통 Phase 원칙

모든 카테고리에서 다음 흐름은 유지한다.

```txt
Phase 0: project/texture/groups 초기화
Phase 1: main silhouette
Phase 2: structure/depth/anchors
Phase 3: detail cubes/features
Phase 4: per-face UV map
Phase 5: texture base + shading guide
Phase 5.5: category-specific post process (twin_blades mirror 등)
```

---

## MCP 검증 게이트

각 Phase 후:

```txt
1. MCP screenshot/cube query
2. /bb-mcp-verify with phase query set
3. HARD FAIL 없을 때만 다음 Phase
4. 시각 확인이 필요한 단계는 사용자 OK 필요
```

---

## 금지

- greatsword Phase를 katana/spear/dagger에 적용 금지
- wing/gem/core를 route.special_features 없이 강제 생성 금지
- p4 Z-depth profile 무시 금지
- 총기류를 Y축으로 세우는 것 금지
- arcane_core를 막대기/구체로 만드는 것 금지

---

## 출력

```txt
Build route: <category> -> <build_skill>
Current phase: <phase>
Next verification: /bb-mcp-verify <phase>
```
