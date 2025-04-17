package com.mills.elements;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

public class SmithingBlocker implements Listener {

    @EventHandler
    public void onSmithingPrepare(PrepareSmithingEvent e) {
        SmithingInventory inventory = e.getInventory();
        ItemStack baseItem = inventory.getInputEquipment();
        ItemStack result = e.getResult();
        if (baseItem == null || result == null) return;

        Material baseType = baseItem.getType();
        Material resultType = result.getType();

        if (isNetheriteArmor(resultType)) {
            if (!isNetheriteArmor(baseType)) {
                e.setResult(null);
            }
        }
    }

    private boolean isNetheriteArmor(Material type) {
        return type == Material.NETHERITE_HELMET ||
                type == Material.NETHERITE_CHESTPLATE ||
                type == Material.NETHERITE_LEGGINGS ||
                type == Material.NETHERITE_BOOTS ||
                type == Material.NETHERITE_SWORD ||
                type == Material.NETHERITE_AXE;
    }
}
