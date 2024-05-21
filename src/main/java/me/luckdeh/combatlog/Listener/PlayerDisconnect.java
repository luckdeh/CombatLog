package me.luckdeh.combatlog.Listener;


import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerDisconnect implements Listener {

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        TimerHandler timerHandler = TimerHandler.getInstance();
        UUID playerUUID = e.getPlayer().getUniqueId();

        if (timerHandler != null && playerUUID != null && !timerHandler.isPlayerTagged(playerUUID)) {
            e.getPlayer().setHealth(0.0);
            timerHandler.stopCombatTimer(playerUUID);
        }
    }
}

