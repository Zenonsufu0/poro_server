# mob_barrelknight_body_01 — MythicMobs 장비 적용 예시

> 이 문서는 개념 예시(draft)이다.
> 실제 MythicMobs 몹 파일 수정은 이 단계에서 진행하지 않는다.
> 구현 준비 시 server/plugins/MythicMobs/Mobs/ 경로에 별도 적용한다.

---

## 아이템 지급 형식

MythicMobs Equipment 슬롯에 아이템을 지정할 때 드롭/장착 아이템은
`{item}` 또는 내장 ItemStack 방식으로 정의한다.
CMD 적용은 NBT 또는 컴포넌트 방식으로 지정한다.

```yaml
# 개념 예시 — 실제 syntax는 MythicMobs 버전에 따라 다를 수 있음
BarrelKnight:
  Type: SKELETON
  Display: '배럴 나이트'
  Health: 30
  Damage: 4
  Equipment:
  # HELMET 슬롯에 barrel armor 모델 적용
  # actual_slot: HELMET / visual_role: body armor (barrel)
  - 'barrel_knight_helmet HELMET'
  Options:
    PreventOtherDrops: true
    PreventItemPickup: true
```

```yaml
# MythicMobs Items 정의 예시
barrel_knight_helmet:
  Id: PAPER
  CustomModelData: 200101
  Display: '배럴 나이트 갑옷'
  Options:
    Unbreakable: true
```

---

## 몬스터 3종 예시 구성

### 1. Barrel Knight (배럴 나이트)

```yaml
BarrelKnight:
  Type: SKELETON
  Display: '배럴 나이트'
  Health: 30
  Damage: 4
  Equipment:
  - 'barrel_knight_helmet HELMET'
  - 'stone_sword MAINHAND'   # vanilla 또는 포식자 무기 치장 적용 가능
  Options:
    PreventOtherDrops: true
```

### 2. Workshop Guardian (공방 수호자)

```yaml
WorkshopGuardian:
  Type: ZOMBIE
  Display: '공방 수호자'
  Health: 45
  Damage: 6
  Equipment:
  - 'barrel_knight_helmet HELMET'
  - 'iron_pickaxe MAINHAND'
  Options:
    PreventOtherDrops: true
    PreventItemPickup: true
```

### 3. Wooden Guard (목제 경비병) — 포식자 시리즈 무기 연계

```yaml
WoodenGuard:
  Type: ZOMBIE
  Display: '목제 경비병'
  Health: 20
  Damage: 3
  Equipment:
  - 'barrel_knight_helmet HELMET'
  # spear_carnivoret_poro_01 — TRIDENT 기반 치장
  - 'wooden_guard_spear MAINHAND'
  Options:
    PreventOtherDrops: true

wooden_guard_spear:
  Id: TRIDENT
  CustomModelData: 100301
  Display: '포식자의 창'
  Options:
    Unbreakable: true
```

---

## 주의 사항

- `CustomModelData: 200101` 은 prop_barrel_poro_01에서 재사용하는 CMD 값이다.
  실제 적용 전 prop_registry.yml → mob_equipment_registry.yml 재등록 여부 확인.
- MythicMobs 1.21.4 버전에서 컴포넌트 기반 아이템 정의(`custom_model_data` 컴포넌트)로 변경된 경우 syntax 검토 필요.
- SKELETON 베이스는 자연 소환 시 기본 활을 들 수 있으므로 `PreventItemPickup: true` 권장.
- 실제 몹 파일 작성 전 EmpireRPG 몬스터 관리 방식(MythicMobs 우선 / 자체 등록 혼용)과 충돌 여부 확인.
