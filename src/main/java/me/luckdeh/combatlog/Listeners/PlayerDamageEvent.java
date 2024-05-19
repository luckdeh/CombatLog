package me.luckdeh.combatlog.Listeners;

import me.luckdeh.combatlog.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDamageEvent implements Listener {
    private final CombatLog plugin;

    public PlayerDamageEvent(CombatLog plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e){
        if (e.getEntity() instanceof Player){
            Player damage = (Player) e.getEntity();
            if (e.getDamager() instanceof Player){
                Player damager = (Player) e.getDamager();


                startTimer(damager);
                startTimer(damage);
            }
        }

    }
    private void startTimer(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                player.sendMessage("You are in combat");
            }
        }.runTaskLater(plugin,10);
    }
}
