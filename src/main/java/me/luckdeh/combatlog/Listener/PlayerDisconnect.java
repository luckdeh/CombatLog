package me.luckdeh.combatlog.Listener;


import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PlayerDisconnect implements Listener {
    private final TimerHandler timerHandler;

    public PlayerDisconnect(JavaPlugin plugin) {
        this.timerHandler = TimerHandler.getInstance();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        if (timerHandler.isPlayerTagged(playerUUID)) {
            event.getPlayer().setHealth(0.0);
            timerHandler.stopCombatTimer(playerUUID);
        }
    }
}

