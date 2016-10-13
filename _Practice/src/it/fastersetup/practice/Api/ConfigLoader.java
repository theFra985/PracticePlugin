/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practice.Api;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigLoader {
    private JavaPlugin plugin;
    private String name;
    private FileConfiguration config;
    private File configFile;

    public ConfigLoader(JavaPlugin plugin, String filename) {
        this.plugin = plugin;
        this.name = filename;
    }

    public FileConfiguration getConfig() {
        if (this.config == null) {
            this.reloadConfig();
        }
        return this.config;
    }

    public void generateFile() {
        this.saveConfig(this.getConfig());
    }

    public void saveConfig(FileConfiguration cfg) {
        this.config = cfg;
        try {
            this.config.save(this.configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), this.name);
        }
        this.config = YamlConfiguration.loadConfiguration((File)this.configFile);
    }

    public void clearConfig() {
        FileConfiguration data = this.getConfig();
        for (String s : data.getConfigurationSection("").getKeys(false)) {
            data.set(s, (Object)null);
        }
        this.saveConfig(data);
    }

    public void setDefault(String path, Object value) {
        FileConfiguration data = this.getConfig();
        if (!data.contains(path)) {
            data.set(path, value);
        }
        this.saveConfig(data);
    }

    public void removeSection(String path) {
        FileConfiguration data = this.getConfig();
        if (data.contains(path)) {
            data.set(path, (Object)null);
        }
        this.saveConfig(data);
    }

    public void set(String path, Object value) {
        FileConfiguration data = this.getConfig();
        data.set(path, value);
        this.saveConfig(data);
    }
}

