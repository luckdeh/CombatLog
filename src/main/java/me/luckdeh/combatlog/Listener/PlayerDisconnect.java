package me.luckdeh.combatlog.Listener;


import me.luckdeh.combatlog.CombatLog;
import me.luckdeh.combatlog.Handler.TimerHandler;
import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerDisconnect implements Listener {


    private CombatLog plugin = CombatLog.getInstance();

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {

        //If the server is stopping, then do not run this event.
        if (plugin.getServer().isStopping()) {
            return;
        }

        // If the player is kicked by another plugin then do not run this event.
        if (e.getReason().equals(PlayerQuitEvent.QuitReason.KICKED) && !plugin.getConfig().getBoolean("combat-log-kick", false)) {
            return;
        }

        TimerHandler timerHandler = TimerHandler.getInstance();

        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Check if the player is tagged for combat
        if (timerHandler.isPlayerTagged(playerUUID)) {
            timerHandler.stopCombatTimer(playerUUID);

            //Spawn NPC?
            boolean spawnNPC = plugin.getConfig().getBoolean("spawn-npc", true); // Default to true if not set.
            if (!spawnNPC) {
                player.setHealth(0);
                return;
            }
            EntityNPC entityNPC = EntityNPC.getInstance();
            entityNPC.spawn(player);
        }
    }
}