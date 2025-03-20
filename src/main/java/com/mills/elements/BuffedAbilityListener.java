package com.mills.elements;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class BuffedAbilityListener {

    public static boolean fireElement(Player player) {
        Location playerLoc = player.getLocation();
        Biome biome = playerLoc.getBlock().getBiome();
        if (biome == Biome.NETHER_WASTES) return true;
        if (biome == Biome.WARPED_FOREST) return true;
        if (biome == Biome.CRIMSON_FOREST) return true;
        if (biome == Biome.BASALT_DELTAS) return true;
        if (biome == Biome.SOUL_SAND_VALLEY) return true;
        return false;
    }

    public static boolean waterElement(Player player) {
        Location playerLoc = player.getLocation();
        Biome biome = playerLoc.getBlock().getBiome();
        if (biome == Biome.OCEAN) return true;
        if (biome == Biome.DEEP_OCEAN) return true;
        if (biome == Biome.FROZEN_OCEAN) return true;
        if (biome == Biome.DEEP_FROZEN_OCEAN) return true;
        if (biome == Biome.COLD_OCEAN) return true;
        if (biome == Biome.DEEP_COLD_OCEAN) return true;
        if (biome == Biome.LUKEWARM_OCEAN) return true;
        if (biome == Biome.DEEP_LUKEWARM_OCEAN) return true;
        if (biome == Biome.WARM_OCEAN) return true;
        return false;
    }

    public static boolean iceElement(Player player) {
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
    public static boolean natureElement(Player player) {
        Location playerLoc = player.getLocation();
        Biome biome = playerLoc.getBlock().getBiome();
        if (biome == Biome.FLOWER_FOREST) return true;
        if (biome == Biome.CHERRY_GROVE) return true;
        return false;
    }

    public static boolean windElement(Player player) {
        Location playerLoc = player.getLocation();
        Biome biome = playerLoc.getBlock().getBiome();
        if (biome == Biome.MEADOW) return true;
        if (biome == Biome.GROVE) return true;
        if (biome == Biome.SNOWY_SLOPES) return true;
        if (biome == Biome.FROZEN_PEAKS) return true;
        if (biome == Biome.JAGGED_PEAKS) return true;
        if (biome == Biome.STONY_PEAKS) return true;
        return false;
    }

    public static boolean shadowElement(Player player) {
        Location playerLoc = player.getLocation();
        Biome biome = playerLoc.getBlock().getBiome();
        if (biome == Biome.THE_END) return true;
        if (biome == Biome.END_HIGHLANDS) return true;
        if (biome == Biome.END_MIDLANDS) return true;
        if (biome == Biome.SMALL_END_ISLANDS) return true;
        if (biome == Biome.END_BARRENS) return true;
        return false;
    }

    public static boolean isDaytime(Player player) {
        long time = player.getWorld().getTime();
        if (player.getWorld().getName().equals("world")) {
            if (time >= 0 && time < 12300) {
                return true;
            }
        }
        return false;
    }

}
