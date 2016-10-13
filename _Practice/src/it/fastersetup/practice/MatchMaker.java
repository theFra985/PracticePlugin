/*
 * Decompiled with CFR 0_115.
 */
package it.fastersetup.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import it.fastersetup.practice.Main;
import it.fastersetup.practice.Queue;
import it.fastersetup.practice.Api.GameType;
import it.fastersetup.practice.Api.Request;

public class MatchMaker {
    private Queue ranked;
    private Queue unranked;
    private Queue party;
    private int section;
    private int tr;
    private Main plugin;

    public MatchMaker(Main pl) {
        this.plugin = pl;
        this.ranked = new Queue(pl);
        this.unranked = new Queue(pl);
        this.party = new Queue(pl);
        this.section = 1;
        this.tr = 1;
    }

    public void match() {
        if (this.plugin.arenas.getAvailableArena() != null) {
            switch (this.section) {
                case 1: {
                    if (this.matchRanked()) {
                        this.tr = 4;
                        break;
                    }
                }
                case 2: {
                    if (this.matchUnranked()) {
                        this.tr = 4;
                        break;
                    }
                }
                case 3: {
                    if (!this.matchParty()) break;
                    this.tr = 4;
                    break;
                }
            }
            ++this.section;
            if (this.section > 3) {
                this.section = 1;
            }
            ++this.tr;
            if (this.tr <= 3) {
                this.match();
            } else {
                this.tr = 1;
            }
        }
    }

    public boolean inQueue(UUID id) {
        if (!(this.ranked.contains(id) || this.unranked.contains(id) || this.party.contains(id))) {
            return false;
        }
        return true;
    }

    public void addRanked(UUID id, GameType type) {
        this.ranked.add(id, type);
    }

    public void addUnranked(UUID id, GameType type) {
        this.unranked.add(id, type);
    }

    public void addParty(UUID id, GameType type) {
        this.party.add(id, type);
    }

    public void eject(UUID id) {
        this.ranked.remove(id);
        this.unranked.remove(id);
        this.party.remove(id);
    }

    public HashMap<GameType, Integer> getRanked() {
        HashMap<GameType, Integer> temp = new HashMap<GameType, Integer>();
        GameType[] arrgameType = GameType.values();
        int n = arrgameType.length;
        int n2 = 0;
        while (n2 < n) {
            GameType type = arrgameType[n2];
            if (type != GameType.UNKNOWN) {
                temp.put(type, this.ranked.size(type));
            }
            ++n2;
        }
        return temp;
    }

    public HashMap<GameType, Integer> getUnranked() {
        HashMap<GameType, Integer> temp = new HashMap<GameType, Integer>();
        GameType[] arrgameType = GameType.values();
        int n = arrgameType.length;
        int n2 = 0;
        while (n2 < n) {
            GameType type = arrgameType[n2];
            if (type != GameType.UNKNOWN) {
                temp.put(type, this.unranked.size(type));
            }
            ++n2;
        }
        return temp;
    }

    public HashMap<GameType, Integer> getParty() {
        HashMap<GameType, Integer> temp = new HashMap<GameType, Integer>();
        GameType[] arrgameType = GameType.values();
        int n = arrgameType.length;
        int n2 = 0;
        while (n2 < n) {
            GameType type = arrgameType[n2];
            if (type != GameType.UNKNOWN) {
                temp.put(type, this.party.size(type));
            }
            ++n2;
        }
        return temp;
    }

    private boolean matchRanked() {
        if (this.ranked.next() != null) {
            Map.Entry<UUID, UUID> players = this.ranked.next();
            if (this.plugin.arenas.getAvailableArena() != null) {
                Request r = new Request(players.getKey(), players.getValue());
                r.setRanked(true);
                r.setGameType(this.ranked.getGameType(players.getKey()));
                this.plugin.arenas.duel(r);
                this.ranked.remove(players.getKey());
                this.ranked.remove(players.getValue());
                return true;
            }
        }
        return false;
    }

    private boolean matchUnranked() {
        if (this.unranked.next() != null) {
            Map.Entry<UUID, UUID> players = this.unranked.next();
            if (this.plugin.arenas.getAvailableArena() != null) {
                Request r = new Request(players.getKey(), players.getValue());
                r.setGameType(this.unranked.getGameType(players.getKey()));
                this.plugin.arenas.duel(r);
                this.unranked.remove(players.getKey());
                this.unranked.remove(players.getValue());
                return true;
            }
        }
        return false;
    }

    private boolean matchParty() {
        if (this.party.next() != null) {
            Map.Entry<UUID, UUID> players = this.party.next();
            if (this.plugin.arenas.getAvailableArena() != null) {
                Request r = new Request(players.getKey(), players.getValue());
                r.setParty(true);
                r.setGameType(this.party.getGameType(players.getKey()));
                this.plugin.arenas.duel(r);
                this.party.remove(players.getKey());
                this.party.remove(players.getValue());
                return true;
            }
        }
        return false;
    }
}

