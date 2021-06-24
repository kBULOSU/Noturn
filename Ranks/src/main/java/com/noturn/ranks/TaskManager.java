package com.noturn.ranks;

import org.bukkit.Bukkit;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TaskManager {

    public static <T> Future<T> callSync(final Callable<T> callable) {
        return Bukkit.getScheduler().callSyncMethod(RanksPlugin.INSTANCE, callable);
    }
}