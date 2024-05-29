package me.luckdeh.combatlog;

import me.luckdeh.combatlog.Handler.TimerHandler;
import me.luckdeh.combatlog.Listener.*;
import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CombatLog extends JavaPlugin {

    static CombatLog instance;
    private Logger log;

    @Override
    public void onEnable() {
        // Plugin startup logic
        log = getLogger();
        instance = this;
        log.info("[CombatLog] Registering events...");
        getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new EntityDamagedByPlayer(), this);
        getServer().getPluginManager().registerEvents(new EntityTransform(), this);
        getServer().getPluginManager().registerEvents(new EntityNPCDeath(), this);

        log.info("[CombatLog] Plugin enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        //Remove all combat tags.
        TimerHandler.getInstance().clearAllCombatTags();
        EntityNPC.getInstance().removeAllNPCData();
        log.info("[CombatLog] Plugin disabled.");
    }

    public static CombatLog getInstance() {
        return instance;
    }
}
