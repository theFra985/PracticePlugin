/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package it.fastersetup.practice;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.GameType;
import it.fastersetup.practice.Api.QEntry;

public class Queue {
    private QEntry[] map;
    private Main plugin;

    public Queue(Main pl) {
        this.plugin = pl;
    }

    public String[] list() {
        String[] f = new String[this.map.length];
        int i = 0;
        while (i < this.map.length) {
            f[i] = String.valueOf(String.valueOf(i)) + " " + Bukkit.getPlayer((UUID)this.map[i].getUUID()).getName();
            ++i;
        }
        return f;
    }

    public int size(GameType type) {
        if (this.map == null) {
            return 0;
        }
        int f = 0;
        QEntry[] arrqEntry = this.map;
        int n = arrqEntry.length;
        int n2 = 0;
        while (n2 < n) {
            QEntry entry = arrqEntry[n2];
            if (entry.getType() == type) {
                ++f;
            }
            ++n2;
        }
        return f;
    }

    public void add(UUID id, GameType type) {
        int length = 0;
        length = this.map == null ? 1 : this.map.length + 1;
        QEntry[] temp = new QEntry[length];
        if (this.map != null) {
            int i = 0;
            while (i < this.map.length) {
                temp[i] = this.map[i];
                ++i;
            }
        }
        temp[temp.length - 1] = new QEntry(id, type);
        this.map = temp;
    }

    public void remove(UUID id) {
        if (this.contains(id)) {
            int index = 0;
            int i = 0;
            while (i < this.map.length) {
                if (this.map[i].getUUID() == id) {
                    index = i;
                    break;
                }
                ++i;
            }
            QEntry[] temp = new QEntry[this.map.length - 1];
            int i2 = 0;
            while (i2 < temp.length) {
                if (i2 < index) {
                    temp[i2] = this.map[i2];
                } else if (i2 >= index) {
                    temp[i2] = this.map[i2 + 1];
                }
                ++i2;
            }
            this.map = temp;
        }
    }

    public boolean contains(UUID id) {
        if (this.map == null) {
            return false;
        }
        QEntry[] arrqEntry = this.map;
        int n = arrqEntry.length;
        int n2 = 0;
        while (n2 < n) {
            QEntry entry = arrqEntry[n2];
            if (entry.getUUID() == id) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public GameType getGameType(UUID id) {
        if (this.contains(id)) {
            QEntry[] arrqEntry = this.map;
            int n = arrqEntry.length;
            int n2 = 0;
            while (n2 < n) {
                QEntry entry = arrqEntry[n2];
                if (entry.getUUID() == id) {
                    return entry.getType();
                }
                ++n2;
            }
        }
        return GameType.UNKNOWN;
    }

    private ArrayList<UUID> getOthers(UUID id) {
        if (this.contains(id)) {
            ArrayList<UUID> f = new ArrayList<UUID>();
            if (this.getGameType(id) != GameType.UNKNOWN) {
                GameType m = this.getGameType(id);
                QEntry[] arrqEntry = this.map;
                int n = arrqEntry.length;
                int n2 = 0;
                while (n2 < n) {
                    QEntry entry = arrqEntry[n2];
                    if (entry.getUUID() != id && entry.getType() == m) {
                        f.add(entry.getUUID());
                    }
                    ++n2;
                }
                return f;
            }
        }
        return null;
    }

    public Map.Entry<UUID, UUID> next() {
        UUID p1;
        if (this.map == null) {
            return null;
        }
        if (this.map.length >= 2 && this.getOthers(p1 = this.map[0].getUUID()) != null) {
            UUID p2 = this.plugin.elo.closestELO(p1, this.getOthers(p1), this.getGameType(p1));
            if (p1 != null && p2 != null) {
                return new AbstractMap.SimpleEntry<UUID, UUID>(p1, p2);
            }
        }
        return null;
    }
}

