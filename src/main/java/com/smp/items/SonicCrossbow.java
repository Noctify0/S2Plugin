package com.smp.items;

import com.smp.utils.OneTimeCraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SonicCrossbow {

    public static ItemStack createItem() {
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        ItemMeta meta = crossbow.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "ꜱᴏɴɪᴄ ᴄʀᴏꜱꜱʙᴏᴡ");
            meta.setUnbreakable(true);
            meta.setCustomModelData(566);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "ᴀ ʟᴇɢᴇɴᴅᴀʀʏ ᴄʀᴏꜱꜱʙᴏᴡ ᴏʀɪɢɪɴᴀᴛɪɴɢ ꜰʀᴏᴍ",
                    ChatColor.GRAY + "ᴛʜᴇ ᴅᴇᴇᴘ ᴅᴀʀᴋ",
                    "",
                    ChatColor.GOLD + "ʟᴇɢᴇɴᴅᴀʀʏ",
                    ChatColor.WHITE + "ᴀʙɪʟɪᴛɪᴇꜱ:",
                    ChatColor.WHITE + "ꜱʜᴏᴏᴛ" + ChatColor.GRAY + " ᴛᴏ ʟᴀᴜɴᴄʜ ᴀ ꜱᴏɴɪᴄ ᴀʀʀᴏᴡ, ᴄʀᴇᴀᴛɪɴɢ",
                    ChatColor.GRAY + "ᴀ ʟᴀʀɢᴇ ꜱᴄᴜʟᴋ ᴇxᴘʟᴏꜱɪᴏɴ ᴏɴ ɪᴍᴘᴀᴄᴛ ᴛʜᴀᴛ",
                    ChatColor.GRAY + "ᴋɴᴏᴄᴋꜱ ᴘʟᴀʏᴇʀꜱ ᴀᴡᴀʏ.",
                    ChatColor.DARK_GRAY + "20ꜱ ᴄᴏᴏʟᴅᴏᴡɴ",
                    ""
            ));
            crossbow.setItemMeta(meta);
        }

        return crossbow;
    }

    public static ShapedRecipe getRecipe() {
        ItemStack crossbow = createItem();
        NamespacedKey key = new NamespacedKey(Bukkit.getPluginManager().getPlugin("Smp"), "sonic_crossbow");

        ShapedRecipe recipe = new ShapedRecipe(key, crossbow);
        recipe.shape("BTB", "TCT", "SDS");
        recipe.setIngredient('B', Material.DIAMOND_BLOCK);
        recipe.setIngredient('T', Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
        recipe.setIngredient('C', Material.CROSSBOW);
        recipe.setIngredient('D', Material.MUSIC_DISC_5);
        recipe.setIngredient('S', Material.ECHO_SHARD);

        OneTimeCraftUtils.markAsOneTimeCraft("sonic_crossbow", Material.CROSSBOW, 566);

        return recipe;
    }
}