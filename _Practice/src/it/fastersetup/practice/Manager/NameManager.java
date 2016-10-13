/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practice.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.ConfigLoader;

public class NameManager {
    private ConfigLoader config;

    public NameManager(Main pl) {
        this.config = new ConfigLoader(pl, "names.yml");
        this.config.generateFile();
    }

    private List<String> getNames(UUID id) {
        if (this.config.getConfig().contains(id.toString())) {
            return this.config.getConfig().getStringList(id.toString());
        }
        return new ArrayList<String>();
    }

    public UUID getId(String name) {
        if (this.config.getConfig().contains("names")) {
            for (String id : this.config.getConfig().getConfigurationSection("names").getKeys(false)) {
                for (String n : this.config.getConfig().getStringList("names." + id)) {
                    if (!n.equalsIgnoreCase(name)) continue;
                    return UUID.fromString(id);
                }
            }
        }
        return null;
    }

    public String properName(String name) {
        if (this.config.getConfig().contains("names")) {
            for (String id : this.config.getConfig().getConfigurationSection("names").getKeys(false)) {
                for (String n : this.config.getConfig().getStringList("names." + id)) {
                    if (!n.equalsIgnoreCase(name)) continue;
                    return n;
                }
            }
        }
        return name;
    }

    public String currentName(UUID id) {
        if (this.config.getConfig().contains("current." + id.toString())) {
            return this.config.getConfig().getString("current." + id.toString());
        }
        return null;
    }

    public void save(Player player) {
        List<String> names = this.getNames(player.getUniqueId());
        boolean c = false;
        if (!names.contains(player.getName())) {
            c = true;
            names.add(player.getName());
        }
        if (this.config.getConfig().contains("current." + player.getUniqueId().toString())) {
            if (!this.config.getConfig().getString("current." + player.getUniqueId().toString()).equals(player.getName())) {
                this.config.set("current." + player.getUniqueId().toString(), player.getName());
            }
        } else {
            this.config.set("current." + player.getUniqueId().toString(), player.getName());
        }
        if (c) {
            if (this.config.getConfig().contains("names")) {
                for (String id : this.config.getConfig().getConfigurationSection("names").getKeys(false)) {
                    if (id.equals(player.getUniqueId().toString()) || !this.getNames(UUID.fromString(id)).contains(player.getName())) continue;
                    List<String> temp = this.getNames(UUID.fromString(id));
                    temp.remove(player.getName());
                    this.config.set("names." + id, temp);
                }
            }
            this.config.set("names." + player.getUniqueId().toString(), names);
        }
    }
}

