package com.mills.elements;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpiralEffect {

    public static void startSpiralEffect(Player player, int red, int green, int blue) {
        new BukkitRunnable() {
            double y = 0;
            double radius = 0.6;
            double angle = 0;

            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(red, green, blue), 1.5F);

            int maxTicks = 20;
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= maxTicks || !player.isOnline()) {
                    cancel();
                    return;
                }

                Location loc = player.getLocation();

                double x = Math.cos(angle) * radius;
                double z = Math.sin(angle) * radius;

                Location particleLoc = loc.clone().add(x, y, z);
                player.getWorld().spawnParticle(Particle.DUST, particleLoc, 1, dustOptions);

                angle += Math.PI / 4;
                y += 0.1;
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }
}
