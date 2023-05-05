package net.lunaria.api.core.redis.messaging;

import lombok.Getter;
import net.lunaria.api.core.redis.RedisManager;
import net.lunaria.api.plugins.bukkit.BukkitAPI;

public class RedisMessage {

    private transient String content;
    private final transient @Getter String channel;

    private String classPath;
    private String from;

    public RedisMessage() {
        this.classPath = getClass().getCanonicalName();
        this.channel = "Update";
    }

    public void publish(){
        RedisManager.getInstance().publishRedisMessage(this);
    }

    public boolean isFromInstance(){
        return BukkitAPI.getServerName().equalsIgnoreCase(this.from);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
