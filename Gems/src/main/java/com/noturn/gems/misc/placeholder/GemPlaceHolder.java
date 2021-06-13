package com.noturn.gems.misc.placeholder;

import com.noturn.gems.controller.GemsUserController;
import com.noturn.gems.misc.utils.NumberUtil;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class GemPlaceHolder extends PlaceholderExpansion {

    private final GemsUserController userController;

    @Override
    public @NotNull String getIdentifier() {
        return "noturngems";
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
        if (!params.equalsIgnoreCase("gemas")) {
            return "";
        }

        Double value = userController.get(player.getName());
        if (value == null) {
            return "0.0";
        }

        return NumberUtil.toK(value);
    }
}
