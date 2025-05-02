package com.smp.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ProjectileUtils {

    private static final Random random = new Random();

    public static void createCustomProjectile(Plugin plugin, Player shooter, Particle trail, int despawnTime,
                                              boolean isOnFire, boolean makesExplosion, float explosionStrength,
                                              boolean sculkEffect, double speed, double damage) {
        Arrow arrow = shooter.launchProjectile(Arrow.class);
        arrow.setMetadata("custom_projectile", new FixedMetadataValue(plugin, true));

        // Set the speed of the projectile
        arrow.setVelocity(shooter.getLocation().getDirection().multiply(speed));

        if (isOnFire) {
            arrow.setFireTicks(Integer.MAX_VALUE);
        }

        // Add particle trail
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
                    world.spawnParticle(trail, loc, 5, 0.2, 0.2, 0.2, 0.01);
                }
            }
        }.runTaskTimer(plugin, 0, 2);

        // Schedule despawn
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!arrow.isDead()) {
                    arrow.remove();
                }
            }
        }.runTaskLater(plugin, despawnTime * 20L);

        // Handle impact
        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onHit(org.bukkit.event.entity.ProjectileHitEvent event) {
                if (event.getEntity() != arrow) return;

                Location hitLocation = arrow.getLocation();
                World world = hitLocation.getWorld();
                if (world == null) return;

                if (makesExplosion) {
                    world.createExplosion(hitLocation, explosionStrength, false, true);
                }

                if (sculkEffect) {
                    int radius = 3;
                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (x * x + z * z <= radius * radius) {
                                Location blockLocation = hitLocation.clone().add(x, -1, z);
                                Block block = blockLocation.getBlock();
                                if (block.getType() != Material.AIR && isDestructible(block.getType()) && random.nextDouble() <= 0.6) {
                                    block.setType(Material.SCULK);
                                }
                            }
                        }
                    }
                }

                // Apply damage to entities
                if (event.getHitEntity() instanceof LivingEntity livingEntity) {
                    livingEntity.damage(damage, shooter);
                }

                arrow.remove();
                org.bukkit.event.HandlerList.unregisterAll(this);
            }
        }, plugin);
    }

    private static boolean isDestructible(Material material) {
        return switch (material) {
            case BEDROCK, OBSIDIAN, ENDER_CHEST, ANVIL, ENCHANTING_TABLE -> false;
            default -> true;
        };
    }
}