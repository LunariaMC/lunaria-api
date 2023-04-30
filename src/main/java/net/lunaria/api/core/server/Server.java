package net.lunaria.api.core.server;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import net.lunaria.api.core.redis.RedisDBIndex;
import net.lunaria.api.core.redis.RedisManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class Server {
    protected static Map<String, Server> serverNameMap = new ConcurrentHashMap<>();

    private UUID uuid;
    private String name;
    private String templateName;
    private int port;
    private Environment environment;

    private boolean running = false;
    private File serverDirectory;

    private long createdMillis;

    public Server(String name, String templateName, int port, Environment environment) {
        this.name = name;
        this.templateName = templateName;
        this.port = port;
        this.environment = environment;

        this.createdMillis = System.currentTimeMillis();
    }

    public void storeProperties() {
        File propertiesFile = new File(serverDirectory, "server.properties");

        if (!propertiesFile.exists()) {
            try {
                propertiesFile.createNewFile();

                Properties properties = new Properties();
                properties.load(Files.newInputStream(propertiesFile.toPath()));

                properties.put("online-mode", "false");
                properties.put("online_mode", "false");
                properties.put("max-players", String.valueOf(environment.getPlayers()));
                properties.put("server-port", String.valueOf(port));
                properties.put("server-ip", "127.0.0.1");
                properties.put("motd", "LUNARIA SERVER");
                properties.put("announce-player-achievements", "false");
                properties.put("allow-nether", "false");
                properties.put("SM_ENVIRONMENT", environment.name());
                properties.put("SM_TEMPLATE", templateName);
                properties.put("SM_NAME", name);
                properties.put("server-name", name);
                properties.store(Files.newOutputStream(propertiesFile.toPath()), "Minecraft server properties");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void generateStartFile() {
        File startFile = new File(serverDirectory, "start.sh");

        if (!startFile.exists()) {
            try {
                startFile.createNewFile();

                String startFileContent = "nice -n 4 screen -dmS " + this.name + " java -Xmx" + environment.getRam() + "G -jar -Dlog4j2.formatMsgNoLookups=true spigot.jar";
                Files.write(startFile.toPath(), startFileContent.getBytes());
                startFile.setExecutable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        File startFile = new File(serverDirectory, "start.sh");

        if (!startFile.exists()) generateStartFile();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("sh", startFile.getAbsolutePath());
            processBuilder.directory(serverDirectory);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            RedisManager.set("Server." + name, new Gson().toJson(this), RedisDBIndex.SERVER_CACHE.getIndex());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        ProcessBuilder processBuilder = new ProcessBuilder("screen", "-S", this.name, "-X", "stuff", "stop\\r");

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Server fromName(String name) {
        return serverNameMap.get(name);
    }
    public static Server fromRedis(String serverName) {
        String json = RedisManager.get("Server." + serverName, RedisDBIndex.SERVER_CACHE.getIndex());
        return new Gson().fromJson(json, Server.class);
    }
}
