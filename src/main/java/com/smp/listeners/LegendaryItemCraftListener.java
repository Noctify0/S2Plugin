package com.smp.listeners;

import com.smp.Smp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LegendaryItemCraftListener implements Listener {

    private final Smp plugin;

    public LegendaryItemCraftListener(Smp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        FileConfiguration config = plugin.getConfig();
        ItemStack result = event.getInventory().getResult();

        if (result == null) return;

        // Check if the crafted item is one of the legendary items
        if (isLegendaryItem(result, "strength_gauntlet") && config.getBoolean("strength_gauntlet_crafted")) {
            event.getInventory().setResult(null);
        } else if (isLegendaryItem(result, "excalibur") && config.getBoolean("excalibur_crafted")) {
            event.getInventory().setResult(null);
        } else if (isLegendaryItem(result, "dragon_katana") && config.getBoolean("dragon_katana_crafted")) {
            event.getInventory().setResult(null);
        } else if (isLegendaryItem(result, "heart_blade") && config.getBoolean("heart_blade_crafted")) {
            event.getInventory().setResult(null);
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        FileConfiguration config = plugin.getConfig();
        ItemStack result = event.getCurrentItem();

        if (result == null) return;

        Player player = (Player) event.getWhoClicked();

        // Check and handle crafting of each legendary item
        if (isLegendaryItem(result, "strength_gauntlet") && !config.getBoolean("strength_gauntlet_crafted")) {
            handleLegendaryCraft(player, "strength_gauntlet", "Strength Gauntlet");
        } else if (isLegendaryItem(result, "excalibur") && !config.getBoolean("excalibur_crafted")) {
            handleLegendaryCraft(player, "excalibur", "Excalibur");
        } else if (isLegendaryItem(result, "dragon_katana") && !config.getBoolean("dragon_katana_crafted")) {
            handleLegendaryCraft(player, "dragon_katana", "Dragon Katana");
        } else if (isLegendaryItem(result, "heart_blade") && !config.getBoolean("heart_blade_crafted")) {
            handleLegendaryCraft(player, "heart_blade", "Heart Blade");
        }
    }

    private boolean isLegendaryItem(ItemStack item, String itemName) {
        // Check if the item matches the custom item based on its type and custom model data
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        switch (itemName) {
            case "strength_gauntlet":
                return item.getType() == Material.DIAMOND_SWORD && meta.getCustomModelData() == 2556;
            case "excalibur":
                return item.getType() == Material.NETHERITE_SWORD && meta.getCustomModelData() == 333;
            case "dragon_katana":
                return item.getType() == Material.NETHERITE_SWORD && meta.getCustomModelData() == 3456;
            case "heart_blade":
                return item.getType() == Material.NETHERITE_SWORD && meta.getCustomModelData() == 5545;
            default:
                return false;
        }
    }

    private void handleLegendaryCraft(Player player, String configKey, String itemName) {
        // Update the config
        FileConfiguration config = plugin.getConfig();
        config.set(configKey + "_crafted", true);
        plugin.saveConfig();

        // Broadcast message to the server
        Bukkit.broadcastMessage(ChatColor.GOLD + "The " + itemName + " has been crafted by " + player.getName() +
                "! This legendary can not be crafted again!");

        // Play sound effect to all players
        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.8f));
    }
}