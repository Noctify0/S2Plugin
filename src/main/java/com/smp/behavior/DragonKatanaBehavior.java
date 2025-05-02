package com.smp.behavior;

import com.smp.Smp;
import com.smp.utils.CooldownUtils;
import com.smp.utils.TeleportUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class DragonKatanaBehavior implements Listener {

    private final Smp plugin;

    public DragonKatanaBehavior(Smp plugin) {
        this.plugin = plugin;
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
            String ability = "Teleport";
            int cooldownTime = 30;

            // Check if the teleport ability is on cooldown
            if (CooldownUtils.isOnCooldown(playerId, ability)) {
                double timeLeft = CooldownUtils.getRemainingCooldown(playerId, ability);
                CooldownUtils.sendCooldownMessage(player, ability, timeLeft);
                return;
            }

            // Get the block the player is looking at within a range of 50 blocks
            Block targetBlock = player.getTargetBlockExact(50);
            if (targetBlock == null) {
                player.sendMessage("§cNo valid block in range to teleport to!");
                return;
            }

            Location teleportLocation = targetBlock.getLocation().add(0.5, 1, 0.5); // Adjust to stand above the block

            // Use the TeleportUtils utility to handle teleportation
            boolean success = TeleportUtils.teleportPlayer(player, teleportLocation, 50, true, true, true);

            // Activate cooldown only if teleportation was successful
            if (success) {
                CooldownUtils.setCooldown(playerId, ability, cooldownTime);
            }
        }
    }
}