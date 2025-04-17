package com.mills.elements;

import com.mills.elements.Discord.EmbededMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CombatLogManager implements Listener {

    private JDA bot;

    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private static String prefix = ChatColor.translateAlternateColorCodes('&', "&6&lCombat &r&8» &7");
    private final Map<UUID, Long> logoutTimes = new HashMap<>();
    private final long cooldownTime = 180000;

    public CombatLogManager(JDA bot) {
        this.bot = bot;
    }

    @EventHandler
    public void onPlayerAttacker(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();

            if (!isOnCooldown(attacker.getUniqueId())) {
                attacker.sendMessage(prefix + "You have entered combat with " + ChatColor.RED + victim.getName() + ChatColor.GRAY + " for 3 minutes, please do not logout!");
            }

            if (!isOnCooldown(victim.getUniqueId())) {
                victim.sendMessage(prefix + "You have entered combat with " + ChatColor.RED + attacker.getName() + ChatColor.GRAY + " for 3 minutes, please do not logout!");
            }

            setCooldown(attacker.getUniqueId());
            setCooldown(victim.getUniqueId());

            ItemCaps.startInventoryCheck(attacker);
            ItemCaps.startInventoryCheck(victim);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (isOnCooldown(uuid)) {
            PlayerQuitEvent.QuitReason reason = e.getReason();
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
            double timeRemaining = getCooldown(player.getUniqueId());

            logoutTimes.put(uuid, System.currentTimeMillis());

            String formattedReason = reason.toString().substring(0,1).toUpperCase() + reason.toString().substring(1).toLowerCase();

            TextChannel channel = bot.getTextChannelById("1355602995752210591");
            if (channel != null) {
                channel.sendMessageEmbeds(EmbededMessages.combatLog(player.getName(), formattedReason, timeRemaining, itemNames).build()).queue();
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (logoutTimes.containsKey(uuid)) {
            long logoutTime = logoutTimes.get(uuid);
            long timeLoggedOut = currentTime - logoutTime;

            String player = e.getPlayer().getName();
            String time = formatDuration(timeLoggedOut);

            TextChannel channel = bot.getTextChannelById("1355602995752210591");
            if (channel != null) {
                channel.sendMessageEmbeds(EmbededMessages.lastLogin(player, time).build()).queue();
            }

            logoutTimes.remove(uuid);
        }
    }

    public void startCooldownChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<UUID> finished = new ArrayList<>();

                for (UUID uuid : cooldown.keySet()) {
                    if (!isOnCooldown(uuid)) {
                        Player player = org.bukkit.Bukkit.getPlayer(uuid);
                        if (player != null && player.isOnline()) {
                            ItemCaps.stopInventoryCheck(player);
                            player.sendMessage(prefix + "your combat timer has ended!");
                        }
                        finished.add(uuid);
                    }
                }

                for (UUID uuid : finished) {
                    cooldown.remove(uuid);
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L); // runs every 1 second
    }

    private String formatDuration(long milliseconds) {
        long seconds = milliseconds / 1000 % 60;
        long minutes = milliseconds / (1000 * 60) % 60;
        long hours = milliseconds / (1000 * 60 * 60) % 60;
        long days = milliseconds / (1000 * 60 * 60 * 24);

        StringBuilder sb = new StringBuilder();

        if (days > 0) sb.append(days).append(" day").append(days > 1 ? "s" : "").append(", ");
        if (hours > 0) sb.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(", ");
        if (minutes > 0) sb.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(", ");
        if (seconds > 0 || sb.length() == 0) // always show seconds unless everything else is 0
            sb.append(seconds).append(" second").append(seconds != 1 ? "s" : "");

        String result = sb.toString();
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }

        return result;
    }

    private void setCooldown(UUID playerUUID) {
        cooldown.put(playerUUID, System.currentTimeMillis());
    }

    private double getCooldown(UUID playerUUID) {
        if (isOnCooldown(playerUUID)) {
            long timeLeft = (cooldown.get(playerUUID) + cooldownTime) - System.currentTimeMillis();
            DecimalFormat format = new DecimalFormat("#.#");
            double secondsLeft = timeLeft / 1000.0;
            return Double.parseDouble(format.format(secondsLeft));
        }
        return -1;
    }

    public boolean isOnCooldown(UUID playerUUID) {
        if (!cooldown.containsKey(playerUUID)) return false;
        long timeLeft = (cooldown.get(playerUUID) + cooldownTime) - System.currentTimeMillis();
        return timeLeft > 0;
    }

}