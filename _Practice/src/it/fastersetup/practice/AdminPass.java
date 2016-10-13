/*
 * Decompiled with CFR 0_115.
 */
package it.fastersetup.practice;

import java.util.ArrayList;
import java.util.UUID;

public class AdminPass {
    private ArrayList<UUID> list = new ArrayList<UUID>();

    public boolean contains(UUID id) {
        return this.list.contains(id);
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
}

