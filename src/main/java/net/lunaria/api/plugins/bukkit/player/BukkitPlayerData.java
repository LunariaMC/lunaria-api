package net.lunaria.api.plugins.bukkit.player;

import org.bukkit.entity.Player;

import java.util.UUID;

@lombok.Data
public class BukkitPlayerData {
    private Player player;
    private String name;
    private UUID uuid;
    private float lunes;
    private float planetes;
    private int powerMOD;
    private int powerVIP;
    private int level;
    private float xp;
}
