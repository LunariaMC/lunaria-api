package net.lunaria.api.core.redis.bucket;

import com.google.gson.reflect.TypeToken;
import net.lunaria.api.core.redis.RedisManager;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisMap<K, V> extends RedisBucket<Map<K, V>> implements Map<K, V> {
    private final Type keyType;
    private final Type valueType;

    public RedisMap(RedisManager redisManager, Type keyType, Type valueType, String key) {
        super(redisManager, key, new TypeToken<Map<String, String>>(){}.getType());
        this.keyType = keyType;
        this.valueType = valueType;
        this.result = setupObject();
    }

    @Override
    protected Map<K, V> setupObject() {
        Map<K, V> content = new HashMap<>();

        Map<String, String> map = gson.fromJson(original, new TypeToken<Map<String, String>>(){}.getType());

        if (map != null) {
            for (Entry<String, String> entry : map.entrySet()) {
                K key = gson.fromJson(entry.getKey(), keyType);
                V value = gson.fromJson(entry.getValue(), valueType);

                if(key != null && value != null) content.put(key,value);
            }
        }

        return content;
    }

    @Override
    protected String serialize() {
        Map<String, String> map = new HashMap<>();
        for (Entry<K, V> entry : this.entrySet()) {
            map.put(gson.toJson(entry.getKey()), gson.toJson(entry.getValue()));
        }
        return gson.toJson(map);
    }

    @Override
    public int size() {
        return this.result.size();
    }

    @Override
    public boolean isEmpty() {
        return this.result.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.result.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.result.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return this.result.get(key);
    }

    @Override
    public V put(K key, V value) {
        return this.result.put(key,value);
    }

    @Override
    public V remove(Object key) {
        return this.result.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        this.result.putAll(m);
    }

    @Override
    public void clear() {
        this.result.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.result.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.result.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.result.entrySet();
    }
}
