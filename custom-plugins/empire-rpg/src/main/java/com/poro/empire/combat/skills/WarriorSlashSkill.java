package com.poro.empire.combat.skills;

import com.poro.empire.classes.PlayerClass;
import com.poro.empire.combat.ClassSkill;
import com.poro.empire.combat.SkillContext;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.time.Duration;

public class WarriorSlashSkill implements ClassSkill {
    private static final String KEY = "slash";
    private static final Duration COOLDOWN = Duration.ofSeconds(4);

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public PlayerClass requiredClass() {
        return PlayerClass.WARRIOR;
    }

    @Override
    public Duration cooldown() {
        return COOLDOWN;
    }

    @Override
    public boolean execute(Player caster, SkillContext context) {
        LivingEntity target = caster.getWorld()
                .getNearbyLivingEntities(caster.getLocation(), 3.0, 2.0, 3.0).stream()
                .filter(entity -> entity != caster)
                .filter(entity -> isInFront(caster, entity))
                .findFirst()
                .orElse(null);

        if (target == null) {
            caster.sendMessage("No target in front of you.");
            return false;
        }

        target.damage(6.0, caster);
        caster.getWorld().spawnParticle(Particle.SWEEP_ATTACK, target.getLocation().add(0, 1.0, 0), 1);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
        caster.sendMessage("Used Slash.");
        return true;
    }

    private boolean isInFront(Player caster, LivingEntity target) {
        Vector toTarget = target.getLocation().toVector().subtract(caster.getLocation().toVector()).normalize();
        Vector look = caster.getLocation().getDirection().normalize();
        return look.dot(toTarget) >= 0.4;
    }
}
