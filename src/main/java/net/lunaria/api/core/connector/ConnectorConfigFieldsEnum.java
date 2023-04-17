package net.lunaria.api.core.connector;

public enum ConnectorConfigFieldsEnum {

    ADDRESS("String"),
    USERNAME("String"),
    DATABASE("String"),
    PORT("Integer"),
    PASSWORD("String"),
    VIRTUALHOST("String");

    private String type;

    ConnectorConfigFieldsEnum(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

