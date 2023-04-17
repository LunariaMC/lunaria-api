package net.lunaria.api.core.enums;

import lombok.Getter;

public enum Prefix {
    LUNARIA("§b§lLUNARIA §8❘§f "),
    MAINTENANCE("§6§lMAINTENANCE §8❘§f ");

    private final @Getter String prefix;
    Prefix(String prefix) {
        this.prefix = prefix;
    }
}
