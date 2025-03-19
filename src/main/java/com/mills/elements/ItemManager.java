package com.mills.elements;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    private static final Map<String, AbilityManager> registeredItems = new HashMap<>();

    public static void registerItem(String itemName, ItemStack item,
                                    LeftClickAbility leftClickAbility,
                                    RightClickAbility rightClickAbility) {
        registeredItems.put(itemName, new AbilityManager(item, leftClickAbility, rightClickAbility));
    }

    public static AbilityManager getElementItem(ItemStack item) {
        if (item == null) return null;

        for (AbilityManager abilityManager : registeredItems.values()) {
            if (item.isSimilar(abilityManager.getItem())) {
                return abilityManager;
            }
        }
        return null;
    }
}