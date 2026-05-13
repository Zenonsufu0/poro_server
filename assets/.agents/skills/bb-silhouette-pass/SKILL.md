---
name: bb-silhouette-pass
description: Blockbench 무기 제작/리워크에서 가장 먼저 큰 덩어리와 실루엣을 평가하고 수정한다. 큐브/텍스처 디테일 전에 plain stick 문제를 막는 스킬.
version: v13.0
---

# bb-silhouette-pass

## 목적
세부 큐브와 텍스처 전에, 무기가 멀리서도 카테고리와 정체성을 읽히는지 검증한다.

이 스킬은 **기술 검증이 아니라 형태 검증**이다.

출력 파일:

```txt
silhouette_plan.md
silhouette_review.md
```

---

## 선행 입력

```txt
refs/ref_spec.json
refs/art_spec.json
refs/*_p1.png~p4.png
source/*.bbmodel 또는 blockout 모델
```

`refs/art_spec.json`이 없으면 먼저 `/bb-art-director`를 실행한다.

---

## PASS 전에는 금지

```txt
- 세밀한 텍스처 작업 금지
- UV 최종화 금지
- export final 금지
- tiny detail cube만 추가하는 방식 금지
```

실루엣이 약하면 작은 디테일을 늘려도 좋아지지 않는다.

---

## 실루엣 평가 방법

다음 뷰를 확인한다.

```txt
front view
side view
45-degree / isometric view
GUI 64px downscale
handheld approximate view
```

가능하면 스크린샷을 생성한다.

```txt
exports/screenshots/silhouette_front.png
exports/screenshots/silhouette_iso.png
exports/screenshots/silhouette_gui_64.png
```

---

## 점수표

각 항목 0~10점.

```txt
category_readability        무기 종류가 보이는가
identity_readability        variant 정체성이 보이는가
large_shape_rhythm          큰 덩어리 리듬이 있는가
focal_point_strength        중심 시선 포인트가 강한가
negative_space_quality      빈 공간/틈이 형태를 살리는가
not_plain_stick             막대기처럼 보이지 않는가
```

### 게이트

```txt
평균 8 이상: PASS
평균 7~7.9: MINOR REWORK
평균 6~6.9: MAJOR REWORK
6 미만: REBUILD SILHOUETTE
```

7점 미만이면 다음 build phase로 넘어가지 않는다.

---

## greatsword + wing 전용 체크

```txt
- 날개가 "작은 장식"이 아니라 guard 중심부의 주 실루엣으로 보이는가?
- blade가 긴 흰 막대가 아니라 tip/upper/mid/lower 리듬을 가지는가?
- guard가 중앙 금색 덩어리 하나로 뭉치지 않고, 좌우 어깨/링/보석이 읽히는가?
- pommel이 사각 블록이 아니라 작은 보석 장식으로 보이는가?
- 64px에서도 wing + gem + blade core 셋 중 최소 2개가 읽히는가?
```

---

## 리워크 권장 규칙

실루엣이 약하면 다음을 허용한다.

```txt
wing visual reach +1~2 px
guard shoulder side spread +0.5~1 px
center gem/socket front face emphasis
blade lower near-guard widening through decorative bevels
pommel ring inflate +0.5 px
tip shaping by removing/adding small cubes
```

단, 다음은 유지한다.

```txt
total_y
pivot
center_z
segments_y main range
category route
```

---

## 출력 형식

```txt
Silhouette Review: PASS / MINOR REWORK / MAJOR REWORK / REBUILD SILHOUETTE
Scores:
- category_readability: n/10
- identity_readability: n/10
- large_shape_rhythm: n/10
- focal_point_strength: n/10
- negative_space_quality: n/10
- not_plain_stick: n/10

Top 3 silhouette problems:
1.
2.
3.

Required edits before detail phase:
- part_name: edit instruction
```

