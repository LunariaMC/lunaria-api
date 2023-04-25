package net.lunaria.api.plugins.bukkit.maintenance;

import net.lunaria.api.core.enums.Prefix;
import net.lunaria.api.core.enums.Rank;
import net.lunaria.api.plugins.bukkit.command.LunaCommand;
import net.lunaria.api.plugins.bukkit.command.SubCommand;
import net.lunaria.api.plugins.bungee.maintenance.MaintenanceManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MaintenanceCommand extends LunaCommand {
    public MaintenanceCommand() {
        super("maintenance", Rank.ADMIN.getPowerMod(), null, false, "mtn");
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        return true;
    }

    @Override
    public void sendHelp() {
        player.sendMessage(" ");
        player.sendMessage(Prefix.MAINTENANCE.getPrefix() + "Liste des sous-commandes:");
        player.sendMessage(" ");
        sendHelpSubCommand("maintenance", "list", ChatColor.YELLOW, ChatColor.GOLD, "Afficher la liste des joueurs dans la maintenance.");
        sendHelpSubCommand("maintenance", "add <joueur>", ChatColor.YELLOW, ChatColor.GOLD, "Ajouter un joueur à la maintenance.");
        sendHelpSubCommand("maintenance", "remove <joueur>", ChatColor.YELLOW, ChatColor.GOLD, "Retirer un joueur de la maintenance.");
        sendHelpSubCommand("maintenance", "set <on/off>", ChatColor.YELLOW, ChatColor.GOLD, "Changer le statut de la maintenance.");
        player.sendMessage(" ");
    }

    @SubCommand(arg = "list", position = 0)
    void list() {
        player.sendMessage(" ");
        player.sendMessage(Prefix.MAINTENANCE.getPrefix() + "Liste de la maintenance: §e(" + MaintenanceManager.getPlayerWhitelist().size() + ")");
        player.sendMessage(" ");
        for (String playerName : MaintenanceManager.getPlayerWhitelist()) {
            player.sendMessage("§e » §f" + playerName);
        }
        player.sendMessage(" ");
    }
}
