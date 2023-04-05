package net.lunaria.api.core.account;

import lombok.Data;
import net.lunaria.api.core.enums.Rank;

@Data
public class Subscription {
    boolean subscribed;

    String prefix = Rank.PLAYER.getPrefix();
    String suffix = "§e✯";

    String nameColor;

    String joinMessage = "a rejoint le lobby !";

    // Queue priority

    int friendsLimit = 10;
    int groupLimit = 5;

    int hostsNumber = 5;

    boolean useNick = false;

    public Subscription(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
