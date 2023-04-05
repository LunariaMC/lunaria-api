package net.lunaria.api.core.redis;

import net.lunaria.api.core.connectors.RedisConnector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import javax.annotation.Nullable;

public class RedisManager {
    public static void set(String key, String value, @Nullable Integer dbIndex) {
        Jedis jedis = RedisConnector.getClient();

        if (dbIndex != null) jedis.select(dbIndex);
        jedis.set(key, value);

        jedis.close();
    }

    public static void unset(String key, @Nullable Integer dbIndex) {
        Jedis jedis = RedisConnector.getClient();

        if (dbIndex != null) jedis.select(dbIndex);
        jedis.del(key);

        jedis.close();
    }

    public static String get(String key, @Nullable Integer dbIndex) {
        Jedis jedis = RedisConnector.getClient();

        if (dbIndex != null) jedis.select(dbIndex);
        String value = jedis.get(key);

        jedis.close();
        return value;
    }

    public static void clearRedisCache(int... dbIndexes) {
        Jedis jedis = RedisConnector.getClient();

        for (int i : dbIndexes) {
            jedis.select(i);
            jedis.flushDB();
        }

        jedis.close();
    }
}
