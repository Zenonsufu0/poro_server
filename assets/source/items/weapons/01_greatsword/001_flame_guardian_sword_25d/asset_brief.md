# poro_flame_guardian_sword_25d

## Role
- weapon_type: greatsword
- server_set_or_theme: 불꽃 / 수호자 (Flame / Guardian)
- combat_identity: 강력한 화염 수호 대검. 불꽃이 날 전체에 타오르는 공격적·수호적 이중 인상.

## Reference Inputs
- primary_ref: `source/poro_flame_guardian_sword_cutout_full.png`
- support_refs:
  - `refs/poro_flame_guardian_sword_concept_raw.png`
  - `textures/poro_flame_guardian_sword_25d.png`
- source_note: ChatGPT 생성 프로덕션 레퍼런스. Poro 서버 오리지널 변형으로 제작.

## Poro Transformation
- What is kept: 전반적 실루엣 — 길고 얇은 날, 위쪽 곡선 날개 가드, 다이아몬드 보석, 불꽃 톱니 엣지
- What is changed: 2.5D 제약 — 검신은 얇은 평면 (Z=1u), 불꽃 디테일은 텍스처 처리, 가드 날개 큐브 수 최소화
- What is removed: 검신의 내부 불꽃 디테일 geometry (텍스처로 대체), 미세 장식 큐브 전면 제거

## Minecraft Readability
- Viewed in hand, the model must read as: 불꽃 타오르는 화염 대검. 위쪽으로 뻗은 날개형 가드와 중앙 보석이 즉시 인식되어야 함.
- Silhouette priority: 검신 엣지 톱니(좌우 각 4개) → 위쪽 스위핑 가드 날개 → 다이아몬드 폼멜
- Color priority: 딥 크림슨/다크 오렌지 베이스 + 브라이트 오렌지/노랑 불꽃 강조

## Blockout Plan
Main parts:
- blade/head: `blade_core` (Z=1u 평면) + `blade_edge_l/r` 톱니 ×4쌍 + `blade_tip_main` + `blade_tip_spike`
- guard/core: `guard_lower_core` + `guard_lower_spike_l/r` + `guard_hub` + `center_gem` + `guard_wing_l/r` (base/mid/tip ×3)
- handle/shaft: `handle_top_band` + `handle` + `handle_bot_band`
- pommel/back module: `pommel_neck` + `pommel_main`

## Texture Plan
- Palette:
  - primary: #4A1200 (딥 크림슨 베이스)
  - secondary: #CC4400 (다크 오렌지 날)
  - accent: #FF7700 (브라이트 오렌지 불꽃)
  - glow: #FFCC00 (노란 불꽃 팁/코어)
  - shadow: #1A0500 (다크 브라운/블랙 손잡이)
- Material cues: 불꽃 마법 날, 단조 다크 메탈 가드, 가죽 랩 손잡이
- Glow/accent zones: 검신 양쪽 엣지, 검신 팁 스파이크, 보석 중앙, 가드 날개 엣지

## Risk Notes
Top 3 risks:
1. **가드 날개 비율**: 날개가 너무 넓으면 손에 쥐었을 때 어색해 보임 — 총 가드 폭 10u 이내 유지.
2. **검신 납작함**: Z=1u 검신은 측면 뷰에서 거의 안 보임 — 엣지 톱니(Z=1u)로 측면 실루엣 보완 필수.
3. **보석 돌출**: center_gem Z=2.5u가 blade_core Z=0.5u와 자연스럽게 연결되어야 함 — gem이 공중에 뜨지 않도록 guard_hub와 Z 겹침 설계.
