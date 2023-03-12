package net.lunaria.api.core.connectors;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import net.lunaria.api.core.config.Config;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConnector {

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

            Config config = new Config(Config.isSpigot(), ConnectorEnum.RABBITMQ);

            factory.setUsername((String) config.getData(ConnectorConfigFieldsEnum.USERNAME));
            factory.setPassword((String) config.getData(ConnectorConfigFieldsEnum.PASSWORD));
            factory.setVirtualHost((String) config.getData(ConnectorConfigFieldsEnum.VIRTUALHOST));
            factory.setAutomaticRecoveryEnabled(true);
            factory.setHost((String) config.getData(ConnectorConfigFieldsEnum.ADDRESS));
            factory.setPort((int) config.getData(ConnectorConfigFieldsEnum.PORT));

            connection = factory.newConnection();
            System.out.println("Class » " + RabbitConnector.class.getSimpleName() + " I: RabbitMQ s'est bien connecté.");

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
