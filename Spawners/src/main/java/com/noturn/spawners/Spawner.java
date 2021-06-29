package com.noturn.spawners;

import com.noturn.spawners.api.SerializedLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

@AllArgsConstructor
@Getter
public class Spawner {

    private final Integer id;

    private final String owner;

    private final EntityType entityType;

    @Setter
    private int amount;

    private final SerializedLocation serializedLocation;

    public Location getLocation() {
        return new Location(
                Bukkit.getWorld(serializedLocation.getWorldName()),
                serializedLocation.getX(),
                serializedLocation.getY(),
                serializedLocation.getZ()
        );
    }
}
