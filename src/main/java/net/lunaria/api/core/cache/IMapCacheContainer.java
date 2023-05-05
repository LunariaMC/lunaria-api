package net.lunaria.api.core.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface IMapCacheContainer<K, V> {

    Map<K, V> getCache();

    default void setCache(Map<K, V> map){

        if(map == null) return;
        map = new LinkedHashMap<>(map);

        Map<K, V> cache = getCache();
        if(cache == null) return;

        cache.clear();
        cache.putAll(map);
    }

    default void addToCache(K key, V value){
        Map<K, V> cache = getCache();
        if(cache == null) return;
        key = transFromKey(key);
        cache.put(key, value);
    }

    default void removeFromCache(K key){
        Map<K, V> cache = getCache();
        if(cache == null || cache.isEmpty()) return;
        key = transFromKey(key);
        cache.remove(key);
    }

    default boolean existInCache(K key){
        Map<K, V> cache = getCache();
        if(cache == null || cache.isEmpty()) return false;
        return cache.containsKey(key);
    }

    static <K> K transFromKey(K key){
        if(key instanceof String) key = (K) ((String) key).toLowerCase();
        return key;
    }

}
