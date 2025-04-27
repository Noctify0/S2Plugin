package com.smp.behavior;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class SmeltersPickaxeBehavior implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals("§fꜱᴍᴇʟᴛᴇʀꜱ ᴘɪᴄᴋᴀxᴇ")) {
            return;
        }

        Block block = event.getBlock();
        Material blockType = block.getType();
        ItemStack smeltedDrop = null;

        switch (blockType) {
            case IRON_ORE -> smeltedDrop = new ItemStack(Material.IRON_INGOT);
            case GOLD_ORE -> smeltedDrop = new ItemStack(Material.GOLD_INGOT);
            case SAND -> smeltedDrop = new ItemStack(Material.GLASS);
            case COPPER_ORE -> {
                int amount = new Random().nextInt(3) + 2; // Random between 2 and 4
                smeltedDrop = new ItemStack(Material.COPPER_INGOT, amount);
            }
            case RAW_COPPER_BLOCK -> smeltedDrop = new ItemStack(Material.COPPER_BLOCK);
            case RAW_IRON_BLOCK -> smeltedDrop = new ItemStack(Material.IRON_BLOCK);
            case RAW_GOLD_BLOCK -> smeltedDrop = new ItemStack(Material.GOLD_BLOCK);
            default -> {
                return; // Do nothing for other blocks
            }
        }

        event.setDropItems(false); // Prevent default drops
        block.getWorld().dropItemNaturally(block.getLocation(), smeltedDrop);
        block.getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 0.5, 0.5), 10, 0.2, 0.2, 0.2, 0.01);
    }
}
