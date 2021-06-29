package com.noturn.spawners;

import com.noturn.spawners.cache.local.SpawnersLocalCache;
import com.noturn.spawners.dao.SpawnersDAO;
import com.noturn.spawners.database.MysqlDatabase;
import lombok.Getter;
import me.saiintbrisson.minecraft.command.CommandFrame;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class SpawnersPlugin extends JavaPlugin {

    public SpawnersPlugin INSTANCE;

    @Getter
    private MysqlDatabase mysqlDatabase;

    @Getter
    private SpawnersLocalCache spawnersLocalCache;

    @Getter
    private SpawnersDAO spawnersDAO;

    @Override
    public void onEnable() {
        super.onEnable();

        INSTANCE = this;

        saveDefaultConfig();

        createDatabase();

        spawnersDAO = new SpawnersDAO(mysqlDatabase);
        spawnersDAO.createTable();

        spawnersLocalCache = new SpawnersLocalCache(spawnersDAO);

        CommandFrame frame = new CommandFrame(this);
        frame.setErrorMessage("§cUm erro ocorreu! {error}");
        frame.setLackPermMessage("§cVocê não possui permissão para executar este comando.");
        frame.setUsageMessage("§cUtilize /{usage}.");
        frame.setIncorrectTargetMessage("§cVocê não pode utilizar este comando pois ele é direcionado apenas para {target}.");
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
