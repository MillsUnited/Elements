package com.mills.elements;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ItemCaps implements Listener {

    private Map<Material, Integer> maxItems = new HashMap<>();
    private String prefix = ChatColor.translateAlternateColorCodes('&', "&6&lInventory &r&8» &7");

    public ItemCaps() {
        maxItems.put(Material.ENDER_PEARL, 8);
        maxItems.put(Material.BREEZE_ROD, 64);
        maxItems.put(Material.WIND_CHARGE, 64);
        maxItems.put(Material.EXPERIENCE_BOTTLE, 192);
        maxItems.put(Material.GOLDEN_APPLE, 128);
        maxItems.put(Material.ENCHANTED_GOLDEN_APPLE, 3);
        maxItems.put(Material.COBWEB, 128);
        maxItems.put(Material.TOTEM_OF_UNDYING, 2);
    }

    public void startInventoryCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
                    checkInventory(player);
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 10);
    }

    // Check the player's inventory for item limits
    private void checkInventory(Player player) {
        for (ItemStack item : player.getInventory()) {
            if (item != null && maxItems.containsKey(item.getType())) {
                int maxAmount = maxItems.get(item.getType());
                int currentAmount = 0;

                // Count the amount of this item in the player's inventory
                for (ItemStack stack : player.getInventory()) {
                    if (stack != null && stack.isSimilar(item)) {
                        currentAmount += stack.getAmount();
                    }
                }

                // If the player exceeds the max amount, handle the overflow
                if (currentAmount > maxAmount) {
                    String itemFormat = item.getType().toString().toLowerCase().replace('_', ' ');
                    player.sendMessage(prefix + "You cannot have more than " +
                            ChatColor.RED + maxAmount + ChatColor.GRAY + " of " + ChatColor.RED + itemFormat + ChatColor.GRAY + " in your inventory at once!");

                    // Calculate how many excess items there are
                    int excessAmount = currentAmount - maxAmount;
                    dropExcessItems(player, item, excessAmount);
                }
            }
        }
    }

    // Drop excess items on the ground
    private void dropExcessItems(Player player, ItemStack item, int excessAmount) {
        // Make a new item stack with the excess amount
        ItemStack excessItem = new ItemStack(item);
        excessItem.setAmount(excessAmount);

        // Drop the excess items at the player's location
        player.getWorld().dropItemNaturally(player.getLocation(), excessItem);
        player.getInventory().removeItem(excessItem);
    }
}
