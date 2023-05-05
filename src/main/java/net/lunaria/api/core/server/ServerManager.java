package net.lunaria.api.core.server;

import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.Setter;
import net.lunaria.api.core.redis.RedisManager;
import net.lunaria.api.core.utils.FileCopy;
import net.md_5.bungee.api.ProxyServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.*;

public class ServerManager {
    private static final int REDIS_INDEX = 2;

    protected static @Getter Set<Server> serverSet = new HashSet<>();
    protected static @Getter @Setter Set<Integer> usedPorts = new HashSet<>();

    protected static final String SERVERS_DIR = "../servers/";
    protected static final String PLUGINS_DIR = "../servers/plugins/";
    protected static final String TEMPLATES_DIR = "../servers/templates/";
    protected static final String RUNNING_DIR = "../servers/running/";

    public void init() {
        File serversFile = new File(SERVERS_DIR);
        if (!serversFile.exists()) serversFile.mkdir();

        File runningFile = new File(RUNNING_DIR);
        if (!runningFile.exists()) runningFile.mkdir();

        for (Environment env : Environment.values()) {
            File envPlugins = new File(PLUGINS_DIR + env.name() + "/");
            if (!envPlugins.exists()) envPlugins.mkdirs();

            File envTemplates = new File(TEMPLATES_DIR + env.name() + "/");
            if (!envTemplates.exists()) envTemplates.mkdirs();
        }
    }

    public void createServer(String templateName, Environment environment) {
        String serverName = getNextServerName(templateName);
        serverName = environment.name().toLowerCase() + "." + serverName;

        int port = new Random().nextInt(65564 - 25566) + 25566;
        while (usedPorts.contains(port)) {
            port = new Random().nextInt(65564 - 25566) + 25566;
        }
        if (serverName.equals("prod.lobby1")) port = 25564;
        usedPorts.add(port);

        Server server = new Server(serverName, templateName, port, environment);
        Server.serverNameMap.put(serverName, server);

        serverSet.add(server);

        copyServer(templateName, server);
        File file = new File(RUNNING_DIR + serverName);
        server.setServerDirectory(file);

        if (!ProxyServer.getInstance().getServers().containsKey(serverName))
            ProxyServer.getInstance().getServers().put(serverName, ProxyServer.getInstance().constructServerInfo(serverName, new InetSocketAddress("localhost", port), "Generated server", false));
        server.storeProperties();
        server.generateStartFile();

        server.start();
    }


    public void deleteServer(String serverName) {
        RedisManager.getInstance().deleteKey("Server." + serverName, REDIS_INDEX);
        Server server = Server.fromName(serverName);
        if (server == null) return;

        server.stop();
        FileCopy.delete(server.getServerDirectory());
        serverSet.remove(server);
        Server.serverNameMap.remove(serverName);
        usedPorts.remove(server.getPort());

        ProxyServer.getInstance().getServers().remove(serverName);
    }

    public void deleteUnknownServer(String serverName) {
        RedisManager.getInstance().deleteKey("Server." + serverName, REDIS_INDEX);
        ProcessBuilder processBuilder = new ProcessBuilder("screen", "-S", serverName, "-X", "stuff", "stop\\r");
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

        File serverFile = new File(RUNNING_DIR + serverName);
        if (serverFile.exists()) FileCopy.delete(serverFile);
    }

    private String getNextServerName(String templateName) {
        Set<String> templateServersNames = new ConcurrentSet<>();

        for (Server server : serverSet) {
            if (server.getTemplateName().equalsIgnoreCase(templateName)) {
                templateServersNames.add(server.getName());
            }
        }
        if (templateServersNames.size() == 0) return templateName + "1";

        for (String serverName : templateServersNames) {
            templateServersNames.remove(serverName);
            serverName = serverName.substring(serverName.indexOf(".") + 1);
            templateServersNames.add(serverName);
        }
        ArrayList<String> templateServersNamesList = new ArrayList<>(templateServersNames);
        Collections.sort(templateServersNamesList);

        for (String serverName : templateServersNamesList) {
            if (!templateServersNamesList.contains(templateName + "1")) {
                return templateName + "1";
            }
            int id = Integer.parseInt(serverName.replaceAll("[\\D]", "").replaceAll("[.]", "")) + 1;

            if (!templateServersNamesList.contains(templateName + id)) {
                return templateName + id;
            }
        }
        return null;
    }

    private void copyServer(String templateName, Server server) {
        File serverFile = new File(RUNNING_DIR + server.getName());
        if (!serverFile.exists()) serverFile.mkdir();

        FileCopy.copy(
                new File(PLUGINS_DIR + server.getEnvironment().name() + "/"),
                new File(RUNNING_DIR + server.getName() + "/plugins/")
        );
        FileCopy.copy(
                new File(TEMPLATES_DIR + server.getEnvironment().name() + "/" + templateName),
                new File(RUNNING_DIR + server.getName())
        );
    }
}
