# prop_barrel_poro_01 — 매핑 플랜

> 분석일: 2026-05-08
> 원본: BarrelKnight_1.1.zip (original/ 폴더에 보관)
> 원 제작자: BarrelKnight_1.1
> 원본 설계 용도: HMCCosmetics head cosmetics
> 추출 기준: ItemsAdder 포맷 (CraftEngine 포맷 미포함)

---

## 1. 원본 파일 목록 (barrel 관련)

```
ItemsAdder/contents/barrel_knight/resourcepack/assets/cosmetics/
├── models/
│   ├── barrel.json              ← 단일 정적 모델 (18 elements) ← 사용
│   └── barrel_thirdperson.json  ← 3인칭 전용 변형 모델 (18 elements, 동일 텍스처)
└── textures/
    └── barrel.png               ← 1,850 bytes, 정적
```

> `barrel_thirdperson.json`은 원저자가 3인칭 손持 전용으로 별도 제공. 1차 테스트는 `barrel.json` 단일 모델 사용. 3인칭 표시 어색 시 교체 검토.

---

## 2. 원본 구조 분석

### 2-1. 원본 텍스처 경로

```json
"textures": {
  "0": "cosmetics:barrel",
  "particle": "cosmetics:barrel"
}
```

- 원본 namespace `cosmetics:` → **`poro:` 변경 필요**
- 텍스처 키 `"0"` + `"particle"` (Carnivoret 무기 세트의 `"1"+"particle"` 패턴과 다름)

### 2-2. 텍스처

- 정적 PNG (1,850 bytes)
- mcmeta 없음 → 애니메이션 없음

### 2-3. 원본 CMD

- HMCCosmetics/ItemsAdder 자체 시스템 — 별도 vanilla CMD override 없음
- Poro 배정 CMD: `200101`

---

## 3. 파일 매핑

| 원본 파일 | 목표 경로 | 수정 사항 |
|---|---|---|
| `cosmetics/models/barrel.json` | `assets/poro/models/item/props/prop_barrel_poro_01.json` | 텍스처 경로 수정 + mcmodels 제거 |
| `cosmetics/textures/barrel.png` | `assets/poro/textures/item/props/prop_barrel_poro_01.png` | 파일명 변경만 |
| (없음) | `assets/minecraft/items/paper.json` | 신규 작성 (1.21.4+ 포맷) |

---

## 4. 포로 서버 export 경로

```
assets/export/resourcepack/assets/
├── poro/
│   ├── models/item/props/
│   │   └── prop_barrel_poro_01.json
│   └── textures/item/props/
│       └── prop_barrel_poro_01.png
└── minecraft/
    └── items/
        └── paper.json              ← CMD 200101 조건 (신규)
```

---

## 5. namespace/path 수정 사항

### 5-1. 모델 JSON 텍스처 경로

```json
// 변경 전
"textures": {
  "0": "cosmetics:barrel",
  "particle": "cosmetics:barrel"
}

// 변경 후
"textures": {
  "0": "poro:item/props/prop_barrel_poro_01",
  "particle": "poro:item/props/prop_barrel_poro_01"
}
```

### 5-2. paper.json (1.21.4+ 신규)

```json
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [
      {
        "when": "200101",
        "model": {
          "type": "minecraft:model",
          "model": "poro:item/props/prop_barrel_poro_01"
        }
      }
    ],
    "fallback": {
      "type": "minecraft:model",
      "model": "minecraft:item/paper"
    }
  }
}
```

---

## 6. CustomModelData 등록

| 항목 | 값 |
|---|---|
| 원본 CMD | 없음 (HMCCosmetics 자체 시스템) |
| **Poro 배정 CMD** | **200101** |
| material | PAPER |
| namespace | poro |
| model_path | `poro:item/props/prop_barrel_poro_01` |
| texture_path | `poro:item/props/prop_barrel_poro_01` |

---

## 7. display 설정 (원본 값 — HMCCosmetics 기준)

| 뷰 | rotation | translation | scale | 비고 |
|---|---|---|---|---|
| firstperson_righthand | [0, 100, 0] | [1, 16, 1.25] | [0.8, 0.8, 0.8] | — |
| firstperson_lefthand | [0, 100, 0] | [1, 16, 1.25] | [0.8, 0.8, 0.8] | — |
| thirdperson_righthand | [60, -29, 0] | [-1.25, 9.75, 14] | — (기본 1) | scale 없음 주의 |
| thirdperson_lefthand | [60, -29, 0] | [-1.25, 9.75, 14] | — (기본 1) | scale 없음 주의 |
| gui | [18, -160, 8] | [2.5, 21, 0] | [1.3, 1.3, 1.3] | 인벤토리 아이콘 |
| ground | — | [0, 22, 0] | — (기본 1) | scale 없음 → 부유 가능 |
| head | [-5, 0, 0] | [0, 37, -3.5] | [3.2, 3.2, 3.2] | 원본 head cosmetic용 |
| fixed | — | [0, 34, -1.5] | [2, 2, 2] | ItemDisplay 향후 활용 |

---

## 8. 작업 체크리스트

- [x] 원본 zip 수령 및 original/ 배치
- [x] 원본 구조 분석 완료
- [x] mapping_plan.md 작성 완료
- [x] `source/` 폴더에 수정용 복사본 준비 (`prop_barrel_poro_01.json`)
- [x] 텍스처 `textures/` 배치 (`prop_barrel_poro_01.png`)
- [x] export/resourcepack 배치 (model json + texture)
- [x] `assets/minecraft/items/paper.json` CMD 조건 추가 (when: "200101")
- [x] `prop_registry.yml` 등록 (status: export_ready)
- [ ] 인게임 테스트 통과: `/minecraft:give @s paper[custom_model_data={strings:["200101"]}]`
- [ ] status → active
