# hammer_carnivoret_poro_01 — 매핑 플랜

> 분석일: 2026-05-08
> 원본: Carnivoret Weapon Set Volume 1.zip (original/ 폴더에 보관)
> 원 제작자: elitecreatures
> 추출 기준: CraftEngine 포맷 (vanilla 리소스팩 구조와 유사)

---

## 1. 원본 파일 목록 (hammer 관련)

```
Weapons/plugins/CraftEngine/resources/elitecreatures/resourcepack/assets/elitecreatures/
├── models/item/carnivoret_weapon_set/
│   └── hammer.json              ← 단일 정적 모델 (72 elements)
└── textures/item/carnivoret_weapon_set/
    └── hammer.png               ← 3,549 bytes, 정적 (mcmeta 없음)
```

> 동일 자산이 ItemsAdder / Nexo / Oraxen 포맷으로도 포함됨. CraftEngine 버전 사용.

---

## 2. 원본 구조 분석

### 2-1. 원본 텍스처 경로

```json
"textures": {
  "1": "elitecreatures:item/carnivoret_weapon_set/hammer",
  "particle": "elitecreatures:item/carnivoret_weapon_set/hammer"
}
```

- 텍스처 키 `"1"` 및 `"particle"` 모두 동일 경로 참조
- **Poro namespace로 변경 필요**

### 2-2. 텍스처

- 정적 PNG (3,549 bytes)
- mcmeta 없음 → 애니메이션 없음

### 2-3. 원본 CMD

- CraftEngine 자체 시스템 — 별도 CMD override 없음
- Poro 배정 CMD: `100201`

---

## 3. 파일 매핑

| 원본 파일 | 목표 경로 | 수정 사항 |
|---|---|---|
| `carnivoret_weapon_set/hammer.json` | `assets/poro/models/item/weapons/hammer_carnivoret_poro_01.json` | 텍스처 경로 수정 |
| `carnivoret_weapon_set/hammer.png` | `assets/poro/textures/item/weapons/hammer_carnivoret_poro_01.png` | 파일명 변경만 |
| (없음) | `assets/minecraft/items/mace.json` | 신규 작성 (1.21.4+ 포맷) |

---

## 4. 포로 서버 export 경로

```
assets/export/resourcepack/assets/
├── poro/
│   ├── models/item/weapons/
│   │   └── hammer_carnivoret_poro_01.json
│   └── textures/item/weapons/
│       └── hammer_carnivoret_poro_01.png
└── minecraft/
    └── items/
        └── mace.json              ← CMD 100201 조건 (신규)
```

---

## 5. namespace/path 수정 사항

### 5-1. 모델 JSON 텍스처 경로

```json
// 변경 전
"textures": {
  "1": "elitecreatures:item/carnivoret_weapon_set/hammer",
  "particle": "elitecreatures:item/carnivoret_weapon_set/hammer"
}

// 변경 후
"textures": {
  "1": "poro:item/weapons/hammer_carnivoret_poro_01",
  "particle": "poro:item/weapons/hammer_carnivoret_poro_01"
}
```

### 5-2. mace.json (1.21.4+ 신규)

```json
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [
      {
        "when": "100201",
        "model": {
          "type": "minecraft:model",
          "model": "poro:item/weapons/hammer_carnivoret_poro_01"
        }
      }
    ],
    "fallback": {
      "type": "minecraft:model",
      "model": "minecraft:item/mace"
    }
  }
}
```

---

## 6. CustomModelData 등록

| 항목 | 값 |
|---|---|
| 원본 CMD | 없음 (CraftEngine 자체 시스템) |
| **Poro 배정 CMD** | **100201** |
| material | MACE |
| namespace | poro |
| model_path | `poro:item/weapons/hammer_carnivoret_poro_01` |
| texture_path | `poro:item/weapons/hammer_carnivoret_poro_01` |

---

## 7. display 설정 (원본 값)

| 뷰 | rotation | translation | scale |
|---|---|---|---|
| firstperson_righthand | [6, 0, 0] | [3.5, 2.25, 0] | [0.45547, 0.45547, 0.45547] |
| firstperson_lefthand | [6, 0, 0] | [3.5, 2.25, 0] | [0.45547, 0.45547, 0.45547] |
| thirdperson_righthand | — | [0, 2.25, 1.75] | [0.9, 0.9, 0.9] |
| thirdperson_lefthand | — | [0, 2.25, 1.75] | [0.9, 0.9, 0.9] |
| gui | [90, 45, -90] | [-2.5, -2.5, 0] | [0.50055, 0.50055, 0.50055] |
| ground | [-45, 0, 0] | [0, 6, 0] | [0.6, 0.6, 0.6] |
| head | — | — | **[0, 0, 0]** (머리 슬롯 투명 — 의도적) |
| fixed | [0, -90, 0] | [0, -2.25, -2.25] | [2.2, 2.2, 2.2] |

---

## 8. 작업 체크리스트

- [x] 원본 zip 수령 및 original/ 배치
- [x] 원본 구조 분석 완료
- [x] mapping_plan.md 작성 완료
- [x] `source/` 폴더에 수정용 복사본 준비 (`hammer_carnivoret_poro_01.json`)
- [x] 텍스처 `textures/` 배치 (`hammer_carnivoret_poro_01.png`)
- [x] export/resourcepack 배치 (model json + texture)
- [x] `assets/minecraft/items/mace.json` CMD 조건 추가 (when: "100201")
- [x] `weapon_cosmetic_registry.yml` 등록 (status: export_ready)
- [x] 인게임 테스트 통과: `/minecraft:give @s mace[custom_model_data={strings:["100201"]}]`
- [x] status → active
