package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.CombatLog;
import me.luckdeh.combatlog.Handler.TimerHandler;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {

    CombatLog plugin;
    public PlayerDamage(CombatLog plugin){
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player defender)) {
            return;
        }

        Player attacker;

        if (e.getDamager() instanceof Player) {
            attacker = (Player) e.getDamager();
        } else if (e.getDamager() instanceof Projectile projectile) {
            if(projectile.getShooter() instanceof Player){
                if (projectile.getType().equals(EntityType.SNOWBALL) || projectile.getType().equals(EntityType.EGG)) return;
                attacker = (Player) projectile.getShooter();
            } else {
                return;
            }
        } else {
            return;
        }

        if (attacker.hasPermission("combatlog.bypass") || defender.hasPermission("combatlog.bypass") || attacker.equals(defender)) return;

        if (!attacker.hasPermission("combatlog.tagcreative") && attacker.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        //Combat Time
        double combatTime = plugin.getConfig().getDouble("combat-time", 30); // Default to 30 if not set

        TimerHandler timerHandler = TimerHandler.getInstance();

        timerHandler.startCombatTimer(attacker, combatTime);
        timerHandler.startCombatTimer(defender, combatTime);
    }
}