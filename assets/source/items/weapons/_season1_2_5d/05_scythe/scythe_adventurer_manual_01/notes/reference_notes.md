# scythe_adventurer_manual_01 — Reference Notes

작성일: 2026-05-11

## 레퍼런스 파일

| 파일 | 역할 |
|---|---|
| `reference/scythe_adventurer_ref_01.png` | 실루엣/형태 기준 레퍼런스 |

## 포로 서버 변형 방향

- 원본 레퍼런스의 낫 형태를 유지하되 포로 서버 독자 디자인으로 제작
- 로고·텍스트·저작권 마킹 재현 금지
- 전체 실루엣 → 비율 → 날 형태 순서로 유사성 우선

## 파트 구성 (9 cubes)

| 파트명 | 역할 |
|---|---|
| `shaft_main` | 자루 본체 |
| `grip_main` | 손잡이 부분 |
| `butt_cap_main` | 자루 끝 캡 |
| `collar_main` | 날-자루 연결 고리 |
| `socket_main` | 소켓 (날 장착부) |
| `blade_heel` | 날 뒤꿈치 |
| `blade_arc` | 날 호 (메인 곡선) |
| `blade_upper` | 날 상부 |
| `blade_tip` | 날 끝 |

## 텍스처 재질 분리

| 텍스처 파일 | 대상 파트 |
|---|---|
| `scythe_adventurer_manual_01_blade.png` | blade_heel / blade_arc / blade_upper / blade_tip |
| `scythe_adventurer_manual_01_metal.png` | butt_cap_main / collar_main / socket_main |
| `scythe_adventurer_manual_01_wood.png` | shaft_main |
| `scythe_adventurer_manual_01_grip.png` | grip_main |

## 현재 단계

- Blockout: **완료** (placeholder 텍스처 적용)
- 텍스처: **미완료** — Phase 2 작업 필요
- Display: **완료**
- Export JSON: **완료** (bad UV: 0 검증 통과)
