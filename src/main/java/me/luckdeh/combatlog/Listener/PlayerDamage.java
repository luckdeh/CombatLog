package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.CombatLog;
import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {

    CombatLog plugin;

    public PlayerDamage(CombatLog plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player defender) || !(e.getDamager() instanceof Player attacker)) {
            return;
        }

        //Combat Time
        double combatTime = plugin.getConfig().getDouble("combat-time", 30); // Default to 30 if not set

        TimerHandler timerHandler = TimerHandler.getInstance();

        timerHandler.startCombatTimer(attacker, combatTime);
        timerHandler.startCombatTimer(defender, combatTime);
    }
}