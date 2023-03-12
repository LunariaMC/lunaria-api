package net.lunaria.api.plugins.bukkit.menus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface GuiBuilder {
    String name(String[] paramArrayOfString);

    String nameWithoutArgs();

    void setArgs(String[] paramArrayOfString);

    int getSize();

    void contents(Player paramPlayer, Inventory paramInventory, String[] paramArrayOfString);

    void onClick(Player paramPlayer, Inventory paramInventory, ItemStack paramItemStack, int paramInt);

    void onRightClick(Player paramPlayer, Inventory paramInventory, ItemStack paramItemStack, int paramInt);

    void onLeftClick(Player paramPlayer, Inventory paramInventory, ItemStack paramItemStack, int paramInt);
}
