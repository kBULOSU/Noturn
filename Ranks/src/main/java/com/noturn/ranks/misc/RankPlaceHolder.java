package com.noturn.ranks.misc;

import com.noturn.ranks.cache.local.PlayerRanksLocalCache;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class RankPlaceHolder extends PlaceholderExpansion {

    private final PlayerRanksLocalCache playerRanksLocalCache;

    @Override
    public @NotNull String getIdentifier() {
        return "noturnranks";
    }

    @Override
    public @NotNull String getAuthor() {
        return "yiatzz";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1-ALPHA";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (!params.equalsIgnoreCase("rank")) {
            return "";
        }

        return playerRanksLocalCache.get(player.getName()).getDisplayName();
    }
}
