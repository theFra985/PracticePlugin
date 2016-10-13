/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 */
package it.fastersetup.practice;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.API;

public class Lobby {
    private ArrayList<UUID> list;
    private Location lobby;
    private Main plugin;

    public Lobby(Main pl) {
        this.plugin = pl;
        this.list = new ArrayList<UUID>();
        FileConfiguration data = pl.getMainConfig();
        if (data.contains("spawn")) {
            this.lobby = API.stringToLoc(data.getString("spawn"));
        }
    }

    public void load() {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            this.list.add(p.getUniqueId());
            ++n2;
        }
    }

    public void save() {
        if (this.isSet()) {
            FileConfiguration data = this.plugin.getMainConfig();
            data.set("spawn", (Object)API.locToString(this.lobby));
            this.plugin.saveMainConfig(data);
        }
    }

    public void add(UUID id) {
        if (!this.list.contains(id)) {
            this.list.add(id);
        }
    }

    public void remove(UUID id) {
        if (this.list.contains(id)) {
            this.list.remove(id);
        }
    }

    public boolean contains(UUID id) {
        return this.list.contains(id);
    }

    public void set(Location loc) {
        this.lobby = loc;
    }

    public Location getLoc() {
        return this.lobby;
    }

    public boolean isSet() {
        if (this.lobby != null) {
            return true;
        }
        return false;
    }

    public void tp(UUID id) {
        if (this.lobby != null) {
            Bukkit.getPlayer((UUID)id).teleport(this.lobby);
        }
    }

    public void tp(Player player) {
        if (this.lobby != null) {
            player.teleport(this.lobby);
        }
    }

    public void tpAll() {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player player = arrplayer[n2];
            this.tp(player);
            ++n2;
        }
    }
}

