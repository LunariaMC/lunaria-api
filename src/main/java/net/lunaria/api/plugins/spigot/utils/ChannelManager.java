package net.lunaria.api.plugins.spigot.utils;

import net.lunaria.api.core.connectors.RabbitMQ;
import net.lunaria.api.plugins.spigot.listeners.ChannelEvent;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ChannelManager {

    @SneakyThrows
    public static void sendMessage(String message, String queue, String nameExchange){

        Thread thread = new Thread(() -> {
            try {

                Channel channel = null;
                channel = RabbitMQ.getConnection().createChannel();
                channel.exchangeDeclare(nameExchange, "direct");

                channel.basicPublish(nameExchange, queue, null, message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();

    }

    public static void init(String queue, String nameExchange){
        new Thread(() -> {
            try{

                Channel channel = RabbitMQ.getConnection().createChannel();

                channel.exchangeDeclare(nameExchange, "direct");
                String queueName = channel.queueDeclare().getQueue();

                channel.queueBind(queueName, nameExchange, queue);
                System.out.println("   - Initialisation du channel " + queue + " (" + nameExchange + ")");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    ChannelEvent event = new ChannelEvent(queue, message);
                    Bukkit.getPluginManager().callEvent(event);

                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
            } catch (IOException | NoClassDefFoundError ignored){}
        }).start();

    }

}