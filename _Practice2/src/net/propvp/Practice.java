/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.propvp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import net.propvp.backend.Backend;
import net.propvp.file.Logger;
import net.propvp.world.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Practice
extends JavaPlugin {
    private static String prefix;
    private static Practice instance;
    private static Backend backend;
    private static Logger logger;
    private static WorldManager wm;

    public void onEnable() {
        Practice.loadConfig0();
        instance = this;
        prefix = (Object)ChatColor.RED + "Practice" + (Object)ChatColor.DARK_GRAY + " \u00bb ";
        logger = new Logger(new File(this.getDataFolder() + File.separator + "log.txt"));
        wm = new WorldManager();
        backend = new Backend();
        backend.initialize();
    }

    public void onDisable() {
        wm.clearClonedWorlds();
    }

    public static Practice getInstance() {
        return instance;
    }

    public static Backend getBackend() {
        return backend;
    }

    public static Logger getLog() {
        return logger;
    }

    public static WorldManager getWorldManager() {
        return wm;
    }

    public static String getPrefix() {
        return prefix;
    }
}

