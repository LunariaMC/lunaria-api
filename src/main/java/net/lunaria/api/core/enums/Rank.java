package net.lunaria.api.core.enums;

import lombok.Getter;

public enum Rank {
    ADMIN("§4Admin §4", "§4Admin §4", false),
    RESP("§cResponsable §c", "§cResponsable §c", false),
    MODPLUS("§6Modérateur+ §6", "§6Modératrice+ §6", false),
    MOD("§eModérateur §e", "§eModératrice §e", false),
    HELPER("§aAssistant §a", "§aAssistant §a", false),
    BUILDER("§2Constructeur §2", "§2Constructrice §2", false),
    STAFF("§9Staff §9", "§9Staff §9", false),

    FRIEND("§fAmi §f", "§fAmie §f", false),
    MEDIA("§5Média §5", "§5Média §5", false),

    PLAYER("§7", "§7", true);


    private final @Getter String mascPrefix;
    private final @Getter String femPrefix;
    private final @Getter boolean overrideSubscriberPrefix;

    Rank(String mascPrefix, String femPrefix, boolean overrideSubscriberPrefix) {
        this.mascPrefix = mascPrefix;
        this.femPrefix = femPrefix;
        this.overrideSubscriberPrefix = overrideSubscriberPrefix;
    }
}
