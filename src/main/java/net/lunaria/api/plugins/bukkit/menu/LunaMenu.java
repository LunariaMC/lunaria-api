package net.lunaria.api.plugins.bukkit.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LunaMenu implements InventoryHolder {
    private static Map<Player, LunaMenu> currentMenu = new ConcurrentHashMap<>();

    private @Getter String name;
    private @Getter int size;

    private @Getter Inventory inventory;
    public LunaMenu(Player player, String name, int size) {
        this.name = name;
        this.size = size;

        this.inventory = Bukkit.createInventory(this, size, name);
    }

    public LunaMenu(Player player, String name, InventoryType inventoryType) {
        this.name = name;
        this.size = inventoryType.getDefaultSize();

        this.inventory = Bukkit.createInventory(this, inventoryType, name);
    }

    public abstract void onOpen(Player player, Inventory inventory);
    public abstract void onClick(InventoryClickEvent event, Player player);
    public abstract void onClose(Player player);

    public void open(Player player) {
        currentMenu.put(player, this);
        onOpen(player, this.inventory);
        player.openInventory(inventory);
    }
    public static LunaMenu fromPlayer(Player player) {
        return currentMenu.get(player);
    }
    public static void deletePlayer(Player player) {
        currentMenu.remove(player);
    }
}
