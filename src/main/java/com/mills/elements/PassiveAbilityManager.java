package com.mills.elements;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PassiveAbilityManager implements Listener {

    private static final Map<UUID, ItemStack> activeItems = new HashMap<>();

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

        if (clickedItem != null && isElementalItem(clickedItem)) {

            if (activeItems.containsKey(player.getUniqueId())) {
                activeItems.remove(player.getUniqueId());
                removeEffectIfNeeded(player);
            }
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent e) {
        ItemStack movedItem = e.getItem();

        if (isElementalItem(movedItem)) {
            Player player = (Player) e.getSource().getHolder();

            if (!activeItems.containsKey(player.getUniqueId())) {
                activeItems.put(player.getUniqueId(), movedItem.clone());
                applyEffectIfNeeded(player, movedItem);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (activeItems.containsKey(player.getUniqueId()) && activeItems.get(player.getUniqueId()).isSimilar(droppedItem)) {
            activeItems.remove(player.getUniqueId());
            removeEffectIfNeeded(player);
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

    private void removeEffectIfNeeded(Player player) {
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }
}
