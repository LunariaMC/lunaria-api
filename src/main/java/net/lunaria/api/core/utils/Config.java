package net.lunaria.api.core.utils;

import net.lunaria.api.plugins.bungee.config.ConfigGen;
import net.lunaria.api.core.API;
import net.md_5.bungee.config.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private ConfigConnector configConnector;
    private FileConfiguration fileConfiguration;
    private Configuration configuration;

    private static boolean isSpigot;
    public static String packagePlugin = "net.lunaria.api.plugins";

    public static boolean isSpigot() {
        return isSpigot;
    }

    public static void setIsSpigot(boolean isSpigot) {
        Config.isSpigot = isSpigot;
    }

    public Config(boolean isSpigot, ConfigConnector configConnector){
        this.configConnector = configConnector;
        if(isSpigot){
            fileConfiguration = API.getInstance().getConfig();
        } else {
            configuration = ConfigGen.getConfig();
        }
    }

    public Object getData(ConfigData configData){
        if(fileConfiguration != null){
            if(!configData.getType().equals("Integer")){
                return fileConfiguration.getString(configConnector.getName() + "." + configData.name().toLowerCase());
            } else {
                return fileConfiguration.getInt(configConnector.getName() + "." + configData.name().toLowerCase());
            }
        } else if(!configData.getType().equals("Integer")){
            return configuration.getString(configConnector.getName() + "." + configData.name().toLowerCase());
        } else {
            return configuration.getInt(configConnector.getName() + "." + configData.name().toLowerCase());
        }

    }
}
