/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.ConfigLoader;

public class Coins {
    private HashMap<UUID, Integer> map;
    private ConfigLoader config;

    public Coins(Main pl) {
        this.config = new ConfigLoader(pl, "coins.yml");
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
        @SuppressWarnings("deprecation")
		Player[] t = Bukkit.getOnlinePlayers();
        int uuid = t.length;
        int n = 0;
        while (n < uuid) {
            Player p = t[n];
            this.check(p.getUniqueId());
            ++n;
        }
    }

    public void save() {
        FileConfiguration data = this.config.getConfig();
        for (Map.Entry<UUID, Integer> entry : this.map.entrySet()) {
            data.set(entry.getKey().toString(), (Object)entry.getValue());
        }
        this.config.saveConfig(data);
    }

    public void check(UUID id) {
        if (!this.map.containsKey(id)) {
            this.map.put(id, 50);
        }
    }

    public int get(UUID id) {
        if (this.map.containsKey(id)) {
            return this.map.get(id);
        }
        return 50;
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

