package com.noturn.gems;

import com.google.common.base.Enums;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.*;
import java.util.stream.Collectors;

public class NoturnGemsConstants {

    public static class Mysql {

        public static class Tables {

            public static final String GEMS_TABLE = "user_gems";

        }

    }

    public static class Config {

        private static final ConfigurationSection GEM_SECTION = NoturnGemsPlugin.INSTANCE.getConfig()
                .getConfigurationSection("gemas");

        private static final ConfigurationSection ITEM_SECTION = NoturnGemsPlugin.INSTANCE.getConfig()
                .getConfigurationSection("gem-item");

        public static final int DEFAULT_DROP_AMOUNT = GEM_SECTION.getInt("defaultDropAmount");

        public static final double DEFAULT_SELL_VALUE = GEM_SECTION.getDouble("defaultSellValue");

        public static final Map<String, Double> MULTIPLIERS = new HashMap<>();

        public static final Set<EntityType> ENTITIES = EnumSet.noneOf(EntityType.class);

        public static final Material ITEM_MATERIAL = Material.getMaterial(ITEM_SECTION.getInt("material"));

        public static final short ITEM_DATA = (short) ITEM_SECTION.getInt("data");

        public static final String ITEM_DISPLAY_NAME = ChatColor.translateAlternateColorCodes(
                '&', ITEM_SECTION.getString("displayName"));

        public static final List<String> ITEM_LORE = ITEM_SECTION.getStringList("lore");

        static {
            ConfigurationSection sellMultipliers = GEM_SECTION.getConfigurationSection("sellMultipliers");

            for (String multiplier : sellMultipliers.getKeys(false)) {
                MULTIPLIERS.put(multiplier, sellMultipliers.getDouble(multiplier));
            }

            Set<EntityType> entities = NoturnGemsPlugin.INSTANCE.getConfig().getStringList("entities")
                    .stream()
                    .map(name -> Enums.getIfPresent(EntityType.class, name).orNull())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            ENTITIES.addAll(entities);
        }

    }
}
