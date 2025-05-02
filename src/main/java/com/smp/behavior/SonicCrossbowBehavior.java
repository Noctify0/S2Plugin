package com.smp.behavior;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class SonicCrossbowBehavior implements Listener {

    private final Plugin plugin;
    private final Random random = new Random();

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

        // Replace the default arrow with a custom arrow
        if (event.getProjectile() instanceof Arrow arrow) {
            arrow.setMetadata("sonic_arrow", new FixedMetadataValue(plugin, true));

            // Add particle effects to the arrow while in the air
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (arrow.isDead() || !arrow.isValid() || arrow.isOnGround()) {
                        cancel();
                        return;
                    }

                    Location loc = arrow.getLocation();
                    World world = loc.getWorld();
                    if (world != null) {
                        world.spawnParticle(Particle.SONIC_BOOM, loc, 5, 0.2, 0.2, 0.2, 0.01);
                    }
                }
            }.runTaskTimer(plugin, 0, 2);
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;

        // Check if the arrow is a custom Sonic Arrow
        if (!arrow.hasMetadata("sonic_arrow")) return;

        Location hitLocation = arrow.getLocation();
        World world = hitLocation.getWorld();
        if (world == null) return;

        // Create a natural explosion with safeguards
        world.createExplosion(hitLocation, 3.0F, false, true);

        // Schedule the arrow to despawn after 3 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                arrow.remove();
            }
        }.runTaskLater(plugin, 60L); // 60 ticks = 3 seconds

        // Additional logic for sculk transformation and entity knockback
        Location sculkCenter = hitLocation;
        if (event.getHitEntity() != null) {
            sculkCenter = event.getHitEntity().getLocation();
        }

        int highestY = sculkCenter.getWorld().getHighestBlockYAt(sculkCenter);
        if (sculkCenter.getY() - highestY > 3 || highestY <= 0) {
            return;
        }

        int radius = 3;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    Location blockLocation = sculkCenter.clone().add(x, -1, z);
                    Block block = blockLocation.getBlock();
                    if (block.getType() != Material.AIR && isDestructible(block.getType()) && random.nextDouble() <= 0.6) {
                        block.setType(Material.SCULK);
                    }
                }
            }
        }

        for (Entity entity : world.getNearbyEntities(hitLocation, 4, 4, 4)) {
            if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player)) {
                livingEntity.setVelocity(livingEntity.getLocation().toVector()
                        .subtract(hitLocation.toVector()).normalize().multiply(1.5));
                livingEntity.damage(random.nextInt(5) + 2);
            }
        }
    }

    private boolean isDestructible(Material material) {
        return switch (material) {
            case BEDROCK, OBSIDIAN, ENDER_CHEST, ANVIL, ENCHANTING_TABLE -> false;
            default -> true;
        };
    }
}