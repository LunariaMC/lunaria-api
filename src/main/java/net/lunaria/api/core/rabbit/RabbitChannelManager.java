package net.lunaria.api.core.rabbit;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import net.lunaria.api.core.connectors.RabbitConnector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class RabbitChannelManager {
    @SneakyThrows
    public void sendMessage(String message, String queue, String nameExchange) {
        new Thread(() -> {
            try {
                Channel channel = null;

                channel = RabbitConnector.getConnection().createChannel();
                channel.exchangeDeclare(nameExchange, "direct");

                channel.basicPublish(nameExchange, queue, null, message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public abstract void initChannel(String queue, String nameExchange);
}
