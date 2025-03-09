package com.mills.elements;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ItemManager {
    private static final Map<String, AbilityManager> elementItems = new HashMap<>();

    public static void registerItem(String name, ItemStack item,
//                                    Consumer<Player> inventoryAbility,
                                    String leftClickAbilityName, Consumer<Player> leftClickAbility, long leftClickCooldown,
                                    String rightClickAbilityName, Consumer<Player> rightClickAbility, long rightClickCooldown) {
        elementItems.put(name, new AbilityManager(item, name,  leftClickAbilityName, leftClickAbility, leftClickCooldown, rightClickAbilityName, rightClickAbility, rightClickCooldown));
//        inventoryAbility,
    }

    public static AbilityManager getElementItem(ItemStack item) {
        for (AbilityManager elementalItem : elementItems.values()) {
            if (elementalItem.getItem().isSimilar(item)) {
                return elementalItem;
            }
        }
        return null;
    }
}
