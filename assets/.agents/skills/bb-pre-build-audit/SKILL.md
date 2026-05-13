---
name: bb-pre-build-audit
description: design.md 빌드 전 자동 검증. 공용 검사 후 카테고리별 검사로 분기한다.
version: v12.0-category-aware
---

# Pre-Build Audit

## 입력
```txt
design.md
refs/category_route.json
refs/ref_spec.json 권장
```

---

## 공통 HARD FAIL
```txt
[ ] design.md missing
[ ] [TBD] remains
[ ] category_route.json missing
[ ] category mismatch: route vs design.md
[ ] p1~p4 references missing
[ ] p4 Z-depth not reflected in design.md §5/§8.2
[ ] palette has fewer than 6 roles
[ ] Neutral color missing
[ ] texture strategy missing or not square PNG
[ ] primary axis missing
[ ] cube budget missing
[ ] build skill missing
[ ] physical attachment graph missing
```

---

## category별 HARD FAIL

### greatsword
```txt
[ ] Total Y not 48~52
[ ] blade_lower/mid/upper/tip missing
[ ] guard_W <= blade_W
[ ] guard_W > blade_W × 1.5
[ ] wing feature present but wing reach/steps missing
[ ] wing feature absent but wing required
[ ] front/back pair missing for gem/core/raise/bevel if used
```

### katana
```txt
[ ] Total Y not 62~64
[ ] blade width > 4.5 unless explicitly justified
[ ] tsuba missing
[ ] grip wrap/texture plan missing
[ ] wing_group required
[ ] greatsword guard_shoulder required
```

### spear
```txt
[ ] Total Y not 48~56
[ ] shaft missing
[ ] shaft length < 45% total
[ ] spearhead missing
[ ] collar connection missing
[ ] greatsword blade segment structure required
```

### dagger
```txt
[ ] Total Y not 12~14
[ ] blade/grip/pommel not separated
[ ] cube ceiling > 45 without reason
[ ] GUI readability check missing
[ ] wing/greatsword structure present
```

### twin_blades
```txt
[ ] one blade Total Y not 16~18
[ ] right master not specified
[ ] Phase 5.5 mirror not specified
[ ] mirror audit not specified
```

---

## WARN
```txt
[ ] route confidence < 0.65
[ ] many estimated values in ref_spec.json
[ ] palette conflict
[ ] art review not yet run
[ ] texture guide too generic
```

---

## 판정
```txt
Any HARD FAIL -> STOP. Do not build.
WARN only -> PASS. Run /bb-build-phase.
```

---

## 출력
```txt
Pre-Build Audit: PASS/FAIL
Category: <category>
Build skill: <skill>
Hard fails: ...
Warnings: ...
Next: /bb-build-phase
```
