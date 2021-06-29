package com.noturn.ranks;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RanksRegistry {

    private static final TIntObjectMap<Rank> RANKS_BY_ID = new TIntObjectHashMap<>();
    private static final Map<String, Rank> RANKS_BY_NAME = new HashMap<>();

    public static Rank DEFAULT_RANK;

    public static void register(List<Rank> ranks) {
        for (Rank rank : ranks) {
            RANKS_BY_ID.put(rank.getId(), rank);

            if (rank.isDefaultRank()) {
                System.out.println("O Rank default tem o ID " + rank.getId());

                DEFAULT_RANK = rank;
            }

            RANKS_BY_NAME.put(rank.getDisplayName(), rank);
        }
    }

    public static Rank getById(int id) {
        return RANKS_BY_ID.get(id);
    }

    public static Rank getByName(String name) {
        return RANKS_BY_NAME.get(name);
    }

    public static Rank getNext(Rank rank) {
        if (rank == null) {
            return null;
        }

        return RANKS_BY_ID.get(rank.getId() + 1);
    }

    public static Rank getPrevious(Rank rank) {
        return RANKS_BY_ID.get(rank.getId() - 1);
    }

    public static Collection<Rank> getAll() {
        return RANKS_BY_ID.valueCollection();
    }
}
