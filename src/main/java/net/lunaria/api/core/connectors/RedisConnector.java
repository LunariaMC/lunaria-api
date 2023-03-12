package net.lunaria.api.core.connectors;

import net.lunaria.api.core.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnector {

    private static JedisPool client;

    private static String address;
    private static String password;
    private static int port;

    public static void init(){

        Config configFile = new Config(Config.isSpigot(), ConnectorEnum.REDIS);

        address = (String) configFile.getData(ConnectorConfigFieldsEnum.ADDRESS);
        password = (String) configFile.getData(ConnectorConfigFieldsEnum.PASSWORD);
        port = (int) configFile.getData(ConnectorConfigFieldsEnum.PORT);

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(-1);
        config.setJmxEnabled(false);
        client = new JedisPool(config, address, port, 0, password);
        System.out.println("Class » " + RedisConnector.class.getSimpleName() + " I:Le client redis a réussi à se connecter.");

    }

    public static void disconnect(){
        getClient().close();
    }

    public static Jedis getClient() {
        return client.getResource();
    }
}
