package net.lunaria.api.core.cache.redis.map;

import net.lunaria.api.core.cache.IMapCacheContainer;
import net.lunaria.api.core.cache.redis.CacheUpdateType;
import net.lunaria.api.core.redis.RedisManager;
import net.lunaria.api.core.redis.bucket.RedisMap;
import net.lunaria.api.core.redis.messaging.IRedisMessageCatcher;
import net.lunaria.api.core.redis.messaging.RedisMessage;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public interface IRedisMapCache<K, V, R extends MapUpdateRedisMessage<K, V>> extends IMapCacheContainer<K, V>, IRedisMessageCatcher {

    Type getKeyType();
    Type getValueType();

    Class<R> getUpdateRedisMessageClass();
    R createBlankRedisMessage();
    String getRedisKey();

    @Override
    default boolean catchRedisMessage(RedisMessage message) {

        Class<R> clazz = getUpdateRedisMessageClass();
        if(!clazz.isInstance(message)) return false;
        if(message.isFromInstance()) return false;

        R mapUpdateRedisMessage = (R) message;
        CacheUpdateType updateType = mapUpdateRedisMessage.getUpdateType();
        if(updateType == null) return false;

        Map<K, V> updatedMap = mapUpdateRedisMessage.getMap();
        if(updatedMap == null) return false;

        Map<K, V> cache = getCache();
        if(cache == null) return false;

        if(updateType != CacheUpdateType.CLEAR_AND_ADD_ALL){

            for (Map.Entry<K, V> kvEntry : updatedMap.entrySet()) {

                if(updateType == CacheUpdateType.REMOVE_ALL) {

                    if(cache.containsKey(kvEntry.getKey())) if(onRemove(kvEntry.getKey())) cache.remove(kvEntry.getKey());

                } else if(updateType == CacheUpdateType.ADD_ALL){

                    if(cache.containsKey(kvEntry.getKey())) {
                        if(onUpdatePut(kvEntry.getKey(), kvEntry.getValue())) cache.put(kvEntry.getKey(), kvEntry.getValue());;
                    } else {
                        if(onPut(kvEntry.getKey(), kvEntry.getValue())) cache.put(kvEntry.getKey(), kvEntry.getValue());;
                    }

                }

            }

        } else {

            if(onClearAndPutAll(updatedMap)){
                cache.clear();
                cache.putAll(updatedMap);
            }

        }

        onFinish();
        return true;
    }

    default void getCacheFromRedis(){
        Map<K, V> cache = getCache();
        cache.clear();
        cache.putAll(getRedisBucket());
    }

    default void updateCacheEverywhere() {
        updateMapEverywhere(getCache(), CacheUpdateType.CLEAR_AND_ADD_ALL);
    }

    default void updateSingleEverywhere(K key, V value, CacheUpdateType updateType) {
        updateMapEverywhere(Collections.singletonMap(key, value), updateType);
    }

    default void updateMapEverywhere(Map<K, V> map, CacheUpdateType updateType) {

        if(map == null) return;

        map = new LinkedHashMap<>(map);

        Map<K, V> cache = getCache();
        if(updateType == CacheUpdateType.CLEAR_AND_ADD_ALL) cache.clear();
        else if(updateType.isRemove()) {
            for(K key : map.keySet()) cache.remove(key);
        }

        if(updateType.isAdd()) cache.putAll(map);

        RedisMap<K, V> redisMap = getRedisBucket();
        redisMap.clear();
        redisMap.set(cache);
        redisMap.update();

        R redisMessage = createBlankRedisMessage();
        redisMessage.setUpdateType(updateType);
        redisMessage.setMap(map);
        redisMessage.publish();

    }

    default RedisMap<K, V> getRedisBucket(){
        return RedisManager.getInstance().getMap(getRedisKey(), getKeyType(), getValueType());
    }

    default boolean onPut(K key, V v){
        return true;
    }

    default boolean onUpdatePut(K key, V v){
        return true;
    }

    default boolean onClearAndPutAll(Map<K, V> map){
        return true;
    }

    default boolean onRemove(K key){
        return true;
    }

    default void onFinish(){

    }

}
