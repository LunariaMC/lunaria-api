package net.lunaria.api.core.cache;

public interface ISingleCacheContainer<T> {

    T getCache();
    void setCache(T t);

}
