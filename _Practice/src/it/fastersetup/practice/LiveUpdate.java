/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 */
package it.fastersetup.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.Invs;

public class LiveUpdate {
    private HashMap<UUID, Current> map = new HashMap<UUID, Current>();
    private Main plugin;

    public LiveUpdate(Main pl) {
        this.plugin = pl;
    }

    public void add(UUID id, Current c) {
        if (c == Current.NONE) {
            this.remove(id);
        } else {
            this.map.put(id, c);
        }
    }

    public void remove(UUID id) {
        if (this.map.containsKey(id)) {
            this.map.remove(id);
        }
    }

    public Current get(UUID id) {
        if (this.map.containsKey(id)) {
            return this.map.get(id);
        }
        return Current.NONE;
    }

    public void update(Current c) {
        Inventory inv = null;
        if (c == Current.NONE) {
            return;
        }
        if (c == Current.LIST_PARTIES) {
            inv = Invs.listParties(this.plugin.pman.getParties());
        } else if (c == Current.RANKED) {
            inv = Invs.ranked(this.plugin.mm.getRanked());
        } else if (c == Current.UNRANKED) {
            inv = Invs.unranked(this.plugin.mm.getUnranked());
        } else if (c == Current.PARTY) {
            inv = Invs.party2(this.plugin.mm.getParty());
        }
        for (Map.Entry<UUID, Current> entry : this.map.entrySet()) {
            if (entry.getValue() != c || inv == null) continue;
            Bukkit.getPlayer((UUID)entry.getKey()).openInventory(inv);
            this.map.put(entry.getKey(), c);
        }
    }
    public static enum Current {
        NONE,
        LIST_PARTIES,
        RANKED,
        UNRANKED,
        PARTY;
        

        private Current(String string2, int n2) {
        }
        Current(){
        }
    }

}

