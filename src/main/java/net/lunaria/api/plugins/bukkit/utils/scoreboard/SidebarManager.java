package net.lunaria.api.plugins.bukkit.utils.scoreboard;

import net.lunaria.api.plugins.bukkit.player.BukkitPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SidebarManager {
    Map<BukkitPlayer, Sidebar> sidebars = new HashMap<>();
    Map<BukkitPlayer, Sideline> sidelines = new HashMap<>();

    @SuppressWarnings("unused")
    private TimeProvider timeProvider = null;

    public synchronized void update() {
        for (Map.Entry<BukkitPlayer, Sidebar> e : sidebars.entrySet()) {
            BukkitPlayer p = e.getKey();
            Sidebar sb = e.getValue();
            Sideline sl = sidelines.get(e.getKey());

            SidebarEditor editor = new SidebarEditor(sb, sl, "JumpLeague");
            build(p, editor);
            editor.send();
        }
    }
    @Deprecated
    public void gameSec(Integer time) {
        update();
    }

    @Deprecated
    public void gameUpdate() {
        update();
    }

    @Deprecated
    public void ingame(Sidebar sb, Sideline sl, UUID p) {
    }

    @Deprecated
    public void finished(Sidebar sb, Sideline sl, UUID p) {
    }

    public void build(BukkitPlayer p, SidebarEditor e) {
    }

    public void join(BukkitPlayer p) {
        Sidebar sb = new Sidebar(p);
        sidebars.put(p, sb);
        sidelines.put(p, new Sideline(sb));
    }

    public void leave(BukkitPlayer p) {
        Sidebar sb = sidebars.remove(p);
        if (sb != null) {
            sb.remove();
        }
        sidelines.remove(p);
    }

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public interface TimeProvider {

        Integer getTime();
    }

    public class SidebarEditor {
        Sidebar sb;
        Sideline sl;
        String title;
        Boolean flush = false;

        SidebarEditor(Sidebar sb, Sideline sl, String defaultTitle) {
            this.sb = sb;
            this.sl = sl;
            this.title = defaultTitle;
        }

        public void add(String str) {
            sl.add(str);
            flush = true;
        }

        public void setAt(Integer i, String str) {
            sl.set(i, str);
        }

        public void setByScore(String str, Integer score) {
            sb.setLine(str, score);
        }

        public void clear() {
            sl.clear();
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getRemainingSize() {
            return sl.getRemainingSize();
        }

        void send() {
            if (flush)
                sl.flush();
            else
                sb.send();
            sb.setName(title);
        }
    }
}
