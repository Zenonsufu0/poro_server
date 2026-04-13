package com.poro.empire.boss.engine;

public interface BossRunRecordHook {
    void onRunStarted(BossRun run);

    void onRunEnded(BossResultSummary summary);
}
