package me.luckdeh.combatlog.Listener;


import me.luckdeh.combatlog.CombatLog;
import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerDisconnect implements Listener {

    CombatLog plugin = CombatLog.getInstance();

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {

        //If the player is kicked by the server (by admin or by server shutdown), then do not run this event.
        if (Bukkit.getServer().isStopping() || e.getReason().equals(PlayerQuitEvent.QuitReason.KICKED)) {
            return;
        }

        TimerHandler timerHandler = TimerHandler.getInstance();

        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        //Spawn an NPC that goes away after the set amount of combat tag.
        if (timerHandler.isPlayerTagged(playerUUID)) {
            player.setHealth(0.0);
            timerHandler.stopCombatTimer(player.getUniqueId());
        }

    }
}

