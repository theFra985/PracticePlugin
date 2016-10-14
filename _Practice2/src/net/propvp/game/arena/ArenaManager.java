/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 */
package net.propvp.game.arena;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.propvp.Practice;
import net.propvp.file.Config;
import net.propvp.game.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenaManager {
    private Map<String, Arena> arenas = new HashMap<String, Arena>();

    public ArenaManager() {
        this.loadArenas();
    }

    public Map<String, Arena> getArenas() {
        return this.arenas;
    }

    public Arena getArena(String string) {
        return this.arenas.get(string);
    }

    public void removeArena(String string) {
        this.arenas.remove(string);
    }

    public void putArena(String string, Arena arena) {
        this.arenas.put(string, arena);
    }

    public void saveAll() {
        for (Arena arena : this.arenas.values()) {
            arena.save();
        }
    }

    public void loadArenas() {
        Config config = Practice.getBackend().getArenasConfig();
        if (config.getConfig().getConfigurationSection("arena") == null) {
            Bukkit.getLogger().severe("There are no arenas in the configuration.");
            return;
        }
        for (String string : config.getConfig().getConfigurationSection("arena").getKeys(false)) {
            try {
                String string2 = config.getConfig().getString("arena." + string + ".region1");
                String string3 = config.getConfig().getString("arena." + string + ".region2");
                String string4 = config.getConfig().getString("arena." + string + ".spawn1");
                String string5 = config.getConfig().getString("arena." + string + ".spawn2");
                Arena arena = new Arena(string, string2, string3, string4, string5);
                this.arenas.put(string, arena);
                continue;
            }
            catch (NullPointerException var4_5) {
                Practice.getLog().log("Failed to load arena '" + string + "' because a value was null.", true);
            }
        }
    }
}

