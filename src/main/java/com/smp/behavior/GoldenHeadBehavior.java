package com.smp.behavior;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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

            // Prevent usage if another player is using a Golden Head
            if (!activeUsers.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Another player is already using a Golden Head!");
                event.setCancelled(true);
                return;
            }

            // Consume the item and apply effects
            item.setAmount(item.getAmount() - 1);
            activeUsers.add(player.getUniqueId());
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 45 * 20, 1)); // 15 seconds of Regeneration II
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 0)); // 2 minutes of Absorption I
            player.sendMessage(ChatColor.GOLD + "You consumed a Golden Head!");

            // Schedule removal from active users after the effect duration
            player.getServer().getScheduler().runTaskLaterAsynchronously(
                    player.getServer().getPluginManager().getPlugin("Smp"),
                    () -> activeUsers.remove(player.getUniqueId()),
                    15 * 20L // 15 seconds
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