package net.lunaria.api.plugins.bukkit.listeners;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;

import net.lunaria.api.plugins.bukkit.menus.GuiEventManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class ListenerManager {
    public static void initListeners(String packageName, JavaPlugin plugin) {
        try {
            int list = 0;
            File ur = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            URL[] url = { new URL("jar:file:" + ur.getPath() + "!/" + packageName.replaceAll("\\.", "/")) };
            for (Class<?> clazz : (new Reflections((new ConfigurationBuilder()).addUrls(url))).getSubTypesOf(Listener.class)) {
                String packClass = clazz.getPackage().getName();
                if (packClass.contains("lunalib")) {
                    plugin.getServer().getPluginManager().registerEvents((Listener)new GuiEventManager(), (Plugin)plugin);
                    list++;
                }
                if (packClass.contains(packageName))
                    try {
                        Listener listener = (Listener) clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                        plugin.getServer().getPluginManager().registerEvents(listener, (Plugin)plugin);
                        list++;
                    } catch (InvocationTargetException|InstantiationException|IllegalAccessException|NoSuchMethodException e) {
                        System.out.println("[" + plugin.getName() + "] (initGui) : Une erreur s'est produite | Class=" + clazz.getSimpleName());
                    }
            }
            System.out.println("  [" + plugin.getName() + "]   - Initialisation de " + list + " listener(s).");
        } catch (URISyntaxException|java.net.MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
