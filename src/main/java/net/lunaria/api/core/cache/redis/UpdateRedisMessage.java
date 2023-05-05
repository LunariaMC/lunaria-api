package net.lunaria.api.core.cache.redis;


import net.lunaria.api.core.redis.messaging.RedisMessage;

public class UpdateRedisMessage extends RedisMessage {

    protected CacheUpdateType updateType;

    public UpdateRedisMessage() {
    }

    public CacheUpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(CacheUpdateType updateType) {
        this.updateType = updateType;
    }

}
