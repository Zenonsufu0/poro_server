# ZenonMonCore Data / Storage Schema (런타임 데이터 저장)

> ZenonMonCore가 **런타임에 쓰는 상태**(진행/티켓/룸 세션)의 저장 포맷. 상위: `module_structure.md` §3(data).
> config(`config_structure.md`)와 구분: config = 규칙/정의(사람이 편집), data = 플레이어/세션 상태(서버가 기록).
> 원칙: **서버 권위 저장**, 월드와 정합, Cobblemon 데이터와 **중복 저장 금지**.
> ⚠️ 코드/파일 미생성. 본 문서는 구현 기준 스키마.

## 1. 저장 메커니즘 선택

| 후보 | 설명 | 채택 |
|---|---|---|
| **Fabric `PersistentState`** (월드 저장에 부속) | 서버 종료/저장 시 월드와 함께 NBT 저장. 정합성↑ | ✅ 0.1 기본 |
| 플레이어별 JSON 파일 | `world/zenonmoncore/players/<uuid>.json` 등 사람이 읽기 쉬움 | 보조/디버그용 검토 |
| 외부 DB(Mongo 등) | 대규모/분산 시 | ❌ 초기 미사용 |

- **0.1 = PersistentState(월드 종속) 채택.** 월드 백업/롤백(`server_runbook.md`)과 자동 정합. Cobblemon도 월드+player NBT 저장이라 백업 단위가 일치.
- 저장 위치(개념): 오버월드 `DimensionDataStorage` 의 `zenonmoncore_*` 키.
- **포켓몬 파티/PC는 절대 ZenonMonCore가 저장하지 않음** — Cobblemon 소관. ZenonMonCore는 "참조 키(예: 진행 플래그, 배지)"만.

## 2. 저장 트리거
- 주기/이벤트: 서버 autosave, 월드 저장, 서버 `stop`.
- 중요 변경(티켓 사용/룸 배정/보상 지급) 직후 **dirty 마킹** → 다음 저장에 반영. 필요 시 즉시 flush.
- 플레이어 진행은 **UUID 키**(이름 변경 안전). 첫 접속 시 엔트리 생성.

## 3. 스키마 (0.1)

### 3-1. PlayerProgress  (UUID → 객체)
```jsonc
{
  "schemaVersion": 1,
  "uuid": "…",
  "firstJoinEpoch": 0,
  "lastKnownName": "PlayerName",    // 경제/운영 모니터 표시용 캐시
  "leaguePassGiven": true,        // 아이템 최초 지급 여부
  "badges": [],                    // 향후 gym: ["rock","water",...]
  "unlocks": {                     // 향후 mega: 해금 플래그
    "mega": false, "tera": false, "dynamax": false, "zmove": false
  },
  "tickets": [                     // 보유 티켓 (EncounterTicket)
    // 아래 3-2 참조
  ],
  "balance": 0,                    // 골드 잔액 (단일 화폐, economy_design.md)
  "titles": ["wealth_record"],      // 보유 칭호 id
  "activeTitle": "wealth_record",   // 현재 표시 칭호 id(없으면 "")
  "battleTower": { "highestClearedFloor": 0, "rewardedFloors": [] },
  "rankedLeague": { "score": 1000, "wins": 0, "losses": 0, "recentOpponents": [] },
  "stats": { "legendaryCaught": 0, "gymCleared": 0 },
  "flags": {}                      // 확장용 키-값
}
```

### 3-2. EncounterTicket  (PlayerProgress.tickets[] 항목)
```jsonc
{
  "ticketId": "uuid-v4",           // 인스턴스 고유 id
  "type": "legendary_basic",       // tickets.json 의 ticketType.id
  "acquiredEpoch": 0,
  "source": "admin",               // admin|gym|quest|shop|event
  "consumed": false,
  "consumedEpoch": null
}
```

### 3-3. RoomSession  (전역, 활성 룸 점유)
```jsonc
{
  "schemaVersion": 1,
  "sessions": [
    {
      "sessionId": "uuid-v4",
      "poolId": "legendary_default",   // rooms.json
      "instanceId": "room_1",
      "playerUuid": "…",
      "ticketId": "…",                 // 소모된 티켓 추적
      "startEpoch": 0,
      "expireEpoch": 0,                // startEpoch + maxSessionSeconds
      "state": "active"                // active|cleaning|done
    }
  ]
}
```
- 서버 시작 시 만료/고아 세션 정리(점유 룸을 풀로 반환). 룸 풀 가용성 = `instances - active sessions`.

### 3-4. EconomyStats  (전역 텔레메트리 — 가격 조정 근거)
> `economy_design.md` §6. 골드 흐름·판매/사용 빈도 집계. 가벼운 카운터 + 거래 로그(1차), SQLite는 선택.
```jsonc
{
  "schemaVersion": 1,
  "periodStartEpoch": 0,
  "goldFaucet": { "sell": 0, "gym": 0, "battleTower": 0, "wildPokemon": 0, "event": 0 },
  "goldSink":   { "ticket": 0, "megaStone": 0, "unlock": 0, "pokeball": 0, "heal": 0 },
  "goldInBySource": { "sell:minecraft:iron_ingot": 0 },
  "goldOutBySource": { "ticket:basic": 0 },
  "playerGoldIn": { "<uuid>": 0 },
  "playerGoldOut": { "<uuid>": 0 },
  "itemSellCount": { "minecraft:iron_ingot": 0 },   // 판매 수량
  "itemSellGold": { "minecraft:iron_ingot": 0 },    // 품목별 골드 생산량
  "itemBuyCount":  { "cobblemon:poke_ball": 0 },    // 구매/사용 수량
  "itemBuyGold":  { "cobblemon:poke_ball": 0 },     // 품목별 골드 소모량
  "economyTransactions": [
    {
      "epochMillis": 0,
      "playerUuid": "<uuid>",
      "playerName": "PlayerName",
      "delta": 0,
      "balanceAfter": 0,
      "source": "sell:minecraft:iron_ingot",
      "itemId": "minecraft:iron_ingot",
      "count": 0
    }
  ]
}
```
- 모든 골드 in/out은 `EconomyBridge`에서 **출처 태그**와 함께 카운트 → 위 집계 갱신.
- 최근 거래 이벤트는 PersistentState에 ring buffer로 보관(구현 기본 500건). 장기 상세 분석이 필요하면 SQLite로 승격(선택).
- 운영자 GUI `경제 모니터`는 총계/유저별/판매 품목별/소모처별/최근 거래 탭으로 위 집계를 표시한다.

### 3-5. TitleState  (전역 칭호 기록 + PlayerProgress 보유 칭호)
> 결정 052. 플레이어별 보유/활성 칭호는 `PlayerProgress`에, 전역 최초/최고 기록은 PersistentState 루트에 저장한다.
```jsonc
{
  "wealthRecord": {
    "uuid": "<uuid>",
    "name": "PlayerName",
    "balance": 0
  },
  "firstApexCatch": {
    "rayquaza": {
      "playerUuid": "<uuid>",
      "playerName": "PlayerName",
      "displayName": "레쿠쟈",
      "epochMillis": 0
    }
  }
}
```
- `wealth_current`: 현재 보유 골드 1위. 잔액 변화/접속 시 재계산하며 1위가 바뀌면 기존 보유자에게서 회수한다.
- `wealth_record`: 관측 최고 보유 골드 기록 갱신자에게 영구 지급한다.
- `first_apex:<species>`: 조우방에서 최상위 전설(`pool.type=apex` 또는 후보 `stage=apex`)을 종별로 최초 포획한 플레이어에게 영구 지급한다.
- 채팅 접두사는 1차 범위에서 제외. 표시 위치는 메인 메뉴 플레이어 정보와 `/zenonmon progress`, 선택 명령은 `/zenonmon title list|set|clear`.

## 4. 마이그레이션 / 호환
- 모든 루트 객체에 `schemaVersion`. 로드 시 버전 < 현재 → 업그레이드 함수 체인 적용.
- 알 수 없는 키는 보존(전방호환) 또는 `flags`로 흡수.
- 손상 데이터: 백업 후 기본값으로 복구(전체 wipe 금지), `AuditLog`에 기록.

## 5. 동시성 / 검증
- 서버 스레드에서만 접근(메인). 비동기 접근 금지.
- 티켓 소모·룸 배정은 **단일 트랜잭션처럼**: (1) 티켓 유효 검사 → (2) 빈 룸 확보 → (3) 티켓 consumed=true → (4) 세션 생성 → (5) 텔레포트. 중간 실패 시 롤백(룸 반환/티켓 복구).
- 모든 상태 변경은 서버측 검증 후에만(클라 신뢰 금지).

## 6. 관련
- 모듈: `module_structure.md` (data=PlayerProgressManager, room=InstanceRoomManager, encounter=EncounterTicketManager)
- 정의/규칙: `config_structure.md` (tickets.json/rooms.json)
- 백업/롤백 정합: `../02_server/server_runbook.md` §3~5
- 레전드 흐름: `../04_game_design/legendary_encounter.md`
