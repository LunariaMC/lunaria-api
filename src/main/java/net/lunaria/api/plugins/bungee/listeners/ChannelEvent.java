package net.lunaria.api.plugins.bungee.listeners;

import net.md_5.bungee.api.plugin.Event;

public class ChannelEvent extends Event {

    private String queue;
    private String message;

    public ChannelEvent(String queue, String message){
        this.queue = queue;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getQueue() {
        return queue;
    }
}
