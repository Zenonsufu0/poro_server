# 포로 서버 관리자 / 테스트 명령 구조 초안

## 문서 목적
이 문서는 포로 서버의 결전 보스 / 극상위 보스 QA를 위한 **관리자용 테스트 명령 체계**를 정리한 초안이다.

목표는 다음과 같다.

- 보스 패턴을 빠르게 반복 테스트할 수 있게 한다.
- 페이즈 전환, 무력화, 발악 패턴을 강제로 검증할 수 있게 한다.
- 1인 / 2인 / 3인 보정, 클리어 타임, 실패 원인을 쉽게 확인할 수 있게 한다.
- 개발 중 디버깅과 밸런싱 시간을 크게 줄인다.

---

## 1. 기본 원칙

### 1-1. 관리자 명령은 “개발 도구”다
이 명령들은 운영용 치트가 아니라 QA 도구다.

즉 목적은:
- 재현
- 검증
- 밸런스 조정
- 버그 확인

이다.

---

### 1-2. 전투를 처음부터 다시 돌리지 않아도 돼야 한다
보스 QA에서 제일 큰 낭비는
- 패턴 하나 보려고 5분 이상 다시 싸우는 것
- 3페이즈 확인하려고 1페이즈부터 계속 보는 것

이다.

그래서 **특정 패턴 / 특정 페이즈 / 특정 상태를 바로 강제할 수 있어야 한다.**

---

### 1-3. 수치 변경과 상태 변경은 분리해야 한다
예를 들어
- 체력 변경
- 페이즈 변경
- 패턴 강제 발동
- 무력화 상태 강제
- 인원 보정 변경

이걸 한 명령에 다 몰아넣지 말고 분리하는 게 좋다.

---

## 2. 명령 분류 구조

추천은 크게 7개로 나눈다.

1. 보스 소환 / 입장 명령
2. 전투 상태 제어 명령
3. 페이즈 제어 명령
4. 패턴 강제 발동 명령
5. 밸런스 / 수치 테스트 명령
6. 로그 / 디버그 출력 명령
7. 초기화 / 정리 명령

---

## 3. 보스 소환 / 입장 명령

### 목적
- 특정 보스를 바로 테스트
- 특정 테마/보스룸 입장
- 인스턴스 생성 검증

### 추천 명령 예시
- `/boss test spawn <boss_id>`
- `/boss test room <boss_id>`
- `/boss test enter <boss_id>`
- `/boss test leave`
- `/boss test start <boss_id>`

### 필요한 기능
- 보스룸 바로 생성
- 위치 이동
- 전투 시작 전 대기 상태 진입
- 일반 입장 조건 무시

### 추천 옵션
- `solo`
- `party2`
- `party3`
- `story`
- `final`
- `extreme`

예:
- `/boss test start morgvain solo`
- `/boss test start agner party3`

---

## 4. 전투 상태 제어 명령

### 목적
- 전투 진행 중 보스/플레이어 상태를 빠르게 조정
- 특정 상황 재현

### 추천 명령 예시
- `/boss state hp <value>`
- `/boss state shield <value>`
- `/boss state invincible <on/off>`
- `/boss state stun <seconds>`
- `/boss state enrage <on/off>`
- `/boss state reset`

### 플레이어 관련 예시
- `/boss player heal`
- `/boss player kill`
- `/boss player clearstatus`
- `/boss player sethp <value>`
- `/boss player givebuff <type>`
- `/boss player debuff <type> <value> <duration>`

### 필요한 이유
예:
- 발악 패턴 직전 체력 만들기
- 무력화 상태 강제
- 특정 상태이상 중첩 검증
- 죽었을 때 리셋 로직 확인

---

## 5. 페이즈 제어 명령

### 목적
- 특정 페이즈만 바로 테스트
- 페이즈 전환 연출 / 조건 검증

### 추천 명령 예시
- `/boss phase set <phase_number>`
- `/boss phase next`
- `/boss phase prev`
- `/boss phase list`
- `/boss phase trigger`

### 예시
- `/boss phase set 3`
- `/boss phase trigger`

### 필요한 기능
- 체력 구간 무시하고 페이즈 강제 진입
- 페이즈 전환 연출 포함 / 제외 선택 가능
- 페이즈 진입 시 필요한 전장 오브젝트까지 같이 세팅

### 꼭 필요한 옵션
- `--animation on/off`
- `--fullsetup on/off`

예:
- `/boss phase set 2 --animation off --fullsetup on`

이 기능이 있어야  
실제 페이즈 상태를 제대로 검증할 수 있다.

---

## 6. 패턴 강제 발동 명령

### 목적
- 원하는 패턴만 반복 테스트
- 특정 패턴 버그/판독/UI 검증

### 추천 명령 예시
- `/boss pattern cast <pattern_id>`
- `/boss pattern queue <pattern_id>`
- `/boss pattern repeat <pattern_id> <count>`
- `/boss pattern stop`
- `/boss pattern list`

### 예시
- `/boss pattern cast frost_mark`
- `/boss pattern repeat arena_split 5`

### 필요한 이유
이게 없으면
- 설혼 안개만 계속 보고 싶을 때
- 분화선만 보고 싶을 때
- 레이저 구획 분할만 테스트하고 싶을 때

엄청 불편하다.

### 추가 추천 옵션
- 대상 지정:
  - `self`
  - `target`
  - `all`
  - `random`
- 난수 고정:
  - `seed`

예:
- `/boss pattern cast verdict_mark target`
- `/boss pattern cast mirage_safezone random --seed 12`

---

## 7. 밸런스 / 수치 테스트 명령

### 목적
- 체력, 방어력, 피해량, 타이머 등을 바로 조정
- 보스 체감 난이도 빠르게 체크

### 추천 명령 예시
- `/boss balance hpmult <value>`
- `/boss balance def <value>`
- `/boss balance dmgmult <value>`
- `/boss balance timer <seconds>`
- `/boss balance partyscale <1|2|3>`
- `/boss balance cooldown <pattern_id> <seconds>`

### 예시
- `/boss balance hpmult 0.5`
- `/boss balance partyscale 3`
- `/boss balance cooldown final_core 8`

### 필요한 이유
알파/베타 때
- 체력이 너무 높은지
- 패턴 간격이 너무 짧은지
- 3인 보정이 과한지

를 빨리 확인할 수 있어야 한다.

---

## 8. 로그 / 디버그 출력 명령

### 목적
- 지금 어떤 상태인지 바로 확인
- 버그 리포트 남기기 쉽게 만들기

### 추천 명령 예시
- `/boss debug on`
- `/boss debug off`
- `/boss debug state`
- `/boss debug pattern`
- `/boss debug target`
- `/boss debug log save`

### 출력 추천 항목
- 현재 페이즈
- 현재 체력/보호막
- 현재 활성 패턴
- 다음 예약 패턴
- 현재 전장 위험 상태
- 현재 파티 규모 보정
- 현재 플레이어 표식 / 디버프 상태
- 현재 무력화 가능 여부

### 추천 로그 저장 정보
- 시간
- 패턴 이름
- 대상
- 성공/실패 여부
- 사망 원인
- 전멸 원인

---

## 9. 초기화 / 정리 명령

### 목적
- 테스트 종료 후 즉시 원상복구
- 인스턴스 꼬임 방지

### 추천 명령 예시
- `/boss reset`
- `/boss cleanup`
- `/boss room reset`
- `/boss room delete`
- `/boss player resetall`

### 필요한 이유
보스 테스트는
- 남은 장판
- 남은 소환체
- 남은 표식
- 남은 UI
때문에 자주 꼬인다.

그래서 “완전 초기화” 명령이 반드시 있어야 한다.

---

## 10. 꼭 있어야 하는 QA 프리셋

명령을 매번 길게 치는 건 불편하니까  
프리셋도 추천한다.

### 추천 프리셋
- `/boss qa basic`
- `/boss qa phase2`
- `/boss qa final`
- `/boss qa enrage`
- `/boss qa wipecheck`
- `/boss qa solo`
- `/boss qa party3`

### 예시 기능
#### `/boss qa basic`
- 체력 100%
- 페이즈 1
- UI 켜짐
- 디버그 끔

#### `/boss qa final`
- 마지막 페이즈 직전
- 발악 패턴 준비
- 플레이어 체력/버프 기본화

#### `/boss qa wipecheck`
- 전멸기 직전 상태
- 실패 처리 로직 검증용

---

## 11. 보스별 꼭 테스트해야 하는 전용 항목

### 수도권
- 죄목 선고 누적
- 판결 스택 증가/감소
- 잘못된 구획 진입 판정
- 최종 판결 실패 처리

### 서부 황야
- 폭풍 장벽 이동 판정
- 함몰지대 붕괴 타이밍
- 유사 안전지대 판정
- 폭풍핵 폭주 카운트다운

### 동부 숲
- 본체/분신 판별 힌트
- 안개 분리 시 가시성
- 생장 역류 경로 차단
- 세계수 심핵 폭주

### 북부 설원
- 설혼 안개 시야 감소 강도
- 백빙 낙인 폭발 범위
- 혹한 외곽 틱 피해
- 백빙 심핵 최종 노출

### 남부 화산
- 분화선 경고 가시성
- 낙인 연쇄폭발 판정
- 과열 단계 상승 속도
- 심핵 폭주 발악 타이머

### 사하르 상인연합
- 가짜 안전지대 구분 가능성
- 환영 상단 판별 가능성
- 가짜 클리어 연출
- 진실 페이즈 전환 안정성

### 아르카논
- 레이저 구획 분할 판정
- 안전 구역 재배치 가시성
- 공명 붕괴 연결선 판정
- 심핵 붕괴 임계 발악 처리

---

## 12. 알파 테스트 추천 흐름

### 1차
- 보스룸 / 전투 시작 / 종료
- 기본 패턴
- 페이즈 전환
- 무력화 성공/실패
- 발악 진입/종료

### 2차
- 전용 기믹
- UI/알림 확인
- 1인 / 2인 / 3인 보정
- 사망/전멸/리스폰 처리

### 3차
- 로그 저장
- 기록 반영
- 첫 클리어 연출
- 반복 클리어 연출
- 서버 기록 반영

---

## 13. 베타 테스트 추천 체크리스트

### 전투 구조
- 15분 제한 정상 작동?
- 1인 / 2인 / 3인 체력 보정 정상?
- 보스 리셋 시 꼬임 없는가?

### 패턴
- 경고가 충분히 보이는가?
- 피할 수 있는가?
- 억까처럼 느껴지는가?

### UI
- 표식 대상이 명확한가?
- 발악 패턴 시간이 잘 보이는가?
- 안전지대가 읽히는가?

### 기록
- 최초 클리어 기록 저장되는가?
- 실패 로그 남는가?
- 디스코드/웹 연동 가능한 형태인가?

---

## 14. 현실적인 최소 명령 세트

이건 정말 최소다.

### 반드시 필요한 명령
- `/boss test start <boss_id>`
- `/boss phase set <n>`
- `/boss pattern cast <pattern_id>`
- `/boss state hp <value>`
- `/boss debug state`
- `/boss reset`

이 6개만 있어도 QA 생산성이 크게 오른다.

---

## 15. 추천 최종 명령 체계 요약

### 전투 시작
- `/boss test start`

### 상태 조절
- `/boss state ...`

### 페이즈 조절
- `/boss phase ...`

### 패턴 강제
- `/boss pattern ...`

### 밸런스 조절
- `/boss balance ...`

### 디버그 출력
- `/boss debug ...`

### 정리
- `/boss reset`
- `/boss cleanup`

---

## 16. 다음으로 가야 할 것

### 1순위
DB / 웹 / 디스코드 노출 항목 정리
- 업적 기록
- 최초 클리어 기록
- 보스별 실패 통계
- 명예의 전당 표시
- 시즌 기록 구조

### 2순위
테마별 보이스/사운드 방향 정리
- 전용 기믹 대표 사운드
- 발악 패턴 전용 사운드
- 클리어/실패 연출 사운드

### 3순위
운영자용 밸런스 조정 표 정리
- 체력
- 방어력
- 피해 배수
- 상태이상 시간
- 무력화 시간
- 발악 타이머
