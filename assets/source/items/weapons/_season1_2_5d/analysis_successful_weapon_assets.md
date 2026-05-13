# 성공한 무기 에셋 분석 보고서

작성일: 2026-05-11  
목적: 기존 성공 에셋의 생산 규칙을 추출해 새 창 제작에 재활용

---

## 1. 분석 대상 요약표

| 에셋 | elements | X span | Y span | Z span | Z-style | 텍스처 | bad UV | max UV |
|---|---|---|---|---|---|---|---|---|
| sword_vanquisher_poro_01 | 32 | 2.5 | 42.5 | 15.0 | chunky 3D | 단일 64×64 | 0 | 12.5 |
| sword_adventurer_poro_01 | 5 | 7.0 | 35.3 | 2.0 | chunky 3D | 단일 64×64 | 0 | 13.25 |
| spear_adventurer_poro_01 | 15 | 8.0 | 35.0 | 2.0 | **flat 2.5D** | 단일 64×64 | 0 | 14.5 |
| spear_carnivoret_poro_01 | 30 | 3.85 | 42.1 | 19.8 | chunky 3D | 단일 64×64 | 0 | 10.375 |
| staff_carnivoret_poro_01 | 32 | 9.7 | 43.9 | 11.25 | chunky 3D | 단일 64×64 | 0 | 10.125 |
| hammer_carnivoret_poro_01 | 72 | 16.25 | 46.4 | 24.0 | chunky 3D | 단일 64×64 | 0 | 12.25 |

---

## 2. 에셋별 상세 분석

### 2-1. sword_vanquisher_poro_01

- **element 수:** 32 — 핵심 shaft(1) + 의장용 pommel/guard/tip 조각(31)
- **geometry vs texture 분리:**
  - geometry: shaft, grip, guard 기본 볼륨을 cube로 구성
  - texture: 64×64 단일 시트에 재질별 컬러 영역 배분 (금속/목재/장식)
- **Z depth:** 0.5~15 — 3D 입체. shaft는 Z=1, guard/pommel은 Z~15 풀 입체
- **display 키:** 8개 (thirdperson R/L, firstperson R/L, ground, gui, head, fixed)
- **head:** scale=[0,0,0] — 머리 착용 비활성화 안전처리
- **왜 잘 작동하나:** 회전(±22.5°, ±45°) 적극 사용으로 곡선·비틀림 표현. UV는 64×64 공간을 0~12.5 범위만 사용해 여유 있게 분포.

### 2-2. sword_adventurer_poro_01

- **element 수:** 5 — 가장 단순한 구조
- **geometry vs texture 분리:**
  - geometry: blade body + guard + pommel 3파트를 3개 큐브, tip 장식 2개
  - texture: 64×64 단일 시트, 각 파트별 영역 배분
- **Z depth:** 약 2.0 — 2.5D에 가깝지만 guard는 Z 확장
- **45° z-rotation 사용:** blade_top에 45° z-rotation으로 다이아몬드 끝 표현
- **왜 잘 작동나:** element 5개만으로 충분한 실루엣. 64×64 텍스처로 상세도 확보.

### 2-3. spear_adventurer_poro_01

- **element 수:** 15 — 창 표준 구조
- **geometry vs texture 분리:**
  - geometry: spearhead(tip/step2/step3/lower 4단 계단형), lug L/R + hook, shaft_collar, spear_socket, shaft_upper, shaft_lower, grip_wrap×2, butt_cap
  - texture: 64×64 단일 시트. spearhead(UV [0.5,0.5,2.5,2.5]), lug(UV [4.5,0.5,6.5,2.5]), shaft(UV [8.5,0.5,10.5,2.5]), grip(UV [12.5,0.5,14.5,2.5]) — **파트별 세로 열로 구획**
- **Z depth:** 2.0 (Z=7.5→8.5) — 완전 flat 2.5D
- **회전 없음** — 모든 element가 축 정렬
- **왜 잘 작동하나:** 창 길이가 긴 무기 특성상 flat 2.5D가 인게임에서 더 자연스럽게 보임. 계단형 spearhead로 뾰족한 실루엣 표현. UV 배치가 파트별로 명확히 구획되어 텍스처 작업이 쉬움.

### 2-4. spear_carnivoret_poro_01

- **element 수:** 30 — 기괴한 형태의 복합 창
- **geometry vs texture 분리:**
  - geometry: 복잡한 뼈/유기체 형태를 cube rotation(-22.5°, +22.5°, ±45°)으로 구성
  - texture: 64×64 단일 시트, UV 0~10.375 범위
- **Z depth:** 19.8 — 풀 3D, 장식 파트들이 Z축으로 확장
- **특이점:** zero-thickness plane(from.x == to.x 또는 from.z == to.z)이 장식선으로 사용됨 — **단, 이는 의도적 설계로 ref_spec에 명시된 경우만 허용**
- **왜 잘 작동하나:** 회전 조합으로 유기적 곡선 표현. 64×64 단일 텍스처로 세밀한 재질 표현.

### 2-5. staff_carnivoret_poro_01

- **element 수:** 32 — spear_carnivoret 구조와 유사
- **geometry vs texture 분리:**
  - geometry: staff head는 spear_carnivoret의 grip 영역과 유사한 복합 구조, 두 개의 직교 평면 element로 X자 orb 표현
  - texture: 64×64 단일 시트, UV 0~10.125
- **Z depth:** 11.25 — 3D
- **특이점:** `from [3.15, y, 7.975]` `to [12.85, y, 8.025]` — X축으로 9.7 span의 orb wing. Z는 0.05 두께 (매우 얇은 평면) — 의도적 설계
- **particle 텍스처 없음** — textures에 `particle` 키 부재. staff_carnivoret만 해당

### 2-6. hammer_carnivoret_poro_01

- **element 수:** 72 — 가장 복잡한 모델
- **geometry vs texture 분리:**
  - geometry: 해머 헤드(큰 볼륨)를 다수의 세그먼트로 분할해 organic 형태 구성
  - texture: 64×64 단일 시트, UV 0~12.25
- **Z depth:** 24.0 — 가장 입체적인 모델
- **display 키:** 8개 모두 포함 (head scale=0 포함)
- **왜 잘 작동하나:** 72개 element가 많아 보이지만, 해머 특성상 무거운 볼륨 표현에 필요. UV 분포가 64×64 공간 내 12.25까지만 사용해 여백 확보.

---

## 3. 공통 성공 규칙

### 3-1. UV 규칙 (가장 중요)

| 규칙 | 이유 |
|---|---|
| 모든 face UV 값 0~16 이내 유지 | Minecraft 렌더러의 유효 UV 범위. 초과 시 broken/fence 렌더링 버그 |
| 64×64 atlas UV는 0~16 범위 내에서만 사용 | texture_size [64,64]는 단지 해상도, UV 좌표계는 0~16 고정 |
| 절대 금지: UV v값이 32, 48, 64 등 16 초과 | scythe 이전 사고 원인 — atlas-space UV 혼용 |

### 3-2. 텍스처 규칙

| 규칙 | 이유 |
|---|---|
| **64×64 단일 PNG** 사용 (6개 모두) | 4× 해상도 향상, 단일 draw call |
| 파트별 UV 영역을 텍스처 내에서 명확히 구획 | 작업 편의성 + 나중에 수정 시 충돌 방지 |
| texture_size: [64, 64] 명시 필수 | 없으면 Minecraft가 16×16으로 가정 |
| particle 텍스처는 blade 또는 주요 면 텍스처로 지정 | staff_carnivoret만 예외적으로 particle 없음 |

### 3-3. Geometry 규칙

| 규칙 | 이유 |
|---|---|
| 실루엣은 geometry로, 질감은 texture로 | geometry 없는 순수 texture 표현은 한 면만 보임 |
| Z depth: 무기 특성에 맞게 결정 | 창/지팡이는 flat 2.5D(Z=1~2), 해머/검은 chunky 3D |
| 회전(±22.5°, ±45°)은 Blockbench에서 직접 조정 | 수치 직접 계산 금지 — visual approval 필수 |
| zero-thickness plane은 의도적 설계일 때만 | ref_spec.json에 명시된 경우만 허용 |

### 3-4. display 규칙

| 규칙 |
|---|
| display 블록은 사용자가 직접 조정, AI는 절대 수정하지 않는다 |
| 8개 display 키 모두 포함: thirdperson R/L, firstperson R/L, ground, gui, head, fixed |
| head는 scale=[0,0,0]으로 비활성화 (안전 처리) |
| export JSON에는 bbmodel의 display 블록을 그대로 복사 |

---

## 4. 위험 패턴 목록

| 패턴 | 위험도 | 설명 |
|---|---|---|
| UV v=32, v=48, v=64 등 16 초과 값 | 🔴 치명 | Minecraft broken/fence 렌더링 버그 직접 원인 |
| 16×64 또는 64×64 통합 atlas UV | 🔴 치명 | UV가 16을 초과하게 됨 |
| texture_size 미명시 | 🟠 높음 | 64×64 텍스처를 16×16으로 잘못 렌더링 |
| display AI 자동 생성 | 🟠 높음 | 인게임 손 위치 틀어짐 |
| particle 텍스처 누락 | 🟡 중간 | 피격 파티클 누락 (staff_carnivoret 제외) |
| zero-thickness plane (의도 외) | 🟡 중간 | 한 면이 invisible, Blockbench 경고 |
| head scale 누락 | 🟡 중간 | 머리 착용 시 이상한 렌더링 |
| elements가 Minecraft 렌더 한계(~112개) 초과 | 🟠 높음 | 일부 element 무시됨 |

---

## 5. 새 창(Spear) 제작 템플릿

### 5-1. 파트 구성 (spear_adventurer_poro_01 기반)

```
spearhead_tip      : 날 끝 (가장 좁음)
spearhead_step2    : 날 2단
spearhead_step3    : 날 3단
spearhead_lower    : 날 하단 (가장 넓음)
lug_left           : 왼쪽 걸이
lug_left_hook      : 왼쪽 걸이 훅
lug_right          : 오른쪽 걸이
lug_right_hook     : 오른쪽 걸이 훅
shaft_collar       : 자루-창두 연결 고리
spear_socket       : 소켓
shaft_upper        : 자루 상부
shaft_lower        : 자루 하부
grip_wrap_front    : 손잡이 전면
grip_wrap_back     : 손잡이 후면
butt_cap           : 자루 끝 캡
```

최소 15 elements. 장식 추가 시 최대 ~30 elements.

### 5-2. 텍스처 UV 구획 (64×64 단일 PNG)

spear_adventurer_poro_01의 UV 구획 방식을 그대로 재사용:

| UV 열 (U 범위) | 대상 파트 |
|---|---|
| 0.5 ~ 2.5 | spearhead (날) |
| 4.5 ~ 6.5 | lug / butt_cap / socket (금속 파츠) |
| 8.5 ~ 10.5 | shaft_upper / shaft_lower (자루) |
| 12.5 ~ 14.5 | grip_wrap (손잡이) |

V 범위: 0~2.5 기본. 상세 디테일 추가 시 V 4.5까지만 사용 (16 이내 유지).

### 5-3. Z depth 전략

| 스타일 | Z span | 예시 | 추천 상황 |
|---|---|---|---|
| flat 2.5D | Z = 1.0 | from.z=7.5, to.z=8.5 | 클래식 창, 가늘고 긴 창 |
| slim 3D | Z = 2.0~3.0 | from.z=7, to.z=9 | 창두에 두께 표현이 필요할 때 |
| chunky 3D | Z = 4.0+ | from.z=6, to.z=10 | 특수 창, 판타지 창 |

**새 창 기본값: Z=1.0 (flat 2.5D, spear_adventurer_poro_01 답습)**

### 5-4. 권장 export JSON 구조

```json
{
  "credit": "Made with Blockbench",
  "texture_size": [64, 64],
  "textures": {
    "0": "poro:item/weapons/<asset_id>",
    "particle": "poro:item/weapons/<asset_id>"
  },
  "elements": [ ... ],
  "gui_light": "front",
  "display": {
    "thirdperson_righthand": { ... },
    "thirdperson_lefthand":  { ... },
    "firstperson_righthand": { ... },
    "firstperson_lefthand":  { ... },
    "ground":  { ... },
    "gui":     { ... },
    "head":    { "scale": [0, 0, 0] },
    "fixed":   { ... }
  }
}
```

---

## 6. 검증 체크리스트

export 전 매번 실행:

```python
import json
with open("<asset_id>.json") as f:
    mc = json.load(f)

# 1. UV 범위 검증
bad = 0
for el in mc["elements"]:
    for fn, fd in el["faces"].items():
        for v in fd["uv"]:
            if v < 0 or v > 16:
                bad += 1
print(f"bad UV count: {bad}")  # 반드시 0

# 2. texture_size 확인
assert mc.get("texture_size") == [64, 64], "texture_size 누락 또는 오류"

# 3. display 키 확인
required_display = {"thirdperson_righthand","thirdperson_lefthand",
                    "firstperson_righthand","firstperson_lefthand",
                    "ground","gui","head","fixed"}
missing = required_display - set(mc.get("display", {}).keys())
print(f"display 누락 키: {missing}")  # 빈 집합이어야 함

# 4. particle 텍스처 확인
assert "particle" in mc.get("textures", {}), "particle 텍스처 누락"

# 5. element 수 확인
print(f"elements: {len(mc['elements'])}")  # 112 이하 권장
```

---

## 7. scythe_adventurer_manual_01 비교 메모

| 항목 | scythe_manual_01 | 기존 성공 에셋 | 결론 |
|---|---|---|---|
| 텍스처 방식 | 재질별 16×16 분리 (blade/metal/wood/grip) | 단일 64×64 PNG | 둘 다 valid. UV가 0~16 이내면 OK |
| bad UV | 0 ✓ | 0 ✓ | 동일하게 안전 |
| texture_size | 없음 (16×16 기본값) | [64, 64] 명시 | scythe는 16×16 기반이라 texture_size 불필요 |
| display 키 수 | 6개 (head/fixed 없음) | 8개 | scythe에 head=[0,0,0], fixed 추가 권장 |
| 텍스처 파일명 | `{id}_blade.png` 등 | `{id}.png` 단일 | scythe 방식은 Phase 2 텍스처 작업 시 유지 |
