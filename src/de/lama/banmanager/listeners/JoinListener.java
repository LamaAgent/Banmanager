package de.lama.banmanager.listeners;

import de.lama.banmanager.main.Main;
import de.lama.banmanager.utils.BanManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(BanManager.isBanned(p.getUniqueId().toString())) {
            BanManager.unban(p.getUniqueId().toString());
        }
    }

}
