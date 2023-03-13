package net.lunaria.api.plugins.bukkit.data;

import com.google.gson.Gson;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class LunaPlayer {

    public static HashMap<UUID, Data> data = new HashMap<>();

    //Avec un player
    public static Data getData(Player player){
        return data.get(player.getUniqueId());
    }

    public static Data remove(Player player){
        return data.remove(player.getUniqueId());
    }

    //Avec un uuid
    public static Data getData(UUID uuid){
        return data.get(uuid);
    }

    public static Data remove(UUID uuid){
        return data.remove(uuid);
    }

    //set
    public static Data setData(UUID uuid, String json) {
        data.put(uuid, fromJson(json));
        return data.get(uuid);
    }

    public static Data fromJson(String json) {
        return new Gson().fromJson(json, Data.class);
    }


}
