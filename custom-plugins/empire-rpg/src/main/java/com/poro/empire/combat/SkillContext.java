package com.poro.empire.combat;

import com.poro.empire.storage.PlayerDataManager;

public class SkillContext {
    private final PlayerDataManager playerDataManager;
    private final CooldownManager cooldownManager;

    public SkillContext(PlayerDataManager playerDataManager, CooldownManager cooldownManager) {
        this.playerDataManager = playerDataManager;
        this.cooldownManager = cooldownManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}
