package net.lunaria.api.core.enums;

import lombok.Getter;

public enum Prefixs {
    LUNARIA("§b§lLUNARIA §8❘§f "),
    MAINTENANCE("§6§lMAINTENANCE §8❘§f ");

    private final @Getter String prefix;
    Prefixs(String prefix) {
        this.prefix = prefix;
    }
}
