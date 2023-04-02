package net.lunaria.api.plugins.bukkit.listeners.player;

import net.lunaria.api.plugins.bukkit.player.BukkitPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BukkitPlayer bukkitPlayer = BukkitPlayer.fromPlayer(event.getPlayer());
        bukkitPlayer.uncache();
    }
}
