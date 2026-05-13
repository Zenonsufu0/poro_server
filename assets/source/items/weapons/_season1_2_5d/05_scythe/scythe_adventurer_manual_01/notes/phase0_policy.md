# scythe_adventurer_manual_01 — Phase 0 정책

asset_id : scythe_adventurer_manual_01
test CMD : 100595
작성일   : 2026-05-10

---

## 경로 정의

| 역할 | 경로 |
|---|---|
| **작업 중 원본 (.bbmodel)** | `~/dev/poro-server/assets/source/items/weapons/_season1_2_5d/05_scythe/scythe_adventurer_manual_01/source/scythe_adventurer_manual_01.bbmodel` |
| Blockbench Save As (UNC) | `\\wsl.localhost\Ubuntu\home\zenonsufu1\dev\poro-server\assets\source\items\weapons\_season1_2_5d\05_scythe\scythe_adventurer_manual_01\source\scythe_adventurer_manual_01.bbmodel` |
| 최종 백업/보관 (수정 금지) | `C:\Users\User\Project\poro-assets-work\_season1_2_5d` |
| export model JSON | `~/dev/poro-server/assets/export/resourcepack/assets/poro/models/item/weapons/` |
| export texture PNG | `~/dev/poro-server/assets/export/resourcepack/assets/poro/textures/item/weapons/` |

---

## 이번 시즌 정책 변경 사항 (2026-05-10~)

- **WSL source `.bbmodel` = 작업 중 유일 원본.** C 경로는 최종 백업/보관용이며 작업 중 직접 수정하지 않는다.
- **rsync 단계 폐지.** 이전 정책의 Windows → WSL rsync는 더 이상 불필요하다.
- Blockbench에서 UNC 경로(`\\wsl.localhost\Ubuntu\...`)로 직접 Save As하면 WSL 파일이 갱신된다.

---

## 절대 금지

- `/mnt/c/Users/User/Project/poro-assets-work` 아래 `.bbmodel` 직접 수정
- C 경로 `.bbmodel` 직접 수정
- WSL source와 C backup을 동시에 원본처럼 취급
- Python/터미널로 `.bbmodel` raw JSON 직접 생성
- `java_item` 포맷 `.bbmodel` 생성
- export JSON만 수동 수정해서 완료 처리

---

## Phase 별 작업 계획

| Phase | 내용 | 상태 |
|---|---|---|
| 0 | 폴더 생성, 정책 문서, Blockbench 안내 | ✅ 완료 |
| 1 | Blockbench에서 geometry 제작 | 대기 중 |
| 2 | texture / UV 매핑 | 대기 중 |
| 3 | display 설정 (GUI/ground/hand) | 대기 중 |
| 4 | MCP로 export JSON 생성 | 대기 중 |
| 5 | CMD 100595 registry 등록 | 대기 중 |

---

## 인게임 테스트 명령어 (Phase 5 이후)

```
/clear @s minecraft:netherite_hoe
/minecraft:give @s minecraft:netherite_hoe[minecraft:custom_model_data={strings:["100595"]}] 1
```
