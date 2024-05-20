package me.luckdeh.combatlog;

import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLog extends JavaPlugin {

    static CombatLog instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("CombatLog has been loaded.");
        instance = this;
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
