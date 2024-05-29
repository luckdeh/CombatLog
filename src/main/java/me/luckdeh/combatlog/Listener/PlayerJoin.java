package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.Handler.TimerHandler;
import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Check if an NPC exists for this player and remove it
        EntityNPC entityNPC = EntityNPC.getInstance();
        if (entityNPC.getNPC(playerUUID) != null) {
            Location entityNPCLocation = entityNPC.getNPC(playerUUID).getLocation();
            entityNPC.removeNPC(playerUUID);
            TimerHandler.getInstance().startCombatTimer(player, 30d);
            player.teleportAsync(entityNPCLocation);
        }
    }
}