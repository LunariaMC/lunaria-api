package net.lunaria.api.core.cache.redis;

public enum CacheUpdateType {

    CLEAR_AND_ADD_ALL,
    ADD_ALL,
    REMOVE_ALL;

    ;

    public boolean isRemove(){
        return name().startsWith("REMOVE");
    }

    public boolean isAdd(){
        return !isRemove();
    }

}
