package net.lunaria.api.plugins.bungee.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import net.lunaria.api.core.rabbit.RabbitChannelManager;
import net.lunaria.api.plugins.bungee.BungeeAPI;
import net.lunaria.api.core.connectors.RabbitConnector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BungeeChannelManager extends RabbitChannelManager {
    public void initChannel(String queue, String nameEchange) {
        new Thread(() -> {
            try {

                Channel channel = RabbitConnector.getConnection().createChannel();

                channel.exchangeDeclare(nameEchange, "direct");
                String queueName = channel.queueDeclare().getQueue();

                channel.queueBind(queueName, nameEchange, queue);
                System.out.println("   - Initialisation du channel " + queue + " (" + nameEchange + ")");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    BungeeChannelEvent event = new BungeeChannelEvent(queue, message);
                    BungeeAPI.getInstance().getProxy().getPluginManager().callEvent(event);
                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                });
            } catch (IOException | NoClassDefFoundError ignored) {
            }
        }).start();

    }
}
