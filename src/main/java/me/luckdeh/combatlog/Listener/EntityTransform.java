package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;

public class EntityTransform implements Listener {

    @EventHandler
    public void onEntityTransform(EntityTransformEvent e) {
        EntityNPC entityNPC = EntityNPC.getInstance();
        if (!entityNPC.isNPCContainedInHashMap(e.getEntity())) {
            return;
        }
        e.setCancelled(true);
    }

}
