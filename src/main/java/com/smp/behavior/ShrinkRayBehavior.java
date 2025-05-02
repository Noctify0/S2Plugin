package com.smp.behavior;

import com.smp.utils.CooldownUtils;
import com.smp.utils.EntitySizeUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class ShrinkRayBehavior implements Listener {

    private final Plugin plugin;

    public ShrinkRayBehavior(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() != Material.PAPER || item.getItemMeta() == null) return;
        if (!item.getItemMeta().hasCustomModelData() || item.getItemMeta().getCustomModelData() != 266) return;

        UUID playerId = player.getUniqueId();

        // Handle SHIFT + LEFT_CLICK to shrink the player
        if (event.getAction().toString().contains("LEFT_CLICK") && player.isSneaking()) {
            if (CooldownUtils.isOnCooldown(playerId, "shrink")) {
                double timeLeft = CooldownUtils.getRemainingCooldown(playerId, "shrink");
                CooldownUtils.sendCooldownMessage(player, "Shrink", timeLeft);
                return;
            }
            CooldownUtils.setCooldown(playerId, "shrink", 30); // 30-second cooldown
            shrinkEntity(player, 0.5f);
        }

        // Handle SHIFT + RIGHT_CLICK to activate the beam
        if (event.getAction().toString().contains("RIGHT_CLICK") && player.isSneaking()) {
            if (CooldownUtils.isOnCooldown(playerId, "beam")) {
                double timeLeft = CooldownUtils.getRemainingCooldown(playerId, "beam");
                CooldownUtils.sendCooldownMessage(player, "Beam", timeLeft);
                return;
            }
            CooldownUtils.setCooldown(playerId, "beam", 45); // 45-second cooldown
            shootBeam(player);
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack bow = event.getBow();
        if (bow == null || bow.getItemMeta() == null || bow.getItemMeta().getCustomModelData() != 266) return;

        UUID playerId = player.getUniqueId();

        if (CooldownUtils.isOnCooldown(playerId, "beam")) {
            double timeLeft = CooldownUtils.getRemainingCooldown(playerId, "beam");
            CooldownUtils.sendCooldownMessage(player, "Beam", timeLeft);
            event.setCancelled(true);
            return;
        }
        CooldownUtils.setCooldown(playerId, "beam", 45); // 45-second cooldown
        shootBeam(player);
        event.setCancelled(true);
    }

    private void shootBeam(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        Vector start = player.getEyeLocation().toVector();

        new BukkitRunnable() {
            double t = 0;

            @Override
            public void run() {
                t += 0.5;
                Vector pos = start.clone().add(direction.clone().multiply(t));
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLUE, 1.0f);
                player.getWorld().spawnParticle(Particle.DUST, pos.toLocation(player.getWorld()), 10, 0.75, 0.75, 0.75, dustOptions);

                for (Entity entity : player.getNearbyEntities(50, 50, 50)) {
                    if (entity instanceof LivingEntity target && pos.toLocation(player.getWorld()).distance(entity.getLocation()) < 1.5) {
                        EntitySizeUtils.setSize(target, 2.0f);
                        target.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 20 * 12, 1));

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                EntitySizeUtils.resetSize(target);
                            }
                        }.runTaskLater(plugin, 20 * 12);

                        this.cancel();
                        break;
                    }
                }
                if (t > 30) this.cancel();
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void shrinkEntity(Player player, float scale) {
        EntitySizeUtils.setSize(player, scale);

        player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 20 * 12, 3));
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.JUMP_BOOST, 20 * 12, 3));

        new BukkitRunnable() {
            @Override
            public void run() {
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                EntitySizeUtils.resetSize(player);
            }
        }.runTaskLater(plugin, 20 * 12);
    }
}