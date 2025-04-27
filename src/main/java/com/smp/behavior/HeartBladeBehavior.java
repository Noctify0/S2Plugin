package com.smp.behavior;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartBladeBehavior implements Listener {
    private final Map<UUID, Long> dashCooldown = new HashMap<>();
    private final long dashCooldownTime = 10 * 1000; // 10 seconds in milliseconds
    private final JavaPlugin plugin;

    public HeartBladeBehavior(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDash(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack weapon = player.getInventory().getItemInMainHand();

        // Check if the item is the Heart Blade
        if (!isHeartBlade(weapon)) {
            return;
        }

        // Check if the action is a right-click
        if (!event.getAction().toString().contains("RIGHT_CLICK")) {
            return;
        }

        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Check cooldown
        if (dashCooldown.containsKey(playerId)) {
            long timeLeft = (dashCooldownTime - (currentTime - dashCooldown.get(playerId))) / 1000; // Convert to seconds
            if (timeLeft > 0) {
                player.sendMessage(ChatColor.RED + "Dash is on cooldown! Time left: " + timeLeft + " seconds.");
                return;
            }
        }

        // Perform the dash
        Vector direction = player.getLocation().getDirection().normalize().multiply(2); // Dash distance
        player.setVelocity(direction);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);

        // Add cooldown
        dashCooldown.put(playerId, currentTime);
    }

    private boolean isHeartBlade(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_SWORD) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 5545;
    }
}