package net.lunaria.api.plugins.bukkit.data;

import org.bukkit.entity.Player;

import java.util.UUID;

@lombok.Data
public class Data {

    private Player player;
    private String name;
    private UUID uuid;
    private Float lunes;
    private Float planetes;
    private int powerMOD;
    private int powerVIP;
    private int level;
    private Float xp;

}
