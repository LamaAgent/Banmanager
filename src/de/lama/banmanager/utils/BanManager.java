package de.lama.banmanager.utils;

import de.lama.banmanager.mysql.MySQL;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class BanManager {

    public static void ban(String uuid, String playername, String reason, long seconds) {
        long end = 0;
        if(seconds == -1) {
            end = -1;
        } else {
            long current = System.currentTimeMillis();
            long millis = seconds*1000;
            end = current+millis;
        }
        MySQL.update("INSERT INTO bans (Spielername, UUID, Ende, Grund) VALUES ('"+playername+"', '"+uuid+"', '"+end+"', '"+reason+"')");
        if(Bukkit.getPlayer(playername) != null) {
            Bukkit.getPlayer(playername).kickPlayer("§cDu wurdest vom Server gebannt!\n" +
                    "\n" +
                    "§3Grund: §e" + getReason(uuid) + "\n" +
                            "\n" +
                            "§3Verbleibende Zeit: §e" + getRemainingTime(uuid) + "\n" +
                                "\n" +
                                "§3Du kannst §c§nkeinen§3 Entbannungsantrag stellen!");
        }
    }

    public static void unban(String uuid) {
        MySQL.update("DELETE FROM BannedPlayers WHERE UUID='"+uuid+"'");
    }

    public static boolean isBanned(String uuid) {
        ResultSet rs = MySQL.getResult("SELECT Ende FROM BannedPlayers WHERE UUID='"+uuid+"'");
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getReason(String uuid) {
        ResultSet rs = MySQL.getResult("SELECT * FROM BannedPlayers WHERE UUID='"+uuid+"'");
        try {
            while(rs.next()) {
                return rs.getString("Grund");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Long getEnd(String uuid) {
        ResultSet rs = MySQL.getResult("SELECT * FROM BannedPlayers WHERE UUID='"+uuid+"'");
        try {
            while(rs.next()) {
                return rs.getLong("Ende");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return null;
    }

    public static List<String> getBannedPlayers() {
        List<String> list = new ArrayList<>();
        ResultSet rs = MySQL.getResult("SELECT * FROM BannedPlayers");
        try {
            while (rs.next()) {
                list.add(rs.getString("Spielername"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getRemainingTime(String uuid) {
        long current = System.currentTimeMillis();
        long end = getEnd(uuid);
        long millis = end - current;
        if(end == -1) {
            return "§4PERMANENT";
        }
        String output = "";
        
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        long weeks = 0;
        while(millis > 1000) {
            millis-=1000;
            seconds++;
        }
        while(seconds > 60) {
            seconds-=60;
            minutes++;
        }
        while(minutes > 60) {
            minutes-=60;
            hours++;
        }
        while(hours > 24) {
            hours-=24;
            days++;
        }
        while(days > 7) {
            days-=7;
            weeks++;
        }
        
        if(!(weeks == 0)) 
            output = "§e" + weeks + " Woche(n) " + days + " Tag(e) " + hours + " Stunde(n) " + minutes + " Minute(n) " + seconds + " Sekunde(n) ";
         else
            if(!(days == 0)) 
                output = "§e" + days + " Tag(e) " + hours + " Stunde(n) " + minutes + " Minute(n) " + seconds + " Sekunde(n) ";
             else
                if(!(hours == 0)) 
                    output = "§e" + hours + " Stunde(n) " + minutes + " Minute(n) " + seconds + " Sekunde(n) ";
                 else
                    if(!(minutes == 0)) 
                        output = "§e" + minutes + " Minute(n) " + seconds + " Sekunde(n) ";
                     else
                        if(!(seconds == 0)) 
                            output = "§e" + seconds + " Sekunde(n) ";
                        
                        return output;
    }
    
}
