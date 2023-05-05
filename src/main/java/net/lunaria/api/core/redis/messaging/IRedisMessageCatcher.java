package net.lunaria.api.core.redis.messaging;

import java.lang.reflect.Type;

public interface IRedisMessageCatcher {

    boolean catchRedisMessage(RedisMessage message) throws Exception;

}
