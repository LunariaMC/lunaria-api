package net.lunaria.api.plugins.bungee.maintenance;

import lombok.Data;

@Data
public class Maintenance {
    String MONGO_TYPE = "MAINTENANCE_DATA";

    boolean active;
    String[] playersNamesWhitelist = {"Papipomme", "NeiZow", "CrypenterTV"};
}
