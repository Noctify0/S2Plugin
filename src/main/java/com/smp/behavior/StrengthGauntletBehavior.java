package com.smp.behavior;

import com.smp.Smp;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;

public class StrengthGauntletBehavior implements Listener {
    private final Map<UUID, Long> earthquakeCooldown;
    private final Map<UUID, Long> dashCooldown;
    private final Smp plugin;

    public StrengthGauntletBehavior(Map<UUID, Long> earthquakeCooldown, Map<UUID, Long> dashCooldown, Smp plugin) {
        this.earthquakeCooldown = earthquakeCooldown;
        this.dashCooldown = dashCooldown;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractWithGauntlet(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Ensure the player is holding the Strength Gauntlet
        if (!isGauntlet(item)) return;

        UUID playerId = player.getUniqueId();
        Action action = event.getAction();
        long now = System.currentTimeMillis();

        // Earthquake ability: Sneak + Left Click
        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && player.isSneaking()) {
            if (earthquakeCooldown.containsKey(playerId)) {
                long timeLeft = (60000 - (now - earthquakeCooldown.get(playerId))) / 1000; // 1-minute cooldown
                if (timeLeft > 0) {
                    player.sendMessage("§cEarthquake is on cooldown! Time left: " + timeLeft + " seconds.");
                    return;
                }
            }

            earthquakeCooldown.put(playerId, now);
            Location center = player.getLocation();
            int radius = 20;
            World world = player.getWorld();

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            Location blockLoc = center.clone().add(x, -1, z);
                            if (blockLoc.distance(center) <= radius) {
                                Block block = world.getBlockAt(blockLoc);

                                // Skip the block the player is standing on
                                if (blockLoc.equals(center.clone().add(0, -1, 0))) continue;

                                if (block.getType().isSolid()) {
                                    // Spawn falling block
                                    FallingBlock falling = world.spawnFallingBlock(block.getLocation().add(0, 1, 0), block.getBlockData());
                                    falling.setVelocity(new Vector(0, 0.6, 0));
                                    falling.setDropItem(false);

                                    // Set the original block to air
                                    block.setType(Material.AIR);

                                    // Spawn explosion particles
                                    world.spawnParticle(Particle.EXPLOSION, block.getLocation().add(0.5, 0.5, 0.5), 10, 0.2, 0.2, 0.2, 0);

                                    // Play explosion sound to all nearby players
                                    world.playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 40f, 1f);
                                }
                            }
                        }
                    }
                }
            }.runTask(plugin);
        }

        // Dash ability: Right Click
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && !player.isSneaking()) {
            if (dashCooldown.containsKey(playerId)) {
                long timeLeft = (10000 - (now - dashCooldown.get(playerId))) / 1000; // 10-second cooldown
                if (timeLeft > 0) {
                    player.sendMessage("§cDash is on cooldown! Time left: " + timeLeft + " seconds.");
                    return;
                }
            }

            dashCooldown.put(playerId, now);

            // Apply dash velocity
            Vector direction = player.getLocation().getDirection().normalize().multiply(2); // Adjust multiplier for speed
            player.setVelocity(direction);

            // Play sound and particle effects
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 1.5f);
            player.spawnParticle(Particle.CLOUD, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
        }
    }

    private boolean isGauntlet(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasCustomModelData() && meta.getCustomModelData() == 2556;
    }
}