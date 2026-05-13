---
name: bb-design-melee-verify
version: v12.0-category-aware
description: 근접무기 design.md 카테고리별 검증. HARD FAIL이면 spec 재실행.
---

# bb-design-melee-verify

## 공통 HARD FAIL

```txt
[ ] design.md에 [TBD] 존재
[ ] category_route.json.category와 design.md Category 불일치
[ ] p1~p4 중 하나 누락
[ ] p4 Z-Depth Profile이 §5/§8.2에 반영되지 않음
[ ] 팔레트 6색 미만 또는 Neutral 없음
[ ] Primary axis 누락
[ ] cube budget 누락
[ ] texture strategy 누락
[ ] physical attachment graph 누락
[ ] build skill 누락
```

---

## category별 HARD FAIL

### greatsword
```txt
[ ] Total Y가 48~52 밖
[ ] blade segment tip/upper/mid/lower 누락
[ ] guard_W <= blade_W
[ ] guard_W > blade_W × 1.5
[ ] wing feature가 있는데 wing_steps/wing_reach 검산 없음
[ ] wing feature가 없는데 wing_group을 required로 강제
```

### katana
```txt
[ ] Total Y가 62~64 밖
[ ] blade가 guard/grip보다 충분히 길지 않음
[ ] tsuba 누락
[ ] blade 폭이 5px 이상으로 대검처럼 보임
[ ] wing_group이 required로 들어감
```

### spear
```txt
[ ] Total Y가 48~56 밖
[ ] shaft 누락
[ ] spearhead 누락
[ ] shaft 비중이 전체의 45% 미만
[ ] 대검식 blade_lower/mid/upper/tip 구조를 필수로 사용
```

### dagger
```txt
[ ] Total Y가 12~14 밖
[ ] blade/grip/pommel 구분 누락
[ ] 과도한 세그먼트로 큐브 예산 40 초과
[ ] GUI readability note 누락
```

### twin_blades
```txt
[ ] one blade Total Y가 16~18 밖
[ ] right master / left mirror 계획 누락
[ ] Phase 5.5 mirror 명시 없음
[ ] pair consistency 체크 누락
```

---

## WARN

```txt
[ ] ref_spec.json에 estimated 수치가 많음
[ ] palette_conflict 존재
[ ] art readability pass 누락
[ ] category confidence < 0.65
```

---

## 판정

```txt
HARD FAIL → bb-design-melee-spec 재실행
WARN만 → PASS, 다음 /bb-pre-build-audit
```

---

## 출력

```txt
PASS/FAIL
Category: <category>
Hard fails: <list>
Warnings: <list>
Next: /bb-pre-build-audit 또는 fix design.md
```
