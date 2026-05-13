# mob_barrelknight_body_01 — 몬스터 장비 후보 문서

> 분류일: 2026-05-08
> 원본 자산: prop_barrel_poro_01 (BarrelKnight_1.1.zip)
> 재분류 사유: 영지 소품 부적합, 인게임에서 barrel backpack / 몸통 갑옷 연출로 자연스러움

---

## 자산 기본 정보

| 항목 | 값 |
|---|---|
| asset_id | mob_barrelknight_body_01 |
| display_name | 배럴 나이트 갑옷 |
| 콘텐츠 분류 (visual_role) | body armor / backpack / barrel armor |
| **구현 슬롯 (actual_slot)** | **HELMET** (Minecraft Equipment head 슬롯) |
| material | PAPER |
| custom_model_data | 200101 |
| model_path | poro:item/props/prop_barrel_poro_01 |
| texture_path | poro:item/props/prop_barrel_poro_01 |
| 원본 팩 | BarrelKnight_1.1 (HMCCosmetics head cosmetics) |

> **슬롯명-콘텐츠 분류 분리 원칙**
> Minecraft는 등짐/몸통 슬롯을 직접 지원하지 않으므로 HELMET 슬롯을 사용한다.
> 그러나 이 자산의 시각적 역할은 body armor(몸통 갑옷) / backpack(등짐)으로 기록한다.
> 구현 슬롯명과 콘텐츠 의미가 다름을 반드시 분리하여 문서화한다.

---

## 인게임 테스트 결과 요약

- player 뒤태에서 barrel backpack(등짐형 나무통)처럼 보임
- skeleton에 HELMET 슬롯으로 장착 시 배럴 나이트 몬스터로 자연스럽게 연출됨
- prop(바닥 오브젝트)보다 mob cosmetic(몬스터 장비)으로 훨씬 적합

---

## 추천 몬스터 3종

### 1. Barrel Knight (배럴 나이트)

| 항목 | 값 |
|---|---|
| 베이스 몹 | SKELETON |
| 장비 슬롯 | HELMET ← mob_barrelknight_body_01 |
| 주무기 | 나무 검 / 돌 검 (별도 치장 또는 vanilla) |
| 콘셉트 | 나무통을 갑옷으로 두른 초중반 기사형 몬스터 |
| 등장 위치 | 훈련장 / 공방 구역 / 초반 필드 |
| 포지션 | 중급 야전 기사. 배럴 나이트 시리즈 리더 |

### 2. Workshop Guardian (공방 수호자)

| 항목 | 값 |
|---|---|
| 베이스 몹 | ZOMBIE |
| 장비 슬롯 | HELMET ← mob_barrelknight_body_01 |
| 주무기 | 곡괭이 / 도끼 계열 |
| 콘셉트 | 공방/제련소 구역을 지키는 목제 수호 몬스터 |
| 등장 위치 | 영지 내 공방 구역 / 던전 전초 구역 |
| 포지션 | 공방 연출 보조 몬스터. 배럴 나이트보다 둔중한 느낌 |

### 3. Wooden Guard (목제 경비병)

| 항목 | 값 |
|---|---|
| 베이스 몹 | ZOMBIE |
| 장비 슬롯 | HELMET ← mob_barrelknight_body_01 |
| 주무기 | 나무 창 (spear_carnivoret_poro_01) 또는 vanilla 창 |
| 콘셉트 | 목재 장비로 무장한 하급 경비 몬스터 |
| 등장 위치 | 훈련장 입구 / 영지 외곽 |
| 포지션 | 최약체 배치 몬스터. 창과 조합 시 포식자 시리즈 시각 통일 |

---

## 포식자 시리즈 세트 연계 가능성

Carnivoret 무기 세트(포식자 시리즈)와 배럴 나이트 장비를 조합하면 동일 세트 연출이 가능하다.

| 자산 | asset_id | 슬롯 |
|---|---|---|
| 포식자의 망치 | hammer_carnivoret_poro_01 | MAINHAND |
| 포식자의 창 | spear_carnivoret_poro_01 | MAINHAND |
| 포식자의 낫 | scythe_carnivoret_poro_01 | MAINHAND |
| 배럴 나이트 갑옷 | mob_barrelknight_body_01 | HELMET |

> Wooden Guard에 spear_carnivoret_poro_01 + mob_barrelknight_body_01 조합 시
> "포식자 목제 병사" 시리즈 몬스터 연출 가능.
