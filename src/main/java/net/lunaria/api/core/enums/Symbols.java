package net.lunaria.api.core.enums;

import lombok.Getter;

public enum Symbols {
    BAR("❘"),
    ARROW("»"),
    SQUARE("▪"),
    WARN("⚠"),
    CHECK("✔"),
    CROSS("✖");

    private final @Getter String symbol;
    Symbols(String symbol) {
        this.symbol = symbol;
    }
}
