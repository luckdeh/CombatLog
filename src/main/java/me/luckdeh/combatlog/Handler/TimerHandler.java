package me.luckdeh.combatlog.Handler;

import me.luckdeh.combatlog.CombatLog;
import me.luckdeh.combatlog.utils.Countdown;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class TimerHandler {
    private final HashMap<UUID, Double> timer = new HashMap<>();
    private final Logger log = CombatLog.getInstance().getLogger();

    public void startCombatTimer(Player player, Double taggedTime) {

        if (player == null) {
            return;
        }

        UUID playerUUID = player.getUniqueId();

        //Checks if the countdown timer already exists. If it does (meaning it is not null), then it just tags the player again.
        if (isPlayerTagged(playerUUID)) {
            setCombatTimer(playerUUID, taggedTime);
            return;
        }

        timer.put(playerUUID, taggedTime);
        Countdown countdown = new Countdown();
        countdown.performCountdown(playerUUID, player);
    }

    public Double getCombatTimer(UUID playerUUID) {
        return timer.get(playerUUID);
    }

    //Combat timer must be started for it to be set.
    public void setCombatTimer(UUID playerUUID, Double taggedTime) {
        timer.replace(playerUUID, taggedTime);
    }

    public void stopCombatTimer(UUID playerUUID) {
        timer.remove(playerUUID);
    }

    public boolean isPlayerTagged(UUID playerUUID) {
        return timer.containsKey(playerUUID);
    }

    //Should only be called in the event of a server shutdown!
    public void clearAllCombatTags() {
        log.info("[CombatLog] Removing all combat tags...");
        if (timer.isEmpty()) {
            log.info("[CombatLog] No combat tags found. Skipping...");
            return;
        }
        for (UUID playerUUID : timer.keySet()) {
            stopCombatTimer(playerUUID);
        }
        timer.clear();
        log.info("[CombatLog] Combat tags removed successfully!");
    }

    private static TimerHandler instance;

    public static TimerHandler getInstance() {
        if (instance == null) {
            instance = new TimerHandler();
        }
        return instance;
    }

}
