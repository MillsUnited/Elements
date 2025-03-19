package com.mills.elements;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilityManager {
    private final ItemStack item;
    private final LeftClickAbility leftClickAbility;
    private final RightClickAbility rightClickAbility;

    public AbilityManager(ItemStack item, LeftClickAbility leftClickAbility, RightClickAbility rightClickAbility) {
        this.item = item;
        this.leftClickAbility = leftClickAbility;
        this.rightClickAbility = rightClickAbility;
    }

    public ItemStack getItem() {
        return item;
    }

    public void executeLeftClick(Player player) {
        if (leftClickAbility != null) {
            leftClickAbility.execute(player);
        }
    }

    public void executeRightClick(Player player) {
        if (rightClickAbility != null) {
            rightClickAbility.execute(player);
        }
    }
}
