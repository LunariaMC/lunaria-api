package net.lunaria.api.core.connectors;

import net.lunaria.api.core.utils.ConfigConnector;
import net.lunaria.api.core.utils.ConfigData;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.lunaria.api.core.utils.Config;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQ {

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public static boolean isOnline(){
        try {
            return getConnection() != null && getConnection().isOpen();
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void init(){
        try {
            ConnectionFactory factory = new ConnectionFactory();

            Config config = new Config(Config.isSpigot(), ConfigConnector.RABBITMQ);

            factory.setUsername((String) config.getData(ConfigData.USERNAME));
            factory.setPassword((String) config.getData(ConfigData.PASSWORD));
            factory.setVirtualHost((String) config.getData(ConfigData.VIRTUALHOST));
            factory.setAutomaticRecoveryEnabled(true);
            factory.setHost((String) config.getData(ConfigData.ADDRESS));
            factory.setPort((int) config.getData(ConfigData.PORT));

            connection = factory.newConnection();
            System.out.println("Class » " + RabbitMQ.class.getSimpleName() + " I: RabbitMQ s'est bien connecté.");

        } catch (IOException | TimeoutException e){
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try {
            if(isOnline()){
                try {
                    System.out.println("RabbitMQ - disconnected.");
                    getConnection().close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (NoClassDefFoundError ignored){}
    }

}
