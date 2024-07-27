package me.luckdeh.combatlog.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.luckdeh.combatlog.CombatLog;
import me.luckdeh.combatlog.Language.Lang;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class EntityNPC {

    private final CombatLog plugin = CombatLog.getInstance();
    private final ConcurrentHashMap<UUID, Entity> npcHashMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Player> offlinePlayerHashMap = new ConcurrentHashMap<>();
    private final Logger log = CombatLog.getInstance().getLogger();

    DecimalFormat decimalFormat = new DecimalFormat("0.0");

    // Spawn NPC
    public void spawn(Player player) {

        Location location = player.getLocation();
        UUID playerUUID = player.getUniqueId();

        EntityType entityType = entityType();
        Entity entity = location.getWorld().spawnEntity(location, entityType, CreatureSpawnEvent.SpawnReason.CUSTOM);

        // Set custom name for the entity
        Component customNameComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(Lang.NPC_NAMETAG.toString());
        entity.customName(customNameComponent);
        entity.setCustomNameVisible(true);

        // Apply common properties
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.getEquipment().setArmorContents(player.getEquipment().getArmorContents());
            livingEntity.getEquipment().setItemInMainHand(player.getEquipment().getItemInMainHand());
            livingEntity.getEquipment().setItemInOffHand(player.getEquipment().getItemInOffHand());
            livingEntity.getEquipment().setDropChance(EquipmentSlot.HEAD, 0);
            livingEntity.getEquipment().setDropChance(EquipmentSlot.CHEST, 0);
            livingEntity.getEquipment().setDropChance(EquipmentSlot.LEGS, 0);
            livingEntity.getEquipment().setDropChance(EquipmentSlot.FEET, 0);
            livingEntity.getEquipment().setDropChance(EquipmentSlot.HAND, 0);
            livingEntity.getEquipment().setDropChance(EquipmentSlot.OFF_HAND, 0);
            livingEntity.addPotionEffects(player.getActivePotionEffects());
            livingEntity.setCanPickupItems(false);

            boolean entityAI = this.plugin.getConfig().getBoolean("npc-ai");
            livingEntity.setAI(entityAI);
        }

        //Specific Cases.
        if (entity instanceof Villager villager) {
            villager.setProfession(Villager.Profession.NITWIT);
            villager.setBreed(false);
        } else if (entity instanceof Skeleton skeleton) {
            skeleton.setShouldBurnInDay(false);
        } else if (entity instanceof Zombie zombie) {
            zombie.setShouldBurnInDay(false);
        }
        // Add more specific cases if needed.

        npcHashMap.put(playerUUID, entity);
        offlinePlayerHashMap.put(playerUUID, player);

        // Remove NPC and player data after combat time.
        // Implement this logic as needed.
        // Could use a repeating task.
        Double combatTime = plugin.getConfig().getDouble("combat-time", 30d);
        removeNPCAfter(combatTime, entity, player);
    }

    private void removeNPCAfter(Double combatTime, Entity npc, Player player) {

        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        AtomicReference<Double> time = new AtomicReference<>(combatTime);
        String nametag = PlaceholderAPI.setPlaceholders(player, Lang.NPC_NAMETAG.toString());

        plugin.getServer().getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> {

            if (!isNPCContainedInHashMap(npc)) {
                scheduledTask.cancel();
                return;
            }

            time.set(time.get() - 0.1d);
            if (time.get() < 0) {
                plugin.getServer().getRegionScheduler().run(plugin, npc.getLocation(), scheduledSyncTask -> removeNPC(player.getUniqueId()));
                scheduledTask.cancel();
                return;
            }

            // Update custom name for the entity
            String nametagUpdated = nametag.replace("%despawn_time%", decimalFormat.format(time.get()));
            Component customNameComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(nametagUpdated);
            npc.customName(customNameComponent);

        }, 0, 100, TimeUnit.MILLISECONDS);

    }

    // Get NPC
    public Entity getNPC(UUID playerUUID) {
        return npcHashMap.get(playerUUID);
    }

    // Remove NPC
    public void removeNPC(UUID playerUUID) {
        Entity entity = getNPC(playerUUID);
        if (entity != null) {
            entity.remove();
        }
        npcHashMap.remove(playerUUID);
        offlinePlayerHashMap.remove(playerUUID);
    }

    public void removeDataFromHashMaps(Entity entity) {
        for (UUID playerUUID : npcHashMap.keySet()) {
            if (entity == getNPC(playerUUID)) {
                removeNPCFromHashMap(playerUUID);
                removeOfflinePlayerFromHashMap(playerUUID);
            }
        }
    }
    public void removeNPCFromHashMap(UUID playerUUID) {
        npcHashMap.remove(playerUUID);
    }

    public void removeOfflinePlayerFromHashMap(UUID playerUUID) {
        offlinePlayerHashMap.remove(playerUUID);
    }

    // Removes all NPCs in the world. Should only be called when server stops!
    public void removeAllNPCData() {
        // Clear NPC HashMap
        log.info("Removing all NPCs...");
        if (npcHashMap.isEmpty()) {
            log.info("No NPCs found. Skipping...");
        } else {
            for (Entity entity : npcHashMap.values()) {
                entity.remove();
            }
            npcHashMap.clear();
            log.info("All NPCs removed successfully!");
        }

        // Clear NPCInventory HashMap
        log.info("Removing all players...");
        if (offlinePlayerHashMap.isEmpty()) {
            log.info("No players found. Skipping...");
            return;
        }
        offlinePlayerHashMap.clear();
        log.info("All players removed successfully!");
    }

    public EntityType entityType() {
        String npcTypeName = plugin.getConfig().getString("npc-type", "VILLAGER").toUpperCase();
        try {
            EntityType entityType = EntityType.valueOf(npcTypeName);
            return entityType;
        } catch (IllegalArgumentException e) {
            log.warning("Invalid NPC type specified in config: " + npcTypeName + ". Defaulting to VILLAGER.");
            return EntityType.VILLAGER;
        }
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
