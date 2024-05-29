package me.luckdeh.combatlog.utils;

import me.luckdeh.combatlog.CombatLog;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class EntityNPC {
    private final HashMap<UUID, Entity> npcHashMap = new HashMap<>();
    private final HashMap<UUID, Inventory> npcInventoryHashMap = new HashMap<>();
    private final HashMap<UUID, Float> npcExpHashMap = new HashMap<>();
    private final Logger log = CombatLog.getInstance().getLogger();

    //Spawn NPC
    public void spawn(Player player) {

        Location location = player.getLocation();
        UUID playerUUID = player.getUniqueId();
        String playerName = player.getName();
        Inventory playerInventory = player.getInventory();
        Float playerExp = player.getExp();

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
        npcInventoryHashMap.put(playerUUID, playerInventory);
        npcExpHashMap.put(playerUUID, playerExp);
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
        npcInventoryHashMap.remove(playerUUID);
        npcExpHashMap.remove(playerUUID);
    }

    public void removeNPCFromHashMap(UUID playerUUID) {
        npcHashMap.remove(playerUUID);
    }
    public void removeInventoryFromHashMap(UUID playerUUID) {
        npcInventoryHashMap.remove(playerUUID);
    }
    public void removeExpFromHashMap(UUID playerUUID) {
        npcExpHashMap.remove(playerUUID);
    }

    //Removes all NPCs in the world. Should only be called when server stops!
    public void removeAllNPCData() {
        //Clear NPC HashMap
        log.info("[CombatLog] Removing all NPCs...");
        if (npcHashMap.isEmpty()) {
            log.info("[CombatLog] No NPCs found. Skipping...");
            return;
        }
        for (Entity entity : npcHashMap.values()) {
            entity.remove();
        }
        npcHashMap.clear();
        log.info("[CombatLog] All NPCs removed successfully!");

        //Clear NPCInventory HashMap
        log.info("[CombatLog] Removing all inventories...");
        if (npcInventoryHashMap.isEmpty()) {
            log.info("[CombatLog] No inventories found. Skipping...");
            return;
        }
        npcInventoryHashMap.clear();
        log.info("[CombatLog] All inventories removed successfully!");

        //Clear ExphashMap
        log.info("[CombatLog] Removing all saved exp...");
        if (npcExpHashMap.isEmpty()) {
            log.info("[CombatLog] No saved exp found. Skipping...");
            return;
        }
        npcExpHashMap.clear();
        log.info("[CombatLog] All saved exp removed successfully!");
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

    public Inventory getNPCInventory(UUID playerUUID) {
        return npcInventoryHashMap.get(playerUUID);
    }
    public Float getNPCExp(UUID playerUUIO) {
        return npcExpHashMap.get(playerUUIO);
    }

    private static EntityNPC instance;

    public static EntityNPC getInstance() {
        if (instance == null) {
            instance = new EntityNPC();
        }
        return instance;
    }

}
