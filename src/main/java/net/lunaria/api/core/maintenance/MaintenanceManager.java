package net.lunaria.api.core.maintenance;

import lombok.Getter;
import net.lunaria.api.core.cache.redis.single.IRedisSingleCache;
import net.lunaria.api.core.maintenance.redis.MaintenanceRedisMessage;

import java.lang.reflect.Type;

public class MaintenanceManager implements IRedisSingleCache<Maintenance, MaintenanceRedisMessage> {
    private static final String REDIS_KEY = "maintenance_infos";

    private static @Getter Maintenance maintenance;

    private static @Getter MaintenanceManager instance;

    public MaintenanceManager() {
        instance = this;

        this.getCacheFromRedis();
        if (maintenance == null) {
            maintenance = new Maintenance();
            maintenance.nameWhitelist.add("Papipomme");
            this.updateCacheEverywhere();
        }
    }

    @Override
    public Maintenance getCache() {
        return maintenance;
    }

    @Override
    public void setCache(Maintenance _maintenance) {
        maintenance = _maintenance;
    }

    @Override
    public Type getElementType() {
        return Maintenance.class;
    }

    @Override
    public Class<MaintenanceRedisMessage> getUpdateRedisMessageClass() {
        return MaintenanceRedisMessage.class;
    }

    @Override
    public MaintenanceRedisMessage createBlankRedisMessage() {
        return new MaintenanceRedisMessage();
    }

    @Override
    public String getRedisKey() {
        return REDIS_KEY;
    }
}
