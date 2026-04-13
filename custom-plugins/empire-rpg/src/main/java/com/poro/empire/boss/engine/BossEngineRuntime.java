package com.poro.empire.boss.engine;

public record BossEngineRuntime(
        BossRunService runService,
        BossEntryRuleRegistry entryRuleRegistry,
        BossPatternRegistry patternRegistry,
        InMemoryBossRunRecordHook runRecordHook
) {
}
