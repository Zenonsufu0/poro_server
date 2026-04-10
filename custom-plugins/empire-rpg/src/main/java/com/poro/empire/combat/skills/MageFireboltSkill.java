package com.poro.empire.combat.skills;

import com.poro.empire.classes.PlayerClass;
import com.poro.empire.combat.ClassSkill;
import com.poro.empire.combat.SkillContext;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.time.Duration;

public class MageFireboltSkill implements ClassSkill {
    private static final String KEY = "firebolt";
    private static final Duration COOLDOWN = Duration.ofSeconds(5);

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public PlayerClass requiredClass() {
        return PlayerClass.MAGE;
    }

    @Override
    public Duration cooldown() {
        return COOLDOWN;
    }

    @Override
    public boolean execute(Player caster, SkillContext context) {
        RayTraceResult hit = caster.getWorld().rayTrace(
                caster.getEyeLocation(),
                caster.getLocation().getDirection(),
                12.0,
                FluidCollisionMode.NEVER,
                true,
                0.3,
                entity -> entity instanceof LivingEntity && entity != caster
        );

        if (hit == null || !(hit.getHitEntity() instanceof LivingEntity target)) {
            caster.sendMessage("Firebolt missed.");
            caster.getWorld().spawnParticle(Particle.FLAME, caster.getEyeLocation(), 12, 0.2, 0.2, 0.2, 0.02);
            return true;
        }

        target.damage(7.0, caster);
        target.setFireTicks(40);
        caster.getWorld().spawnParticle(Particle.FLAME, target.getLocation().add(0, 1.0, 0), 24, 0.25, 0.5, 0.25, 0.01);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.1f);
        caster.sendMessage("Used Firebolt.");
        return true;
    }
}
