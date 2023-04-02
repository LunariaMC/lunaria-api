package net.lunaria.api.plugins.bukkit.player;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.lunaria.api.core.account.Account;
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
}
