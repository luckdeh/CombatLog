package me.luckdeh.combatlog.utils;

import me.luckdeh.combatlog.CombatLog;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.entity.Player;

public class CountdownHandler {

    TimerHandler timerHandler = TimerHandler.getInstance();
    CombatLog plugin = CombatLog.getInstance();

    public void performCountdown(UUID playerUUID, Player player) {

        plugin.getServer().getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> {

            //If the timer does not exist/is stopped, then end this countdown task.
            if (timerHandler.getCombatTimer(playerUUID).isNaN() || player == null) {
                timerHandler.stopCombatTimer(playerUUID);
                scheduledTask.cancel();
                return;
            }

            Float combatTime = timerHandler.getCombatTimer(playerUUID);
            timerHandler.setCombatTimer(playerUUID, combatTime-0.1f);

            //If the timer is less then zero, stop the countdown.
            if (combatTime < 0f) {
                timerHandler.stopCombatTimer(playerUUID);
                scheduledTask.cancel();
                return;
            }

            //Send the time to the players.
            player.sendMessage(combatTime.toString());

        }, 0,100, TimeUnit.MILLISECONDS);

    }
}
