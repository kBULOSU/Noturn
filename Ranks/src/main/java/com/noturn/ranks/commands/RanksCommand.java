package com.noturn.ranks.commands;

import com.noturn.gems.controller.GemsUserController;
import com.noturn.gems.dao.GemsUserDAO;
import com.noturn.gems.misc.utils.NumberUtil;
import com.noturn.ranks.Rank;
import com.noturn.ranks.RanksRegistry;
import com.noturn.ranks.cache.local.PlayerRanksLocalCache;
import com.noturn.ranks.dao.PlayerRanksDAO;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.command.Execution;
import me.saiintbrisson.minecraft.command.annotations.Command;
import me.saiintbrisson.minecraft.command.annotations.CommandTarget;
import me.saiintbrisson.minecraft.command.exceptions.IncorrectTargetException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class RanksCommand {

    private final PlayerRanksDAO playerRanksDAO;
    private final PlayerRanksLocalCache playerRanksLocalCache;

    private final GemsUserController gemsUserController;
    private final GemsUserDAO gemsUserDAO;

    private final Economy economy;

    @Command(
            name = "rankup",
            usage = "rankup"
    )
    public void rankUpCommand(Execution execution) {
        if (!execution.isPlayer()) {
            throw new IncorrectTargetException(CommandTarget.PLAYER);
        }

        Player player = execution.getPlayer();
        String playerName = player.getName();

        Rank rank = playerRanksLocalCache.get(playerName);

        Rank nextRank = RanksRegistry.getNext(rank);
        if (nextRank == null) {
            execution.sendMessage("§cVocê já está no rank máximo :)");
            return;
        }

        Double gems = gemsUserController.get(playerName);
        if (!economy.has(playerName, nextRank.getCoinsRequirement()) ||
                gems < nextRank.getGemsRequirement()) {

            double coins = economy.getBalance(playerName);

            execution.sendMessage(new String[]{
                    "§cVocê não tem todos os requisitos necessários para evoluir ao rank §f" +
                            nextRank.getDisplayName() + "§c.",
                    "§7§l■ §c" + NumberUtil.format(gems) + "§7/§c" + NumberUtil.format(nextRank.getGemsRequirement()),
                    "§7§l■ §c" + NumberUtil.format(coins) + "§7/§c" + NumberUtil.format(nextRank.getCoinsRequirement()),
                    ""
            });

            return;
        }

        economy.withdrawPlayer(playerName, nextRank.getCoinsRequirement());

        playerRanksLocalCache.put(playerName, nextRank);

        CompletableFuture.runAsync(() -> playerRanksDAO.insertOrUpdate(playerName, nextRank));

        Double mergedGems = gemsUserController.merge(playerName, nextRank.getGemsRequirement(), false);
        gemsUserDAO.insertOrUpdate(playerName, mergedGems);

        nextRank.getCommandsOnRankUp().replaceAll(string -> string.replace("<player>", playerName));

        for (String string : nextRank.getCommandsOnRankUp()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string);
        }

        execution.sendMessage(new String[]{
                "",
                "§aParabéns! Você evoluiu para o rank §f" + nextRank.getDisplayName() + "§a.",
                ""
        });
    }
}
