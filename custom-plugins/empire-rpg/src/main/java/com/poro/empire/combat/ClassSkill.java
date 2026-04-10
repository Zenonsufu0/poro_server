package com.poro.empire.combat;

import com.poro.empire.classes.PlayerClass;
import org.bukkit.entity.Player;

import java.time.Duration;

public interface ClassSkill {
    String key();

    PlayerClass requiredClass();

    Duration cooldown();

    boolean execute(Player caster, SkillContext context);
}
