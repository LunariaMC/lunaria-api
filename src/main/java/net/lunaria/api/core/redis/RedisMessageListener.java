package net.lunaria.api.core.redis;

import net.lunaria.api.core.connectors.RedisConnector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

public class RedisMessageListener extends JedisPubSub {
    private JedisPubSub jedisPubSub;
    private Jedis jedis;

    private String channel;

    public RedisMessageListener(String channel) {
        this.jedis = RedisConnector.getClient();
        this.jedisPubSub = this;

        this.channel = channel;

        this.jedis.subscribe(this, channel);
        this.jedis.close();
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
