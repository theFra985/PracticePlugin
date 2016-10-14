/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerQuitEvent
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
import net.propvp.event.PartyJoinEvent;
import net.propvp.file.Config;
import net.propvp.game.Game;
import net.propvp.game.GameMode;
import net.propvp.game.Match;
import net.propvp.game.arena.Arena;
import net.propvp.game.arena.ArenaManager;
import net.propvp.game.ladder.Ladder;
import net.propvp.gui.MenuManager;
import net.propvp.player.DataManager;
import net.propvp.player.InventoryManager;
import net.propvp.player.PlayerData;
import net.propvp.timing.ElapsedTimer;
import net.propvp.util.Elo;
import net.propvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class OvO
implements Ladder,
Listener {
    private String name;
    private UUID identifier;
    private GameMode game;
    private List<Match> matches;
    private LinkedList<Player> queue;
    private Map<Player, Integer> range;
    private Map<Player, ElapsedTimer> timer;
    private BukkitTask matchmaker;
    private boolean ranked;
    private boolean status;
    FileConfiguration messages;

    public OvO(final GameMode gameMode, boolean bl) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Practice.getInstance());
        this.name = "1vs1 " + (bl ? "Ranked" : "Unranked") + " " + gameMode.getName();
        this.game = gameMode;
        this.identifier = UUID.randomUUID();
        this.matches = new ArrayList<Match>();
        this.queue = new LinkedList();
        this.range = new HashMap<Player, Integer>();
        this.timer = new HashMap<Player, ElapsedTimer>();
        this.ranked = bl;
        this.status = true;
        this.messages = Practice.getBackend().getMessagesConfig().getConfig();
        this.matchmaker = new BukkitRunnable(){
            private Arena arena;

            public void run() {
                if (OvO.this.queue.size() >= 2) {
                    Object[] arrobject = OvO.this.queue.toArray();
                    if (arrobject[0] != null && arrobject[1] != null) {
                        Player player = (Player)OvO.this.queue.poll();
                        Player player2 = (Player)OvO.this.queue.poll();
                        OvO.this.range.remove((Object)player);
                        OvO.this.timer.remove((Object)player);
                        OvO.this.range.remove((Object)player2);
                        OvO.this.timer.remove((Object)player2);
                        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
                        PlayerData playerData2 = Practice.getBackend().getDataManager().getData(player2);
                        player.sendMessage(Utils.color(OvO.this.messages.getString("queue.matched").replace("$player", player.getName()).replace("$playerElo", String.valueOf(playerData.getRating(gameMode))).replace("$rival", player2.getName()).replace("$rivalElo", String.valueOf(playerData2.getRating(gameMode).getRating()))));
                        player2.sendMessage(Utils.color(OvO.this.messages.getString("queue.matched").replace("$player", player2.getName()).replace("$playerElo", String.valueOf(playerData2.getRating(gameMode))).replace("$rival", player.getName()).replace("$rivalElo", String.valueOf(playerData.getRating(gameMode).getRating()))));
                        playerData.setQueue(null);
                        playerData2.setQueue(null);
                        for (Arena object2 : Practice.getBackend().getArenaManager().getArenas().values()) {
                            if (object2.inUse() || !object2.isSetup()) continue;
                            object2.setInUse(true);
                            this.arena = object2;
                            break;
                        }
                        if (this.arena != null) {
                            Match match = new Match(OvO.this, this.arena, gameMode, (Object)player, (Object)player2);
                            playerData.setMatch(match);
                            playerData2.setMatch(match);
                            match.startMatch();
                            MenuManager.updateMenus();
                            this.arena = null;
                            return;
                        }
                        player.sendMessage(Utils.color(OvO.this.messages.getString("queue.error-unexpected")));
                        player2.sendMessage(Utils.color(OvO.this.messages.getString("queue.error-unexpected")));
                        return;
                    }
                    if (arrobject[0] == null) {
                        OvO.this.queue.remove(arrobject[0]);
                    } else if (arrobject[1] == null) {
                        OvO.this.queue.remove(arrobject[1]);
                    }
                }
            }
        }.runTaskTimer((Plugin)Practice.getInstance(), 0, 5);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isRanked() {
        return this.ranked;
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

    public int getRange(Player player) {
        return this.range.get((Object)player);
    }

    @Override
    public ElapsedTimer getTimer(Object object) {
        return this.timer.get((Object)((Player)object));
    }

    @Override
    public void addObject(Object object) {
        final Player player = (Player)object;
        final PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (playerData.hasQueue() || playerData.hasMatch() || playerData.isSpectating() || playerData.isEditing() || playerData.hasParty()) {
            player.sendMessage(Utils.color(this.messages.getString("player.busy")));
            return;
        }
        playerData.setQueue(this);
        this.queue.offer(player);
        this.timer.put(player, new ElapsedTimer());
        if (this.ranked) {
            this.range.put(player, 200);
            player.sendMessage(Utils.color(this.messages.getString("queue.joined").replace("$queue", String.valueOf(this.name) + " queue").replace("$playerElo", String.valueOf(playerData.getRating(this.game).getRating()))));
            new BukkitRunnable(){

                public void run() {
                    if (!OvO.this.range.containsKey((Object)player)) {
                        this.cancel();
                        return;
                    }
                    int n = (Integer)OvO.this.range.get((Object)player);
                    if (n >= 500) {
                        OvO.this.removeObject((Object)player);
                        player.sendMessage(Utils.color(OvO.this.messages.getString("queue.error-nomatch")));
                        this.cancel();
                        return;
                    }
                    OvO.this.range.replace(player, n + 50);
                    player.sendMessage(Utils.color(OvO.this.messages.getString("queue.ranges").replace("$ranges", String.valueOf("" + (playerData.getRating(OvO.this.game).getRating() - (Integer)OvO.this.range.get((Object)player)) + " -> " + (playerData.getRating(OvO.this.game).getRating() + (Integer)OvO.this.range.get((Object)player))))));
                }
            }.runTaskTimer((Plugin)Practice.getInstance(), 0, 100);
        } else {
            player.sendMessage(Utils.color(this.messages.getString("queue.joined-unranked").replace("$queue", String.valueOf(this.name) + " queue")));
        }
        Practice.getBackend().getInventoryManager();
        player.getInventory().setContents(new ItemStack[]{InventoryManager.getQueueLeaveItem()});
        player.updateInventory();
    }

    @Override
    public void removeObject(Object object) {
        Player player = (Player)object;
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        playerData.setQueue(null);
        this.range.remove((Object)player);
        this.timer.remove((Object)player);
        this.queue.remove((Object)player);
        player.sendMessage(Utils.color(this.messages.getString("queue.removed")));
        Practice.getBackend().getInventoryManager();
        player.getInventory().setContents(InventoryManager.getSoloInventory(player));
    }

    @EventHandler
    public void onPartyJoin(PartyJoinEvent partyJoinEvent) {
        if (this.queue.contains((Object)partyJoinEvent.getPlayer())) {
            this.removeObject((Object)partyJoinEvent.getPlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent playerQuitEvent) {
        if (this.queue.contains((Object)playerQuitEvent.getPlayer())) {
            this.removeObject((Object)playerQuitEvent.getPlayer());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent playerKickEvent) {
        if (this.queue.contains((Object)playerKickEvent.getPlayer())) {
            this.removeObject((Object)playerKickEvent.getPlayer());
        }
    }

}

