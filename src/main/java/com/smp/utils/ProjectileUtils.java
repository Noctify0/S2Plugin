package com.smp.utils;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ProjectileUtils {

    public static void createCustomProjectile(
            Plugin plugin,
            Player shooter,
            Particle trailParticle,
            int despawnTimeAfterHit, // Time in seconds after hitting something
            boolean isOnFire,
            boolean makesExplosion,
            float explosionStrength,
            boolean sculkEffect,
            double speed,
            double damage,
            boolean useSpeedBasedDamage
    ) {
        // Spawn the projectile
        Projectile projectile = shooter.launchProjectile(org.bukkit.entity.Snowball.class);
        projectile.setVelocity(shooter.getLocation().getDirection().multiply(speed));
        projectile.setShooter(shooter);

        if (isOnFire) {
            projectile.setFireTicks(100);
        }

        // Add a trail effect
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!projectile.isValid() || projectile.isDead()) {
                    cancel();
                    return;
                }
                projectile.getWorld().spawnParticle(trailParticle, projectile.getLocation(), 1, 0, 0, 0, 0);
            }
        }.runTaskTimer(plugin, 0L, 1L);

        // Listen for collision events
        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onProjectileHit(org.bukkit.event.entity.ProjectileHitEvent event) {
                if (event.getEntity().equals(projectile)) {
                    // Start the despawn timer after collision
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (projectile.isValid()) {
                                projectile.remove();
                            }
                        }
                    }.runTaskLater(plugin, despawnTimeAfterHit * 20L); // Convert seconds to ticks

                    // Handle explosion if enabled
                    if (makesExplosion) {
                        projectile.getWorld().createExplosion(projectile.getLocation(), explosionStrength, false, false);
                    }

                    // Unregister the event listener
                    org.bukkit.event.HandlerList.unregisterAll(this);
                }
            }
        }, plugin);
    }
}