package net.lunaria.api.core.account;

import lombok.Data;
import net.lunaria.api.core.enums.Rank;

import java.util.UUID;

@Data
public class Account {
    UUID uuid;

    String playerName;
    String lowerName;

    Rank rank = Rank.PLAYER;
    int powerMod = 0;
    Subscription subscription = new Subscription(false);

    int level = 1;
    long exp = 0L;

    float lunes = 0f;
    float planetes = 0f;

    long firstSeen = System.currentTimeMillis();
    long lastSeen = System.currentTimeMillis();
    long playTime = 0L;
}
