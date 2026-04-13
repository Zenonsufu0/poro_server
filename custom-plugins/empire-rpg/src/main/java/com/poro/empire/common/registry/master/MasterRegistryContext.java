package com.poro.empire.common.registry.master;

public record MasterRegistryContext(
        ItemMasterRegistry itemMasters,
        SkillMasterRegistry skillMasters,
        BossMasterRegistry bossMasters,
        QuestMasterRegistry questMasters,
        AchievementMasterRegistry achievementMasters,
        RegionMasterRegistry regionMasters,
        TownMasterRegistry townMasters,
        NpcMasterRegistry npcMasters
) {
}
