/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 */
package it.fastersetup.practice.Api;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Party {
    private UUID leader;
    private ArrayList<UUID> members = new ArrayList<UUID>();
    private boolean open;

    public Party(UUID id) {
        this.leader = id;
    }

    public void add(UUID id) {
        if (id == this.leader) {
            return;
        }
        if (!this.members.contains(id)) {
            this.members.add(id);
        }
    }

    public void remove(UUID id) {
        if (id == this.leader) {
            return;
        }
        if (this.members.contains(id)) {
            this.members.remove(id);
        }
    }

    public boolean contains(UUID id) {
        if (id == this.leader || this.members != null && this.members.contains(id)) {
            return true;
        }
        return false;
    }

    public void setLeader(UUID id) {
        this.leader = id;
    }

    public UUID getLeader() {
        return this.leader;
    }

    public ArrayList<UUID> getMembers() {
        return this.members;
    }

    public ArrayList<UUID> memberClone() {
        ArrayList<UUID> temp = new ArrayList<UUID>();
        for (UUID id : this.members) {
            temp.add(id);
        }
        return temp;
    }

    public ArrayList<UUID> getAll() {
        ArrayList<UUID> temp = this.memberClone();
        temp.add(this.leader);
        return temp;
    }

    public void tp(Location loc) {
        Bukkit.getPlayer((UUID)this.leader).teleport(loc);
        for (UUID id : this.members) {
            Bukkit.getPlayer((UUID)id).teleport(loc);
        }
    }

    public int size() {
        if (this.members != null) {
            return this.members.size() + 1;
        }
        return 1;
    }

    public void msgAll(String m) {
        for (UUID id : this.getAll()) {
            Bukkit.getPlayer((UUID)id).sendMessage(m);
        }
    }

    public void setOpen(boolean o) {
        this.open = o;
    }

    public boolean isOpen() {
        return this.open;
    }

    public UUID randomMember() {
        if (this.members.size() > 0) {
            return this.members.get(new Random().nextInt(this.members.size()));
        }
        return null;
    }

    public Party split() {
        UUID id = this.randomMember();
        if (id != null) {
            int r = (int)(Math.floor(this.size() / 2) - 1.0);
            this.remove(id);
            Party p = new Party(id);
            int i = 0;
            while (i < r) {
                UUID u = this.randomMember();
                this.remove(u);
                p.add(u);
                ++i;
            }
            return p;
        }
        return null;
    }
}

