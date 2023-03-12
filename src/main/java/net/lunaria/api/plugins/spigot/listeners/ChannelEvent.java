package net.lunaria.api.plugins.spigot.listeners;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChannelEvent extends Event {
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    private String queue;
    private String message;

    public ChannelEvent(String queue, String message){
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
