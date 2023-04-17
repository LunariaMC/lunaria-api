package net.lunaria.api.plugins.bukkit;

import lombok.Getter;
import net.lunaria.api.core.connector.MongoConnector;
import net.lunaria.api.core.connector.RedisConnector;
import net.lunaria.api.core.config.Config;
import net.lunaria.api.core.redis.RedisMessage;
import net.lunaria.api.plugins.bukkit.listener.ListenerRegister;
import net.lunaria.api.plugins.bukkit.listener.player.JoinEvent;
import net.lunaria.api.plugins.bukkit.listener.player.LunaMenuEvents;
import net.lunaria.api.plugins.bukkit.listener.player.PreLoginEvent;
import net.lunaria.api.plugins.bukkit.listener.player.QuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class BukkitAPI extends JavaPlugin {

    static BukkitAPI instance;

    private static @Getter String serverName;

    @Override
    public void onEnable() {
        instance = this;
        Config.setIsSpigot(true);
        saveDefaultConfig();

        ListenerRegister.registerListeners(this,
                new JoinEvent(), new PreLoginEvent(), new QuitEvent(), new LunaMenuEvents()
        );

        MongoConnector.init();
        RedisConnector.init();

        //RedisListenersRegister.registerListeners();

        try {
            File properties = new File("server.properties");
            Properties serverProperties = new Properties();
            serverProperties.load(Files.newInputStream(properties.toPath()));

            serverName = serverProperties.getProperty("SM_NAME");
        } catch (IOException e) {
            e.printStackTrace();
        }

        new RedisMessage("Bukkit:ServerManager:aliveSignal").publish(serverName);
    }

    @Override
    public void onDisable() {
        MongoConnector.disconnect();
        RedisConnector.disconnect();
    }

    public static BukkitAPI getInstance() {
        return instance;
    }

}
