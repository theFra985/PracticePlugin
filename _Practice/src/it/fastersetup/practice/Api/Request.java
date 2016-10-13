/*
 * Decompiled with CFR 0_115.
 */
package it.fastersetup.practice.Api;

import java.util.UUID;

import it.fastersetup.practice.Api.GameType;

public class Request {
    private UUID sender;
    private UUID other;
    private boolean ffa = false;
    private boolean ranked = false;
    private boolean party = false;
    private GameType type = GameType.UNKNOWN;
    private boolean sent = false;

    public Request(UUID sender, UUID other) {
        this.sender = sender;
        this.other = other;
    }

    public UUID getSender() {
        return this.sender;
    }

    public UUID getReceiver() {
        return this.other;
    }

    public void setSender(UUID id) {
        this.sender = id;
    }

    public void setReceiver(UUID id) {
        this.other = id;
    }

    public boolean isFFA() {
        return this.ffa;
    }

    public void setFFA(boolean b) {
        this.ffa = b;
    }

    public boolean isRanked() {
        return this.ranked;
    }

    public void setRanked(boolean b) {
        this.ranked = b;
    }

    public boolean isParty() {
        return this.party;
    }

    public void setParty(boolean b) {
        this.party = b;
    }

    public GameType getGameType() {
        return this.type;
    }

    public void setGameType(GameType t) {
        this.type = t;
    }

    public boolean isSent() {
        return this.sent;
    }

    public void setSent(boolean b) {
        this.sent = b;
    }
}

