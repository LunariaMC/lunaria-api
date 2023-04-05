package net.lunaria.api.plugins.bukkit.listeners.player;

import net.lunaria.api.plugins.bukkit.menu.LunaMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LunaMenuEvents implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        LunaMenu lunaMenu = LunaMenu.fromPlayer(player);

        if (event.getInventory().getHolder() == lunaMenu) {
            event.setCancelled(true);
            lunaMenu.onClick(event, player);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        LunaMenu lunaMenu = LunaMenu.fromPlayer(player);

        if (lunaMenu == null) return;

        if (event.getInventory().getHolder() == lunaMenu) {
            lunaMenu.onClose(player);
            LunaMenu.deletePlayer(player);
        }
    }

    @EventHandler
    public void onInventoryQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LunaMenu lunaMenu = LunaMenu.fromPlayer(player);

        if (lunaMenu == null) return;

        LunaMenu.deletePlayer(player);
    }
}
