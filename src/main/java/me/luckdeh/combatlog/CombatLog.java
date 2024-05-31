package me.luckdeh.combatlog;

import me.luckdeh.combatlog.Handler.TimerHandler;
import me.luckdeh.combatlog.Language.Lang;
import me.luckdeh.combatlog.Listener.*;
import me.luckdeh.combatlog.utils.EntityNPC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CombatLog extends JavaPlugin {

    static CombatLog instance;
    private Logger log;

    public static YamlConfiguration LANG;
    public static File LANG_FILE;

    @Override
    public void onEnable() {
        // Plugin startup logic
        log = getLogger();
        instance = this;
        saveDefaultConfig();
        loadLang();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            log.warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        log.info("Registering events...");
        getServer().getPluginManager().registerEvents(new PlayerDamage(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerDisconnect(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new EntityDamagedByPlayer(), this);
        getServer().getPluginManager().registerEvents(new EntityTransform(), this);
        getServer().getPluginManager().registerEvents(new EntityNPCDeath(), this);
        getServer().getPluginManager().registerEvents(new EntityUnload(), this);
        log.info("Plugin enabled.");
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

    /**
     * Load the lang.yml file.
     * @return The lang.yml config.
     */
    public void loadLang() {
        File lang = new File(getDataFolder(), "lang.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = this.getResource("lang.yml");
                if (defConfigStream != null) {
                    copyInputStreamToFile(defConfigStream, lang);
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(lang);
                    defConfig.save(lang);
                    Lang.setFile(defConfig);
                }
            } catch(IOException e) {
                e.printStackTrace(); // So they notice
                log.severe("Failed to create lang.yml for CombatLog.");
                log.severe("Now disabling...");
                this.setEnabled(false); // Without it loaded, we can't send them messages
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for(Lang item:Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        LANG = conf;
        LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch(IOException e) {
            log.log(Level.WARNING, "Failed to save lang.yml for CombatLog");
            log.log(Level.WARNING, "Now disabling...");
            e.printStackTrace();
        }
    }

    /**
     * Gets the lang.yml config.
     * @return The lang.yml config.
     */
    public YamlConfiguration getLang() {
        return LANG;
    }

    /**
     * Get the lang.yml file.
     * @return The lang.yml file.
     */
    public File getLangFile() {
        return LANG_FILE;
    }

    public static void copyInputStreamToFile(InputStream input, File file) {

        try (OutputStream output = new FileOutputStream(file)) {
            input.transferTo(output);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

}
