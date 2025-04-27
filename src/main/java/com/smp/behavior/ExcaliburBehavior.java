package com.smp.behavior;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExcaliburBehavior implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, Integer> activeHits = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!isExcalibur(item)) return;
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                break;
            default:
                return;
        }

        UUID playerId = player.getUniqueId();
        long now = System.currentTimeMillis();

        // Check cooldown
        if (cooldowns.containsKey(playerId)) {
            long timeLeft = (45000 - (now - cooldowns.get(playerId))) / 1000;
            if (timeLeft > 0) {
                player.sendMessage(ChatColor.RED + "Invincibility is on cooldown! Time left: " + timeLeft + " seconds.");
                return;
            }
        }

        // Activate invincibility
        cooldowns.put(playerId, now);
        activeHits.put(playerId, 3);
        player.sendMessage(ChatColor.AQUA + "Invincibility activated!");

        new BukkitRunnable() {
            double duration = 10.0; // 10 seconds

            @Override
            public void run() {
                if (duration <= 0 || !activeHits.containsKey(playerId)) {
                    activeHits.remove(playerId);
                    cancel();
                    return;
                }

                int hitsLeft = activeHits.get(playerId);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "Invincibility: " + hitsLeft + "🗡 | " + String.format("%.1f", duration) + "s left"));
                duration -= 0.1;
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Smp"), 0, 2);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        UUID playerId = player.getUniqueId();
        if (!activeHits.containsKey(playerId)) return;

        int hitsLeft = activeHits.get(playerId);
        if (hitsLeft > 0) {
            event.setCancelled(true);
            activeHits.put(playerId, hitsLeft - 1);

            if (hitsLeft - 1 <= 0) {
                activeHits.remove(playerId);
                player.sendMessage(ChatColor.RED + "Invincibility has ended!");
            }
        }
    }

    private boolean isExcalibur(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasCustomModelData() && meta.getCustomModelData() == 333;
    }
}