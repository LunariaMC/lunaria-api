package net.lunaria.api.core.enums;

import lombok.Getter;

public enum Prefixs {
    LUNARIA("§bLunaria §8❘§f "),
    MAINTENANCE("§6Maintenance §8❘§f ");

    private final @Getter String prefix;
    Prefixs(String prefix) {
        this.prefix = prefix;
    }
}
