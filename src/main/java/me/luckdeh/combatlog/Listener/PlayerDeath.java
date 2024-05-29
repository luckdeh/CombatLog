package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


import java.util.UUID;

public class PlayerDeath implements Listener {


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getKiller() == null) {
            return;
        }

        TimerHandler timerHandler = TimerHandler.getInstance();

        UUID playerUUID = player.getUniqueId();
        if (timerHandler.isPlayerTagged(playerUUID)) {
            timerHandler.stopCombatTimer(playerUUID);
        }

        //Will be made configurable later.
        //Player killer = player.getKiller();
        //UUID killerUUID = killer.getUniqueId();
        //if (timerHandler.isPlayerTagged(killerUUID)) {
        //    timerHandler.stopCombatTimer(killerUUID);
        //}
    }
}
