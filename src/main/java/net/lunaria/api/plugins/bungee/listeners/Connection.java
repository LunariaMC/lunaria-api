package net.lunaria.api.plugins.bungee.listeners;

import net.lunaria.api.core.enums.Prefixs;
import net.lunaria.api.plugins.bungee.maintenance.Maintenance;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Connection implements Listener {

    @EventHandler
    public void onLogin(LoginEvent e){
        PendingConnection player = e.getConnection();
        if(Maintenance.isActive()){
            if(!Maintenance.getUsers().contains(player.getName())){
                String bar = "§6§m--------------------------------------------";
                player.disconnect(new TextComponent(bar + "\n§e\n" + Prefixs.MAINTENANCE.getPrefix() + "Une erreur s'est produite !\n§e\n§fLe serveur est en §6maintenance§f !\n§e\n§c§l⚠ §fSi c'est une erreur,\n§fcontactez l'§4administration§f.\n§e\n" + bar));
            }
        }
    }

}
