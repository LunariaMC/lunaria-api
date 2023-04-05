package net.lunaria.api.plugins.bungee.maintenance;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import net.lunaria.api.core.connectors.MongoConnector;
import org.bson.Document;

public class MaintenanceManager {
    private static Maintenance maintenance;

    public static Maintenance getMaintenance() {
        if (maintenance != null) return maintenance;

        MongoCollection<Document> collection = MongoConnector.getConnection().getCollection("global_infos");
        Document document = collection.find(Filters.eq("MONGO_TYPE", Filters.eq("MAINTENANCE_DATA"))).first();

        if (document == null) {
            maintenance = new Maintenance();
            String json = new Gson().toJson(maintenance);

            collection.insertOne(Document.parse(json));

            return new Gson().fromJson(json, Maintenance.class);
        }

        maintenance = new Gson().fromJson(document.toJson(), Maintenance.class);
        return maintenance;
    }
}
