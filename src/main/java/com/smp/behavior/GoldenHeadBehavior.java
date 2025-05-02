package com.smp.behavior;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GoldenHeadBehavior implements Listener {

    private final Set<UUID> activeUsers = new HashSet<>();

    @EventHandler
    public void onPlayerUseGoldenHead(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the item is a Golden Head
        if (item != null && item.getType() == Material.PLAYER_HEAD && item.hasItemMeta() &&
                (ChatColor.GOLD + "ɢᴏʟᴅᴇɴ ʜᴇᴀᴅ").equals(item.getItemMeta().getDisplayName())) {

            // Prevent usage if the player is already using a Golden Head
            if (activeUsers.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are already using a Golden Head!");
                event.setCancelled(true);
                return;
            }

            // Consume the item and apply effects
            item.setAmount(item.getAmount() - 1);
            activeUsers.add(player.getUniqueId());
            int regenDuration = 45; // in seconds
            int absorptionDuration = 120; // in seconds

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, regenDuration * 20, 1)); // 45 seconds of Regeneration II
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, absorptionDuration * 20, 0)); // 2 minutes of Absorption I

            // Start a repeating task to update the action bar
            new BukkitRunnable() {
                double timeLeft = regenDuration; // Start with the regeneration duration

                @Override
                public void run() {
                    if (timeLeft <= 0 || !activeUsers.contains(player.getUniqueId())) {
                        this.cancel();
                        return;
                    }

                    // Send action bar message
                    player.sendActionBar(ChatColor.GOLD + "" + ChatColor.BOLD + "Golden Head: " + String.format("%.1fs", timeLeft));
                    timeLeft -= 0.1; // Decrease time left by 0.1 seconds
                }
            }.runTaskTimerAsynchronously(player.getServer().getPluginManager().getPlugin("Smp"), 0L, 2L); // Update every 2 ticks (0.1 seconds)

            // Schedule removal from active users after the effect duration
            player.getServer().getScheduler().runTaskLaterAsynchronously(
                    player.getServer().getPluginManager().getPlugin("Smp"),
                    () -> activeUsers.remove(player.getUniqueId()),
                    regenDuration * 20L // 45 seconds
            );
        }

        // Prevent usage of other Player Heads while a Golden Head is active
        else if (item != null && item.getType() == Material.PLAYER_HEAD) {
            if (!activeUsers.isEmpty()) {
                player.sendMessage(ChatColor.RED + "You cannot use Player Heads while a Golden Head is active!");
                event.setCancelled(true);
            }
        }
    }
}