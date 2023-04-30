package net.lunaria.api.plugins.bukkit.maintenance;

import net.lunaria.api.core.enums.Prefix;
import net.lunaria.api.core.enums.Rank;
import net.lunaria.api.core.maintenance.MaintenanceManager;
import net.lunaria.api.plugins.bukkit.command.LunaCommand;
import net.lunaria.api.plugins.bukkit.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaintenanceCommand extends LunaCommand {
    public MaintenanceCommand() {
        super("maintenance", Prefix.MAINTENANCE.getPrefix(), ChatColor.YELLOW, ChatColor.GOLD, Rank.ADMIN.getPowerMod(), null, false, "mtn");
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        return true;
    }

    @SubCommand(arg = "list", description = "Afficher la liste des joueurs dans la maintenance.")
    void list() {
        player.sendMessage(" ");
        player.sendMessage(prefix + "Liste de la maintenance: "+color+"(" + MaintenanceManager.getMaintenance().getNameWhitelist().size() + ")");
        player.sendMessage(" ");
        for (String playerName : MaintenanceManager.getMaintenance().getNameWhitelist()) {
            player.sendMessage(color+" » §f" + playerName);
        }
        player.sendMessage(" ");
    }

    @SubCommand(arg = "add %player%", description = "Ajouter un joueur à la maintenance.")
    void add(Player target) {
        player.sendMessage("DONE");
        target.sendMessage("DONE2");
    }
    @SubCommand(arg = "remove %player% %string% %string_builder%", description = "Retirer un joueur de la maintenance.")
    void remove(Player target, String text, String text1) {
        player.sendMessage("DONE");
        target.sendMessage("DONE2 " + text + ".\n" + text1);
    }
    @SubCommand(arg = "test %string% one %int%", description = "hehe boy.")
    void test(String text, Integer i) {
        player.sendMessage("DONE " + text + i);
    }
    @SubCommand(arg = "test %string% two %int%", description = "hehe boy2.")
    void test1(String text, Integer i) {
        player.sendMessage("DONE " + text + i);
    }
}
