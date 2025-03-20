package com.mills.elements;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AttackSpeedHandler {

    private static final Map<UUID, AttributeModifier> attackSpeedModifiers = new HashMap<>();

    public static void applyAttackSpeedBoost(Player player, double boostAmount) {
        AttributeInstance attribute = player.getAttribute(Attribute.ATTACK_SPEED);
        if (attribute != null) {
            removeAttackSpeedBoost(player); // Ensure no duplicate modifier

            UUID playerUUID = player.getUniqueId();
            AttributeModifier modifier = new AttributeModifier(
                    playerUUID, // Use player's UUID as the modifier's unique ID
                    "custom_attack_speed_boost",
                    boostAmount,
                    AttributeModifier.Operation.ADD_NUMBER
            );

            attackSpeedModifiers.put(playerUUID, modifier); // Store the modifier
            attribute.addModifier(modifier);
        }
    }

    public static void removeAttackSpeedBoost(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.ATTACK_SPEED);
        if (attribute != null) {
            UUID playerUUID = player.getUniqueId();
            AttributeModifier modifier = attackSpeedModifiers.remove(playerUUID);

            if (modifier != null) {
                attribute.removeModifier(modifier);
            }
        }
    }

    public static boolean isAttackSpeedBoosted(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.ATTACK_SPEED);
        if (attribute != null) {
            return attribute.getModifiers().stream()
                    .anyMatch(modifier -> modifier.getName().equals("custom_attack_speed_boost"));
        }
        return false;
    }
}
