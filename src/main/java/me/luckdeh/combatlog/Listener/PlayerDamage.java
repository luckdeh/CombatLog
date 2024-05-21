package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player defender) || !(e.getDamager() instanceof Player attacker)) { return; }

        TimerHandler timerHandler = TimerHandler.getInstance();

        //Code to send message to the player goes here.
        //attacker.sendMessage

        //Tag both players for 30 seconds (will add configuration later).
        timerHandler.startCombatTimer(attacker, 30d);
        timerHandler.startCombatTimer(defender, 30d);

    }
}
