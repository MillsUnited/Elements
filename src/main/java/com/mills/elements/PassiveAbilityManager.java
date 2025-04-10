package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PassiveAbilityManager implements Listener {

    private static final Map<UUID, Set<ItemStack>> activeItems = new HashMap<>();
    private final HashMap<UUID, BukkitRunnable> coordinateTasks = new HashMap<>();
    private final Set<UUID> noFallDamage = new HashSet<>();
    private String prefix = ChatColor.translateAlternateColorCodes('&', "&4&lJuggernaut &r&8» &c");

    private final JavaPlugin plugin;

    public PassiveAbilityManager(JavaPlugin plugin) {
        this.plugin = plugin;
        startInventoryCheck();
        startJugernautCheck();
    }

    private void startInventoryCheck() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID uuid = player.getUniqueId();
                Set<ItemStack> currentItems = new HashSet<>();
                boolean hasWindItem = false;
                boolean hasHelmetItem = false;
                boolean hasGlowing = false;

                for (ItemStack item : player.getInventory().getContents()) {
                    if (item == null) continue;

                    if (isElementalItem(item)) {
                        currentItems.add(item);

                        String type = getElementalType(item);
                        if ("wind".equals(type)) {
                            hasWindItem = true;
                        }

                        applyAbility(player, item);
                    }
                }

                ItemStack juggernautHelmet = player.getInventory().getHelmet();
                ItemStack juggernautChestplate = player.getInventory().getChestplate();
                ItemStack juggernautLeggings = player.getInventory().getLeggings();
                ItemStack juggernautBoots = player.getInventory().getBoots();

                if (isFullJuggernautArmor(juggernautHelmet, juggernautChestplate, juggernautLeggings, juggernautBoots)) {
                    hasGlowing = true;
                    hasHelmetItem = true;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 60, 0, true, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0, true, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 0, true, false));
                }

                ItemStack helmet = player.getInventory().getHelmet();
                if (helmet != null && isElementalItem(helmet) && "king".equals(getElementalType(helmet))) {
                    hasHelmetItem = true;
                    applyAbility(player, helmet);
                }

                if (hasWindItem && !hasNoFallDamage(player)) {
                    addNoFallDamage(player);
                } else if (!hasWindItem && hasNoFallDamage(player)) {
                    removeNoFallDamage(player);
                }

                if (hasGlowing) {
                    JuggernautTeamManger.addTeam(player);
                } else {
                    JuggernautTeamManger.removeTeam(player);
                }

                if (hasHelmetItem && !isPlayerExtraHearts(player)) {
                    setPlayerHealth(player, 40.0);
                } else if (!hasHelmetItem && isPlayerExtraHearts(player)) {
                    setPlayerHealth(player, 20.0);
                }

                activeItems.put(uuid, currentItems);
            }
        }, 0L, 20L);
    }

    private void startJugernautCheck() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID playerID = player.getUniqueId();
                    ItemStack juggernautHelmet = player.getInventory().getHelmet();
                    ItemStack juggernautChestplate = player.getInventory().getChestplate();
                    ItemStack juggernautLeggings = player.getInventory().getLeggings();
                    ItemStack juggernautBoots = player.getInventory().getBoots();

                    if (isFullJuggernautArmor(juggernautHelmet, juggernautChestplate, juggernautLeggings, juggernautBoots)) {
                        if (!coordinateTasks.containsKey(playerID)) {
                            startCoordinateBroadcast(player);
                        }
                    } else {
                        stopCoordinateBroadcast(player);
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0 ,20);
    }

    private void startCoordinateBroadcast(Player player) {
        UUID uuid = player.getUniqueId();

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isFullJuggernautArmor(player.getInventory().getHelmet(), player.getInventory().getChestplate(),
                        player.getInventory().getLeggings(), player.getInventory().getBoots())) {
                    stopCoordinateBroadcast(player);
                    return;
                }

                Location loc = player.getLocation();
                String message = prefix + ChatColor.translateAlternateColorCodes('&', player.getName() + "'s coordinates: " +
                        ChatColor.RED + "(" + loc.getBlockX() +
                        ", " + loc.getBlockY() +
                        ", " + loc.getBlockZ() + ")!");

                Bukkit.broadcastMessage(message);
            }
        };
        task.runTaskTimer(Main.getInstance(), 0, 2400);
        coordinateTasks.put(uuid, task);
    }

    private void stopCoordinateBroadcast(Player player) {
        UUID uuid = player.getUniqueId();
        if (coordinateTasks.containsKey(uuid)) {
            coordinateTasks.get(uuid).cancel();
            coordinateTasks.remove(uuid);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        ItemStack clickedItem = e.getCurrentItem();
        if (isElementalItem(clickedItem) && isNotPlayersInventory(e.getInventory())) {
            e.setCancelled(true);
        }
    }

    private boolean isFullJuggernautArmor(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        return isJuggernautHelmet(helmet) && isJuggernautChestplate(chestplate) && isJuggernautLeggings(leggings) && isJuggernautBoots(boots);
    }

    private boolean isJuggernautHelmet(ItemStack item) {
        return isSameItem(item, Items.juggernautHelmet());
    }

    private boolean isJuggernautChestplate(ItemStack item) {
        return isSameItem(item, Items.juggernautChestplate());
    }

    private boolean isJuggernautLeggings(ItemStack item) {
        return isSameItem(item, Items.juggernautLeggings());
    }

    private boolean isJuggernautBoots(ItemStack item) {
        return isSameItem(item, Items.juggernautBoots());
    }

    private boolean isElementalItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(Items.getElementalKey(), PersistentDataType.STRING);
    }

    private String getElementalType(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(Items.getElementalKey(), PersistentDataType.STRING);
    }

    private boolean isSameItem(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        if (a.getType() != b.getType()) return false;
        ItemMeta metaA = a.getItemMeta();
        ItemMeta metaB = b.getItemMeta();
        if (metaA == null || metaB == null) return false;
        return metaA.hasDisplayName() && metaB.hasDisplayName() && metaA.getDisplayName().equals(metaB.getDisplayName());
    }

    private void applyAbility(Player player, ItemStack item) {
        String type = getElementalType(item);
        if (type == null) return;

        switch (type) {
            case "fire":
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0, true, false));
                break;
            case "water":
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 60, 0, true, false));
                break;
            case "shadow":
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60, 0, true, false));
                break;
            case "earth":
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 60, 1, true, false));
                break;
            case "nature":
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, 1, true, false));
                break;
            case "sun":
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0, true, false));
                break;
            case "ice":
                if (isInIceBiome(player)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1, true, false));
                }
                break;
            case "king":
                player.setHealthScale(40);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 60, 0, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 60, 0, true, false));
                break;
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

    private void setPlayerHealth(Player player, double amount) {
        player.setHealthScale(amount);
    }

    private boolean isPlayerExtraHearts(Player player) {
        double healthScale = player.getHealthScale();
        return healthScale == 40.0;
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