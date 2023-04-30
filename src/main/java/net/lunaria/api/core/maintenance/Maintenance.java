package net.lunaria.api.core.maintenance;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Maintenance {
    public boolean active = true;
    public ArrayList<String> nameWhitelist = new ArrayList<>();
}
