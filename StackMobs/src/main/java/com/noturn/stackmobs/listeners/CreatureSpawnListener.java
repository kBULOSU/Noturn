package com.noturn.stackmobs.listeners;

import com.noturn.stackmobs.StackMobsAPI;
import com.noturn.stackmobs.StackMobsPlugin;
import com.noturn.stackmobs.StackedEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void on(CreatureSpawnEvent event) {
        if (!event.getLocation().getWorld().getName().equals(StackMobsAPI.WORLD)) {
            return;
        }

        LivingEntity entity = event.getEntity();
        EntityType type = entity.getType();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if (type == EntityType.ARMOR_STAND) {
            return;
        }

        if (entity.hasMetadata(StackMobsAPI.NEW_STACK_ENTITY_TAG)) {
            entity.removeMetadata(StackMobsAPI.NEW_STACK_ENTITY_TAG, StackMobsPlugin.INSTANCE);
            return;
        }

        if (StackMobsAPI.handle(entity, reason, event.getLocation())) {
            entity.remove();
            return;
        }

        StackedEntity stacked = new StackedEntity(entity);
        stacked.setSize(1);

        StackMobsAPI.setAi(entity);
    }
}
