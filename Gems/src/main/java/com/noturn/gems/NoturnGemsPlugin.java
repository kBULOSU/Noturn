package com.noturn.gems;

import com.noturn.gems.commands.GemsCommand;
import com.noturn.gems.controller.GemsUserController;
import com.noturn.gems.dao.GemsUserDAO;
import com.noturn.gems.database.MysqlDatabase;
import com.noturn.gems.listeners.EntityListeners;
import com.noturn.gems.listeners.item.GemItemListener;
import com.noturn.gems.listeners.player.PlayerConnectionListener;
import com.noturn.gems.misc.placeholder.GemPlaceHolder;
import lombok.Getter;
import me.saiintbrisson.minecraft.command.CommandFrame;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;

public class NoturnGemsPlugin extends JavaPlugin {

    public static NoturnGemsPlugin INSTANCE;

    @Getter
    private MysqlDatabase mysqlDatabase;

    @Getter
    private GemsUserDAO userDao;

    @Getter
    private GemsUserController userController;

    @Getter
    private Economy economy;

    @Override
    public void onEnable() {
        super.onEnable();

        INSTANCE = this;

        saveDefaultConfig();

        createDatabase();

        userDao = new GemsUserDAO(mysqlDatabase);
        userDao.createTable();

        userController = new GemsUserController();

        economy = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

        new GemPlaceHolder(userController).register();

        CommandFrame frame = new CommandFrame(this);
        frame.setErrorMessage("§cUm erro ocorreu! {error}");
        frame.setLackPermMessage("§cVocê não possui permissão para executar este comando.");
        frame.setUsageMessage("§cUtilize /{usage}.");
        frame.setIncorrectTargetMessage("§cVocê não pode utilizar este comando pois ele é direcionado apenas para {target}.");

        frame.register(new GemsCommand(userController));

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerConnectionListener(userDao, userController), this);
        pluginManager.registerEvents(new EntityListeners(), this);
        pluginManager.registerEvents(new GemItemListener(userController), this);

        System.out.println("Plugin de gemas ligado!");
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

        System.out.println("Conexão MySQL aberta!");
    }


}
