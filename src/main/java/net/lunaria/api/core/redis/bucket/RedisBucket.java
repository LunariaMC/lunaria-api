package net.lunaria.api.core.redis.bucket;

import com.google.gson.Gson;
import net.lunaria.api.core.redis.RedisManager;

import java.lang.reflect.Type;

public class RedisBucket<T> {
    protected final RedisManager redisManager;

    protected final String key;
    protected final Gson gson = new Gson();
    protected final String original;

    protected final Type type;
    protected T result;

    public RedisBucket(RedisManager redisManager, String key, Type type) {
        this.redisManager = redisManager;

        this.key = key;
        this.type = type;

        this.original = redisManager.getKey(key);
        if(!this.getClass().getSuperclass().equals(RedisBucket.class)) this.result = setupObject();
    }

    protected T setupObject() {return gson.fromJson(this.original, type);}
    protected String serialize() {return gson.toJson(result);}

    public T get() {
        return result;
    }
    public void set(T element) {
        this.result = element;
    }

    public void update() {
        if(this.result == null) redisManager.deleteKey(this.key);
        else redisManager.setKey(this.key, serialize());
    }
    public void delete() {
        this.result = null;
    }
}
