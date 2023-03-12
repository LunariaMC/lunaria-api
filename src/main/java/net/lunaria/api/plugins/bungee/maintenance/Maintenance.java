package net.lunaria.api.plugins.bungee.maintenance;

import net.lunaria.api.core.connectors.RedisConnector;
import net.lunaria.api.core.utils.Json;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;

public class Maintenance {

    private static Boolean isActive;
    private static ArrayList<String> users;
    private static final String name = "maintenance.info";

    public static void init() {
        Jedis jedis = RedisConnector.getClient();
        String answer = jedis.get(name);
        if(answer == null){
            isActive = true;
            users = new ArrayList<>(Arrays.asList("Papipomme", "CrypenterTV"));
            Json json = new Json();
            json.set("isActive", true);
            json.set("users", users);
            jedis.set(name, json.toJson());
        } else {
            Json json = new Json();
            json.fromString(answer);
            isActive = json.getBoolean("isActive");
            users = new ArrayList<>(json.getList(String[].class, "users"));
        }
        jedis.close();
    }

    public static boolean isActive() {
        if(isActive == null){
            Jedis jedis = RedisConnector.getClient();
            Json json = new Json();
            json.fromString(jedis.get(name));
            isActive = json.getBoolean("isActive");
            jedis.close();
        }
        return isActive;
    }

    public static void setIsActive(Boolean isActive) {
        Maintenance.isActive = isActive;
        Jedis jedis = RedisConnector.getClient();
        Json json = new Json();
        json.fromString(jedis.get(name));
        json.set("isActive", isActive);
        jedis.set(name, json.toJson());
        jedis.close();
    }

    public static ArrayList<String> getUsers() {
        if(users == null){
            Jedis jedis = RedisConnector.getClient();
            Json json = new Json();
            json.fromString(jedis.get(name));
            users = new ArrayList<>(json.getList(String[].class, "users"));
            jedis.close();
        }
        return users;
    }

    public static void addUser(String user){
        users.add(user);
        Jedis jedis = RedisConnector.getClient();
        Json json = new Json();
        json.fromString(jedis.get(name));
        json.set("users", users);
        jedis.set(name, json.toJson());
        jedis.close();
    }

    public static void removeUser(String user){
        users.remove(user);
        Jedis jedis = RedisConnector.getClient();
        Json json = new Json();
        json.fromString(jedis.get(name));
        json.set("users", users);
        jedis.set(name, json.toJson());
        jedis.close();
    }

}
