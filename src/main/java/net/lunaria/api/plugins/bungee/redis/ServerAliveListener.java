package net.lunaria.api.plugins.bungee.redis;

import net.lunaria.api.core.redis.RedisMessageListener;
import net.lunaria.api.core.servers.Server;
import net.md_5.bungee.api.ChatColor;

public class ServerAliveListener extends RedisMessageListener {
    public ServerAliveListener() {
        super("Bukkit:ServerManager:aliveSignal");
    }

    @Override
    protected void onSimpleMessage(String message) {
        Server server = Server.fromName(message);

        server.setRunning(true);
        System.out.println(ChatColor.GREEN + "[SM] " + server.getName() + " running.");
    }
}
