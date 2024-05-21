package me.luckdeh.combatlog.utils;

import me.luckdeh.combatlog.CombatLog;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.entity.Player;

public class Countdown {

    TimerHandler timerHandler = TimerHandler.getInstance();
    CombatLog plugin = CombatLog.getInstance();

    DecimalFormat decimalFormat = new DecimalFormat("0.0");

    public void performCountdown(UUID playerUUID, Player player) {

        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        plugin.getServer().getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> {

            //If the timer does not exist/is stopped, then end this countdown task.
            if (timerHandler.getCombatTimer(playerUUID).isNaN() || player == null) {
                timerHandler.stopCombatTimer(playerUUID);
                scheduledTask.cancel();
                return;
            }

            Double combatTime = timerHandler.getCombatTimer(playerUUID);
            timerHandler.setCombatTimer(playerUUID, combatTime-0.1d);

            //If the timer is less then zero, stop the countdown.
            if (combatTime < 0f) {
                timerHandler.stopCombatTimer(playerUUID);
                scheduledTask.cancel();
                return;
            }

            //Send the time message to the players (will change it to the actionbar later).
            player.sendMessage(decimalFormat.format(combatTime));

        }, 0,100, TimeUnit.MILLISECONDS);
    }

}
