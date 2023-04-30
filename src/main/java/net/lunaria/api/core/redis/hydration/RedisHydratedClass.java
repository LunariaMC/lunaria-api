package net.lunaria.api.core.redis.hydration;

import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

public class RedisHydratedClass extends JedisPubSub {
    protected transient String hydrationChannel;

    protected transient String storingRedisKey;
    protected transient int storingRedisDbIndex;

    protected transient String content;

    private String classPath;


    RedisHydratedClass(String hydrationChannel, String storingRedisKey, int storingRedisDbIndex) {
        this.classPath = this.getClass().getCanonicalName();

        this.hydrationChannel = hydrationChannel;
        this.storingRedisKey = storingRedisKey;
        this.storingRedisDbIndex = storingRedisDbIndex;
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!Objects.equals(channel, this.hydrationChannel)) return;

    }


}
