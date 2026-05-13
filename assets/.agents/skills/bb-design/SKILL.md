---
name: bb-design
description: Entry-point design automation. 레퍼런스만 받아도 카테고리를 자동 판독하고 맞는 하위 스킬로 라우팅하여 design.md를 완성한다.
version: v12.0-auto-router
---

# bb-design — Auto Design Entry Point

## 역할
사용자가 `/bb-design`만 호출해도 다음을 자동으로 수행한다.

1. `refs/`의 p1~p4 레퍼런스를 찾는다.
2. `bb-reference-router`로 카테고리/세트/특수요소를 판독한다.
3. 판독 결과를 `refs/category_route.json`에 저장한다.
4. 카테고리에 맞는 설계 스킬을 후속 호출한다.
5. `design.md`의 `[TBD]`를 제거하고 빌드 가능한 상태로 만든다.

> 핵심 원칙: 사용자가 “이게 대검인지 단검인지” 직접 말하지 않아도, 레퍼런스가 충분하면 스킬이 먼저 판단한다.

---

## 필수 입력

허용 파일명 패턴:

```txt
refs/<variant>_p1.png 또는 refs/p1.png
refs/<variant>_p2.png 또는 refs/p2.png
refs/<variant>_p3.png 또는 refs/p3.png
refs/<variant>_p4.png 또는 refs/p4.png
```

p1~p4는 모두 필수다. p4가 없으면 STOP.

- p1: 3면도 + Wing Analysis / 주요 클로즈업
- p2: Cross Section + Thickness + Color Palette + Texture Guide
- p3: Blockbench Notes + Dimension Summary + Validation Checklist
- p4: Z-Depth Profile + Detail Cube Library + Layer Structure

---

## 자동 라우팅 절차

### Step 0 — 레퍼런스 존재 확인

```txt
p1/p2/p3/p4 4장 모두 확인.
누락 시: 누락 파일명만 보고하고 중단.
```

### Step 1 — 카테고리 판독

반드시 `/bb-reference-router`를 먼저 로드한다.

출력 파일:

```txt
refs/category_route.json
```

필수 필드:

```json
{
  "category": "greatsword|katana|spear|dagger|twin_blades|pistol|sniper|shotgun|staff|orb|arcane_core",
  "confidence": 0.0,
  "set": "Celestial|Arcane Shadow|Verdant|Arc Reactor|Crimson Inferno|Glacial|Forge|Lunar|Heroic|unknown",
  "special_features": ["wing", "gem", "blade_core", "reactor", "scope", "tassel"],
  "primary_axis": "Y|Z|disc|orb",
  "evidence": ["p1 front view total length ≈ 50", "p1 has wing analysis", "p3 category label says greatsword"],
  "next_skill": "bb-design-melee|bb-design-ranged|bb-design-magic"
}
```

### Step 2 — 하위 설계 오케스트레이터 호출

```txt
greatsword / katana / spear / dagger / twin_blades -> /bb-design-melee
pistol / sniper / shotgun                         -> /bb-design-ranged
staff / orb / arcane_core                         -> /bb-design-magic
```

하위 스킬은 반드시 `refs/category_route.json`을 기준으로 동작한다.

---

## 판단 우선순위

카테고리 판독 시 다음 순서로 신뢰한다.

1. p3 Blockbench Notes의 `Category:` 텍스트
2. p1 좌측 상단 헤더 라벨의 Category
3. p3 Dimension Summary의 Total Length / Primary Axis
4. p1 실루엣과 3면도
5. 파일명/폴더명
6. `design.md §8.0` 기존 값

충돌 시:

```txt
p3/p1 라벨과 실루엣이 다르면 실루엣을 우선하되, route.json에 conflict 기록.
confidence < 0.65면 best_guess로 진행하고 report에 경고.
confidence < 0.45면 STOP하고 사용자에게 p1/p3 라벨 보강 요청.
```

---

## 공통 설계 규칙

- 공용 스킬은 절대 대검/날개 구조를 기본값으로 강제하지 않는다.
- Wing, gem, blade_core, guard_raise, reactor, scope 등은 모두 feature-library 옵션이다.
- p1~p4 4장 레퍼런스가 최신 기준이다. 3장 기준 문구는 사용하지 않는다.
- `ref_spec.json` 또는 `refs/ref_analysis.md`가 있으면 수치 기준으로 사용하되, 레퍼런스와 충돌하면 `category_route.json`에 기록한다.
- 이미지에서 읽은 수치보다 p3 Dimension Summary / p4 Z-Depth Profile의 명시 수치를 우선한다.

---

## 최종 출력 형식

```txt
✅ bb-design complete
Detected category: <category> (confidence <0.xx>)
Set: <set>
Special features: <features>
Route: bb-design -> bb-reference-router -> <next_skill> -> <category canonical>
Generated: refs/category_route.json, refs/ref_analysis.md, design.md
Next: /bb-pre-build-audit
```

---

## Self-check

- [ ] p1~p4 4장 확인
- [ ] bb-reference-router 선실행
- [ ] category_route.json 생성
- [ ] category_route.json의 next_skill로 후속 호출
- [ ] 공용 단계에서 greatsword/wing 전제 금지
- [ ] design.md에 `[TBD]` 없음
- [ ] p4 Z-Depth/Profile 반영
- [ ] 후속 단계 안내
