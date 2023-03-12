package net.lunaria.api.plugins.bukkit.rabbit;

import net.lunaria.api.core.connectors.RabbitConnector;
import net.lunaria.api.core.rabbit.RabbitChannelManager;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BukkitChannelManager extends RabbitChannelManager {
    @Override
    public void initChannel(String queue, String nameExchange) {
        new Thread(() -> {
            try{

                Channel channel = RabbitConnector.getConnection().createChannel();

                channel.exchangeDeclare(nameExchange, "direct");
                String queueName = channel.queueDeclare().getQueue();

                channel.queueBind(queueName, nameExchange, queue);
                System.out.println("   - Initialisation du channel " + queue + " (" + nameExchange + ")");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                    BukkitChannelEvent event = new BukkitChannelEvent(queue, message);
                    Bukkit.getPluginManager().callEvent(event);

                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
            } catch (IOException | NoClassDefFoundError ignored){}
        }).start();
    }

}