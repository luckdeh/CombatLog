package me.luckdeh.combatlog;

import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLog extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("CombatLog has been loaded.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("CombatLog has been stopped.");
    }
}
