package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class PassiveAbilityManager implements Listener {

    private static final Map<UUID, Set<ItemStack>> activeItems = new HashMap<>();
    private final Set<UUID> noFallDamage = new HashSet<>();

    private final JavaPlugin plugin;

    public PassiveAbilityManager(JavaPlugin plugin) {
        this.plugin = plugin;
        startInventoryCheck();
    }

    public void startInventoryCheck() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                Set<ItemStack> currentItems = new HashSet<>();
                boolean hasWindItem = false;

                for (ItemStack item : player.getInventory().getContents()) {
                    if (item == null) continue;

                    if (isElementalItem(item)) {
                        currentItems.add(item);

                        if (item.isSimilar(Items.wind())) {
                            hasWindItem = true;
                        }

                        // Apply effect every tick
                        applyAbility(player, item);
                    }
                }

                if (hasWindItem && !hasNoFallDamage(player)) {
                    addNoFallDamage(player);
                } else if (!hasWindItem && hasNoFallDamage(player)) {
                    removeNoFallDamage(player);
                }

                activeItems.put(uuid, currentItems);
            }
        }, 0L, 1L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        if (isElementalItem(clickedItem) && isNotPlayersInventory(e.getInventory())) {
            e.setCancelled(true);
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
                (item.isSimilar(Items.ice())) ||
                (item.isSimilar(Items.wind())));
    }

    private void applyAbility(Player player, ItemStack item) {
        if (item.isSimilar(Items.fire())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0, true, false));
        } else if (item.isSimilar(Items.water())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60, 0, true, false));
        } else if (item.isSimilar(Items.shadow())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 0, true, false));
        } else if (item.isSimilar(Items.earth())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 60, 1, true, false));
        } else if (item.isSimilar(Items.nature())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, 1, true, false));
        } else if (item.isSimilar(Items.sun())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 60, 0, true, false));
        } else if (item.isSimilar(Items.ice())) {
            if (isInIceBiome(player)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1, true, false));
            }
        }
    }

    private boolean isNotPlayersInventory(Inventory inventory) {
        return !(inventory.getHolder() instanceof Player);
    }

    private boolean isInIceBiome(Player player) {
        Location playerLoc = player.getLocation();
        Biome biome = playerLoc.getBlock().getBiome();
        if (biome == Biome.ICE_SPIKES) return true;
        if (biome == Biome.SNOWY_PLAINS) return true;
        if (biome == Biome.SNOWY_TAIGA) return true;
        if (biome == Biome.SNOWY_SLOPES) return true;
        if (biome == Biome.FROZEN_PEAKS) return true;
        if (biome == Biome.JAGGED_PEAKS) return true;
        if (biome == Biome.GROVE) return true;
        if (biome == Biome.FROZEN_OCEAN) return true;
        if (biome == Biome.FROZEN_RIVER) return true;
        if (biome == Biome.DEEP_FROZEN_OCEAN) return true;
        return false;
    }

    private void addNoFallDamage(Player player) {
        noFallDamage.add(player.getUniqueId());
    }

    private void removeNoFallDamage(Player player) {
        noFallDamage.remove(player.getUniqueId());
    }

    private boolean hasNoFallDamage(Player player) {
        return noFallDamage.contains(player.getUniqueId());
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            if (noFallDamage.contains(player.getUniqueId()) && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setDamage(0);
            }
        }
    }
}
