package com.smp.behavior;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerHeadBehavior implements Listener {

    private final JavaPlugin plugin;
    private final Set<UUID> activePlayerHeads = new HashSet<>(); // Track active effects

    public PlayerHeadBehavior(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.PLAYER_HEAD && item.getItemMeta() != null &&
                ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("ᴘʟᴀʏᴇʀ ʜᴇᴀᴅ")) {

            // Check if the player is already under the effect
            if (activePlayerHeads.contains(playerId)) {
                player.sendMessage(ChatColor.RED + "You cannot use another Player Head while the effect is active!");
                return;
            }

            // Consume the item
            item.setAmount(item.getAmount() - 1);

            // Apply golden apple effects for 15 seconds
            int duration = 15 * 20; // 15 seconds in ticks
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, duration, 1));

            // Add the player to the active set
            activePlayerHeads.add(playerId);

            // Display bold action bar message
            new BukkitRunnable() {
                double timeLeft = duration / 20.0; // Convert ticks to seconds

                @Override
                public void run() {
                    if (timeLeft <= 0) {
                        cancel();
                        activePlayerHeads.remove(playerId); // Remove the player when the effect ends
                        return;
                    }
                    player.sendActionBar(ChatColor.GOLD + "§lPlayer Head: " + String.format("%.1fs", timeLeft));
                    timeLeft -= 0.1; // Decrease time left by 0.1 seconds
                }
            }.runTaskTimer(plugin, 0, 2); // Run every 2 ticks (0.1 seconds)
        }
    }
}