package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class LeftClickAbility {
    private final String abilityName;
    private final long cooldownMillis;
    private final Consumer<Player> abilityAction;
    private final ItemStack requiredItem;

    private static final Map<UUID, Map<String, BossBar>> activeBossBars = new HashMap<>();

    public LeftClickAbility(String abilityName, Consumer<Player> abilityAction, long cooldownSeconds, ItemStack requiredItem) {
        this.abilityAction = abilityAction;
        this.abilityName = abilityName;
        this.cooldownMillis = cooldownSeconds;
        this.requiredItem = requiredItem;
    }

    public void execute(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (!isMatchingItem(heldItem)) {
            return;
        }

        UUID playerId = player.getUniqueId();

        if (CooldownManager.isOnCooldown(playerId, abilityName)) {
            return;
        }

        CooldownManager.setCooldown(playerId, abilityName, cooldownMillis);

        abilityAction.accept(player);

        startCooldownBossBar(player, heldItem);
    }

    private void startCooldownBossBar(Player player, ItemStack heldItem) {
        UUID playerId = player.getUniqueId();
        activeBossBars.putIfAbsent(playerId, new HashMap<>());

        if (activeBossBars.get(playerId).containsKey(abilityName)) {
            activeBossBars.get(playerId).get(abilityName).removeAll();
        }

        BarColor barColor = getBossBarColor(heldItem);
        BossBar bossBar = Bukkit.createBossBar(abilityName, barColor, BarStyle.SOLID);
        bossBar.addPlayer(player);
        activeBossBars.get(playerId).put(abilityName, bossBar);

        new BukkitRunnable() {
            long startTime = System.currentTimeMillis();
            long endTime = startTime + cooldownMillis;

            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long remainingTime = endTime - currentTime;

                if (remainingTime <= 0) {
                    bossBar.removeAll();
                    activeBossBars.get(playerId).remove(abilityName);
                    if (activeBossBars.get(playerId).isEmpty()) {
                        activeBossBars.remove(playerId);
                    }
                    this.cancel();
                    return;
                }

                double progress = (double) remainingTime / cooldownMillis;
                bossBar.setProgress(Math.max(0, progress));
                double remainingTimeSec = (double) remainingTime / 1000;
                String formatted = String.format("%.1f", remainingTimeSec);
                bossBar.setTitle(abilityName + " - " + formatted + "s");
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    private boolean isMatchingItem(ItemStack item) {
        return item != null && item.isSimilar(requiredItem);
    }

    private BarColor getBossBarColor(ItemStack item) {
        if (item.isSimilar(Items.fire())) {
            return BarColor.RED;
        } else if (item.isSimilar(Items.water())) {
            return BarColor.BLUE;
        } else if (item.isSimilar(Items.ice())) {
            return BarColor.WHITE;
        } else if (item.isSimilar(Items.shadow())) {
            return BarColor.PURPLE;
        } else if (item.isSimilar(Items.earth())) {
            return BarColor.YELLOW;
        } else if (item.isSimilar(Items.nature())) {
            return BarColor.GREEN;
        } else if (item.isSimilar(Items.sun())) {
            return BarColor.PINK;
        } else if (item.isSimilar(Items.wind())) {
            return BarColor.WHITE;
        }
        return BarColor.WHITE;
    }
}
