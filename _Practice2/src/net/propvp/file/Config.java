/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package net.propvp.file;

import java.io.File;
import java.io.IOException;
import net.propvp.Practice;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    public String fileName;
    public File configFile;
    private FileConfiguration config;

    public Config(String string) {
        this.fileName = string;
        File file = Practice.getInstance().getDataFolder();
        this.configFile = new File(file, string);
        if (!this.configFile.exists()) {
            this.configFile.getParentFile().mkdirs();
            Practice.getInstance().saveResource(string, false);
        }
        this.config = YamlConfiguration.loadConfiguration((File)this.configFile);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            this.getConfig().save(this.configFile);
        }
        catch (IOException var1_1) {
            Bukkit.getLogger().severe("Could not save config file " + this.configFile.toString());
            var1_1.printStackTrace();
        }
    }
}

