package me.luckdeh.combatlog.Language;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * An enum for requesting strings from the language file.
 * @author gomeow
 */
public enum Lang {
    PREFIX("prefix", "&2[&cCombatLog&2] &8»&r"),
    COMBAT("combat", "&cYou are now in combat. Do not log out!"),
    NO_COMBAT("no-combat", "&aYou are no longer in combat."),
    ACTIONBAR("actionbar", "&c%combat_time%"),
    NPC_NAMETAG("npc-nametag", "&aCombatLogger &8| &r%player_name% &8| &c%despawn_time%");

    private final String path;
    private final String def;
    private static YamlConfiguration LANG;

    /**
     * Lang enum constructor.
     * @param path The string path.
     * @param start The default string.
     */
    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }

    /**
     * Set the {@code YamlConfiguration} to use.
     * @param config The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }

    @Override
    public String toString() {
        if (this == PREFIX)
            return LANG.getString(this.path, def) + " ";
        return LANG.getString(this.path, def);
    }

    /**
     * Get the default value of the path.
     * @return The default value of the path.
     */
    public String getDefault() {
        return this.def;
    }

    /**
     * Get the path to the string.
     * @return The path to the string.
     */
    public String getPath() {
        return this.path;
    }
}
