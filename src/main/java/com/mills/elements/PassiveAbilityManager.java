package com.mills.elements;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PassiveAbilityManager implements Listener {

    // Map to store the player's elemental items
    private static final Map<UUID, Set<ItemStack>> activeItems = new HashMap<>();

    private final FallDamageListener fallDamageListener;

    public PassiveAbilityManager(FallDamageListener fallDamageListener) {
        this.fallDamageListener = fallDamageListener;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Set<ItemStack> playerItems = new HashSet<>();

        // Check the entire inventory on login for elemental items (excluding offhand)
        for (ItemStack item : player.getInventory().getContents()) {
            if (isElementalItem(item)) {
                playerItems.add(item.clone());
                applyEffectIfNeeded(player, item);
            }
        }

        if (!playerItems.isEmpty()) {
            activeItems.put(player.getUniqueId(), playerItems);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack pickedUpItem = event.getItem().getItemStack();

        if (isElementalItem(pickedUpItem)) {
            Set<ItemStack> playerItems = activeItems.getOrDefault(player.getUniqueId(), new HashSet<>());
            playerItems.add(pickedUpItem);
            activeItems.put(player.getUniqueId(), playerItems);
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
                Set<ItemStack> playerItems = activeItems.get(player.getUniqueId());
                if (playerItems != null) {
                    playerItems.remove(hotbarItem);
                    removeEffectIfNeeded(player, hotbarItem);
                }
            }

            // Keep effect if moved via hotkey
            if (isElementalItem(clickedItem)) {
                Set<ItemStack> playerItems = activeItems.getOrDefault(player.getUniqueId(), new HashSet<>());
                playerItems.add(clickedItem.clone());
                activeItems.put(player.getUniqueId(), playerItems);
                applyEffectIfNeeded(player, clickedItem);
            }
        }

        // Handle Hotbar Swap and Move-And-Readd
        if (e.getAction() == InventoryAction.HOTBAR_SWAP ||
                e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {

            ItemStack swapItem = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());

            if (isElementalItem(swapItem)) {
                Set<ItemStack> playerItems = activeItems.get(player.getUniqueId());
                if (playerItems != null) {
                    playerItems.remove(swapItem);
                    removeEffectIfNeeded(player, swapItem);
                }
            }

            if (isElementalItem(clickedItem)) {
                Set<ItemStack> playerItems = activeItems.getOrDefault(player.getUniqueId(), new HashSet<>());
                playerItems.add(clickedItem.clone());
                activeItems.put(player.getUniqueId(), playerItems);
                applyEffectIfNeeded(player, clickedItem);
            }
        }

        // Handle Normal Clicking/Moving
        if (isElementalItem(clickedItem)) {
            Set<ItemStack> playerItems = activeItems.get(player.getUniqueId());
            if (playerItems != null && playerItems.contains(clickedItem)) {
                playerItems.remove(clickedItem);
                activeItems.put(player.getUniqueId(), playerItems);
                removeEffectIfNeeded(player, clickedItem);
            }
        }

        // Handle placing item in the hotbar (dragging it in)
        if (isElementalItem(cursorItem)
                && (e.getSlot() >= 0 && e.getSlot() <= 35)) {
            Set<ItemStack> playerItems = activeItems.getOrDefault(player.getUniqueId(), new HashSet<>());
            playerItems.add(cursorItem.clone());
            activeItems.put(player.getUniqueId(), playerItems);
            applyEffectIfNeeded(player, cursorItem);
        }
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        // Check if the entity is an item frame
        if (entity instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) entity;

            // Check if the item frame is empty
            if (itemFrame.getItem().getType() == Material.AIR) {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();

                // Check if the player is holding an elemental item
                if (isElementalItem(itemInHand)) {
                    // Remove the corresponding effect
                    removeEffectIfNeeded(player, itemInHand);
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        Set<ItemStack> playerItems = activeItems.get(player.getUniqueId());
        if (playerItems != null && playerItems.contains(droppedItem)) {
            playerItems.remove(droppedItem);
            activeItems.put(player.getUniqueId(), playerItems);
            removeEffectIfNeeded(player, droppedItem);
        }
    }

    private boolean isElementalItem(ItemStack item) {
        if (item == null) return false;
        return (item.isSimilar(Items.fire()) ||
                (item.isSimilar(Items.water())) ||
                (item.isSimilar(Items.shadow())) ||
                (item.isSimilar(Items.earth())) ||
                (item.isSimilar(Items.nature())) ||
                (item.isSimilar(Items.sun())) ||
                (item.isSimilar(Items.wind())));
    }

    private void applyEffectIfNeeded(Player player, ItemStack item) {
        if (item.isSimilar(Items.fire())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 0, true, false));
        } else if (item.isSimilar(Items.water())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, PotionEffect.INFINITE_DURATION, 0, true, false));
        } else if (item.isSimilar(Items.shadow())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 0, true, false));
        } else if (item.isSimilar(Items.earth())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, PotionEffect.INFINITE_DURATION, 1, true, false));
        } else if (item.isSimilar(Items.nature())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, PotionEffect.INFINITE_DURATION, 1, true, false));
        } else if (item.isSimilar(Items.sun())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 0, true, false));
        } else if (item.isSimilar(Items.wind())) {
            if (!fallDamageListener.hasNoFallDamage(player)) {
                fallDamageListener.addNoFallDamage(player);
            }
        }
    }

    private void removeEffectIfNeeded(Player player, ItemStack item) {
        if (item.isSimilar(Items.fire())) {
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        } else if (item.isSimilar(Items.water())) {
            player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        } else if (item.isSimilar(Items.shadow())) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        } else if (item.isSimilar(Items.earth())) {
            player.removePotionEffect(PotionEffectType.HASTE);
        } else if (item.isSimilar(Items.nature())) {
            player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        } else if (item.isSimilar(Items.sun())) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        } else if (item.isSimilar(Items.wind())) {
            if (fallDamageListener.hasNoFallDamage(player)) {
                fallDamageListener.removeNoFallDamage(player);
            }
        }
    }

    // Check if the inventory is not the player's (i.e., it's another GUI like chest, anvil, etc.)
    private boolean isNotPlayersInventory(Inventory inventory) {
        return !(inventory.getHolder() instanceof Player);  // If the inventory holder is not the player
    }
}