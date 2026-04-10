package com.poro.empire.combat;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {
    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

    public boolean isOnCooldown(UUID playerId, String key) {
        return getRemainingMillis(playerId, key) > 0;
    }

    public long getRemainingMillis(UUID playerId, String key) {
        Map<String, Long> playerCooldowns = cooldowns.get(playerId);
        if (playerCooldowns == null) {
            return 0L;
        }

        long now = System.currentTimeMillis();
        long expireAt = playerCooldowns.getOrDefault(key.toLowerCase(), 0L);
        long remaining = expireAt - now;
        if (remaining <= 0) {
            playerCooldowns.remove(key.toLowerCase());
            if (playerCooldowns.isEmpty()) {
                cooldowns.remove(playerId);
            }
            return 0L;
        }

        return remaining;
    }

    public void applyCooldown(UUID playerId, String key, Duration duration) {
        long expireAt = System.currentTimeMillis() + duration.toMillis();
        cooldowns.computeIfAbsent(playerId, ignored -> new ConcurrentHashMap<>())
                .put(key.toLowerCase(), expireAt);
    }

    public static String formatSeconds(long millis) {
        double seconds = millis / 1000.0;
        return String.format("%.1f", seconds);
    }
}
