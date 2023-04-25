package net.lunaria.api.plugins.bukkit.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandRegister {

    public static void registerCommands(JavaPlugin plugin, LunaCommand... commands) {
        SimplePluginManager simplePluginManager = (SimplePluginManager) plugin.getServer().getPluginManager();

        Field field = null;

        try {
            field = simplePluginManager.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        SimpleCommandMap simpleCommandMap = null;
        try {
            simpleCommandMap = (SimpleCommandMap) field.get(simplePluginManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for (LunaCommand command : commands) {
            simpleCommandMap.register(plugin.getName(), command);
        }
    }

}
