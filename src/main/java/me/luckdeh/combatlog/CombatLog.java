package me.luckdeh.combatlog;

import me.luckdeh.combatlog.Handler.TimerHandler;
import me.luckdeh.combatlog.Listener.PlayerDamage;
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
        getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
        log.info("[CombatLog] Plugin enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        //Remove all combat tags.
        log.info("[CombatLog] Removing all combat tags...");
        TimerHandler.getInstance().clearAllCombatTags();
        log.info("[CombatLog] All combat tags successfully removed.");
        log.info("[CombatLog] Plugin disabled.");
    }

    public static CombatLog getInstance() {
        return instance;
    }
}
