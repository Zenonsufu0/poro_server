package com.poro.empire.quest.engine;

import com.poro.empire.common.registry.master.model.AchievementMaster;

public interface HallOfFameHook {
    void onAchievementRecorded(PlayerQuestHonorState state, AchievementMaster achievement, String recordId, long amount);
}
