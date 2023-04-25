package net.lunaria.api.plugins.bukkit.command;

import edu.emory.mathcs.backport.java.util.Arrays;
import lombok.Setter;
import net.lunaria.api.core.account.Subscription;
import net.lunaria.api.core.enums.Rank;
import net.lunaria.api.core.enums.Symbol;
import net.lunaria.api.plugins.bukkit.player.BukkitPlayer;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;

public abstract class LunaCommand extends BukkitCommand implements TabCompleter {

    protected final String name;
    protected final String[] aliases;

    protected @Setter Class<? extends CommandSender> commandSender = CommandSender.class;
    protected int requiredPower;
    protected boolean staffByPass;
    protected BukkitPlayer bukkitPlayer;
    protected Player player;
    protected Predicate<Subscription> subCondition;

    public LunaCommand(String name, int requiredPower, Predicate<Subscription> subCondition, boolean staffByPass, String... aliases) {
        super(name, "", "", Arrays.asList(aliases));
        this.name = name;
        this.aliases = aliases;
        this.requiredPower = requiredPower;
        this.staffByPass = staffByPass;
        this.subCondition = subCondition;
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

        for (Method method : this.getClass().getDeclaredMethods()) {
            method.setAccessible(true);

            if (!method.isAnnotationPresent(SubCommand.class)) continue;
            SubCommand subCommand = method.getAnnotation(SubCommand.class);

            if (args.length - 1 >= subCommand.position() && args[subCommand.position()].equalsIgnoreCase(subCommand.arg())) {
                try {
                    method.invoke(this);
                    break;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }

        return true;
    }

    public abstract boolean onCommand(CommandSender sender, String label, String[] args);
    public abstract void sendHelp();

    public void sendHelpSubCommand(String command, String args, ChatColor color, ChatColor accentColor, String description) {
        TextComponent tooltip = new TextComponent(color + "[? Aide]");
        tooltip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(accentColor + "❘§f " + description).create()));

        TextComponent execute = new TextComponent("§a[» Exécuter]");
        execute.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a❘§f Cliquez ici pour §aexécuter§f la commande " + color + "/" + command + " " + args).create()));
        execute.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + command + " " + args));

        player.spigot().sendMessage(new TextComponent(" §8§l❘ " + color + "/" + command + " " + accentColor + args + " §8§l"+ Symbol.SQUARE.getSymbol()+" "), tooltip , new TextComponent(" "), execute);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
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
