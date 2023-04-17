package net.lunaria.api.core.enums;

import lombok.Getter;

public enum Symbol {
    BAR("❘"),
    ARROW("»"),
    SQUARE("▪"),
    WARN("⚠"),
    CHECK("✔"),
    CROSS("✖"),
    LINE("§m--------------------------------------------");

    private final @Getter String symbol;
    Symbol(String symbol) {
        this.symbol = symbol;
    }
}
