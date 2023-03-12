package net.lunaria.api.core.utils;

public enum ConfigData {

    ADDRESS("String"),
    USERNAME("String"),
    DATABASE("String"),
    PORT("Integer"),
    PASSWORD("String"),
    VIRTUALHOST("String");

    private String type;

    ConfigData(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

