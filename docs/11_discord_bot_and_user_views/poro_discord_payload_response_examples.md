# 포로 서버 디스코드 봇 payload / response 예시 초안

## 문서 목적
이 문서는 디코 슬래시 명령이 실제로 어떤 payload를 받고 어떤 response를 반환하면 좋은지 예시를 정리한 초안이다.

핵심 목표:
- 구현 전에 명령 입출력 구조를 고정한다.
- slash command 핸들러와 응답 builder를 쉽게 만들게 한다.

---

## 1. `/내장비` payload 예시
```json
{
  "command": "내장비",
  "userId": "u_1001",
  "options": {
    "상세여부": false
  }
}
```

### response 예시
```json
{
  "title": "현도의 장비 요약",
  "classId": "warrior",
  "weaponId": "greatsword",
  "totalEnhancement": 87,
  "setSummary": "황성의 위광 4세트",
  "topPotentials": ["핵심 태그 피해 +8%", "최대 체력 +6%"]
}
```

## 2. `/시세` payload 예시
```json
{
  "command": "시세",
  "userId": "u_1001",
  "options": {
    "아이템명": "용암 결정핵"
  }
}
```

### response 예시
```json
{
  "title": "시세 정보: 용암 결정핵",
  "itemId": "lava_core",
  "avgPrice": 1980,
  "tradeVolume": 37,
  "changeRate": 4.2,
  "relatedTheme": "남부 T2 장비"
}
```

## 3. 한 줄 요약

**디코 봇 명령은 command/options payload와 카드형 요약 response DTO를 미리 정리해두면 구현이 훨씬 단순해진다.**
