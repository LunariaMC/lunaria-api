package net.lunaria.api.core.cache.redis.map;

import net.lunaria.api.core.cache.redis.UpdateRedisMessage;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapUpdateRedisMessage<K, V> extends UpdateRedisMessage {

    private Map<K, V> map;

    public MapUpdateRedisMessage() {
    }

    public MapUpdateRedisMessage(K key, V value) {
        this(Collections.singletonMap(key, value));
    }

    public MapUpdateRedisMessage(Map<K, V> map) {
        this.map = new LinkedHashMap<>(map);
    }

    public Map<K, V> getMap() {
        return this.map != null ? this.map : Collections.emptyMap();
    }

    public void setMap(Map<K, V> map) {
        this.map = map;
    }

}
