package net.lunaria.api.plugins.bukkit.maintenance;

import net.lunaria.api.core.enums.Prefix;
import net.lunaria.api.core.enums.Rank;
import net.lunaria.api.core.maintenance.MaintenanceManager;
import net.lunaria.api.plugins.bukkit.command.LunaCommand;
import net.lunaria.api.plugins.bukkit.command.subcommand.SubCommand;
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

    @SubCommand(arg = "add %player_name%", description = "Ajouter un joueur à la maintenance.")
    void add(String targetName) {
        if (MaintenanceManager.getMaintenance().getNameWhitelist().contains(targetName)) {
            player.sendMessage(prefix + "Le joueur " + color + targetName + "§f est déjà dans la maintenance.");
            return;
        }

        MaintenanceManager.getMaintenance().getNameWhitelist().add(targetName);
        MaintenanceManager.getInstance().updateCacheEverywhere();
        player.sendMessage(prefix + "Le joueur " + color + targetName + "§f a été §aajouté§f à la maintenance.");
    }

    @SubCommand(arg = "remove %player_name%", description = "Retirer un joueur de la maintenance.")
    void remove(String targetName) {
        if (!MaintenanceManager.getMaintenance().getNameWhitelist().contains(targetName)) {
            player.sendMessage(prefix + "Le joueur " + color + targetName + "§f n'est pas dans la maintenance.");
            return;
        }

        MaintenanceManager.getMaintenance().getNameWhitelist().remove(targetName);
        MaintenanceManager.getInstance().updateCacheEverywhere();
        player.sendMessage(prefix + "Le joueur " + color + targetName + "§f a été §cretiré§f de la maintenance.");
    }

    @SubCommand(arg = "set %boolean%", description = "Changer le statut de la maintenance.")
    void set(boolean status) {
        MaintenanceManager.getMaintenance().setActive(status);
        MaintenanceManager.getInstance().updateCacheEverywhere();
        player.sendMessage(prefix + "Le statut de la maintenance est désormais: " + color + status);
    }
}
