package com.noturn.ranks.listeners;

import com.noturn.ranks.Rank;
import com.noturn.ranks.TaskManager;
import com.noturn.ranks.cache.local.PlayerRanksLocalCache;
import com.noturn.ranks.dao.PlayerRanksDAO;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final PlayerRanksDAO playerRanksDAO;
    private final PlayerRanksLocalCache playerRanksLocalCache;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }

        String userName = event.getName();

        Rank rank = playerRanksDAO.fetch(userName);

        Future<Rank> rankFuture = TaskManager.callSync(() -> playerRanksLocalCache.put(userName, rank));

        try {
            rankFuture.get();
        } catch (final InterruptedException | ExecutionException e) {
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    "§cHouve um erro ao carregar seus dados.");
        }
    }
}
