package net.lunaria.api.core.redis;

import lombok.Getter;

public enum RedisDBIndex {
    GLOBAL_INFOS(0),
    ACCOUNT_CACHE(1),
    SERVER_CACHE(2);

    private final @Getter int index;
    RedisDBIndex(int index) {
        this.index = index;
    }
}
