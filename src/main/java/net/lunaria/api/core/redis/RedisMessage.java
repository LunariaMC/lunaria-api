package net.lunaria.api.core.redis;

import net.lunaria.api.core.connector.RedisConnector;
import redis.clients.jedis.Jedis;

public class RedisMessage {
    private Jedis jedis;
    private String channel;

    public RedisMessage(String channel) {
        this.jedis = RedisConnector.getClient();
        this.channel = channel;
    }

    public void publish(String message) {
        this.jedis.publish(this.channel, message);
        this.jedis.close();
    }
}
