package com.smp.behavior;

import com.smp.Smp;
import com.smp.utils.CooldownUtils;
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

public class StrengthGauntletBehavior implements Listener {
    private final Smp plugin;

    public StrengthGauntletBehavior(Smp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractWithGauntlet(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Ensure the player is holding the Strength Gauntlet
        if (!isGauntlet(item)) return;

        Action action = event.getAction();

        // Earthquake ability: Sneak + Left Click
        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) && player.isSneaking()) {
            handleEarthquakeAbility(player);
        }

        // Dash ability: Right Click
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && !player.isSneaking()) {
            handleDashAbility(player);
        }
    }

    private void handleEarthquakeAbility(Player player) {
        if (CooldownUtils.isOnCooldown(player.getUniqueId(), "earthquake")) {
            double timeLeft = CooldownUtils.getRemainingCooldown(player.getUniqueId(), "earthquake");
            CooldownUtils.sendCooldownMessage(player, "Earthquake", timeLeft);
            return;
        }

        CooldownUtils.setCooldown(player.getUniqueId(), "earthquake", 60); // 1-minute cooldown
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

    private void handleDashAbility(Player player) {
        if (CooldownUtils.isOnCooldown(player.getUniqueId(), "dash")) {
            double timeLeft = CooldownUtils.getRemainingCooldown(player.getUniqueId(), "dash");
            CooldownUtils.sendCooldownMessage(player, "Dash", timeLeft);
            return;
        }

        CooldownUtils.setCooldown(player.getUniqueId(), "dash", 10); // 10-second cooldown

        // Apply dash velocity
        Vector direction = player.getLocation().getDirection().normalize().multiply(2); // Adjust multiplier for speed
        player.setVelocity(direction);

        // Play sound and particle effects
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 1.5f);
        player.spawnParticle(Particle.CLOUD, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
    }

    private boolean isGauntlet(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasCustomModelData() && meta.getCustomModelData() == 2556;
    }
}