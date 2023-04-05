package net.lunaria.api.plugins.bukkit.utils.nametag;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.function.Consumer;

public final class NametagUtil {

    private static final int MAX_CHAR_SIZE = 16;
    private static final Map<Player, String> PLAYER_NAME_TEAM = new HashMap<>();
    private static final Map<Player, Map<Player, NameTagInfo>> PLAYERS_TAG_INFO = new HashMap<>();

    public static void setBukkitNameTag(Player player, int priority, String prefix, String suffix){

        String teamName;
        if(!PLAYER_NAME_TEAM.containsKey(player)) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            teamName = priority + "-" + (uuid.substring(0, MAX_CHAR_SIZE - (Integer.toString(priority).length() + 1)));
            PLAYER_NAME_TEAM.put(player, teamName);
        } else teamName = PLAYER_NAME_TEAM.get(player);

        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(teamName);
        if(team == null) team = scoreboard.registerNewTeam(teamName);

        if(prefix.length() > MAX_CHAR_SIZE) prefix = prefix.substring(0, MAX_CHAR_SIZE);
        if(suffix.length() > MAX_CHAR_SIZE) suffix = suffix.substring(0, MAX_CHAR_SIZE);

        team.setPrefix(prefix);
        team.setSuffix(suffix);

        if(!team.hasEntry(player.getName())) team.addEntry(player.getName());

    }

    public static void setPacketNameTag(Player player, List<Player> players, int priority, String prefix, String suffix){
        setPacketNameTag(player, players, priority, prefix, suffix, null);
    }

    public static void setPacketNameTag(Player player, List<Player> players, int priority, String prefix, String suffix, Consumer<ScoreboardTeam> teamConsumer){

        String uuid = UUID.randomUUID().toString().replace("-", "");
        String teamName = priority + "-" + (uuid.substring(0, MAX_CHAR_SIZE - (Integer.toString(priority).length() + 1)));

        ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) player.getScoreboard()).getHandle(), teamName);
        if(prefix.length() > MAX_CHAR_SIZE) prefix = prefix.substring(0, MAX_CHAR_SIZE);
        if(suffix.length() > MAX_CHAR_SIZE) suffix = suffix.substring(0, MAX_CHAR_SIZE);

        final String finalPrefix = prefix;
        final String finalSuffix = suffix;

        team.setPrefix(finalPrefix);
        team.setSuffix(finalSuffix);
        if(teamConsumer != null) teamConsumer.accept(team);

        List<String> playerNames = new ArrayList<>();
        NameTagInfo nameTagInfo = new NameTagInfo(prefix, suffix, priority);

        for(Player lplayer : players) {
            playerNames.add(lplayer.getName());

            if(!PLAYERS_TAG_INFO.containsKey(player)) PLAYERS_TAG_INFO.put(player, new HashMap<>());
            Map<Player, NameTagInfo> nameTagInfoMap = PLAYERS_TAG_INFO.get(player);
            nameTagInfoMap.put(lplayer, nameTagInfo);
        }

        List<Packet<?>> packets = new ArrayList<>();
        packets.add(new PacketPlayOutScoreboardTeam(team, 1));
        packets.add(new PacketPlayOutScoreboardTeam(team, 0));
        packets.add(new PacketPlayOutScoreboardTeam(team, playerNames, 3));


        CraftPlayer craftPlayer = (CraftPlayer) player;
        for(Packet<?> packet : packets)
            craftPlayer.getHandle().playerConnection.sendPacket(packet);

    }

    public static void resetNameTag(Player player){
        for(Team team : player.getScoreboard().getTeams()) if(team.hasEntry(player.getName())) {
            team.removeEntry(player.getName());
            break;
        }
    }

    public static void clearTeams(){
        for(Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) team.unregister();
    }

    public static NameTagInfo getNameTagInfo(Player player, Player target){
        if(!PLAYERS_TAG_INFO.containsKey(player)) return null;
        return PLAYERS_TAG_INFO.get(player).get(target);
    }

    public static class NameTagInfo {

        private final String prefix;
        private final String suffix;
        private final int priority;

        public NameTagInfo(String prefix, String suffix, int priority) {
            this.prefix = prefix;
            this.suffix = suffix;
            this.priority = priority;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        public int getPriority() {
            return priority;
        }

    }

    /*public static class EventListener implements Listener {

        @EventHandler
        public void onQuit(PlayerQuitEvent event){
            NameTagManager.getInstance().playerNameTeam.remove(event.getPlayer());
        }

    }*/

}