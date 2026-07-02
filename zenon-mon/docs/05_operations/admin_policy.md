# 운영 제재 정책

> Zenon Mon 마크 서버 제재 기준. 디스코드 제재는 `zenon-discord/docs/moderation.md`가 담당한다.

## 1. 제재 경계

- **마크 제재와 디스코드 제재는 분리한다.**
- 마크 밴/킥/경고는 ZenonMonCore가 권위다.
- 디스코드 타임아웃/추방/차단은 Discord 봇이 권위다.
- 한쪽 제재가 다른 쪽 제재를 자동으로 발생시키지 않는다.

## 2. 마크 제재 기록

- 모든 마크 제재는 `AuditLogManager`에 기록한다.
- 기록 항목: `action`, `targetUuid/name`, `operator`, `reason`, `createdAt`, `source`.
- 인게임 op/콘솔 제재와 Discord 봇에서 요청한 제재를 모두 같은 마크 제재 로그로 남긴다.
- Discord 표시가 필요하면 `poromon.minecraft_sanction` 이벤트로 봇에 push한다.

## 3. Discord 연동

- Discord 봇의 `/마크경고`, `/마크킥`, `/마크밴`, `/마크밴해제`는 ZenonMonCore 운영 API 성공 후에만 `제재내역`에 기록한다.
- 실패한 요청은 Discord 제재 기록으로 남기지 않는다.
- 대상 식별은 ZenonMonCore가 최종 검증한다. 봇은 마크 닉네임/UUID 문자열을 전달할 뿐, 계정 매핑의 권위가 아니다.
