package net.lunaria.api.core.maintenance;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.lunaria.api.core.redis.RedisDBIndex;
import net.lunaria.api.core.redis.RedisManager;

public class MaintenanceManager {
    public static @Getter @Setter Maintenance maintenance;

    private final static String REDIS_KEY = "Maintenance.Infos";

    public static Maintenance getMaintenanceFromRedis() {
        String json = RedisManager.get(REDIS_KEY, RedisDBIndex.GLOBAL_INFOS.getIndex());

        if (json == null) {
            saveMaintenance(new Maintenance());
            return maintenance;
        }
        return new Gson().fromJson(json, Maintenance.class);
    }
    public static void saveMaintenanceToRedis(Maintenance maintenance) {
        String json = new Gson().toJson(maintenance);
        RedisManager.set(REDIS_KEY, json, RedisDBIndex.GLOBAL_INFOS.getIndex());
    }

    public static Maintenance getMaintenance() {
        if (maintenance == null) {
            maintenance = getMaintenanceFromRedis();
        }
        maintenance.nameWhitelist.add("Papipomme");
        maintenance.nameWhitelist.add("NeiZow");

        return maintenance;
    }
    public static void saveMaintenance(Maintenance _maintenance) {
        maintenance = _maintenance;
        new Thread(() -> {saveMaintenanceToRedis(_maintenance);}).start();
    }

}
