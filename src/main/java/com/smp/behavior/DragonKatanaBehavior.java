package com.smp.behavior;

import com.smp.Smp;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

public class DragonKatanaBehavior implements Listener {

    private final Map<UUID, Long> teleportCooldown;
    private final Smp plugin;

    public DragonKatanaBehavior(Map<UUID, Long> teleportCooldown, Smp plugin) {
        this.teleportCooldown = teleportCooldown;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        // Check if the dropped item is the Dragon Katana
        boolean isDragonKatana = droppedItem != null && droppedItem.hasItemMeta() &&
                droppedItem.getItemMeta().hasCustomModelData() &&
                droppedItem.getItemMeta().getCustomModelData() == 3456;

        if (isDragonKatana) {
            // Remove Speed effect if the player is no longer holding the Dragon Katana
            ItemStack currentItem = player.getInventory().getItemInMainHand();
            boolean isStillHoldingDragonKatana = currentItem != null && currentItem.hasItemMeta() &&
                    currentItem.getItemMeta().hasCustomModelData() &&
                    currentItem.getItemMeta().getCustomModelData() == 3456;

            if (!isStillHoldingDragonKatana) {
                player.removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractWithDragonKatana(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the item is the Dragon Katana
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasCustomModelData() || meta.getCustomModelData() != 3456) return;

        // Check if the action is a right-click
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            UUID playerId = player.getUniqueId();
            long now = System.currentTimeMillis();

            // Check if the teleport ability is on cooldown
            if (teleportCooldown.containsKey(playerId)) {
                long timeLeft = (30000 - (now - teleportCooldown.get(playerId))) / 1000; // 30-second cooldown
                if (timeLeft > 0) {
                    player.sendMessage("§cTeleport is on cooldown! Time left: " + timeLeft + " seconds.");
                    return;
                }
            }

            // Get the block the player is looking at within a range of 50 blocks
            Block targetBlock = player.getTargetBlockExact(50);
            if (targetBlock == null) {
                player.sendMessage("§cNo valid block in range to teleport to!");
                return;
            }

            Location teleportLocation = targetBlock.getLocation().add(0.5, 1, 0.5); // Adjust to stand above the block
            teleportLocation.setYaw(player.getLocation().getYaw()); // Preserve yaw (horizontal direction)
            teleportLocation.setPitch(player.getLocation().getPitch()); // Preserve pitch (vertical direction)
            player.teleport(teleportLocation);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            player.spawnParticle(Particle.PORTAL, teleportLocation, 50, 0.5, 0.5, 0.5, 0.1);

            // Activate cooldown only after successful teleport
            teleportCooldown.put(playerId, now);
        }
    }

    @EventHandler
    public void onPlayerHoldDragonKatana(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

        // Check if the new item is the Dragon Katana
        boolean isHoldingDragonKatana = newItem != null && newItem.hasItemMeta() &&
                newItem.getItemMeta().hasCustomModelData() &&
                newItem.getItemMeta().getCustomModelData() == 3456;

        // Check if the previous item was the Dragon Katana
        boolean wasHoldingDragonKatana = previousItem != null && previousItem.hasItemMeta() &&
                previousItem.getItemMeta().hasCustomModelData() &&
                previousItem.getItemMeta().getCustomModelData() == 3456;

        // Apply Speed effect if holding the Dragon Katana
        if (isHoldingDragonKatana && !wasHoldingDragonKatana) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false, true));
        }

        // Remove Speed effect if switching away from the Dragon Katana
        if (!isHoldingDragonKatana && wasHoldingDragonKatana) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }
}