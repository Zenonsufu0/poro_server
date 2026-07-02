# 배포 / 운영 런북 (T8)

> **[STATUS: ACTIVE]** — 봇 구동·스테이징 검증 절차. 호스팅 위치는 무관(오라클·VPS·집서버·도커
> 어디든). 봇은 호스팅 비종속이며, 위치에 따라 달라지는 건 코드가 아니라 `.env` 값뿐이다.
> 코드 사실 기준(2026-07-01): discord.py 2.3.2 / Python 3.12 / aiohttp·mcstatus.

## 빠른 배포 (복붙)

`<user>`=VM 리눅스 유저, `<vm-ip>`=VM 외부 IP 만 바꾸면 됨. 상세·근거는 §1.1.

**① VM — 준비 + deploy key** (실행 후 나온 공개키를 GitHub repo → Settings → Deploy keys 에 Read-only 등록)
```bash
sudo apt update && sudo apt install -y git python3-venv
ssh-keygen -t ed25519 -f ~/.ssh/zenon_deploy -N ""
printf 'Host github.com\n  IdentityFile ~/.ssh/zenon_deploy\n  IdentitiesOnly yes\n' >> ~/.ssh/config
cat ~/.ssh/zenon_deploy.pub
```

**② VM — clone + 설치**
```bash
git clone git@github.com:Zenonsufu0/zenon-server.git
cd ~/zenon-server/zenon-discord && python3 -m venv .venv && .venv/bin/pip install -r requirements.txt
```

**③ 로컬 PC — .env 복사**
```bash
scp zenon-discord/.env <user>@<vm-ip>:~/zenon-server/zenon-discord/.env
```

**④ VM — 서비스 등록·기동**
```bash
sudo sed "s/<USER>/$USER/g" ~/zenon-server/zenon-discord/deploy/yuki-bot.service | sudo tee /etc/systemd/system/yuki-bot.service >/dev/null
sudo systemctl daemon-reload && sudo systemctl enable --now yuki-bot
journalctl -u yuki-bot -f
```

**업데이트**
```bash
cd ~/zenon-server && git pull && sudo systemctl restart yuki-bot
```

## 0. 전제

- 게임 호스팅과 **분리**된 상시 프로세스(봇은 디스코드 측 운영 허브, 게임 로직 아님 — DL-133).
- 단일 길드 전제(`GUILD_ID`). 멀티 길드 미지원.
- 비밀정보(토큰·키·시크릿)는 `.env`(gitignored)에만. **절대 커밋 금지.**

## 1. 런타임 설치

```bash
python3.12 -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt          # discord.py·aiohttp·python-dotenv·mcstatus
cp .env.example .env                      # 값 채우기(§3)
python -m compileall main.py core integrations modules   # 구문 점검(선택)
python main.py                            # 기동
```

프로세스 관리(상시): systemd 서비스/`Restart=on-failure` 또는 docker `restart: unless-stopped`.
표준출력 로그 수집 + 재기동 정책 권장. (mcstatus 미설치 시 접속정보 기능만 graceful 비활성.)

### 1.1 GCE VM 배포 (git clone + systemd, 권장)

상시 VM(GCE 등)에 올리는 표준 절차. 봇은 디스코드 게이트웨이에 상시 웹소켓을 물고 있어
**Cloud Run/서버리스 부적합**(scale-to-zero·요청 타임아웃에 죽음) — 상시 VM에 systemd 로 띄운다.
모노레포라 **clone 하나로 봇+Zenon Mon 둘 다** 받고, 같은 VM이면 `git pull` 한 번에 양쪽 갱신.

**인증 = GitHub Deploy Key**(읽기전용, 만료 없음, 세팅 후 `git pull`에 토큰 불필요) 권장.

```bash
# 0) 패키지 (Debian/Ubuntu). Python 3.10+ 면 됨(코드가 `X | None` 문법 사용).
sudo apt update && sudo apt install -y git python3-venv

# 1) deploy key → 공개키를 GitHub repo > Settings > Deploy keys 에 Read-only 로 추가
ssh-keygen -t ed25519 -f ~/.ssh/zenon_deploy -N ""
cat ~/.ssh/zenon_deploy.pub
cat >> ~/.ssh/config <<'EOF'
Host github.com
  IdentityFile ~/.ssh/zenon_deploy
  IdentitiesOnly yes
EOF

# 2) clone (봇+몬 한 방)
git clone git@github.com:Zenonsufu0/zenon-server.git
cd zenon-server/zenon-discord

# 3) venv + 의존성
python3 -m venv .venv
.venv/bin/pip install -r requirements.txt

# 4) 비밀값 — .env 는 gitignored 라 clone 에 안 옴. 둘 중 하나:
#    (A·권장) 로컬 .env 를 그대로 복사 — 로컬 PC 에서 실행:
#       scp zenon-discord/.env <user>@<vm-ip>:~/zenon-server/zenon-discord/.env
#    (B) VM 에서 새로 작성:
cp .env.example .env && nano .env            # (B 를 택했을 때만)
```

> `.env` 는 배포에서 git 이 못 가져오는 **유일한 파일**이다. 방법 A(scp)면 토큰·역할/채널 ID·
> `INBOUND_*`·`ZENON_MON_*` 가 통째로 넘어가 재입력이 없다. 같은 VM 이면 `127.0.0.1` 값들이
> 그대로 유효하다. 비밀은 오직 `.env` 에만 두므로 **이 런북·서비스 파일엔 비밀이 없다**(gitignore 불필요).

**systemd 서비스**(템플릿 = [`../deploy/yuki-bot.service`](../deploy/yuki-bot.service)):

```bash
# <USER> 를 실제 유저로 치환해 설치
sudo sed 's/<USER>/'"$USER"'/g' ~/zenon-server/zenon-discord/deploy/yuki-bot.service \
  | sudo tee /etc/systemd/system/yuki-bot.service >/dev/null
sudo systemctl daemon-reload
sudo systemctl enable --now yuki-bot     # 부팅 자동시작 + 즉시 실행
journalctl -u yuki-bot -f                # "YukiBot ready" 확인
```

**업데이트 워크플로:**
```bash
cd ~/zenon-server && git pull
# 의존성 변동 시: ~/zenon-server/zenon-discord/.venv/bin/pip install -r zenon-discord/requirements.txt
sudo systemctl restart yuki-bot
```

- 배포 후 `.env` 에서 확인할 값: `DISCORD_TOKEN`·`GUILD_ID`(필수), `INBOUND_SECRET`+`INBOUND_PORT`
  (게임서버와 동일 시크릿 — 마크→디코 자동제재 인바운드), `ZENON_MON_AUTH_URL`/`ZENON_MON_API_URL`.
- 같은 VM이면 봇 `.env` 의 `ZENON_MON_AUTH_URL` = `http://127.0.0.1:<포트>`, `INBOUND_HOST=127.0.0.1`.
  게임서버가 **다른 호스트**면 `INBOUND_HOST=0.0.0.0` + `INBOUND_ALLOW_IPS=<게임서버IP>` + 방화벽에서 그 IP만 허용.
- 봇 자원은 가벼움(~50–100MB RAM). VM 사이징은 Zenon Mon(Java) 기준으로 잡고 봇은 얹으면 됨.
- Zenon Mon 의 **빌드 산출물(jar·모드팩)이 git 에 있는지**는 zenon-mon 소관 — 소스만 clone 되고
  서버 구동물은 별도 빌드/전송이 필요할 수 있다(zenon-work-mon 에서 확인).

## 2. 디스코드 봇 설정 (Developer Portal)

### 2.1 특권 인텐트 (Privileged Gateway Intents)
| 인텐트 | 필요? | 용도 |
|---|---|---|
| **SERVER MEMBERS** | ✅ ON | 온보딩 역할전이·생애주기 일괄 전이·`on_member_join` 자동배정 (`intents.members`) |
| MESSAGE CONTENT | ❌ OFF | **미사용 정책**(§9.2). 메시지 내용 미열람 — 켜지 말 것 |
| PRESENCE | ❌ OFF | 불필요 |

> `voice_states`(임시음성·음성 XP)는 **비특권**이라 `Intents.default()`에 포함 — 별도 설정 불필요.

### 2.2 봇 권한 (역할 또는 초대 URL 스코프)
실제 호출 API 기준 필요 권한:

| 권한 | 쓰는 기능 |
|---|---|
| **Manage Channels** | 서버신설(카테고리·채널 생성), 티켓/임시음성 채널 생성·삭제, 가시성(`set_permissions`) |
| **Manage Roles** | 온보딩 3역할 생성·전이, 임시역할 부여/회수, 클래스/알림 역할 토글 |
| **Moderate Members** | `/타임아웃` |
| **Kick Members** | `/추방` |
| **Ban Members** | `/차단`·`/차단해제` |
| **Move Members** | 임시음성 개인방으로 이동 |
| View Channels · Send Messages · Embed Links · Read Message History | 알림·패널·임베드 기본 |

> ⚠ **역할 위계:** 봇 최상위 역할이 봇이 다루는 역할(온보딩·임시·제재 대상)보다 **위**에 있어야
> 한다. 아니면 `Forbidden`(코드가 graceful 안내). 권한 역할(owner/admin/매니저)은 봇이 절대
> 자동 지급하지 않음 — 운영진 수동 부여(권한 상승 통로 차단).
> Manage Nicknames는 칭호 닉네임 prefix(`「칭호」 닉네임`) 반영에 필요하다. 서버 소유자나 봇보다 상위/동급 역할은 디스코드 제한으로 변경 불가.

## 3. `.env` 설정

### 3.1 필수 (없으면 기동 실패 — `os.environ`)
| 키 | 설명 |
|---|---|
| `DISCORD_TOKEN` | 봇 토큰 |
| `GUILD_ID` | 운영 길드 ID |

> ⚠ 브랜드 변경 이력: 구 `PORO_API_URL/KEY`(2026-06-10) → `PORONG_API_URL/KEY` →
> **`ZENON_RPG_API_URL/KEY`**(현행). 전환기 한정으로 구 `PORONG_API_*` 도 폴백으로 인식하나,
> 신규 `.env` 는 새 이름을 사용하고 구 이름은 차후 제거한다.

### 3.2 선택 (미설정 시 0/기본값 → 해당 기능 graceful 비활성)
- **API:** `ZENON_RPG_API_URL`(기본 localhost:8765, 구 `PORONG_API_URL` 폴백),
  `ZENON_RPG_API_KEY`(구 `PORONG_API_KEY` 폴백), `ZENON_MON_AUTH_URL/KEY`, `ZENON_MON_API_URL/KEY`
- **DB:** `BOT_DB_PATH`(기본 `yuki_bot.sqlite3`, 인스턴스 로컬·gitignored)
- **채널:** `CHANNEL_FIELD_BOSS_ID`·`CHANNEL_MODLOG_ID`(운영로그)·`CHANNEL_NOTICE_ID`(공지)·
  `CHANNEL_ZENON_MON_NOTICE_ID`·`CHANNEL_BUGREPORT_ID`·`CHANNEL_LEVELUP_ID`·`AFK_CHANNEL_ID`·
  `CATEGORY_티켓_ID`
- **전역 단일 active:** `ROLE_서버준비_ID`·`CATEGORY_통합_ID`(생애주기 일괄 전이용)
- **권한 역할:** `ROLE_OWNER_ID`·`ROLE_ADMIN_ID`·`ROLE_RPG_MANAGER_ID`·`ROLE_ZENON_MON_MANAGER_ID`·
  `ROLE_EVENT_MANAGER_ID`·`ROLE_SUPPORT_ID` — **운영 명령 권한 판정에 필수적**(미설정 시 owner만 통과)
- **알림 역할:** `ROLE_필드보스알림_ID`·`시즌보스`·`월드보스`·`Zenon Mon`·`이벤트`·`점검`·`업데이트알림_ID`
- **클래스 역할:** `ROLE_검사_ID` 등 6종
- **튜닝:** `CHAT_XP_*`·`VOICE_*`·`ATTENDANCE_XP_*`·`XP_EXCLUDE_CHANNEL_IDS`
- **온보딩(Zenon Mon):** `ROLE_포로몬접근/인증전/플레이어_ID`·`CHANNEL_포로몬약관/인증_ID`

### 3.3 인바운드 알림 리스너 (선택 — 게임서버 push 수신, T1)
`INBOUND_SECRET`·`INBOUND_PORT` **둘 다 설정해야 기동**(미설정 시 무인증 엔드포인트 안 엶).
- `INBOUND_SECRET`(강엔트로피, 게임서버와 공유) · `INBOUND_HOST`(기본 127.0.0.1) · `INBOUND_PORT`
- `INBOUND_ALLOW_IPS`(게임 호스팅 IP, 방화벽과 이중) · `INBOUND_TS_TOLERANCE`(기본 300s)
- ⚠ 외부 노출 시 **방화벽/보안그룹으로 게임 IP만 허용** + 시크릿 + (가능하면) 역프록시 TLS.

### 3.4 DEPRECATED (미참조·optional — 비워둬도 됨)
`CHANNEL_AUTH_ID`·`ROLE_미인증/접속대기/인증유저_ID`·`TERMS_MESSAGE_ID` (구 RPG 단일서버 온보딩, DL-138 폐기).

## 4. 운영 런북 (서버 시즌 시작 절차)

전역 단일 active 모델 — 한 시점에 한 게임만 운영(task.md §5).

1. `/서버신설 <도메인> <시즌> <표시명> [접속주소]` — 레지스트리 `prep` + 카테고리/3역할 자동 전개.
2. `/약관설정` — 약관 본문 입력(모달, 4000자 이하). 긴 약관은 `/약관파일설정` 으로 `.txt` 업로드.
3. `/서버시작 <id>` — `prep→active` + 카테고리 가시화 + **온보딩 패널 자동 게시** + 일괄 역할 전이.
4. (선택) `/서버주소`로 접속주소 등록 → `접속정보` 채널 라이브 갱신.
5. 시즌 종료: `/서버종료 <id> <사유>` — 아카이브 + `서버준비` 일괄 부여.

> 신규 유저: 가입 → 자동 임시역할 → 약관 동의 버튼 → 인증 버튼/모달(인게임 `/인증` 코드) → 플레이어 승급.

## 5. 스테이징 e2e 체크리스트

- [x] 봇 기동·슬래시 동기화(`/핑`) / 인텐트·권한 경고 없는지 로그 확인 (2026-07-02)
- [x] `/서버신설`→`/서버시작` 카테고리·역할 자동 생성 + 가시성 (2026-07-01)
- [x] 온보딩: 약관 버튼→인증전 역할, 인증 모달→verify→플레이어 (2026-07-01)
- [x] `/도움말`: 일반 유저는 공통 명령만, 운영자는 권한 명령까지 노출(권한별 필터 확인) (2026-07-02)
- [x] `/서버권한재적용`: 활성 시즌1(Zenon Mon)에 정보/로그 읽기전용 + 문의/문의 보관 카테고리 소급 생성 확인 (2026-07-02)
- [ ] 생애주기 일괄 역할 전이(`/서버종료`·`/서버시작`) — 인원 많으면 수십초(레이트리밋)
- [ ] 커뮤니티: 수동 칭호(`/칭호생성`·`/칭호부여`·`/칭호회수`·`/칭호`)·`/출석`(KST 경계, XP 보상 없음)
- [ ] 임시역할: `/임시역할부여` → 만료 tick 회수 / 임시음성: 허브 입장→개인방→비면 삭제
- [ ] 모더레이션: `/경고`·`/타임아웃`·`/추방`·`/차단` + `#운영로그` 적재
- [~] 지원: `/문의`(문의 카테고리 생성·개설자만 가시·채팅)·`/티켓종료`(문의 보관 이동·개설자 읽기전용) 확인 (2026-07-02) / `/faq`·`/faq추가`·`/버그제보`(상태버튼·DM) 미검증
- [ ] Zenon Mon 운영 API: `/마크제재패널`·`/마크경고`·`/마크제재조회`
- [ ] 알림: `/공지`·`/점검`·`/보스알림`·`/이벤트알림` → 공지 채널 게시 + 역할 멘션
- [ ] (선택) 인바운드: 게임서버 push → `/events` 200·서명검증·dedup (게임서버 송신 구현 후)
- [ ] 접속정보: mcstatus SLP 핑 라이브 갱신(3분)

## 6. DB / 백업

- 단일 SQLite 파일(`BOT_DB_PATH`, 기본 `yuki_bot.sqlite3`) — 인스턴스 로컬, gitignored.
  - 기존 `porong_bot.sqlite3` 사용자는 파일명을 바꾸거나 `BOT_DB_PATH` 로 기존 경로를 명시한다(자동 마이그레이션 없음).
- 증분 마이그레이션 v1~v13 자동 적용(기동 시 `schema_meta.version`). 다운그레이드 미지원.
- 백업 = 파일 주기 복사(WAL 모드 — `.sqlite3`·`-wal`·`-shm` 함께) 또는 `sqlite3 .backup`.
