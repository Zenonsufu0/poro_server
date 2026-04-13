package com.poro.empire.boss.engine;

public interface UnlockQuestChecker {
    boolean hasUnlocked(String userId, String unlockQuestCode);
}
