"""CLI 엔트리포인트 — PR#4 확장(리포트 포맷 + CLI UX).

사용법:
    python3 -m tools.cmd_registry_lint <csv_path> [--format=human|json] [--summary]

옵션:
    --format=human  기본값. 사람이 읽기 쉬운 multi-line 리포트.
    --format=json   CI Job Summary용 기계 파싱 가능한 JSON.
    --summary       에러·경고 합계만 1줄 출력(CI 요약용). format 옵션과 병용 가능.

exit code:
    0 — 린트 에러 없음
    1 — 린트 에러 1건 이상
    2 — 실행 오류(파일 없음·형식 이상 등)
"""

from __future__ import annotations

import argparse
import csv
import json
import sys
from dataclasses import asdict
from pathlib import Path

from . import rules


def load_csv(path: Path) -> list[dict[str, str]]:
    with path.open(encoding="utf-8", newline="") as f:
        reader = csv.DictReader(f)
        return list(reader)


def format_human(errors: list[rules.LintError]) -> str:
    lines = []
    for err in errors:
        prev = f" (이전 row {err.prev_row})" if err.prev_row else ""
        hint = f"  힌트: {err.hint}" if err.hint else ""
        lines.append(f"[{err.code}] row {err.row}, col '{err.col}': {err.value}{prev}")
        if hint:
            lines.append(hint)
    return "\n".join(lines)


def format_json(rows_count: int, errors: list[rules.LintError]) -> str:
    payload = {
        "rows": rows_count,
        "error_count": len(errors),
        "ok": not errors,
        "errors": [asdict(e) for e in errors],
    }
    return json.dumps(payload, ensure_ascii=False, indent=2)


def format_summary(rows_count: int, errors: list[rules.LintError]) -> str:
    if not errors:
        return f"OK: {rows_count} rows · 에러 0건"
    codes: dict[str, int] = {}
    for err in errors:
        codes[err.code] = codes.get(err.code, 0) + 1
    breakdown = " · ".join(f"{code} {cnt}" for code, cnt in sorted(codes.items()))
    return f"FAIL: {rows_count} rows · 에러 {len(errors)}건 ({breakdown})"


def main(argv: list[str]) -> int:
    parser = argparse.ArgumentParser(
        prog="python3 -m tools.cmd_registry_lint",
        description="CustomModelData CSV registry linter (R1~R5).",
    )
    parser.add_argument("csv_path", type=Path, help="CSV 파일 경로")
    parser.add_argument(
        "--format",
        choices=("human", "json"),
        default="human",
        help="출력 포맷 (기본: human)",
    )
    parser.add_argument(
        "--summary",
        action="store_true",
        help="에러·경고 합계만 1줄 출력(CI 요약용)",
    )
    ns = parser.parse_args(argv[1:])

    if not ns.csv_path.exists():
        print(f"error: CSV 파일을 찾을 수 없음: {ns.csv_path}", file=sys.stderr)
        return 2

    rows = load_csv(ns.csv_path)
    report = rules.run_all(rows)

    if ns.summary:
        print(format_summary(len(rows), report.errors))
    elif ns.format == "json":
        print(format_json(len(rows), report.errors))
    else:
        if report.ok:
            print(f"OK: {len(rows)} rows · 린트 에러 없음")
        else:
            print(f"FAIL: {len(rows)} rows · 에러 {len(report.errors)}건")
            print(format_human(report.errors))

    return 0 if report.ok else 1


if __name__ == "__main__":
    sys.exit(main(sys.argv))
