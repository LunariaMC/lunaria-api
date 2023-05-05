package net.lunaria.api.plugins.bungee.server;

import net.lunaria.api.core.redis.RedisSimpleMessageListener;
import net.lunaria.api.core.server.Server;

public class ServerAliveSimpleListener extends RedisSimpleMessageListener {
    public ServerAliveSimpleListener() {
        super("B_ServerManager:serverAlive");
    }

    @Override
    protected void onSimpleMessage(String message) {
        Server server = Server.fromName(message);

        server.setRunning(true);
        System.out.println("§aServer " + message + " running.");

        server.saveToRedis();
    }
}
