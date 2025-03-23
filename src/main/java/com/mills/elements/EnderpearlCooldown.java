package com.mills.elements;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class EnderpearlCooldown implements Listener {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final long cooldownTime = 60000;

    @EventHandler
    public void onEnderPearl(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.ENDER_PEARL) {
            if (isOnCooldown(uuid)) {
                e.setCancelled(true); // Prevent using pearl
                return;
            }

            setCooldown(uuid);
            startActionBarTimer(player, uuid);
        }
    }

    private void setCooldown(UUID playerUUID) {
        cooldown.put(playerUUID, System.currentTimeMillis());
    }

    private long getTimeLeft(UUID playerUUID) {
        Long startTime = cooldown.get(playerUUID);
        if (startTime == null) return 0;
        return Math.max(0, (startTime + cooldownTime) - System.currentTimeMillis());
    }

    private boolean isOnCooldown(UUID playerUUID) {
        if (!cooldown.containsKey(playerUUID)) return false;
        long timeLeft = (cooldown.get(playerUUID) + cooldownTime) - System.currentTimeMillis();
        return timeLeft > 0;
    }

    private void startActionBarTimer(Player player, UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isOnCooldown(uuid)) {
                    this.cancel();
                    return;
                }

                long secondsLeft = getTimeLeft(uuid) / 1000;

                String prefix = Util.parseHexColors("&#0C4C44E&#0E5048n&#10554Cd&#12594Fe&#145D53r &#17665BP&#196A5Fe&#1B6F63a&#1D7366r&#1F776Al &#238072C&#258576o&#27897Ao&#298D7Dl&#2B9281d&#2D9685o&#2E9A89w&#309F8Dn&#32A391: ");
                String suffix = Util.parseHexColors("&#36AC98" + secondsLeft + "&#38B09Cs");
                String message = prefix + suffix;

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

}
