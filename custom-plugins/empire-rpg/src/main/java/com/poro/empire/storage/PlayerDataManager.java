package com.poro.empire.storage;

import com.poro.empire.classes.PlayerClass;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private final Map<UUID, PlayerData> playerDataMap = new ConcurrentHashMap<>();

    public PlayerData getOrCreate(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, PlayerData::new);
    }

    public PlayerData getOrCreate(Player player) {
        return getOrCreate(player.getUniqueId());
    }

    public Optional<PlayerData> find(UUID uuid) {
        return Optional.ofNullable(playerDataMap.get(uuid));
    }

    public Optional<PlayerData> find(Player player) {
        return find(player.getUniqueId());
    }

    public void onPlayerJoin(UUID uuid) {
        getOrCreate(uuid);
    }

    public void onPlayerJoin(Player player) {
        onPlayerJoin(player.getUniqueId());
    }

    public void onPlayerQuit(UUID uuid) {
        playerDataMap.remove(uuid);
    }

    public void onPlayerQuit(Player player) {
        onPlayerQuit(player.getUniqueId());
    }

    public boolean hasSelectedClass(UUID uuid) {
        return getOrCreate(uuid).getPlayerClass().isSelected();
    }

    public void setPlayerClass(UUID uuid, PlayerClass playerClass) {
        getOrCreate(uuid).setPlayerClass(playerClass);
    }

    public void clear() {
        playerDataMap.clear();
    }

    public int size() {
        return playerDataMap.size();
    }
}
