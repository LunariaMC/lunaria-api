package net.lunaria.api.core;

import net.lunaria.api.core.connectors.Redis;
import net.lunaria.api.core.connectors.MongoDB;
import net.lunaria.api.core.connectors.RabbitMQ;
import net.lunaria.api.core.utils.Config;
import net.lunaria.api.plugins.bungee.config.ConfigGen;
import net.lunaria.api.plugins.bungee.listeners.Connection;
import net.lunaria.api.plugins.bungee.listeners.PingEvent;
import net.lunaria.api.plugins.bungee.utils.Maintenance;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class Bungee extends Plugin {

    private static Bungee instance;

    public void onEnable(){
        instance = this;
        Config.setIsSpigot(false);
        ConfigGen.init();
        MongoDB.init();
        RabbitMQ.init();
        Redis.init();

        initListeners();
        Maintenance.init();

    }

    public void onDisable(){
        Redis.disconnect();
        RabbitMQ.disconnect();
        MongoDB.disconnect();
    }

    public static Bungee getInstance() {
        return instance;
    }

    public void initListeners(){
        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        pluginManager.registerListener(this, new PingEvent());
        pluginManager.registerListener(this, new Connection());
    }

}
