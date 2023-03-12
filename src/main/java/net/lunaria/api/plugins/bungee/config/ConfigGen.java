package net.lunaria.api.plugins.bungee.config;

import lombok.SneakyThrows;
import net.lunaria.api.plugins.bungee.BungeeAPI;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigGen {

    private static BungeeAPI instance = BungeeAPI.getInstance();
    private static Configuration configuration;

    @SneakyThrows
    public static void init(){
        if(!instance.getDataFolder().exists()) instance.getDataFolder().mkdir();

        File file = new File(instance.getDataFolder(), "config.yml");
        configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(instance.getDataFolder(), "config.yml"));
        if(!file.exists()){
            try (InputStream in = instance.getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
                configuration.set("RabbitMQ.address", "127.0.0.1");
                configuration.set("RabbitMQ.virtualhost", "/");
                configuration.set("RabbitMQ.username", "guest");
                configuration.set("RabbitMQ.port", 5672);
                configuration.set("RabbitMQ.password", "guest");

                configuration.set("Redis.address", "127.0.0.1");
                configuration.set("Redis.port", 6379);
                configuration.set("Redis.password", "passhere");

                configuration.set("MongoDB.address", "127.0.0.1");
                configuration.set("MongoDB.username", "user");
                configuration.set("MongoDB.database", "database");
                configuration.set("MongoDB.port", 27017);
                configuration.set("MongoDB.password", "passhere");
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(instance.getDataFolder(), "config.yml"));

    }

    public static Configuration getConfig() {
        return configuration;
    }

}
