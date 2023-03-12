package net.lunaria.api.plugins.bukkit.menus;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiEventManager implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        if (current == null)
            return;
        if (!current.hasItemMeta())
            return;
        if (!current.getItemMeta().hasDisplayName())
            return;
        Player player = (Player)event.getWhoClicked();
        Inventory inv = event.getInventory();
        Map<Class<? extends GuiBuilder>, GuiBuilder> gm = GuiManager.getRegisteredMenus();
        gm.values().stream()
                .filter(menu -> inv.getName().contains(menu.nameWithoutArgs()))
                .forEach(menu -> {
                    menu.onClick(player, inv, current, event.getSlot());
                    event.setCancelled(true);
                });
        if (event.isRightClick()) {
            gm.values().stream()
                    .filter(menu -> inv.getName().contains(menu.nameWithoutArgs()))
                    .forEach(menu -> {
                        menu.onRightClick(player, inv, current, event.getSlot());
                        event.setCancelled(true);
                    });
        } else if (event.isLeftClick()) {
            gm.values().stream()
                    .filter(menu -> inv.getName().contains(menu.nameWithoutArgs()))
                    .forEach(menu -> {
                        menu.onLeftClick(player, inv, current, event.getSlot());
                        event.setCancelled(true);
                    });
        }
    }

    public void addMenu(GuiBuilder m) {
        GuiManager.getRegisteredMenus().put(m.getClass(), m);
    }

    public void open(Player player, String[] args, Class<? extends GuiBuilder> gClass) {
        if (!GuiManager.getRegisteredMenus().containsKey(gClass))
            return;
        GuiBuilder menu = GuiManager.getRegisteredMenus().get(gClass);
        menu.setArgs(args);
        Inventory inv = Bukkit.createInventory(null, menu.getSize(), menu.name(args));
        menu.contents(player, inv, args);
        player.openInventory(inv);
    }
}
