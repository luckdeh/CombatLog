package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.utils.EntityNPC;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
        Player offlinePlayer = entityNPC.getOfflinePlayer(playerUUID);

        Inventory inventory = offlinePlayer.getInventory();
        int offlinePlayerTotalExperience = offlinePlayer.getTotalExperience();
        e.getDrops().clear();
        e.getDrops().addAll(Arrays.stream(inventory.getContents()).collect(Collectors.toSet()));
        e.setDroppedExp(0);
        //Load the player data and change it.
        offlinePlayer.loadData();
        offlinePlayer.getInventory().setContents(inventory.getContents());
        offlinePlayer.setTotalExperience(offlinePlayerTotalExperience);
        offlinePlayer.incrementStatistic(Statistic.DEATHS);
        //Save the player's data to the file (allows for plugins such as Inventory Rollback Plus to save the inventory),
        offlinePlayer.saveData();

        //Call a player death event.
        Component deathMessage = Component.text().content(offlinePlayer.getName() + "died.").build();
        PlayerDeathEvent event = new PlayerDeathEvent(offlinePlayer, Arrays.asList(inventory.getContents()), 0, 0, 0, 0, deathMessage, true);
        Bukkit.getServer().getPluginManager().callEvent(event);
        //Kill the player, set exp to zero, and clear inventory.
        offlinePlayer.getInventory().clear();
        offlinePlayer.setTotalExperience(0);
        offlinePlayer.setLevel(0);
        offlinePlayer.setHealth(0);
        offlinePlayer.saveData();

        //Remove everything from HashMaps.
        entityNPC.removeOfflinePlayerFromHashMap(playerUUID);
        entityNPC.removeNPCFromHashMap(playerUUID);
    }

}
