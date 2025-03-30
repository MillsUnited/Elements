package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;

public class CraftingRecipeHandler {

    public static void recipeChanger() {
        // === removal default Golden Apple Recipe ===
        Iterator<Recipe> it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe oldGoldenApple = it.next();
            if (oldGoldenApple instanceof ShapedRecipe) {
                ItemStack result = oldGoldenApple.getResult();
                if (result.getType() == Material.GOLDEN_APPLE) {
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
                "SBS",
                "SSS"
        );
        cobwebRecipe.setIngredient('S', Material.STRING);
        cobwebRecipe.setIngredient('B', Material.SLIME_BALL);
        Bukkit.addRecipe(cobwebRecipe);
    }

}
