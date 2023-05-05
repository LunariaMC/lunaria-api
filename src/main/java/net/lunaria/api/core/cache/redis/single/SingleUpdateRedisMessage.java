package net.lunaria.api.core.cache.redis.single;

import net.lunaria.api.core.cache.redis.UpdateRedisMessage;

public class SingleUpdateRedisMessage<T> extends UpdateRedisMessage {

    private T element;

    public SingleUpdateRedisMessage() {
    }

    public SingleUpdateRedisMessage(T element) {
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

}
