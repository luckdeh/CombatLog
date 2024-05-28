package me.luckdeh.combatlog.Listener;

import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        UUID playerUUID = e.getPlayer().getUniqueId();

        // Check if an NPC exists for this player and remove it
        if (EntityNPC.getNPC(playerUUID) != null) {
            EntityNPC.removeNPC(playerUUID);
        }
    }
}