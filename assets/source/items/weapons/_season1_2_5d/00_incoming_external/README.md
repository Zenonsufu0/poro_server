# 외부 자산 수령 임시 보관소

외부에서 받은 원본 zip/파일을 이 폴더에 먼저 배치한다.
압축 해제, 편집, 이름 변경 없이 원본 그대로 보관.

## 처리 절차

1. 받은 zip 파일을 이 폴더에 그대로 배치.
2. `weapon_cosmetic_registry.yml`에서 해당 자산의 `asset_id`와 `custom_model_data`를 먼저 등록.
3. 담당자가 해당 자산의 목표 `weapon_class`를 결정.
4. `_season1_2_5d/{weapon_class}/{asset_id}/` 폴더를 생성 (또는 이미 있으면 확인).
5. `original/` 폴더에 zip 압축 해제 내용물을 **복사** (원본 zip 유지).
6. `spec.yaml`, `notes.md`, `mapping_plan.md`를 채운다.
7. 처리 완료 후 이 폴더의 원본 zip은 별도 결정에 따라 유지 또는 삭제.

## 대기 중인 자산

| 파일명 | 목표 클래스 | asset_id | 상태 |
|---|---|---|---|
| Vanquisher_Sword-4fll7y.zip | sword | sword_vanquisher_poro_01 | 파일 미수령 대기 중 |
