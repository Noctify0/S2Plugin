package com.smp.behavior;

import com.smp.utils.ProjectileUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class SonicCrossbowBehavior implements Listener {

    private final Plugin plugin;

    public SonicCrossbowBehavior(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack item = event.getBow();
        if (item == null || item.getType() != Material.CROSSBOW) return;

        // Check if the crossbow is the Sonic Crossbow
        if (!item.hasItemMeta() || item.getItemMeta() == null ||
                !item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "ꜱᴏɴɪᴄ ᴄʀᴏꜱꜱʙᴏᴡ")) return;

        UUID playerId = player.getUniqueId();

        // Check cooldown
        if (com.smp.utils.CooldownUtils.isOnCooldown(playerId, "sonic_crossbow")) {
            double timeLeft = com.smp.utils.CooldownUtils.getRemainingCooldown(playerId, "sonic_crossbow");
            com.smp.utils.CooldownUtils.sendCooldownMessage(player, "Sonic Crossbow", timeLeft);
            event.setCancelled(true);
            return;
        }

        // Set cooldown for 20 seconds
        com.smp.utils.CooldownUtils.setCooldown(playerId, "sonic_crossbow", 20);

        // Create custom projectile
        ProjectileUtils.createCustomProjectile(
                plugin,
                player,
                Particle.SONIC_BOOM,
                3,
                false,
                true,
                3.0F,
                true,
                2.0,
                5.0,
                false // Use speed-based damage
        );

        event.setCancelled(true);
    }
}