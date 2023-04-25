package net.lunaria.api.plugins.bungee.redis;

import com.google.gson.Gson;
import net.lunaria.api.core.redis.RedisDBIndex;
import net.lunaria.api.core.redis.RedisManager;
import net.lunaria.api.core.redis.RedisMessageListener;
import net.lunaria.api.core.server.Server;
import net.md_5.bungee.api.ChatColor;

public class ServerAliveListener extends RedisMessageListener {
    public ServerAliveListener() {
        super("Bukkit:ServerManager:aliveSignal");
    }

    @Override
    protected void onSimpleMessage(String message) {
        Server server = Server.fromName(message);

        server.setRunning(true);
        RedisManager.set("Server." + server.getName(), new Gson().toJson(server), RedisDBIndex.SERVER_CACHE.getIndex());

        System.out.println(ChatColor.GREEN + "[SM] " + server.getName() + " running.");
    }
}
