package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PassiveAbilityManager extends BukkitRunnable {
    private final JavaPlugin plugin;

    public PassiveAbilityManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    AbilityManager abilityManager = ItemManager.getElementItem(item);
                    if (abilityManager != null) {
                        abilityManager.executeInventoryAbility(player);
                    }
                }
            }
        }
    }
}
