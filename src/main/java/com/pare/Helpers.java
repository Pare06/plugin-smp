package com.pare;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Helpers {
    public static ShapedRecipe heartRecipe;
    public static ItemStack heartItem;

    public static ShapedRecipe appleRecipe;
    public static void registerCraftingRecipes() {
        heartItem = new ItemStack(Material.APPLE);
        ItemMeta meta = heartItem.getItemMeta();
        meta.setDisplayName("Cuore");
        String[] lore = { "Aggiunge un cuore permanente alla tua salute massima", "/withdrawheart per riprendere il cuore come item"};
        meta.setLore(Arrays.asList(lore));
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        heartItem.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "apple");
        heartRecipe = new ShapedRecipe(key, heartItem);

        heartRecipe.shape("nnn", "sac", "nnn");
        heartRecipe.setIngredient('n', Material.NETHERITE_INGOT);
        heartRecipe.setIngredient('s', Material.NETHER_STAR);
        heartRecipe.setIngredient('a', Material.ENCHANTED_GOLDEN_APPLE);
        heartRecipe.setIngredient('c', Material.SCULK_CATALYST);

        Bukkit.addRecipe(heartRecipe);

        NamespacedKey key2 = new NamespacedKey(Main.plugin, "enchanted_golden_apple");
        appleRecipe = new ShapedRecipe(key2, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
        appleRecipe.shape("gdg", "ata", "geg");
        appleRecipe.setIngredient('g', Material.GOLD_BLOCK);
        appleRecipe.setIngredient('d', Material.DIAMOND_BLOCK);
        appleRecipe.setIngredient('a', Material.GOLDEN_APPLE);
        appleRecipe.setIngredient('t', Material.TOTEM_OF_UNDYING);
        appleRecipe.setIngredient('e', Material.ENCHANTING_TABLE);
        Bukkit.addRecipe(appleRecipe);
    }
}
