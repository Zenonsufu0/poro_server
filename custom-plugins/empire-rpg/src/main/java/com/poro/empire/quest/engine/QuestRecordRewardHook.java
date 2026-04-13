package com.poro.empire.quest.engine;

public interface QuestRecordRewardHook {
    void onQuestRecordReward(PlayerQuestHonorState state, String sourceId, String recordId, long amount);
}
