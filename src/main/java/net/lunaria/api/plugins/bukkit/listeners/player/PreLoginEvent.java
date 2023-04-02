package net.lunaria.api.plugins.bukkit.listeners.player;

import net.lunaria.api.core.account.Account;
import net.lunaria.api.core.account.AccountManager;
import net.lunaria.api.plugins.bukkit.player.BukkitPlayer;
import net.lunaria.api.plugins.bukkit.player.BukkitPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import java.util.UUID;

public class PreLoginEvent implements Listener {
    @EventHandler
    public void onPreLogin(PlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        BukkitPlayer bukkitPlayer = new BukkitPlayerManager().getAccountFromRedis(uuid);
        bukkitPlayer.setUuid(uuid);
        bukkitPlayer.cache(uuid);
    }
}
