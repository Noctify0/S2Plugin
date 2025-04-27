package com.smp.items;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class DragonKatana {
    public static ItemStack createItem() {
        ItemStack dragonKatana = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = dragonKatana.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§dᴅʀᴀɢᴏɴ ᴋᴀᴛᴀɴᴀ");
            meta.setLore(Arrays.asList(
                    "§7ᴀ ʟᴇɢᴇɴᴅᴀʀʏ ᴡᴇᴀᴘᴏɴ ᴛʜᴀᴛ ʜᴀʀɴᴇꜱꜱᴇꜱ ᴛʜᴇ",
                    "§7ꜱᴡɪꜰᴛɴᴇꜱꜱ ᴏꜰ ᴀ ɢʀᴇᴀᴛ ᴅʀᴀɢᴏɴ.",
                    "",
                    "§6ʟᴇɢᴇɴᴅᴀʀʏ",
                    "§fᴀʙɪʟɪᴛɪᴇꜱ:",
                    "§fꜱᴡɪꜰᴛɴᴇꜱꜱ: §7ᴛʜᴇ ᴡᴇɪʟᴅᴇʀ ɢᴇᴛꜱ ᴜɴʟɪᴍɪᴛᴇᴅ ꜱᴘᴇᴇᴅ ɪ",
                    "§fᴛᴇʟᴇᴘᴏʀᴛ: §7ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ ᴛᴇʟᴇᴘᴏʀᴛ ɪɴ ᴛʜᴇ",
                    "§7ᴅɪʀᴇᴄᴛɪᴏɴ ʏᴏᴜ ᴀʀᴇ ʟᴏᴏᴋɪɴɢ.",
                    "§830ꜱ ᴄᴏᴏʟᴅᴏᴡɴ"
            ));
            meta.setCustomModelData(3456);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            dragonKatana.setItemMeta(meta);
        }
        return dragonKatana;
    }

    public static ShapedRecipe getRecipe() {
        ItemStack dragonKatana = createItem();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("dragon_katana"), dragonKatana);
        recipe.shape(" E ", "NSN", " D ");
        recipe.setIngredient('E', Material.ENDER_PEARL);
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.NETHERITE_SWORD);
        recipe.setIngredient('D', Material.DRAGON_EGG);
        return recipe;
    }
}