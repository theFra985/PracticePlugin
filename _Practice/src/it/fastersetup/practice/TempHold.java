/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.Inventory
 */
package it.fastersetup.practice;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.inventory.Inventory;

public class TempHold {
    private HashMap<UUID, Inventory> map = new HashMap<UUID, Inventory>();

    public void store(UUID id, Inventory inv) {
        this.map.put(id, inv);
    }

    public Inventory get(UUID id) {
        if (this.map.containsKey(id)) {
            return this.map.get(id);
        }
        return null;
    }

    public void remove(UUID id) {
        if (this.map.containsKey(id)) {
            this.map.remove(id);
        }
    }
}

