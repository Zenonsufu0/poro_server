---
name: bb-design-melee
description: 근접무기 설계 자동 오케스트레이터. category_route.json을 기준으로 greatsword/katana/spear/dagger/twin_blades 전용 canonical로 분기한다.
version: v12.0-category-aware
---

# bb-design-melee — Category-Aware Orchestrator

## 역할
`bb-design` 또는 사용자가 직접 호출했을 때, 근접무기 설계를 자동 완성한다.

이 스킬은 직접 greatsword 구조를 강제하지 않는다. 반드시 `refs/category_route.json`의 `canonical_skill`을 사용한다.

---

## 사전 확인

```txt
필수 파일:
- refs/category_route.json
- refs/p1.png 또는 refs/<variant>_p1.png
- refs/p2.png 또는 refs/<variant>_p2.png
- refs/p3.png 또는 refs/<variant>_p3.png
- refs/p4.png 또는 refs/<variant>_p4.png
```

`category_route.json`이 없으면 `/bb-reference-router`를 먼저 실행한다.

허용 category:

```txt
greatsword / katana / spear / dagger / twin_blades
```

다른 category면 STOP하고 `next_skill`로 넘긴다.

---

## 자동 실행 순서

```txt
1. /bb-design-melee-ref 실행
   refs/p1~p4 + category_route.json 분석
   → refs/ref_analysis.md + refs/ref_spec.json 저장

2. category_route.json.canonical_skill 로드
   greatsword -> /bb-design-greatsword-canonical
   katana -> /bb-design-katana-canonical
   spear -> /bb-design-spear-canonical
   dagger -> /bb-design-dagger-canonical
   twin_blades -> /bb-design-twin-blades-canonical

3. /bb-design-melee-spec 실행
   canonical + ref_spec.json 기반 design.md 작성

4. /bb-design-melee-verify 실행
   카테고리별 HARD FAIL 검사
```

---

## TodoWrite 사용 권장

```json
[
  {"id":"route-check","content":"category_route.json 확인 또는 bb-reference-router 실행","status":"in_progress","priority":"high"},
  {"id":"melee-ref","content":"bb-design-melee-ref: p1~p4 분석 → ref_analysis.md/ref_spec.json","status":"pending","priority":"high"},
  {"id":"canonical","content":"category별 canonical 로드","status":"pending","priority":"high"},
  {"id":"melee-spec","content":"bb-design-melee-spec: design.md 작성","status":"pending","priority":"high"},
  {"id":"melee-verify","content":"bb-design-melee-verify: 카테고리별 검증","status":"pending","priority":"high"}
]
```

---

## 실패 처리

```txt
route confidence < 0.45:
  STOP. p1/p3 category label 보강 요청.

melee-ref FAIL:
  누락된 p1~p4 섹션 보고 후 STOP.

melee-spec FAIL:
  ref_spec.json 오류 항목 수정 후 1회 재실행.

melee-verify HARD FAIL:
  design.md 수정 후 1회 재검증.
  2회 실패 시 FAIL 목록만 보고.
```

---

## 최종 보고

```txt
✅ melee design complete
Category: <category>
Canonical: <canonical_skill>
Total Length: <value>
Primary features: <features>
Generated: design.md, refs/ref_analysis.md, refs/ref_spec.json
Next: /bb-pre-build-audit
```

---

## 금지

- `greatsword`가 아닌데 wing_group/guard_shoulder를 필수로 요구하지 말 것.
- `dagger`에 Total Y=50을 강제하지 말 것.
- `katana`에 대검식 두꺼운 crossguard를 강제하지 말 것.
- `spear`에 blade_lower/mid/upper/tip 대검 분할을 강제하지 말 것.
- `twin_blades`를 한 파일에 두 자루 모두 강제로 넣지 말 것. 기본은 right master + mirror.
