# sword_adventurer_poro_01 — 매핑 플랜

> 분석일: 2026-05-08
> 원본: adventurer_tools_v8.zip (crossbow_adventurer_poro_01/original/ 공유)
> 원본 소스 모델: adventurer_tools_resourcepack/assets/minecraft/models/item/akaleaf/adventurer_tools/adventurer_sword.json
> 추출 기준: adventurer_tools_resourcepack 직접 경로

---

## 1. 원본 파일 목록

```
adventurer_tools_resourcepack/assets/minecraft/
├── models/item/akaleaf/adventurer_tools/
│   └── adventurer_sword.json     ← 5 elements, gui_light: front
└── textures/item/akaleaf/adventurer_tools/
    └── adventurer_sword.png      ← 461 bytes, 정적
```

---

## 2. 원본 구조 분석

### 2-1. 원본 텍스처 경로

```json
"textures": {
  "1": "item/akaleaf/adventurer_tools/adventurer_sword",
  "particle": "item/akaleaf/adventurer_tools/adventurer_sword"
}
```

- vanilla 경로 (minecraft: namespace 내부, items/ prefix 없음)
- **Poro namespace로 변경 필요**
- `mcmodels` 키 포함 → 제거 필요

### 2-2. 텍스처

- 정적 PNG (461 bytes)
- mcmeta 없음 → 애니메이션 없음

### 2-3. 원본 CMD

- Oraxen/ItemsAdder 기반 배포 팩 (자체 CMD 시스템)
- Poro 배정 CMD: `100102`

---

## 3. 파일 매핑

| 원본 파일 | 목표 경로 | 수정 사항 |
|---|---|---|
| `adventurer_sword.json` | `assets/poro/models/item/weapons/sword_adventurer_poro_01.json` | 텍스처 경로 + mcmodels 제거 |
| `adventurer_sword.png` | `assets/poro/textures/item/weapons/sword_adventurer_poro_01.png` | 파일명 변경만 |
| `assets/minecraft/items/netherite_sword.json` | 기존 파일 수정 | 100101 보존 + 100102 case 추가 |

---

## 4. 포로 서버 export 경로

```
assets/export/resourcepack/assets/
├── poro/
│   ├── models/item/weapons/
│   │   └── sword_adventurer_poro_01.json
│   └── textures/item/weapons/
│       └── sword_adventurer_poro_01.png
└── minecraft/
    └── items/
        └── netherite_sword.json    ← 100101 보존 + 100102 추가
```

---

## 5. namespace/path 수정 사항

### 5-1. 모델 JSON 텍스처 경로

```json
// 변경 전
"textures": {
  "1": "item/akaleaf/adventurer_tools/adventurer_sword",
  "particle": "item/akaleaf/adventurer_tools/adventurer_sword"
}

// 변경 후
"textures": {
  "1": "poro:item/weapons/sword_adventurer_poro_01",
  "particle": "poro:item/weapons/sword_adventurer_poro_01"
}
```

### 5-2. mcmodels 키 제거

```json
// 제거 대상
"mcmodels": "f2613f95f991e30961ad99a91599986b"
```

### 5-3. netherite_sword.json (100101 보존 + 100102 추가)

```json
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [
      {
        "when": "100101",
        "model": { "type": "minecraft:model", "model": "poro:item/weapons/sword_vanquisher_poro_01" }
      },
      {
        "when": "100102",
        "model": { "type": "minecraft:model", "model": "poro:item/weapons/sword_adventurer_poro_01" }
      }
    ],
    "fallback": { "type": "minecraft:model", "model": "minecraft:item/netherite_sword" }
  }
}
```

---

## 6. CustomModelData 등록

| 항목 | 값 |
|---|---|
| **Poro 배정 CMD** | **100102** |
| material | NETHERITE_SWORD |
| namespace | poro |
| model_path | `poro:item/weapons/sword_adventurer_poro_01` |
| texture_path | `poro:item/weapons/sword_adventurer_poro_01` |

---

## 7. display 설정 (원본 값)

| 뷰 | rotation | translation | scale |
|---|---|---|---|
| firstperson_righthand | [20, -90, 0] | [1.25, 1.25, -0.5] | [0.6, 0.6, 0.6] |
| firstperson_lefthand | [20, 90, 0] | [1.25, 1.25, -0.5] | [0.6, 0.6, 0.6] |
| thirdperson_righthand | [-10, -90, 0] | [0, 3, 0.25] | [0.6, 0.6, 0.6] |
| thirdperson_lefthand | [-10, 90, 0] | [0, 3, 0.25] | [0.6, 0.6, 0.6] |
| gui | [0, 0, -45] | [-0.5, -0.65, 0] | [0.62, 0.62, 0.62] |
| ground | [0, 0, -45] | [0, 3, 0] | [0.35, 0.35, 0.35] |
| head | [0, 0, 45] | [9.75, 7.5, 0.75] | [0.6, 0.6, 0.6] |
| fixed | [0, -180, -45] | [0.5, -0.5, 0.75] | [0.5, 0.5, 0.5] |

---

## 8. 작업 체크리스트

- [x] 원본 zip 추출 및 분석 완료
- [x] mapping_plan.md 작성 완료
- [x] `source/` 폴더에 수정용 복사본 준비 (`sword_adventurer_poro_01.json`)
- [x] 텍스처 `textures/` 배치 (`sword_adventurer_poro_01.png`)
- [x] export/resourcepack 배치 (model json + texture)
- [x] `assets/minecraft/items/netherite_sword.json` 100102 case 추가 (100101 보존)
- [x] `weapon_cosmetic_registry.yml` 등록 (status: export_ready)
- [x] 인게임 테스트 통과: `/minecraft:give @s minecraft:netherite_sword[minecraft:custom_model_data={strings:["100102"]}] 1`
- [x] status → active
