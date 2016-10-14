/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.propvp.game.ladder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.propvp.Practice;
import net.propvp.event.PartyDisbandEvent;
import net.propvp.event.PartyJoinEvent;
import net.propvp.event.PartyKickEvent;
import net.propvp.event.PartyLeaveEvent;
import net.propvp.file.Config;
import net.propvp.game.GameMode;
import net.propvp.game.Match;
import net.propvp.game.arena.Arena;
import net.propvp.game.arena.ArenaManager;
import net.propvp.game.ladder.Ladder;
import net.propvp.gui.MenuManager;
import net.propvp.party.Party;
import net.propvp.player.DataManager;
import net.propvp.player.InventoryManager;
import net.propvp.player.PlayerData;
import net.propvp.timing.ElapsedTimer;
import net.propvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class TvT
implements Ladder,
Listener {
    private String name;
    private UUID identifier;
    private GameMode game;
    private List<Match> matches;
    private LinkedList<Party> queue;
    private Map<Party, ElapsedTimer> timer;
    private BukkitTask matchmaker;
    private int playersPerTeam;
    private boolean status;
    FileConfiguration messages;

    public TvT(final GameMode gameMode) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Practice.getInstance());
        this.name = "2vs2  Party " + gameMode.getName();
        this.game = gameMode;
        this.identifier = UUID.randomUUID();
        this.matches = new ArrayList<Match>();
        this.queue = new LinkedList();
        this.timer = new HashMap<Party, ElapsedTimer>();
        this.status = true;
        this.messages = Practice.getBackend().getMessagesConfig().getConfig();
        this.playersPerTeam = 2;
        this.matchmaker = new BukkitRunnable(){
            private Arena arena;

            public void run() {
                if (TvT.this.queue.size() >= 2) {
                    Object[] arrobject = TvT.this.queue.toArray();
                    if (arrobject[0] != null && arrobject[1] != null) {
                        Party party = (Party)arrobject[0];
                        Party party2 = (Party)arrobject[1];
                        if (party.getMembers().size() + 1 != TvT.this.playersPerTeam) {
                            TvT.this.removeObject(party);
                            party.sendMessage((Object)ChatColor.RED + "Your party found a match but you can only have " + TvT.this.playersPerTeam + " players in your party for this ladder.");
                            party2.sendMessage((Object)ChatColor.RED + "Your party found a match but they had more than " + TvT.this.playersPerTeam + " players in their party. You now have priority over other parties in this queue.");
                            return;
                        }
                        if (party2.getMembers().size() + 1 != TvT.this.playersPerTeam) {
                            TvT.this.removeObject(party2);
                            party2.sendMessage((Object)ChatColor.RED + "Your party found a match but you can only have " + TvT.this.playersPerTeam + " players in your party for this ladder.");
                            party.sendMessage((Object)ChatColor.RED + "Your party found a match but they had more than " + TvT.this.playersPerTeam + " players in their party. You now have priority over other parties in this queue.");
                            return;
                        }
                        party = null;
                        party2 = null;
                        Party party3 = (Party)TvT.this.queue.poll();
                        Party party4 = (Party)TvT.this.queue.poll();
                        TvT.this.timer.remove(party3);
                        TvT.this.timer.remove(party4);
                        Practice.getBackend().getDataManager().getData(party3.getLeader()).setQueue(null);
                        Practice.getBackend().getDataManager().getData(party4.getLeader()).setQueue(null);
                        for (Player object222 : party3.getMembers()) {
                            Practice.getBackend().getDataManager().getData(object222).setQueue(null);
                        }
                        for (Player player : party4.getMembers()) {
                            Practice.getBackend().getDataManager().getData(player).setQueue(null);
                        }
                        party3.sendMessage((Object)ChatColor.YELLOW + "You've been matched against the party of " + (Object)ChatColor.RED + party4.getLeader().getName());
                        party4.sendMessage((Object)ChatColor.YELLOW + "You've been matched against the party of " + (Object)ChatColor.RED + party3.getLeader().getName());
                        for (Arena arena : Practice.getBackend().getArenaManager().getArenas().values()) {
                            if (arena.inUse() || !arena.isSetup()) continue;
                            arena.setInUse(true);
                            this.arena = arena;
                            break;
                        }
                        if (this.arena != null) {
                            this.cancel();
                            Match match = new Match(TvT.this, this.arena, gameMode, party3, party4);
                            match.startMatch();
                            MenuManager.updateMenus();
                            this.arena = null;
                            return;
                        }
                        party3.sendMessage(Utils.color(TvT.this.messages.getString("queue.error-unexpected")));
                        party4.sendMessage(Utils.color(TvT.this.messages.getString("queue.error-unexpected")));
                        return;
                    }
                    if (arrobject[0] == null) {
                        TvT.this.queue.remove(arrobject[0]);
                    } else if (arrobject[1] == null) {
                        TvT.this.queue.remove(arrobject[1]);
                    }
                }
            }
        }.runTaskTimer((Plugin)Practice.getInstance(), 0, 5);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public boolean getStatus() {
        return this.status;
    }

    @Override
    public UUID getIdentifier() {
        return this.identifier;
    }

    @Override
    public GameMode getGame() {
        return this.game;
    }

    public List<Match> getMatchList() {
        return this.matches;
    }

    @Override
    public int getAmountInQueue() {
        return this.queue.size();
    }

    @Override
    public int getAmountInMatch() {
        return this.matches.size();
    }

    public void cancelMatchmaker() {
        this.matchmaker.cancel();
    }

    @Override
    public ElapsedTimer getTimer(Object object) {
        return this.timer.get(object);
    }

    @Override
    public void addObject(Object object) {
        Party party = (Party)object;
        if (party.getMembers().size() + 1 < 2 || party.getMembers().size() + 1 < 2) {
            party.sendMessage((Object)ChatColor.RED + "You need atleast 2 players (including leader) in your party to join this ladder.");
            return;
        }
        for (Player player : party.getMembers()) {
            Practice.getBackend().getDataManager().getData(player).setQueue(this);
            player.getInventory().clear();
            player.updateInventory();
        }
        Practice.getBackend().getDataManager().getData(party.getLeader()).setQueue(this);
        party.getLeader().getInventory().setContents(new ItemStack[]{InventoryManager.getQueueLeaveItem()});
        party.getLeader().updateInventory();
        this.queue.offer(party);
        this.timer.put(party, new ElapsedTimer());
        party.sendMessage((Object)ChatColor.YELLOW + "You have joined the 2v2 " + this.game.getName() + " queue.");
    }

    @Override
    public void removeObject(Object object) {
        Party party = (Party)object;
        for (Player player : party.getMembers()) {
            Practice.getBackend().getDataManager().getData(player).setQueue(null);
            Practice.getBackend().getInventoryManager();
            player.getInventory().setContents(InventoryManager.getPartyMemberInventory(player));
            player.updateInventory();
        }
        Practice.getBackend().getDataManager().getData(party.getLeader()).setQueue(null);
        party.getLeader().getInventory().setContents(InventoryManager.getPartyLeaderInventory(party.getLeader()));
        party.getLeader().updateInventory();
        this.queue.remove(party);
        this.timer.remove(party);
    }

    @Override
    public boolean isRanked() {
        return false;
    }

    @EventHandler
    public void onPartyJoin(PartyJoinEvent partyJoinEvent) {
        if (this.queue.contains(partyJoinEvent.getParty())) {
            this.removeObject(partyJoinEvent.getParty());
            partyJoinEvent.getParty().sendMessage((Object)ChatColor.RED + "Your party has been removed from the queue.");
        }
    }

    @EventHandler
    public void onPartyLeave(PartyLeaveEvent partyLeaveEvent) {
        if (this.queue.contains(partyLeaveEvent.getParty())) {
            this.removeObject(partyLeaveEvent.getParty());
            partyLeaveEvent.getParty().sendMessage((Object)ChatColor.RED + "Your party has been removed from the queue.");
        }
    }

    @EventHandler
    public void onPartyKick(PartyKickEvent partyKickEvent) {
        if (this.queue.contains(partyKickEvent.getParty())) {
            this.removeObject(partyKickEvent.getParty());
            partyKickEvent.getParty().sendMessage((Object)ChatColor.RED + "Your party has been removed from the queue.");
        }
    }

    @EventHandler
    public void onPartyDisband(PartyDisbandEvent partyDisbandEvent) {
        if (this.queue.contains(partyDisbandEvent.getParty())) {
            this.removeObject(partyDisbandEvent.getParty());
            partyDisbandEvent.getParty().sendMessage((Object)ChatColor.RED + "Your party has been removed from the queue.");
        }
    }

}

