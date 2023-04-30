package net.lunaria.api.plugins.bungee;

import lombok.Getter;
import net.lunaria.api.core.connector.RedisConnector;
import net.lunaria.api.core.connector.MongoConnector;
import net.lunaria.api.core.config.Config;
import net.lunaria.api.core.redis.RedisDBIndex;
import net.lunaria.api.core.redis.RedisListenersRegister;
import net.lunaria.api.core.redis.RedisManager;
import net.lunaria.api.core.server.Environment;
import net.lunaria.api.core.server.ServerManager;
import net.lunaria.api.core.server.ServerQueue;
import net.lunaria.api.plugins.bungee.config.ConfigGen;
import net.lunaria.api.plugins.bungee.listener.player.Connection;
import net.lunaria.api.plugins.bungee.listener.PingEvent;
import net.lunaria.api.plugins.bungee.listener.player.QuitEvent;
import net.lunaria.api.plugins.bungee.redis.ServerAliveListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.util.Objects;

public class BungeeAPI extends Plugin {

    private static BungeeAPI instance;

    private static @Getter ServerManager serverManager;

    private static @Getter boolean running = false;

    public void onEnable(){
        instance = this;
        Config.setIsSpigot(false);
        ConfigGen.init();
        MongoConnector.init();
        RedisConnector.init();

        RedisManager.clearRedisCache(RedisDBIndex.ACCOUNT_CACHE.getIndex(), RedisDBIndex.SERVER_CACHE.getIndex());

        initListeners();

        RedisListenersRegister.registerListeners(new ServerAliveListener());

        serverManager = new ServerManager();
        serverManager.init();

        for (String fileName : Objects.requireNonNull(new File("../servers/running/").list())) {
            serverManager.deleteUnknownServer(fileName);
        }

        for (Environment environment : Environment.values()) {
            new ServerQueue(environment);
        }
        ServerQueue prodQueue = ServerQueue.fromEnvironment(Environment.PROD);
        prodQueue.queueAllTemplates();
        prodQueue.startQueue(serverManager);

        running = true;
    }

    public void onDisable(){
        RedisConnector.disconnect();
        MongoConnector.disconnect();
    }

    public static BungeeAPI getInstance() {
        return instance;
    }

    public void initListeners(){
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerListener(this, new PingEvent());
        pluginManager.registerListener(this, new Connection());
        pluginManager.registerListener(this, new QuitEvent());
    }

}
