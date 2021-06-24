package com.noturn.stackmobs.listeners;

import com.noturn.stackmobs.StackMobsAPI;
import com.noturn.stackmobs.StackMobsPlugin;
import com.noturn.stackmobs.StackedEntity;
import com.noturn.stackmobs.events.StackMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void on(SlimeSplitEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }

        if (!event.getEntity().getWorld().getName().equals(StackMobsAPI.WORLD)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void on(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }

        if (!event.getEntity().getWorld().getName().equals(StackMobsAPI.WORLD)) {
            return;
        }

        LivingEntity entity = event.getEntity();

        StackedEntity stacked = new StackedEntity(entity);

        StackMobDeathEvent stackMobDeathEvent = new StackMobDeathEvent(stacked, 1, event);

        Bukkit.getPluginManager().callEvent(stackMobDeathEvent);

        int withdraw = stackMobDeathEvent.getDeathAmount();

        if (withdraw >= stacked.getSize()) {
            return;
        }

        Entity newEntity = spawnNewEntity(stacked.getSize(), withdraw, entity);

        if (newEntity != null) {
            StackMobsAPI.updateEntityName(newEntity);
        }
    }

    public static Entity spawnNewEntity(int oldSize, int withdraw, Entity dead) {

        dead.removeMetadata(StackMobsAPI.STACK_SIZE_TAG, StackMobsPlugin.INSTANCE);

        Entity clone = StackMobsAPI.duplicate(dead, entity -> {
            entity.fromMobSpawner = true;
            entity.getBukkitEntity().setMetadata(StackMobsAPI.NEW_STACK_ENTITY_TAG, new FixedMetadataValue(StackMobsPlugin.INSTANCE, true));
        });

        if (clone != null) {
            StackedEntity stacked = new StackedEntity(clone);
            stacked.setSize(oldSize - withdraw);

            ((LivingEntity) clone).setCanPickupItems(false);

            if (clone instanceof Zombie) {
                ((Zombie) clone).setBaby(false);
                ((Zombie) clone).setVillager(false);
            }

            if (clone instanceof PigZombie) {
                ((PigZombie) clone).setBaby(false);
            }

            if (clone instanceof Slime) {
                ((Slime) clone).setSize(3);
            }

            if (clone instanceof MagmaCube) {
                ((MagmaCube) clone).setSize(3);
            }

            if (clone instanceof Skeleton) {
                ((Skeleton) clone).setSkeletonType(Skeleton.SkeletonType.NORMAL);
            }

            return clone;
        }

        return null;
    }
}
