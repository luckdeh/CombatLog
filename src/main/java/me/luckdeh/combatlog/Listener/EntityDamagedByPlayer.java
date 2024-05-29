package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.Handler.TimerHandler;
import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamagedByPlayer implements Listener {

    @EventHandler
    public void onEntityDamagedByPlayer(EntityDamageByEntityEvent e) {
        EntityNPC entityNPC = EntityNPC.getInstance();
        if ( (e.getDamager() instanceof Player player) && entityNPC.isNPCContainedInHashMap(e.getEntity()) ) {
            TimerHandler.getInstance().setCombatTimer(player.getUniqueId(), 30d);
        }
    }

}
