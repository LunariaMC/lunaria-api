package net.lunaria.api.core.redis;

import lombok.Getter;
import net.lunaria.api.core.connectors.RedisConnector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

public class RedisMessageListener extends JedisPubSub {
    private Jedis jedis;
    private @Getter String channel;

    public RedisMessageListener(String channel) {
        this.channel = channel;
    }

    @Override
    public void onMessage(String channel, String message) {
        this.jedis = RedisConnector.getClient();
        if (!Objects.equals(channel, this.channel)) return;
        onSimpleMessage(message);
        this.jedis.close();
    }

    protected void onSimpleMessage(String message) {

    }
}
