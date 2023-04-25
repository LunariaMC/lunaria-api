package net.lunaria.api.plugins.bukkit.player;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.lunaria.api.core.account.AccountManager;
import net.lunaria.api.core.connector.MongoConnector;
import net.lunaria.api.core.redis.RedisDBIndex;
import net.lunaria.api.core.redis.RedisManager;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayerManager extends AccountManager {
    @Override
    public BukkitPlayer getAccountFromMongo(UUID uuid) {
        MongoCollection<Document> collection = MongoConnector.getConnection().getCollection("players");
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) return null;

        return new Gson().fromJson(document.toJson(), BukkitPlayer.class);
    }

    @Override
    public BukkitPlayer getAccountFromRedis(UUID uuid) {
        String json = RedisManager.get("Player." + uuid, RedisDBIndex.ACCOUNT_CACHE.getIndex());

        return new Gson().fromJson(json, BukkitPlayer.class);
    }

    public static BukkitPlayer getAccount(UUID uuid) {
        BukkitPlayer bukkitPlayer;

        bukkitPlayer = BukkitPlayer.fromUuid(uuid);
        if (bukkitPlayer != null) return bukkitPlayer;

        bukkitPlayer = new BukkitPlayerManager().getAccountFromRedis(uuid);
        if (bukkitPlayer != null) return bukkitPlayer;

        bukkitPlayer = new BukkitPlayerManager().getAccountFromMongo(uuid);
        return bukkitPlayer;
    }

    public static BukkitPlayer getAccount(Player player) {
        BukkitPlayer bukkitPlayer;

        bukkitPlayer = BukkitPlayer.fromPlayer(player);
        if (bukkitPlayer != null) return bukkitPlayer;

        bukkitPlayer = new BukkitPlayerManager().getAccountFromRedis(player.getUniqueId());
        if (bukkitPlayer != null) return bukkitPlayer;

        bukkitPlayer = new BukkitPlayerManager().getAccountFromMongo(player.getUniqueId());
        return bukkitPlayer;
    }


    public static void saveAccount(BukkitPlayer bukkitPlayer) {
        storeInRedis(bukkitPlayer, bukkitPlayer.getUuid());
        storeInMongo(bukkitPlayer, bukkitPlayer.getUuid());
    }
}
