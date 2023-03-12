package net.lunaria.api.plugins.bungee.listeners;

import net.lunaria.api.plugins.bungee.utils.Maintenance;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Connection implements Listener {

    @EventHandler
    public void loginEvent(LoginEvent e){
        PendingConnection player = e.getConnection();
        if(Maintenance.isActive()){
            if(!Maintenance.getUsers().contains(player.getName())){
                String bar = "§6§m--------------------------------------------";
                player.disconnect(new TextComponent(bar + "\n§e\n" + "§6Maintenance §8❘ §fUne erreur s'est produite !\n§e\n§7Vous ne faites pas partis de la maintenance !\n§e\n§c⚠ §7Si c'est une erreur, contactez l'administration.\n§e\n" + bar));
            }
        }
    }

}
