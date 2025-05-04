package com.smp.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ProjectileUtils implements Listener {

    private static Plugin plugin;

    public static void initialize(Plugin pluginInstance) {
        plugin = pluginInstance;
        Bukkit.getPluginManager().registerEvents(new ProjectileUtils(), plugin);
    }

    public static void createCustomProjectile(
            Plugin plugin,
            Player shooter,
            Particle trailParticle,
            int particleCount,
            boolean gravity,
            boolean glowing,
            int knockbackStrength,
            boolean silent,
            double speed,
            double damage,
            boolean speedBasedDamage,
            EntityType projectileType,
            boolean useModelEngine,
            String modelId,
            int despawnTime
    ) {
        Location spawnLocation = shooter.getEyeLocation();
        Entity projectile = spawnLocation.getWorld().spawnEntity(spawnLocation, projectileType);
        projectile.setGravity(gravity);
        projectile.setGlowing(glowing);
        projectile.setSilent(silent);

        if (projectile instanceof Arrow arrow) {
            arrow.setCustomName("custom_projectile");
            arrow.setCustomNameVisible(false);
            arrow.setKnockbackStrength(knockbackStrength);

            if (!speedBasedDamage) {
                arrow.setMetadata("custom_damage", new FixedMetadataValue(plugin, damage));
            }
        }

        Vector direction = spawnLocation.getDirection().normalize().multiply(speed);
        projectile.setVelocity(direction);

        // Schedule particle trail
        new BukkitRunnable() {
            @Override
            public void run() {
                if (projectile.isDead() || !projectile.isValid()) {
                    cancel();
                    return;
                }

                projectile.getWorld().spawnParticle(trailParticle, projectile.getLocation(), particleCount, 0, 0, 0, 0);
            }
        }.runTaskTimer(plugin, 0L, 1L);

        // Schedule projectile despawn if despawnTime is greater than 0
        if (despawnTime > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!projectile.isDead() && projectile.isValid()) {
                        projectile.remove();
                    }
                }
            }.runTaskLater(plugin, despawnTime * 20L); // Convert seconds to ticks
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow arrow && "custom_projectile".equals(arrow.getCustomName())) {
            if (arrow.getShooter() instanceof Player shooter) {
                if (arrow.hasMetadata("custom_damage")) {
                    event.setCancelled(true); // Cancel vanilla damage
                    double customDamage = arrow.getMetadata("custom_damage").get(0).asDouble();
                    if (event.getEntity() instanceof LivingEntity target) {
                        target.damage(customDamage, shooter); // Apply custom damage
                    }
                }
            }
        }
    }
}