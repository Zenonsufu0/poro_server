# spear_adventurer_poro_01 — Asset Brief

> 작성일: 2026-05-08
> 단계: bb-asset-brief (Phase 2)
> 레퍼런스: original/spear_adventurer_poro_01_ref.png

---

## 1. 자산 개요

| 항목 | 값 |
|---|---|
| asset_id | spear_adventurer_poro_01 |
| display_name | 모험가의 창 |
| set | Adventurer (starter) |
| rarity | starter/common |
| weapon_class | spear |
| material | TRIDENT |
| CMD | 100302 |

---

## 2. 레퍼런스 해석

레퍼런스는 실사풍 목제 폴암/재블린. Minecraft 2.5D로 단순화 시 다음 특징을 유지한다.

### 유지할 특징 (실루엣 우선)
- 창날의 위-아래 테이퍼 (상단 극세 → 하단 3~4px)
- 날 하단 좌우 대칭 갈고리 루그 — 이 자산의 가장 강한 실루엣 포인트
- 긴 직선형 목제 자루 (자루 자체는 단순, 2px 폭)
- 하단 1/3에 명확히 구분되는 감긴 그립 (색/질감 차이)
- 소형 하단 금속 캡

### 제거/단순화할 요소
- 실사풍 나무결 음영 → 단순 나무색 + 1~2픽셀 엣지 대비
- 루그의 곡선 → 각진 블록 후크로 표현
- 금속 광택 → 밝음/어두움 2단계 대비로만 표현

---

## 3. 파츠 구조 (목표 11-13 elements)

| 파트명 | 역할 | elements | 크기 (X, Y, Z) |
|---|---|---|---|
| `spearhead_tip` | 최상단 극세 포인트 | 1 | 1×2×1 |
| `spearhead_upper` | 날 상단부 (좁음) | 1 | 2×4×1 |
| `spearhead_lower` | 날 하단부 (넓음) | 1 | 4×4×1 |
| `lug_left` | 좌측 갈고리 날개 | 1 | 2×2×1 |
| `lug_right` | 우측 갈고리 날개 | 1 | 2×2×1 |
| `shaft_collar` | 날-자루 결합 금속 밴드 | 1 | 3×2×1 |
| `shaft_upper` | 상단 목제 자루 | 1 | 2×12×1 |
| `grip_wrap` | 감긴 그립 (2.5px 폭) | 2 | 3×6×2 (앞/뒤) |
| `shaft_lower` | 하단 목제 자루 | 1 | 2×3×1 |
| `butt_cap` | 하단 금속 캡 | 1 | 2×2×1 |

**총 11 elements** — Carnivoret 30 대비 경량, starter 등급 적합

---

## 4. 색상 팔레트

| 파트 | 색상 | Hex |
|---|---|---|
| 창날 (밝은 면) | 연은 | `#C8C8C8` |
| 창날 (어두운 면/엣지) | 진은회 | `#888888` |
| 갈고리 루그 | 어두운 금속 | `#787878` |
| 금속 칼라/캡 | 중간 은회 | `#989898` |
| 목제 자루 (밝음) | 중간 갈색 | `#8B5E3C` |
| 목제 자루 (어두움/엣지) | 진갈색 | `#6B3D1E` |
| 그립 감기 (밝음) | 어두운 갈색 | `#4A3020` |
| 그립 감기 (어두움) | 거의 검정 | `#2A1A10` |

---

## 5. Texture 레이아웃 계획 (64×64)

```
U  0-16 / V  0-16  → spearhead (tip + upper + lower)
U  0-16 / V 16-32  → lugs + collar
U  0-16 / V 32-48  → shaft_upper (wood grain 2-3색 세로 줄)
U  0-16 / V 48-64  → grip_wrap + shaft_lower + butt_cap
```

---

## 6. 품질 게이트 기준

blockbench 블록아웃 통과 조건:
- [x] 창날 테이퍼 실루엣이 레퍼런스와 유사하게 읽힘
- [x] 좌우 루그가 날개 모양으로 식별 가능
- [x] 자루와 그립 영역이 색/폭으로 구분 가능
- [x] 하단 캡이 창날보다 훨씬 작게 표현됨
- [x] 단순 직사각형 막대 모양이 아님

---

## 7. 다음 단계

- Phase 3: `bb-build-blockout` — Blockbench MCP로 .bbmodel 제작
- Phase 4: `bb-texture-pass` — 팔레트 적용
- Phase 5: `bb-export-register` — JSON export + resourcepack 등록
