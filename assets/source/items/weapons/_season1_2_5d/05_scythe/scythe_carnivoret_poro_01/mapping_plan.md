# scythe_carnivoret_poro_01 — 매핑 플랜

> 분석일: 2026-05-08
> 원본: Carnivoret Weapon Set Volume 1.zip (original/ 폴더에 보관)
> 원 제작자: elitecreatures
> 추출 기준: CraftEngine 포맷 (vanilla 리소스팩 구조와 유사)

---

## 1. 원본 파일 목록 (scythe 관련)

```
Weapons/plugins/CraftEngine/resources/elitecreatures/resourcepack/assets/elitecreatures/
├── models/item/carnivoret_weapon_set/
│   └── scythe.json              ← 단일 정적 모델 (36 elements)
└── textures/item/carnivoret_weapon_set/
    └── scythe.png               ← 2,403 bytes, 정적 (mcmeta 없음)
```

> 동일 자산이 ItemsAdder / Nexo / Oraxen 포맷으로도 포함됨. CraftEngine 버전 사용.

---

## 2. 원본 구조 분석

### 2-1. 원본 텍스처 경로

```json
"textures": {
  "1": "elitecreatures:item/carnivoret_weapon_set/scythe",
  "particle": "elitecreatures:item/carnivoret_weapon_set/scythe"
}
```

- 텍스처 키 `"1"` 및 `"particle"` 모두 동일 경로 참조
- **Poro namespace로 변경 필요**

### 2-2. 텍스처

- 정적 PNG (2,403 bytes)
- mcmeta 없음 → 애니메이션 없음

### 2-3. 원본 CMD

- CraftEngine 자체 시스템 — 별도 CMD override 없음
- Poro 배정 CMD: `100501`

---

## 3. 파일 매핑

| 원본 파일 | 목표 경로 | 수정 사항 |
|---|---|---|
| `carnivoret_weapon_set/scythe.json` | `assets/poro/models/item/weapons/scythe_carnivoret_poro_01.json` | 텍스처 경로 수정 + mcmodels 제거 |
| `carnivoret_weapon_set/scythe.png` | `assets/poro/textures/item/weapons/scythe_carnivoret_poro_01.png` | 파일명 변경만 |
| (없음) | `assets/minecraft/items/netherite_hoe.json` | 신규 작성 (1.21.4+ 포맷) |

---

## 4. 포로 서버 export 경로

```
assets/export/resourcepack/assets/
├── poro/
│   ├── models/item/weapons/
│   │   └── scythe_carnivoret_poro_01.json
│   └── textures/item/weapons/
│       └── scythe_carnivoret_poro_01.png
└── minecraft/
    └── items/
        └── netherite_hoe.json              ← CMD 100501 조건 (신규)
```

---

## 5. namespace/path 수정 사항

### 5-1. 모델 JSON 텍스처 경로

```json
// 변경 전
"textures": {
  "1": "elitecreatures:item/carnivoret_weapon_set/scythe",
  "particle": "elitecreatures:item/carnivoret_weapon_set/scythe"
}

// 변경 후
"textures": {
  "1": "poro:item/weapons/scythe_carnivoret_poro_01",
  "particle": "poro:item/weapons/scythe_carnivoret_poro_01"
}
```

### 5-2. netherite_hoe.json (1.21.4+ 신규)

```json
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [
      {
        "when": "100501",
        "model": {
          "type": "minecraft:model",
          "model": "poro:item/weapons/scythe_carnivoret_poro_01"
        }
      }
    ],
    "fallback": {
      "type": "minecraft:model",
      "model": "minecraft:item/netherite_hoe"
    }
  }
}
```

---

## 6. CustomModelData 등록

| 항목 | 값 |
|---|---|
| 원본 CMD | 없음 (CraftEngine 자체 시스템) |
| **Poro 배정 CMD** | **100501** |
| material | NETHERITE_HOE |
| namespace | poro |
| model_path | `poro:item/weapons/scythe_carnivoret_poro_01` |
| texture_path | `poro:item/weapons/scythe_carnivoret_poro_01` |

---

## 7. display 설정 (원본 값)

| 뷰 | rotation | translation | scale |
|---|---|---|---|
| firstperson_righthand | [6, 0, 0] | [4, 4.75, 0] | [0.45742, 0.45742, 0.45742] |
| firstperson_lefthand | [6, 0, 0] | [4, 4.75, 0] | [0.45742, 0.45742, 0.45742] |
| thirdperson_righthand | — | [0, 1.75, 1.75] | [0.8, 0.8, 0.8] |
| thirdperson_lefthand | — | [0, 1.75, 1.75] | [0.8, 0.8, 0.8] |
| gui | [90, 45, -90] | [-2, -1.5, 0] | [0.53375, 0.53375, 0.53375] |
| ground | [-45, 0, 0] | [0, 6, 0] | [0.6, 0.6, 0.6] |
| head | — | — | **[0, 0, 0]** (머리 슬롯 투명 — 의도적) |
| fixed | [0, -90, 0] | [0, 0.75, -1.75] | [2, 2, 2] |

---

## 8. 작업 체크리스트

- [x] 원본 zip 수령 및 original/ 배치
- [x] 원본 구조 분석 완료
- [x] mapping_plan.md 작성 완료
- [x] `source/` 폴더에 수정용 복사본 준비 (`scythe_carnivoret_poro_01.json`)
- [x] 텍스처 `textures/` 배치 (`scythe_carnivoret_poro_01.png`)
- [x] export/resourcepack 배치 (model json + texture)
- [x] `assets/minecraft/items/netherite_hoe.json` CMD 조건 추가 (when: "100501")
- [x] `weapon_cosmetic_registry.yml` 등록 (status: export_ready)
- [x] 인게임 테스트 통과: `/minecraft:give @s netherite_hoe[custom_model_data={strings:["100501"]}]`
- [x] status → active
