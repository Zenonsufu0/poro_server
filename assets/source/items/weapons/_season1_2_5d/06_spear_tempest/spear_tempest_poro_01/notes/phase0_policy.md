# spear_tempest_poro_01 — Phase 0 Policy

작성일: 2026-05-11

## 소스 원칙

- WSL source-of-truth 정책 (2026-05-10 ~) 준수
- 작업 원본: `~/dev/poro-server/assets/source/items/weapons/_season1_2_5d/06_spear_tempest/spear_tempest_poro_01/source/`
- Blockbench 저장 경로: UNC `\\wsl.localhost\Ubuntu\...` 직접 저장
- rsync 불필요

## 파트 구성 (24 cubes)

| 파트명 | 역할 | 재질 |
|---|---|---|
| blade_tip | 날 끝 | blade |
| blade_step1~3 | 날 단계별 확장 | blade |
| blade_base | 날 기저 | blade |
| blade_edge_left/right | 날 측면 엣지 | blade |
| wing_left/right | 날개 | wing |
| wing_left/right_hook | 날개 갈고리 | wing |
| socket_upper/lower | 소켓 | socket |
| crystal_core | 크리스탈 코어 | crystal |
| crystal_left/right | 크리스탈 보조 | cyan |
| shaft_upper | 자루 상부 | shaft |
| shaft_mid_ring | 자루 중간 링 | ring |
| shaft_lower | 자루 하부 | shaft |
| grip_main | 손잡이 | grip |
| grip_ring_upper/lower | 손잡이 링 | ring |
| butt_cap | 석 캡 | socket |
| butt_tip | 석 끝 | blade |

## 텍스처 구성

- 단일 64×64 PNG
- UV 0-16 공간 (Project.texture_width=16)
- 8개 재질 구역: blade/wing/cyan/crystal/socket/ring/shaft/grip
- Phase 2: 실제 번개 테마 텍스처 작업 예정

## 현재 단계

- Blockout: **완료** (placeholder 텍스처)
- 텍스처: **미완료** — Phase 2 작업 필요
- Display: **완료** (spear_carnivoret_poro_01 기준)
- Export JSON: **완료** (bad_uv=0 검증 통과)
- CMD 등록: **완료** (trident 100303)
