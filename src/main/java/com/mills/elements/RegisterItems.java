package com.mills.elements;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class RegisterItems {

    public static void registerElementalItems() {
        ItemManager.registerItem("Fire Element", Items.fire(),
                new LeftClickAbility("Fire Blast", player -> {

                    player.sendMessage(ChatColor.RED + "You used Fire Blast!");
                    fireBlastAbility(player);

                }, 120000, Items.fire()),

                new RightClickAbility("Volcanic Eruption", player -> {

                    player.sendMessage(ChatColor.RED + "You activated Volcano Eruption!");
                    volcanicEruption(player);

                }, 30000, Items.fire())
        );
    }

    private static void fireBlastAbility(Player attacker) {

        new BukkitRunnable() {
            int ticks = 0;
            Set<LivingEntity> hitEntities = new HashSet<>();

            @Override
            public void run() {
                if (ticks >= 200) {
                    cancel();
                    return;
                }

                Location start = attacker.getEyeLocation();
                Vector direction = start.getDirection().normalize();
                double stepSize = 0.5; // distance between each particle
                double maxDistance = 50.0; // beam length

                for (double i = 0; i <= maxDistance; i += stepSize) {
                    Location currentPoint = start.clone().add(direction.clone().multiply(i));

                    currentPoint.add(0, -1, 0);
                    currentPoint.getWorld().spawnParticle(Particle.FLAME, currentPoint, 1, 0, 0, 0, 0);

                    for (Entity entity : currentPoint.getWorld().getNearbyEntities(currentPoint, 0.5, 0.5, 0.5)) {
                        if (entity instanceof LivingEntity && entity != attacker) {
                            LivingEntity target = (LivingEntity) entity;

                            target.damage(2.0, attacker);
                            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                            hitEntities.add(target);
                        }
                    }
                }
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

    }

    private static void volcanicEruption(Player attacker) {
        Location originalLocation = attacker.getLocation();

        // Apply a very high upward velocity
        Vector velocity = new Vector(0, 50, 0);
        attacker.setVelocity(velocity);

        if (!FallDamageListener.hasNoFallDamage(attacker)) {
            FallDamageListener.addNoFallDamage(attacker);
        }

        // Place lava after launch
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            Location loc = originalLocation.clone();
            loc.setY(loc.getY());
            Block block = loc.getBlock();

            if (block.getType() == Material.AIR) {
                block.setType(Material.LAVA);
            }
        }, 5L);

        // Check when the player lands
        new BukkitRunnable() {
            @Override
            public void run() {
                Location belowPlayer = attacker.getLocation().clone().subtract(0, 1, 0);
                Block blockBelow = belowPlayer.getBlock();

                if (blockBelow.getType() != Material.AIR) {
                    // Ensure fall damage is fully prevented
                    attacker.setFallDistance(0);

                    // Delay immunity removal to ensure fall event doesn't register
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        FallDamageListener.removeNoFallDamage(attacker);
                    }, 40L); // 40 ticks = 2 seconds
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 5L, 2L);
    }
}
