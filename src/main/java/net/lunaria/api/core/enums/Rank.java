package net.lunaria.api.core.enums;

import lombok.Getter;

import java.util.Arrays;

public enum Rank {
    ADMIN("§4Admin §4", true, 7),
    RESP("§cResponsable §c", true, 6),
    MODPLUS("§6Modérateur+ §6", true, 5),
    MOD("§eModérateur §e", true, 4),
    HELPER("§aAssistant §a", true, 3),
    BUILDER("§2Constructeur §2", true, 2),
    STAFF("§9Staff §9", true, 1),

    FRIEND("§fAmi §f", true, 0),
    MEDIA("§5Média §5", true, 0),

    PLAYER("§7Joueur §7", false, 0);


    private final @Getter String prefix;
    private final @Getter boolean overrideSubscriberPrefix;
    private final @Getter int powerMod;

    Rank(String prefix, boolean overrideSubscriberPrefix, int powerMod) {
        this.prefix = prefix;
        this.overrideSubscriberPrefix = overrideSubscriberPrefix;
        this.powerMod = powerMod;
    }

    public static Rank fromPower(int requiredPower){
        for (Rank value : Rank.values()) {
            if(value.getPowerMod() == requiredPower){
                return value;
            }
        }
        return null;
    }

}
