package net.lunaria.api.plugins.bukkit;

import lombok.Getter;
import net.lunaria.api.core.common.CommonManager;
import net.lunaria.api.core.connector.MongoConnector;
import net.lunaria.api.core.connector.RedisConnector;
import net.lunaria.api.core.config.Config;
import net.lunaria.api.core.redis.RedisManager;
import net.lunaria.api.plugins.bukkit.command.CommandRegister;
import net.lunaria.api.plugins.bukkit.listener.ListenerRegister;
import net.lunaria.api.plugins.bukkit.listener.player.JoinEvent;
import net.lunaria.api.plugins.bukkit.listener.player.LunaMenuEvents;
import net.lunaria.api.plugins.bukkit.listener.player.PreLoginEvent;
import net.lunaria.api.plugins.bukkit.listener.player.QuitEvent;
import net.lunaria.api.plugins.bukkit.maintenance.MaintenanceCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class BukkitAPI extends JavaPlugin {

    static BukkitAPI instance;

    private static @Getter String serverName;

    private static @Getter RedisManager redisManager;

    @Override
    public void onEnable() {
        instance = this;
        Config.setIsSpigot(true);
        saveDefaultConfig();

        ListenerRegister.registerListeners(this,
                new JoinEvent(), new PreLoginEvent(), new QuitEvent(), new LunaMenuEvents()
        );
        CommandRegister.registerCommands(this, new MaintenanceCommand());

        MongoConnector.init();
        RedisConnector.init();

        redisManager = new RedisManager();

        // Initialise les messages communs entre spigot et bungee
        CommonManager.init();
        //RedisListenersRegister.registerListeners();

        try {
            File properties = new File("server.properties");
            Properties serverProperties = new Properties();
            serverProperties.load(Files.newInputStream(properties.toPath()));

            serverName = serverProperties.getProperty("SM_NAME");
        } catch (IOException e) {
            e.printStackTrace();
        }

        redisManager.publishMessage("B_ServerManager:serverAlive", serverName);
    }

    @Override
    public void onDisable() {
        try {
            RedisManager.getInstance().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MongoConnector.disconnect();
        RedisConnector.disconnect();
    }

    public static BukkitAPI getInstance() {
        return instance;
    }

}
