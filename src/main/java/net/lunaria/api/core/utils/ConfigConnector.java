package net.lunaria.api.core.utils;

public enum ConfigConnector {
    MONGODB("MongoDB"),
    REDIS("Redis"),
    RABBITMQ("RabbitMQ");

    private String name;

    ConfigConnector(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
