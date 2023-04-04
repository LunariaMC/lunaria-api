package net.lunaria.api.plugins.bukkit.listeners.player;

import net.lunaria.api.plugins.bukkit.player.BukkitPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        BukkitPlayer bukkitPlayer = BukkitPlayer.fromPlayer(event.getPlayer());

        bukkitPlayer.setPlayer(event.getPlayer());
    }
}
