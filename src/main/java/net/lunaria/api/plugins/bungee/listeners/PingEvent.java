package net.lunaria.api.plugins.bungee.listeners;

import net.lunaria.api.plugins.bungee.utils.Maintenance;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PingEvent implements Listener {

    @EventHandler
    public void serverPing(ProxyPingEvent e){

        ServerPing.PlayerInfo[] sample = null;
        if(Maintenance.isActive()){
            List<String> lines = Arrays.asList("§8▪ §cServeur en maintenance");
            sample = new ServerPing.PlayerInfo[lines.size()];
            for (int i = 0; i < sample.length; i++) {
                sample[i] = new ServerPing.PlayerInfo(lines.get(i), "");
            }
        }

        final ServerPing serverPing = e.getResponse();
        if(sample != null){
            serverPing.getPlayers().setSample(sample);
        }
        serverPing.setPlayers(new ServerPing.Players(250, ProxyServer.getInstance().getOnlineCount(), serverPing.getPlayers().getSample()));
        serverPing.setVersion(new ServerPing.Protocol("§c1.7x - 1.13x", serverPing.getVersion().getProtocol()));
        serverPing.setDescriptionComponent(new TextComponent("          §e1.8 §7- §e1.13 §8▪ §b§lLunaria§f§oMC §8▪ §eMini-Jeux\n" + "       §f§l» §bSkyFast §8- §3PvPBox §8- §9ComboHit §8- [§7...§8] §f§l«"));

        try{
            serverPing.setFavicon(Favicon.create(ImageIO.read(new File("server-icon.png"))));
        } catch (IOException e1){
            e1.printStackTrace();
        }

        e.setResponse(serverPing);

    }

}
