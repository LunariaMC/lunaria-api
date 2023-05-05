package net.lunaria.api.core.maintenance.redis;

import net.lunaria.api.core.cache.redis.single.SingleUpdateRedisMessage;
import net.lunaria.api.core.maintenance.Maintenance;

public class MaintenanceRedisMessage extends SingleUpdateRedisMessage<Maintenance> {
    public MaintenanceRedisMessage() {
    }

    public MaintenanceRedisMessage(Maintenance element) {
        super(element);
    }
}
