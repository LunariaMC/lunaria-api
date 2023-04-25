package net.lunaria.api.core.server;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LunariaServerIdentifier {
    private String lsi;

    public static String getLsi(Server server) {
        String environment = server.getEnvironment().name();
        String serverId = server.getName().split("(\\.)")[1];

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(server.getCreatedMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMM-hhmm");
        String time = simpleDateFormat.format(calendar.getTime());

        return environment + serverId + "." + time;
    }
}
