package me.luckdeh.combatlog.utils;

import me.luckdeh.combatlog.CombatLog;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.entity.Player;

public class CountdownHandler {

    TimerHandler timerHandler = TimerHandler.getInstance();

    public void performCountdown(CombatLog plugin, UUID playerUUID, Player player) {

        plugin.getServer().getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> {

            Float combatTime = timerHandler.getCombatTimer(playerUUID);
            timerHandler.setCombatTimer(playerUUID, combatTime-1f);

            if (combatTime <= 0f) {
                scheduledTask.cancel();
            }

            player.sendMessage(combatTime.toString());

        }, 0,1, TimeUnit.SECONDS);

    }
}
