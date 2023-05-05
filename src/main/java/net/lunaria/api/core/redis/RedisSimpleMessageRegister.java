package net.lunaria.api.core.redis;

import net.lunaria.api.core.connector.RedisConnector;
import redis.clients.jedis.Jedis;

public class RedisSimpleMessageRegister {
    public static void register(RedisSimpleMessageListener... listeners) {
        Jedis jedis = RedisConnector.getClient();
        for (RedisSimpleMessageListener listener : listeners) {
            new Thread(() -> jedis.subscribe(listener, listener.getChannel())).start();
        }
        jedis.disconnect();
        jedis.close();
        RedisConnector.reconnect();
    }
}
