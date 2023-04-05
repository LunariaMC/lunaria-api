package net.lunaria.api.core.redis;

import net.lunaria.api.core.connectors.RedisConnector;
import redis.clients.jedis.Jedis;


public class RedisListenersRegister {
    public static void registerListeners(RedisMessageListener... listeners) {
        Jedis jedis = RedisConnector.getClient();

        for (RedisMessageListener redisMessageListener : listeners) {
            new Thread(() -> jedis.subscribe(redisMessageListener, redisMessageListener.getChannel())).start();
        }

        jedis.disconnect();
        jedis.close();
        RedisConnector.reconnect();
    }
}
