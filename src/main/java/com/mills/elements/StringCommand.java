package com.mills.elements;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class StringCommand implements CommandExecutor {

    private HashMap<UUID, Long> cooldown = new HashMap<>();
    private final long cooldownTime = 60000;
    private final String prefix = ChatColor.translateAlternateColorCodes('&', "&6&lString &r&8» &7");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();

            if (isOnCooldown(uuid)) {
                long seconds = getTimeLeft(uuid) / 1000;
                player.sendMessage(prefix + ChatColor.RED + "wait " + seconds + " more seconds!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            } else {
                player.sendMessage(prefix + ChatColor.YELLOW + "claimed 2304x String!");
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
                player.getInventory().addItem(new ItemStack(Material.STRING, 2304));
                setCooldown(uuid);
            }

        }

        return false;
    }

    private void setCooldown(UUID playerUUID) {
        cooldown.put(playerUUID, System.currentTimeMillis());
    }

    private long getTimeLeft(UUID playerUUID) {
        return Math.max(0, (cooldown.get(playerUUID) + cooldownTime) - System.currentTimeMillis());
    }

    private boolean isOnCooldown(UUID playerUUID) {
        if (!cooldown.containsKey(playerUUID)) return false;
        long timeLeft = (cooldown.get(playerUUID) + cooldownTime) - System.currentTimeMillis();
        return timeLeft > 0;
    }
}