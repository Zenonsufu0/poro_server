# prop_chest_poro_01 — 매핑 플랜

> 분석일: 2026-05-08
> 원본: Chests_Pack-p7uqtu.rar (original/ 폴더에 보관)
> 원본 소스 모델: normal_chest
> 추출 기준: 직접 경로 (ItemsAdder / Nexo 등 별도 포맷 미포함, 단일 구조)

---

## 1. 원본 파일 목록 (normal_chest 관련)

```
Chests Pack/assets/chests_pack/
├── models/
│   └── normal_chest.json     ← 단일 정적 모델 (12 elements)
└── textures/
    └── normal_chest.png      ← 3,639 bytes, 정적
```

> 동일 팩 내 medium_chest / premium_chest는 후속 보상 상자 등급 후보 (미등록)

---

## 2. 원본 구조 분석

### 2-1. 원본 텍스처 경로

```json
"textures": {
  "0": "chests_pack:normal_chest",
  "particle": "chests_pack:normal_chest"
}
```

- 원본 namespace `chests_pack:` — 서브디렉토리 없는 단순 경로
- **Poro namespace로 변경 필요**
- `mcmodels` 키 없음 → 제거 작업 불필요

### 2-2. 텍스처

- 정적 PNG (3,639 bytes)
- mcmeta 없음 → 애니메이션 없음

### 2-3. 원본 CMD

- 자체 시스템 — 별도 vanilla CMD override 없음
- Poro 배정 CMD: `200102`

---

## 3. 파일 매핑

| 원본 파일 | 목표 경로 | 수정 사항 |
|---|---|---|
| `chests_pack/models/normal_chest.json` | `assets/poro/models/item/props/prop_chest_poro_01.json` | 텍스처 경로 수정 |
| `chests_pack/textures/normal_chest.png` | `assets/poro/textures/item/props/prop_chest_poro_01.png` | 파일명 변경만 |
| `assets/minecraft/items/paper.json` | 기존 파일 수정 | 200101 보존 + 200102 case 추가 |

---

## 4. 포로 서버 export 경로

```
assets/export/resourcepack/assets/
├── poro/
│   ├── models/item/props/
│   │   └── prop_chest_poro_01.json
│   └── textures/item/props/
│       └── prop_chest_poro_01.png
└── minecraft/
    └── items/
        └── paper.json    ← 200101 보존 + 200102 추가
```

---

## 5. namespace/path 수정 사항

### 5-1. 모델 JSON 텍스처 경로

```json
// 변경 전
"textures": {
  "0": "chests_pack:normal_chest",
  "particle": "chests_pack:normal_chest"
}

// 변경 후
"textures": {
  "0": "poro:item/props/prop_chest_poro_01",
  "particle": "poro:item/props/prop_chest_poro_01"
}
```

### 5-2. paper.json (200101 보존 + 200102 추가)

```json
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [
      {
        "when": "200101",
        "model": { "type": "minecraft:model", "model": "poro:item/props/prop_barrel_poro_01" }
      },
      {
        "when": "200102",
        "model": { "type": "minecraft:model", "model": "poro:item/props/prop_chest_poro_01" }
      }
    ],
    "fallback": { "type": "minecraft:model", "model": "minecraft:item/paper" }
  }
}
```

---

## 6. CustomModelData 등록

| 항목 | 값 |
|---|---|
| **Poro 배정 CMD** | **200102** |
| material | PAPER |
| namespace | poro |
| model_path | `poro:item/props/prop_chest_poro_01` |
| texture_path | `poro:item/props/prop_chest_poro_01` |

---

## 7. display 설정 (원본 값)

| 뷰 | rotation | translation | scale |
|---|---|---|---|
| firstperson_righthand | [0, -180, 0] | [-9, 3.5, -7.25] | — (기본 1) |
| firstperson_lefthand | [0, -180, 0] | [-9, 3, -8.5] | — (기본 1) |
| thirdperson_righthand | [64, 0, 0] | [0, 1, -1] | [0.5, 0.5, 0.5] |
| thirdperson_lefthand | [64, 0, 0] | [0, 1, -1] | [0.5, 0.5, 0.5] |
| gui | [33, 139, 0] | [0, 2.75, 0] | — (기본 1) |
| ground | — | [0, 0.25, 0] | [0.5, 0.5, 0.5] |
| head | — | — | [0, 0, 0] |
| fixed | [-90, 0, 0] | [0, 0, -23] | [3, 3, 3] |

---

## 8. 작업 체크리스트

- [x] 원본 rar 수령 및 original/ 배치
- [x] 원본 구조 분석 완료
- [x] mapping_plan.md 작성 완료
- [x] `source/` 폴더에 수정용 복사본 준비 (`prop_chest_poro_01.json`)
- [x] 텍스처 `textures/` 배치 (`prop_chest_poro_01.png`)
- [x] export/resourcepack 배치 (model json + texture)
- [x] `assets/minecraft/items/paper.json` 200102 case 추가 (200101 보존)
- [x] `prop_registry.yml` 등록 (status: export_ready)
- [ ] 인게임 테스트 통과: `/minecraft:give @s minecraft:paper[minecraft:custom_model_data={strings:["200102"]}] 1`
- [ ] status → active
