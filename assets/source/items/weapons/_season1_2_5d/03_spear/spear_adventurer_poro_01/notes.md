# spear_adventurer_poro_01 — 작업 노트

## 진행 상태 체크리스트

### Phase 1 — 레퍼런스 정리
- [x] 레퍼런스 이미지 수령 (spear_adventurer_poro_01_ref.png)
- [x] original/ 배치

### Phase 2 — 설계 문서 + seed .bbmodel
- [x] asset_brief.md 작성 (파츠 구조, 색상 팔레트, 품질 게이트)
- [x] ref_spec.json 작성 (Blockbench 좌표 계획, 11 elements)
- [x] spec.yaml 작성
- [x] notes.md 작성
- [x] mapping_plan.md 작성
- [x] seed .bbmodel 생성 (6 groups / 11 elements / display 설정 포함)

### Phase 3 — Blockbench 수동/MCP 편집
- [x] source/spear_adventurer_poro_01.bbmodel을 Blockbench에서 열기
- [x] 파츠 형태 조정 및 실루엣 완성 (MCP: 14 elements, 4-step 창날 + hook guard)
- [x] 블록아웃 품질 게이트 통과 (geometry 승인됨 2026-05-09)

### Phase 4 — 텍스처 draft (완료)
- [x] textures/spear_adventurer_poro_01.png 생성 (64×64, 4-zone material draft, ref 색감 반영)
- [x] .bbmodel 텍스처 내장 (base64 embedded source)
- [x] 전체 14 elements UV 연결 확인 (tex=0, zone별 UV 정상)
- [x] notes.md 재질 구역 문서화

### Phase 4.5 — 텍스처 polish (완료, 2026-05-09)
- [x] 8×8 UV 샘플 영역 픽셀 단위 재설계 (zone별 상세 명암)
- [x] spearhead: 다이아몬드형 중앙 하이라이트 (#6C7680→#E0E8F0), 외곽 어두운 엣지
- [x] dark metal: 2단 명암 (#2E3440→#7A8490), 상단/중앙 하이라이트
- [x] wood shaft: 다크 엣지+라이트 센터 원통감, 수평 결 주기 배치
- [x] leather grip: 5톤 대각선 랩 패턴 (↘ 방향)
- [x] Blockbench Paint 모드 시각 확인 완료 (재질 구분 정상 렌더 확인)

### Phase 4.6 — geometry connection fix (완료, 2026-05-09)
- [x] 문제 진단: shaft_collar(Y=20-22)↔spearhead_lower(Y=23-26) 사이 Y=22-23 1유닛 gap
- [x] spear_socket 신규 추가: Y=22-23, X=6-10, Z=7.5-8.5 (Zone B dark metal UV)
- [x] wooden_shaft 그룹에 배치, UV tex=0 정상 연결
- [x] Paint 모드 시각 확인: 창날→소켓→가드→자루 끊김 없이 연결 확인
- [x] 총 element 수: 14 → 15개
- [x] spear_adventurer_poro_01.json 및 .png export
- [x] source/ 및 textures/ 배치

### Phase 5 — resourcepack 반영 + 등록 (완료, 2026-05-09)
- [x] export/resourcepack 배치 (model json + texture)
- [x] trident.json 100302 추가 (100301 절대 보존)
- [x] weapon_cosmetic_registry.yml 등록 (status: export_ready)
- [ ] 인게임 테스트 통과
  - `/minecraft:give @s minecraft:trident[minecraft:custom_model_data={strings:["100302"]}] 1`
- [ ] status → active

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | spear_adventurer_poro_01 |
| display_name | 모험가의 창 |
| CMD | 100302 |
| material | TRIDENT |
| rarity | starter |
| pipeline | blockbench_new_build |
| 레퍼런스 | spear_adventurer_poro_01_ref.png |
| 목표 elements | 11 |
| 실제 elements | 15 (Phase 4.6 이후) |
| 텍스처 | textures/spear_adventurer_poro_01.png (64×64, draft) |

## 파츠 설계 요약

| 파트 | elements | 역할 |
|---|---|---|
| spearhead_tip | 1 | 극세 포인트 (1px) |
| spearhead_upper | 1 | 날 상단 (2px) |
| spearhead_lower | 1 | 날 하단 (4px, 테이퍼) |
| lug_left | 1 | 좌측 갈고리 날개 |
| lug_right | 1 | 우측 갈고리 날개 |
| shaft_collar | 1 | 금속 결합 밴드 |
| shaft_upper | 1 | 상단 목제 자루 |
| grip_wrap_front | 1 | 감긴 그립 앞 |
| grip_wrap_back | 1 | 감긴 그립 뒤 |
| shaft_lower | 1 | 하단 목제 자루 |
| butt_cap | 1 | 소형 하단 금속 캡 |

**총 11 elements**

## 레퍼런스 분석 메모

- 루그(날개 갈고리)가 이 창의 가장 강한 실루엣 포인트 — 반드시 식별 가능해야 함
- 그립 감기는 색상(어두운 갈색)과 폭(약간 두텁게)으로 자루와 구분
- Carnivoret 창(30 elements) 대비 11 elements로 단순화 — starter 등급 시각 차이 의도적

## Carnivoret 창 display 참고값

| 뷰 | rotation | translation | scale |
|---|---|---|---|
| firstperson_right | [6, 0, 0] | [3.5, 2, 0] | [0.463, 0.463, 0.463] |
| thirdperson_right | — | [0, 1.5, 1.75] | [0.85, 0.85, 0.85] |
| gui | [90, 45, -90] | [-3.25, -3.75, 0] | [0.610, 0.610, 0.610] |
| ground | [-45, 0, 0] | [0, 6, 0] | [0.6, 0.6, 0.6] |
| head | — | — | [0, 0, 0] |

> Phase 5 display 기준으로 활용, 실제 scale은 인게임 테스트 후 확정

## 텍스처 draft 재질 구역 (64×64)

| Zone | X 범위 | 색상 | 적용 파츠 |
|------|--------|------|-----------|
| A — 창날 금속 | 0–15 | #BCC6CC (pale steel) | spearhead_tip/step2/step3/lower |
| B — 어두운 금속 | 16–31 | #4E5860 (dark iron) | lug×4, shaft_collar, butt_cap |
| C — 목재 | 32–47 | #663E1A (oak brown) | shaft_upper, shaft_lower |
| D — 가죽 | 48–63 | #371E0C (dark leather) | grip_wrap_front, grip_wrap_back |

UV 방식: box_uv=false, per-face UV [zone_x+2, 2, zone_x+10, 10] (8×8 patch, solid color)

> 최종 텍스처 품질은 Blockbench Paint 모드 또는 외부 픽셀 아트 편집에서 처리.
> MCP 뷰포트 스크린샷은 텍스처 렌더를 반영 못함 — Paint 탭 전환 후 수동 확인 필요.

## 이슈 로그

- Phase 3: spearhead_upper → spearhead_step2 리네임, spearhead_step3 신규, lug hook 2개 신규 추가
- Phase 4 draft: Blockbench fromPath() EISDIR 오류로 data URL 방식으로 텍스처 로드
- 텍스처 path 필드는 None(data URL 로드 시 정상) — source(base64) 내장으로 .bbmodel 자급
