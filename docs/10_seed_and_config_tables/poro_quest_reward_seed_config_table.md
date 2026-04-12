# 포로 서버 퀘스트 보상 seed / config 표 초안

## 문서 목적
이 문서는 퀘스트 보상을 데이터 기반으로 관리하기 위한 seed/config 표 초안이다.

핵심 목표:
- 메인/서브/생활/영지/일일 퀘스트 보상을 통일된 구조로 관리한다.
- 골드, 아이템, 해금, 경험치, 기록 보상을 함께 담는다.
- Codex가 quest reward resolver를 쉽게 만들 수 있게 한다.

---

## 1. 추천 필드
- `quest_id`
- `reward_order`
- `reward_type`
- `reward_target_id`
- `reward_amount`
- `is_first_clear_only`
- `notes`

## 2. reward_type 예시
- `gold`
- `item`
- `life_exp`
- `unlock`
- `title`
- `badge`
- `record`

## 3. 예시
| quest_id | order | reward_type | reward_target_id | amount | first_only | notes |
|---|---:|---|---|---:|---|---|
| q_life_intro_01 | 1 | gold | gold | 250 | false | 생활 입문 |
| q_life_intro_01 | 2 | item | refined_herb | 1 | false | 정제 약초 |
| q_estate_unlock_02 | 1 | unlock | estate_access | 1 | true | 영지 해금 |
| q_estate_unlock_02 | 2 | item | blueprint_basic_herb | 1 | true | 기본 청사진 |
| q_clear_region_south | 1 | title | title_ragnes_clear | 1 | true | 남부 결전 칭호 |

## 4. 한 줄 요약

**퀘스트 보상은 gold/item/life_exp/unlock/title 같은 reward_type을 가진 seed/config 구조로 관리하면, 메인·서브·생활·영지·일일 퀘스트를 모두 같은 방식으로 처리할 수 있다.**
