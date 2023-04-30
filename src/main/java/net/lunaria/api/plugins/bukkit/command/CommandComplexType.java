package net.lunaria.api.plugins.bukkit.command;

import java.util.ArrayList;

public interface CommandComplexType {
    boolean runWhenCalled(String[] args, ArrayList<Object> complexArgsList, int index);
}
