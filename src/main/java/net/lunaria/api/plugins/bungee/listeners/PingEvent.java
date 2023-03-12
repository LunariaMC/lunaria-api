package net.lunaria.api.plugins.bungee.listeners;

import net.lunaria.api.core.enums.Symbols;
import net.lunaria.api.core.text.CenterText;
import net.lunaria.api.plugins.bungee.maintenance.Maintenance;
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
            List<String> lines = Arrays.asList("§8" + Symbols.ARROW.getSymbol() + " §cUne maintenance est en cours.");
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
        serverPing.setVersion(new ServerPing.Protocol("§c1.8x - 1.13x", serverPing.getVersion().getProtocol()));
        serverPing.setDescriptionComponent(new TextComponent(
                CenterText.centerText("§9§l»§3§l»§f§l» §b§lLunaria§f§oMC §8❘ §eDe retour en v2 §f§l«§3§l«§9§l«\n", 123) +
                CenterText.centerText("§8" + Symbols.SQUARE.getSymbol() + " §fDécollage imminent §8(§e1.8-1.13§8) §8" + Symbols.SQUARE.getSymbol() , 123)
                )
        );

        try{
            serverPing.setFavicon(Favicon.create(ImageIO.read(new File("server-icon.png"))));
        } catch (IOException e1){
            e1.printStackTrace();
        }

        e.setResponse(serverPing);

    }

}
