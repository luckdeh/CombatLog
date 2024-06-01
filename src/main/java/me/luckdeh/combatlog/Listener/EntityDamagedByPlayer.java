package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.CombatLog;
import me.luckdeh.combatlog.Handler.TimerHandler;
import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamagedByPlayer implements Listener {

    private final CombatLog plugin = CombatLog.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamagedByPlayer(EntityDamageByEntityEvent e) {
        EntityNPC entityNPC = EntityNPC.getInstance();
        if ( (e.getDamager() instanceof Player player) && entityNPC.isNPCContainedInHashMap(e.getEntity()) ) {

            //Combat Time
            double combatTime = plugin.getConfig().getDouble("combat-time", 30); // Default to 30 if not set

            TimerHandler.getInstance().setCombatTimer(player.getUniqueId(), combatTime);

            e.setCancelled(false);
        }
    }

}
