package com.mills.elements;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashSet;
import java.util.Set;

public class FallDamageListener implements Listener {

    // Store players who have fall damage disabled
    private final Set<Player> noFallDamage = new HashSet<>();

    // Add a player to the list to disable fall damage
    public void addNoFallDamage(Player player) {
        noFallDamage.add(player);
    }

    // Remove a player from the list to enable fall damage
    public void removeNoFallDamage(Player player) {
        noFallDamage.remove(player);
    }

    // Check if a player has fall damage disabled
    public boolean hasNoFallDamage(Player player) {
        return noFallDamage.contains(player);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Cancel fall damage if the player is in the noFallDamage list
            if (noFallDamage.contains(player) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }
}
