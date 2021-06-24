package com.noturn.gems;

import com.google.common.base.Enums;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class NoturnGemsConstants {

    public static class Mysql {

        public static class Tables {

            public static final String GEMS_TABLE = "user_gems";

        }

    }

    public static class Config {

        public static final ItemStack ITEM;

        private static final ConfigurationSection GEM_SECTION = NoturnGemsPlugin.INSTANCE.getConfig()
                .getConfigurationSection("gemas");

        private static final ConfigurationSection ITEM_SECTION = NoturnGemsPlugin.INSTANCE.getConfig()
                .getConfigurationSection("gem-item");

        public static final int DEFAULT_DROP_AMOUNT = GEM_SECTION.getInt("defaultDropAmount");

        public static final double DEFAULT_SELL_VALUE = GEM_SECTION.getDouble("defaultSellValue");

        public static final Map<String, Double> MULTIPLIERS = new HashMap<>();

        public static final Map<EntityType, Integer> PER_ENTITY_DROPS = Maps.newEnumMap(EntityType.class);

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

            ConfigurationSection perEntityAmountSection = NoturnGemsPlugin.INSTANCE.getConfig()
                    .getConfigurationSection("per-entity-amount");

            for (String key : perEntityAmountSection.getKeys(false)) {
                PER_ENTITY_DROPS.put(Enums.getIfPresent(EntityType.class, key).orNull(), perEntityAmountSection.getInt(key));
            }

            Set<EntityType> entities = NoturnGemsPlugin.INSTANCE.getConfig().getStringList("entities")
                    .stream()
                    .map(name -> Enums.getIfPresent(EntityType.class, name).orNull())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            ENTITIES.addAll(entities);

            ITEM = new ItemStack(NoturnGemsConstants.Config.ITEM_MATERIAL, NoturnGemsConstants.Config.ITEM_DATA);

            ItemMeta itemMeta = ITEM.getItemMeta();
            itemMeta.setDisplayName(NoturnGemsConstants.Config.ITEM_DISPLAY_NAME);
            itemMeta.setLore(NoturnGemsConstants.Config.ITEM_LORE);

            ITEM.setItemMeta(itemMeta);
        }

    }
}
