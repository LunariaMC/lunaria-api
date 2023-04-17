package net.lunaria.api.core.account;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.lunaria.api.core.connector.MongoConnector;
import net.lunaria.api.core.redis.RedisDBIndex;
import net.lunaria.api.core.redis.RedisManager;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.util.UUID;

public class AccountManager {
    public Account getAccountFromMongo(UUID uuid) {
        MongoCollection<Document> collection = MongoConnector.getConnection().getCollection("players");
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) return null;

        JsonWriterSettings relaxed = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();

        return new Gson().fromJson(document.toJson(relaxed), Account.class);
    }
    public Account getAccountFromRedis(UUID uuid) {
        String json = RedisManager.get("Player." + uuid, RedisDBIndex.ACCOUNT_CACHE.getIndex());

        return new Gson().fromJson(json, Account.class);
    }

    public static void storeInRedis(Account account, UUID uuid) {
        String json = new Gson().toJson(account);

        RedisManager.set("Player." + uuid, json, RedisDBIndex.ACCOUNT_CACHE.getIndex());
    }
    public static void storeInMongo(Account account, UUID uuid) {
        String json = new Gson().toJson(account);
        Document document = Document.parse(json);

        MongoCollection<Document> collection = MongoConnector.getConnection().getCollection("players");
        Document existingDocument = collection.find(Filters.eq("uuid", uuid.toString())).first();
        if (existingDocument == null) {collection.insertOne(document); return;}

        collection.replaceOne(Filters.eq("uuid", uuid.toString()), document);
    }
}
