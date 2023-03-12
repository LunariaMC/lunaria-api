package net.lunaria.api.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.*;

public class Json {

    private Map<String, String> data;

    public Json() {
        this.data = new HashMap<>();
    }

    public void set(String key, String value)
    {
        this.data.put(key, value);
    }

    public void set(String key, Object obj) {
        this.data.put(key, obj.toString());
    }

    public String get(String key) {
        return this.data.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(this.data.get(key));
    }

    public byte getByte(String key) {
        return Byte.parseByte(this.data.get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(this.data.get(key));
    }

    public short getShort(String key) {
        return Short.parseShort(this.data.get(key));
    }

    public double getDouble(String key) {
        return Double.parseDouble(this.data.get(key));
    }

    public float getFloat(String key) {
        return Float.parseFloat(this.data.get(key));
    }

    public long getLong(String key) {
        return Long.parseLong(this.data.get(key));
    }

    public boolean contains(String key) {
        return this.data.containsKey(key);
    }

    public <T> List<T> getList(Class<T[]> class1, String key) {
        T[] arr = new Gson().fromJson(get(key), class1);
        return Arrays.asList(arr);
    }

    public <T> void set(String key, List<T> list) {
        set(key, new Gson().toJson(list));
    }

    public void delete(String key){
        data.remove(key);
    }


    public <K, V> void set(String key, Map<K, V> map) {
        set(key, new GsonBuilder().serializeNulls().create().toJson(map, new TypeToken<Map<K,V>>(){}.getType()));
    }

    public <K,V> Map<K,V> getMap(Class<K> clazz, Class<V> clazz1, String key){
        return new GsonBuilder().serializeNulls().create().fromJson(get(key), new TypeToken<Map<K,V>>(){}.getType());
    }

    public String toJson() {
        return new Gson().toJson(data);
    }

    @SuppressWarnings("unchecked")
    public void fromString(String json) {
        this.data = new GsonBuilder().serializeNulls().create().fromJson(json, new TypeToken<Map<String,String>>(){}.getType());
    }
}