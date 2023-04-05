package net.lunaria.api.plugins.bukkit.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.lunaria.api.plugins.bukkit.BukkitAPI;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class ActionBar {
    private static final boolean enabled = true;

    private static final Map<UUID, String> actionMessages = new ConcurrentHashMap<>();

    private static Runnable actionMessagesUpdater = null;

    private static boolean actionMessagesUpdaterRunning = false;

    private static BukkitTask actionMessagesUpdaterTask = null;

    public static void sendPermanentMessage(Player player, String message) {
        actionMessages.put(player.getUniqueId(), message);
        sendMessage(player, message);
        checkActionMessageUpdaterRunningState();
    }

    public static void sendPermanentMessage(UUID playerUUID, String message) {
        actionMessages.put(playerUUID, message);
        sendMessage(playerUUID, message);
        checkActionMessageUpdaterRunningState();
    }

    public static void sendMessage(UUID playerUUID, String message) {
        sendMessage(Bukkit.getPlayer(playerUUID), message);
    }

    public static void sendMessage(Player player, String message) {
        if (player != null && message != null) {
            CraftPlayer p = (CraftPlayer)player;
            (p.getHandle()).playerConnection.sendPacket((Packet)new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}"), ChatPosition.GAME_INFO));
        }
    }

    public static void removeMessage(Player player, boolean instant) {
        actionMessages.remove(player.getUniqueId());
        if (instant)
            sendMessage(player, "");
        checkActionMessageUpdaterRunningState();
    }

    public static void removeMessage(UUID playerUUID, boolean instant) {
        actionMessages.remove(playerUUID);
        if (instant)
            sendMessage(playerUUID, "");
        checkActionMessageUpdaterRunningState();
    }

    public static void removeMessage(Player player) {
        removeMessage(player, false);
    }

    public static void removeMessage(UUID playerUUID) {
        removeMessage(playerUUID, false);
    }

    private static void initActionMessageUpdater() {
        actionMessagesUpdater = (() -> {
            Iterator<Map.Entry<UUID, String>> var0 = actionMessages.entrySet().iterator();
            while (var0.hasNext()) {
                Map.Entry<UUID, String> entry = var0.next();
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null && player.isOnline())
                    sendMessage(player, entry.getValue());
            }
        });
    }

    private static void checkActionMessageUpdaterRunningState() {
        int messagesCount = actionMessages.size();
        if (messagesCount == 0 && actionMessagesUpdaterRunning) {
            actionMessagesUpdaterTask.cancel();
            actionMessagesUpdaterTask = null;
            actionMessagesUpdaterRunning = false;
        } else if (messagesCount > 0 && !actionMessagesUpdaterRunning) {
            actionMessagesUpdaterTask = Bukkit.getScheduler().runTaskTimer((Plugin) BukkitAPI.getInstance(), actionMessagesUpdater, 2L, 30L);
            actionMessagesUpdaterRunning = true;
        }
    }

    static {
        initActionMessageUpdater();
    }

    public static class ChatPosition {
        public static byte CHAT = 1;

        public static byte SYSTEM_MESSAGE = 1;

        public static byte GAME_INFO = 2;
    }
}