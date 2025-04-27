package com.smp.items;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SmeltersPickaxe {
    public static ItemStack createItem() {
        ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = pickaxe.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§fꜱᴍᴇʟᴛᴇʀꜱ ᴘɪᴄᴋᴀxᴇ");
            meta.setLore(Arrays.asList(
                    "§7ᴀɴ ᴇꜱꜱᴇɴᴛɪᴀʟ ᴘɪᴄᴋᴀxᴇ ᴛʜᴀᴛ ꜱᴍᴇʟᴛꜱ ᴏʀᴇꜱ",
                    "§7ᴀᴜᴛᴏᴍᴀᴛɪᴄᴀʟʟʏ.",
                    "",
                    "§fᴄᴏᴍᴍᴏɴ",
                    "§fᴀʙɪʟɪᴛɪᴇꜱ:",
                    "§fᴀᴜᴛᴏ ꜱᴍᴇʟᴛ: §7ᴡʜᴇɴ ʏᴏᴜ ʙʀᴇᴀᴋ ᴀɴ ᴏʀᴇ, ɪᴛ ɢɪᴠᴇꜱ ʏᴏᴜ ᴛʜᴇ ꜱᴍᴇʟᴛᴇᴅ ɪᴛᴇᴍ."
            ));
            meta.setCustomModelData(2504);
            pickaxe.setItemMeta(meta);
        }
        return pickaxe;
    }

    public static ShapedRecipe getRecipe() {
        ItemStack pickaxe = createItem();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("smelters_pickaxe"), pickaxe);
        recipe.shape("III", "CSC", " S ");
        recipe.setIngredient('I', Material.RAW_IRON);
        recipe.setIngredient('C', Material.COAL);
        recipe.setIngredient('S', Material.STICK);
        return recipe;
    }
}