package net.lunaria.api.plugins.bukkit.player;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.lunaria.api.core.account.Account;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitPlayer extends Account {
    private static Map<UUID, BukkitPlayer> playerMap = new ConcurrentHashMap<>();

    private @Getter @Setter Player player;

    public void cache(UUID uuid) {
        playerMap.put(uuid, this);
    }
    public void uncache() {
        playerMap.remove(this.getUuid());
    }

    public static BukkitPlayer fromPlayer(Player player) {
        return playerMap.get(player.getUniqueId());
    }
    public static BukkitPlayer fromUuid(UUID uuid) {
        return playerMap.get(uuid);
    }

    public String getPrefix() {
        if (getRank().isOverrideSubscriberPrefix()) return getRank().getPrefix();
        return getSubscription().getPrefix();
    }
    public String getSuffix() {
        if (getSubscription().isSubscribed()) return "§8 ❘ " + getSubscription().getSuffix();
        return "";
    }

    @Deprecated
    public int getMaxExp() {
        return (getLevel()+100)*4;
    }
    @Deprecated
    public float getProgressExp() {
        return (float) (getExp() / getMaxExp()) * 100;
    }
    public String getFormattedExp(int totalBars, char symbol, ChatColor[] completedColor, ChatColor[] notCompletedColor) {
        float progress = getProgressExp();
        int bars = (int) (totalBars*progress);
        return "" + completedColor[0] + completedColor[1] + Strings.repeat("" + symbol, bars) + notCompletedColor[0] + notCompletedColor[1] +Strings.repeat("" + symbol, totalBars - bars);
    }
}
