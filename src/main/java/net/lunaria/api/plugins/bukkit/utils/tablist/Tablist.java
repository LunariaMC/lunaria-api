package net.lunaria.api.plugins.bukkit.utils.tablist;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Tablist {
    public static void sendTablist(Player player, String header, String footer) {
        CraftPlayer craftPlayer = (CraftPlayer) player;

        PlayerConnection connection = craftPlayer.getHandle().playerConnection;
        IChatBaseComponent headerComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \""+header+"\"}");
        IChatBaseComponent footerComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \""+footer+"\"}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            Field footerField = packet.getClass().getDeclaredField("b");
            headerField.setAccessible(true); footerField.setAccessible(true);

            headerField.set(packet, headerComponent);
            footerField.set(packet, footerComponent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        connection.sendPacket(packet);
    }

    public static void sendGlobalTablist(String header, String footer) {
        for (Player player : Bukkit.getOnlinePlayers()) sendTablist(player, header, footer);
    }
}
