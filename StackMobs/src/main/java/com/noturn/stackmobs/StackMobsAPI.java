package com.noturn.stackmobs;

import com.noturn.stackmobs.listeners.ChunkLoadListener;
import com.noturn.stackmobs.listeners.CreatureSpawnListener;
import com.noturn.stackmobs.listeners.EntityDeathListener;
import com.noturn.stackmobs.misc.utils.NMS;
import com.noturn.stackmobs.tasks.MergeTask;
import com.noturn.stackmobs.tasks.UpdateDisplayNameTask;
import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class StackMobsAPI {

    public static final String STACK_SIZE_TAG = "stackmobs:stack-size";
    public static final String NEW_STACK_ENTITY_TAG = "stackmobs:stack-size";
    public static final String PREVENT_STACK_TAG = "stackmobs:prevent-stack";
    public static final String SINGLE_KILL_TAG = "stackmobs:single-kill";

    public static final String WORLD = StackMobsPlugin.INSTANCE.getConfig().getString("main-world");
    public static final int LIMIT = StackMobsPlugin.INSTANCE.getConfig().getInt("limite");

    public static Vector CHECK_AREA = new Vector(7, 4, 7);

    private static boolean ENABLED = false;

    public static void disable() {
        ENABLED = false;
    }

    public static void enable() {
        if (ENABLED) {
            return;
        }

        ENABLED = true;

        PluginManager pluginManager = StackMobsPlugin.INSTANCE.getServer().getPluginManager();
        pluginManager.registerEvents(new CreatureSpawnListener(), StackMobsPlugin.INSTANCE);
        pluginManager.registerEvents(new EntityDeathListener(), StackMobsPlugin.INSTANCE);
        pluginManager.registerEvents(new ChunkLoadListener(), StackMobsPlugin.INSTANCE);

        new UpdateDisplayNameTask().runTaskTimer(StackMobsPlugin.INSTANCE, 0, 10);
        new MergeTask().runTaskTimer(StackMobsPlugin.INSTANCE, 0, 20 * 3);
    }

    public static StackedEntity getStackedEntity(Entity entity) {
        return new StackedEntity(entity);
    }

    public static boolean handle(Entity entity, CreatureSpawnEvent.SpawnReason reason, Location location) {
        StackedEntity stacked = new StackedEntity(entity);

        if (stacked.isStackingPrevented()) {
            return false;
        }

        if (stacked.getSize() >= LIMIT) {
            return true;
        }

        return handle(entity.getType(), reason, location);
    }

    public static void updateEntityName(Entity entity) {
        StackedEntity stacked = new StackedEntity(entity);

        if (!stacked.isStackingPrevented() && stacked.hasStackSizeTag()) {
            entity.setCustomName(StackMobsAPI.getMobName(entity.getType(), stacked.getSize()));

            entity.setCustomNameVisible(true);
        }
    }

    public static boolean handle(EntityType type, CreatureSpawnEvent.SpawnReason reason, Location location) {
        if (type == EntityType.ARMOR_STAND) {
            return false;
        }

        double xLoc = CHECK_AREA.getX();
        double yLoc = CHECK_AREA.getY();
        double zLoc = CHECK_AREA.getZ();

        for (Entity nearby : location.getWorld().getNearbyEntities(location, xLoc, yLoc, zLoc)) {
            if (type != nearby.getType() || nearby.isDead()) {
                continue;
            }

            StackedEntity stacked = new StackedEntity(nearby);

            if (stacked.isStackingPrevented()) {
                continue;
            }

            if (stacked.getSize() >= 300) {
                return true;
            }

            stacked.setSize(stacked.getSize() + 1);
            return true;
        }

        return false;
    }

    public static Entity duplicate(@NonNull Entity original, Consumer<net.minecraft.server.v1_8_R3.Entity> preSpawn) {
        net.minecraft.server.v1_8_R3.Entity entity;

        CraftWorld world = (CraftWorld) original.getWorld();

        if (original instanceof Zombie || original instanceof Skeleton) {
            Location location = new Location(
                    original.getWorld(),
                    original.getLocation().getBlockX() + 0.5,
                    original.getLocation().getY(),
                    original.getLocation().getBlockZ() + 0.5
            );

            entity = world.createEntity(location, original.getType().getEntityClass());
        } else {
            entity = world.createEntity(original.getLocation(), original.getType().getEntityClass());
        }

        preSpawn.accept(entity);

        Entity clone = world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        setAi(clone);

        return cloneTraits(clone);
    }

    public static Entity cloneTraits(Entity clone) {
        return clone;
    }

    public static void setAi(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();

        if (nmsEntity instanceof EntityInsentient) {
            NMS.clearEntitySelectors((EntityInsentient) nmsEntity);

            ((EntityInsentient) nmsEntity).getAttributeInstance(GenericAttributes.c).setValue(0.1D);
        }
    }

    public static Set<Entity> getLoadedEntities() {
        List<Entity> entities = Bukkit.getWorld(WORLD).getEntities();

        Set<Entity> set = new HashSet<>();
        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity)) {
                continue;
            }

            if (entity.hasMetadata(STACK_SIZE_TAG)) {
                set.add(entity);
            }
        }

        return set;
    }

    public static String getMobName(EntityType type, int size) {
        String[] names = type.name().replace("_", " ").toLowerCase().split(" ");
        String name = "";

        for (String name1 : names) {
            name = name1.substring(0, 1).toUpperCase() + name1.substring(1).toLowerCase() + " ";
        }

        if (name.equals("")) {
            name = type.name().replace("_", " ").substring(0, 1).toUpperCase() + type.name().replace("_", " ").substring(1).toLowerCase();
        }

        return ChatColor.YELLOW.toString() + size + "x " + name.trim();
    }
}
