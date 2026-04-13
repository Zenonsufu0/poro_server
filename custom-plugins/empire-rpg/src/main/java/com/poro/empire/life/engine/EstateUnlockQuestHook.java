package com.poro.empire.life.engine;

public interface EstateUnlockQuestHook {
    boolean canUnlock(PlayerLifeState state, String unlockQuestId);
}
