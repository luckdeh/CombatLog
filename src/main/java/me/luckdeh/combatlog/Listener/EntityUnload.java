package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesUnloadEvent;

public class EntityUnload implements Listener {

    @EventHandler
    public void onEntityUnload(EntitiesUnloadEvent e) {
        EntityNPC entityNPC = EntityNPC.getInstance();
        for (Entity entity : e.getEntities()) {
            if (entityNPC.isNPCContainedInHashMap(entity)) {
                entityNPC.removeDataFromHashMaps(entity);
                entity.remove();
            }
        }
    }

}
