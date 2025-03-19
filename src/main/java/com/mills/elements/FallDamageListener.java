package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FallDamageListener implements Listener {

    private static final Set<UUID> noFallDamage = new HashSet<>();

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;

        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (hasNoFallDamage(player)) {
                e.setCancelled(true);
                e.setDamage(0);
                player.setFallDistance(0);

                // Delay immunity removal to ensure no accidental damage
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    removeNoFallDamage(player);
                }, 20L); // 1-second delay after fall damage prevention
            }
        }
    }

    public static void addNoFallDamage(Player player) {
        noFallDamage.add(player.getUniqueId());
    }

    public static void removeNoFallDamage(Player player) {
        noFallDamage.remove(player.getUniqueId());
    }

    public static boolean hasNoFallDamage(Player player) {
        return noFallDamage.contains(player.getUniqueId());
    }
}
