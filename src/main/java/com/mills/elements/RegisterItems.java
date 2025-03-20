package com.mills.elements;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.WindCharge;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class RegisterItems {

    public static void registerElementalItems() {
        ItemManager.registerItem("Fire Element", Items.fire(),
                new LeftClickAbility("Fire Blast", player -> {

                    player.sendMessage(Main.prefix + "You have activated Fire Blast!");
                    fireBlastAbility(player);

                }, 120000, Items.fire()),

                new RightClickAbility("Volcanic Eruption", player -> {

                    player.sendMessage(Main.prefix + "You have activated Volcanic Eruption!");
                    volcanicEruption(player);

                }, 30000, Items.fire())
        );
        ItemManager.registerItem("Ice Element", Items.ice(),
                new LeftClickAbility("Player Freeze", player -> {

                    player.sendMessage(Main.prefix + "You have activated Player Freeze!");
                    playerFreeze(player, 5.0);

                }, 120000, Items.ice()),

                new RightClickAbility("Ice Dash", player -> {

                    player.sendMessage(Main.prefix + "You have activated Ice Dash!");
                    iceDash(player);

                }, 20000, Items.ice())
        );
        ItemManager.registerItem("Water Element", Items.water(),
                new LeftClickAbility("Poseidon’s Rush", player -> {

                    player.sendMessage(Main.prefix + "You have activated Poseidon’s Rush!");
                    poseidonsRush(player);

                }, 120000, Items.water()),

                new RightClickAbility("Water knockback", player -> {

                    player.sendMessage(Main.prefix + "You have activated Water knockback!");
                    waterKB(player, 10.0);

                }, 60000, Items.water())
        );
        ItemManager.registerItem("Shadow Element", Items.shadow(),
                new LeftClickAbility("True Invisibility", player -> {

                    player.sendMessage(Main.prefix + "You have activated True Invisibility!");
                    trueInvisibility(player);

                }, 120000, Items.shadow()),

                new RightClickAbility("Shadow Speed", player -> {

                    player.sendMessage(Main.prefix + "You have activated Shadow Speed!");
                    shadowSpeed(player, 10.0);

                }, 90000, Items.shadow())
        );
        ItemManager.registerItem("Earth Element", Items.earth(),
                new LeftClickAbility("Hasty Attack Speed", player -> {

                    player.sendMessage(Main.prefix + "You have activated Hasty Attack Speed!");
                    attackSpeed(player, 3.0);

                }, 120000, Items.earth()),

                new RightClickAbility("Hulk Jump", player -> {

                    player.sendMessage(Main.prefix + "You have activated Hulk Jump!");
                    hulkJump(player);

                }, 30000, Items.earth())
        );
        ItemManager.registerItem("Nature Element", Items.nature(),
                new LeftClickAbility("Juggernaut Resistance", player -> {

                    player.sendMessage(Main.prefix + "You have activated Juggernaut Resistance!");
                    juggernautResistance(player);

                }, 120000, Items.nature()),

                new RightClickAbility("Allie Heal", player -> {

                    player.sendMessage(Main.prefix + "You have activated Allie Heal!");
                    allieHeal(player);

                }, 60000, Items.nature())
        );
        ItemManager.registerItem("Sun Element", Items.sun(),
                new LeftClickAbility("Solar Strength", player -> {

                    player.sendMessage(Main.prefix + "You have activated Solar Strength!");
                    solarStrength(player);

                }, 120000, Items.sun()),

                new RightClickAbility("Sun’s Rays", player -> {

                    player.sendMessage(Main.prefix + "You have activated Sun’s Rays!");
                    sunsRays(player);

                }, 60000, Items.sun())
        );
        ItemManager.registerItem("Wind Element", Items.wind(),
                new LeftClickAbility("Wind Crush", player -> {

                    player.sendMessage(Main.prefix + "You have activated Wind Crush!");
                    windCrush(player);

                }, 120000, Items.wind()),

                new RightClickAbility("Tornado Charge", player -> {

                    player.sendMessage(Main.prefix + "You have activated Tornado Charge!");
                    tornadoCharge(player);

                }, 30000, Items.wind())
        );
    }

    private static void fireBlastAbility(Player attacker) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 200) {
                    cancel();
                    return;
                }

                ItemStack item = attacker.getInventory().getItemInMainHand();
                if (!item.isSimilar(Items.fire())) {
                    cancel();
                    return;
                }

                Location start = attacker.getEyeLocation();
                Vector direction = start.getDirection().normalize();
                double stepSize = 0.5;
                double maxDistance = 50.0;

                for (double i = 0; i <= maxDistance; i += stepSize) {
                    Location currentPoint = start.clone().add(direction.clone().multiply(i));

                    currentPoint.add(0, -1, 0);
                    currentPoint.getWorld().spawnParticle(Particle.FLAME, currentPoint, 1, 0, 0, 0, 0);

                    if (ticks % 10 == 0) {
                        for (Entity entity : currentPoint.getWorld().getNearbyEntities(currentPoint, 0.5, 0.5, 0.5)) {
                            if (entity instanceof Player && entity != attacker) {
                                Player target = (Player) entity;

                                if (target.isDead() || target.getHealth() <= 0) {
                                    continue;
                                }

                                double fixedDamage = 0.5;
                                if (target.getHealth() <= fixedDamage) {
                                    target.setHealth(0);
                                    target.damage(0, attacker);
                                } else {
                                    target.setHealth(target.getHealth() - fixedDamage);
                                }

                                Vector kbDirection = target.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize();

                                double horizontalStrength = 0.8; // Lower horizontal impact
                                double verticalBoost = 0.35; // Stronger upward motion

                                Vector kb = kbDirection.multiply(horizontalStrength);
                                kb.setY(verticalBoost); // Set strong upward force

                                target.setVelocity(kb);
                                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                                attacker.playSound(attacker.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
                            }
                        }
                    }
                }
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    private static void volcanicEruption(Player attacker) {
        Location originalLocation = attacker.getLocation();

        Vector velocity = new Vector(0, 50, 0);
        attacker.setVelocity(velocity);

        if (!FallDamageListener.hasNoFallDamage(attacker)) {
            FallDamageListener.addNoFallDamage(attacker);
        }

        // place lava after player launches
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            Location loc = originalLocation.clone();
            loc.setY(loc.getY());
            Block block = loc.getBlock();

            if (block.getType() == Material.AIR || block.getType() == Material.WATER) {
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

                    attacker.setFallDistance(0);

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        FallDamageListener.removeNoFallDamage(attacker);
                    }, 40L);
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 5L, 2L);
    }

    private static void playerFreeze(Player attacker, double radius) {
        Set<UUID> affectedPlayers = new HashSet<>();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(attacker) && target.getWorld().equals(attacker.getWorld()) && target.getLocation().distance(attacker.getLocation()) <= radius) {
                if (!PlayerMovementListener.isFrozen(target)) {
                    PlayerMovementListener.addFrozenPlayer(target);
                    target.sendMessage(Main.prefix + "You have been frozen by " + attacker.getName() + " for 5 seconds!");
                }
                affectedPlayers.add(target.getUniqueId());
                target.sendMessage(Main.prefix + "You have been given frozen effect by " + attacker.getName() + " for 30 seconds!");
                target.setFreezeTicks(Integer.MAX_VALUE);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (PlayerMovementListener.isFrozen(player)) {
                        PlayerMovementListener.removeFrozenPlayer(player);
                        player.sendMessage(Main.prefix + "You are no longer movement-restricted!");
                    }
                }
            }
        }.runTaskLater(Main.getInstance(), 200);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : affectedPlayers) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.getFreezeTicks() > 0) {
                        player.setFreezeTicks(0);
                        player.sendMessage(Main.prefix + "Your freeze effect has fully worn off!");
                    }
                }
            }
        }.runTaskLater(Main.getInstance(), 600);
    }

    private static void iceDash(Player attacker) {
        Vector direction = attacker.getLocation().getDirection().normalize(); // Get player's facing direction and normalize it
        Vector moveVector = direction.multiply(0.6 * 2.5); // Moves about 5 blocks forward
        moveVector.setY(0.5 * 1); // Moves about 3 blocks up

        attacker.setVelocity(moveVector);
    }

    private static void poseidonsRush(Player attacker) {
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 200, 0, true, false));
    }

    private static void waterKB(Player attacker, double radius) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(attacker) && target.getWorld().equals(attacker.getWorld()) && target.getLocation().distance(attacker.getLocation()) <= radius) {
                Vector knockback = target.getLocation().toVector()
                        .subtract(attacker.getLocation().toVector())
                        .normalize()
                        .multiply(2.5)
                        .setY(1);
                target.setVelocity(knockback);
            }
        }

    }

    public static void trueInvisibility(Player attacker) {
        if (!TrueInvisibilityListener.isHidden(attacker)) {
            TrueInvisibilityListener.hidePlayer(attacker);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (TrueInvisibilityListener.isHidden(attacker)) {
                    TrueInvisibilityListener.showPlayer(attacker);
                    attacker.sendMessage(Main.prefix + "Your true invisibility has fully worn off!");
                }
            }
        }.runTaskLater(Main.getInstance(), 100);
    }

    private static void shadowSpeed(Player attacker, double radius) {
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 2, true, false));
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(attacker) && target.getWorld().equals(attacker.getWorld()) && target.getLocation().distance(attacker.getLocation()) <= radius) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 0, true, false));
                target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 1.0f, 1.0f);
            }
        }
    }

    private static void attackSpeed(Player attacker, double boostAmount) {
        if (!AttackSpeedHandler.isAttackSpeedBoosted(attacker)) {
            AttackSpeedHandler.applyAttackSpeedBoost(attacker, boostAmount);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                AttackSpeedHandler.removeAttackSpeedBoost(attacker);
                attacker.sendMessage(Main.prefix + "Your attack speed boost has worn off!");
            }
        }.runTaskLater(Main.getInstance(), 200);
    }

    private static void hulkJump(Player attacker) {

        Vector velocity = new Vector(0, 50, 0);
        attacker.setVelocity(velocity);

        if (!FallDamageListener.hasNoFallDamage(attacker)) {
            FallDamageListener.addNoFallDamage(attacker);
        }

        // Check when the player lands
        new BukkitRunnable() {
            @Override
            public void run() {
                Location belowPlayer = attacker.getLocation().clone().subtract(0, 1, 0);
                Block blockBelow = belowPlayer.getBlock();

                if (blockBelow.getType() != Material.AIR) {

                    attacker.setFallDistance(0);

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        FallDamageListener.removeNoFallDamage(attacker);
                    }, 40L);
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 5L, 2L);
    }

    private static void juggernautResistance(Player attacker) {
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 2, true, false));
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 2, true, false));
    }

    private static void allieHeal(Player attacker) {
        double radius = 5;
        int points = 80;
        Location center = attacker.getLocation().add(0, 1, 0);

        new BukkitRunnable() {
            int tick = 0;

            @Override
            public void run() {
                if (tick >= 60) {
                    cancel();
                    return;
                }

                for (int i = 0; i < points; i++) {
                    double angle = (2 * Math.PI / points) * i;
                    double x = center.getX() + (radius * Math.cos(angle));
                    double z = center.getZ() + (radius * Math.sin(angle));

                    Location particleLoc = new Location(center.getWorld(), x, center.getY(), z);
                    attacker.getWorld().spawnParticle(Particle.DRAGON_BREATH, particleLoc, 0);

                }
                tick++;
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(attacker) && target.getWorld().equals(attacker.getWorld()) && target.getLocation().distance(attacker.getLocation()) <= radius) {
                target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 9, true, false, false));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 9, true, false, false));
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 1.0f);
            }
        }

        attacker.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 9, true, false, false));
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 9, true, false, false));
        attacker.getWorld().playSound(attacker.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 1.0f);
    }

    private static void solarStrength(Player attacker) {
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 400, 2, true, false));
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 400, 2, true, false));
    }

    private static void sunsRays(Player attacker) {
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 4, true, false));
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 9, true, false, false));
    }

    private static void windCrush(Player attacker) {
        Location eyeLoc = attacker.getLocation();
        Vector direction = eyeLoc.getDirection().normalize();
        List<Entity> entitiesInFront = new ArrayList<>();

        for (double i = 1; i <= 5; i++) {
            Location checkLoc = eyeLoc.clone().add(direction.clone().multiply(i));
            for (Entity entity : attacker.getWorld().getNearbyEntities(checkLoc, 1, 1, 1)) {
                if (!entity.equals(attacker) && entity instanceof Player && !entitiesInFront.contains(entity)) {
                    entitiesInFront.add(entity);
                }
            }
        }

        for (Entity entity : entitiesInFront) {
            entity.setVelocity(new Vector(0, 1.7, 0));
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> entity.setVelocity(new Vector(0, -50, 0)), 20L);

            if (!FallDamageListener.hasNoFallDamage((Player) entity)) {
                FallDamageListener.addNoFallDamage((Player) entity);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    Location belowPlayer = entity.getLocation().clone().subtract(0, 1, 0);
                    Block blockBelow = belowPlayer.getBlock();

                    if (blockBelow.getType() != Material.AIR) {

                        entity.setFallDistance(0);
                        double fixedDamage = 12.0;
                        if (((Player) entity).getHealth() <= fixedDamage) {
                            ((Player) entity).setHealth(0);
                            ((Player) entity).damage(0, attacker);
                        } else {
                            ((Player) entity).setHealth(((Player) entity).getHealth() - fixedDamage);
                        }

                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                            FallDamageListener.removeNoFallDamage((Player) entity);
                        }, 40L);
                        this.cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 5L, 2L);
        }
    }

    private static void tornadoCharge(Player attacker) {
        WindChargeManager.spawnWindCharge(attacker);
    }
}
