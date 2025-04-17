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

    private static Map<Material, Integer> maxItems = new HashMap<>();
    private static String prefix = ChatColor.translateAlternateColorCodes('&', "&6&lInventory &r&8» &7");
    private static final Map<Player, BukkitRunnable> activeChecks = new HashMap<>();

    public ItemCaps() {
        maxItems.put(Material.ENDER_PEARL, 8);
        maxItems.put(Material.BREEZE_ROD, 64);
        maxItems.put(Material.WIND_CHARGE, 64);
        maxItems.put(Material.EXPERIENCE_BOTTLE, 192);
        maxItems.put(Material.GOLDEN_APPLE, 128);
        maxItems.put(Material.ENCHANTED_GOLDEN_APPLE, 3);
        maxItems.put(Material.COBWEB, 64);
        maxItems.put(Material.TOTEM_OF_UNDYING, 2);
        maxItems.put(Material.GOLDEN_CARROT, 64);
        maxItems.put(Material.BUCKET, 16);
    }

    public static void startInventoryCheck(Player player) {
        stopInventoryCheck(player);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                checkInventory(player);
            }
        };
        task.runTaskTimer(Main.getInstance(), 0, 10);
        activeChecks.put(player, task);
    }

    public static void stopInventoryCheck(Player player) {
        BukkitRunnable task = activeChecks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }

    private static void checkInventory(Player player) {
        for (ItemStack item : player.getInventory()) {
            if (item != null && maxItems.containsKey(item.getType())) {
                int maxAmount = maxItems.get(item.getType());
                int currentAmount = 0;

                for (ItemStack stack : player.getInventory()) {
                    if (stack != null && stack.isSimilar(item)) {
                        currentAmount += stack.getAmount();
                    }
                }

                if (currentAmount > maxAmount) {
                    String itemFormat = item.getType().toString().toLowerCase().replace('_', ' ');
                    player.sendMessage(prefix + "You cannot have more than " +
                            ChatColor.RED + maxAmount + ChatColor.GRAY + " of " + ChatColor.RED + itemFormat + ChatColor.GRAY + " in your inventory at once!");

                    int excessAmount = currentAmount - maxAmount;
                    dropExcessItems(player, item, excessAmount);
                }
            }
        }
    }

    private static void dropExcessItems(Player player, ItemStack item, int excessAmount) {
        ItemStack excessItem = new ItemStack(item);
        excessItem.setAmount(excessAmount);

        player.getWorld().dropItemNaturally(player.getLocation(), excessItem);
        player.getInventory().removeItem(excessItem);
    }
}
