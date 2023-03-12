package net.lunaria.api.core;

import net.lunaria.api.core.connectors.MongoDB;
import net.lunaria.api.core.connectors.RabbitMQ;
import net.lunaria.api.core.connectors.Redis;
import net.lunaria.api.core.utils.Config;
import net.lunaria.api.plugins.spigot.utils.CommandManager;
import net.lunaria.api.plugins.spigot.utils.GuiManager;
import net.lunaria.api.plugins.spigot.utils.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class API extends JavaPlugin {

    static API instance;

    @Override
    public void onEnable() {
        instance = this;
        Config.setIsSpigot(true);
        saveDefaultConfig();

        ListenerManager.initListeners(Config.packagePlugin, this);
        CommandManager.initCommands(Config.packagePlugin, this);
        GuiManager.initGui(Config.packagePlugin, this);

        MongoDB.init();
        Redis.init();
        RabbitMQ.init();
    }

    @Override
    public void onDisable() {
        MongoDB.disconnect();
        RabbitMQ.disconnect();
    }

    public static API getInstance() {
        return instance;
    }

}
