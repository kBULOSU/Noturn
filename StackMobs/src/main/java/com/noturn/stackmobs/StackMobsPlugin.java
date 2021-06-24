package com.noturn.stackmobs;

import org.bukkit.plugin.java.JavaPlugin;

public class StackMobsPlugin extends JavaPlugin {

    public static StackMobsPlugin INSTANCE;

    @Override
    public void onEnable() {
        super.onEnable();

        INSTANCE = this;

        saveDefaultConfig();

        StackMobsAPI.enable();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        StackMobsAPI.disable();
    }
}
