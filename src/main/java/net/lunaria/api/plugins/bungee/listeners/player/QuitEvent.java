package net.lunaria.api.plugins.bungee.listeners.player;

import net.lunaria.api.core.account.Account;
import net.lunaria.api.core.account.AccountManager;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitEvent implements Listener {
    @EventHandler
    public void onQuit(ServerDisconnectEvent event) {
        Account account = new AccountManager().getAccountFromRedis(event.getPlayer().getUniqueId());

        account.setLastSeen(System.currentTimeMillis());

        AccountManager.storeInMongo(account, event.getPlayer().getUniqueId());
    }
}
