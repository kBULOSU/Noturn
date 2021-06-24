package com.noturn.stackdrops;

import com.noturn.stackdrops.listeners.ItemListener;
import com.noturn.stackdrops.listeners.ItemPickupListener;
import org.bukkit.entity.Item;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StackDropsPlugin extends JavaPlugin {

    public static StackDropsPlugin INSTANCE;

    public static final String METADATA_KEY = "itemAmount";

    @Override
    public void onEnable() {
        super.onEnable();

        INSTANCE = this;


        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ItemListener(), this);
        pluginManager.registerEvents(new ItemPickupListener(), this);
    }

    public static void updateItemSilent(final Item item, final int amount) {
        item.setMetadata(METADATA_KEY, new FixedMetadataValue(StackDropsPlugin.INSTANCE, amount));
        item.getItemStack().setAmount(1);

        item.setCustomNameVisible(true);
        item.setCustomName(StackDropsConstants.TRANSLATE_ITEM.get(item.getItemStack(), true, amount));
    }
}
