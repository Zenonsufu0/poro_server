# scythe_adventurer_poro_01 — 작업 노트

## Phase 0 — 스캐폴드 생성 (완료)

- [x] 폴더 구조 생성 (original/, source/, textures/, export/resourcepack/)
- [x] asset_brief.md 생성 (Phase 0 플레이스홀더)
- [x] spec.yaml 생성 (scaffold_ready 상태)
- [x] ref_spec.json 생성 (Phase 0 플레이스홀더)
- [x] notes.md / mapping_plan.md 생성
- [x] source/scythe_adventurer_poro_01.bbmodel 생성 (빈 플레이스홀더)
- [x] textures/scythe_adventurer_poro_01.png 생성 (64×64 플레이스홀더)
- [x] export/.../scythe_adventurer_poro_01.json 생성 (플레이스홀더)
- [x] export/.../scythe_adventurer_poro_01.png 생성 (플레이스홀더)

## Phase 1 — 레퍼런스 정리 및 설계 (완료)

- [x] 레퍼런스 이미지 `original/` 배치
  - `original/scythe_adventurer_poro_01_ref.png` (실사풍 3D 보조 레퍼런스)
  - `original/scythe_adventurer_poro_01_texture_ref.png` (Minecraft 픽셀아트 주 레퍼런스)
- [x] 레퍼런스 분석 — 실루엣, 비율, 파츠 구분, 색상 팔레트
- [x] asset_brief.md 완성 (파츠 13 elements, 색상 팔레트, 품질 게이트)
- [x] ref_spec.json 완성 (Blockbench 좌표 계획, 텍스처 존 4구역)
- [x] spec.yaml 업데이트 (design_ready, target_elements:13)
- [x] mapping_plan.md 완성 (Phase 2~5 계획 포함)
- [x] notes.md 업데이트

## Phase 2 — 설계 확정 (다음 단계)

- [ ] `bb-asset-brief` — ref_spec.json 좌표 최종 검토
- [ ] Phase 3 진입 준비 확인

## Phase 3 — Blockbench 블록아웃 (bb-build-blockout, MCP 필수)

- [ ] Blockbench MCP로 .bbmodel 제작
  - 13 elements / 5 groups
  - blade(4) + socket(3) + shaft(2) + grip(2) + hardware(2)
  - Y범위 -2~38, 날 X방향 확장 (stepped crescent)
- [ ] 블록아웃 품질 게이트 통과
- [ ] source/scythe_adventurer_poro_01.bbmodel 저장

## Phase 4 — 텍스처 (bb-texture-pass)

- [ ] 4존 팔레트 적용 (A:날강철 / B:어두운철 / C:목재 / D:가죽)
- [ ] textures/scythe_adventurer_poro_01.png 최종 제작 (64×64)
- [ ] .bbmodel 텍스처 내장 (base64 embedded)
- [ ] 전체 elements UV 연결 확인

## Phase 5 — Export + 등록 (bb-export-register)

- [ ] export/resourcepack 배치 (model json + texture)
- [ ] netherite_hoe.json 100502 추가 (100501 절대 보존)
- [ ] weapon_cosmetic_registry.yml 등록 (status: export_ready)
- [ ] 인게임 테스트
  - `/minecraft:give @s netherite_hoe[custom_model_data={strings:["100502"]}]`
- [ ] status → active

---

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | scythe_adventurer_poro_01 |
| display_name | 모험가의 낫 |
| CMD | 100502 (예정) |
| material | NETHERITE_HOE |
| rarity | starter |
| pipeline | blockbench_new_build |
| 목표 elements | 13 |
| 텍스처 | 64×64 (4존 구조) |

## 레퍼런스 분석 메모

### 주 레퍼런스 (texture_ref — 픽셀아트)
- 전형적인 낫 형태: 직선 자루 + 상단 소켓 브래킷 + 우상향 초승달 날
- 날 커브가 이 무기의 가장 강한 실루엣 포인트 — base(넓음)→tip(좁음)이 필수
- 소켓 브래킷이 자루-날 경계를 명확히 분리 — 구조물로서 식별 가능해야 함
- 그립 대각선 감기 패턴: 어둡고 자루보다 약간 넓음

### Carnivoret 낫 display 참고값

| 뷰 | rotation | translation | scale |
|---|---|---|---|
| firstperson | [6, 0, 0] | [4, 4.75, 0] | [0.45742, 0.45742, 0.45742] |
| thirdperson | — | [0, 1.75, 1.75] | [0.8, 0.8, 0.8] |
| gui | [90, 45, -90] | [-2, -1.5, 0] | [0.53375, 0.53375, 0.53375] |
| ground | [-45, 0, 0] | [0, 6, 0] | [0.6, 0.6, 0.6] |
| head | — | — | [0, 0, 0] |
| fixed | [0, -90, 0] | [0, 0.75, -1.75] | [2, 2, 2] |

> Phase 5 display 기준. 실제 scale은 인게임 테스트 후 확정.

## 후속 검토 항목

### NETHERITE_HOE 우클릭 상호작용
- NETHERITE_HOE material은 흙 블록 우클릭 시 경작지 변환 동작 발생
- 실제 서버 운영 시 EmpireRPG에서 right-click 이벤트 intercept 또는 커스텀 material 대체 방안 검토 필요
- 검토 시점: 낫 전투 스킬 구현 단계

## 이슈 로그

(없음)
