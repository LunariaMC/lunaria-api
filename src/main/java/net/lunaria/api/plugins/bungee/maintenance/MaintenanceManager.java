package net.lunaria.api.plugins.bungee.maintenance;

import lombok.Getter;
import net.lunaria.api.core.connectors.RedisConnector;
import net.lunaria.api.core.utils.Json;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MaintenanceManager {
    private static @Getter boolean active;
    private static ArrayList<String> playerWhitelist = new ArrayList<>();
    private static final String REDIS_KEY = "Maintenance.Infos";

    public static void init() {
        Jedis jedis = RedisConnector.getClient();

        String maintenance = jedis.get(REDIS_KEY);

        if (maintenance == null) {
            playerWhitelist.add("Papipomme");
            Json json = new Json();
            json.set("active", true);
            json.set("whitelist", playerWhitelist);

            active = true;

            jedis.set(REDIS_KEY, json.toJson());
            jedis.close();
            return;
        }
        jedis.close();

        Json json = new Json();
        json.fromString(maintenance);
        active = json.getBoolean("active");
        playerWhitelist = new ArrayList<>(json.getList(String[].class, "whitelist"));
    }

    public static void addPlayer(String playerName) {
        MaintenanceManager.playerWhitelist.add(playerName);

        Jedis jedis = RedisConnector.getClient();

        Json json = new Json();
        json.fromString(jedis.get(REDIS_KEY));
        json.set("whitelist", playerWhitelist.toArray());

        jedis.set(REDIS_KEY, json.toJson());
    }

    public static ArrayList<String> getPlayerWhitelist() {
        return playerWhitelist;
    }

    public static void setActive(boolean active) {
        MaintenanceManager.active = active;

        Jedis jedis = RedisConnector.getClient();

        Json json = new Json();
        json.fromString(jedis.get(REDIS_KEY));
        json.set("active", active);

        jedis.set(REDIS_KEY, json.toJson());
    }

    public static boolean isActive() {
        return active;
    }
}
