package com.mills.elements;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class AbilityManager {
    private final ItemStack item;
    private final String itemName;
    private final String leftClickAbilityName;
    private final String rightClickAbilityName;
//    private final Consumer<Player> inventoryAbility;
    private final Consumer<Player> leftClickAbility;
    private final Consumer<Player> rightClickAbility;
    private final long leftClickCooldown;
    private final long rightClickCooldown;

    private final Map<UUID, Long> lastLeftClickUse = new HashMap<>();
    private final Map<UUID, Long> lastRightClickUse = new HashMap<>();

    public AbilityManager(ItemStack item, String itemName,
//                      Consumer<Player> inventoryAbility,
                      String leftClickAbilityName, Consumer<Player> leftClickAbility, long leftClickCooldown,
                      String rightClickAbilityName, Consumer<Player> rightClickAbility, long rightClickCooldown) {
        this.item = item;
        this.itemName = itemName;
//        this.inventoryAbility = inventoryAbility;
        this.leftClickAbilityName = leftClickAbilityName;
        this.leftClickAbility = leftClickAbility;
        this.leftClickCooldown = leftClickCooldown;
        this.rightClickAbilityName = rightClickAbilityName;
        this.rightClickAbility = rightClickAbility;
        this.rightClickCooldown = rightClickCooldown;
    }

    public ItemStack getItem() {
        return item;
    }

//    public void executeInventoryAbility(Player player) {
//        if (inventoryAbility != null) {
//            inventoryAbility.accept(player);
//        }
//    }

    public void executeLeftClickAbility(Player player) {
        if (leftClickAbility != null) {
            long currentTime = System.currentTimeMillis();
            if (lastLeftClickUse.containsKey(player)) {
                long elapsedTime = currentTime - lastLeftClickUse.get(player);
                if (elapsedTime < leftClickCooldown) {
                    long remainingTime = (leftClickCooldown - elapsedTime) / 1000;
                    player.sendMessage(ChatColor.RED + "Your " + ChatColor.GOLD + itemName + ChatColor.RED + " " + leftClickAbilityName + " is on cooldown for " + remainingTime + " more seconds.");
                    return;
                }
            }
            lastLeftClickUse.put(player.getUniqueId(), currentTime);
            leftClickAbility.accept(player);
        }
    }

    public void executeRightClickAbility(Player player) {
        if (rightClickAbility != null) {
            long currentTime = System.currentTimeMillis();
            if (lastRightClickUse.containsKey(player)) {
                long elapsedTime = currentTime - lastRightClickUse.get(player);
                if (elapsedTime < rightClickCooldown) {
                    long remainingTime = (rightClickCooldown - elapsedTime) / 1000;
                    player.sendMessage(ChatColor.RED + "Your " + ChatColor.GOLD + itemName + ChatColor.RED + " " + rightClickAbilityName + " is on cooldown for " + remainingTime + " more seconds.");
                    return;
                }
            }
            lastRightClickUse.put(player.getUniqueId(), currentTime);
            rightClickAbility.accept(player);
        }
    }

}
