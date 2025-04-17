package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;
import java.util.Set;

public class CraftingRecipeHandler {

    public static void recipeChanger() {
        // === removal of Recipes ===
        Set<Material> contraband = Set.of(
                Material.GOLDEN_APPLE,
                Material.MACE
        );
        Iterator<Recipe> it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe recipe = it.next();
            if (recipe instanceof ShapedRecipe) {
                ItemStack result = recipe.getResult();
                if (contraband.contains(result.getType())) {
                    it.remove();
                }
            }
        }

        // === Golden Apple Recipe ===
        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE);
        NamespacedKey goldenAppleKey = new NamespacedKey(Main.getInstance(), "golden_apple_custom");
        ShapedRecipe goldenAppleRecipe = new ShapedRecipe(goldenAppleKey, goldenApple);
        goldenAppleRecipe.shape(
                " G ",
                "GAG",
                " G "
        );
        goldenAppleRecipe.setIngredient('G', Material.GOLD_INGOT);
        goldenAppleRecipe.setIngredient('A', Material.APPLE);
        Bukkit.addRecipe(goldenAppleRecipe);

        // === Enchanted Golden Apple Recipe ===
        ItemStack enchantedGoldenApple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        NamespacedKey enchantedGoldenAppleKey = new NamespacedKey(Main.getInstance(), "enchanted_golden_apple_custom");
        ShapedRecipe enchantedGoldenAppleRecipe = new ShapedRecipe(enchantedGoldenAppleKey, enchantedGoldenApple);
        enchantedGoldenAppleRecipe.shape(
                "BKB",
                "AAA",
                "BTB"
        );
        enchantedGoldenAppleRecipe.setIngredient('B', new RecipeChoice.ExactChoice(new ItemStack(Material.GOLD_BLOCK, 16)));
        enchantedGoldenAppleRecipe.setIngredient('A', Material.GOLDEN_APPLE);
        enchantedGoldenAppleRecipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
        enchantedGoldenAppleRecipe.setIngredient('K', Material.OMINOUS_TRIAL_KEY);
        Bukkit.addRecipe(enchantedGoldenAppleRecipe);

        // === Cobweb Recipe ===
        ItemStack cobweb = new ItemStack(Material.COBWEB);
        NamespacedKey cobwebKey = new NamespacedKey(Main.getInstance(), "cobweb_custom");
        ShapedRecipe cobwebRecipe = new ShapedRecipe(cobwebKey, cobweb);
        cobwebRecipe.shape(
                "SSS",
                "SSS",
                "SSS"
        );
        cobwebRecipe.setIngredient('S', Material.STRING);
        Bukkit.addRecipe(cobwebRecipe);

        // === Shulker Recipe ===
        ItemStack shulker = new ItemStack(Material.SHULKER_BOX);
        NamespacedKey shulkerKey = new NamespacedKey(Main.getInstance(), "shulker_custom");
        ShapedRecipe shulkerRecipe = new ShapedRecipe(shulkerKey, shulker);
        shulkerRecipe.shape(
                "AAA",
                "SBS",
                "CCC"
        );
        shulkerRecipe.setIngredient('A', Material.AMETHYST_BLOCK);
        shulkerRecipe.setIngredient('S', Material.MAGENTA_BUNDLE);
        shulkerRecipe.setIngredient('B', Material.ENDER_CHEST);
        shulkerRecipe.setIngredient('C', Material.END_CRYSTAL);
        Bukkit.addRecipe(shulkerRecipe);
    }

}
