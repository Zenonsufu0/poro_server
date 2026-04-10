package com.poro.empire.storage;

import com.poro.empire.classes.PlayerClass;

import java.util.Objects;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private PlayerClass playerClass;
    private int reputation;

    public PlayerData(UUID uuid) {
        this(uuid, PlayerClass.UNSELECTED, 0);
    }

    public PlayerData(UUID uuid, PlayerClass playerClass) {
        this(uuid, playerClass, 0);
    }

    public PlayerData(UUID uuid, PlayerClass playerClass, int reputation) {
        this.uuid = Objects.requireNonNull(uuid, "uuid");
        this.playerClass = Objects.requireNonNull(playerClass, "playerClass");
        this.reputation = reputation;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = Objects.requireNonNull(playerClass, "playerClass");
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = Math.max(0, reputation);
    }
}
