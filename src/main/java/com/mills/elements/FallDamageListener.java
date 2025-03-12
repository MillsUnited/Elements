package com.mills.elements;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FallDamageListener implements Listener {

    private final Set<UUID> noFallDamage = new HashSet<>();

    public void addNoFallDamage(Player player) {
        noFallDamage.add(player.getUniqueId());
    }

    public void removeNoFallDamage(Player player) {
        noFallDamage.remove(player.getUniqueId());
    }

    public boolean hasNoFallDamage(Player player) {
        return noFallDamage.contains(player.getUniqueId());
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (noFallDamage.contains(player) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setDamage(0);
            }
        }
    }
}
