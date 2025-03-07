package com.mills.elements;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RegisterItems {

    public static void registerElementalItems() {
        ItemManager.registerItem("Fire Element", Items.fire(),
                player -> player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, PotionEffect.INFINITE_DURATION, 0, true, false)),
                null, null, 0,
                null, null, 0
        );
    }
}
