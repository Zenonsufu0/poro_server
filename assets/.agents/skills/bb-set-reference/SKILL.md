---
name: bb-set-reference
description: Weapon set/category/palette reference. 최신 ref-memory v4.2 기준 p1~p4 4장 레퍼런스와 6색 팔레트 기준.
version: v12.0-ref-v4.2
---

# Weapon Set & Category Reference

## 최신 레퍼런스 기준

모든 새 레퍼런스는 p1/p2/p3/p4 4장 필수다.

```txt
p1: 3면도 + closeups + Wing Analysis if wing exists
p2: Cross Section + Thickness Guide + Color Palette + Texture Painting Guide
p3: Blockbench Notes + Scale Rules + Dimension Summary + Validation Checklist
p4: Z-Depth Profile + Detail Cube Library + Layer Structure
```

p4가 없으면 build 금지.

---

## 11 Category Sizes

| Category | Axis | Size | Key rule | Build skill |
|---|---:|---|---|---|
| greatsword | Y | 48~52 | wide blade + guard, wing optional | bb-build-phase-greatsword |
| katana | Y | 62~64 | thin long blade + tsuba + wrap | bb-build-phase-katana |
| spear | Y | 48~56 | shaft/head/collar | bb-build-phase-spear |
| dagger | Y | 12~14 | compact GUI readability | bb-build-phase-dagger |
| twin_blades | Y | 16~18 each | right master + left mirror | bb-build-phase-twin-blades |
| pistol | Z | 22~26 | horizontal gun, Z barrel | ranged rules |
| sniper | Z | 56~64 | long barrel/scope | ranged rules |
| shotgun | Z | 28~36 | thick body/barrel | ranged rules |
| staff | Y | 56~64 | shaft + head | magic rules |
| orb | center | diameter 16~20 | floating focus | magic rules |
| arcane_core | X/Y disc | diameter 16~20, Z 4~6 | flat chest reactor | magic rules |

---

## Fixed Palettes

```txt
Celestial       Primary #F2F2F2 / Secondary #E0B347 / Core_Glow #FFD54D / Gem #3DBBFF / Dark #0D234A / Neutral #808080
Arcane Shadow   Primary #1A1A2E / Secondary #6A0DAD / Core_Glow #BF5FFF / Gem #7B2FBE / Dark #0D0D1A / Neutral #3D3D5C
Verdant         Primary #F5F5F0 / Secondary #4CAF50 / Core_Glow #A5D6A7 / Gem #FFD700 / Dark #1B5E20 / Neutral #607D5B
Arc Reactor     Primary #B0BEC5 / Secondary #1565C0 / Core_Glow #29B6F6 / Gem #00E5FF / Dark #0D1B2A / Neutral #546E7A
Crimson Inferno Primary #1A0A00 / Secondary #C62828 / Core_Glow #FF6D00 / Gem #FF1744 / Dark #0D0000 / Neutral #4A2000
Glacial         Primary #E3F2FD / Secondary #90CAF9 / Core_Glow #B3E5FC / Gem #E1F5FE / Dark #1A237E / Neutral #78909C
Forge           Primary #616161 / Secondary #FF6F00 / Core_Glow #FFAB40 / Gem #FF8F00 / Dark #212121 / Neutral #757575
Lunar           Primary #0D1B4A / Secondary #E0B347 / Core_Glow #FFF9C4 / Gem #FFD700 / Dark #050D1F / Neutral #37474F
Heroic          Primary #F5F5F5 / Secondary #E0B347 / Core_Glow #FFF176 / Gem #42A5F5 / Dark #1A237E / Neutral #90A4AE
```

---

## Set language

### Celestial
```txt
White/gold/sapphire, divine/angelic, wing/star/halo motifs.
```

### Arcane Shadow
```txt
Dark metal, purple void, rune/eclipse motifs.
```

### Verdant
```txt
Ivory/green/gold, vine/leaf/forest motifs.
```

### Arc Reactor
```txt
Steel/arc blue, engineered reactor nodes, rails, conduits.
```

### Crimson Inferno
```txt
Black/red/orange, magma, oni, volcanic cracks.
```

### Glacial
```txt
White/silver/ice blue, crystal shards, frost edges.
```

### Forge
```txt
Dark steel/orange heat, industrial plates, rivets.
```

### Lunar
```txt
Navy/gold/moonlight, Japanese crescent motifs.
```

### Heroic
```txt
Silver/gold/blue gem, classic noble fantasy.
```

---

## Automation note

`/bb-design`는 이 파일을 직접 묻지 않고도 `/bb-reference-router`를 통해 set/category를 추정할 수 있다. 다만 팔레트 충돌이 있으면 이 파일의 hex를 기준으로 warning을 낸다.
