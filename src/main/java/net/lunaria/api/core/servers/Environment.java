package net.lunaria.api.core.servers;

import lombok.Getter;

public enum Environment {
    PROD(3,250),DEV(2,250);

    private final @Getter int ram;
    private final @Getter int players;
    Environment(int ram, int players) {
        this.ram = ram;
        this.players = players;
    }
}
