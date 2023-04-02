package net.lunaria.api.core.redis;

import net.lunaria.api.core.connectors.RedisConnector;
import org.reflections.Reflections;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;

public class RedisListenersRegister {
    public static void registerListeners(String packageName) {
        new Thread(() -> {
            Jedis jedis = RedisConnector.getClient();
            int list = 0;
            for (Class<?> clazz : (new Reflections(packageName).getSubTypesOf(RedisMessageListener.class))) {
                String packClass = clazz.getPackage().getName();
                if (packClass.contains(packageName))
                    try {
                        RedisMessageListener listener = (RedisMessageListener) clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);

                        jedis.subscribe(listener, listener.getChannel());

                        list++;
                    } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                             NoSuchMethodException e) {
                        System.out.println("(initRedis) : Une erreur s'est produite | Class=" + clazz.getSimpleName());
                    } finally {
                        jedis.close();
                    }
            }
            System.out.println(" - Initialisation de " + list + " listener(s) redis.");
        }).start();
    }
}
