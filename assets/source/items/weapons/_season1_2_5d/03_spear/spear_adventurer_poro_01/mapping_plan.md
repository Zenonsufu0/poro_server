# spear_adventurer_poro_01 — 매핑 플랜

> 작성일: 2026-05-08
> 파이프라인: blockbench_new_build (레퍼런스 기반 신규 제작)
> 레퍼런스: original/spear_adventurer_poro_01_ref.png

---

## 1. 소스 구조

```
03_spear/spear_adventurer_poro_01/
├── original/
│   └── spear_adventurer_poro_01_ref.png     ← 레퍼런스 이미지
├── source/
│   ├── asset_brief.md                        ← 파츠 설계 + 색상 팔레트
│   ├── ref_spec.json                         ← Blockbench 좌표 계획
│   ├── spear_adventurer_poro_01.bbmodel      ← [Phase 3 생성]
│   └── spear_adventurer_poro_01.json         ← [Phase 5 export]
├── textures/
│   └── spear_adventurer_poro_01.png          ← [Phase 4-5 생성]
└── previews/                                 ← [선택적 스크린샷]
```

---

## 2. export 경로

```
assets/export/resourcepack/assets/
├── poro/
│   ├── models/item/weapons/
│   │   └── spear_adventurer_poro_01.json     ← [Phase 5 배치]
│   └── textures/item/weapons/
│       └── spear_adventurer_poro_01.png      ← [Phase 5 배치]
└── minecraft/
    └── items/
        └── trident.json                      ← 100301 보존 + 100302 추가
```

---

## 3. 텍스처 경로 (Phase 5 적용)

```json
"textures": {
  "1": "poro:item/weapons/spear_adventurer_poro_01",
  "particle": "poro:item/weapons/spear_adventurer_poro_01"
}
```

---

## 4. trident.json 변경 계획

```json
// 변경 후 (100301 보존 + 100302 추가)
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [
      {
        "when": "100301",
        "model": { "type": "minecraft:model", "model": "poro:item/weapons/spear_carnivoret_poro_01" }
      },
      {
        "when": "100302",
        "model": { "type": "minecraft:model", "model": "poro:item/weapons/spear_adventurer_poro_01" }
      }
    ],
    "fallback": { "type": "minecraft:model", "model": "minecraft:item/trident" }
  }
}
```

---

## 5. CustomModelData 등록

| 항목 | 값 |
|---|---|
| **Poro 배정 CMD** | **100302** |
| material | TRIDENT |
| namespace | poro |
| model_path | `poro:item/weapons/spear_adventurer_poro_01` |
| texture_path | `poro:item/weapons/spear_adventurer_poro_01` |

---

## 6. display 설정 계획

Carnivoret 창(100301) display 값을 기준으로 Phase 5에서 적용, 인게임 테스트 후 scale 확정.

| 뷰 | 계획 기준값 |
|---|---|
| firstperson_righthand | rotation [6, 0, 0], translation [3.5, 2, 0], scale [0.463, 0.463, 0.463] |
| thirdperson_righthand | translation [0, 1.5, 1.75], scale [0.85, 0.85, 0.85] |
| gui | rotation [90, 45, -90], translation [-3.25, -3.75, 0], scale [0.610, 0.610, 0.610] |
| ground | rotation [-45, 0, 0], translation [0, 6, 0], scale [0.6, 0.6, 0.6] |
| head | scale [0, 0, 0] |

---

## 7. 작업 체크리스트

- [x] 레퍼런스 이미지 original/ 배치
- [x] asset_brief.md 작성
- [x] ref_spec.json 작성
- [x] spec.yaml 작성
- [x] notes.md 작성
- [x] mapping_plan.md 작성
- [x] seed .bbmodel 생성 (6 groups / 11 elements / Carnivoret 창 display 기준)
- [ ] Blockbench에서 .bbmodel 열어 파츠 편집 (Phase 3)
- [ ] 블록아웃 품질 게이트 통과 (Phase 3)
- [ ] 텍스처 적용 및 export (Phase 4)
- [ ] source/spear_adventurer_poro_01.json 배치
- [ ] textures/spear_adventurer_poro_01.png 배치
- [ ] export/resourcepack 배치 (Phase 5)
- [ ] trident.json 100302 추가 (100301 보존) (Phase 5)
- [ ] weapon_cosmetic_registry.yml 등록 (status: export_ready) (Phase 5)
- [ ] 인게임 테스트: `/minecraft:give @s minecraft:trident[minecraft:custom_model_data={strings:["100302"]}] 1`
- [ ] status → active
