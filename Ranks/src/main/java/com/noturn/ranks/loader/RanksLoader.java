package com.noturn.ranks.loader;

import com.google.common.primitives.Ints;
import com.noturn.ranks.Rank;
import com.noturn.ranks.RanksRegistry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class RanksLoader {

    public void load(FileConfiguration config) {
        ConfigurationSection ranks = config.getConfigurationSection("ranks");
        if (ranks == null) {
            return;
        }

        for (String key : ranks.getKeys(false)) {
            Integer id = Ints.tryParse(key);
            if (id == null) {
                System.out.println("O id " + key + " é inválido.");
                continue;
            }

            String name = ranks.getString(key + ".Name");

            boolean isDefault = ranks.getBoolean(key + ".default");

            List<String> commandsOnRankup = ranks.getStringList(key + ".commandsOnRankup");

            double coinsPrice = ranks.getDouble(key + ".coinsPrice");
            double gemsprice = ranks.getDouble(key + ".gemsPrice");

            Rank rank = new Rank(
                    id,
                    name,
                    isDefault,
                    commandsOnRankup,
                    coinsPrice,
                    gemsprice
            );

            RanksRegistry.register(rank);
        }
    }

}
