/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practice.Manager;

import java.util.UUID;

import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.ConfigLoader;

public class DayManager {
    private ConfigLoader config;

    public DayManager(Main pl) {
        this.config = new ConfigLoader(pl, "day.yml");
        this.config.generateFile();
    }

    public void mark(UUID id) {
        long t = System.currentTimeMillis();
        this.config.set(id.toString(), t);
    }

    public boolean hasBeenDay(UUID id) {
        long t = System.currentTimeMillis();
        if (this.config.getConfig().contains(id.toString())) {
            long l = this.config.getConfig().getLong(id.toString());
            if (t - l >= 86400000) {
                return true;
            }
            return false;
        }
        return true;
    }
}

