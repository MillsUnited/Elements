package com.mills.elements;

import com.mills.elements.Teams.TeamManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class RegisterItems {

    private Main main;
    private TeamManager teamManager;

    public RegisterItems(Main main) {
        this.main = main;
        this.teamManager = main.getTeamManager();
    }

    public void registerElementalItems() {
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

                new RightClickAbility("Hulk Smash", player -> {

                    player.sendMessage(Main.prefix + "You have activated Hulk Smash!");
                    hulkSmash(player, 5);

                }, 120000, Items.earth())
        );
        ItemManager.registerItem("Nature Element", Items.nature(),
                new LeftClickAbility("Nature’s Resistance", player -> {

                    player.sendMessage(Main.prefix + "You have activated Nature’s Resistance!");
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
                    windCrush(player, 5.0);

                }, 120000, Items.wind()),

                new RightClickAbility("Tornado Charge", player -> {

                    player.sendMessage(Main.prefix + "You have activated Tornado Charge!");
                    tornadoCharge(player);

                }, 120000, Items.wind())
        );
    }

    private void fireBlastAbility(Player attacker) {
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

                                if (target.hasPermission(Main.adminPerm)) {
                                    return;
                                }

                                    if (!main.getTeamManager().isInSameTeam(attacker.getUniqueId(), target.getUniqueId())) {
                                        boolean inNetherBiome = BuffedAbilityListener.fireElement(attacker);
                                        double damageSetter = inNetherBiome ? 0.5 : 0.25;
                                        int fireDuration = 100;
                                        target.setFireTicks(fireDuration);

                                        if (target.getHealth() + target.getAbsorptionAmount() <= damageSetter) {
                                            target.damage(75, attacker);
                                        } else {
                                            if (target.getAbsorptionAmount() > 0) {
                                                double absorption = target.getAbsorptionAmount();

                                                if (damageSetter <= absorption) {
                                                    target.setAbsorptionAmount(absorption - damageSetter);
                                                    damageSetter = 0;
                                                } else {
                                                    target.setAbsorptionAmount(0);
                                                    damageSetter -= absorption;
                                                }
                                            }

                                            target.setHealth(target.getHealth() - damageSetter);
                                        }

                                        Vector kbDirection = target.getLocation().toVector().subtract(attacker.getLocation().toVector()).normalize();

                                        double horizontalStrength = 0.8; // Lower horizontal impact
                                        double verticalBoost = 0.35; // Stronger upward motion

                                        Vector kb = kbDirection.multiply(horizontalStrength);
                                        kb.setY(verticalBoost); // Set strong upward force

                                        target.setVelocity(kb);
                                        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                                        attacker.playSound(attacker.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);
                                    } else {
                                        attacker.sendMessage(Main.prefix + target.getName() + " is part of your team!");
                                    }
                            }
                        }
                    }
                }
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
        SpiralEffect.startSpiralEffect(attacker, 252, 208, 92);
        attacker.playSound(attacker.getLocation(),Sound.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
    }


    private void volcanicEruption(Player attacker) {
        Location originalLocation = attacker.getLocation();

        boolean inNetherBiome = BuffedAbilityListener.fireElement(attacker);
        long velocitySetter = inNetherBiome ? 50 : 35;

        SpiralEffect.startSpiralEffect(attacker, 252, 137, 92);
        Vector velocity = new Vector(0, velocitySetter, 0);
        attacker.setVelocity(velocity);

        if (!FallDamageListener.hasNoFallDamage(attacker)) {
            FallDamageListener.addNoFallDamage(attacker);
        }

        // place lava after player launches
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            Location loc = originalLocation.clone();
            loc.setY(loc.getY());
            Block block = loc.getBlock();

            if (block.getType() == Material.AIR || block.getType() == Material.WATER || block.getType() == Material.SNOW) {
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
                    attacker.playSound(attacker.getLocation(),Sound.ITEM_MACE_SMASH_GROUND, 1.0F, 1.0F);

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        FallDamageListener.removeNoFallDamage(attacker);
                    }, 40L);
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 5L, 2L);
    }

    private void playerFreeze(Player attacker, double radius) {
        Set<UUID> affectedPlayers = new HashSet<>();

        boolean inIceBiome = BuffedAbilityListener.iceElement(attacker);

        long unfreezeDelay = inIceBiome ? 300 : 200;
        long freezeEffectEndDelay = inIceBiome ? 600 : 400;

        SpiralEffect.startSpiralEffect(attacker, 10, 186, 255);
        attacker.playSound(attacker.getLocation(),Sound.BLOCK_CONDUIT_DEACTIVATE, 1.0F, 1.0F);

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(attacker) && target.getWorld().equals(attacker.getWorld()) && target.getLocation().distance(attacker.getLocation()) <= radius) {
                if (target.hasPermission(Main.adminPerm)) {
                    return;
                }
                    if (!teamManager.isInSameTeam(attacker.getUniqueId(), target.getUniqueId())) {
                        if (!PlayerMovementListener.isFrozen(target)) {
                            PlayerMovementListener.addFrozenPlayer(target);
                            target.sendMessage(Main.prefix + "You have been frozen by " + attacker.getName() + " for 5 seconds!");
                        }
                        target.playSound(target.getLocation(),Sound.BLOCK_CONDUIT_DEACTIVATE, 1.0F, 1.0F);
                        affectedPlayers.add(target.getUniqueId());
                        target.sendMessage(Main.prefix + "You have been given frozen effect by " + attacker.getName() + " for 30 seconds!");
                        target.setFreezeTicks(Integer.MAX_VALUE);
                    } else {
                        attacker.sendMessage(Main.prefix + target.getName() + " is part of your team!");
                    }
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!attacker.equals(player)) {
                        if (teamManager.isInSameTeam(attacker.getUniqueId(), player.getUniqueId())) {
                            cancel();
                            return;
                        }

                        if (PlayerMovementListener.isFrozen(player)) {
                            PlayerMovementListener.removeFrozenPlayer(player);
                            player.sendMessage(Main.prefix + "You are no longer movement-restricted!");
                        }
                    }
                }
            }
        }.runTaskLater(Main.getInstance(), unfreezeDelay);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : affectedPlayers) {

                    if (teamManager.isInSameTeam(attacker.getUniqueId(), uuid)) {
                        cancel();
                        return;
                    }

                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && player.getFreezeTicks() > 0) {
                        player.setFreezeTicks(0);
                        player.sendMessage(Main.prefix + "Your freeze effect has fully worn off!");
                    }
                }
            }
        }.runTaskLater(Main.getInstance(), freezeEffectEndDelay);
    }

    private void iceDash(Player attacker) {
        boolean inIceBiome = BuffedAbilityListener.iceElement(attacker);
        double forwardVelocity = inIceBiome ? 4.0 : 2.5;
        double upVelocity = inIceBiome ? 3.0 : 1.0;

        Vector direction = attacker.getLocation().getDirection().normalize(); // Get player's facing direction and normalize it
        Vector moveVector = direction.multiply(0.6 * forwardVelocity); // Moves about 5 blocks forward
        moveVector.setY(0.5 * upVelocity); // Moves about 3 blocks up

        attacker.setVelocity(moveVector);
        SpiralEffect.startSpiralEffect(attacker, 84, 109, 255);
        attacker.playSound(attacker.getLocation(),Sound.ENTITY_BREEZE_IDLE_AIR, 1.0F, 1.0F);
    }

    private void poseidonsRush(Player attacker) {
        boolean inWaterBiome = BuffedAbilityListener.waterElement(attacker);
        int duration = inWaterBiome ? 400 : 200;

        attacker.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, duration, 0, true, false));
        SpiralEffect.startSpiralEffect(attacker, 92, 200, 252);
        attacker.playSound(attacker.getLocation(),Sound.BLOCK_CONDUIT_ACTIVATE, 1.0F, 1.0F);
    }

    private void waterKB(Player attacker, double radius) {
        SpiralEffect.startSpiralEffect(attacker, 92, 252, 220);
        attacker.playSound(attacker.getLocation(),Sound.BLOCK_CONDUIT_ATTACK_TARGET, 1.0F, 1.0F);
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(attacker) && target.getWorld().equals(attacker.getWorld()) && target.getLocation().distance(attacker.getLocation()) <= radius) {
                if (target.hasPermission(Main.adminPerm)) {
                    return;
                }
                    if (!teamManager.isInSameTeam(attacker.getUniqueId(), target.getUniqueId())) {
                        boolean inWaterBiome = BuffedAbilityListener.waterElement(attacker);
                        double forwardVelocity = inWaterBiome ? 6 : 2.5;
                        double upVelocity = inWaterBiome ? 3 : 1;

                        Vector knockback = target.getLocation().toVector()
                                .subtract(attacker.getLocation().toVector())
                                .normalize()
                                .multiply(forwardVelocity)
                                .setY(upVelocity);
                        target.setVelocity(knockback);
                    } else {
                        attacker.sendMessage(Main.prefix + target.getName() + " is part of your team!");
                    }
            }
        }

    }

    private void trueInvisibility(Player attacker) {
        SpiralEffect.startSpiralEffect(attacker, 51, 13, 96);
        attacker.playSound(attacker.getLocation(),Sound.ENTITY_WARDEN_DEATH, 1.0F, 1.0F);
        if (!TrueInvisibilityListener.isHidden(attacker)) {
            TrueInvisibilityListener.hidePlayer(attacker);
        }
        boolean inEndBiome = BuffedAbilityListener.shadowElement(attacker);
        long delay = inEndBiome ? 200 : 100;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (TrueInvisibilityListener.isHidden(attacker)) {
                    TrueInvisibilityListener.showPlayer(attacker);
                    attacker.sendMessage(Main.prefix + "Your true invisibility has fully worn off!");
                }
            }
        }.runTaskLater(Main.getInstance(), delay);
    }

    private void shadowSpeed(Player attacker, double radius) {
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 2, true, false));
        SpiralEffect.startSpiralEffect(attacker, 252, 137, 92);
        attacker.playSound(attacker.getLocation(),Sound.ENTITY_WARDEN_ROAR, 1.0F, 1.0F);
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(attacker) && target.getWorld().equals(attacker.getWorld()) && target.getLocation().distance(attacker.getLocation()) <= radius) {
                if (target.hasPermission(Main.adminPerm)) {
                    return;
                }
                    if (!teamManager.isInSameTeam(attacker.getUniqueId(), target.getUniqueId())) {
                        boolean inEndBiome = BuffedAbilityListener.shadowElement(attacker);
                        int duration = inEndBiome ? 400 : 200;

                        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 0, true, false));
                        target.playSound(target.getLocation(), Sound.ENTITY_WARDEN_HEARTBEAT, 1.0f, 1.0f);
                    } else {
                        attacker.sendMessage(Main.prefix + target.getName() + " is part of your team!");
                    }
            }
        }
    }

    private void attackSpeed(Player attacker, double boostAmount) {
        SpiralEffect.startSpiralEffect(attacker, 130, 131, 24);
        attacker.playSound(attacker.getLocation(),Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
        if (!AttackSpeedHandler.isAttackSpeedBoosted(attacker)) {
            AttackSpeedHandler.applyAttackSpeedBoost(attacker, boostAmount);
        }

        boolean inEarthBiome = BuffedAbilityListener.earthElement(attacker);
        long duration = inEarthBiome ? 400 : 200;

        new BukkitRunnable() {
            @Override
            public void run() {
                AttackSpeedHandler.removeAttackSpeedBoost(attacker);
                attacker.sendMessage(Main.prefix + "Your attack speed boost has worn off!");
            }
        }.runTaskLater(Main.getInstance(), duration);
    }

    private void hulkSmash(Player attacker, double radius) {

        Location attackerLoc = attacker.getLocation();
        List<Player> entities = new ArrayList<>();
        SpiralEffect.startSpiralEffect(attacker, 131, 102, 24);
        attacker.playSound(attacker.getLocation(),Sound.ITEM_MACE_SMASH_GROUND, 1.0F, 1.0F);

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(attacker) && target.getWorld().equals(attacker.getWorld()) && target.getLocation().distance(attacker.getLocation()) <= radius) {
                if (target.hasPermission(Main.adminPerm)) {
                    return;
                }
                    if (!teamManager.isInSameTeam(attacker.getUniqueId(), target.getUniqueId())) {
                        entities.add(target);
                    } else {
                        attacker.sendMessage(Main.prefix + target.getName() + " is part of your team!");
                    }
            }
        }

        for (Player target : entities) {
            if (target.hasPermission(Main.adminPerm)) {
                return;
            }
                if (!teamManager.isInSameTeam(attacker.getUniqueId(), target.getUniqueId())) {
                    Location targetLoc = target.getLocation();
                    Vector direction = targetLoc.toVector().subtract(attackerLoc.toVector());
                    direction.normalize();
                    direction.setY(10);
                    direction.multiply(5);
                    target.setVelocity(direction);

                    if (!FallDamageListener.hasNoFallDamage(target)) {
                        FallDamageListener.addNoFallDamage(target);
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Location belowPlayer = target.getLocation().clone().subtract(0, 1, 0);
                            Block blockBelow = belowPlayer.getBlock();

                            if (blockBelow.getType() != Material.AIR) {
                                Location targetNewLoc = target.getLocation();
                                target.setFallDistance(0);
                                target.playSound(targetNewLoc, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 1.0f, 1.0f);
                                boolean inEarthBiome = BuffedAbilityListener.earthElement(attacker);
                                double damage = inEarthBiome ? 16.0 : 12.0;
                                target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0F, 1.0F);

                                if (target.getHealth() + target.getAbsorptionAmount() <= damage) {
                                    target.damage(75, attacker);
                                } else {
                                    if (target.getAbsorptionAmount() > 0) {
                                        double absorption = target.getAbsorptionAmount();

                                        if (damage <= absorption) {
                                            target.setAbsorptionAmount(absorption - damage);
                                            damage = 0;
                                        } else {
                                            target.setAbsorptionAmount(0);
                                            damage -= absorption;
                                        }
                                    }

                                    target.setHealth(target.getHealth() - damage);
                                }

                                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                                    FallDamageListener.removeNoFallDamage(target);
                                }, 40L);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Main.getInstance(), 5L, 2L);
                }
        }
    }

    private void juggernautResistance(Player attacker) {
        boolean inNatureBiome = BuffedAbilityListener.natureElement(attacker);
        int resistanceDuration = inNatureBiome ? 300 : 200;

        SpiralEffect.startSpiralEffect(attacker, 76, 176, 103);
        attacker.playSound(attacker.getLocation(),Sound.BLOCK_CONDUIT_AMBIENT, 1.0F, 1.0F);

        attacker.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, resistanceDuration, 2, true, false));
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 2, true, false));
    }

    private void allieHeal(Player attacker) {
        double radius = 5;
        int points = 80;
        Location center = attacker.getLocation().add(0, 1, 0);

        SpiralEffect.startSpiralEffect(attacker, 88, 131, 38);

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
                if (target.hasPermission(Main.adminPerm)) {
                    return;
                }
                    if (teamManager.isInSameTeam(attacker.getUniqueId(), target.getUniqueId())) {
                        boolean inNatureBiome = BuffedAbilityListener.natureElement(attacker);
                        if (inNatureBiome) {
                            target.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 1, true, false, true));
                        }

                        target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 9, true, false, false));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 9, true, false, false));
                        target.playSound(target.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);
                    }
            }
        }
        boolean inNatureBiome = BuffedAbilityListener.natureElement(attacker);
        if (inNatureBiome) {
            attacker.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 0, true, false, true));
        }
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 9, true, false, false));
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 9, true, false, false));
        attacker.getWorld().playSound(attacker.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);
    }

    private void solarStrength(Player attacker) {
        boolean inSun = BuffedAbilityListener.isDaytime(attacker);
        int duration = inSun ? 500 : 400;

        attacker.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, duration, 2, true, false));
        SpiralEffect.startSpiralEffect(attacker, 252, 208, 92);
        attacker.playSound(attacker.getLocation(),Sound.BLOCK_BEACON_POWER_SELECT, 1.0F, 1.0F);
    }

    private void sunsRays(Player attacker) {
        boolean inSun = BuffedAbilityListener.isDaytime(attacker);
        int amplifier = inSun ? 5 : 4;

        attacker.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, amplifier, true, false));
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 9, true, false, false));
        SpiralEffect.startSpiralEffect(attacker, 252, 137, 92);
        attacker.playSound(attacker.getLocation(),Sound.BLOCK_CONDUIT_ACTIVATE, 1.0F, 1.0F);
    }

    private void windCrush(Player attacker, double radius) {
        List<Player> entities = new ArrayList<>();

        SpiralEffect.startSpiralEffect(attacker, 165, 165, 165);
        attacker.playSound(attacker.getLocation(), Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 1.0F, 1.0F);

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!target.equals(attacker) && target.getWorld().equals(attacker.getWorld()) && target.getLocation().distance(attacker.getLocation()) <= radius) {
                if (target.hasPermission(Main.adminPerm)) {
                    return;
                }
                    if (!teamManager.isInSameTeam(attacker.getUniqueId(), target.getUniqueId())) {
                        entities.add(target);
                    } else {
                        attacker.sendMessage(Main.prefix + target.getName() + " is part of your team!");
                    }
            }
        }

        // Do your ability logic for each valid entity
        for (Player target : entities) {
            target.setVelocity(new Vector(0, 1.7, 0));
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> target.setVelocity(new Vector(0, -50, 0)), 20L);

            if (!FallDamageListener.hasNoFallDamage(target)) {
                FallDamageListener.addNoFallDamage(target);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    Location belowPlayer = target.getLocation().clone().subtract(0, 1, 0);
                    Block blockBelow = belowPlayer.getBlock();

                    if (blockBelow.getType() != Material.AIR) {
                        target.setFallDistance(0);
                        boolean inWind = BuffedAbilityListener.windElement(attacker);
                        double damage = inWind ? 14.0 : 12.0;
                        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0F, 1.0F);

                        if (target.getHealth() + target.getAbsorptionAmount() <= damage) {
                            target.damage(75, attacker);
                        } else {
                            if (target.getAbsorptionAmount() > 0) {
                                double absorption = target.getAbsorptionAmount();

                                if (damage <= absorption) {
                                    target.setAbsorptionAmount(absorption - damage);
                                    damage = 0;
                                } else {
                                    target.setAbsorptionAmount(0);
                                    damage -= absorption;
                                }
                            }

                            target.setHealth(target.getHealth() - damage);
                        }

                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                            FallDamageListener.removeNoFallDamage(target);
                        }, 40L);
                        this.cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 5L, 2L);
        }
    }

    private void tornadoCharge(Player attacker) {
        SpiralEffect.startSpiralEffect(attacker, 227, 227, 227);
        attacker.playSound(attacker.getLocation(),Sound.ENTITY_BREEZE_IDLE_GROUND, 1.0F, 1.0F);
        WindChargeManager.spawnWindCharge(attacker);
    }
}