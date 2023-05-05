package net.lunaria.api.core.redis;

import lombok.Getter;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

public class RedisSimpleMessageListener extends JedisPubSub {
    private @Getter String channel;

    public RedisSimpleMessageListener(String channel) {
        this.channel = channel;
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!Objects.equals(channel, this.channel) && !channel.startsWith("B_")) return;
        onSimpleMessage(message);
    }

    protected void onSimpleMessage(String message) {

    }
}
