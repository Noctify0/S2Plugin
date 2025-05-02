package com.smp.items;

import com.smp.utils.OneTimeCraftUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class StrengthGauntlet {
    public static ItemStack createItem() {
        ItemStack gauntlet = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = gauntlet.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§4ꜱᴛʀᴇɴɢᴛʜ ɢᴀᴜɴᴛʟᴇᴛ");
            meta.setLore(Arrays.asList(
                    "§7ᴀ ʟᴇɢᴇɴᴅᴀʀʏ ɢᴀᴜɴᴛʟᴇᴛ ᴛʜᴀᴛ ʜᴀʀɴᴇꜱꜱᴇꜱ ᴛʜᴇ ᴘᴏᴡᴇʀ ᴏꜰ ᴛʜᴇ",
                    "§7ᴇᴀʀᴛʜ ᴛᴏ ᴘʀᴏᴛᴇᴄᴛ ᴛʜᴇ ᴡᴇɪʟᴅᴇʀ ꜰʀᴏᴍ ɪɴꜱɪᴅɪᴏᴜꜱ ᴀᴛᴛᴀᴄᴋꜱ.",
                    "",
                    "§6ʟᴇɢᴇɴᴅᴀʀʏ",
                    "§fᴀʙɪʟɪᴛɪᴇꜱ:",
                    "§fᴇᴀʀᴛʜǫᴜᴀᴋᴇ: §7ꜱʜɪꜰᴛ + ʟᴇꜰᴛ ᴄʟɪᴄᴋ",
                    "§81ᴍ ᴄᴏᴏʟᴅᴏᴡɴ",
                    "§fʜᴏᴍᴇʀᴜɴ: §720% ᴄʜᴀɴᴄᴇ ᴛᴏ ᴋɴᴏᴄᴋ ᴀ ᴘʟᴀʏᴇʀ ᴜᴘ ɪɴ ᴛʜᴇ ᴀɪʀ",
                    "§fᴅᴀꜱʜ: §7ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ ᴅᴀꜱʜ ꜰᴏʀᴡᴀʀᴅ",
                    "§810ꜱ ᴄᴏᴏʟᴅᴏᴡɴ",
                    ""
            ));
            meta.setCustomModelData(2556);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.addEnchant(Enchantment.LOOTING, 5, true);
            meta.addEnchant(Enchantment.SHARPNESS, 3, true);
            gauntlet.setItemMeta(meta);
        }
        return gauntlet;
    }

    public static ShapedRecipe getRecipe() {
        ItemStack gauntlet = createItem();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("strength_gauntlets"), gauntlet);
        recipe.shape("WNT", "DSA", "TZW");
        recipe.setIngredient('W', Material.BREEZE_ROD);
        recipe.setIngredient('N', Material.NETHERITE_SWORD);
        recipe.setIngredient('T', Material.TNT);
        recipe.setIngredient('D', Material.DIAMOND_PICKAXE);
        recipe.setIngredient('A', Material.DIAMOND_AXE);
        recipe.setIngredient('Z', Material.NETHER_STAR);
        recipe.setIngredient('S', Material.SPAWNER);

        OneTimeCraftUtils.markAsOneTimeCraft("strength_gauntlets", Material.DIAMOND_SWORD, 2556);

        return recipe;
    }
}