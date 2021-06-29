package com.noturn.spawners.cache.local;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.noturn.spawners.Spawner;
import com.noturn.spawners.api.SerializedLocation;
import com.noturn.spawners.dao.SpawnersDAO;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SpawnersLocalCache {

    private final SpawnersDAO spawnersDAO;

    private final LoadingCache<SerializedLocation, Spawner> spawnersCache = CacheBuilder.newBuilder()
            .expireAfterAccess(3L, TimeUnit.MINUTES)
            .build(new CacheLoader<SerializedLocation, Spawner>() {
                @Override
                public Spawner load(SerializedLocation serializedLocation) {
                    return spawnersDAO.fetch(serializedLocation);
                }
            });

    public Spawner get(Location location) {
        SerializedLocation serializedLocation = SerializedLocation.of(location);

        try {
            return spawnersCache.get(serializedLocation);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void invalidate(Location location) {
        SerializedLocation serializedLocation = SerializedLocation.of(location);

        spawnersCache.invalidate(serializedLocation);
    }
}
