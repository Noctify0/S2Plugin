package com.smp.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Pistol {
    public static ItemStack createItem() {
        ItemStack pistol = new ItemStack(Material.FLINT);
        ItemMeta meta = pistol.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Pistol");
            meta.setCustomModelData(6);
            meta.setLore(Collections.singletonList(ChatColor.GRAY + "A simple firearm with a 10-bullet magazine."));
            pistol.setItemMeta(meta);
        }
        return pistol;
    }

    public static ShapedRecipe getRecipe(NamespacedKey key) {
        ItemStack pistol = createItem();
        ShapedRecipe recipe = new ShapedRecipe(key, pistol);
        recipe.shape(" I ", "IFI", " I ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('F', Material.FLINT);
        return recipe;
    }
}