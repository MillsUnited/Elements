package com.mills.elements;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMovementListener implements Listener {

    private static final Set<UUID> frozenPlayers = new HashSet<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    public static void addFrozenPlayer(Player player) {
        frozenPlayers.add(player.getUniqueId());
    }

    public static void removeFrozenPlayer(Player player) {
        frozenPlayers.remove(player.getUniqueId());
    }

    public static boolean isFrozen(Player player) {
        return frozenPlayers.contains(player.getUniqueId());
    }

}
