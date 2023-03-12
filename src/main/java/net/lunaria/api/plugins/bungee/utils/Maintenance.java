package net.lunaria.api.plugins.bungee.utils;

import net.lunaria.api.core.connectors.Redis;
import net.lunaria.api.core.utils.Json;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;

public class Maintenance {

    private static Boolean isActive;
    private static ArrayList<String> users;
    private static final String name = "maintenance.info";

    public static void init() {
        Jedis jedis = Redis.getClient();
        String answer = jedis.get(name);
        if(answer == null){
            isActive = true;
            users = new ArrayList<>(Arrays.asList("Papipomme", "CrypenterTV"));
            Json json = new Json();
            json.set("isActive", true);
            json.set("users", users);
            jedis.set(name, json.encodeData());
        } else {
            Json json = new Json();
            json.decodeData(answer);
            isActive = json.getBoolean("isActive");
            users = new ArrayList<>(json.getList(String[].class, "users"));
        }
        jedis.close();
    }

    public static boolean isActive() {
        if(isActive == null){
            Jedis jedis = Redis.getClient();
            Json json = new Json();
            json.decodeData(jedis.get(name));
            isActive = json.getBoolean("isActive");
            jedis.close();
        }
        return isActive;
    }

    public static void setIsActive(Boolean isActive) {
        Maintenance.isActive = isActive;
        Jedis jedis = Redis.getClient();
        Json json = new Json();
        json.decodeData(jedis.get(name));
        json.set("isActive", isActive);
        jedis.set(name, json.encodeData());
        jedis.close();
    }

    public static ArrayList<String> getUsers() {
        if(users == null){
            Jedis jedis = Redis.getClient();
            Json json = new Json();
            json.decodeData(jedis.get(name));
            users = new ArrayList<>(json.getList(String[].class, "users"));
            jedis.close();
        }
        return users;
    }

    public static void addUser(String user){
        users.add(user);
        Jedis jedis = Redis.getClient();
        Json json = new Json();
        json.decodeData(jedis.get(name));
        json.set("users", users);
        jedis.set(name, json.encodeData());
        jedis.close();
    }

    public static void removeUser(String user){
        users.remove(user);
        Jedis jedis = Redis.getClient();
        Json json = new Json();
        json.decodeData(jedis.get(name));
        json.set("users", users);
        jedis.set(name, json.encodeData());
        jedis.close();
    }

}
