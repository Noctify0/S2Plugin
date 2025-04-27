package com.smp.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PlayerHead {
    public static ItemStack createItem() {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = playerHead.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "ᴘʟᴀʏᴇʀ ʜᴇᴀᴅ");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "A mysterious head filled with power.", // Description
                    "",
                    ChatColor.YELLOW + "ᴍʏᴛʜɪᴄ",
                    ChatColor.WHITE + "ᴀʙɪʟɪᴛɪᴇꜱ:",
                    ChatColor.WHITE + "ɢʀᴀɴᴅ ʜᴇᴀʟ: " + ChatColor.GRAY + "ʀɪɢʜᴛ ᴄʟɪᴄᴋ ᴛᴏ ᴄᴏɴꜱᴜᴍᴇ.",
                    ChatColor.GRAY + "ɢɪᴠᴇꜱ ʏᴏᴜ ᴛʜᴇ ᴇꜰꜰᴇᴄᴛꜱ ᴏꜰ ᴀ ɢᴏʟᴅᴇɴ ᴀᴘᴘʟᴇ ꜰᴏʀ",
                    ChatColor.GRAY + "𝟣𝟧 ꜱᴇᴄᴏɴᴅꜱ.",
                    ChatColor.DARK_GRAY + "1 ᴛɪᴍᴇ ᴜꜱᴇ"
            ));
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            playerHead.setItemMeta(meta);
        }
        return playerHead;
    }
}