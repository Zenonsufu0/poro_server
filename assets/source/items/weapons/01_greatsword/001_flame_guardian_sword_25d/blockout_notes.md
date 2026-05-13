# poro_flame_guardian_sword_25d — Blockout Notes

## 현재 버전
`source/poro_flame_guardian_sword_25d_blockout_v2.bbmodel` — **29큐브**, bb-audit-fix 적용, 2.5D

---

## v2 구조 요약 (29큐브) — 현재

| 그룹 | 큐브 수 | 내용 |
|------|---------|------|
| blade_group | 7 | blade_core + 좌측 불꽃 스텝 3 + 우측 불꽃 스텝 3 |
| blade_tip_group | 2 | tip_main + tip_spike |
| guard_group | 13 | lower_core + spike_l/r + hub + center_gem + wing_l 4단계 + wing_r 4단계 |
| handle_group | 2 | handle_main + top_band |
| pommel_group | 3 | pommel_tip + pommel_mid + pommel_body |

---

## v1 → v2 변경 내역 (bb-audit-fix)

| 항목 | v1 | v2 |
|------|----|----|
| 검신 불꽃 스텝 | 균일 4쌍 (2u×1u 각각) | 비균일 3쌍, 크기 점감 (좌: 1.5u/1u/0.5u 돌출, 우: 1u/1u/0.5u, 좌우 오프셋) |
| 가드 날개 단계 | 3단계, X±5.5u | 4단계, X±6.5u (25% 확장) |
| 가드 날개 실루엣 | 짧은 뿔 | 위쪽 곡선 호 더 뚜렷 |
| 보석 Z 깊이 | ±1.25u | ±1.5u (20% 증가, 더 두드러짐) |
| 손잡이 폭 | 1.5u (X±0.75) | 1.25u (X±0.625, 슬리머) |
| 손잡이 큐브 수 | 3 (neck+main+band) | 2 (main+band, neck 제거) |
| 폼멜 형태 | 단순 직사각 1큐브 | 3단계 다이아몬드 (tip→mid→body 점층) |

---

## Y 레이아웃 (총 40u)

| 구간 | Y 범위 | 파트 |
|------|--------|------|
| 폼멜 | 0–2 | pommel_tip(0–0.5), pommel_mid(0.5–1), pommel_body(1–2) |
| 손잡이 | 2–10 | handle_main(2–9), handle_top_band(9–10) |
| 가드 하단 | 10–13 | guard_lower_core, guard_lower_spike_l/r |
| 가드 허브 | 13–17 | guard_hub, center_gem |
| 가드 날개 | 15–23 | wing_l/r base→mid→upper→tip (위쪽 스위핑) |
| 검신 본체 | 17–37 | blade_core + 불꽃 스텝 6개 |
| 검신 팁 | 37–40 | blade_tip_main + blade_tip_spike |

---

## 2.5D 깊이 설계

| 파트 | Z 깊이 | 비고 |
|------|--------|------|
| 검신 코어 | 1u | 2.5D 핵심 — 정면/후면 평면 |
| 검신 불꽃 스텝 | 1u | 실루엣 돌출 geometry |
| 가드 하단/허브 | 2u | 약간의 입체감 |
| 가드 날개 base/mid | 2u | 날개 두께 |
| 가드 날개 upper/tip | 1.5u / 1u | 팁으로 갈수록 얇아짐 |
| 보석 | 3u | 허브 밖으로 돌출 |
| 손잡이 | 1u | 평면 |
| 폼멜 | 1u–2u | 하단 얇고 상단 두꺼운 다이아몬드 형태 |

---

## 전면 뷰 실루엣 평가 (v2)

**충족된 항목:**
- 불꽃 장식 대검 실루엣 인식 가능 ✓
- 검신 불꽃 스텝 비균일 — 하단 큰 돌출, 상단 점감 (유기적 불꽃 느낌) ✓
- 가드 날개 4단계 위쪽 스위핑 — 레퍼런스 날개 비율 근접 ✓
- 가드 하단 스파이크 ✓
- 보석 Z 돌출 (45도 뷰에서 확인 가능) ✓
- 손잡이 슬림 / 폼멜 다이아몬드형 분리 ✓
- 검신 팁 + 스파이크 ✓
- 총 큐브 29개 (2.5D 기준 적정) ✓

**texture-pass 진행 가능 판정: 가능**

---

## v1 구조 요약 (26큐브) — 폐기

| 그룹 | 큐브 수 | 내용 |
|------|---------|------|
| blade_group | 9 | blade_core + 좌측 엣지 톱니 4 + 우측 엣지 톱니 4 |
| blade_tip_group | 2 | tip_main + tip_spike |
| guard_group | 11 | lower_core + spike_l/r + hub + center_gem + wing_l 3단계 + wing_r 3단계 |
| handle_group | 3 | neck + main + top_band |
| pommel_group | 1 | pommel_main |

---

## 버전 이력

- v1: 26큐브, 폐기 (균일 톱니, 가드 날개 좁음, 단순 폼멜)
- v2: 29큐브, **현재** (비균일 불꽃 스텝, 가드 날개 4단계 확장, 다이아몬드 폼멜 — texture-pass 진행 가능)
