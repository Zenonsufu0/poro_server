package com.poro.empire;

import com.poro.empire.command.EmpireCommand;
import com.poro.empire.combat.CooldownManager;
import com.poro.empire.combat.SkillContext;
import com.poro.empire.combat.SkillService;
import com.poro.empire.combat.skills.AssassinShadowstepSkill;
import com.poro.empire.combat.skills.MageFireboltSkill;
import com.poro.empire.combat.skills.WarriorSlashSkill;
import com.poro.empire.listener.HealthHudListener;
import com.poro.empire.listener.HungerLockListener;
import com.poro.empire.reputation.ReputationManager;
import com.poro.empire.storage.PlayerDataManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EmpireRPGPlugin extends JavaPlugin {
    private PlayerDataManager playerDataManager;
    private SkillService skillService;
    private ReputationManager reputationManager;

    @Override
    public void onEnable() {
        getLogger().info("EmpireRPG enabled.");
        saveDefaultConfig();

        this.playerDataManager = new PlayerDataManager();
        this.reputationManager = new ReputationManager(playerDataManager);
        CooldownManager cooldownManager = new CooldownManager();
        SkillContext skillContext = new SkillContext(playerDataManager, cooldownManager);

        this.skillService = new SkillService(skillContext);
        skillService.registerSkill(new WarriorSlashSkill());
        skillService.registerSkill(new AssassinShadowstepSkill());
        skillService.registerSkill(new MageFireboltSkill());

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("EmpireRPG disabled.");
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ReputationManager getReputationManager() {
        return reputationManager;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new HungerLockListener(), this);
        getServer().getPluginManager().registerEvents(new HealthHudListener(this), this);
    }

    private void registerCommands() {
        EmpireCommand empireCommand = new EmpireCommand(playerDataManager, skillService, reputationManager);

        if (getCommand("empire") == null) {
            getLogger().severe("Failed to register /empire command. Check plugin.yml.");
            return;
        }

        getCommand("empire").setExecutor(empireCommand);
        getCommand("empire").setTabCompleter(empireCommand);
    }
}
