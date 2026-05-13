# gs_002_harvest_blade

## Role
weapon_type: greatsword
server_set_or_theme: 수확의 계절 (Harvest Season) — 냉기·결정 계열
combat_identity: 광범위 참격형 대검. 넓고 비대칭적인 날이 강렬한 존재감을 준다.

## Reference Inputs
- primary_ref: wuthering_waves_jinhsi_ages_of_harvest_broadblade_01.png
- support_refs: []
- source_note: 명조(Wuthering Waves) Jinhsi 전용 무기 '태평성대'. 직접 복제 금지, 실루엣·테마만 참조.

## Poro Transformation
What is kept:
- 비대칭 광폭 날 (한쪽이 더 넓게 펼쳐지는 브로드블레이드 실루엣)
- 날개형 가드 — 수평으로 뻗는 윙 가드 1~2매
- 냉기·결정 색채: 아이시 블루-화이트 팔레트
- 날 상단의 뾰족하게 솟은 크레센트 모티프

What is changed:
- 모든 곡선 → Minecraft 블록 직선 계단 처리
- 반투명/글로우 블레이드 → 불투명 블록 + 텍스처 하이라이트 표현
- 젬/크리스탈 장식 → 1개 포인트 젬으로 축소 (가드 중심)
- 복잡한 파팅라인 조각 → 2~3픽셀 트림선으로 단순화

What is removed:
- 투명도 기반 날 질감
- 파티클·트레일 이펙트 (런타임 효과, 모델 범위 밖)
- 날 안쪽 세밀한 엔그레이빙 문양
- 손잡이 끝의 이중 루프 장식 (폼멜 단순 블록으로 대체)

## Minecraft Readability
Viewed in hand, the model must read as:
  냉기 속성의 대형 광검 — 한눈에 '넓고 날카롭다' 인식 가능해야 함.
Silhouette priority:
  1. 날 비대칭 너비 (왼쪽이 오른쪽보다 한 블록 이상 넓어야 함)
  2. 윙 가드의 수평 확장
  3. 날 상단의 뾰족한 크레센트 돌출
Color priority:
  1. 아이시 블루-화이트 (날 면)
  2. 미드 블루 그림자 (날 가장자리·하단)
  3. 포인트 젬의 밝은 악센트

## Blockout Plan
Main parts:
- blade_core: 중앙 두꺼운 블록 기둥 (Y 방향 길이 약 24u), 폭 4u, 깊이 2u
- blade_wide: blade_core 왼쪽에 붙는 넓은 날 면 (폭 4u 추가), 계단형 테이퍼
- blade_tip: 상단 크레센트 돌출 (2~3블록, X 방향 좌측 비대칭)
- wing_guard_l: 가드 좌측 수평 날개 (폭 5u, 높이 1u, 깊이 1u)
- wing_guard_r: 가드 우측 수평 날개 (폭 3u, 높이 1u, 깊이 1u) — 비대칭
- guard_core: 날개 연결 중심 블록 (2×2×2u)
- gem_center: 가드 중심 포인트 젬 (1×1×1u, 악센트 색)
- handle: 원기둥형 손잡이 (1×1u 단면, 길이 6u)
- pommel: 하단 타원형 마무리 (2×1×2u)

## Texture Plan
Palette:
  - icy_blue:   #A8C8E8  (날 메인 면)
  - ice_white:  #DCF0FF  (날 하이라이트 선)
  - mid_blue:   #5080A8  (날 가장자리·그림자)
  - deep_blue:  #1E3A5A  (윤곽선·그림자 최하단)
  - gem_glow:   #C0E8FF  (포인트 젬)
  - handle_cool: #384858 (손잡이·폼멜)

Material cues:
  - 날: 광택 있는 결정 금속 — 수직 하이라이트 1~2픽셀 선
  - 가드: 날보다 약간 더 짙은 블루-그레이
  - 손잡이: 매트한 다크 슬레이트

Glow/accent zones:
  - blade_tip 최상단 1~2픽셀: ice_white 강조
  - gem_center 전면: gem_glow (2×2px 발광 느낌)
  - blade_wide 외곽 엣지: ice_white 1픽셀 하이라이트

## Risk Notes
Top 3 risks:
  1. 비대칭 날 폭이 너무 과하면 손에 들었을 때 좌측으로 치우쳐 보임 → blade_wide 최대 +4u로 제한, 테스트 필수
  2. blade_core + blade_wide 연결부가 부유 블록처럼 보일 수 있음 → 반드시 면 접합 확인
  3. 윙 가드 날개가 너무 길면 다른 아이템과 클리핑 → wing_guard_l 최대 5u, wing_guard_r 최대 3u 고수
