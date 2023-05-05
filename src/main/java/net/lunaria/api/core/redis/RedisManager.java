package net.lunaria.api.core.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.lunaria.api.core.connector.RedisConnector;
import net.lunaria.api.core.redis.bucket.RedisBucket;
import net.lunaria.api.core.redis.bucket.RedisList;
import net.lunaria.api.core.redis.bucket.RedisMap;
import net.lunaria.api.core.redis.messaging.IRedisMessageCatcher;
import net.lunaria.api.core.redis.messaging.RedisMessage;
import net.lunaria.api.core.redis.messaging.RedisMessageSerializer;
import net.lunaria.api.plugins.bukkit.BukkitAPI;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class RedisManager implements Closeable {
    public static final String CHANNEL = "redis";

    private static @Getter RedisManager instance;

    private final @Getter Jedis client;

    private final Set<ExecutorService> executorServices = new CopyOnWriteArraySet<>();
    private final Set<String> registeredChannels = new CopyOnWriteArraySet<>();
    private final Set<IRedisMessageCatcher> catchers = new CopyOnWriteArraySet<>();
    private final Set<Jedis> jedisListeners = new CopyOnWriteArraySet<>();

    public RedisManager() {
        this.client = RedisConnector.getClient();
        instance = this;
    }

    @Override
    public void close() throws IOException {
        this.client.close();
        this.jedisListeners.forEach(Jedis::close);
        this.executorServices.forEach(ExecutorService::shutdownNow);
    }

    public <T> RedisBucket<T> getBucket(String key, Type type){
        return new RedisBucket<>(this, key, type);
    }
    public <T> RedisList<T> getList(String key, Type type){
        return new RedisList<>(this, key, type);
    }
    public <K, V> RedisMap<K, V> getMap(String key, Type keyType, Type valueType){
        return new RedisMap<>(this, keyType, valueType, key);
    }

    public String getKey(String key, int index) {
        AtomicReference<String> result = new AtomicReference<>("");

        jedisConsumer(jedis -> {
            jedis.select(index);
            result.set(jedis.get(key));
            jedis.select(0);
        });

        try {
            if(result.get() != null)
                result.set(result.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result.get();
    }

    public String getKey(String key){
        AtomicReference<String> result = new AtomicReference<>("");
        jedisConsumer(jedis -> result.set(jedis.get(key)));

        try {
            if(result.get() != null)
                result.set(result.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result.get();
    }
    public void setKey(String key, String value){
        jedisConsumer(jedis ->{
            jedis.set(key, value);
        });
    }
    public void setKey(String key, String value, int index){
        jedisConsumer(jedis -> {
            jedis.select(index);
            jedis.set(key, value);
            jedis.select(0);
        });
    }

    public void deleteKey(String key){
        jedisConsumer(jedis -> jedis.del(key));
    }
    public void deleteKey(String key, int index){
        jedisConsumer(jedis -> {
            jedis.select(index);
            jedis.del(key);
            jedis.select(0);
        });
    }

    public void registerListener(IRedisMessageCatcher listener){
        if(listener == null) return;
        this.catchers.add(listener);
    }

    public void unregisterListener(IRedisMessageCatcher listener){
        if(listener == null) return;
        this.catchers.remove(listener);
    }

    public Set<IRedisMessageCatcher> getCatchers() {
        return new HashSet<>(catchers);
    }

    public void publishRedisMessage(RedisMessage redisMessage){
        redisMessage.setFrom(BukkitAPI.getServerName());
        publishMessage(CHANNEL, new GsonBuilder().registerTypeAdapter(RedisMessage.class, new RedisMessageSerializer()).create().toJson(redisMessage));
    }

    public void publishMessage(String channel, String message){
        jedisConsumer(jedis -> {
            jedis.publish(channel, message);
        });
    }

    public void registerChannel(String... channels){

        try {

            if(channels == null || channels.length <= 0) return;

            Set<String> channelSet = new HashSet<>();
            for (String channel : channels) channelSet.add(channel.toLowerCase());

            channelSet.removeIf(this.registeredChannels::contains);
            if(channelSet.isEmpty()) return;

            this.registeredChannels.addAll(channelSet);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            this.executorServices.add(executorService);

            executorService.execute(() -> {

                try {

                    String[] toListen = new ArrayList<>(channelSet).toArray(new String[0]);
                    toListen = new ArrayList<>(Arrays.asList(toListen)).toArray(new String[0]);

                    Jedis jedis = RedisConnector.getClient();
                    jedis.subscribe(this.createBlankPubSub(), toListen);

                    this.jedisListeners.add(jedis);
                } catch (Exception e){
                    e.printStackTrace();
                }

            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void jedisConsumer(Consumer<Jedis> jedisConsumer){
        jedisConsumer.accept(this.client);
    }

    private JedisPubSub createBlankPubSub() {
        return new JedisPubSub() {

            @Override
            public void onMessage(String channel, String message) {

                /*try {
                    message = ArchiveUtil.decompressString(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/

                Gson gson = new GsonBuilder().registerTypeAdapter(RedisMessage.class, new RedisMessageSerializer()).create();
                for(IRedisMessageCatcher catcher : RedisManager.this.catchers){

                    try {

                        RedisMessage redisMessage;

                        try {
                            redisMessage = gson.fromJson(message, RedisMessage.class);
                        } catch (Exception ignored){
                            redisMessage = new RedisMessage();
                        }

                        if(redisMessage == null) redisMessage = new RedisMessage();
                        redisMessage.setContent(message);

                        catcher.catchRedisMessage(redisMessage);

                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }

        };

    }

    public void clearRedisDb(int... indexes) {
        for (int i : indexes) {
            client.select(i);
            client.flushDB();
        }
        client.select(0);
    }
}
