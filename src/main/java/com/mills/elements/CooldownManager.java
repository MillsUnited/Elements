package com.mills.elements;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public static boolean isOnCooldown(UUID playerId, String abilityName) {
        if (!cooldowns.containsKey(playerId)) return false;
        if (!cooldowns.get(playerId).containsKey(abilityName)) return false;

        long expiresAt = cooldowns.get(playerId).get(abilityName);
        if (System.currentTimeMillis() >= expiresAt) {
            cooldowns.get(playerId).remove(abilityName);
            if (cooldowns.get(playerId).isEmpty()) {
                cooldowns.remove(playerId);
            }
            return false;
        }
        return true;
    }

    public static void setCooldown(UUID playerId, String abilityName, long cooldownMillis) {
        long expiresAt = System.currentTimeMillis() + cooldownMillis;
        cooldowns.computeIfAbsent(playerId, k -> new HashMap<>()).put(abilityName, expiresAt);
    }

    public static long getRemainingCooldown(UUID playerId, String abilityName) {
        if (!isOnCooldown(playerId, abilityName)) return 0;
        return Math.max(0, cooldowns.get(playerId).get(abilityName) - System.currentTimeMillis());
    }
}
