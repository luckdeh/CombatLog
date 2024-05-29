package me.luckdeh.combatlog.utils;

import me.luckdeh.combatlog.CombatLog;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class EntityNPC {
    private final HashMap<UUID, Entity> npcHashMap = new HashMap<>();
    private final HashMap<UUID, Player> offlinePlayerHashMap = new HashMap<>();
    private final Logger log = CombatLog.getInstance().getLogger();

    //Spawn NPC
    public void spawn(Player player) {

        Location location = player.getLocation();
        UUID playerUUID = player.getUniqueId();
        String playerName = player.getName();

        Entity entity = location.getWorld().spawnEntity(location, entityType());

        Component customNameComponent = Component.text()
                .content("CombatLogger | ")
                .append(Component.text(playerName))
                .build();
        entity.customName(customNameComponent);
        entity.setCustomNameVisible(true);

        LivingEntity livingEntity = (LivingEntity) entity;
        livingEntity.getEquipment().setArmorContents(player.getEquipment().getArmorContents());
        livingEntity.getEquipment().setDropChance(EquipmentSlot.HEAD, 0);
        livingEntity.getEquipment().setDropChance(EquipmentSlot.BODY, 0);
        livingEntity.getEquipment().setDropChance(EquipmentSlot.LEGS, 0);
        livingEntity.getEquipment().setDropChance(EquipmentSlot.FEET, 0);
        livingEntity.getEquipment().setDropChance(EquipmentSlot.HAND, 0);
        livingEntity.getEquipment().setDropChance(EquipmentSlot.OFF_HAND, 0);

        //Will be made configurable later.
        //livingEntity.setAI(false);

        if (entity.getType().equals(EntityType.VILLAGER)) {
            Villager villager = (Villager) entity;
            villager.setProfession(Villager.Profession.NITWIT);
            villager.setBreed(false);
        }

        npcHashMap.put(playerUUID, entity);
        offlinePlayerHashMap.put(playerUUID, player);

        //Remove NPC and player data after combat time.

    }

    //Get NPC
    public Entity getNPC(UUID playerUUID) {
        return npcHashMap.get(playerUUID);
    }

    //remove NPC
    public void removeNPC(UUID playerUUID) {
        Entity entity = getNPC(playerUUID);
        if (entity != null) {
            entity.remove();
        }
        npcHashMap.remove(playerUUID);
        offlinePlayerHashMap.remove(playerUUID);
    }

    public void removeNPCFromHashMap(UUID playerUUID) {
        npcHashMap.remove(playerUUID);
    }
    public void removeOfflinePlayerFromHashMap(UUID playerUUID) {
        offlinePlayerHashMap.remove(playerUUID);
    }

    //Removes all NPCs in the world. Should only be called when server stops!
    public void removeAllNPCData() {
        //Clear NPC HashMap
        log.info("[CombatLog] Removing all NPCs...");
        if (npcHashMap.isEmpty()) {
            log.info("[CombatLog] No NPCs found. Skipping...");
        } else {
            for (Entity entity : npcHashMap.values()) {
                entity.remove();
            }
            npcHashMap.clear();
            log.info("[CombatLog] All NPCs removed successfully!");
        }

        //Clear NPCInventory HashMap
        log.info("[CombatLog] Removing all players...");
        if (offlinePlayerHashMap.isEmpty()) {
            log.info("[CombatLog] No players found. Skipping...");
            return;
        }
        offlinePlayerHashMap.clear();
        log.info("[CombatLog] All players removed successfully!");
    }

    public EntityType entityType() {
        return EntityType.fromName("VILLAGER");
    }

    public boolean isNPCContainedInHashMap(Entity entity) {
        return npcHashMap.containsValue(entity);
    }

    public UUID getLoggedPlayerUUID(Entity entity) {
        for (UUID loggedPlayerUUID : npcHashMap.keySet()) {
            if (getNPC(loggedPlayerUUID).equals(entity)) {
                return loggedPlayerUUID;
            }
        }
        return null;
    }

    public Player getOfflinePlayer(UUID playerUUID) {
        return offlinePlayerHashMap.get(playerUUID);
    }

    private static EntityNPC instance;

    public static EntityNPC getInstance() {
        if (instance == null) {
            instance = new EntityNPC();
        }
        return instance;
    }

}
