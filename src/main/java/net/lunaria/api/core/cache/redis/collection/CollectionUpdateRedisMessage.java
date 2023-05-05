package net.lunaria.api.core.cache.redis.collection;

import net.lunaria.api.core.cache.redis.CacheUpdateType;
import net.lunaria.api.core.cache.redis.UpdateRedisMessage;

import java.util.*;

public class CollectionUpdateRedisMessage<E> extends UpdateRedisMessage {

    private Collection<E> collection;

    public CollectionUpdateRedisMessage() {
    }

    public CollectionUpdateRedisMessage(E element, CacheUpdateType updateType) {
        this(new ArrayList<>(Collections.singletonList(element)), updateType);
    }

    public CollectionUpdateRedisMessage(Collection<? extends E> elements, CacheUpdateType updateType) {
        this.collection = new ArrayList<>(elements);
        this.updateType = updateType;
    }

    public Collection<E> getCollection() {
        return collection;
    }

    public void setCollection(Collection<? extends E> collection) {
        this.collection = new ArrayList<>(collection);
    }

}
