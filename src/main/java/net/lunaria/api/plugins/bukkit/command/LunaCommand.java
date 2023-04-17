package net.lunaria.api.plugins.bukkit.command;

import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class LunaCommand implements CommandExecutor, TabCompleter {

    protected final String name;
    protected final String[] aliases;

    protected @Setter Class<? extends CommandSender> commandSender = CommandSender.class;
    protected int requiredPower;

    public LunaCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (commandSender.isAssignableFrom(sender.getClass())) {

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
