package com.smp.items;

import com.smp.utils.OneTimeCraftUtils;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ShrinkRay {

    public static ItemStack createItem() {
        ItemStack shrinkRay = new ItemStack(Material.PAPER); // Changed to PAPER
        ItemMeta meta = shrinkRay.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6ꜱʜʀɪɴᴋ ʀᴀʏ");
            meta.setLore(Arrays.asList(
                    "§7ᴀ ᴍʏꜱᴛᴇʀɪᴏᴜꜱ ᴡᴇᴀᴘᴏɴ ᴛʜᴀᴛ",
                    "§7ᴄᴀɴ ᴍᴀɴɪᴘᴜʟᴀᴛᴇ ꜱɪᴢᴇ.",
                    "",
                    "§6ʟᴇɢᴇɴᴅᴀʀʏ",
                    "§fᴀʙɪʟɪᴛɪᴇꜱ:",
                    "§fꜱʜʀɪɴᴋ: §7ꜱɴᴇᴀᴋ + ʟᴇꜰᴛ ᴄʟɪᴄᴋ ᴛᴏ",
                    "§7ʙᴇᴄᴏᴍᴇ ᴀ ʙʟᴏᴄᴋ ᴛᴀʟʟ ᴡɪᴛʜ ꜱᴘᴇᴇᴅ ɪɪ.",
                    "§845ꜱ ᴄᴏᴏʟᴅᴏᴡɴ",
                    "§fʙᴇᴀᴍ: §7ꜱɴᴇᴀᴋ + ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ",
                    "§7ꜰɪʀᴇ ᴀ ʙᴇᴀᴍ ᴛʜᴀᴛ",
                    "§7ᴇxᴘᴀɴᴅꜱ ᴇɴᴇᴍɪᴇꜱ ᴀɴᴅ ɢɪᴠᴇꜱ ꜱʟᴏᴡɴᴇꜱꜱ.",
                    "§830ꜱ ᴄᴏᴏʟᴅᴏᴡɴ",
                    ""
            ));
            meta.setCustomModelData(266);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            shrinkRay.setItemMeta(meta);
        }
        return shrinkRay;
    }

    public static ShapedRecipe getRecipe() {
        ItemStack shrinkRay = createItem();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("shrink_ray"), shrinkRay);
        recipe.shape("IBR", "SDT", "HBC");
        recipe.setIngredient('I', Material.NETHERITE_INGOT);
        recipe.setIngredient('B', Material.REDSTONE_BLOCK);
        recipe.setIngredient('R', Material.REPEATER);
        recipe.setIngredient('C', Material.COMPARATOR);
        recipe.setIngredient('H', Material.NETHERITE_HOE);
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('S', Material.NETHER_STAR);
        recipe.setIngredient('T', Material.REDSTONE_TORCH);

        OneTimeCraftUtils.markAsOneTimeCraft("shrink_ray", Material.PAPER, 266);

        return recipe;
    }
}