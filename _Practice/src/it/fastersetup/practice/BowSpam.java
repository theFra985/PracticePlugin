/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package it.fastersetup.practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import it.fastersetup.practice.Main;

public class BowSpam {
    private HashMap<UUID, Integer> shots = new HashMap<UUID, Integer>();
    private HashMap<UUID, BukkitTask> task = new HashMap<UUID, BukkitTask>();
    private HashMap<UUID, Integer> sec = new HashMap<UUID, Integer>();
    private Main plugin;

    public BowSpam(Main pl) {
        this.plugin = pl;
    }

    private int getInt(UUID id) {
        if (this.sec.containsKey(id)) {
            return this.sec.get(id);
        }
        return 0;
    }

    public void mark(UUID id) {
        if (this.task.containsKey(id)) {
            int s = 0;
            if (this.shots.containsKey(id)) {
                s = this.shots.get(id);
            }
            this.shots.put(id, ++s);
        }
    }

    public void track(final UUID id) {
        BukkitTask t = Bukkit.getScheduler().runTaskTimer((Plugin)this.plugin, new Runnable(){

            @Override
            public void run() {
                @SuppressWarnings("unused")
				int s;
                if (BowSpam.this.shots.containsKey(id) && (s = ((Integer)BowSpam.this.shots.get(id)).intValue()) > 0) {
                    int i = BowSpam.this.getInt(id);
                    BowSpam.this.sec.put(id, ++i);
                    BowSpam.this.shots.put(id, 0);
                }
            }
        }, 20, 20);
        this.task.put(id, t);
    }

    public void track(ArrayList<UUID> list) {
        for (UUID id : list) {
            this.track(id);
        }
    }

    public double stop(UUID id) {
        if (this.task.containsKey(id)) {
            this.task.get(id).cancel();
            this.task.remove(id);
            if (this.shots.containsKey(id)) {
                this.shots.remove(id);
            }
            this.sec.containsKey(id);
        }
        return 0.0;
    }

}

