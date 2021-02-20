package de.lama.banmanager.main;

import de.lama.banmanager.commands.BanCommands;
import de.lama.banmanager.listeners.JoinListener;
import de.lama.banmanager.listeners.PlayerLoginListener;
import de.lama.banmanager.mysql.MySQL;
import de.lama.banmanager.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public String prefix;
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        registerEvents();
        registerCommands();
        FileManager.setStandardConfig();
        FileManager.setStandardMySQL();
        FileManager.readConfig();
        FileManager.readMySQL();
        MySQL.connect();
        MySQL.createTable();
        Bukkit.getConsoleSender().sendMessage(prefix +"§cPlugin enabled!");
    }


    @Override
    public void onDisable() {
        MySQL.close();
    }

    private void registerCommands() {
        BanCommands banCMD = new BanCommands(this);
        getCommand("ban").setExecutor(banCMD);
        getCommand("tempban").setExecutor(banCMD);
        getCommand("unban").setExecutor(banCMD);
        getCommand("check").setExecutor(banCMD);
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerLoginListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }
}
