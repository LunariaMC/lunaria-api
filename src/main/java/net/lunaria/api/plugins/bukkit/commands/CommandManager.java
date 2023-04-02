package net.lunaria.api.plugins.bukkit.commands;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class CommandManager {
    public static void initCommands(String packageName, JavaPlugin plugin) {
        CommandMap map = null;
        try {
            Field cmdMap = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            cmdMap.setAccessible(true);
            map = (CommandMap)cmdMap.get(plugin.getServer().getPluginManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HashSet<Command> coms = new HashSet<>();
        Set<Class<? extends Command>> commands = (new Reflections(packageName).getSubTypesOf(Command.class));
        int list = 0;
        for (Class<? extends Command> cls : commands) {
            if (cls.getPackage().getName().contains(packageName))
                try {
                    Command command = cls.getDeclaredConstructor(new Class[] { String.class }).newInstance(new Object[] { cls.getSimpleName().toLowerCase() });
                    assert map != null;
                    map.register("", command);
                    coms.add(command);
                    list++;
                } catch (Exception e) {
                    System.out.println("[" + plugin.getName() + "] (initGui) : Une erreur s'est produite | Class=" + cls.getSimpleName());
                }
        }
        System.out.println("  [" + plugin.getName() + "]   - Initialisation de " + list + " commande(s).");
        genHelp(coms, plugin);
    }

    private static void genHelp(Collection<Command> cmds, JavaPlugin javaPlugin) {
        List<HelpTopic> topics = new ArrayList<>();
        for (Command c : cmds)
            topics.add(new GenericCommandHelpTopic(c));
        Bukkit.getHelpMap().addTopic((HelpTopic)new IndexHelpTopic(javaPlugin
                .getName(), javaPlugin
                .getName() + " description", javaPlugin
                .getName().toLowerCase(Locale.ROOT) + ".permission.help", topics, javaPlugin

                .getName() + " Help"));
    }
}
