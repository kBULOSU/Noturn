package com.noturn.stackmobs.misc.utils;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

import java.lang.reflect.Field;

public class NMS {

    public static void clearEntitySelectors(EntityInsentient entity) {
        clearGoalSelector(entity);
        clearTargetSelector(entity);
    }

    public static void clearGoalSelector(EntityInsentient entity) {
        try {
            ((UnsafeList<?>) PATHFINDER_GOAL_1.get(entity.goalSelector)).clear();
            ((UnsafeList<?>) PATHFINDER_GOAL_2.get(entity.goalSelector)).clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void clearTargetSelector(EntityInsentient entity) {
        try {
            ((UnsafeList<?>) PATHFINDER_GOAL_1.get(entity.targetSelector)).clear();
            ((UnsafeList<?>) PATHFINDER_GOAL_2.get(entity.targetSelector)).clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Field PATHFINDER_GOAL_1, PATHFINDER_GOAL_2;

    static {
        try {
            PATHFINDER_GOAL_1 = PathfinderGoalSelector.class.getDeclaredField("b");
            PATHFINDER_GOAL_1.setAccessible(true);

            PATHFINDER_GOAL_2 = PathfinderGoalSelector.class.getDeclaredField("c");
            PATHFINDER_GOAL_2.setAccessible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
