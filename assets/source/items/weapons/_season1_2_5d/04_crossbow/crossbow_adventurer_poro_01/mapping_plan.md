# crossbow_adventurer_poro_01 — 매핑 플랜

> 분석일: 2026-05-08
> 원본: adventurer_tools_v8.zip (original/ 폴더에 보관)
> 원 제작자: akaleaf
> 원본 포맷: ItemsAdder / Oraxen / vanilla resourcepack 세 버전 포함 (vanilla 버전 사용)

---

## 1. 원본 파일 목록 (crossbow 관련)

```
adventurer_tools_resourcepack/
├── assets/minecraft/
│   ├── models/item/
│   │   ├── crossbow.json                              ← 구 CMD override (CMD 2501, overrides 포맷)
│   │   └── akaleaf/adventurer_tools/
│   │       ├── adventurer_crossbow.json               ← 메인 정적 모델 (12 elements)
│   │       ├── adventurer_crossbow_pulling_0.json     ← 당기기 0단계
│   │       ├── adventurer_crossbow_pulling_1.json     ← 당기기 1단계
│   │       ├── adventurer_crossbow_pulling_2.json     ← 당기기 2단계 (완전 당김)
│   │       ├── adventurer_crossbow_arrow.json         ← 화살 장전 상태
│   │       └── adventurer_crossbow_firework.json      ← 불꽃 장전 상태
│   └── textures/item/akaleaf/adventurer_tools/
│       └── adventurer_crossbow.png                    ← 953 bytes, 정적 (mcmeta 없음)
```

---

## 2. 원본 구조 분석

### 2-1. 원본 CMD (crossbow.json 구 포맷 발췌)

```json
"overrides": [
  {"predicate": {"custom_model_data": 2501},
   "model": "item/akaleaf/adventurer_tools/adventurer_crossbow"},
  {"predicate": {"custom_model_data": 2501, "pulling": 1},
   "model": "item/akaleaf/adventurer_tools/adventurer_crossbow_pulling_0"},
  ...
]
```

- **원본 CMD 값: `2501`** → Poro 배정: `100401` (변경)
- **포맷: `overrides` 방식** → 1.21.4+ 새 포맷으로 재작성 필요

### 2-2. 원본 모델 텍스처 경로

```json
"textures": {"1": "item/akaleaf/adventurer_tools/adventurer_crossbow"}
```

- 텍스처 키가 `"1"` (vanquisher의 `"0"`과 다름 — 원작자 선택)
- **Poro 목표 경로로 변경 필요**

### 2-3. 텍스처

- 정적 PNG (953 bytes)
- mcmeta 없음 → 애니메이션 없음

---

## 3. 파일 매핑 (원본 → Poro export 목표 경로)

| 원본 파일 | 목표 경로 | 수정 사항 |
|---|---|---|
| `akaleaf/.../adventurer_crossbow.json` | `assets/poro/models/item/weapons/crossbow_adventurer_poro_01.json` | 텍스처 경로 수정 |
| `akaleaf/.../adventurer_crossbow.png` | `assets/poro/textures/item/weapons/crossbow_adventurer_poro_01.png` | 파일명 변경만 |
| `models/item/crossbow.json` (구 override) | `assets/minecraft/items/crossbow.json` | 1.21.4+ 새 포맷으로 재작성 |

> pulling/arrow/firework 모델은 source/ 폴더에 보존. export 연결은 2차 단계.

---

## 4. 포로 서버 export 경로

```
assets/export/resourcepack/assets/
├── poro/
│   ├── models/item/weapons/
│   │   └── crossbow_adventurer_poro_01.json     ← 정적 메인 모델
│   └── textures/item/weapons/
│       └── crossbow_adventurer_poro_01.png      ← 정적 텍스처
└── minecraft/
    └── items/
        └── crossbow.json                        ← CMD 100401 조건 (1.21.4+ 포맷)
```

---

## 5. namespace/path 수정 사항

### 5-1. 모델 JSON 텍스처 경로 수정

```json
// 변경 전
"textures": {"1": "item/akaleaf/adventurer_tools/adventurer_crossbow"}

// 변경 후
"textures": {"1": "poro:item/weapons/crossbow_adventurer_poro_01"}
```

### 5-2. 아이템 정의 포맷 (1.21.4+ 신규)

```json
// assets/minecraft/items/crossbow.json
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [
      {
        "when": "100401",
        "model": {
          "type": "minecraft:model",
          "model": "poro:item/weapons/crossbow_adventurer_poro_01"
        }
      }
    ],
    "fallback": {
      "type": "minecraft:model",
      "model": "minecraft:item/crossbow"
    }
  }
}
```

> ⚠️ pulling/charged 상태 연결은 2차 단계. 현재 정적 모델만 등록.

---

## 6. CustomModelData 등록

| 항목 | 값 |
|---|---|
| 원본 CMD | 2501 (사용 안 함) |
| **Poro 배정 CMD** | **100401** |
| material | CROSSBOW |
| namespace | poro |
| model_path | `poro:item/weapons/crossbow_adventurer_poro_01` |
| texture_path | `poro:item/weapons/crossbow_adventurer_poro_01` |

---

## 7. display 설정 (원본 값)

| 뷰 | rotation | translation | scale |
|---|---|---|---|
| firstperson_righthand | [95, -179, 80] | [0.5, 1.5, -1] | [0.6, 0.6, 0.6] |
| firstperson_lefthand | [95, -179, -100] | [2.25, 1.5, -0.75] | [0.6, 0.6, 0.6] |
| thirdperson_righthand | [-90, 0, -105] | [1, 0.25, -5] | [0.6, 0.6, 0.6] |
| thirdperson_lefthand | [-90, 0, 75] | [3, 0.25, -4.5] | [0.6, 0.6, 0.6] |
| gui | [0, 0, -45] | [-4.25, 2.25, -0.75] | [0.815, 0.815, 0.815] |
| ground | [0, 0, -45] | [0, 3, 0] | [0.3, 0.3, 0.3] |
| head | [0, 0, -90] | [-1, 9, 0.75] | [0.6, 0.6, 0.6] |
| fixed | [180, 0, 135] | [3.5, 1.75, 0.75] | [0.65, 0.65, 0.65] |

---

## 8. 2차 단계 계획 (정적 테스트 통과 후)

석궁 상태별 모델 연결 시 추가 등록이 필요한 모델:

| poro 모델명 | 대응 상태 |
|---|---|
| `crossbow_adventurer_poro_01_pulling_0` | pulling 시작 |
| `crossbow_adventurer_poro_01_pulling_1` | pulling 중간 |
| `crossbow_adventurer_poro_01_pulling_2` | pulling 완료 |
| `crossbow_adventurer_poro_01_arrow` | 화살 장전 완료 |
| `crossbow_adventurer_poro_01_firework` | 불꽃 장전 완료 |

---

## 9. 작업 체크리스트

- [x] 원본 zip 수령 및 original/ 배치
- [x] 원본 구조 분석 완료
- [x] mapping_plan.md 작성 완료
- [x] `source/` 폴더에 수정용 복사본 준비 (6종 JSON, 텍스처 경로 수정)
- [x] 텍스처 `textures/` 배치
- [x] export/resourcepack 배치 (메인 모델 + 텍스처)
- [x] `assets/minecraft/items/crossbow.json` CMD 조건 추가 (when: "100401")
- [x] `weapon_cosmetic_registry.yml` 등록 (status: export_ready)
- [x] 인게임 테스트 통과 (손/인벤토리/3인칭 정상)
- [x] status → active
- [ ] 2차 단계: pulling/charged 상태 모델 연결
