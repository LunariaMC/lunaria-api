package net.lunaria.api.core.connectors;

import net.lunaria.api.core.utils.Config;
import net.lunaria.api.core.utils.ConfigConnector;
import net.lunaria.api.core.utils.ConfigData;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDB {

    private static MongoDatabase connection;

    public static MongoDatabase getConnection(){
        return connection;
    }

    private static MongoClient mongoClient;

    public static boolean isOnline(){
        try {
            return getConnection() != null;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void init(){

        Config config = new Config(Config.isSpigot(), ConfigConnector.MONGODB);

        String address = (String) config.getData(ConfigData.ADDRESS);
        String username = (String) config.getData(ConfigData.USERNAME);
        String database = (String) config.getData(ConfigData.DATABASE);
        int port = (int) config.getData(ConfigData.PORT);
        String password = (String) config.getData(ConfigData.PASSWORD);

        mongoClient = new MongoClient(new MongoClientURI("mongodb://"+ username +":"+ password +"@"+ address +":"+ port +"/?authSource=lunaria"));
        connection = mongoClient.getDatabase(database);
        System.out.println("Class » " + MongoDB.class.getSimpleName() + " I: MongoDB s'est bien connecté.");
    }

    public static void disconnect(){

        try {
            if (MongoDB.isOnline()){
                mongoClient.close();
            }
        } catch (NullPointerException ignored){ }

    }

    public static MongoCollection<Document> getAccount(){
        if(MongoDB.getConnection() == null){
            new MongoDB().disconnect();
            new MongoDB().init();
        }
        return MongoDB.getConnection().getCollection("account");
    }

}
