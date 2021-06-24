package com.noturn.ranks.cache.local;

import com.noturn.ranks.Rank;
import com.noturn.ranks.RanksRegistry;

import java.util.HashMap;
import java.util.Map;

public class PlayerRanksLocalCache {

    private final Map<String, Rank> playerRankMap;

    public PlayerRanksLocalCache() {
        playerRankMap = new HashMap<>();
    }

    public Rank get(String userName) {
        Rank rank = playerRankMap.get(userName);
        if (rank == null) {
            return RanksRegistry.DEFAULT_RANK;
        }

        return rank;
    }

    public Rank put(String userName, Rank rank) {
        return playerRankMap.put(userName, rank);
    }

    public void invalidate(String userName) {
        playerRankMap.remove(userName);
    }
}
