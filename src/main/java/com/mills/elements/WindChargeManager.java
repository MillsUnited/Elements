package com.mills.elements;

import com.mills.elements.Teams.TeamManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class WindChargeManager implements Listener {

    private Main main;
    private TeamManager teamManager;

    public WindChargeManager(Main main) {
        this.main = main;
        this.teamManager = main.getTeamManager();
    }

    public static void spawnWindCharge(Player attacker) {
        World world = attacker.getWorld();
        Location loc = attacker.getEyeLocation();
        Vector direction = loc.getDirection().normalize().multiply(1.5);

        WindCharge firstCharge = world.spawn(loc.add(direction), WindCharge.class);
        firstCharge.setVelocity(direction);
        firstCharge.setShooter(attacker);
        firstCharge.setMetadata("wind_charge_1", new FixedMetadataValue(Main.getInstance(), true));
    }

    // Event listener to check when the first Wind Charge hits a player
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof WindCharge) {
            WindCharge hitCharge = (WindCharge) event.getEntity();

            if (hitCharge.hasMetadata("wind_charge_1") && event.getHitEntity() instanceof Player) {
                Player target = (Player) event.getHitEntity();
                Player attacker = (Player) hitCharge.getShooter();
                if (attacker != null) {
                    if (target.hasPermission(Main.adminPerm)) {
                        return;
                    }
                        if (!teamManager.isInSameTeam(attacker.getUniqueId(), target.getUniqueId())) {

                            target.getWorld().strikeLightningEffect(target.getLocation());

                            double damageSetter = 4.0;
                            if (target.getHealth() <= damageSetter) {
                                target.setHealth(0);
                                target.damage(0, attacker);
                            } else {
                                target.setHealth(target.getHealth() - damageSetter);
                            }

                            spawnTrackingWindCharges(attacker, target);
                        } else {
                            attacker.sendMessage(Main.prefix + target.getName() + " is part of your team!");
                        }
                }
            }
        }
    }

    // Spawns Wind Charges 2-5 with increasing speed
    private void spawnTrackingWindCharges(Player attacker, Player target) {
        new BukkitRunnable() {
            int count = 0;
            double baseSpeed = 1.5; // Start speed
            double speedIncrease = 1.0; // Each Wind Charge will be faster by this amount

            @Override
            public void run() {
                boolean inWind = BuffedAbilityListener.windElement(attacker);
                double windChargesAmount = inWind ? 6 : 4;
                if (count >= windChargesAmount) {
                    this.cancel();
                    return;
                }

                World world = attacker.getWorld();
                Location spawnLoc = attacker.getEyeLocation().add(attacker.getEyeLocation().getDirection().multiply(1.5));
                WindCharge trackingCharge = world.spawn(spawnLoc, WindCharge.class);

                trackingCharge.setShooter(attacker);
                trackingCharge.setVelocity(getTrackingVector(spawnLoc, target, baseSpeed + (count * speedIncrease))); // Increase speed each time
                target.getWorld().strikeLightningEffect(target.getLocation());
                target.playSound(target.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.0F);

                double damageSetter = 2.0;
                if (target.getHealth() <= damageSetter) {
                    target.setHealth(0);
                    target.damage(0, attacker);
                } else {
                    target.setHealth(target.getHealth() - damageSetter);
                }

                count++;
            }
        }.runTaskTimer(Main.getInstance(), 10L, 10L); // Fires every 10 ticks (0.5s)
    }

    // Calculates a velocity vector from attacker → target with increasing speed
    private Vector getTrackingVector(Location from, Player target, double speed) {
        Location targetLoc = target.getLocation().add(0, 1, 0);
        Vector direction = targetLoc.toVector().subtract(from.toVector());

        // Prevents division by zero
        if (direction.lengthSquared() == 0) {
            return new Vector(0, 0, 0);
        }

        return direction.normalize().multiply(speed);
    }
}
