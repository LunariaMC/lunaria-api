package net.lunaria.api.core.connectors;

public enum ConnectorEnum {
    MONGODB("MongoDB"),
    REDIS("Redis"),
    RABBITMQ("RabbitMQ");

    private String name;

    ConnectorEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
