package com.smp.items;

import com.smp.utils.OneTimeCraftUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Excalibur {

    public static ItemStack createItem() {

        ItemStack excalibur = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = excalibur.getItemMeta();
        if (meta != null) {

            meta.setDisplayName("§6ᴇxᴄᴀʟɪʙᴜʀ");

            meta.setLore(Arrays.asList(
                    "§7ᴀ ʟᴇɢᴇɴᴅᴀʀʏ ʙʟᴀᴅᴇ ᴛʜᴀᴛ ʜᴀʀɴᴇꜱꜱᴇꜱ ᴛʜᴇ",
                    "§7ᴘᴏᴡᴇʀ ᴏꜰ ᴛʜᴇ ɢᴏᴅꜱ ᴛᴏ ᴘʀᴏᴛᴇᴄᴛ ᴛʜᴇ ᴘʟᴀʏᴇʀ",
                    "§7ꜰʀᴏᴍ ɪɴᴄᴏᴍɪɴɢ ᴀᴛᴛᴀᴄᴋꜱ.",
                    "",
                    "§6ʟᴇɢᴇɴᴅᴀʀʏ",
                    "§fᴀʙɪʟɪᴛɪᴇꜱ:",
                    "§fɪɴᴠɪɴᴄɪʙɪʟɪᴛʏ: §7ʀɪɢʜᴛ ᴄʟɪᴄᴋ",
                    "§7ᴛᴏ ᴀᴄᴛɪᴠᴀᴛᴇ ᴀɴ ɪɴᴠɪɴᴄɪʙɪʟɪᴛʏ ᴘᴇʀɪᴏᴅ ꜰᴏʀ 10ꜱ.",
                    "§7ᴛʜᴇ ꜰɪʀꜱᴛ ᴛʜʀᴇᴇ ʜɪᴛꜱ ʏᴏᴜ ᴛᴀᴋᴇ ɪɴ ᴛʜɪꜱ",
                    "§7ᴘᴇʀɪᴏᴅ ᴡɪʟʟ ʙᴇ ᴄᴀɴᴄᴇʟʟᴇᴅ.",
                    "§845ꜱ ᴄᴏᴏʟᴅᴏᴡɴ",
                    ""
            ));

            meta.setCustomModelData(333);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            excalibur.setItemMeta(meta);
        }
        return excalibur;
    }

    public static ShapedRecipe getRecipe() {
        ItemStack excalibur = createItem();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("excalibur"), excalibur);
        recipe.shape(
                "RER",
                "BNB",
                "RCR"
        );
        recipe.setIngredient('R', Material.BLAZE_ROD);
        recipe.setIngredient('B', Material.NETHERITE_BLOCK);
        recipe.setIngredient('N', Material.NETHERITE_SWORD);
        recipe.setIngredient('E', Material.ENCHANTED_GOLDEN_APPLE);
        recipe.setIngredient('C', Material.BEACON);

        // Mark as a one-time craft
        OneTimeCraftUtils.markAsOneTimeCraft("excalibur", Material.NETHERITE_SWORD, 333);

        return recipe;
    }
}