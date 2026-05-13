---
name: bb-art-review
description: Blockbench 무기 미감/가독성 검토. 기술 PASS 후 GUI/손持/실루엣/세트 언어를 평가한다.
version: v12.0
---

# bb-art-review

## 목표
좌표가 맞아도 멋없고 밋밋한 모델을 걸러낸다. 이 스킬은 기술 검증이 아니라 아트 디렉션 검증이다.

## 선행 조건
```txt
/bb-mcp-verify에서 HARD FAIL 없음
front / side / 45도 / gui / handheld screenshot 존재 권장
```

---

## 공통 검토
```txt
1. GUI 64px 축소에서 카테고리가 즉시 읽히는가?
2. 45도 뷰에서 핵심 장식이 보이는가?
3. 실루엣만 봐도 무기 종류가 드러나는가?
4. 색 대비가 충분한가?
5. 장식이 너무 난잡하거나 너무 빈약하지 않은가?
6. 텍스처 없이도 덩어리가 예쁜가?
7. 텍스처 적용 후 재질 차이가 살아나는가?
8. 같은 세트의 다른 무기와 시각 언어가 맞는가?
```

---

## 카테고리별 핵심

### greatsword
```txt
강한 blade/guard 비율, wing 있으면 날개 실루엣이 45도에서도 읽혀야 함.
```

### katana
```txt
얇고 길며 정제감 있어야 함. guard가 크면 실패.
```

### spear
```txt
shaft가 빈약하면 실패. head/collar/shaft 균형이 중요.
```

### dagger
```txt
작아도 blade/grip/pommel이 읽혀야 함. 미세장식보다 대비가 중요.
```

### twin_blades
```txt
한 자루 단독과 두 자루 세트감 모두 자연스러워야 함.
```

### guns
```txt
옆모습이 총처럼 읽히고 barrel/muzzle/grip이 분리되어야 함.
```

### orb / arcane_core
```txt
orb는 구형/부유 초점, arcane_core는 납작한 가슴 원판으로 보여야 함.
```

---

## 출력
```txt
Art Review: PASS / NEEDS PASS
Strong points: 2~4개
Weak points: 2~4개
Most valuable next improvement: 1개
Suggested edits: part_name 기준으로 구체적으로
```
