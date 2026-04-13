package com.poro.empire.quest.engine;

import com.poro.empire.common.registry.master.model.AchievementMaster;

public interface SymbolicHonorHook {
    void onSymbolicHonorGranted(PlayerQuestHonorState state, AchievementMaster achievement, String honorCode, long amount);
}
