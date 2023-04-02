package net.lunaria.api.plugins.bukkit.player;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.lunaria.api.core.account.Account;
import net.lunaria.api.core.account.AccountManager;
import net.lunaria.api.core.connectors.MongoConnector;
import net.lunaria.api.core.redis.RedisDBIndex;
import net.lunaria.api.core.redis.RedisManager;
import org.bson.Document;

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
}
