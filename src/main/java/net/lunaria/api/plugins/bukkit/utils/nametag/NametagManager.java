package net.lunaria.api.plugins.bukkit.utils.nametag;

import net.lunaria.api.plugins.bukkit.player.BukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;

public class NametagManager {
    public static void setNameTag() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BukkitPlayer bukkitPlayer = BukkitPlayer.fromUuid(player.getUniqueId());

            for (Player target : Bukkit.getOnlinePlayers()) {
                String prefix = bukkitPlayer.getPrefix();

                NametagUtil.setPacketNameTag(target, Collections.singletonList(player), bukkitPlayer.getRank().ordinal(), prefix, bukkitPlayer.getSuffix());
            }
        }
    }
}
