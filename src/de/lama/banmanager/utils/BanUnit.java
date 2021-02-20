package de.lama.banmanager.utils;

import java.util.ArrayList;
import java.util.List;

public enum BanUnit {

    SECOND("Sekunde(n)", 1, "s"),
    MINUTE("Minute(n)", 60, "m"),
    HOUR("Stunde(n)", 60*60, "h"),
    DAY("Tag(e)", 60*60*24, "d"),
    WEEK("Woche(n)", 60*60*24*7, "w");

    private String name;
    private long toSecond;
    private String shortcut;

    private BanUnit(String name, long toSecond, String shortcut) {
        this.name = name;
        this.toSecond = toSecond;
        this.shortcut = shortcut;
    }

    public long getToSecond() {
        return toSecond;
    }

    public String getName() {
        return name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public static List<String> getUnitsAsString() {
        List<String> units = new ArrayList<>();
        for(BanUnit unit : BanUnit.values()) {
            units.add(unit.getShortcut().toLowerCase());
        }
        return units;
    }

    public static BanUnit getUnit(String unit) {
        for(BanUnit units : BanUnit.values()) {
            if(units.getShortcut().equalsIgnoreCase(unit)) {
                return units;
            }
        }
        return null;
    }
}
