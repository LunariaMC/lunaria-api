package net.lunaria.api.plugins.bukkit.rabbit;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BukkitChannelEvent extends Event {
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    private String queue;
    private String message;

    public BukkitChannelEvent(String queue, String message){
        this.message = message;
        this.queue = queue;
    }

    public String getMessage() {
        return message;
    }

    public String getQueue() {
        return queue;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }

    public static HandlerList handlerList = new HandlerList();
}
