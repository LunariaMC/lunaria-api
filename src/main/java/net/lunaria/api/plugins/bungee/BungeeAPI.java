package net.lunaria.api.plugins.bungee;

import lombok.Getter;
import net.lunaria.api.core.connectors.RedisConnector;
import net.lunaria.api.core.connectors.MongoConnector;
import net.lunaria.api.core.config.Config;
import net.lunaria.api.core.servers.Environment;
import net.lunaria.api.core.servers.ServerManager;
import net.lunaria.api.core.servers.ServerQueue;
import net.lunaria.api.plugins.bungee.config.ConfigGen;
import net.lunaria.api.plugins.bungee.listeners.Connection;
import net.lunaria.api.plugins.bungee.listeners.PingEvent;
import net.lunaria.api.plugins.bungee.maintenance.Maintenance;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeAPI extends Plugin {

    private static BungeeAPI instance;

    private static @Getter ServerManager serverManager;

    public void onEnable(){
        instance = this;
        Config.setIsSpigot(false);
        ConfigGen.init();
        MongoConnector.init();
        RedisConnector.init();

        initListeners();
        Maintenance.init();

        serverManager = new ServerManager();
        serverManager.init();

        for (Environment environment : Environment.values()) {
            new ServerQueue(environment);
        }
        ServerQueue prodQueue = ServerQueue.fromEnvironment(Environment.PROD);
        prodQueue.queueAllTemplates();
        prodQueue.startQueue(serverManager);
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
    }

}
