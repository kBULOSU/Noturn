package com.noturn.stackmobs.tasks;

import com.noturn.stackmobs.StackMobsAPI;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateDisplayNameTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Entity entity : StackMobsAPI.getLoadedEntities()) {
            StackMobsAPI.updateEntityName(entity);
        }
    }
}
