package com.poro.empire.combat;

import com.poro.empire.classes.PlayerClass;
import com.poro.empire.combat.weapon.ClassWeaponPreferences;
import com.poro.empire.combat.weapon.WeaponType;
import com.poro.empire.combat.weapon.WeaponTypeResolver;
import com.poro.empire.storage.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class SkillService {
    private final Map<String, ClassSkill> skillsByKey = new LinkedHashMap<>();
    private final SkillContext context;

    public SkillService(SkillContext context) {
        this.context = context;
    }

    public void registerSkill(ClassSkill skill) {
        skillsByKey.put(skill.key().toLowerCase(Locale.ROOT), skill);
    }

    public boolean useSkill(Player player, String rawSkillKey) {
        String skillKey = rawSkillKey.toLowerCase(Locale.ROOT);
        ClassSkill skill = skillsByKey.get(skillKey);
        if (skill == null) {
            player.sendMessage(ChatColor.RED + "Unknown skill: " + rawSkillKey);
            return true;
        }

        PlayerData playerData = context.getPlayerDataManager().getOrCreate(player.getUniqueId());
        PlayerClass playerClass = playerData.getPlayerClass();
        if (!playerClass.isSelected()) {
            player.sendMessage(ChatColor.RED + "Select a class first with /empire class <warrior|assassin|mage>.");
            return true;
        }

        if (playerClass != skill.requiredClass()) {
            player.sendMessage(ChatColor.RED + "Your class cannot use this skill. Required: "
                    + skill.requiredClass().name().toLowerCase(Locale.ROOT));
            return true;
        }

        WeaponType currentWeaponType = WeaponTypeResolver.resolve(player);
        if (!ClassWeaponPreferences.isPreferred(playerClass, currentWeaponType)) {
            player.sendMessage(ChatColor.RED + "Invalid weapon type for your class. Current: "
                    + currentWeaponType.name().toLowerCase(Locale.ROOT)
                    + ", preferred: "
                    + ClassWeaponPreferences.getPreferredWeapons(playerClass).toString().toLowerCase(Locale.ROOT));
            return true;
        }

        long remaining = context.getCooldownManager().getRemainingMillis(player.getUniqueId(), skill.key());
        if (remaining > 0) {
            player.sendMessage(ChatColor.YELLOW + "Skill is on cooldown: " + CooldownManager.formatSeconds(remaining) + "s");
            return true;
        }

        boolean used = skill.execute(player, context);
        if (!used) {
            return true;
        }

        context.getCooldownManager().applyCooldown(player.getUniqueId(), skill.key(), skill.cooldown());
        return true;
    }

    public Collection<String> getSkillKeys() {
        return skillsByKey.keySet();
    }
}
