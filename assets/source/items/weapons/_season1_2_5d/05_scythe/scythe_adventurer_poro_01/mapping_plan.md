# scythe_adventurer_poro_01 — 매핑 플랜

> 작성일: 2026-05-09
> 최종 수정: Phase 1 완료 (레퍼런스 분석 + 설계 확정)
> 파이프라인: blockbench_new_build (레퍼런스 기반 신규 제작)
> 상태: design_ready

---

## 1. 소스 구조

```
05_scythe/scythe_adventurer_poro_01/
├── original/
│   ├── scythe_adventurer_poro_01_ref.png           ← 보조 레퍼런스 (실사풍 3D)
│   └── scythe_adventurer_poro_01_texture_ref.png   ← 주 레퍼런스 (픽셀아트)
├── source/
│   └── scythe_adventurer_poro_01.bbmodel           ← [Phase 3 MCP 제작]
├── textures/
│   └── scythe_adventurer_poro_01.png               ← [Phase 4 제작]
├── asset_brief.md                                  ← Phase 1 완료
├── ref_spec.json                                   ← Phase 1 완료
├── spec.yaml                                       ← Phase 1 완료
├── notes.md
└── mapping_plan.md
```

---

## 2. Phase 2 .bbmodel 빌드 계획 (bb-build-blockout 준비)

### 2-1. 그룹 구조

```
scythe_adventurer_poro_01 (root)
├── blade/
│   ├── blade_base
│   ├── blade_mid
│   ├── blade_upper
│   └── blade_tip
├── socket/
│   ├── socket_body
│   ├── socket_band_upper
│   └── socket_band_lower
├── shaft/
│   ├── shaft_upper
│   └── shaft_lower
├── grip/
│   ├── grip_front
│   └── grip_back
└── hardware/
    ├── collar
    └── butt_cap
```

### 2-2. 좌표 계획 (rough — Phase 3에서 세부 조정)

| 파트 | from [X, Y, Z] | to [X, Y, Z] | 비고 |
|---|---|---|---|
| `butt_cap` | [6.5, -2, 7] | [9.5, 0, 9] | 하단 금속 페룰 |
| `shaft_lower` | [7, 0, 7.5] | [9, 5, 8.5] | 그립 아래 목재 |
| `grip_front` | [6.5, 5, 7] | [9.5, 14, 8] | 가죽 감기 앞면 |
| `grip_back` | [6.5, 5, 8] | [9.5, 14, 9] | 가죽 감기 뒷면 |
| `collar` | [6.5, 13.5, 6.5] | [9.5, 15.5, 9.5] | 그립 상단 금속 칼라 |
| `shaft_upper` | [7, 15, 7.5] | [9, 24, 8.5] | 소켓 아래 목재 자루 |
| `socket_band_lower` | [6.5, 23, 6.5] | [10, 25, 9.5] | 소켓 하단 밴드 |
| `socket_body` | [7, 24, 7] | [13, 30, 9] | 메인 브래킷 |
| `socket_band_upper` | [6.5, 29, 6.5] | [12, 31, 9.5] | 소켓 상단 밴드 |
| `blade_base` | [10, 26, 7.5] | [16, 31, 8.5] | 날 기저부 |
| `blade_mid` | [14, 29, 7.5] | [19, 33, 8.5] | 날 중간 (우상향) |
| `blade_upper` | [16, 32, 7.5] | [20, 36, 8.5] | 날 상단 (좁아짐) |
| `blade_tip` | [18, 35, 7.5] | [20, 38, 8.5] | 날 끝 포인트 |

**Y 범위:** -2(버트캡 하단) ~ 38(날 끝), 총 40유닛  
**X 범위:** 6.5(자루 외측) ~ 20(날 끝)  
**Z 깊이:** 2.5D — 자루 Z=7.5-8.5(1유닛), 소켓 Z=7-9(2유닛), 그립 Z=7-9

### 2-3. 날 커브 근사 방식

```
blade step 구조 (측면 실루엣):
  Y=26-31 → X=10-16  (blade_base, 가장 넓음)
  Y=29-33 → X=14-19  (blade_mid, 겹침으로 연결)
  Y=32-36 → X=16-20  (blade_upper, 좁아짐)
  Y=35-38 → X=18-20  (blade_tip, 포인트)

  겹침(overlap) 활용으로 stepped 단차를 줄여 곡선처럼 보이게 함
```

---

## 3. export 경로 (Phase 5 최종 배치 대상)

```
assets/export/resourcepack/assets/
├── poro/
│   ├── models/item/weapons/
│   │   └── scythe_adventurer_poro_01.json     ← [Phase 5 배치]
│   └── textures/item/weapons/
│       └── scythe_adventurer_poro_01.png      ← [Phase 5 배치]
└── minecraft/
    └── items/
        └── netherite_hoe.json                 ← 100501 보존 + 100502 추가
```

---

## 4. 텍스처 경로 (Phase 5 적용)

```json
"textures": {
  "1": "poro:item/weapons/scythe_adventurer_poro_01",
  "particle": "poro:item/weapons/scythe_adventurer_poro_01"
}
```

---

## 5. netherite_hoe.json 변경 계획 (Phase 5)

```json
{
  "model": {
    "type": "minecraft:select",
    "property": "minecraft:custom_model_data",
    "cases": [
      {
        "when": "100501",
        "model": { "type": "minecraft:model", "model": "poro:item/weapons/scythe_carnivoret_poro_01" }
      },
      {
        "when": "100502",
        "model": { "type": "minecraft:model", "model": "poro:item/weapons/scythe_adventurer_poro_01" }
      }
    ],
    "fallback": { "type": "minecraft:model", "model": "minecraft:item/netherite_hoe" }
  }
}
```

> 100501 (scythe_carnivoret_poro_01) 절대 보존.

---

## 6. CustomModelData 등록

| 항목 | 값 |
|---|---|
| **Poro 배정 CMD** | **100502** |
| material | NETHERITE_HOE |
| namespace | poro |
| model_path | `poro:item/weapons/scythe_adventurer_poro_01` |
| texture_path | `poro:item/weapons/scythe_adventurer_poro_01` |

---

## 7. display 설정 계획 (Phase 5에서 확정)

Carnivoret 낫(100501) display 값을 기준으로 Phase 5에서 적용, 인게임 테스트 후 scale 확정.

| 뷰 | 계획 기준값 (Carnivoret 낫 참고) |
|---|---|
| firstperson_righthand | rotation [6, 0, 0], translation [4, 4.75, 0], scale [0.457, 0.457, 0.457] |
| firstperson_lefthand | rotation [6, 0, 0], translation [4, 4.75, 0], scale [0.457, 0.457, 0.457] |
| thirdperson_righthand | translation [0, 1.75, 1.75], scale [0.8, 0.8, 0.8] |
| thirdperson_lefthand | translation [0, 1.75, 1.75], scale [0.8, 0.8, 0.8] |
| gui | rotation [90, 45, -90], translation [-2, -1.5, 0], scale [0.534, 0.534, 0.534] |
| ground | rotation [-45, 0, 0], translation [0, 6, 0], scale [0.6, 0.6, 0.6] |
| head | scale [0, 0, 0] |
| fixed | rotation [0, -90, 0], translation [0, 0.75, -1.75], scale [2, 2, 2] |

---

## 8. 전체 체크리스트

### Phase 0 (완료)
- [x] 폴더 구조 생성
- [x] 플레이스홀더 파일 생성

### Phase 1 (완료)
- [x] 레퍼런스 이미지 `original/` 배치
- [x] 레퍼런스 분석 (texture_ref + ref 비교)
- [x] asset_brief.md 완성 (파츠 구조, 색상 팔레트, 품질 게이트)
- [x] ref_spec.json 완성 (13 elements, 좌표 계획, 텍스처 존)
- [x] spec.yaml 업데이트 (design_ready)
- [x] mapping_plan.md 업데이트

### Phase 2 (다음)
- [ ] `bb-asset-brief` — ref_spec.json 좌표 최종 확인

### Phase 3 (bb-build-blockout, MCP 필수) ✓ 완료 2026-05-09 [재구성]
- [x] Blockbench MCP로 .bbmodel 제작 (10 elements, 5 groups) — texture-first 전환
- [x] blade stepped-cube(4ea) → blade_plane_main(1ea) + 투명 PNG alpha 방식
- [x] 블록아웃 품질 게이트 통과
- [x] source/scythe_adventurer_poro_01.bbmodel 저장
- [x] textures/scythe_adventurer_poro_01.png 64x64 초안 저장 (초승달 blade + 4존 팔레트)

### Phase 4 (bb-texture-pass) ✓ 완료 2026-05-09
- [x] 4존 팔레트 적용 (A:날강철 / B:어두운철 / C:목재 / D:가죽)
- [x] textures/scythe_adventurer_poro_01.png 최종 제작
- [x] .bbmodel 텍스처 내장

### Phase 5 (bb-export-register) ✓ 완료 2026-05-09
- [x] export/resourcepack 배치 (model json + texture)
- [x] netherite_hoe.json 100502 조건 추가 (100501 보존)
- [x] weapon_cosmetic_registry.yml 등록 (status: export_ready)
- [ ] 인게임 테스트: `/minecraft:give @s netherite_hoe[custom_model_data={strings:["100502"]}]`
- [ ] status → active
