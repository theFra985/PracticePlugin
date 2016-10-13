/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.ConfigLoader;

public class Matches {
    private HashMap<UUID, Integer> map;
    private ConfigLoader config;

    public Matches(Main pl) {
        this.config = new ConfigLoader(pl, "matches.yml");
        this.config.generateFile();
        this.map = new HashMap<UUID, Integer>();
    }

    public void load() {
        FileConfiguration data = this.config.getConfig();
        Set<String> parts = data.getConfigurationSection("").getKeys(false);
        for (String s : parts) {
            UUID uuid = UUID.fromString(s);
            int t = data.getInt(s);
            this.map.put(uuid, t);
        }
    }

    public void save() {
        FileConfiguration data = this.config.getConfig();
        for (Map.Entry<UUID, Integer> entry : this.map.entrySet()) {
            data.set(entry.getKey().toString(), (Object)entry.getValue());
        }
        this.config.saveConfig(data);
    }

    public int get(UUID id) {
        if (this.map.containsKey(id)) {
            return this.map.get(id);
        }
        return 10;
    }

    public void set(UUID id, int v) {
        this.map.put(id, v);
    }

    public void add(UUID id, int v) {
        int t = this.get(id);
        this.map.put(id, t + v);
    }

    public void remove(UUID id, int v) {
        int t = this.get(id);
        this.map.put(id, t - v);
    }
}

