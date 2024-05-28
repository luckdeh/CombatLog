package me.luckdeh.combatlog.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class EntityNPC {

    private static Plugin plugin;
    private static HashMap<UUID, Villager> npcMap = new HashMap<>();

    public static void initialize(Plugin pluginInstance) {
        plugin = pluginInstance;
    }

    //Spawn villager
    public static void spawnVillagerNPC(Location location, String playerName, UUID playerUUID) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
            villager.setCustomName(playerName + "'s Combat NPC");
            villager.setCustomNameVisible(true);
            villager.setAI(true);

            npcMap.put(playerUUID, villager);
        });
    }

    //Get NPC
    public static Villager getNPC(UUID playerUUID) {
        return npcMap.get(playerUUID);
    }

    //remove NPC
    public static void removeNPC(UUID playerUUID) {
        Villager villager = npcMap.remove(playerUUID);
        if (villager != null) {
            villager.remove();
        }
    }
}
