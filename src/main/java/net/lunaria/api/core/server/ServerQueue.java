package net.lunaria.api.core.server;

import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerQueue {
    private static Map<Environment, ServerQueue> queues = new ConcurrentHashMap<>();

    private @Getter @Setter Environment environment;
    private Set<String> templatesQueue = new ConcurrentSet<>(); // stores templates to be created

    public ServerQueue(Environment environment) {
        this.environment = environment;
        queues.put(environment,this);
    }

    public void addTemplate(String template) {
        templatesQueue.add(template);
    }

    public void queueAllTemplates() {
        File templates = new File(ServerManager.TEMPLATES_DIR + environment.name() + "/");
        templatesQueue.addAll(Arrays.asList(Objects.requireNonNull(templates.list())));
    }

    public void startQueue(ServerManager serverManager) {
        new Thread(() -> {
            for (String serverTemplate : templatesQueue) {
                serverManager.createServer(serverTemplate, this.environment);
                templatesQueue.remove(serverTemplate);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static ServerQueue fromEnvironment(Environment environment) {
        return queues.get(environment);
    }
}
