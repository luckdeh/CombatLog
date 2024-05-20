package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {

    TimerHandler timer = TimerHandler.getInstance();

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) { return; }

        Player attacker = (Player) e.getDamager();
        Player defender = (Player) e.getEntity();

        timer.startCombatTimer(attacker, 30f);
        timer.startCombatTimer(defender, 30f);

    }
}
