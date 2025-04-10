package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AntiFireAspectBook implements Listener {

    @EventHandler
    public void onPrepareItemEnchant(PrepareItemEnchantEvent e) {
        EnchantmentOffer[] offers = e.getOffers();

        for (int i = 0; i < offers.length; i++) {
            EnchantmentOffer offer = offers[i];
            if (offer != null && offer.getEnchantment() == Enchantment.FIRE_ASPECT) {
                offers[i] = null;
            }
        }
    }

    @EventHandler
    public void onItemEnchant(EnchantItemEvent e) {
        Map<Enchantment, Integer> enchants = e.getEnchantsToAdd();

        enchants.keySet().removeIf(enchant -> enchant == Enchantment.FIRE_ASPECT);
    }

    @EventHandler
    public void onVillagerChangeProfession(VillagerCareerChangeEvent event) {
        Villager villager = event.getEntity();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            List<MerchantRecipe> newRecipes = new ArrayList<>();

            for (MerchantRecipe recipe : villager.getRecipes()) {
                ItemStack result = recipe.getResult();
                if (result.getType() == Material.ENCHANTED_BOOK && result.getItemMeta() instanceof EnchantmentStorageMeta meta) {
                    if (meta.hasStoredEnchant(Enchantment.FIRE_ASPECT)) {
                        continue;
                    }
                }
                newRecipes.add(recipe);
            }

            villager.setRecipes(newRecipes);
        }, 1L);
    }

}
