package com.smp.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Pistol {
    private static final int MAX_MAGAZINE_CAPACITY = 10;

    public static ItemStack createItem() {
        ItemStack pistol = new ItemStack(Material.FLINT);
        updateLore(pistol, MAX_MAGAZINE_CAPACITY); // Initialize with full magazine
        return pistol;
    }

    public static void updateLore(ItemStack pistol, int shotsLeft) {
        if (pistol == null || pistol.getType() != Material.FLINT) return;

        ItemMeta meta = pistol.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "ɢʟᴏᴄᴋ 19");
            meta.setCustomModelData(6);
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "ᴀɴ ᴀᴍᴇʀɪᴄᴀɴ ꜰɪʀᴇᴀʀᴍ ᴡɪᴛʜ ᴀ ",
                    ChatColor.GRAY + "10-ʙᴜʟʟᴇᴛ ᴍᴀɢᴀᴢɪɴᴇ.",
                    "",
                    ChatColor.YELLOW + "Magazine: " + shotsLeft + "/" + MAX_MAGAZINE_CAPACITY,
                    "",
                    ChatColor.GREEN + "ᴜɴᴄᴏᴍᴍᴏɴ",
                    ChatColor.WHITE + "ꜰᴜʟʟ ᴀᴜᴛᴏ" + ChatColor.GRAY + "ʜᴏʟᴅ ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ ꜱʜᴏᴏᴛ ʀᴀᴘɪᴅʟʏ",
                    ChatColor.WHITE + "ꜱʜᴏᴏᴛ" + ChatColor.GRAY + "ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ ꜰɪʀᴇ ᴀ ꜱᴍᴀʟʟ ʙᴜʟʟᴇᴛ",
                    ChatColor.DARK_GRAY + "1/3 ꜱᴇᴄᴏɴᴅ ᴄᴏᴏʟᴅᴏᴡɴ"
            ));
            pistol.setItemMeta(meta);
        }
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