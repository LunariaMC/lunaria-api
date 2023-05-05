package net.lunaria.api.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public interface ICollectionCacheContainer<E> {

    Collection<E> getCache();

    default void setCache(Collection<E> collection){

        if(collection == null) return;
        collection = new ArrayList<>(collection);

        Collection<E> cache = getCache();
        if(cache == null) return;

        cache.clear();
        cache.addAll(collection);
    }

    default void addToCache(E element){
        Collection<E> cache = getCache();
        if(cache == null) return;
        cache.add(element);
    }

    default void removeFromCache(E element){
        Collection<E> cache = getCache();
        if(cache == null || cache.isEmpty()) return;
        cache.remove(element);
    }

    default boolean existInCache(E element){
        Collection<E> cache = getCache();
        if(cache == null || cache.isEmpty()) return false;
        return cache.contains(element);
    }

}
