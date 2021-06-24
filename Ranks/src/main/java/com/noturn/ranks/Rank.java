package com.noturn.ranks;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Rank {

    private final int id;
    private final String displayName;
    private final boolean defaultRank;
    private final List<String> commandsOnRankUp;
    private final double coinsRequirement;
    private final double gemsRequirement;

}
