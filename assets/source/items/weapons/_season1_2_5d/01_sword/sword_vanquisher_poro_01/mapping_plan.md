# sword_vanquisher_poro_01 — 매핑 플랜

> 분석일: 2026-05-08
> 원본: Vanquisher_Sword-4fll7y.zip (original/ 폴더에 보관)
> 원 제작자: kron (pack.mcmeta 표기 기준)
> 원본 pack_format: 7 (Minecraft 1.18 시대 포맷)

---

## 1. 원본 파일 목록

```
Vanquisher Sword/
├── assets/minecraft/
│   ├── models/
│   │   ├── custom/elitecreatures/vanquisher_sword/
│   │   │   ├── vanquisher_sword.json          ← 실제 모델 (15,960 bytes, 2026-05-07 수정)
│   │   │   └── vanquisher_sword.json.bak      ← 원본 백업 (19,994 bytes, 2022-06-27)
│   │   └── item/
│   │       └── netherite_sword.json.bak       ← CMD 오버라이드 구 포맷 (222 bytes)
│   │       └── netherite_sword.json           ← CMD 오버라이드 (242 bytes, 2026-05-07 수정)
│   ├── textures/custom/elitecreatures/vanquisher_sword/
│   │   ├── vanquisher_sword.png               ← 메인 텍스처 (43,159 bytes, 애니메이션)
│   │   └── vanquisher_sword.png.mcmeta        ← 애니메이션 메타 (frametime:2, interpolate:true)
│   └── texts/
│       └── LegalTermsAndConditions.txt
├── pack.mcmeta
└── pack.png
```

**주의:** `.json`과 `.json.bak`이 공존함. `.bak`은 원본 구조 보존용이고 `.json`은 2026-05-07에 수정된 버전. 두 버전 모두 원본 폴더에 보존.

---

## 2. 원본 구조 분석

### 2-1. 원본 CMD (netherite_sword.json)

```json
{
  "parent": "item/generated",
  "textures": {"layer0": "item/netherite_sword"},
  "overrides": [
    {"predicate": {"custom_model_data": 2000001},
     "model": "custom/elitecreatures/vanquisher_sword/vanquisher_sword"}
  ]
}
```

- **원본 CMD 값: `2000001`** → Poro 서버 배정: `100101` (변경 필요)
- **포맷: `overrides` 방식 (1.20 이하 구식)** → Paper 1.21.10은 `assets/minecraft/items/` 새 포맷 사용 (변환 필요)

### 2-2. 원본 모델 텍스처 경로 (vanquisher_sword.json)

```json
"textures": {
  "0": "custom/elitecreatures/vanquisher_sword/vanquisher_sword",
  "particle": "custom/elitecreatures/vanquisher_sword/vanquisher_sword"
}
```

- 원본 텍스처 경로: `assets/minecraft/textures/custom/elitecreatures/vanquisher_sword/vanquisher_sword.png`
- **Poro 목표 경로로 변경 필요**

### 2-3. 텍스처 애니메이션

```json
{"animation": {"interpolate": true, "frametime": 2}}
```

- 텍스처가 **애니메이션 처리됨** (각 프레임 2 tick = 0.1초)
- 프레임 보간 활성화 (`interpolate: true`)
- **export 시 `.png.mcmeta` 파일도 함께 배치 필수**

---

## 3. 파일 매핑 (원본 → Poro export 목표 경로)

| 원본 파일 | 목표 경로 (export/resourcepack 기준) | 수정 사항 |
|---|---|---|
| `assets/minecraft/models/custom/.../vanquisher_sword.json` | `assets/poro/models/item/weapons/sword_vanquisher_poro_01.json` | 텍스처 경로 수정 필요 |
| `assets/minecraft/textures/custom/.../vanquisher_sword.png` | `assets/poro/textures/item/weapons/sword_vanquisher_poro_01.png` | 파일명 변경만 |
| `assets/minecraft/textures/custom/.../vanquisher_sword.png.mcmeta` | `assets/poro/textures/item/weapons/sword_vanquisher_poro_01.png.mcmeta` | 내용 변경 없음 |
| `assets/minecraft/models/item/netherite_sword.json` (원본 구식 오버라이드) | `assets/minecraft/items/netherite_sword.json` | 1.21.4+ 새 포맷으로 재작성 |

---

## 4. 포로 서버 export 예정 경로

```
assets/export/resourcepack/assets/
├── poro/
│   ├── models/item/weapons/
│   │   └── sword_vanquisher_poro_01.json     ← 텍스처 경로 수정된 모델
│   └── textures/item/weapons/
│       ├── sword_vanquisher_poro_01.png      ← 원본 텍스처
│       └── sword_vanquisher_poro_01.png.mcmeta  ← 애니메이션 메타 (필수)
└── minecraft/
    └── items/
        └── netherite_sword.json              ← CMD 조건 추가 (1.21.4+ 새 포맷)
```

---

## 5. namespace/path 수정 사항

### 5-1. 모델 JSON 텍스처 경로 수정

```json
// 변경 전 (원본)
"textures": {
  "0": "custom/elitecreatures/vanquisher_sword/vanquisher_sword",
  "particle": "custom/elitecreatures/vanquisher_sword/vanquisher_sword"
}

// 변경 후 (poro)
"textures": {
  "0": "poro:item/weapons/sword_vanquisher_poro_01",
  "particle": "poro:item/weapons/sword_vanquisher_poro_01"
}
```

### 5-2. 아이템 정의 포맷 변환 (1.21.4+ 새 포맷)

```json
// assets/minecraft/items/netherite_sword.json
// 기존 CMD 조건들 유지 + poro 항목 추가
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [
      {
        "when": "100101",
        "model": {
          "type": "minecraft:model",
          "model": "poro:item/weapons/sword_vanquisher_poro_01"
        }
      }
    ],
    "fallback": {
      "type": "minecraft:model",
      "model": "minecraft:item/netherite_sword"
    }
  }
}
```

> ⚠️ 기존 CMD 항목이 있는 경우 `cases` 배열에 추가하는 방식으로 병합. 덮어쓰기 금지.

---

## 6. CustomModelData 등록 계획

| 항목 | 값 |
|---|---|
| 원본 CMD | 2000001 (사용 안 함) |
| **Poro 배정 CMD** | **100101** |
| material | NETHERITE_SWORD |
| namespace | poro |
| model_path | `poro:item/weapons/sword_vanquisher_poro_01` |
| texture_path | `poro:item/weapons/sword_vanquisher_poro_01` |
| 레지스트리 등록 위치 | `_season1_2_5d/00_registry/weapon_cosmetic_registry.yml` |

---

## 7. display 설정 (원본 값 — 인게임 테스트 후 조정)

| 뷰 | rotation | translation | scale | 조정 필요 여부 |
|---|---|---|---|---|
| firstperson_righthand | [-14.75, 0, 0] | [0, 6, 0.25] | [0.5, 0.5, 0.5] | 테스트 후 확인 |
| firstperson_lefthand | [-14, 0, 0] | [0, 6, 0.25] | [0.5, 0.5, 0.5] | 테스트 후 확인 |
| thirdperson_righthand | — | [0, 7.25, 2.25] | [0.8, 0.8, 0.8] | 테스트 후 확인 |
| thirdperson_lefthand | — | [0, 7.25, 1.75] | [0.8, 0.8, 0.8] | 테스트 후 확인 |
| gui | [-90, 45.75, 90] | [0.75, -1, 0] | [0.46289, 0.46289, 0.46289] | 테스트 후 확인 |
| ground | [-79, -79, 0] | [0, 1.25, 0] | [0.5, 0.5, 0.5] | 테스트 후 확인 |
| head | — | [0, 6, 0] | **[0, 0, 0]** | ⚠️ head 슬롯에서 **완전 투명** (의도적) |
| fixed (액자) | [0, 90, 0] | [0, -1.25, -1.75] | [1.32227, 1.32227, 1.32227] | 테스트 후 확인 |

> head display의 scale이 [0,0,0]이므로 머리 슬롯에 장착 시 보이지 않음. 이는 원본 제작자의 의도로 보임.

---

## 8. 작업 체크리스트

- [x] 원본 zip 수령 및 original/ 배치
- [x] 원본 구조 분석 완료
- [x] mapping_plan.md 작성 완료
- [x] `source/` 폴더에 수정용 복사본 준비 (`sword_vanquisher_poro_01.json`)
- [x] 모델 json 텍스처 경로 수정 (poro namespace 적용)
- [x] 텍스처 + mcmeta `textures/` 배치 (`sword_vanquisher_poro_01.png` + `.png.mcmeta`)
- [x] export/resourcepack 배치 (model json, texture, mcmeta) → `assets/poro/models/item/weapons/`, `assets/poro/textures/item/weapons/`
- [x] `assets/minecraft/items/netherite_sword.json` CMD 조건 추가 (when: "100101")
- [x] `weapon_cosmetic_registry.yml` status `planned` → `export_ready` 변경
- [x] EmpireRPG CMD 100101 등록 확인
- [x] 인게임 테스트 통과 (firstperson/thirdperson/gui/ground/인벤토리 정상)
- [x] status `export_ready` → `active` 변경
