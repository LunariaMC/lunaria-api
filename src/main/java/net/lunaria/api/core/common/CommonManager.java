package net.lunaria.api.core.common;

import net.lunaria.api.core.maintenance.MaintenanceManager;
import net.lunaria.api.core.redis.RedisManager;

public class CommonManager {
    public static void init() {
        RedisManager redisManager = RedisManager.getInstance();
        redisManager.registerChannel("redis");

        redisManager.registerListener(new MaintenanceManager());
    }
}
