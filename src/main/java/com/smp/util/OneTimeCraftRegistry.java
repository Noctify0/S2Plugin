package com.smp.util;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class OneTimeCraftRegistry {

    private static final Map<String, OneTimeCraftItem> registeredItems = new HashMap<>();

    public static void registerItem(String name, Material type, int customModelData) {
        registeredItems.put(name, new OneTimeCraftItem(name, type, customModelData));
    }

    public static OneTimeCraftItem getItem(String name) {
        return registeredItems.get(name);
    }

    public static Map<String, OneTimeCraftItem> getRegisteredItems() {
        return registeredItems;
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