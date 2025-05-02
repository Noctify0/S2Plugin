package com.smp.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Bullet {
    public static ItemStack createItem() {
        ItemStack bullet = new ItemStack(Material.IRON_NUGGET);
        ItemMeta meta = bullet.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GRAY + "Bullet");
            meta.setLore(Collections.singletonList(ChatColor.DARK_GRAY + "Ammunition for the Pistol."));
            bullet.setItemMeta(meta);
        }
        return bullet;
    }

    public static ShapedRecipe getRecipe(NamespacedKey key) {
        ItemStack bullet = createItem();
        ShapedRecipe recipe = new ShapedRecipe(key, bullet);
        recipe.shape(" I ", " I ", " I ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        return recipe;
    }
}