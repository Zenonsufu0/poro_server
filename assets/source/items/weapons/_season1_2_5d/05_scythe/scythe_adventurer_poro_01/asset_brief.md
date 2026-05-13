# scythe_adventurer_poro_01 — Asset Brief

> 작성일: 2026-05-09
> 단계: Phase 1 완료 — 레퍼런스 분석 완료
> 상태: design_ready

---

## 1. 자산 개요

| 항목 | 값 |
|---|---|
| asset_id | scythe_adventurer_poro_01 |
| display_name | 모험가의 낫 |
| set | Adventurer (starter) |
| rarity | starter/common |
| weapon_class | scythe |
| material | NETHERITE_HOE |
| CMD | 100502 (예정) |
| 목표 elements | 13 |
| 텍스처 크기 | 64×64 |

---

## 2. 레퍼런스 분석

### 2-1. 레퍼런스 파일

| 우선순위 | 파일 | 특징 |
|---|---|---|
| 1 (주 기준) | `original/scythe_adventurer_poro_01_texture_ref.png` | Minecraft 픽셀아트 스타일 낫. 2.5D 직접 참고 가능. |
| 2 (보조) | `original/scythe_adventurer_poro_01_ref.png` | 실사풍 3D 렌더링. 형태·비율·소재감 참고. |

### 2-2. 실루엣 분석 (주 레퍼런스 기준)

**전체 비율 (Y축 기준):**
- 날 + 소켓: 상단 ~30%
- 상단 자루 (bare shaft): ~35%
- 그립 (grip wrap): ~25%
- 하단 자루 + 버트캡: ~10%

**날 (Blade):**
- 소켓에서 시작해 우상방으로 뻗는 초승달(crescent) 형태
- 기저부: 가장 넓음 (~6px 폭), 소켓에 직접 연결
- 중간: 곡선 시작, 우상향으로 넓이 감소
- 상단: 급격히 좁아지며 포인트로 수렴
- 날 끝(tip): 1-2px 날카로운 포인트
- 내측(오목면) = 절삭날, 어두운 엣지로 표현
- 외측(볼록면) = 등날, 밝은 강철 하이라이트

**소켓/마운트 (Socket):**
- 자루 최상단 직사각형 철제 브래킷
- 픽셀아트에서 상/하단 보강 밴드 2개 확인 (각각 자루 위아래 감싸는 형태)
- 날 기저부와 자루 상단을 이어주는 금속 구조물로 실루엣에서 명확히 구분됨
- 자루보다 X방향으로 넓음 (날쪽으로 확장)

**자루 (Shaft):**
- 매우 가늘고 직선 (~2px 두께)
- 전체 자루 색상: 중간~진한 갈색 (오크 계열)
- 그립 위와 아래 두 구간으로 나뉘되, 색상은 동일

**그립 (Grip wrap):**
- 자루 하단 약 35% 구역
- 대각선 가죽 감기 패턴 (픽셀아트에서 명확히 관찰됨, ↘ 방향)
- 자루보다 약간 넓음 (앞/뒤 레이어로 두께감 표현)
- 매우 어두운 색상 (거의 검정에 가까운 짙은 가죽 갈색)
- 상단에 금속 칼라(collar)로 마감

**버트캡 (Butt cap):**
- 자루 최하단 소형 금속 페룰(ferrule)
- 실사 레퍼런스에서 뾰족한 형태
- 픽셀아트에서 자루보다 약간 넓은 금속 블록으로 마감
- 스틸 회색

---

## 3. 파츠 구조 (목표 13 elements)

| 그룹 | 파트명 | 역할 | 예상 크기 (X×Y×Z) |
|---|---|---|---|
| **blade** | `blade_base` | 날 기저부 — 소켓 직결, 가장 넓음 | 6×5×1 |
| **blade** | `blade_mid` | 날 중간 — 우상향 곡선 시작 | 5×4×1 |
| **blade** | `blade_upper` | 날 상단 — 급격히 좁아짐 | 4×4×1 |
| **blade** | `blade_tip` | 날 끝 — 날카로운 포인트 | 2×3×1 |
| **socket** | `socket_body` | 메인 직사각형 브래킷 | 6×6×2 |
| **socket** | `socket_band_upper` | 소켓 상단 보강 밴드 | 5×2×3 |
| **socket** | `socket_band_lower` | 소켓 하단/자루 연결 밴드 | 3×2×3 |
| **shaft** | `shaft_upper` | 소켓 아래 ~ 칼라 위 — 긴 목재 자루 | 2×10×1 |
| **shaft** | `shaft_lower` | 그립 아래 ~ 버트캡 위 | 2×5×1 |
| **grip** | `grip_front` | 가죽 감기 앞면 (약간 돌출) | 3×9×1 |
| **grip** | `grip_back` | 가죽 감기 뒷면 | 3×9×1 |
| **hardware** | `collar` | 그립 상단 금속 칼라 | 3×2×3 |
| **hardware** | `butt_cap` | 자루 하단 금속 페룰 | 3×2×2 |

**총 13 elements** — starter 등급 적합 (Carnivoret 낫 36 대비 경량)

---

## 4. 색상 팔레트

| 파트 그룹 | 색상명 | Hex |
|---|---|---|
| 날 밝은 면 (tip 강조) | 연한 강철 | `#D4DDE2` |
| 날 중간 면 | 중간 은회 | `#B0BCC2` |
| 날 어두운 엣지/내측 | 진한 청회 | `#7A8890` |
| 소켓/밴드 (다크 스틸) | 다크 아이언 | `#484C54` |
| 소켓 하이라이트 | 중간 스틸 | `#7C8088` |
| 목재 자루 밝음 | 중간 갈색 | `#9C6E42` |
| 목재 자루 중간 | 오크 브라운 | `#7A5030` |
| 목재 자루 어두움/나무결 | 진한 갈색 | `#5A3820` |
| 가죽 그립 밝음 (하이라이트) | 어두운 가죽 | `#4A2E18` |
| 가죽 그립 중간 | 짙은 가죽 갈색 | `#341E0C` |
| 가죽 그립 어두움/감기 선 | 거의 검정 | `#1E0E06` |
| 칼라/버트캡 금속 | 중간 스틸 회색 | `#787C84` |

---

## 5. 텍스처 레이아웃 계획 (64×64)

| Zone | U 범위 | 기준 색상 | 적용 파츠 |
|---|---|---|---|
| A — 날 강철 | U=0~15 | `#B8C5CC` (연한 은청회) | blade_base / blade_mid / blade_upper / blade_tip |
| B — 어두운 철 | U=16~31 | `#484C54` (다크 스틸) | socket_body / socket_band × 2 / collar / butt_cap |
| C — 목재 자루 | U=32~47 | `#7A5030` (오크 브라운) | shaft_upper / shaft_lower |
| D — 가죽 그립 | U=48~63 | `#341E0C` (짙은 가죽) | grip_front / grip_back |

UV 방식: `box_uv=false`, per-face, 각 존 내 8×8 patch (solid color draft → Phase 4에서 픽셀 다듬기)

---

## 6. 블록아웃 품질 게이트 (Phase 3 검증 기준)

- [ ] 날 초승달 실루엣이 측면에서 식별 가능 (base → mid → upper → tip 단계 읽힘)
- [ ] 소켓 브래킷이 날과 자루 사이에서 금속 구조물로 명확히 식별 가능
- [ ] 자루가 가늘고 직선 (~2px 두께)
- [ ] 그립 구역이 자루보다 약간 넓고, 색/폭 차이로 명확히 구분 가능
- [ ] 상단 칼라가 그립과 상단 자루 사이에서 식별 가능
- [ ] 버트캡이 자루 하단에서 소형 금속 마감으로 돌출
- [ ] 단순 직사각형 막대 아님 — 날 곡선이 실루엣을 차별화

---

## 7. 다음 단계

- [ ] Phase 2: `bb-asset-brief` — ref_spec.json 좌표 정밀 확인 + .bbmodel 빌드 계획 확정
- [ ] Phase 3: `bb-build-blockout` — Blockbench MCP로 .bbmodel 제작 (MCP 필수)
- [ ] Phase 4: `bb-texture-pass` — 4존 팔레트 적용
- [ ] Phase 5: `bb-export-register` — JSON export + resourcepack + registry 등록
