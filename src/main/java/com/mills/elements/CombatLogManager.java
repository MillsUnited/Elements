package com.mills.elements;

import com.mills.elements.Discord.DiscordCombatlog;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class CombatLogManager implements Listener {

    private DiscordCombatlog discordCombatlog;

    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final long cooldownTime = 10000;
    private final String prefix = ChatColor.translateAlternateColorCodes('&', "&6&lCombat &r&8» &7");

    public CombatLogManager(DiscordCombatlog discordCombatlog) {
        this.discordCombatlog = discordCombatlog;
    }

    @EventHandler
    public void onPlayerAttacker(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();

            if (!isOnCooldown(attacker.getUniqueId())) {
                attacker.sendMessage(prefix + "You are now in combat with " +
                        ChatColor.RED + victim.getName() + ChatColor.GRAY + " for " + ChatColor.RED + "10" + ChatColor.GRAY + " seconds!");
            }

            if (!isOnCooldown(victim.getUniqueId())) {
                victim.sendMessage(prefix + "You are now in combat with " +
                        ChatColor.RED + attacker.getName() + ChatColor.GRAY + " for " + ChatColor.RED + "10" + ChatColor.GRAY + " seconds!");
            }

            setCooldown(attacker.getUniqueId());
            setCooldown(victim.getUniqueId());

        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (isOnCooldown(uuid)) {
//            PlayerQuitEvent.QuitReason reason = e.getReason();
            String reason = "Disconnected";
            List<String> itemNames = new ArrayList<>();
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getAmount() > 0) {
                    String itemName;

                    if (item.isSimilar(Items.fire())) {
                        itemName = "Fire Element";
                    } else if (item.isSimilar(Items.water())) {
                        itemName = "Water Element";
                    } else if (item.isSimilar(Items.ice())) {
                        itemName = "Ice Element";
                    } else if (item.isSimilar(Items.shadow())) {
                        itemName = "Shadow Element";
                    } else if (item.isSimilar(Items.wind())) {
                        itemName = "Wind Element";
                    } else if (item.isSimilar(Items.nature())) {
                        itemName = "Nature Element";
                    } else if (item.isSimilar(Items.sun())) {
                        itemName = "Sun Element";
                    } else if (item.isSimilar(Items.earth())) {
                        itemName = "Earth Element";
                    } else if (item.getType() == Material.ENCHANTED_BOOK && item.hasItemMeta()) {

                        ItemMeta meta = item.getItemMeta();
                        if (meta instanceof EnchantmentStorageMeta bookMeta) {
                            itemName = meta.hasDisplayName() ? meta.getDisplayName() : "Enchanted Book";

                            if (!bookMeta.getStoredEnchants().isEmpty()) {
                                String enchantString = bookMeta.getStoredEnchants().entrySet().stream()
                                        .map(e2 -> Util.format(e2.getKey().getKey().getKey()) + " " + e2.getValue())
                                        .collect(Collectors.joining(", "));
                                itemName += " (" + enchantString + ")";
                            }

                            itemNames.add(item.getAmount() + "x " + itemName);
                            continue;
                        } else {
                            itemName = "Enchanted Book";
                        }
                    } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        itemName = item.getItemMeta().getDisplayName();
                    } else {
                        itemName = Util.format(item.getType().toString());
                    }

                    if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
                        Map<Enchantment, Integer> enchants = item.getItemMeta().getEnchants();
                        String enchantString = enchants.entrySet().stream()
                                .map(e1 -> Util.format(e1.getKey().getKey().getKey()) + " " + e1.getValue())
                                .collect(Collectors.joining(", "));
                        itemName += " (" + enchantString + ")";
                    }

                    itemNames.add(item.getAmount() + "x " + itemName);
                }
            }

            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
                    player.getWorld().dropItem(player.getLocation(), item.clone());
                    player.getInventory().remove(item);
                }
            }

            ItemStack offhand = player.getInventory().getItemInOffHand();
            if (offhand != null && offhand.getType() == Material.TOTEM_OF_UNDYING) {
                player.getInventory().setItemInOffHand(null);
            }

            player.damage(1000.0);

            discordCombatlog.sendMessage(player.getName(), reason, itemNames);
        }
    }

    private void setCooldown(UUID playerUUID) {
        cooldown.put(playerUUID, System.currentTimeMillis());
    }

    private long getTimeLeft(UUID playerUUID) {
        Long startTime = cooldown.get(playerUUID);
        if (startTime == null) return 0;
        return Math.max(0, (startTime + cooldownTime) - System.currentTimeMillis());
    }

    public boolean isOnCooldown(UUID playerUUID) {
        if (!cooldown.containsKey(playerUUID)) return false;
        long timeLeft = (cooldown.get(playerUUID) + cooldownTime) - System.currentTimeMillis();
        return timeLeft > 0;
    }

}