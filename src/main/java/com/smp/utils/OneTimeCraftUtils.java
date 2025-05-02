package com.smp.utils;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class OneTimeCraftUtils {

    private static final Map<String, OneTimeCraftItem> registeredItems = new HashMap<>();
    private static final Map<String, Integer> craftCount = new HashMap<>();

    public static void registerItem(String name, Material type, int customModelData) {
        registeredItems.put(name, new OneTimeCraftItem(name, type, customModelData));
        craftCount.put(name, 0); // Initialize craft count
    }

    public static void markAsOneTimeCraft(String name, Material type, int customModelData) {
        registerItem(name, type, customModelData);
    }

    public static boolean isOneTimeCraft(String name) {
        return registeredItems.containsKey(name);
    }

    public static OneTimeCraftItem getItem(String name) {
        return registeredItems.get(name);
    }

    public static Map<String, OneTimeCraftItem> getRegisteredItems() {
        return registeredItems;
    }

    public static int getCraftCount(String name) {
        return craftCount.getOrDefault(name, 0);
    }

    public static class OneTimeCraftItem {
        private final String name;
        private final Material type;
        private final int customModelData;

        public OneTimeCraftItem(String name, Material type, int customModelData) {
            this.name = name;
            this.type = type;
            this.customModelData = customModelData;
        }

        public String getName() {
            return name;
        }

        public Material getType() {
            return type;
        }

        public int getCustomModelData() {
            return customModelData;
        }
    }
}