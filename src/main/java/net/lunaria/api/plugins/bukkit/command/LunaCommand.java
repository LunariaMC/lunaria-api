package net.lunaria.api.plugins.bukkit.command;

import edu.emory.mathcs.backport.java.util.Arrays;
import lombok.Setter;
import net.lunaria.api.core.account.Subscription;
import net.lunaria.api.core.enums.Prefix;
import net.lunaria.api.core.enums.Rank;
import net.lunaria.api.core.enums.Symbol;
import net.lunaria.api.plugins.bukkit.player.BukkitPlayer;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class LunaCommand extends BukkitCommand implements TabCompleter {

    protected final String name;
    protected final String[] aliases;

    protected String prefix;
    protected ChatColor color;
    protected ChatColor accentColor;

    protected @Setter Class<? extends CommandSender> commandSender = CommandSender.class;
    protected int requiredPower;
    protected boolean staffByPass;
    protected BukkitPlayer bukkitPlayer;
    protected Player player;
    protected Predicate<Subscription> subCondition;

    private Method[] methods;

    public LunaCommand(String name, String prefix, ChatColor color, ChatColor accentColor, int requiredPower, Predicate<Subscription> subCondition, boolean staffByPass, String... aliases) {
        super(name, "", "", Arrays.asList(aliases));
        this.name = name;
        this.aliases = aliases;
        this.requiredPower = requiredPower;
        this.staffByPass = staffByPass;
        this.subCondition = subCondition;
        this.prefix = prefix;
        this.color = color;
        this.accentColor = accentColor;

        this.methods = this.getClass().getDeclaredMethods();
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!commandSender.isAssignableFrom(sender.getClass())) return false;

        if (!(sender instanceof Player)) return false;
        player = (Player) sender;
        bukkitPlayer = BukkitPlayer.fromPlayer(player);

        if (bukkitPlayer.getPowerMod() < requiredPower){
            errorMessage(player, false, requiredPower);
            return false;
        }
        if (subCondition != null) {
            if (!subCondition.test(bukkitPlayer.getSubscription()) && !staffByPass) {
                errorMessage(player, true, requiredPower);
                return false;
            }
        }

        onCommand(sender, label, args);

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp();
            return true;
        }

        methodLoop: for (Method method : methods) {
            method.setAccessible(true);

            if (!method.isAnnotationPresent(SubCommand.class)) continue;
            SubCommand subCommand = method.getAnnotation(SubCommand.class);

            String fullCommand = getComplexCommandSuggestion(label, subCommand.arg());

            String[] splitArgs = subCommand.arg().split(" ");
            ArrayList<Object> complexArgsList = new ArrayList<>();
            int index = -1;

            for (String s : splitArgs) {
                index++;

                if (index > args.length - 1) {
                    syntaxError(fullCommand);
                    return false;
                }

                if (!s.contains("%")){
                    if (!args[index].equalsIgnoreCase(s)) {
                        continue methodLoop;
                    }
                    continue;
                }

                s = s.replace("%","");

                SubCommandComplexTypes subCommandComplexTypes = SubCommandComplexTypes.valueOf(s.toUpperCase());

                if (!subCommandComplexTypes.commandComplexType.runWhenCalled(args, complexArgsList, index)) {
                    syntaxError(fullCommand);
                    return false;
                }
            }
            try {
                if (complexArgsList.size() == 0) method.invoke(this);
                else method.invoke(this, complexArgsList.toArray());

                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        sendHelp();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    public abstract boolean onCommand(CommandSender sender, String label, String[] args);


    private String getComplexCommandSuggestion(@Nullable String command, String complexStructure) {
        StringBuilder stringBuilder = new StringBuilder();
        if (command != null) stringBuilder.append(command).append(" ");

        for (String s : complexStructure.split(" ")) {
            if (!s.contains("%")) {
                stringBuilder.append(s).append(" ");
                continue;
            }

            s = s.replace("%", "");
            stringBuilder.append(SubCommandComplexTypes.valueOf(s.toUpperCase()).helpArg).append(" ");
        }
        return stringBuilder.toString();
    }

    public void sendHelp() {
        player.sendMessage(" ");
        player.sendMessage(this.prefix + "Liste des sous-commandes:");
        player.sendMessage(" ");
        for (Method method : methods) {
            if (!method.isAnnotationPresent(SubCommand.class)) continue;
            SubCommand subCommand = method.getAnnotation(SubCommand.class);

            sendHelpSubCommand(name, getComplexCommandSuggestion(null, subCommand.arg()), color, accentColor, subCommand.description());
        }
        player.sendMessage(" ");
    }
    public void sendHelpSubCommand(String command, String args, ChatColor color, ChatColor accentColor, String description) {
        TextComponent tooltip = new TextComponent("§b[? Aide]");
        tooltip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§b❘§f " + description).create()));

        TextComponent execute = new TextComponent("§a[» Exécuter]");
        execute.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a❘§f Cliquez ici pour §aexécuter§f la commande " + color + "/" + command + " " + args).create()));
        execute.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + command + " " + args));

        player.spigot().sendMessage(new TextComponent(" §8❘ " + color + "/" + command + " " + accentColor + args + "§8"+ Symbol.SQUARE.getSymbol()+" "), tooltip , new TextComponent(" "), execute);
    }
    public void syntaxError(String suggest) {
        String bar = "§8 ❘";
        player.sendMessage("§e \n" +
                "§c§lERREUR §8❘ §fSyntaxe incorrecte\n" +
                "§e \n" +
                bar + "§f Utilisation correcte: §c/" + suggest + "\n" +
                "§e ");
    }
    public void errorMessage(Player player, boolean forSubscribed, int requiredPower){
        String bar = "§8§l ❘ ";
        if(forSubscribed){
            player.sendMessage("§e \n" +
                "§c§lERREUR §8❘ §fPermissions insuffisantes...\n" +
                "§e \n" +
                bar + "§fL'§cabonnement §fest requis.\n" +
                "§e ");
        } else {
            player.sendMessage("§e \n" +
                    "§c§lERREUR §8❘ §fPermissions insuffisantes...\n" +
                    "§e \n" +
                    bar + "§fGrade requis: "+Rank.fromPower(requiredPower).getPrefix()+"\n" +
                    "§e ");
        }
    }
}
