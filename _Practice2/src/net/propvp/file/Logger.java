/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.propvp.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import net.propvp.Practice;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Logger {
    private File storageFile;
    private ArrayList<String> values;

    public Logger(File file) {
        this.storageFile = file;
        this.values = new ArrayList();
        if (!this.storageFile.exists()) {
            try {
                this.storageFile.createNewFile();
            }
            catch (IOException var2_2) {
                var2_2.printStackTrace();
            }
        }
        this.log("Loaded logger for the first time on this instance...", true);
        new BukkitRunnable(){

            public void run() {
                Logger.this.save();
            }
        }.runTaskTimerAsynchronously((Plugin)Practice.getInstance(), 0, 100);
    }

    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(this.storageFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String string : this.values) {
                bufferedWriter.write(string);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            fileWriter.close();
            this.values.clear();
        }
        catch (IOException var1_2) {
            var1_2.printStackTrace();
        }
    }

    public ArrayList<String> checkList() {
        return this.values;
    }

    public void log(String string, boolean bl) {
        String string2 = new SimpleDateFormat().format(new Date());
        if (!this.values.contains("[" + string2 + "] " + string)) {
            this.values.add("[" + string2 + "] " + string);
        }
        if (bl) {
            Bukkit.getLogger().info(string);
        }
    }

    public void remove(String string) {
        this.values.remove(string);
        this.save();
    }

    public boolean contains(String string) {
        if (this.values.contains(string)) {
            return true;
        }
        return false;
    }

}

