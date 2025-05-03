package com.smp.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Bullet {
    public static ItemStack createItem() {
        ItemStack bullet = new ItemStack(Material.IRON_NUGGET, 5); // Set amount to 5
        ItemMeta meta = bullet.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GRAY + "Bullet");
            meta.setLore(Arrays.asList(
                    ChatColor.DARK_GRAY + "Ammunition for the Pistol.",
                    ChatColor.DARK_GRAY + "Crafted with precision.",
                    ChatColor.DARK_GRAY + "Handle with care."
            ));
            bullet.setItemMeta(meta);
        }
        return bullet;
    }

    public static ShapedRecipe getRecipe(NamespacedKey key) {
        ItemStack bullet = createItem();
        ShapedRecipe recipe = new ShapedRecipe(key, bullet);
        recipe.shape(" I ", " G ", " C ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('G', Material.GUNPOWDER);
        recipe.setIngredient('C', Material.COPPER_INGOT);
        return recipe;
    }
}