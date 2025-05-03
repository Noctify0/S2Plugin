package com.smp.behavior;

import com.smp.Smp;
import com.smp.utils.CustomItemUtils;
import com.smp.utils.ProjectileUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PistolBehavior implements Listener {
    private final HashMap<UUID, Integer> ammo = new HashMap<>();
    private final int maxAmmo = 10;
    private final int cooldownTicks = 7;
    private final HashMap<UUID, Long> lastShotTime = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Use CustomItemUtils to check if the item is the custom Pistol
        if (!CustomItemUtils.isCustomItem(item, Material.FLINT, "&6Pistol", 6)) {
            return;
        }

        UUID playerId = player.getUniqueId();
        ammo.putIfAbsent(playerId, 0);

        if (event.getAction().toString().contains("RIGHT_CLICK")) {
            shoot(player);
        } else if (event.getAction().toString().contains("LEFT_CLICK")) {
            reload(player);
        }
    }

    private void shoot(Player player) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastShot = lastShotTime.getOrDefault(playerId, 0L);

        if (currentTime - lastShot < cooldownTicks * 50) {
            player.sendMessage(ChatColor.RED + "You must wait before shooting again!");
            return;
        }

        int currentAmmo = ammo.get(playerId);
        if (currentAmmo <= 0) {
            player.sendMessage(ChatColor.RED + "Out of ammo! Reload your Pistol.");
            return;
        }

        lastShotTime.put(playerId, currentTime);
        ammo.put(playerId, currentAmmo - 1);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Ammo: " + ammo.get(playerId) + "/" + maxAmmo));

        // Use ProjectileUtils to create a custom bullet
        ProjectileUtils.createCustomProjectile(
                Smp.getInstance(), // Plugin instance
                player,            // Shooter
                Particle.FLAME,    // Trail particle
                5,                 // Despawn time in seconds
                false,             // Is on fire
                false,             // Makes explosion
                0,                 // Explosion strength
                false,             // Sculk effect
                5.0,               // Speed of the bullet
                4.0,               // Damage
                false              // Use speed-based damage
        );

        // Play sound for shooting
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
    }

    private void reload(Player player) {
        UUID playerId = player.getUniqueId();
        int currentAmmo = ammo.get(playerId);

        if (currentAmmo == maxAmmo) {
            player.sendMessage(ChatColor.GREEN + "Pistol is already fully loaded.");
            return;
        }

        int bulletsInInventory = countBullets(player);
        int bulletsNeeded = maxAmmo - currentAmmo;

        if (bulletsInInventory == 0) {
            player.sendMessage(ChatColor.RED + "No bullets in your inventory to reload.");
            return;
        }

        int bulletsToReload = Math.min(bulletsNeeded, bulletsInInventory);
        removeBullets(player, bulletsToReload);
        ammo.put(playerId, currentAmmo + bulletsToReload);

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Ammo: " + ammo.get(playerId) + "/" + maxAmmo));
        player.sendMessage(ChatColor.GREEN + "Reloading...");

        lastShotTime.put(playerId, System.currentTimeMillis() + 2000);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(ChatColor.GREEN + "Pistol reloaded.");
            }
        }.runTaskLater(Smp.getInstance(), 40L);
    }

    private int countBullets(Player player) {
        return player.getInventory().all(Material.IRON_NUGGET).values().stream()
                .filter(item -> item.getItemMeta() != null && item.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Bullet"))
                .mapToInt(ItemStack::getAmount)
                .sum();
    }

    private void removeBullets(Player player, int amount) {
        for (ItemStack item : player.getInventory().all(Material.IRON_NUGGET).values()) {
            if (item.getItemMeta() != null && item.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Bullet")) {
                int stackSize = item.getAmount();
                if (stackSize <= amount) {
                    player.getInventory().remove(item);
                    amount -= stackSize;
                } else {
                    item.setAmount(stackSize - amount);
                    break;
                }
            }
        }
    }
}