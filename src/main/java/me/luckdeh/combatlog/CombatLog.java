package me.luckdeh.combatlog;

import me.luckdeh.combatlog.Listener.PlayerDamage;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLog extends JavaPlugin {

    static CombatLog instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("CombatLog has been loaded.");
        instance = this;
        getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("CombatLog has been stopped.");
    }

    public static CombatLog getInstance() {
        return instance;
    }
}
