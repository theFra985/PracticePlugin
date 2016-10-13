/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.scoreboard.Scoreboard
 */
package it.fastersetup.practice.Manager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import it.fastersetup.practice.LiveUpdate;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.EasyBoard;
import it.fastersetup.practice.Api.GameType;
import it.fastersetup.practice.Api.Invs;
import it.fastersetup.practice.Api.Party;
import it.fastersetup.practice.Api.Request;

public class PartyManager {
    private HashMap<UUID, Map.Entry<Party, EasyBoard>> parties = new HashMap<UUID, Map.Entry<Party, EasyBoard>>();
    private ArrayList<UUID> wait = new ArrayList<UUID>();
    private HashMap<UUID, UUID> req = new HashMap<UUID, UUID>();
    private HashMap<UUID, BukkitTask> task = new HashMap<UUID, BukkitTask>();
    private HashMap<String, Map.Entry<UUID, UUID>> split = new HashMap<String, Map.Entry<UUID, UUID>>();
    private Main plugin;

    public PartyManager(Main pl) {
        this.plugin = pl;
    }

    public void newParty(UUID id) {
        Party party = new Party(id);
        EasyBoard board = new EasyBoard("\u00a7bParty");
        board.add(id, 2);
        board.update(id);
        Bukkit.getPlayer((UUID)id).getInventory().setContents(Invs.party(false, party.isOpen()));
        Bukkit.getPlayer((UUID)id).updateInventory();
        Bukkit.getPlayer((UUID)id).sendMessage("\u00a7bParty Creato");
        this.parties.put(id, new AbstractMap.SimpleEntry<Party, EasyBoard>(party, board));
        this.plugin.live.update(LiveUpdate.Current.LIST_PARTIES);
    }

    public void add(UUID owner, UUID id) {
        if (this.hasParty(owner) && !this.inParty(id)) {
            Party party = this.getParty(owner);
            Bukkit.getPlayer((UUID)party.getLeader()).sendMessage("\u00a7b\u00a7l" + Bukkit.getPlayer((UUID)id).getName() + " \u00a7r\u00a7e\u00e8 joinato nel party.");
            for (UUID u : party.getMembers()) {
                Bukkit.getPlayer((UUID)u).sendMessage("\u00a7b\u00a7l" + Bukkit.getPlayer((UUID)id).getName() + " \u00a7r\u00a7e\u00e8 joinato nel party.");
            }
            party.add(id);
            EasyBoard board = this.getBoard(owner);
            board.add(id, 1);
            board.update(party.getLeader());
            board.update(party.getMembers());
            Bukkit.getPlayer((UUID)id).getInventory().setContents(Invs.party(false, party.isOpen()));
            Bukkit.getPlayer((UUID)id).updateInventory();
            Bukkit.getPlayer((UUID)id).sendMessage("\u00a7bSei joinato nel party.");
            this.parties.put(owner, new AbstractMap.SimpleEntry<Party, EasyBoard>(party, board));
            this.plugin.live.update(LiveUpdate.Current.LIST_PARTIES);
            if (party.size() == 2) {
                Bukkit.getPlayer((UUID)party.getLeader()).getInventory().setContents(Invs.party(true, party.isOpen()));
            } else {
                Bukkit.getPlayer((UUID)party.getLeader()).getInventory().setContents(Invs.party(false, party.isOpen()));
            }
        }
    }

    public void add(Party party, UUID id) {
        this.add(party.getLeader(), id);
    }

    private void replace(UUID id, UUID n) {
        for (String a : this.split.keySet()) {
            if (this.split.get(a).getKey() == id) {
                UUID t = this.split.get(a).getValue();
                this.split.put(a, new AbstractMap.SimpleEntry<UUID, UUID>(n, t));
                break;
            }
            if (this.split.get(a).getValue() != id) continue;
            UUID t = this.split.get(a).getKey();
            this.split.put(a, new AbstractMap.SimpleEntry<UUID, UUID>(t, n));
            break;
        }
    }

    public void remove(UUID owner, UUID id) {
        if (this.hasParty(owner)) {
            Party party = this.getParty(owner);
            EasyBoard board = this.getBoard(owner);
            if (party.size() < 3) {
                Bukkit.getPlayer((UUID)owner).closeInventory();
            }
            if (party.size() == 1) {
                board.eject(party.getLeader());
                board.eject(party.getMembers());
                this.plugin.board.show(party.getLeader());
                this.parties.remove(owner);
                party = null;
                board = null;
                this.wait(owner);
                Bukkit.getPlayer((UUID)owner).getInventory().setContents(Invs.main(Bukkit.getPlayer((UUID)owner).getName()));
                Bukkit.getPlayer((UUID)owner).updateInventory();
                this.plugin.live.update(LiveUpdate.Current.LIST_PARTIES);
                return;
            }
            if (owner == id) {
                UUID nl = party.getMembers().get(0);
                this.replace(id, nl);
                party.remove(nl);
                party.setLeader(nl);
                board.eject(owner);
                board.remove(owner);
                board.remove(nl);
                board.add(nl, 2);
                board.update(party.getLeader());
                board.update(party.getMembers());
                this.plugin.board.show(owner);
                Bukkit.getPlayer((UUID)party.getLeader()).sendMessage("\u00a7e" + Bukkit.getPlayer((UUID)id).getName() + " has left the party.");
                if (party.getLeader() != nl) {
                    Bukkit.getPlayer((UUID)party.getLeader()).sendMessage("\u00a7b\u00a7l" + Bukkit.getPlayer((UUID)nl).getName() + " \u00a7r\u00a7eis now the leader.");
                }
                if (party.getLeader() == nl) {
                    Bukkit.getPlayer((UUID)party.getLeader()).sendMessage("\u00a7b\u00a7lYou \u00a7r\u00a7eare now the leader.");
                }
                for (UUID u : party.getMembers()) {
                    Bukkit.getPlayer((UUID)u).sendMessage("\u00a7e" + Bukkit.getPlayer((UUID)id).getName() + " has left the party.");
                    if (u != nl) {
                        Bukkit.getPlayer((UUID)u).sendMessage("\u00a7b\u00a7l" + Bukkit.getPlayer((UUID)nl).getName() + " \u00a7r\u00a7eis now the leader.");
                    }
                    if (u != nl) continue;
                    Bukkit.getPlayer((UUID)u).sendMessage("\u00a7b\u00a7lYou \u00a7r\u00a7eare now the leader.");
                }
                this.wait(owner);
                Bukkit.getPlayer((UUID)owner).getInventory().setContents(Invs.main(Bukkit.getPlayer((UUID)owner).getName()));
                Bukkit.getPlayer((UUID)owner).updateInventory();
                Bukkit.getPlayer((UUID)nl).getInventory().setContents(Invs.party(true, party.isOpen()));
                this.parties.remove(owner);
                this.parties.put(nl, new AbstractMap.SimpleEntry<Party, EasyBoard>(party, board));
                this.plugin.live.update(LiveUpdate.Current.LIST_PARTIES);
                return;
            }
            board.remove(id);
            board.update(party.getLeader());
            board.update(party.getMembers());
            this.plugin.board.show(id);
            party.remove(id);
            Bukkit.getPlayer((UUID)id).setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            Bukkit.getPlayer((UUID)party.getLeader()).sendMessage("\u00a75\u00a7l" + Bukkit.getPlayer((UUID)id).getName() + " \u00a7r\u00a7ehas left the party.");
            for (UUID u : party.getMembers()) {
                Bukkit.getPlayer((UUID)u).sendMessage("\u00a75\u00a7l" + Bukkit.getPlayer((UUID)id).getName() + " \u00a7r\u00a7ehas left the party.");
            }
            this.wait(id);
            Bukkit.getPlayer((UUID)id).getInventory().setContents(Invs.main(Bukkit.getPlayer((UUID)id).getName()));
            Bukkit.getPlayer((UUID)id).updateInventory();
            if (party.size() == 2) {
                Bukkit.getPlayer((UUID)party.getLeader()).getInventory().setContents(Invs.party(true, party.isOpen()));
            } else {
                Bukkit.getPlayer((UUID)party.getLeader()).getInventory().setContents(Invs.party(false, party.isOpen()));
            }
            this.parties.put(owner, new AbstractMap.SimpleEntry<Party, EasyBoard>(party, board));
            this.plugin.live.update(LiveUpdate.Current.LIST_PARTIES);
        }
    }

    public void eject(UUID id) {
        HashMap<UUID, Map.Entry<Party, EasyBoard>> temp = this.parties;
        HashMap<UUID, UUID> ids = new HashMap<UUID, UUID>();
        for (Map.Entry<UUID, Map.Entry<Party, EasyBoard>> entry : temp.entrySet()) {
            if (entry.getKey() == id) {
                ids.put(id, id);
                continue;
            }
            if (!entry.getValue().getKey().contains(id)) continue;
            ids.put(entry.getKey(), id);
        }
        for (UUID u : ids.keySet()) {
            this.remove(u, (UUID)ids.get(u));
        }
    }

    public void sendRequest(final UUID id, final UUID host) {
        if (this.hasParty(host) && !this.req.containsKey(id)) {
            this.req.put(id, this.getParty(host).getLeader());
            Bukkit.getPlayer((UUID)id).openInventory(Invs.partyRequest(this.getParty(host)));
            BukkitTask t = Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, new Runnable(){

                @Override
                public void run() {
                    if (PartyManager.this.req.containsKey(id)) {
                        if (Bukkit.getPlayer((UUID)id) != null) {
                            if (Bukkit.getPlayer((UUID)id).getOpenInventory() != null && Bukkit.getPlayer((UUID)id).getOpenInventory().getTitle().equals("\u00a7bParty Request")) {
                                Bukkit.getPlayer((UUID)id).closeInventory();
                            }
                            Bukkit.getPlayer((UUID)id).sendMessage("\u00a7eParty request expired.");
                        }
                        if (Bukkit.getPlayer((UUID)host) != null) {
                            Bukkit.getPlayer((UUID)host).sendMessage("\u00a7eRequest expired for " + Bukkit.getPlayer((UUID)id).getName());
                        }
                        PartyManager.this.req.remove(id);
                        if (PartyManager.this.task.containsKey(id)) {
                            ((BukkitTask)PartyManager.this.task.get(id)).cancel();
                            PartyManager.this.task.remove(id);
                        }
                    }
                }
            }, 600);
            this.task.put(id, t);
        }
    }

    public boolean accept(UUID id) {
        if (this.req.containsKey(id) && this.hasParty(this.req.get(id))) {
            if (this.task.containsKey(id)) {
                this.task.get(id).cancel();
                this.task.remove(id);
            }
            this.add(this.req.get(id), id);
            this.req.remove(id);
            return true;
        }
        return false;
    }

    public boolean deny(UUID id) {
        if (this.req.containsKey(id)) {
            if (this.task.containsKey(id)) {
                this.task.get(id).cancel();
                this.task.remove(id);
            }
            if (Bukkit.getPlayer((UUID)this.req.get(id)) != null) {
                Bukkit.getPlayer((UUID)this.req.get(id)).sendMessage("\u00a77\u00a7l" + Bukkit.getPlayer((UUID)id).getName() + " \u00a7r\u00a7edenied the request.");
            }
            this.req.remove(id);
            return true;
        }
        return false;
    }

    public boolean isRequesting(UUID id) {
        if (this.req.containsKey(id)) {
            return true;
        }
        if (this.req.values().contains(id)) {
            return true;
        }
        return false;
    }

    public void stopReq(UUID id) {
        for (UUID u : this.req.keySet()) {
            if (this.req.get(u) != id) continue;
            this.deny(u);
        }
    }

    public boolean hasRequest(UUID id) {
        return this.req.containsKey(id);
    }

    private void wait(final UUID id) {
        this.wait.add(id);
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.plugin, new Runnable(){

            @Override
            public void run() {
                PartyManager.this.wait.remove(id);
            }
        }, 100);
    }

    public boolean waiting(UUID id) {
        return this.wait.contains(id);
    }

    public Party getParty(UUID id) {
        if (this.parties.containsKey(id)) {
            return this.parties.get(id).getKey();
        }
        return null;
    }

    public ArrayList<Party> getParties() {
        ArrayList<Party> temp = new ArrayList<Party>();
        for (Map.Entry<UUID, Map.Entry<Party, EasyBoard>> entry : this.parties.entrySet()) {
            temp.add(entry.getValue().getKey());
        }
        return temp;
    }

    private EasyBoard getBoard(UUID id) {
        if (this.parties.containsKey(id)) {
            return this.parties.get(id).getValue();
        }
        return new EasyBoard("\u00a7bParty");
    }

    public void showBoard(UUID id) {
        if (this.inParty(id)) {
            this.getBoard(this.getLeader(id)).update(id);
        }
    }

    public boolean openParty(UUID id) {
        if (this.hasParty(this.getLeader(id))) {
            return this.getParty(this.getLeader(id)).isOpen();
        }
        return false;
    }

    public UUID getLeader(UUID id) {
        for (Party party : this.getParties()) {
            if (!party.contains(id)) continue;
            return party.getLeader();
        }
        return null;
    }

    public boolean hasParty(UUID id) {
        return this.parties.containsKey(id);
    }

    public boolean inParty(UUID id) {
        for (Map.Entry<UUID, Map.Entry<Party, EasyBoard>> entry : this.parties.entrySet()) {
            if (!entry.getValue().getKey().contains(id)) continue;
            return true;
        }
        return false;
    }

    public boolean sameParty(UUID p1, UUID p2) {
        if (this.getLeader(p1) != null && this.getParty(this.getLeader(p1)) != null && this.getParty(this.getLeader(p1)).contains(p2)) {
            return true;
        }
        return false;
    }

    public boolean twoPlayer(UUID id) {
        if (this.hasParty(this.getLeader(id)) && this.getParty(this.getLeader(id)).size() == 2) {
            return true;
        }
        return false;
    }

    public void setOpen(UUID id, boolean open) {
        if (this.inParty(id)) {
            id = this.getLeader(id);
            Party party = this.getParty(id);
            party.setOpen(open);
            EasyBoard board = this.parties.get(id).getValue();
            this.parties.put(id, new AbstractMap.SimpleEntry<Party, EasyBoard>(party, board));
            Bukkit.getPlayer((UUID)party.getLeader()).getInventory().setContents(Invs.party(party.size() == 2, open));
            for (UUID m : party.getMembers()) {
                Bukkit.getPlayer((UUID)m).getInventory().setContents(Invs.party(false, open));
            }
            if (open) {
                party.msgAll("\u00a7bParty settato su: \u00a7a\u00a7lPublico");
            } else {
                party.msgAll("\u00a7bParty settato su: \u00a7c\u00a7lPrivato");
            }
        }
    }

    public boolean sameSize(UUID id, UUID other) {
        if (this.inParty(id) && this.inParty(other) && this.getParty(this.getLeader(id)).size() == this.getParty(this.getLeader(other)).size()) {
            return true;
        }
        return false;
    }

    public int size(UUID id) {
        if (this.inParty(id)) {
            return this.getParty(this.getLeader(id)).size();
        }
        return 0;
    }

    public boolean bothOne(UUID id, UUID other) {
        if (this.inParty(id) && this.inParty(other)) {
            if (this.size(id) == 1 && this.size(other) == 1) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean split(UUID id, GameType type) {
        Party p;
        if (this.plugin.arenas.getAvailableArena() != null && this.hasParty(id) && (p = this.getParty(id)).size() >= 3) {
            if (p.isOpen()) {
                this.setOpen(id, false);
            }
            Party o = p.split();
            Request r = new Request(p.getLeader(), o.getLeader());
            r.setGameType(type);
            r.setParty(true);
            r.setFFA(false);
            r.setSent(true);
            this.parties.put(o.getLeader(), new AbstractMap.SimpleEntry<Party, EasyBoard>(o, null));
            String a = this.plugin.arenas.duel(r);
            if (a != null) {
                this.split.put(a, new AbstractMap.SimpleEntry<UUID, UUID>(id, o.getLeader()));
            }
            return true;
        }
        return false;
    }

    public void combine(String a) {
        if (this.split.containsKey(a) && this.hasParty(this.getLeader(this.split.get(a).getKey()))) {
            UUID id = this.getLeader(this.split.get(a).getKey());
            Party p = this.getParty(id);
            ArrayList<UUID> list = this.plugin.arenas.getPlayers(a);
            if (list != null) {
                for (UUID u : list) {
                    if (p.contains(u)) continue;
                    p.add(u);
                }
                EasyBoard board = new EasyBoard("\u00a7bParty");
                board.add(p.getLeader(), 2);
                for (UUID u2 : p.getMembers()) {
                    board.add(u2, 1);
                }
                if (this.hasParty(this.getLeader(this.split.get(a).getValue()))) {
                    this.parties.remove(this.split.get(a).getValue());
                }
                this.parties.put(id, new AbstractMap.SimpleEntry<Party, EasyBoard>(p, board));
            }
        }
    }

}

