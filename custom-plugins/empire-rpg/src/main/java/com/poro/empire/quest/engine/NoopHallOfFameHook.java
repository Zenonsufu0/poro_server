package com.poro.empire.quest.engine;

import com.poro.empire.common.registry.master.model.AchievementMaster;

public final class NoopHallOfFameHook implements HallOfFameHook {
    @Override
    public void onAchievementRecorded(PlayerQuestHonorState state, AchievementMaster achievement, String recordId, long amount) {
        // no-op
    }
}
