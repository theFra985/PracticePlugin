/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package it.fastersetup.practice;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.GameType;
import it.fastersetup.practice.Api.Invs;
import it.fastersetup.practice.Api.Request;

public class DuelRequests {
    private HashMap<UUID, Request> map = new HashMap<UUID, Request>();
    private HashMap<UUID, BukkitTask> task = new HashMap<UUID, BukkitTask>();
    private Main plugin;

    public DuelRequests(Main pl) {
        this.plugin = pl;
    }

    public void sendRequest(UUID id) {
        if (this.map.containsKey(id)) {
            Request r = this.map.get(id);
            r.setSent(true);
            this.map.put(id, r);
            this.timer(id);
            if (r.isFFA()) {
                Bukkit.getPlayer((UUID)r.getReceiver()).openInventory(Invs.duelRequest(r.getReceiver(), id, r.getGameType(), true));
            } else {
                Bukkit.getPlayer((UUID)r.getReceiver()).openInventory(Invs.duelRequest(r.getReceiver(), id, r.getGameType(), false));
            }
        }
    }

    public UUID get(UUID id) {
        if (this.map.containsKey(id)) {
            return this.map.get(id).getReceiver();
        }
        return null;
    }

    public void prepare(UUID owner, UUID id, boolean party) {
        Request r = new Request(owner, id);
        r.setParty(party);
        this.map.put(owner, r);
    }

    public void prepMode(UUID owner, GameType type) {
        if (this.map.containsKey(owner)) {
            Request r = this.map.get(owner);
            r.setGameType(type);
            this.map.put(owner, r);
        }
    }

    public void prepF(UUID owner) {
        if (this.map.containsKey(owner)) {
            Request r = this.map.get(owner);
            r.setFFA(true);
            this.map.put(owner, r);
        }
    }

    private UUID getMain(UUID id) {
        for (Request r : this.map.values()) {
            if (r.getReceiver() != id) continue;
            return r.getSender();
        }
        return null;
    }

    public void accept(UUID p1) {
        UUID id = this.getMain(p1);
        if (id == null) {
            return;
        }
        if (this.map.containsKey(id)) {
            Request r = this.map.get(id);
            this.plugin.arenas.duel(r);
            this.map.remove(id);
            if (this.task.containsKey(id)) {
                this.task.get(id).cancel();
                this.task.remove(id);
            }
        }
    }

    public void deny(UUID p1) {
        UUID id = this.getMain(p1);
        if (id == null) {
            return;
        }
        if (this.map.containsKey(id)) {
            Bukkit.getPlayer((UUID)p1).sendMessage("\u00a7eRichiesta rifiutata.");
            Bukkit.getPlayer((UUID)id).sendMessage("\u00a7b" + Bukkit.getPlayer((UUID)p1).getName() + " \u00a7eRichiesta rifiutata.");
            this.map.remove(id);
            if (this.task.containsKey(id)) {
                this.task.get(id).cancel();
                this.task.remove(id);
            }
        }
    }

    public void eject(UUID id) {
        for (UUID uid : this.map.keySet()) {
            if (uid != id && this.map.get(id).getReceiver() != id) continue;
            this.map.remove(id);
        }
        if (this.task.containsKey(id)) {
            this.task.get(id).cancel();
            this.task.remove(id);
        }
    }

    private void timer(final UUID id) {
        BukkitTask t = Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, new Runnable(){

            @Override
            public void run() {
                if (DuelRequests.this.map.containsKey(id) && DuelRequests.this.task.containsKey(id)) {
                    Request r = (Request)DuelRequests.this.map.get(id);
                    Bukkit.getPlayer((UUID)id).sendMessage("\u00a7a\u00a7lRichiesta a \u00a7r\u00a7b\u00a7l" + Bukkit.getPlayer((UUID)r.getReceiver()).getName() + " \u00a7r\u00a7a\u00a7lScaduto.");
                    Player rc = Bukkit.getPlayer((UUID)r.getReceiver());
                    rc.sendMessage("\u00a7e\u00a7lRichiesta duello scaduta.");
                    if (rc.getOpenInventory() != null && rc.getOpenInventory().getTopInventory().getTitle().startsWith("\u00a7bRichiesta duello")) {
                        rc.closeInventory();
                    }
                    DuelRequests.this.map.remove(id);
                    ((BukkitTask)DuelRequests.this.task.get(id)).cancel();
                    DuelRequests.this.task.remove(id);
                }
            }
        }, 600);
        this.task.put(id, t);
    }

    public boolean hasRequest(UUID id) {
        if (this.map.containsKey(id) && this.map.get(id).isSent()) {
            return true;
        }
        for (Request r : this.map.values()) {
            if (!r.isSent() || r.getReceiver() != id && r.getSender() != id) continue;
            return true;
        }
        return false;
    }

    public boolean isAccepting(UUID id) {
        for (Request r : this.map.values()) {
            if (!r.isSent() || r.getReceiver() != id) continue;
            return true;
        }
        return false;
    }

}

