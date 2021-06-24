package com.noturn.stackmobs.listeners;

import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.noturn.stackmobs.StackMobsAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChunkLoadListener implements Listener {

    @EventHandler
    public void on(ChunkLoadEvent event) {
        if (!event.getWorld().getName().equals(StackMobsAPI.WORLD)) {
            return;
        }

        Set<Entity> entities = Sets.newHashSet(event.getChunk().getEntities());

        entities.stream()
                .filter(entity -> entity instanceof LivingEntity)
                .forEach(entity -> {
                    String name = entity.getCustomName();

                    if (name == null || name.isEmpty()) {
                        return;
                    }

                    name = ChatColor.stripColor(name);

                    Pattern pattern = Pattern.compile("[0-9]+[x] ");
                    Matcher matcher = pattern.matcher(name);

                    if (!matcher.find()) {
                        return;
                    }

                    String rawSize = matcher.group(0).replaceAll("[^\\d]", "");

                    Integer size = Ints.tryParse(rawSize);

                    if (size == null) {
                        return;
                    }

                    EntityDeathListener.spawnNewEntity(size, 0, entity);
                    entity.remove();
                });
    }
}
