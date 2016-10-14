/*
 * Decompiled with CFR 0_115.
 */
package it.fastersetup.practicehook.Api;

import java.util.UUID;

import it.fastersetup.practicehook.Api.GameType;

public class QEntry {
    private UUID id;
    private GameType type;

    public QEntry(UUID uuid, GameType gt) {
        this.id = uuid;
        this.type = gt;
    }

    public UUID getUUID() {
        return this.id;
    }

    public GameType getType() {
        return this.type;
    }
}

