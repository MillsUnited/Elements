package com.mills.elements;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        String playerName = e.getPlayer().getName();

        String luckpermsPrefix = PlaceholderAPI.setPlaceholders(e.getPlayer(), "%luckperms_prefix%");

        if (luckpermsPrefix != null && !luckpermsPrefix.isEmpty() && !luckpermsPrefix.equals("none")) {
            luckpermsPrefix = ChatColor.translateAlternateColorCodes('&', luckpermsPrefix + "&r");
            e.setFormat(luckpermsPrefix + playerName + ": " + e.getMessage());
        } else {
            e.setFormat(playerName + ": " + e.getMessage());
        }
    }

}
