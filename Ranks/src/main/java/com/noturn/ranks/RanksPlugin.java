package com.noturn.ranks;

import com.noturn.gems.NoturnGemsPlugin;
import com.noturn.ranks.cache.local.PlayerRanksLocalCache;
import com.noturn.ranks.commands.RanksCommand;
import com.noturn.ranks.dao.PlayerRanksDAO;
import com.noturn.ranks.database.MysqlDatabase;
import com.noturn.ranks.listeners.PlayerJoinListener;
import com.noturn.ranks.loader.RanksLoader;
import lombok.Getter;
import me.saiintbrisson.minecraft.command.CommandFrame;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class RanksPlugin extends JavaPlugin {

    public static RanksPlugin INSTANCE;

    @Getter
    private MysqlDatabase mysqlDatabase;

    @Getter
    private PlayerRanksDAO playerRanksDAO;

    @Getter
    private PlayerRanksLocalCache playerRanksCache;

    @Getter
    private Economy economy;

    @Override
    public void onEnable() {
        super.onEnable();

        INSTANCE = this;

        saveDefaultConfig();

        economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();

        new RanksLoader().load(getConfig());

        createDatabase();

        playerRanksDAO = new PlayerRanksDAO(mysqlDatabase);
        playerRanksDAO.createTable();

        playerRanksCache = new PlayerRanksLocalCache();

        CommandFrame frame = new CommandFrame(this);
        frame.setErrorMessage("§cUm erro ocorreu! {error}");
        frame.setLackPermMessage("§cVocê não possui permissão para executar este comando.");
        frame.setUsageMessage("§cUtilize /{usage}.");
        frame.setIncorrectTargetMessage("§cVocê não pode utilizar este comando pois ele é direcionado apenas para {target}.");

        frame.register(
                new RanksCommand(
                        playerRanksDAO,
                        playerRanksCache,
                        NoturnGemsPlugin.INSTANCE.getUserController(),
                        NoturnGemsPlugin.INSTANCE.getUserDao(),
                        economy
                )
        );

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(playerRanksDAO, playerRanksCache), this);

    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (mysqlDatabase != null) {
            mysqlDatabase.closeConnection();
        }
    }

    private void createDatabase() {
        ConfigurationSection section = getConfig().getConfigurationSection("database");

        InetSocketAddress inetSocketAddress = new InetSocketAddress(
                section.getString("address"),
                section.getInt("port")
        );

        String databaseName = section.getString("name");
        String username = section.getString("username");
        String password = section.getString("password");

        mysqlDatabase = new MysqlDatabase(inetSocketAddress, username, password, databaseName);
        mysqlDatabase.openConnection();

        getLogger().fine("Conexão MySQL aberta!");
    }
}
