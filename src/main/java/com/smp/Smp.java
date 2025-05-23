package com.smp;

import com.smp.behavior.*;
import com.smp.items.*;
import com.smp.utils.GammaUtils;
import com.smp.listeners.LegendaryItemCraftListener;
import com.smp.utils.ManaUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public final class  Smp extends JavaPlugin implements Listener {

    private static Smp instance;
    private final Map<String, ItemStack> customItems = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Register behaviors
        Bukkit.getPluginManager().registerEvents(new ExcaliburBehavior(), this);
        Bukkit.getPluginManager().registerEvents(new StrengthGauntletBehavior(this), this);
        Bukkit.getPluginManager().registerEvents(new DragonKatanaBehavior(this), this);
        Bukkit.getPluginManager().registerEvents(new SmeltersPickaxeBehavior(), this);
        Bukkit.getPluginManager().registerEvents(new HeartBladeBehavior(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerHeadBehavior(this), this);
        Bukkit.getPluginManager().registerEvents(new GoldenHeadBehavior(), this);
        Bukkit.getPluginManager().registerEvents(new ShrinkRayBehavior(this), this);
        Bukkit.getPluginManager().registerEvents(new LegendaryItemCraftListener(this), this);
        Bukkit.getPluginManager().registerEvents(new SonicCrossbowBehavior(this), this);
        getServer().getPluginManager().registerEvents(new PistolBehavior(), this);

        // Register custom items
        customItems.put("excalibur", Excalibur.createItem());
        customItems.put("strength_gauntlet", StrengthGauntlet.createItem());
        customItems.put("dragon_katana", DragonKatana.createItem());
        customItems.put("smelters_pickaxe", SmeltersPickaxe.createItem());
        customItems.put("heart_blade", HeartBlade.createItem());
        customItems.put("player_head", PlayerHead.createItem());
        customItems.put("golden_head", GoldenHead.createItem());
        customItems.put("shrink_ray", ShrinkRay.createItem());
        customItems.put("sonic_crossbow", SonicCrossbow.createItem());
        customItems.put("pistol", Pistol.createItem());
        customItems.put("bullet", Bullet.createItem());

        // Add recipes to the game
        Bukkit.addRecipe(Excalibur.getRecipe());
        Bukkit.addRecipe(StrengthGauntlet.getRecipe());
        Bukkit.addRecipe(DragonKatana.getRecipe());
        Bukkit.addRecipe(SmeltersPickaxe.getRecipe());
        Bukkit.addRecipe(ShrinkRay.getRecipe());
        Bukkit.addRecipe(GoldenHead.getRecipe(customItems.get("player_head")));
        Bukkit.addRecipe(SonicCrossbow.getRecipe());
        Bukkit.addRecipe(Pistol.getRecipe(new NamespacedKey(this, "pistol"))); // Register Pistol recipe
        Bukkit.addRecipe(Bullet.getRecipe(new NamespacedKey(this, "bullet"))); // Register Bullet recipe
        Bukkit.addRecipe(HeartBlade.getRecipe(new NamespacedKey(this, "heart_blade")));

        // Register commands
        GammaUtils gammaUtils = new GammaUtils();
        ManaUtils.initialize(this);
        getCommand("nv").setExecutor(gammaUtils);
        getCommand("nightvision").setExecutor(gammaUtils);

        getLogger().info(ChatColor.GREEN + "Custom items plugin enabled!");
    }

    public static Smp getInstance() {
        return instance; // Provide access to the instance
    }

    @Override
    public void onDisable() {
        getLogger().info("Custom items plugin disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        String commandName = command.getName().toLowerCase();
        switch (commandName) {
            case "creload" -> {
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                    return true;
                }

                if (args.length == 0) {
                    reloadConfig();
                    com.smp.utils.CooldownUtils.clearAllCooldowns();
                    player.sendMessage(ChatColor.GREEN + "Plugin reloaded! Config and cooldowns have been reset.");
                } else if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "config" -> {
                            reloadConfig();
                            player.sendMessage(ChatColor.GREEN + "Config reloaded!");
                        }
                        case "cooldowns" -> {
                            com.smp.utils.CooldownUtils.clearAllCooldowns();
                            player.sendMessage(ChatColor.GREEN + "All cooldowns have been reset!");
                        }
                        default -> player.sendMessage(ChatColor.RED + "Invalid argument. Use /creload [config|cooldowns].");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /creload [config|cooldowns]");
                }
            }
            case "cgive" -> {
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /cgive {player} {item} [count]");
                    return true;
                }

                String targetSelector = args[0];
                String itemId = args[1].toLowerCase();
                int count = 1;

                if (args.length >= 3) {
                    try {
                        count = Integer.parseInt(args[2]);
                        if (count <= 0) {
                            player.sendMessage(ChatColor.RED + "Count must be a positive number.");
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Invalid count. Please provide a valid number.");
                        return true;
                    }
                }

                List<Player> targets = resolvePlayers(sender, targetSelector);
                if (targets.isEmpty()) {
                    player.sendMessage(ChatColor.RED + "No players found for selector: " + targetSelector);
                    return true;
                }

                ItemStack item = customItems.get(itemId);
                if (item == null) {
                    player.sendMessage(ChatColor.RED + "Item with ID '" + itemId + "' not found!");
                    return true;
                }
                item.setAmount(count);

                for (Player target : targets) {
                    target.getInventory().addItem(item);
                    target.sendMessage(ChatColor.GOLD + "You have received " + count + "x " + itemId + "!");
                }

                player.sendMessage(ChatColor.GREEN + "Gave " + count + "x " + itemId + " to " + targets.size() + " player(s).");
            }
            case "nv", "nightvision" -> {
                boolean hasNightVision = player.hasMetadata("night_vision");

                if (hasNightVision) {
                    // Disable night vision
                    player.removeMetadata("night_vision", Smp.getInstance());
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION); // Remove night vision effect
                    player.sendMessage(ChatColor.YELLOW + "Night vision disabled.");
                } else {
                    // Enable night vision
                    player.setMetadata("night_vision", new FixedMetadataValue(Smp.getInstance(), true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false)); // Add night vision effect
                    player.sendMessage(ChatColor.GREEN + "Night vision enabled.");
                }
                return true;
            }
            default -> {
                return false;
            }
        }

        return true;
    }

    private List<Player> resolvePlayers(CommandSender sender, String selector) {
        List<Player> players = new ArrayList<>();

        switch (selector) {
            case "@a": // All players
                players.addAll(Bukkit.getOnlinePlayers());
                break;
            case "@p": // Nearest player
                if (sender instanceof Player player) {
                    Player nearest = player.getWorld().getPlayers().stream()
                            .filter(p -> !p.equals(player))
                            .min((p1, p2) -> Double.compare(p1.getLocation().distance(player.getLocation()), p2.getLocation().distance(player.getLocation())))
                            .orElse(null);
                    if (nearest != null) players.add(nearest);
                }
                break;
            case "@s": // Command sender
                if (sender instanceof Player player) {
                    players.add(player);
                }
                break;
            case "@r": // Random player
                List<? extends Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                if (!onlinePlayers.isEmpty()) {
                    Player randomPlayer = onlinePlayers.get(new Random().nextInt(onlinePlayers.size()));
                    players.add(randomPlayer);
                }
                break;
            case "@e": // All entities (filtering only players for this example)
                if (sender instanceof Player player) {
                    players.addAll(player.getWorld().getPlayers());
                }
                break;
            default: // Specific player name
                Player target = Bukkit.getPlayer(selector);
                if (target != null) players.add(target);
                break;
        }

        return players;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("cgive")) {
            if (args.length == 1) {
                // Suggest player selectors and online player names
                List<String> suggestions = new ArrayList<>(List.of("@a", "@p", "@s", "@r"));
                Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
                return suggestions;
            } else if (args.length == 2) {
                // Suggest custom item IDs
                return new ArrayList<>(customItems.keySet());
            } else if (args.length == 3) {
                // Suggest common counts
                return List.of("1", "10", "64");
            }
        } else if (command.getName().equalsIgnoreCase("creload")) {
            if (args.length == 1) {
                // Suggest arguments for /creload
                return List.of("config", "cooldowns");
            }
        }
        return null; // No suggestions for other commands
    }
}