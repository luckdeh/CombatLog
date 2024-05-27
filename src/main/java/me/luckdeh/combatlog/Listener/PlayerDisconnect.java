package me.luckdeh.combatlog.Listener;


import me.luckdeh.combatlog.CombatLog;
import me.luckdeh.combatlog.Handler.TimerHandler;
import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerDisconnect implements Listener {

    private CombatLog plugin = CombatLog.getInstance();

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {

        // If the player is kicked by the server (by admin or by server shutdown), then do not run this event.
        if (Bukkit.getServer().isStopping() || e.getReason().equals(PlayerQuitEvent.QuitReason.KICKED)) {
            return;
        }

        TimerHandler timerHandler = TimerHandler.getInstance();

        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Check if the player is tagged for combat
        if (timerHandler.isPlayerTagged(playerUUID)) {
            timerHandler.stopCombatTimer(playerUUID);

            EntityNPC.spawnVillagerNPC(player.getLocation(), player.getName(), playerUUID);
        }
    }
}