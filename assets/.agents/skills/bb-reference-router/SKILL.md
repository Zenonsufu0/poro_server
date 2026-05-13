---
name: bb-reference-router
description: refs/p1~p4 레퍼런스에서 무기 카테고리, 세트, 특수요소, 후속 스킬을 자동 판독한다.
version: v12.0
---

# bb-reference-router — Reference Classifier

## 목표
사용자가 카테고리를 명시하지 않아도 p1~p4 레퍼런스를 보고 가장 적절한 무기 카테고리와 후속 스킬을 결정한다.

출력:

```txt
refs/category_route.json
```

---

## 입력

반드시 4장을 모두 읽는다.

```txt
p1: front/side/back + closeups + wing analysis
p2: cross section + thickness + palette + texture guide
p3: blockbench notes + dimension summary + validation checklist
p4: z-depth profile + cube library + layer structure
```

이미지 안의 텍스트, 헤더, Dimension Summary, Validation Checklist를 우선 읽는다.

---

## 카테고리 판독 규칙

### 1. Melee — Y축 무기

#### greatsword
```txt
Y length: 48~52px 근처
Blade가 넓고 guard가 뚜렷함
Blade tip/upper/mid/lower 분할 가능
wing analysis가 있으면 greatsword 가능성 상승
손잡이보다 blade 비중이 큼
```

#### katana
```txt
Y length: 62~64px 근처
Blade가 매우 길고 얇음
Tsuba가 작고 얇음
Grip wrap이 중요하게 표시됨
두꺼운 crossguard/wing이 기본이면 katana 아님
```

#### spear
```txt
Y length: 48~56px 근처
Shaft가 전체 길이의 큰 비중
Spearhead/collar/butt 구조
Blade보다 shaft가 중심
p1 side view에서 길고 가느다란 장병기 형태
```

#### dagger
```txt
Y length: 12~14px 근처
짧고 압축적
Blade와 grip 비율이 1:1~2:1 사이
GUI 가독성/굵은 실루엣 강조
```

#### twin_blades
```txt
한 자루 Y length: 16~18px 근처
paired/right-left/mirror 표시
한 자루 기준 설계 + Phase 5.5 mirror 언급
```

---

### 2. Ranged — Z축 총기

#### pistol
```txt
Z length: 22~26px
Y height: 14~16px
Frame/barrel/grip/trigger 구조
옆에서 볼 때 가로로 긴 총기
```

#### sniper
```txt
Z length: 56~64px
길고 낮은 실루엣
Scope/barrel/stock 명확
```

#### shotgun
```txt
Z length: 28~36px
두꺼운 body/barrel
Y height 14~20px
```

---

### 3. Magic

#### staff
```txt
Y length: 56~64px
shaft + head 구조
head 12~16px
```

#### orb
```txt
지름 16~20px 구형/원형
hand-floating/focus 언급
막대기/총기 구조 없음
```

#### arcane_core
```txt
정면 원형 납작 원판
X/Y diameter 16~20px
Z depth 4~6px
chest-mounted/reactor/core/ring 언급
막대기나 구체가 아님
```

---

## 세트 판독 규칙

p2 Color Palette에서 다음 고정 팔레트와 매칭한다.

```txt
Celestial:       #F2F2F2 #E0B347 #FFD54D #3DBBFF #0D234A #808080
Arcane Shadow:   #1A1A2E #6A0DAD #BF5FFF #7B2FBE #0D0D1A #3D3D5C
Verdant:         #F5F5F0 #4CAF50 #A5D6A7 #FFD700 #1B5E20 #607D5B
Arc Reactor:     #B0BEC5 #1565C0 #29B6F6 #00E5FF #0D1B2A #546E7A
Crimson Inferno: #1A0A00 #C62828 #FF6D00 #FF1744 #0D0000 #4A2000
Glacial:         #E3F2FD #90CAF9 #B3E5FC #E1F5FE #1A237E #78909C
Forge:           #616161 #FF6F00 #FFAB40 #FF8F00 #212121 #757575
Lunar:           #0D1B4A #E0B347 #FFF9C4 #FFD700 #050D1F #37474F
Heroic:          #F5F5F5 #E0B347 #FFF176 #42A5F5 #1A237E #90A4AE
```

완전 일치가 아니면 가장 가까운 세트를 적고 `palette_conflict`에 기록.

---

## 특수요소 판독

다음 키워드/시각요소를 `special_features`에 넣는다.

```txt
wing: Wing Analysis, wing step, feather, sweep, celestial/holy wing shape
bat_wing: downward sweep, shadow/bat wing
crystal_wing: upward crystal shards
blade_core: central glowing channel, core slab, blade_core_slab
bevel: blade bevel, edge slab
shield_guard: large crossguard, guard_raise
socket_gem: gem socket/body pair
scope: sniper scope
barrel_emitter: muzzle/emitter
reactor_ring: arcane core ring/core
orb_frame: orb outer/inner ring
shaft_wrap: staff/spear shaft bands
```

---

## 출력 JSON 형식

`refs/category_route.json`:

```json
{
  "category": "greatsword",
  "confidence": 0.92,
  "set": "Celestial",
  "primary_axis": "Y",
  "special_features": ["wing", "blade_core", "socket_gem", "bevel"],
  "evidence": [
    "p3 Category label: greatsword",
    "p1 Total Length ≈ 50px",
    "p1 includes Wing Step Diagram",
    "p2 palette matches Celestial"
  ],
  "conflicts": [],
  "next_skill": "bb-design-melee",
  "canonical_skill": "bb-design-greatsword-canonical",
  "build_skill": "bb-build-phase-greatsword"
}
```

---

## 신뢰도 기준

```txt
0.85~1.00: 자동 진행
0.65~0.84: 자동 진행 + warning 기록
0.45~0.64: best_guess로 design까지만 진행, build 전 사용자 확인 필요
0.00~0.44: STOP, p1/p3 라벨 보강 요청
```

자동화 우선 원칙:

```txt
0.45 이상이면 질문하지 말고 best_guess로 진행한다.
단, build/파일 편집처럼 되돌리기 어려운 단계 전에 확인 게이트를 둔다.
```

---

## Self-check

- [ ] p1~p4 모두 읽음
- [ ] category 하나 선택
- [ ] confidence 수치화
- [ ] evidence 3개 이상
- [ ] set 판독
- [ ] special_features 판독
- [ ] next_skill/canonical_skill/build_skill 지정
- [ ] refs/category_route.json 저장
