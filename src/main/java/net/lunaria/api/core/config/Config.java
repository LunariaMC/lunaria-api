package net.lunaria.api.core.config;

import net.lunaria.api.core.connectors.ConnectorEnum;
import net.lunaria.api.core.connectors.ConnectorConfigFieldsEnum;
import net.lunaria.api.plugins.bungee.config.ConfigGen;
import net.lunaria.api.plugins.bukkit.BukkitAPI;
import net.md_5.bungee.config.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private ConnectorEnum connectorEnum;
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

    public Config(boolean isSpigot, ConnectorEnum connectorEnum){
        this.connectorEnum = connectorEnum;
        if(isSpigot){
            fileConfiguration = BukkitAPI.getInstance().getConfig();
        } else {
            configuration = ConfigGen.getConfig();
        }
    }

    public Object getData(ConnectorConfigFieldsEnum connectorConfigFieldsEnum){
        if(fileConfiguration != null){
            if(!connectorConfigFieldsEnum.getType().equals("Integer")){
                return fileConfiguration.getString(connectorEnum.getName() + "." + connectorConfigFieldsEnum.name().toLowerCase());
            } else {
                return fileConfiguration.getInt(connectorEnum.getName() + "." + connectorConfigFieldsEnum.name().toLowerCase());
            }
        } else if(!connectorConfigFieldsEnum.getType().equals("Integer")){
            return configuration.getString(connectorEnum.getName() + "." + connectorConfigFieldsEnum.name().toLowerCase());
        } else {
            return configuration.getInt(connectorEnum.getName() + "." + connectorConfigFieldsEnum.name().toLowerCase());
        }

    }
}
