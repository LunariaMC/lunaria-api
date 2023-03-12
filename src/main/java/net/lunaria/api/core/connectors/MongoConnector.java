package net.lunaria.api.core.connectors;

import net.lunaria.api.core.config.Config;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnector {

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

        Config config = new Config(Config.isSpigot(), ConnectorEnum.MONGODB);

        String address = (String) config.getData(ConnectorConfigFieldsEnum.ADDRESS);
        String username = (String) config.getData(ConnectorConfigFieldsEnum.USERNAME);
        String database = (String) config.getData(ConnectorConfigFieldsEnum.DATABASE);
        int port = (int) config.getData(ConnectorConfigFieldsEnum.PORT);
        String password = (String) config.getData(ConnectorConfigFieldsEnum.PASSWORD);

        mongoClient = new MongoClient(new MongoClientURI("mongodb://"+ username +":"+ password +"@"+ address +":"+ port +"/?authSource=lunaria"));
        connection = mongoClient.getDatabase(database);
        System.out.println("Class » " + MongoConnector.class.getSimpleName() + " I: MongoDB s'est bien connecté.");
    }

    public static void disconnect(){

        try {
            if (MongoConnector.isOnline()){
                mongoClient.close();
            }
        } catch (NullPointerException ignored){ }

    }

    public static MongoCollection<Document> getAccount(){
        if(MongoConnector.getConnection() == null){
            new MongoConnector().disconnect();
            new MongoConnector().init();
        }
        return MongoConnector.getConnection().getCollection("account");
    }

}
