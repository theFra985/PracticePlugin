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
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import it.fastersetup.practice.Main;

public class Cooldown {
    private ArrayList<UUID> list = new ArrayList<UUID>();
    private Main plugin;

    public Cooldown(Main pl) {
        this.plugin = pl;
    }

    public boolean inCooldown(UUID id) {
        return this.list.contains(id);
    }

    public void addCooldown(final UUID id, int tick, final String message) {
        if (this.plugin.over.contains(id)) {
            return;
        }
        if (!this.list.contains(id)) {
            this.list.add(id);
            Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, new Runnable(){

                @Override
                public void run() {
                    if (Cooldown.this.list.contains(id)) {
                        Cooldown.this.list.remove(id);
                    }
                    if (message != null && !message.isEmpty() && Bukkit.getPlayer((UUID)id) != null) {
                        Bukkit.getPlayer((UUID)id).sendMessage(message);
                    }
                }
            }, (long)tick);
        }
    }

    public void addCooldown(ArrayList<UUID> ids, int tick, String message) {
        for (UUID id : ids) {
            this.addCooldown(id, tick, message);
        }
    }

}

