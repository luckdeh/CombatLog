package me.luckdeh.combatlog.Handler;

import me.luckdeh.combatlog.CombatLog;
import me.luckdeh.combatlog.utils.CountdownHandler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TimerHandler {

    static TimerHandler instance = new TimerHandler();
    private final HashMap<UUID, Float> timer = new HashMap<>();
    private final CountdownHandler countdownHandler = new CountdownHandler();

    public void startCombatTimer(Player player, Float taggedTime) {
        timer.put(player.getUniqueId(), taggedTime);
        //Start the timer
        countdownHandler.performCountdown(player.getUniqueId(), player);
    }

    public Float getCombatTimer(UUID playerUUID) {
        return timer.get(playerUUID);
    }

    public void setCombatTimer(UUID playerUUID, Float taggedTime) {
        timer.replace(playerUUID, taggedTime);
    }

    public void stopCombatTimer(UUID playerUUID) {
        //Stop the timer
        timer.remove(playerUUID);
    }

    public static TimerHandler getInstance() {
        return instance;
    }

}
