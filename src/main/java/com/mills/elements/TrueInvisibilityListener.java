package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TrueInvisibilityListener {
    private static final Set<UUID> invisiblePlayers = new HashSet<>();

    public static void hidePlayer(Player player) {
        if (!isHidden(player)) {
            invisiblePlayers.add(player.getUniqueId());
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                    onlinePlayer.hidePlayer(Main.getInstance(), player);
                }
            }
        }
    }

    public static void showPlayer(Player player) {
        if (isHidden(player)) {
            invisiblePlayers.remove(player.getUniqueId());
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(Main.getInstance(), player);
            }
        }
    }

    public static boolean isHidden(Player player) {
        return invisiblePlayers.contains(player.getUniqueId());
    }
}
