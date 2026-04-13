package com.poro.empire.combat.engine;

import com.poro.empire.common.logging.DomainLogger;
import com.poro.empire.common.result.ErrorCode;
import com.poro.empire.common.result.Result;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

final class DefaultCombatSeedInstaller {
    private DefaultCombatSeedInstaller() {
    }

    static Result<Void> install(JavaPlugin plugin, DomainLogger logger) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(logger, "logger");
        try {
            plugin.saveResource("seeds/state_master.csv", false);
            logger.info("Default combat seed resources are ensured.");
            return Result.success();
        } catch (Exception exception) {
            return Result.failure(
                    ErrorCode.SEED_LOAD_FAILED,
                    "Failed to install combat seed resources",
                    exception
            );
        }
    }
}
