package com.mills.elements;

import com.mills.elements.Discord.DiscordCombatlog;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
                    } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        itemName = item.getItemMeta().getDisplayName();
                    } else {
                        itemName = Util.format(item.getType().toString());
                    }

                    itemNames.add(item.getAmount() + "x " + itemName);
                }
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
