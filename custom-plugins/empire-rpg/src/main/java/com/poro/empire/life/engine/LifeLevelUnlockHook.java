package com.poro.empire.life.engine;

public interface LifeLevelUnlockHook {
    void onLevelUnlocked(PlayerLifeState state, LifeType lifeType, int unlockedLevel, String unlockCode);
}
