package net.lunaria.api.plugins.spigot.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class GuiManager {
    private static GuiEventManager guiEventManager = new GuiEventManager();

    private static Map<Class<? extends GuiBuilder>, GuiBuilder> registeredMenus = new HashMap<>();

    public static void initGui(String packageName, JavaPlugin plugin) {
        try {
            File ur = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            URL[] url = { new URL("jar:file:" + ur.getPath() + "!/" + packageName.replaceAll("\\.", "/")) };
            int list = 0;
            for (Class<?> clazz : (new Reflections((new ConfigurationBuilder()).addUrls(url))).getSubTypesOf(GuiBuilder.class)) {
                if (clazz.getPackage().getName().contains(packageName))
                    try {
                        GuiBuilder guiBuilder = (GuiBuilder) clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                        guiEventManager.addMenu(guiBuilder);
                        list++;
                    } catch (InvocationTargetException|InstantiationException|IllegalAccessException|NoSuchMethodException e) {
                        System.out.println("[" + plugin.getName() + "] (initGui) : Une erreur s'est produite | Class=" + clazz.getSimpleName());
                    }
            }
            System.out.println("  [" + plugin.getName() + "]   - Initialisation de " + list + " gui(s).");
        } catch (URISyntaxException|java.net.MalformedURLException uRISyntaxException) {}
    }

    public static GuiEventManager getManager() {
        return guiEventManager;
    }

    public static Map<Class<? extends GuiBuilder>, GuiBuilder> getRegisteredMenus() {
        return registeredMenus;
    }
}