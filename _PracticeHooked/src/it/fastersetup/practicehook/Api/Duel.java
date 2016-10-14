/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Item
 */
package it.fastersetup.practicehook.Api;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;

import it.fastersetup.practicehook.Api.GameType;
import it.fastersetup.practicehook.Api.Kits;

public class Duel {
    private ArrayList<UUID> a = new ArrayList<UUID>();
    private ArrayList<UUID> b = new ArrayList<UUID>();
    private ArrayList<UUID> s = new ArrayList<UUID>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Location> delete = new ArrayList<Location>();
    private boolean ffa = false;
    private boolean ranked = false;
    private boolean party = false;
    private Location sa = null;
    private Location sb = null;
    private GameType type = GameType.UNKNOWN;

    public Duel(Location pa, Location pb, GameType gt) {
        this.sa = pa;
        this.sb = pb;
        this.type = gt;
    }

    public void addA(UUID id) {
        if (!this.a.contains(id)) {
            this.a.add(id);
        }
    }

    public void addA(ArrayList<UUID> list) {
        for (UUID id : list) {
            this.addA(id);
        }
    }

    public void addB(UUID id) {
        if (!this.b.contains(id)) {
            this.b.add(id);
        }
    }

    public void addB(ArrayList<UUID> list) {
        for (UUID id : list) {
            this.addB(id);
        }
    }

    public void addS(UUID id) {
        if (!this.s.contains(id)) {
            this.s.add(id);
        }
    }

    public void remove(UUID id) {
        if (this.a.contains(id)) {
            this.a.remove(id);
        }
        if (this.b.contains(id)) {
            this.b.remove(id);
        }
        if (this.s.contains(id)) {
            this.s.remove(id);
        }
    }

    private ArrayList<UUID> aClone() {
        ArrayList<UUID> temp = new ArrayList<UUID>();
        for (UUID id : this.a) {
            temp.add(id);
        }
        return temp;
    }

    private ArrayList<UUID> bClone() {
        ArrayList<UUID> temp = new ArrayList<UUID>();
        for (UUID id : this.b) {
            temp.add(id);
        }
        return temp;
    }

    public int teamsLeft(UUID id) {
        ArrayList<UUID> bl;
        int f = 0;
        ArrayList<UUID> al = this.aClone();
        if (al.contains(id)) {
            al.remove(id);
        }
        if ((bl = this.bClone()).contains(id)) {
            bl.remove(id);
        }
        if (al.size() > 0) {
            ++f;
        }
        if (bl.size() > 0) {
            ++f;
        }
        return f;
    }

    public UUID left() {
        if (this.a.size() > 0) {
            return this.a.get(0);
        }
        return this.b.get(0);
    }

    public ArrayList<UUID> getAllPlayers() {
        ArrayList<UUID> temp = new ArrayList<UUID>();
        if (this.a.size() > 0) {
            for (UUID id : this.a) {
                temp.add(id);
            }
        }
        if (this.b.size() > 0) {
            for (UUID id : this.b) {
                temp.add(id);
            }
        }
        if (this.s.size() > 0) {
            for (UUID id : this.s) {
                temp.add(id);
            }
        }
        return temp;
    }

    public ArrayList<UUID> getAPlayers() {
        return this.a;
    }

    public ArrayList<UUID> getBPlayers() {
        return this.b;
    }

    public ArrayList<UUID> getSpectators() {
        return this.s;
    }

    public boolean onTeamA(UUID id) {
        return this.a.contains(id);
    }

    public boolean contains(UUID id) {
        if (!(this.a.contains(id) || this.b.contains(id) || this.s.contains(id))) {
            return false;
        }
        return true;
    }

    public boolean isIngame(UUID id) {
        if (!this.a.contains(id) && !this.b.contains(id)) {
            return false;
        }
        return true;
    }

    public boolean isSpectating(UUID id) {
        return this.s.contains(id);
    }

    public void setFFA(boolean b) {
        this.ffa = b;
    }

    public boolean isFFA() {
        return this.ffa;
    }

    public void setRanked(boolean b) {
        this.ranked = b;
    }

    public boolean isRanked() {
        return this.ranked;
    }

    public void setParty(boolean b) {
        this.party = b;
    }

    public boolean isParty() {
        return this.party;
    }

    public boolean isSingles() {
        return !this.party;
    }

    public UUID opp(UUID id) {
        if (this.a.contains(id)) {
            if (this.b.size() > 0) {
                return this.b.get(0);
            }
            return this.a.get(0);
        }
        if (this.a.size() > 0) {
            return this.a.get(0);
        }
        return this.b.get(0);
    }

    public Location getSpawnA() {
        return this.sa;
    }

    public Location getSpawnB() {
        return this.sb;
    }

    public GameType getGameType() {
        return this.type;
    }

    public void tpPlayers() {
        if (this.a.size() > 0) {
            for (UUID id : this.a) {
                Bukkit.getPlayer((UUID)id).teleport(this.sa);
            }
        }
        if (this.b.size() > 0) {
            for (UUID id : this.b) {
                Bukkit.getPlayer((UUID)id).teleport(this.sb);
            }
        }
    }

    public int amount() {
        return this.a.size() + this.b.size();
    }

    public void applyKit() {
        for (UUID id : this.getAllPlayers()) {
            Kits.apply(id, this.type);
            Bukkit.getPlayer((UUID)id).setHealthScaled(false);
        }
    }

    public void setFoodHealth() {
        for (UUID id : this.getAllPlayers()) {
            Bukkit.getPlayer((UUID)id).setHealth(20.0);
            Bukkit.getPlayer((UUID)id).setFoodLevel(20);
            Bukkit.getPlayer((UUID)id).setSaturation(20.0f);
        }
    }

    public void msgAll(String message) {
        for (UUID id : this.getAllPlayers()) {
            Bukkit.getPlayer((UUID)id).sendMessage(message);
        }
    }

    public void msgA(String message) {
        for (UUID id : this.a) {
            Bukkit.getPlayer((UUID)id).sendMessage(message);
        }
    }

    public void msgB(String message) {
        for (UUID id : this.b) {
            Bukkit.getPlayer((UUID)id).sendMessage(message);
        }
    }

    public void msgTeam(UUID id, String msg) {
        if (this.a.contains(id)) {
            this.msgA(msg);
        }
        if (this.b.contains(id)) {
            this.msgB(msg);
        }
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void removeItems() {
        for (Item i : this.items) {
            if (i.isDead()) continue;
            i.remove();
        }
        this.items.clear();
        this.removeBlocks();
    }

    public void removeBlocks() {
        for (Location loc : this.delete) {
            Material t = loc.getBlock().getType();
            if (t != Material.FIRE && t != Material.STATIONARY_WATER && t != Material.WATER && t != Material.STATIONARY_LAVA && t != Material.LAVA) continue;
            loc.getBlock().setType(Material.AIR);
        }
        this.delete.clear();
    }

    public void addBlock(Location loc) {
        if (!this.delete.contains((Object)loc)) {
            this.delete.add(loc);
        }
    }
}

