package net.lunaria.api.core.cache.redis.single;

import net.lunaria.api.core.cache.ISingleCacheContainer;
import net.lunaria.api.core.cache.redis.CacheUpdateType;
import net.lunaria.api.core.redis.RedisManager;
import net.lunaria.api.core.redis.bucket.RedisBucket;
import net.lunaria.api.core.redis.messaging.IRedisMessageCatcher;
import net.lunaria.api.core.redis.messaging.RedisMessage;

import java.lang.reflect.Type;

public interface IRedisSingleCache<T, R extends SingleUpdateRedisMessage<T>> extends IRedisMessageCatcher, ISingleCacheContainer<T> {

    Type getElementType();
    Class<R> getUpdateRedisMessageClass();
    R createBlankRedisMessage();
    String getRedisKey();

    @Override
    default boolean catchRedisMessage(RedisMessage message) {

        Class<R> clazz = getUpdateRedisMessageClass();
        if(!clazz.isInstance(message)) return false;

        R singleUpdateRedisMessage = (R) message;
        T element = singleUpdateRedisMessage.getElement();

        if(onSet(element)) setCache(element);

        onFinish();

        return true;
    }

    default void getCacheFromRedis(){
        setCache(getRedisBucket().get());
    }

    default void updateCacheEverywhere(){
        updateSingleEverywhere(getCache());
    }

    default void updateSingleEverywhere(T element) {

        if(element == null) return;

        RedisBucket<T> redisBucket = getRedisBucket();
        redisBucket.set(element);
        redisBucket.update();

        R redisMessage = createBlankRedisMessage();
        redisMessage.setUpdateType(CacheUpdateType.ADD_ALL);
        redisMessage.setElement(element);
        redisMessage.publish();

    }

    default RedisBucket<T> getRedisBucket(){
        return RedisManager.getInstance().getBucket(getRedisKey(), getElementType());
    }

    default boolean onSet(T element){
        return true;
    }

    default void onFinish(){

    }

}
