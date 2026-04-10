package com.poro.empire.combat.skills;

import com.poro.empire.classes.PlayerClass;
import com.poro.empire.combat.ClassSkill;
import com.poro.empire.combat.SkillContext;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.time.Duration;

public class AssassinShadowstepSkill implements ClassSkill {
    private static final String KEY = "shadowstep";
    private static final Duration COOLDOWN = Duration.ofSeconds(6);

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public PlayerClass requiredClass() {
        return PlayerClass.ASSASSIN;
    }

    @Override
    public Duration cooldown() {
        return COOLDOWN;
    }

    @Override
    public boolean execute(Player caster, SkillContext context) {
        Location start = caster.getLocation().clone();
        Vector direction = start.getDirection().setY(0).normalize();
        if (direction.lengthSquared() == 0) {
            direction = new Vector(0, 0, 1);
        }

        Location destination = start.clone().add(direction.multiply(4.0));
        destination.setY(start.getY());
        caster.teleport(destination);
        caster.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 0, false, false, true));

        LivingEntity target = caster.getWorld()
                .getNearbyLivingEntities(caster.getLocation(), 2.5, 2.0, 2.5).stream()
                .filter(entity -> entity != caster)
                .findFirst()
                .orElse(null);

        if (target != null) {
            target.damage(5.0, caster);
        }

        caster.getWorld().spawnParticle(
                Particle.DUST,
                start.add(0, 1.0, 0),
                20,
                0.2, 0.4, 0.2,
                new Particle.DustOptions(Color.fromRGB(60, 60, 60), 1.2f)
        );
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 1.3f);
        caster.sendMessage("Used Shadowstep.");
        return true;
    }
}
