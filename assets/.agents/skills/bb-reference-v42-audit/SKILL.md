---
name: bb-reference-v42-audit
description: 생성된 p1~p4 레퍼런스 이미지가 최신 ref-memory v4.2 규칙을 만족하는지 검사한다.
version: v12.0
---

# bb-reference-v42-audit

## 목표
모델 빌드 전에 레퍼런스 자체의 품질을 검증한다. 레퍼런스가 흔들리면 자동화도 흔들린다.

## HARD FAIL
```txt
[ ] p1/p2/p3/p4 중 하나 누락
[ ] p4 Z-Depth Profile & Detail Cube Library 누락
[ ] p2 Color Palette에 Neutral 포함 6색 없음
[ ] p2 Thickness Guide에 각 파트 2D_D 없음
[ ] p3 Dimension Summary 없음
[ ] p3 Validation Checklist 없음
[ ] category label 누락
[ ] total length 누락
[ ] primary axis 누락
[ ] pivot point 누락
[ ] coordinate axis diagram 누락
```

## Wing weapon 추가 HARD FAIL
```txt
[ ] Wing Step Diagram 없음
[ ] Step1이 guard 쪽 base feather가 아님
[ ] Step5가 outer tip feather가 아님
[ ] Y_RISE 없음
[ ] Wing Reach 검산 없음
[ ] Wing Cross Section 없음
[ ] floor constraint 없음
```

## 추천 개선
```txt
- p3 또는 p4 한쪽에 machine-readable ref_spec block 추가
- 모든 수치를 px로 명시
- 이미지 안 수치와 ref_spec.json을 함께 제공
- p1 헤더에 Weapon Name / Category / Set 3줄 고정
```

## 출력
```txt
Reference Audit: PASS/FAIL
Blockers: ...
Warnings: ...
Can run /bb-design: yes/no
```
