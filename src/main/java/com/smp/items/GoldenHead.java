package com.smp.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class GoldenHead {
    public static ItemStack createItem() {
        ItemStack goldenHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) goldenHead.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer("Gaelzila"));
            meta.setDisplayName(ChatColor.GOLD + "ɢᴏʟᴅᴇɴ ʜᴇᴀᴅ");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "ᴀ ᴍʏꜱᴛᴇʀɪᴏᴜꜱ ʜᴇᴀᴅ ꜰɪʟʟᴇᴅ ᴡɪᴛʜ ɢᴏʟᴅ",
                    "",
                    ChatColor.YELLOW + "ᴍʏᴛʜɪᴄ",
                    ChatColor.WHITE + "ᴀʙɪʟɪᴛɪᴇꜱ:",
                    ChatColor.WHITE + "ɢʀᴀɴᴅ ʜᴇᴀʟ: " + ChatColor.GRAY + "ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ ᴄᴏɴꜱᴜᴍᴇ.",
                    ChatColor.GRAY + "ɢɪᴠᴇꜱ ʏᴏᴜ ᴛʜᴇ ᴇꜰꜰᴇᴄᴛꜱ ᴏꜰ ᴀ ɢᴏʟᴅᴇɴ ᴀᴘᴘʟᴇ ꜰᴏʀ 45 ꜱᴇᴄᴏɴᴅꜱ.",
                    ChatColor.DARK_GRAY + "1 ᴛɪᴍᴇ ᴜꜱᴇ",
                    ""
            ));
            goldenHead.setItemMeta(meta);
        }
        return goldenHead;
    }

    public static ShapedRecipe getRecipe(ItemStack customPlayerHead) {
        ItemStack goldenHead = createItem();
        NamespacedKey key = NamespacedKey.minecraft("golden_head");
        ShapedRecipe recipe = new ShapedRecipe(key, goldenHead);
        recipe.shape("GGG", "GSG", "GGG");
        recipe.setIngredient('G', Material.GOLD_INGOT);

        // Check if the customPlayerHead has custom model data before using it
        ItemMeta meta = customPlayerHead.getItemMeta();
        if (meta != null && meta.hasCustomModelData()) {
            recipe.setIngredient('S', customPlayerHead.getType(), meta.getCustomModelData());
        } else {
            recipe.setIngredient('S', customPlayerHead.getType());
        }

        return recipe;
    }
}