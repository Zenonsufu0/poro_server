---
name: bb-design-melee-canonical
version: v12.0-dispatcher
description: Deprecated direct canonical. 이제 근접무기 공용 dispatcher로만 사용한다.
---

# bb-design-melee-canonical — Dispatcher Only

## 중요
이 스킬은 더 이상 직접 설계 템플릿을 제공하지 않는다.
기존 v11.5는 holy_wing/greatsword 전제가 강해서 katana/spear/dagger/twin_blades에 오염을 일으켰다.

반드시 `refs/category_route.json`을 읽고 아래 전용 canonical 중 하나를 로드한다.

```txt
greatsword   -> /bb-design-greatsword-canonical
katana       -> /bb-design-katana-canonical
spear        -> /bb-design-spear-canonical
dagger       -> /bb-design-dagger-canonical
twin_blades  -> /bb-design-twin-blades-canonical
```

## 금지

- Total Y=50 고정 금지
- wing_group 필수 요구 금지
- guard_shoulder 필수 요구 금지
- blade_lower/mid/upper/tip을 모든 melee에 강제 금지
- cube budget min=80을 모든 melee에 강제 금지

## 출력

전용 canonical 이름만 보고하고 즉시 해당 스킬로 진행한다.
