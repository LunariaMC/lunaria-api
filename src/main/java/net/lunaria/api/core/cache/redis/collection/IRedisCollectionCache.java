package net.lunaria.api.core.cache.redis.collection;

import net.lunaria.api.core.cache.ICollectionCacheContainer;
import net.lunaria.api.core.cache.redis.CacheUpdateType;
import net.lunaria.api.core.redis.RedisManager;
import net.lunaria.api.core.redis.bucket.RedisList;
import net.lunaria.api.core.redis.messaging.IRedisMessageCatcher;
import net.lunaria.api.core.redis.messaging.RedisMessage;

import java.lang.reflect.Type;
import java.util.*;

public interface IRedisCollectionCache<E, R extends CollectionUpdateRedisMessage<E>> extends IRedisMessageCatcher, ICollectionCacheContainer<E> {

    Type getElementType();
    Class<R> getUpdateRedisMessageClass();
    R createBlankRedisMessage();
    String getRedisKey();

    @Override
    default boolean catchRedisMessage(RedisMessage message) {

        Class<R> clazz = getUpdateRedisMessageClass();
        if(!clazz.isInstance(message)) return false;
        if(message.isFromInstance()) return false;

        R collectionUpdateRedisMessage = (R) message;
        CacheUpdateType updateType = collectionUpdateRedisMessage.getUpdateType();
        if(updateType == null) return false;

        Collection<E> updatedList = collectionUpdateRedisMessage.getCollection();
        if(updatedList == null) return false;

        Collection<E> cache = getCache();
        if(cache == null) return false;

        if(updateType != CacheUpdateType.CLEAR_AND_ADD_ALL){

            for(E element : updatedList) {

                if(updateType.isRemove() || updateType.isAdd()) {

                    E found;
                    while((found = cache.stream().filter(e -> Objects.equals(e, element)).findAny().orElse(null)) != null) {
                        if(onRemove(found)) cache.remove(found);
                    }

                } if(updateType == CacheUpdateType.ADD_ALL) {

                    if(onAdd(element)) cache.add(element);

                }

            }

        } else {

            if(onClearAndAddAll(updatedList)) {
                cache.clear();
                cache.addAll(updatedList);
            }

        }

        onFinish();
        return true;
    }

    default void getCacheFromRedis(){
        Collection<E> cache = getCache();
        cache.clear();
        cache.addAll(getRedisBucket());
    }

    default void updateCacheEverywhere() {
        updateCollectionEverywhere(getCache(), CacheUpdateType.CLEAR_AND_ADD_ALL);
    }

    default void updateSingleEverywhere(E element, CacheUpdateType updateType) {
        updateCollectionEverywhere(Collections.singleton(element), updateType);
    }

    default void updateCollectionEverywhere(Collection<? extends E> collection, CacheUpdateType updateType) {

        if(collection == null) return;

        collection = new ArrayList<>(collection);

        Collection<E> cache = getCache();
        if(updateType == CacheUpdateType.CLEAR_AND_ADD_ALL) cache.clear();
        else if(updateType.isRemove() || updateType.isAdd()) {
            for(E element : collection) cache.removeIf(el -> Objects.equals(el, element));
        }

        if(updateType.isAdd()){

            for(E element : collection){

                cache.removeIf(el -> Objects.equals(el, element));
                cache.add(element);

            }

        }

        RedisList<E> redisCollection = getRedisBucket();
        redisCollection.clear();
        redisCollection.set(new ArrayList<>(cache));
        redisCollection.update();

        R redisMessage = createBlankRedisMessage();
        redisMessage.setUpdateType(updateType);
        redisMessage.setCollection(collection);
        redisMessage.publish();

    }

    default RedisList<E> getRedisBucket(){
        return RedisManager.getInstance().getList(getRedisKey(), getElementType());
    }

    default boolean onAdd(E element){
        return true;
    }

    default boolean onRemove(E element){
        return true;
    }

    default boolean onClearAndAddAll(Collection<E> collection){
        return true;
    }

    default void onFinish(){

    }

}
