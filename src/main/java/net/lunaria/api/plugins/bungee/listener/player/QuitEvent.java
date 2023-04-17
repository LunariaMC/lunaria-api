package net.lunaria.api.plugins.bungee.listener.player;

import net.lunaria.api.core.account.Account;
import net.lunaria.api.core.account.AccountManager;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitEvent implements Listener {
    @EventHandler
    public void onQuit(ServerDisconnectEvent event) {
        Account account = new AccountManager().getAccountFromRedis(event.getPlayer().getUniqueId());

        Long playTime = Connection.playTimeMap.get(event.getPlayer().getUniqueId());
        if (playTime != null) {
            playTime = System.currentTimeMillis() - playTime;
            Connection.playTimeMap.remove(event.getPlayer().getUniqueId());

            account.setPlayTime(account.getPlayTime() + playTime);
        }

        account.setLastSeen(System.currentTimeMillis());

        AccountManager.storeInMongo(account, event.getPlayer().getUniqueId());
    }
}
