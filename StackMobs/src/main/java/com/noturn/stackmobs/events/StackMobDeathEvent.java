package com.noturn.stackmobs.events;

import com.noturn.stackmobs.StackedEntity;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;

@Getter
@RequiredArgsConstructor
public class StackMobDeathEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final StackedEntity stackedEntity;

    @Setter
    @NonNull
    private int deathAmount;

    private final EntityDeathEvent deathEvent;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
