package com.smp.behavior;

import com.smp.utils.CooldownUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.UUID;

public class HeartBladeBehavior implements Listener {
    private final JavaPlugin plugin;
    private final int dashCooldownTime = 10; // 10 seconds

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

        // Check cooldown
        if (CooldownUtils.isOnCooldown(playerId, "heart_blade_dash")) {
            double timeLeft = CooldownUtils.getRemainingCooldown(playerId, "heart_blade_dash");
            CooldownUtils.sendCooldownMessage(player, "Heart Blade Dash", timeLeft);
            return;
        }

        // Perform the dash
        Vector direction = player.getLocation().getDirection().normalize().multiply(2); // Dash distance
        player.setVelocity(direction);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);

        // Set cooldown
        CooldownUtils.setCooldown(playerId, "heart_blade_dash", dashCooldownTime);
    }

    private boolean isHeartBlade(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_SWORD) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 5545;
    }
}