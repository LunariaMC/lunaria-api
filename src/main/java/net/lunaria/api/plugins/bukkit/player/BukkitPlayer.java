package net.lunaria.api.plugins.bukkit.player;

import com.google.gson.Gson;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BukkitPlayer {

    public static HashMap<UUID, BukkitPlayerData> data = new HashMap<>();

    //Avec un player
    public static BukkitPlayerData getData(Player player){
        return data.get(player.getUniqueId());
    }

    public static BukkitPlayerData remove(Player player){
        return data.remove(player.getUniqueId());
    }

    //Avec un uuid
    public static BukkitPlayerData getData(UUID uuid){
        return data.get(uuid);
    }

    public static BukkitPlayerData remove(UUID uuid){
        return data.remove(uuid);
    }

    //set
    public static BukkitPlayerData setData(UUID uuid, String json) {
        data.put(uuid, fromJson(json));
        return data.get(uuid);
    }

    public static BukkitPlayerData fromJson(String json) {
        return new Gson().fromJson(json, BukkitPlayerData.class);
    }


}
