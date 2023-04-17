package net.lunaria.api.plugins.bukkit.listener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerRegister {
    public static void registerListeners(JavaPlugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

}
