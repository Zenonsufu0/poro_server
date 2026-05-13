# 003_holy_wing_small Design Spec

Reference source: `refs/holy_wing.png`.

## 1. Overall Proportions

Minecraft diamond sword급 스케일의 축약형 성검. 카테고리: sword (§8.0). 총 높이 16px.

실루엣: 세로 단일 축 위주. 블레이드가 전체 높이의 62.5%(10/16)를 차지하는 블레이드-지배형 비율 — 다이아몬드 소드(~66%)와 유사하여 인벤토리에서 어색하지 않음. 크로스가드 날개는 guard_bar 3px 확장으로만 암시(별도 큐브 없음), 가드 전체 폭 최대 8px(guard_center 2 + bar 3 × 양쪽). 그립은 짧은 3px 다크 컬럼. 폼멜이 3px 정방형으로 하단을 마감.

레퍼런스 Holy Wing 대비 축약 결정:
- 원본: 32-48px 대형 크로스가드 날개 → 축약: 3px guard_bar 확장으로 날개 "암시"
- 원본: 다층 날개 큐브 → 축약: guard_bar_r/l 단일 큐브 2개(Mirror)
- Pale holy 정체성(화이트·골드·블루 톤) 유지, 어두운 팔레트 사용 금지

## 2. Part List

| Part | 역할 | 비고 |
|---|---|---|
| blade_tip | 칼날 최상단 2px | 화이트 하이라이트 집중 |
| blade_upper | 칼날 상단 3px | 골드·화이트 혼합 |
| blade_mid | 칼날 중단 3px | 메인 골드 베이스 |
| blade_lower | 칼날 하단 2px | 가드 접합부, 짙은 골드 |
| blade_core | 칼날 중앙 골드 코어 세로 관통 | blade_lower~blade_upper 내부 중앙, 1px 폭 |
| guard_center | 크로스가드 중심부 | 보석 소켓 포함 |
| guard_bar_r | 가드 오른쪽 윙 확장 바 | Blockbench Mirror source |
| guard_bar_l | 가드 왼쪽 윙 확장 바 | guard_bar_r Mirror 결과 |
| guard_center_gem | 가드 중앙 파란 보석 | guard_center 전면 flush 임베드 |
| grip | 손잡이 | 다크 네이비 단색 |
| pommel | 폼멜 | 하단 마무리 볼륨 |
| pommel_gem | 폼멜 중앙 보석 | pommel 전면 flush 임베드 |

## 3. Symmetry Rules

- 대칭 축: X축 좌우 대칭
- **Blockbench Mirror 사용 그룹**: `guard_bar_r` (X ≥ 0 먼저 제작 → Mirror로 `guard_bar_l` 생성)
- **단일 큐브 시각 대칭 그룹** (홀수 폭 파트는 off-center 허용):
  `blade_tip`, `blade_upper`, `blade_mid`, `blade_lower`, `blade_core`,
  `guard_center`, `grip`, `pommel`, `pommel_gem`
- 홀수 폭(3px) 파트(`guard_center`, `pommel`)는 X=-1~X=2 배치, 시각 중심 X=0.5 — 0.5px 비대칭 허용. 16px 인벤토리 스케일에서 시각적으로 무시 가능.
- 짝수 폭(2px) 파트(`blade`, `grip`)는 X=-1~X=1, 완전 대칭.
- 빌드 방향: X ≥ 0 먼저, X < 0 는 Mirror 후처리

## 4. Color Palette

`refs/holy_wing.png` 팔레트 시트 직접 전사. 해석 없음.

| Role | Hex | 사용 위치 |
|---|---|---|
| White Highlight | `#F2F2F2` | blade_tip, blade_upper 엣지 하이라이트 |
| Silver Gray | `#C8CCD4` | blade 측면 보조 톤 |
| Golden Yellow Core | `#FFD54D` | blade_core 전체, blade 내부 중앙 픽셀 |
| Warm Orange Glow | `#FF9E1A` | blade_lower 하단 1px AO 라인 (1px 사용 한정) |
| Main Gold | `#E0B347` | blade_mid, blade_upper 베이스, guard_center, guard_bar, pommel 베이스 |
| Dark Gold AO | `#9C6B16` | blade_lower, guard 하단 음영, pommel 음영 |
| Deep Blue | `#1E3A8A` | guard_center_gem 베이스, pommel_gem 베이스 |
| Bright Blue Gem | `#3DBBFF` | guard_center_gem 하이라이트, pommel_gem 하이라이트 |
| Dark Navy | `#0D234A` | grip 전체, guard_center 측면 어두운 면 |

## 5. Thickness Guide

모든 두께 1~2px 수준 (16px 스케일). 단, pommel만 3px depth로 하단 볼륨감 확보.

| Part | Height (Y, px) | Width (X, px) | Depth (Z, px) |
|---|---|---|---|
| blade_tip | 2 | 2 | 2 |
| blade_upper | 3 | 2 | 2 |
| blade_mid | 3 | 2 | 2 |
| blade_lower | 2 | 2 | 2 |
| blade_core | 8 | 1 | 1 |
| guard_center | 2 | 2 | 2 |
| guard_bar_r | 2 | 3 | 2 |
| guard_bar_l | 2 | 3 | 2 |
| guard_center_gem | 1 | 1 | 1 |
| grip | 3 | 2 | 2 |
| pommel | 1 | 3 | 3 |
| pommel_gem | 1 | 1 | 1 |

## 6. Construction Phases

### Phase 0 — 텍스처 생성
- `holy_wing_tex.png` (16×16 정사각형) 1장 생성
- §4의 9개 hex 팔레트 배치: `#F2F2F2`, `#C8CCD4`, `#FFD54D`, `#FF9E1A`, `#E0B347`, `#9C6B16`, `#1E3A8A`, `#3DBBFF`, `#0D234A`
- `source/textures/holy_wing_tex.png` 에 저장. 사용자가 Blockbench에 수동 임포트.
- 목표: UV 페인팅 전 팔레트 잠금

### Phase 1 — 기본 막대 골격
- `blade_lower`: 2H × 2W × 2D, from=[-1, 6, -1] to=[1, 8, 1]
- `guard_center`: 2H × 2W × 2D, from=[-1, 4, -1] to=[1, 6, 1]
- `grip`: 3H × 2W × 2D, from=[-1, 1, -1] to=[1, 4, 1]
- `pommel`: 1H × 3W × 3D, from=[-1, 0, -1] to=[2, 1, 2]
- 검증: 4큐브, 높이 합산 2+2+3+1=8px (하단 절반), 실루엣 확인

### Phase 2 — 칼날 분할 + 가드 바 확장
- `blade_mid`: 3H × 2W × 2D, from=[-1, 8, -1] to=[1, 11, 1]
- `blade_upper`: 3H × 2W × 2D, from=[-1, 11, -1] to=[1, 14, 1]
- `blade_tip`: 2H × 2W × 2D, from=[-1, 14, -1] to=[1, 16, 1]
- `guard_bar_r`: 2H × 3W × 2D, from=[1, 4, -1] to=[4, 6, 1]
- `guard_bar_l`: Blockbench Mirror of guard_bar_r → from=[-4, 4, -1] to=[-1, 6, 1]
- 검증: 9큐브, 총 높이 16px, 가드 윙 힌트(폭 ~8px) 확인

### Phase 3 — 내부 코어 + 보석 소켓
- `blade_core`: 8H × 1W × 1D, from=[0, 6, 0] to=[1, 14, 1] (blade_lower~blade_upper 내부 관통)
- `guard_center_gem`: 1H × 1W × 1D, from=[0, 4, 1] to=[1, 5, 2] (guard_center 전면 중앙, Z=1 flush)
- 검증: 11큐브, blade_core가 blade 범위 내부에 완전히 포함되는지 확인

### Phase 4 — 폼멜 보석 + 마무리
- `pommel_gem`: 1H × 1W × 1D, from=[0, 0, 2] to=[1, 1, 3] (pommel 전면 중앙, Z=2 flush)
- 최종 큐브 수: 12개 (target 12-18 ✓, ceiling 20 ✓)
- §8.7 attachment graph 기준 부동 큐브 없는지 재확인

### Phase 5 — 텍스처 페인팅
- UV 모드: box UV, `holy_wing_tex` 16×16 공유
- 페인팅 순서:
  1. blade: `#E0B347` 베이스 → `#F2F2F2` 엣지 하이라이트 → `#FFD54D` 코어 중앙 → `#9C6B16` AO 하단
  2. blade_core: `#FFD54D` 전면 + `#FF9E1A` blade_lower 접합부 1px
  3. guard: `#E0B347` 전면 → `#9C6B16` 하단 음영 → `#0D234A` 측면
  4. gems: `#1E3A8A` 베이스 → `#3DBBFF` 하이라이트 1px
  5. grip: `#0D234A` 전체
  6. pommel: `#E0B347` 베이스 → `#9C6B16` AO

## 7. Must-Not-Break Constraints

1. **총 높이 16px 고정** — §8.0 14-16px range 준수. 어떤 파트 추가/제거도 이 수치를 변경할 수 없음.
2. **Pale Holy 정체성** — 어두운 금속·저채도 팔레트 절대 금지. §4의 9색이 유일 허용 팔레트. Dark Gold(`#9C6B16`)는 AO 음영 용도 한정.
3. **모든 텍스처 정사각형 필수** — java_block_item 포맷에서 비정사각형 텍스처는 animated로 오인식. `holy_wing_tex` 16×16 이외 비율 사용 금지.
4. **날개 표현은 guard_bar 확장만** — `wing_tip`, `wing_feather` 등 독립 날개 큐브 추가 금지. 날개 실루엣은 `guard_bar_r`/`guard_bar_l` (3px 확장)으로만 암시.
5. **부동 큐브 금지** — 모든 큐브는 §8.7 attachment graph 연결 구조 준수. `guard_center`가 root이며 모든 파트가 연결 체인을 형성해야 함.
6. **색상 모자이크 배열 금지** — §4 외 색 픽셀 삽입 불가. Warm Orange(`#FF9E1A`)는 blade_lower 1px 라인 한정.
7. **blade_core 내부 관통 유지** — blade_core는 blade 범위([Y=6, Y=14]) 안에서만 존재. blade 외부 돌출 금지.

## 8. Technical Constraints (non-negotiable)

### 8.0 Fixed from variant creation
- Minecraft Format: java_block_item
- Item category: sword
- Category-implied scale range: Total height 14–16 px, Cube budget 10–20 cubes, Texture strategy 1× 16×16 square
- This section is immutable. Changing format or category requires a new variant folder.

### 8.1 Cube budget
- Target range: 12–18 cubes
- Hard ceiling: 20 cubes
- 현재 설계: 12 cubes (Phase 4 완료 기준, 10-20 range ✓)

### 8.2 Absolute pixel dimensions
- **Total height: 16 px** (14–16 range ✓)
- 높이 합산: blade(2+3+3+2) + guard(2) + grip(3) + pommel(1) = 10+2+3+1 = **16 px ✓**
- Per-part dimensions (H × W × D, integers in pixels):

| Part | H | W | D |
|---|---|---|---|
| blade_tip | 2 | 2 | 2 |
| blade_upper | 3 | 2 | 2 |
| blade_mid | 3 | 2 | 2 |
| blade_lower | 2 | 2 | 2 |
| blade_core | 8 | 1 | 1 |
| guard_center | 2 | 2 | 2 |
| guard_bar_r | 2 | 3 | 2 |
| guard_bar_l | 2 | 3 | 2 |
| guard_center_gem | 1 | 1 | 1 |
| grip | 3 | 2 | 2 |
| pommel | 1 | 3 | 3 |
| pommel_gem | 1 | 1 | 1 |

### 8.3 Texture strategy
- UV mode: **box** (java_block_item + sword 표준)
- Texture list:
  - `holy_wing_tex` **16×16** (정사각형 ✓) — 모든 파트가 공유하는 단일 통합 텍스처
    - 용도: blade/guard/grip/pommel 전 파트 UV 공유
    - Palette binding: `#F2F2F2`, `#C8CCD4`, `#FFD54D`, `#FF9E1A`, `#E0B347`, `#9C6B16`, `#1E3A8A`, `#3DBBFF`, `#0D234A` (§4 9색 전부)
- Aspect ratio constraint:
  - Format = `java_block_item`: 모든 텍스처 MUST be square. 16×16 고정. 비정사각형 → HARD FAIL.
- Forbidden patterns:
  - no 1×1 single-color textures
  - no one-texture-per-material silhouette approach
  - no colors outside §4 palette
  - no non-square textures (animated 오인식 회피)
  - no interpretive palette substitution (e.g. §4가 pale holy이므로 §8.3에서 dark metal 지정 불가)

### 8.4 Coordinate rules
- All `from` and `to` values are integers (Blockbench pixel grid)
- `inflate` allowed within ±0.05 to prevent z-fighting

### 8.5 Mirror strategy
- Mirror axis: **X**
- Blockbench Mirror 사용 그룹: `guard_bar_r` → `guard_bar_l` 자동 생성
- 단일 큐브 시각 대칭 그룹 (홀수 폭 off-center 허용):
  `blade_tip`, `blade_upper`, `blade_mid`, `blade_lower`, `blade_core`,
  `guard_center`, `grip`, `pommel`, `pommel_gem`
- 빌드 방향: X ≥ 0 먼저, X < 0 Mirror 후처리

### 8.6 Group origin rules

| Part | Origin [x, y, z] | 근거 |
|---|---|---|
| blade_tip | [0, 14, 0] | blade_upper 상단 |
| blade_upper | [0, 11, 0] | blade_mid 상단 |
| blade_mid | [0, 8, 0] | blade_lower 상단 |
| blade_lower | [0, 6, 0] | guard_center 상단 접합점 |
| blade_core | [0, 6, 0] | blade_lower와 동일 기준점 (내부 관통) |
| guard_center | [0, 4, 0] | grip 상단 접합점 |
| guard_bar_r | [1.5, 5, 0] | guard_center 우측 가운데 (Mirror pivot) |
| guard_bar_l | [-1.5, 5, 0] | guard_bar_r Mirror 결과 |
| guard_center_gem | [0, 5, 0] | guard_center 전면 중앙 |
| grip | [0, 1, 0] | pommel 상단 접합점 |
| pommel | [0, 0, 0] | 하단 기준점 |
| pommel_gem | [0, 0.5, 0] | pommel 전면 중앙 |

### 8.7 Physical attachment graph

- `blade_lower`: attached_to=[`guard_center`.top_face]
- `blade_mid`: attached_to=[`blade_lower`.top_face]
- `blade_upper`: attached_to=[`blade_mid`.top_face]
- `blade_tip`: attached_to=[`blade_upper`.top_face]
- `blade_core`: attached_to=[`blade_lower`.internal ~ `blade_upper`.internal (Y=6–14 내부 관통, 양 끝이 blade_lower.bottom_face 위, blade_upper.top_face 아래에 포함)]
- `guard_center`: root structural part (상위 부착 없음)
- `guard_bar_r`: attached_to=[`guard_center`.right_face (X=1 접합)]
- `guard_bar_l`: attached_to=[`guard_center`.left_face (X=-1 접합, mirror)]
- `guard_center_gem`: attached_to=[`guard_center`.front_face (Z=1, flush embed)]
- `grip`: attached_to=[`guard_center`.bottom_face]
- `pommel`: attached_to=[`grip`.bottom_face]
- `pommel_gem`: attached_to=[`pommel`.front_face (Z=2, flush embed)]

---

## Self-Check Log

- [x] 모든 `[TBD]` 제거됨
- [x] §8.3 palette binding 9개 hex → 전부 §4에 존재 (`#F2F2F2`, `#C8CCD4`, `#FFD54D`, `#FF9E1A`, `#E0B347`, `#9C6B16`, `#1E3A8A`, `#3DBBFF`, `#0D234A`)
- [x] 모든 hex 6자리 대문자 형식 확인
- [x] §1 "pale holy" ↔ §4 화이트·골드·블루 밝은 톤 일치
- [x] §8.2 총 높이 = 16px → §8.0 14–16px range ✓
- [x] §8.3 `holy_wing_tex` 16×16 정사각형 → java_block_item ✓
- [x] §2 12개 파트 → §5 ✓, §6 ✓, §8.6 ✓, §8.7 ✓ 모두 참조
- [x] §8.7에서 §2에 없는 파트 참조 없음
- [x] §5 두께값이 §8.2 치수와 모순 없음
