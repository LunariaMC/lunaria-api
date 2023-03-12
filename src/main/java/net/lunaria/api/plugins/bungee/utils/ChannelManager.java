package net.lunaria.api.plugins.bungee.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;
import net.lunaria.api.core.Bungee;
import net.lunaria.api.core.connectors.RabbitMQ;
import net.lunaria.api.plugins.bungee.listeners.ChannelEvent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ChannelManager {

    @SneakyThrows
    public static void sendMessage(String message, String queue, String nameEchange) {

        Thread thread = new Thread(() -> {
            try {

                Channel channel = null;
                channel = RabbitMQ.getConnection().createChannel();
                channel.exchangeDeclare(nameEchange, "direct");

                channel.basicPublish(nameEchange, queue, null, message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();

    }

    public static void init(String queue, String nameEchange) {
        new Thread(() -> {
            try {

                Channel channel = RabbitMQ.getConnection().createChannel();

                channel.exchangeDeclare(nameEchange, "direct");
                String queueName = channel.queueDeclare().getQueue();

                channel.queueBind(queueName, nameEchange, queue);
                System.out.println("   - Initialisation du channel " + queue + " (" + nameEchange + ")");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    ChannelEvent event = new ChannelEvent(queue, message);
                    Bungee.getInstance().getProxy().getPluginManager().callEvent(event);
                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                });
            } catch (IOException | NoClassDefFoundError ignored) {
            }
        }).start();

    }
}
