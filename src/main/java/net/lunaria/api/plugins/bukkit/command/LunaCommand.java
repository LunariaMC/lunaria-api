package net.lunaria.api.plugins.bukkit.command;

import lombok.Setter;
import net.lunaria.api.core.account.Subscription;
import net.lunaria.api.core.enums.Prefix;
import net.lunaria.api.core.enums.Rank;
import net.lunaria.api.core.enums.Symbol;
import net.lunaria.api.plugins.bukkit.player.BukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class LunaCommand implements CommandExecutor, TabCompleter {

    protected final String name;
    protected final String[] aliases;

    protected @Setter Class<? extends CommandSender> commandSender = CommandSender.class;
    protected int requiredPower;
    protected boolean forSubscribed;
    protected boolean staffByPass;
    protected BukkitPlayer bukkitPlayer;

    public LunaCommand(String name, int requiredPower, boolean forSubscribed, boolean staffByPass, String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.requiredPower = requiredPower;
        this.forSubscribed = forSubscribed;
        this.staffByPass = staffByPass;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (commandSender.isAssignableFrom(sender.getClass())) {

            if(sender instanceof Player){
                Player player = (Player) sender;
                bukkitPlayer = BukkitPlayer.fromPlayer(player);
                if(bukkitPlayer.getPowerMod() < requiredPower){
                    errorMessage(player, forSubscribed, requiredPower);
                    return false;
                } else if(forSubscribed && !staffByPass){
                    if(!bukkitPlayer.getSubscription().isSubscribed()){
                        errorMessage(player, true, requiredPower);
                        return false;
                    }
                }
                return true;
            }

        }

        return false;
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
