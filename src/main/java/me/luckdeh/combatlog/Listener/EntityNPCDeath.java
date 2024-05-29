package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntityNPCDeath implements Listener {

    @EventHandler
    public void onEntityNPCDeath(EntityDeathEvent e) {
        EntityNPC entityNPC = EntityNPC.getInstance();
        if (!entityNPC.isNPCContainedInHashMap(e.getEntity())) {
            return;
        }
        UUID playerUUID = entityNPC.getLoggedPlayerUUID(e.getEntity());
        Inventory inventory = entityNPC.getNPCInventory(playerUUID);
        e.getDrops().clear();
        e.getDrops().addAll(Arrays.stream(inventory.getContents()).collect(Collectors.toSet()));
        e.setDroppedExp(entityNPC.getNPCExp(playerUUID).intValue());
        entityNPC.removeNPCFromHashMap(playerUUID);
        entityNPC.removeInventoryFromHashMap(playerUUID);
        entityNPC.removeExpFromHashMap(playerUUID);
    }

}
