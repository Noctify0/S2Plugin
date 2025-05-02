package com.smp.items;

import com.smp.utils.OneTimeCraftUtils;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class HeartBlade {
    public static ItemStack createItem() {
        ItemStack heartBlade = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = heartBlade.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§4ʜᴇᴀʀᴛ ʙʟᴀᴅᴇ");
            meta.setLore(Arrays.asList(
                    "§7ᴀ ʙʟᴀᴅᴇ ᴛʜᴀᴛ ꜱᴛᴇᴀʟꜱ ᴛʜᴇ ᴇꜱꜱᴇɴᴄᴇ ᴏꜰ ʟɪꜰᴇ.",
                    "",
                    "§6ʟᴇɢᴇɴᴅᴀʀʏ",
                    "§fᴀʙɪʟɪᴛɪᴇꜱ:",
                    "§fʜᴇᴀʀᴛ ꜱᴛᴇᴀʟ: §7ᴘᴇʀᴍᴀɴᴇɴᴛʟʏ ᴛᴀᴋᴇ ᴏɴᴇ ʜᴇᴀʀᴛ ꜰʀᴏᴍ ᴀ ᴘʟᴀʏᴇʀ.",
                    "§fʙʟᴇᴇᴅ: §720% ᴄʜᴀɴᴄᴇ ᴏꜰ ᴄᴀᴜꜱɪɴɢ ʙʟᴇᴇᴅɪɴɢ.",
                    "§fᴅᴀꜱʜ: §7ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ ᴅᴀꜱʜ ꜰᴏʀᴡᴀʀᴅ.",
                    ""
            ));
            meta.setCustomModelData(5545);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            heartBlade.setItemMeta(meta);
        }
        return heartBlade;
    }

    public static ShapedRecipe getRecipe(NamespacedKey key) {
        ItemStack defaultPlayerHead = new ItemStack(Material.PLAYER_HEAD);

        ItemStack heartBlade = createItem();
        ShapedRecipe recipe = new ShapedRecipe(key, heartBlade);
        recipe.shape("PRP", "ANA", "PRP");
        recipe.setIngredient('P', new RecipeChoice.ExactChoice(PlayerHead.createItem()));
        recipe.setIngredient('A', Material.GOLDEN_APPLE);
        recipe.setIngredient('N', Material.NETHERITE_SWORD);
        recipe.setIngredient('R', Material.RED_DYE);

        OneTimeCraftUtils.markAsOneTimeCraft("heart_blade", Material.NETHERITE_SWORD, 5545);

        return recipe;
    }
}