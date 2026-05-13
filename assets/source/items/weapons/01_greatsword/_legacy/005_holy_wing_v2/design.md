# 005_holy_wing_v2 Design Spec

Reference source: `refs/holy_wing.png`.
Concept: 레퍼런스의 Holy Wing 실루엣·파츠 구조를 그대로 유지하되, 색상을 전면 흑철·진홍 계열로 교체한 "타락한 성검" 버전.
골드/화이트 → 근-흑철, 크림슨 글로우, 다크 루비 젬.

---

## 1. Overall Proportions

- 총 높이: **44px** (Y 0 = 폼멜 바닥, Y 44 = 날 끝)
- 수직 구역:

  | 구역 | Y 범위 | 높이 |
  |------|--------|------|
  | Pommel | 0 – 4 | 4px |
  | Grip | 4 – 12 | 8px |
  | Guard + Wing zone | 12 – 16 | 4px |
  | Blade lower | 16 – 22 | 6px |
  | Blade mid | 22 – 34 | 12px |
  | Blade upper | 34 – 42 | 8px |
  | Blade tip | 42 – 44 | 2px |

- 최대 폭: **14px** (X −7 ~ X 7), 날개 스텝 끝에서 측정.
- 블레이드 폭: 4px (X −2 ~ X 2).
- 가드 중심 폭: 6px (X −3 ~ X 3).
- 레퍼런스의 전면/후면 뷰를 실루엣 기준으로 사용. 측면 뷰는 두께 가이드 전용.
- 블레이드가 전체 높이의 약 64% 차지 (28px). 그립+폼멜이 27%, 가드 존이 9%.
- 날개가 가장 넓은 지점. 날개 스팬은 가드 폭의 약 2.3배.

---

## 2. Part List

Blockbench 아웃라이너 그룹 이름 기준:

| 파츠 이름 | 설명 |
|-----------|------|
| `blade_tip` | 날 끝 스텝형 뾰족 캡. 폭 2px, 높이 2px. |
| `blade_upper` | 상단 날 본체. 폭 4px, 깊이 2px. |
| `blade_mid` | 중단 날 본체. 폭 4px, 깊이 2px. |
| `blade_lower` | 하단 날 본체. 폭 4px, 깊이 2px. |
| `blade_core` | 날 중심선을 따라 세로로 뻗은 진홍 코어 스트립. |
| `blade_core_glow` | 코어 하단부의 크림슨 글로우 확장 블록. |
| `blade_edge_r` / `blade_edge_l` | 날 외곽 챔퍼 스트립. 각 1px 폭, 어두운 챠콜. |
| `blade_base_socket` | 날이 가드에 꽂히는 소켓. Y 14–16, guard_center 내 중첩 배치. |
| `guard_center` | 가드 중심 흑철 바디. 젬 전면/후면 포함. |
| `guard_center_gem` | 가드 정면·후면의 다크 루비 젬 페이스. |
| `guard_bar_r` | 가드 우측 면에 부착되는 날개 앵커 아암. 축소 버전(1px 폭, Y 13–15). 오른쪽만 빌드, 미러. |
| `wing_r_step1` ~ `wing_r_step4` | 4단 계단형 날개 페더. 오른쪽만 빌드, 미러. |
| `lower_talon_r` | 가드 하단 양측의 하향 클로 액센트. 오른쪽만 빌드, 미러. |
| `handle_grip` | 그립 본체. 직사각형, 근-흑색. |
| `handle_ribbing` | 그립 위 3개 띠 (Y 5, 8, 11). 다크 블루-블랙. |
| `pommel_gold` | 폼멜 본체. 흑철, 그립보다 넓음. |
| `pommel_gem` | 폼멜 전면·후면 루비 젬. |

※ `wing_joint_r`는 새 44px 스케일에서 `guard_bar_r`에 통합됨. 별도 큐브 없음.

---

## 3. Symmetry Rules

- **Blockbench Mirror 사용 (X 축)**: blade_edge, wing, guard_bar, lower_talon — 오른쪽(x ≥ 0)만 빌드 → Mirror로 왼쪽 생성.
- **시각적 대칭 (단일 큐브)**: grip, pommel — 홀수 폭으로 X=0 완전 대칭 불가. off-center 배치로 단일 큐브 제작 (Mirror 기능 사용 안 함).
- 블레이드 중심선(X = 0)은 폼멜 중심 → 그립 중심 → 가드 젬 → 코어 → 날 끝까지 일직선.
- 전면과 후면은 젬·코어·가드·그립·폼멜 실루엣이 동일해야 함.
- 날개 페더는 단수·높이 스텝·두께가 양쪽 동일. 한 쪽이 더 무겁게 보이면 안 됨.

---

## 4. Color Palette

| 역할 | 색상 코드 | 사용 위치 |
|------|-----------|-----------|
| White / Light Gray | `#F2F2F2` | blade 본체, wing 페더 베이스 |
| Silver Gray | `#C8CCD4` | blade 베벨 스트립, 은은한 섀도우 |
| Golden Yellow (Core) | `#FFD54D` | blade_core 본체 |
| Warm Orange Glow | `#FF9E1A` | blade_core 외곽 글로우 1~2px |
| Main Gold | `#E0B347` | guard_center, guard_bar, pommel, lower_talon |
| Dark Gold (AO) | `#9C6B16` | 가드 하단·조인트 언더사이드 섀도우 |
| Deep Blue | `#1E3A8A` | wing_joint_r/l, handle_ribbing |
| Bright Blue Gem | `#3DBBFF` | guard_center_gem, pommel_gem |
| Dark Navy | `#0D234A` | handle_grip 베이스 |

배치 원칙:
- 블레이드와 날개는 멀리서 흰색으로 읽혀야 함. 실버 그레이는 베벨/섀도우 구조로만.
- 골드가 가드, 폼멜, 하단 탤론, blade_core 를 지배.
- 오렌지는 코어 외곽의 얇은 글로우, 넓은 채움 금지.
- 블루는 보석, 윙 조인트, 그립 밴드에 집중.
- 모자이크 배열 금지. 의도된 밴드, 베벨 스트립, 젬 하이라이트, 스텝 섀도우 사용.

---

## 5. Thickness Guide

새 스케일 기준 (최대 Z: 가드 3px, 블레이드 2px).

| 파츠 | 두께 (Z 축) | 좌표 범위 | 비고 |
|------|------------|-----------|------|
| blade_tip | 2px | Z −1 to 1 | 매우 슬림 |
| blade_upper | 2px | Z −1 to 1 | |
| blade_mid | 2px | Z −1 to 1 | 신규 파츠 (§8.2 참조) |
| blade_lower | 2px | Z −1 to 1 | 코어가 Z 0~1로 약간 돌출 |
| blade_core | 2px | Z −1 to 1 | 블레이드 페이스에 flush |
| blade_core_glow | 2px | Z −1 to 1 | 블레이드 앞면에 붙음 |
| blade_edge_r / blade_edge_l | 2px | Z −1 to 1 | 날 본체와 동일 깊이 |
| blade_base_socket | 2px | Z −1 to 1 | 가드 상단 전환부, guard_center 내 중첩 |
| guard_center | 3px | Z −1 to 2 | 최대 두께 지점, 비대칭 허용 |
| guard_center_gem | 0px (UV 페이스) | Z 2 (front face) | 별도 큐브 없음. guard_center 전면 UV 페이스로만 표현 |
| guard_bar_r | 3px | Z −1 to 2 | 날개 앵커 아암, guard_center와 동일 최대 깊이 |
| wing_r_step1~4 | 2px | Z −1 to 1 | 슬림 날개 페더 |
| lower_talon_r | 2px | Z −1 to 1 | |
| handle_grip | 2px | Z −1 to 1 | |
| handle_ribbing (각 밴드) | 2px | Z −1 to 1 | 그립과 동일 |
| pommel_gold | 3px | Z −1 to 2 | 폼멜 최대 두께, 비대칭 허용 |
| pommel_gem | 0px (UV 페이스) | Z 2 (front face) | 별도 큐브 없음. pommel_gold 전면 UV 페이스로만 표현 |

※ guard_halo, wing_joint_r 는 44px 스케일에서 제거됨. guard_bar_r 는 축소 버전으로 유지.

---

## 6. Construction Phases

1. **중심선·실루엣 블록아웃**: 폼멜 기저(Y=0)부터 날 끝(Y=44)까지 전체 높이 확정. 각 구역 Y 범위 표시.
2. **날 본체**: `blade_lower`(Y 16–22) → `blade_mid`(Y 22–34) → `blade_upper`(Y 34–42) → `blade_tip`(Y 42–44) → `blade_base_socket`(Y 14–16) 순서로 배치. 외곽 챔퍼(`blade_edge_r`, Y 16–42) 추가 후 미러.
3. **날 코어+글로우**: `blade_core` (진홍 세로 스트립, Y 16–44) + `blade_core_glow` (Y 18–26 크림슨 확장). 코어는 날 전면에 반드시 연결.
4. **가드 중심 어셈블리**: `guard_center`(Y 12–16) → `guard_center_gem`. 연결된 하나의 어셈블리.
5. **가드 바+날개+타론**: `guard_bar_r`(X 3–4, Y 13–15) → `wing_r_step1~4` 순서로 오른쪽 빌드 → 좌측 미러. `lower_talon_r`(Y 10–12) 가드 하단 우측에 부착 → 좌측 미러.
6. **그립+폼멜**: `handle_grip`(Y 4–12) → `handle_ribbing` (3개 밴드, Y 5/8/11) → `pommel_gold`(Y 0–4) → `pommel_gem`.
7. **텍스처 패스**: 각 파츠에 per-face UV 매핑. 날개 엣지 1px 밝기 올리기, 가드 슬레이트 액센트, 루비 젬 하이라이트 추가.
8. **최종 점검**: 부유 블록 없음, 전후면 대칭, 날 중심선 무결성, 큐브 수 160 미만 확인.

---

## 7. Must-Not-Break Constraints

- 부유 블록 금지. 날개·헤일로·글로우·타론·젬은 반드시 주 구조물에 물리적으로 연결.
- 칼라 모자이크(무작위 색 배열) 금지. 색은 밴드·베벨·젬 하이라이트·스텝 섀도 형태로만 사용.
- 날개는 4단 계단 구조를 유지. 스텝 수 늘리거나 줄이지 않음.
- 오른쪽 날개+가드 바+타론은 Blockbench Mirror로만 좌측 생성. 수동 복제 금지.
- `assets/source` 내에 export된 JSON·최종 텍스처를 쓰지 않음.
- 인접 파츠 간 z-fighting: inflate ±0.05 이내로만 해결, float 좌표 사용 금지.
- 이 변형 폴더(`005_holy_wing_v2`) 밖의 파일 수정 금지.
- 흑철 테마의 정체성 유지: 근-흑 날·가드·날개, 크림슨 코어, 루비 젬.

---

## 8. Technical Constraints (non-negotiable)

### 8.1 Cube budget
- 목표 범위: **100–140 cubes**
- 하드 실링: **160 cubes**
- 날개 양쪽(4단 × 2 사이드): 8 cubes. 가드 어셈블리(guard_center + guard_bar): ~8. 블레이드 세부: ~30. 그립+폼멜: ~12. 나머지 여유: ~42.
- 160 초과 시 날개 스텝 두께나 블레이드 엣지 큐브를 텍스처 픽셀로 대체.

### 8.2 Absolute pixel dimensions

총 높이: **44px** (Y 0 = 폼멜 바닥, Y 44 = 날 끝)

수직 구역:

| 구역 | Y 범위 | 높이 |
|------|--------|------|
| Pommel | 0 – 4 | 4px |
| Grip | 4 – 12 | 8px |
| Guard (+ socket 내포) | 12 – 16 | 4px |
| Blade lower | 16 – 22 | 6px |
| Blade mid | 22 – 34 | 12px |
| Blade upper | 34 – 42 | 8px |
| Blade tip | 42 – 44 | 2px |

합계 확인: 4+8+4+6+12+8+2 = **44px** ✓  
※ blade_base_socket(2px)은 Guard 구역 상단(Y 14–16)에 중첩 배치되며 별도 구역 높이로 계산하지 않음.

파츠별 치수:

| 파츠 | Height (Y) | Width (X) | Depth (Z) | Y 시작 | Y 끝 |
|------|-----------|-----------|-----------|--------|------|
| pommel_gold | 4px | 5px | 3px | 0 | 4 |
| handle_grip | 8px | 3px | 2px | 4 | 12 |
| handle_ribbing (각 밴드) | 1px | 4px | 2px | 5, 8, 11 | +1 |
| guard_center | 4px | 6px | 3px | 12 | 16 |
| blade_base_socket | 2px | 4px | 2px | 14 | 16 |
| guard_bar_r | 2px | 1px | 3px | 13 | 15 |
| wing_r_step1 (innermost) | 4px | 1px | 2px | 12 | 16 |
| wing_r_step2 | 3px | 1px | 2px | 12 | 15 |
| wing_r_step3 | 2px | 1px | 2px | 13 | 15 |
| wing_r_step4 (outermost) | 1px | 1px | 2px | 13 | 14 |
| lower_talon_r | 2px | 2px | 2px | 10 | 12 |
| blade_edge_r (미러로 _l 생성) | 26px | 1px | 2px | 16 | 42 |
| blade_lower | 6px | 4px | 2px | 16 | 22 |
| blade_mid | 12px | 4px | 2px | 22 | 34 |
| blade_core | 28px | 1px | 2px | 16 | 44 |
| blade_core_glow | 8px | 2px | 2px | 18 | 26 |
| blade_upper | 8px | 4px | 2px | 34 | 42 |
| blade_tip | 2px | 2px | 2px | 42 | 44 |

X 좌표 (오른쪽, X ≥ 0 기준):
- 블레이드 (lower/mid/upper): X −2 to 2 (4px, 중심 대칭)
- blade_tip / blade_core: X −1 to 1 (2px)
- blade_core_glow: X −1 to 1 (2px)
- 가드 중심: X −3 to 3 (6px, 중심 대칭)
- guard_bar_r: X 3 to 4 (1px, 오른쪽 전용)
- wing_r_step1: X 3 to 4 (guard_bar_r와 Y 범위 다름)
- wing_r_step2: X 4 to 5
- wing_r_step3: X 5 to 6
- wing_r_step4: X 6 to 7
- 총 가로 span: 14px (X −7 to 7) ✓
- handle_grip: X −1 to 2 (3px, off-center 허용)
- pommel_gold: X −2 to 3 (5px, off-center 허용)
- lower_talon_r: X 3 to 5, Y 10–12

※ handle_grip(3px) / pommel_gold(5px)는 홀수 폭으로 X=0 완전 대칭 불가. off-center 배치 허용.

### 8.3 Texture strategy
- UV 모드: **per-face** (face별 개별 UV 지정, box UV 사용 금지)
- 텍스처 목록 (44px 스케일 기준):

  | 이름 | 해상도 | 용도 및 팔레트 바인딩 (§4 hex만 사용) |
  |------|--------|---------------------------------------|
  | `blade_tex` | 16×32 | base `#F2F2F2` + bevel `#C8CCD4` + core `#FFD54D` + glow `#FF9E1A` |
  | `guard_tex` | 16×8 | base `#E0B347` + AO `#9C6B16` + gem_hi `#3DBBFF` |
  | `wing_tex` | 16×8 | base `#F2F2F2` + shadow `#C8CCD4` + joint `#1E3A8A` |
  | `grip_tex` | 8×16 | base `#0D234A` + band `#1E3A8A` |
  | `pommel_tex` | 8×8 | base `#E0B347` + AO `#9C6B16` + gem `#3DBBFF` |

- **금지 패턴**:
  - `no 1×1 single-color textures` — 1픽셀 단일색 텍스처로 면 전체를 채우는 방식 절대 금지
  - `no one-texture-per-material silhouette approach` — 재질별 단일 색상 텍스처로 실루엣만 표현하는 방식 금지
  - `no colors outside §4 palette` — §4에 정의되지 않은 색상 사용 금지
  - `no dark metal / crimson / ruby palette` — 흑철·크림슨·루비 계열은 §4 위반, 사용 금지
  - 각 텍스처는 반드시 **면 구분에 기여하는 픽셀 그라데이션, 베벨 스트립, 또는 하이라이트 밴드**를 포함해야 함

### 8.4 Coordinate rules
- `from`/`to` 값은 **정수만** 허용 (`8.001`, `-3.5` 같은 소수 금지)
- `inflate` 프로퍼티: z-fighting 방지 목적으로 `±0.05` 이내만 허용
- 모든 파츠의 중심은 X = 0 기준으로 정렬

### 8.5 Mirror strategy
- 미러 축: **X 축**
- 미러를 사용하는 그룹:
  - `wing_r_step1` ~ `wing_r_step4` (날개 4단)
  - `guard_bar_r`
  - `lower_talon_r`
  - `blade_edge_r`
- 빌드 방향: **X ≥ 0 (오른쪽)만 빌드**, X < 0 (왼쪽)은 Blockbench Mirror 기능으로 생성
- 수동으로 왼쪽 큐브를 복사·붙여넣기하지 않음
- `wing_joint_r`는 `guard_bar_r`에 통합. 별도 미러 불필요.

### 8.6 Group origin rules

| 그룹 | Pivot Origin |
|------|-------------|
| `pommel` | [0, 0, 0] — 폼멜 바닥 중심 |
| `grip` | [0, 4, 0] — 그립 하단, 폼멜-그립 경계 |
| `guard` | [0, 12, 0] — 가드 하단, 그립-가드 경계 |
| `guard_bar_r` | [3, 14, 0] — 가드 우측 면(X=3), 가드 세로 중간(Y=14) |
| `wing_r` (wing steps) | [3, 14, 0] — guard_bar_r 부착점과 날개 시작 경계, Y는 가드 중간 |
| `blade` | [0, 16, 0] — 블레이드 하단, 가드 상단 |
| `lower_talon_r` | [3, 12, 0] — 가드 하단 우측 코너 |

### 8.7 Physical attachment graph

| 파츠 | 부착 대상 | 접촉 면 |
|------|-----------|---------|
| `blade_base_socket` | `guard_center`.top_interior | Y=14–16, guard_center 내 중첩 |
| `blade_lower` | `blade_base_socket`.top_face | Y=16 면에서 만남 |
| `blade_mid` | `blade_lower`.top_face | Y=22 |
| `blade_upper` | `blade_mid`.top_face | Y=34 |
| `blade_tip` | `blade_upper`.top_face | Y=42 |
| `blade_core` | `blade_lower`.center_face | 날 전면 Z=1 면에 flush, Y 16–44 전체 |
| `blade_core_glow` | `blade_core`.outer_pixels | 코어 외곽에서 확장, Y 18–26 |
| `blade_edge_r` | `blade_lower/mid/upper`.right_face | X=2 면에 flush, Y 16–42 |
| `guard_bar_r` | `guard_center`.right_face | X=3 면에서 만남, Y 13–15 |
| `wing_r_step1` | `guard_center`.right_face | X=3, Y 12–16 (guard_bar_r와 X 공유, Z 범위 다름) |
| `wing_r_step2` | `wing_r_step1`.right_face | X=4, Y 12–15 |
| `wing_r_step3` | `wing_r_step2`.right_face | X=5, Y 13–15 |
| `wing_r_step4` | `wing_r_step3`.right_face | X=6, Y 13–14 |
| `lower_talon_r` | `guard_center`.bottom_right_face | X=3–5, Y=12 |
| `handle_ribbing` (각 밴드) | `handle_grip`.outer_face | Z=±1, Y=5/8/11 |
| `pommel_gem` | `pommel_gold`.front_face | Z=2 페이스 (폼멜 전면) |
| `guard_center_gem` | `guard_center`.front_face | Z=2 페이스 (가드 전면) |
