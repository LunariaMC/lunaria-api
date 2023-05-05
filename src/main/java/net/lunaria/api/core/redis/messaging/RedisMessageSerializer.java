package net.lunaria.api.core.redis.messaging;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Collections;

public class RedisMessageSerializer implements JsonDeserializer<RedisMessage> {

    @Override
    public RedisMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if(!jsonObject.has("classPath")) return null;

        Gson gson = new Gson();
        RedisMessage redisMessage;
        String classPath = jsonObject.get("classPath").getAsString();
        try {

            Class<?> classFound = Class.forName(classPath);
            Class<? extends RedisMessage> classCast = (Class<? extends RedisMessage>) classFound;
            redisMessage = gson.fromJson(jsonObject, classCast);

        } catch (ClassCastException | ClassNotFoundException e){
            redisMessage = new RedisMessage();
        }

        return redisMessage;
    }

}
