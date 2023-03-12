package net.lunaria.api.plugins.bukkit;

import net.lunaria.api.core.connectors.MongoConnector;
import net.lunaria.api.core.connectors.RabbitConnector;
import net.lunaria.api.core.connectors.RedisConnector;
import net.lunaria.api.core.config.Config;
import net.lunaria.api.plugins.bukkit.commands.CommandManager;
import net.lunaria.api.plugins.bukkit.menus.GuiManager;
import net.lunaria.api.plugins.bukkit.listeners.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitAPI extends JavaPlugin {

    static BukkitAPI instance;

    @Override
    public void onEnable() {
        instance = this;
        Config.setIsSpigot(true);
        saveDefaultConfig();

        ListenerManager.initListeners(Config.packagePlugin, this);
        CommandManager.initCommands(Config.packagePlugin, this);
        GuiManager.initGui(Config.packagePlugin, this);

        MongoConnector.init();
        RedisConnector.init();
        RabbitConnector.init();
    }

    @Override
    public void onDisable() {
        MongoConnector.disconnect();
        RabbitConnector.disconnect();
    }

    public static BukkitAPI getInstance() {
        return instance;
    }

}
