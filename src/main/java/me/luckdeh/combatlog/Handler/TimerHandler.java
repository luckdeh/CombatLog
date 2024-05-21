package me.luckdeh.combatlog.Handler;

import me.luckdeh.combatlog.utils.Countdown;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TimerHandler {
    private final HashMap<UUID, Double> timer = new HashMap<>();

    public void startCombatTimer(Player player, Double taggedTime) {

        //Checks if the countdown timer already exists. If it does (meaning it is not null), then it just tags the players again.
        if (getCombatTimer(player.getUniqueId()) != null) {
            setCombatTimer(player.getUniqueId(), taggedTime);
            return;
        }

        timer.put(player.getUniqueId(), taggedTime);
        Countdown countdown = new Countdown();
        countdown.performCountdown(player.getUniqueId(), player);
    }

    public Double getCombatTimer(UUID playerUUID) {
        return timer.get(playerUUID);
    }

    public void setCombatTimer(UUID playerUUID, Double taggedTime) {
        timer.replace(playerUUID, taggedTime);
    }

    public void stopCombatTimer(UUID playerUUID) {
        //Stop the timer
        timer.remove(playerUUID);
    }

    private static TimerHandler instance;

    public static TimerHandler getInstance() {
        if (instance == null) {
            instance = new TimerHandler();
        }
        return instance;
    }

}
