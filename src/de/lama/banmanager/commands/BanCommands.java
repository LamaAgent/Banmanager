package de.lama.banmanager.commands;

import de.lama.banmanager.main.Main;
import de.lama.banmanager.utils.BanManager;
import de.lama.banmanager.utils.BanUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BanCommands implements CommandExecutor {

    private final Main plugin;

    public BanCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("ban")) {
            if(args.length >=2) {
                String playername = args[0];
                if(BanManager.isBanned(getUUID(playername))) {
                    sender.sendMessage(plugin.prefix + "§cDieser Spieler ist bereits gebannt!");
                    return true;
                }
                String reason = " ";
                for(int i = 1; i < args.length; i++) {
                    reason += args[i] + " ";
                }
                BanManager.ban(getUUID(playername), playername, reason, -1);
                sender.sendMessage(plugin.prefix + "§7Der Spieler §e" + playername + " §7wurde §4PERMANENT §7gebannt!");
                return true;
            } else
                sender.sendMessage(plugin.prefix + "§c/ban <Spieler> <Grund>");
            return true;
        }

        if(command.getName().equalsIgnoreCase("tempban")) {
            if(args.length >=4) {
                String playername = args[0];
                long value;
                try {
                    value = Integer.valueOf(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(plugin.prefix + "§c<Zahlenwert> muss eine Zahl sein!");
                    return true;
                }

                if(value >= 500) {
                    sender.sendMessage(plugin.prefix + "§c<Zahlenwert> muss unter 500 liegen!");
                    return true;
                }

                String unitString = args[2];
                String reason = " ";
                for(int i = 3; i < args.length; i++) {
                    reason += args[i] + " ";
                }
                List<String> unitList = BanUnit.getUnitsAsString();
                if(unitList.contains(unitString.toLowerCase())) {
                    BanUnit unit = BanUnit.getUnit(unitString);
                    long seconds = value * unit.getToSecond();
                    BanManager.ban(getUUID(playername), playername, reason, seconds);
                    sender.sendMessage(plugin.prefix + "§7Der Spieler §e" + playername + " §7 wurde für §c" + value + unit.getName() + " §7gebannt!");
                    return true;
                } else
                    sender.sendMessage(plugin.prefix + "§cDiese <Zeiteinheit> existiert nicht!");

                return true;
            } else
                sender.sendMessage((plugin.prefix) + "§c/tempban <Spieler> <Zahlenwert> <Zeiteinheit> <Grund>") ;
        }

        if(command.getName().equalsIgnoreCase("check")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("list")) {
                    List<String> list = BanManager.getBannedPlayers();
                    if(list.size() == 0) {
                        sender.sendMessage(plugin.prefix + "§cZurzeit ist kein Spieler gebannt!");
                    }

                    for(String allBanned : BanManager.getBannedPlayers()) {
                        sender.sendMessage(plugin.prefix + "§7---------- §6§lBan-Liste §7----------");
                        sender.sendMessage(plugin.prefix + "§e" + allBanned + " §7(Grund: §r" + BanManager.getReason(getUUID(allBanned)) + "§7)");
                    }
                    return true;
                }
                String playername = args[0];
                sender.sendMessage(plugin.prefix + "§7---------- §6§lBan-Infos §7----------");
                sender.sendMessage(plugin.prefix + "§eName: §r" + playername);
                sender.sendMessage(plugin.prefix + "§eGebannt: §r" + (BanManager.isBanned(getUUID(playername)) ? "§aJa" : "Nein"));
                if(BanManager.isBanned(getUUID(playername))) {
                    sender.sendMessage(plugin.prefix + "§eGrund: §r" + BanManager.getReason(getUUID(playername)));
                    sender.sendMessage(plugin.prefix + "§eVerbleibende Zeit: §r" + BanManager.getRemainingTime(getUUID(playername)));
                    sender.sendMessage(plugin.prefix + "§eUUID: §r" + getUUID(playername));
                }
                return true;
            } else
                sender.sendMessage(plugin.prefix + "§c/check (list) <Spieler>");
        }

        if(command.getName().equalsIgnoreCase("unban")) {
            if(args.length == 1) {
                String playername = args[0];
                if(BanManager.isBanned(getUUID(playername))) {
                    BanManager.unban(getUUID(playername));
                    sender.sendMessage(plugin.prefix + "§7Der Spieler §e" + playername + " §7wurde erfolgreich entbannt!");
                } else
                    sender.sendMessage(plugin.prefix + "§cDieser Spieler ist zurzeit nicht gebannt!");
                return true;
            } else
                sender.sendMessage(plugin.prefix + "§c/unban <Spieler>");

            return true;
        }

        return true;
    }

    private String getUUID(String playername) {

        //noinspection deprecation
        return Bukkit.getOfflinePlayer(playername).getUniqueId().toString();
    }
}
