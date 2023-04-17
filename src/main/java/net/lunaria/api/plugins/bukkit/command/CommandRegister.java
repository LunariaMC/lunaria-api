package net.lunaria.api.plugins.bukkit.command;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CommandRegister {

    public static void registerCommands(JavaPlugin plugin, LunaCommand... listeners) {
        for (LunaCommand command : listeners) {
            PluginCommand pl = plugin.getCommand(command.name);
            pl.setExecutor(command);
            pl.setAliases(Arrays.asList(command.aliases));
        }
    }

}
