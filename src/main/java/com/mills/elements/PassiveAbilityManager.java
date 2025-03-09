package com.mills.elements;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PassiveAbilityManager implements Listener {

    private static final Map<UUID, ItemStack> activeItems = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Check the entire inventory on login for elemental items (excluding offhand)
        for (ItemStack item : player.getInventory().getContents()) {
            if (isElementalItem(item)) {
                activeItems.put(player.getUniqueId(), item.clone());
                applyEffectIfNeeded(player, item);
                break;
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack pickedUpItem = event.getItem().getItemStack();

        if (isElementalItem(pickedUpItem)) {
            activeItems.put(player.getUniqueId(), pickedUpItem);
            applyEffectIfNeeded(player, pickedUpItem);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        ItemStack cursorItem = e.getCursor();

        // If the item is Fire and the player is in any inventory that is not their own (i.e., a chest, anvil, etc.)
        if (isElementalItem(clickedItem) && isNotPlayersInventory(e.getInventory())) {
            e.setCancelled(true);  // Cancel the event to prevent interaction
            return;  // Prevent further processing
        }

        // Handle Hotkey Movement (press 1-9 while hovering)
        if (e.getHotbarButton() != -1) {
            ItemStack hotbarItem = player.getInventory().getItem(e.getHotbarButton());

            // Remove effect if hotbar already has an elemental item
            if (isElementalItem(hotbarItem)) {
                activeItems.remove(player.getUniqueId());
                removeEffectIfNeeded(player, hotbarItem);
            }

            // Keep effect if moved via hotkey
            if (isElementalItem(clickedItem)) {
                activeItems.put(player.getUniqueId(), clickedItem.clone());
                applyEffectIfNeeded(player, clickedItem);
            }
        }

        // Handle Hotbar Swap and Move-And-Readd
        if (e.getAction() == InventoryAction.HOTBAR_SWAP ||
                e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {

            ItemStack swapItem = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());

            if (isElementalItem(swapItem)) {
                activeItems.remove(player.getUniqueId());
                removeEffectIfNeeded(player, swapItem);
            }

            if (isElementalItem(clickedItem)) {
                activeItems.put(player.getUniqueId(), clickedItem.clone());
                applyEffectIfNeeded(player, clickedItem);
            }
        }

        // Handle Normal Clicking/Moving
        if (isElementalItem(clickedItem)) {
            if (activeItems.containsKey(player.getUniqueId())
                    && activeItems.get(player.getUniqueId()).isSimilar(clickedItem)) {
                activeItems.remove(player.getUniqueId());
                removeEffectIfNeeded(player, clickedItem);
            }
        }

        // Handle placing item in the hotbar (dragging it in)
        if (isElementalItem(cursorItem)
                && (e.getSlot() >= 0 && e.getSlot() <= 35)) {
            activeItems.put(player.getUniqueId(), cursorItem.clone());
            applyEffectIfNeeded(player, cursorItem);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (activeItems.containsKey(player.getUniqueId())
                && activeItems.get(player.getUniqueId()).isSimilar(droppedItem)) {
            activeItems.remove(player.getUniqueId());
            removeEffectIfNeeded(player, droppedItem);
        }
    }

    private boolean isElementalItem(ItemStack item) {
        return item != null && (item.isSimilar(Items.fire()));
    }

    private void applyEffectIfNeeded(Player player, ItemStack item) {
        if (item.isSimilar(Items.fire())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 0, true, false));
        }
    }

    private void removeEffectIfNeeded(Player player, ItemStack item) {
        if (item.isSimilar(Items.fire())) {
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }
    }

    // Check if the inventory is not the player's (i.e., it's another GUI like chest, anvil, etc.)
    private boolean isNotPlayersInventory(Inventory inventory) {
        return !(inventory.getHolder() instanceof Player);  // If the inventory holder is not the player
    }
}
