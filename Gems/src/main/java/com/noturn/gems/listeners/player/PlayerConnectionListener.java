package com.noturn.gems.listeners.player;

import com.noturn.gems.TaskManager;
import com.noturn.gems.controller.GemsUserController;
import com.noturn.gems.dao.GemsUserDAO;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final GemsUserDAO userDAO;
    private final GemsUserController userController;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }

        final String name = event.getName();

        final Double fetchedGems = userDAO.fetch(name);

        final Future<?> future = TaskManager.callSync(() -> userController.put(name, fetchedGems));

        try {
            future.get();
        } catch (final InterruptedException | ExecutionException e) {
            e.printStackTrace();
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    "§cHouve um erro ao carregar seus dados.");
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        final String name = event.getPlayer().getName();

        final Double gems = userController.remove(name);

        if (gems != null) {
            CompletableFuture.runAsync(() -> userDAO.insertOrUpdate(name, gems));
        }
    }
}
