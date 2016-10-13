/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitTask
 */
package it.fastersetup.practice.Manager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.API;
import it.fastersetup.practice.Api.ConfigLoader;
import it.fastersetup.practice.Api.Duel;
import it.fastersetup.practice.Api.GameType;
import it.fastersetup.practice.Api.Invs;
import it.fastersetup.practice.Api.Party;
import it.fastersetup.practice.Api.Rank;
import it.fastersetup.practice.Api.Request;

public class ArenaManager
implements CommandExecutor {
    private HashMap<String, Map.Entry<Location, Location>> arenas = new HashMap<String, Map.Entry<Location, Location>>();
    private ArrayList<String> disabled = new ArrayList<String>();
    private HashMap<String, Duel> use = new HashMap<String, Duel>();
    private ConfigLoader config;
    private Main plugin;
    Player me = Bukkit.getPlayer((String)"StarShadow");

    public ArenaManager(Main pl) {
        this.plugin = pl;
        this.config = new ConfigLoader(pl, "arenas.yml");
        this.config.generateFile();
    }

    public void load() {
        FileConfiguration data = this.config.getConfig();
        Set<String> all = data.getConfigurationSection("").getKeys(false);
        for (String s : all) {
            this.loadArena(s);
        }
    }

    private void loadArena(String name) {
        if (this.config.getConfig().contains(String.valueOf(name) + ".a") && this.config.getConfig().contains(String.valueOf(name) + ".b")) {
            @SuppressWarnings("unused")
			boolean d;
            Location a = API.stringToLoc(this.config.getConfig().getString(String.valueOf(name) + ".a"));
            Location b = API.stringToLoc(this.config.getConfig().getString(String.valueOf(name) + ".b"));
            if (this.config.getConfig().contains(String.valueOf(name) + ".disabled") && (d = this.config.getConfig().getBoolean(String.valueOf(name) + ".disabled"))) {
                this.disabled.add(name);
            }
            this.arenas.put(name, new AbstractMap.SimpleEntry<Location, Location>(a, b));
        }
    }

    public boolean arenaExists(String name) {
        if (this.arenas.containsKey(name) && this.arenas.get(name).getKey() != null && this.arenas.get(name).getValue() != null) {
            return true;
        }
        return false;
    }

    public void saveArenaTop(String a, Location loc) {
        this.config.set("region." + a + ".top", API.locToString(loc));
    }

    public void saveArenaBottom(String a, Location loc) {
        this.config.set("region." + a + ".bottom", API.locToString(loc));
    }

    public boolean bothSet(String a) {
        if (this.config.getConfig().contains("region." + a + ".top") && this.config.getConfig().contains("region." + a + ".bottom")) {
            return true;
        }
        return false;
    }

    public Location getArenaTop(String a) {
        if (this.config.getConfig().contains("region." + a + ".top")) {
            return API.stringToLoc(this.config.getConfig().getString("region." + a + ".top"));
        }
        return null;
    }

    public Location getArenaBottom(String a) {
        if (this.config.getConfig().contains("region." + a + ".bottom")) {
            return API.stringToLoc(this.config.getConfig().getString("region." + a + ".bottom"));
        }
        return null;
    }

    public String getAvailableArena() {
        String[] ar = this.allArenas();
        if (ar.length > 0) {
            return ar[new Random().nextInt(ar.length)];
        }
        return null;
    }

    public String[] allArenas() {
        ArrayList<String> temp = new ArrayList<String>();
        for (String s : this.arenas.keySet()) {
            if (this.use.containsKey(s) || this.disabled.contains(s) || !this.spawnsSet(s)) continue;
            temp.add(s);
        }
        String[] f = new String[temp.size()];
        int i = 0;
        while (i < temp.size()) {
            f[i] = (String)temp.get(i);
            ++i;
        }
        return f;
    }

    private boolean spawnsSet(String a) {
        if (this.arenas.containsKey(a) && this.arenas.get(a).getKey() != null && this.arenas.get(a).getValue() != null) {
            return true;
        }
        return false;
    }

    public HashMap<String, Map.Entry<Location, Location>> getArenas() {
        HashMap<String, Map.Entry<Location, Location>> map = new HashMap<String, Map.Entry<Location, Location>>();
        for (Map.Entry<String, Map.Entry<Location, Location>> entry : this.arenas.entrySet()) {
            if (!this.arenaExists(entry.getKey())) continue;
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public String duel(Request r) {
        if (r.getGameType() == GameType.UNKNOWN) {
            return null;
        }
        String a = this.getAvailableArena();
        this.plugin.pman.stopReq(r.getSender());
        this.plugin.pman.stopReq(r.getReceiver());
        if (r.isParty()) {
            if (this.plugin.pman.openParty(r.getSender())) {
                this.plugin.pman.setOpen(r.getSender(), false);
            }
            if (this.plugin.pman.openParty(r.getReceiver())) {
                this.plugin.pman.setOpen(r.getReceiver(), false);
            }
            Party pt1 = this.plugin.pman.getParty(this.plugin.pman.getLeader(r.getSender()));
            Party pt2 = this.plugin.pman.getParty(this.plugin.pman.getLeader(r.getReceiver()));
            if (a != null) {
                ArrayList<UUID> p1 = pt1.getAll();
                ArrayList<UUID> p2 = pt2.getAll();
                for (UUID id2 : p1) {
                    this.plugin.lobby.remove(id2);
                    this.plugin.board.remove(id2);
                    for (UUID oid : p2) {
                        Bukkit.getPlayer((UUID)id2).showPlayer(Bukkit.getPlayer((UUID)oid));
                    }
                }
                for (UUID id2 : p2) {
                    this.plugin.lobby.remove(id2);
                    this.plugin.board.remove(id2);
                    for (UUID oid : p1) {
                        Bukkit.getPlayer((UUID)id2).showPlayer(Bukkit.getPlayer((UUID)oid));
                    }
                }
                Duel d = new Duel(this.getA(a), this.getB(a), r.getGameType());
                d.addA(p1);
                d.addB(p2);
                d.setFFA(r.isFFA());
                d.setParty(true);
                this.use.put(a, d);
                if (r.isFFA()) {
                    this.plugin.grace.addCooldown(d.getAllPlayers(), 100, "\u00a7eGrace period has ended.");
                    d.msgAll("\u00a7e5 second grace period has started.");
                } else {
                    new it.fastersetup.practice.TeamColors(d.getAPlayers(), d.getBPlayers());
                }
                d.tpPlayers();
                d.setFoodHealth();
                d.applyKit();
                return a;
            }
            Bukkit.getPlayer((UUID)r.getSender()).sendMessage("\u00a7eNo arenas available for duel.");
            Bukkit.getPlayer((UUID)r.getReceiver()).sendMessage("\u00a7eNo arenas available for duel.");
        } else {
            if (a != null) {
                this.plugin.lobby.remove(r.getSender());
                this.plugin.lobby.remove(r.getReceiver());
                this.plugin.board.remove(r.getSender());
                this.plugin.board.remove(r.getReceiver());
                Bukkit.getPlayer((UUID)r.getSender()).showPlayer(Bukkit.getPlayer((UUID)r.getReceiver()));
                Bukkit.getPlayer((UUID)r.getReceiver()).showPlayer(Bukkit.getPlayer((UUID)r.getSender()));
                Duel d = new Duel(this.getA(a), this.getB(a), r.getGameType());
                d.addA(r.getSender());
                d.addB(r.getReceiver());
                d.setRanked(r.isRanked());
                this.use.put(a, d);
                d.tpPlayers();
                d.setFoodHealth();
                d.applyKit();
                if (r.isRanked()) {
                    Player se = Bukkit.getPlayer((UUID)r.getSender());
                    Player re = Bukkit.getPlayer((UUID)r.getReceiver());
                    se.sendMessage("\u00a7aYou are now in a ranked match against \u00a7b" + re.getName() + "\u00a7a. \u00a7cELO: \u00a7d" + this.plugin.elo.get(re.getUniqueId(), r.getGameType()));
                    re.sendMessage("\u00a7aYou are now in a ranked match against \u00a7b" + se.getName() + "\u00a7a. \u00a7cELO: \u00a7d" + this.plugin.elo.get(se.getUniqueId(), r.getGameType()));
                }
                return a;
            }
            Bukkit.getPlayer((UUID)r.getSender()).sendMessage("\u00a7eNo arenas available for duel.");
            Bukkit.getPlayer((UUID)r.getReceiver()).sendMessage("\u00a7eNo arenas available for duel.");
        }
        return null;
    }

    public Location getA(String name) {
        if (this.arenas.containsKey(name)) {
            return this.arenas.get(name).getKey();
        }
        return null;
    }

    public Location getB(String name) {
        if (this.arenas.containsKey(name)) {
            return this.arenas.get(name).getValue();
        }
        return null;
    }

    public ArrayList<UUID> getPlayers(String name) {
        if (this.use.containsKey(name)) {
            return this.use.get(name).getAllPlayers();
        }
        return null;
    }

    public String getArena(UUID id) {
        for (String a : this.use.keySet()) {
            if (!this.use.get(a).contains(id)) continue;
            return a;
        }
        return null;
    }

    public boolean isFFA(UUID id) {
        if (this.getArena(id) != null) {
            return this.use.get(this.getArena(id)).isFFA();
        }
        return false;
    }

    public boolean isInArena(UUID id) {
        for (String a : this.use.keySet()) {
            if (!this.use.get(a).contains(id)) continue;
            return true;
        }
        return false;
    }

    public int num(String name) {
        if (this.use.containsKey(name)) {
            return this.use.get(name).amount();
        }
        return 0;
    }

    public boolean isSpectating(UUID id) {
        if (this.getArena(id) != null && this.use.get(this.getArena(id)).isSpectating(id)) {
            return true;
        }
        return false;
    }

    public void updateVisible(UUID id) {
        String a = this.getArena(id);
        this.msg("uuid: " + id.toString());
        Player de = Bukkit.getPlayer((UUID)id);
        this.msg("player null: " + (de == null));
        this.msg("arena: " + a);
        if (a != null) {
            Duel d = this.use.get(a);
            for (UUID uid : d.getAllPlayers()) {
                Player p = Bukkit.getPlayer((UUID)uid);
                if (p != null) {
                    this.msg("updating for " + p.getName() + " (" + uid.toString() + ")");
                    for (UUID uuid : d.getSpectators()) {
                        Player pl = Bukkit.getPlayer((UUID)uuid);
                        if (pl != null && p.getUniqueId() != pl.getUniqueId()) {
                            this.msg("hiding player " + pl.getName() + " for " + p.getName());
                            p.hidePlayer(pl);
                            this.msg("player hidden");
                            continue;
                        }
                        this.msg("spec null or UUID equal");
                    }
                    continue;
                }
                this.msg("null player");
            }
        }
    }

    public void debug(Player p) {
        String a = this.getArena(p.getUniqueId());
        if (a != null) {
            Duel d = this.use.get(a);
            for (UUID uid2 : d.getAllPlayers()) {
                p.sendMessage(uid2.toString());
            }
            p.sendMessage("-");
            for (UUID uid2 : d.getSpectators()) {
                p.sendMessage(uid2.toString());
                Player o = Bukkit.getPlayer((UUID)uid2);
                if (o != null) continue;
                p.sendMessage("null ^");
            }
        } else {
            p.sendMessage("not in arena");
        }
    }

    public GameType getGameType(UUID id) {
        String a = this.getArena(id);
        if (a != null) {
            return this.use.get(a).getGameType();
        }
        return GameType.UNKNOWN;
    }

    public void die(UUID id, Player kill, boolean death) {
        if (this.getArena(id) != null) {
            String a = this.getArena(id);
            Duel d = this.use.get(a);
            if (death) {
                if (kill == null) {
                    kill = Bukkit.getPlayer((UUID)d.opp(id));
                }
                Bukkit.getPlayer((UUID)id).getWorld().playSound(Bukkit.getPlayer((UUID)id).getLocation(), Sound.ITEM_BREAK, 1.0f, 2.0f);
                if (this.shouldEnd(a, id)) {
                    this.end(a, kill.getUniqueId(), true, d.isParty(), d.isFFA());
                } else {
                    if (d.isParty()) {
                        Damageable h = kill;
                        double wh = (double)Math.round(h.getHealth() / h.getMaxHealth()) * kill.getHealthScale() / 2.0;
                        if (wh < 0.5) {
                            wh += 0.5;
                        }
                        Player die = Bukkit.getPlayer((UUID)id);
                        d.msgTeam(id, "\u00a7a" + die.getName() + " \u00a7bhas been killed by \u00a7c" + kill.getName() + " \u00a7bwith \u00a7e" + wh + " \u00a7bhearts.");
                        d.msgTeam(kill.getUniqueId(), "\u00a7c" + die.getName() + " \u00a7bhas been killed by \u00a7a" + kill.getName() + " \u00a7bwith \u00a7e" + wh + " \u00a7bhearts.");
                    }
                    this.spectate(id);
                }
            }
        }
    }

    public void leave(UUID id) {
        if (this.getArena(id) != null) {
            String a = this.getArena(id);
            Duel d = this.use.get(a);
            if (d.isSpectating(id)) {
                d.remove(id);
                this.use.put(a, d);
                this.reset(id);
                return;
            }
            if (this.shouldEnd(a, id)) {
                this.end(a, d.opp(id), false, d.isParty(), d.isFFA());
            } else {
                this.plugin.pman.eject(id);
                d.remove(id);
                this.use.put(a, d);
                this.reset(id);
                d.msgAll("\u00a7b" + Bukkit.getPlayer((UUID)id).getName() + " \u00a7ehas left the arena.");
                Bukkit.getPlayer((UUID)id).sendMessage("\u00a7eYou have left the arena.");
            }
        }
    }

    public boolean shouldEnd(String a, UUID id) {
        if (this.use.containsKey(a)) {
            Duel d = this.use.get(a);
            if (!d.isParty()) {
                return true;
            }
            if (d.isFFA()) {
                if (d.amount() - 1 == 1) {
                    return true;
                }
                return false;
            }
            if (d.teamsLeft(id) == 1) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void end(final String a, UUID id, boolean death, boolean party, boolean ffa) {
        if (this.use.containsKey(a)) {
            final Duel d = this.use.get(a);
            if (!party) {
                Player winner = Bukkit.getPlayer((UUID)id);
                Player loser = Bukkit.getPlayer((UUID)d.opp(id));
                Damageable h = winner;
                double wh = (double)Math.round(h.getHealth() / h.getMaxHealth() * winner.getHealthScale()) / 2.0;
                winner.sendMessage("\u00a7aHai vinto contro \u00a7b" + loser.getName() + " \u00a7acon \u00a7e" + wh + " \u00a7aCuori.");
                loser.sendMessage("\u00a7aSei stato Sconfitto da \u00a7b" + winner.getName() + " \u00a7acon \u00a7e" + wh + " \u00a7aCuori.");
                if (d.isRanked()) {
                    int welo = this.plugin.elo.get(winner.getUniqueId(), d.getGameType());
                    int lelo = this.plugin.elo.get(loser.getUniqueId(), d.getGameType());
                    int f = this.plugin.elo.calc(welo, lelo);
                    this.plugin.elo.add(winner.getUniqueId(), f, d.getGameType());
                    this.plugin.elo.remove(loser.getUniqueId(), f, d.getGameType());
                    if (this.plugin.ranks.getRank(id) == Rank.DEFAULT) {
                        this.plugin.match.remove(id, 1);
                    }
                    if (death) {
                        this.plugin.coins.add(winner.getUniqueId(), 10);
                        winner.sendMessage("\u00a7a\u00a7l+10 Coins");
                        this.plugin.coins.add(id, 5);
                        loser.sendMessage("\u00a7a\u00a7l+5 Coins");
                    }
                    winner.sendMessage("\u00a7bAvevi\u00a7a" + welo + " \u00a7bELO. Ora hai \u00a7a" + (welo + f) + "\u00a7b.");
                    loser.sendMessage("\u00a7bAvevi \u00a7a" + lelo + " \u00a7bELO. Ora hai \u00a7a" + (lelo - f) + "\u00a7b.");
                    if (f >= 0) {
                        winner.sendMessage("\u00a7a\u00a7l+ " + f + " ELO");
                        loser.sendMessage("\u00a7c\u00a7l- " + f + " ELO");
                    } else {
                        winner.sendMessage("\u00a7c\u00a7l- " + f + " ELO");
                        loser.sendMessage("\u00a7a\u00a7l+ " + f + " ELO");
                    }
                    this.plugin.board.show(winner);
                    this.plugin.board.show(loser);
                } else {
                    if (death) {
                        this.plugin.coins.add(winner.getUniqueId(), 2);
                        winner.sendMessage("\u00a7a\u00a7l+2 Coins");
                        this.plugin.coins.add(id, 1);
                        loser.sendMessage("\u00a7a\u00a7l+1 Coins");
                    }
                    this.plugin.board.show(winner);
                    this.plugin.board.show(loser);
                }
                d.removeItems();
                for (UUID u : d.getAllPlayers()) {
                    if (d.isSpectating(u)) continue;
                    this.spectate(u);
                }
                Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, new Runnable(){

                    @Override
                    public void run() {
                        for (UUID current : d.getAllPlayers()) {
                            ArenaManager.this.reset(current);
                        }
                        ArenaManager.this.use.remove(a);
                        ArenaManager.access$2((ArenaManager)ArenaManager.this).mm.match();
                        ArenaManager.access$2((ArenaManager)ArenaManager.this).visible.update();
                    }
                }, 60);
            } else if (ffa) {
                if (id != null) {
                    d.msgAll("\u00a7b" + Bukkit.getPlayer((UUID)id).getName() + " \u00a7eha vinto.");
                } else {
                    d.msgAll("\u00a7b" + Bukkit.getPlayer((UUID)d.left()).getName() + " \u00a7eha vinto.");
                }
                d.removeItems();
                for (UUID u : d.getAllPlayers()) {
                    if (d.isSpectating(u)) continue;
                    this.spectate(u);
                }
                this.plugin.pman.combine(a);
                Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, new Runnable(){

                    @Override
                    public void run() {
                        for (UUID current : d.getAllPlayers()) {
                            ArenaManager.this.reset(current);
                        }
                        ArenaManager.this.use.remove(a);
                        ArenaManager.access$2((ArenaManager)ArenaManager.this).mm.match();
                        ArenaManager.access$2((ArenaManager)ArenaManager.this).visible.update();
                    }
                }, 60);
            } else {
                UUID lead = this.plugin.pman.getLeader(id);
                Party w = this.plugin.pman.getParty(lead);
                d.msgAll("\u00a7b" + Bukkit.getPlayer((UUID)w.getLeader()).getName() + "'s \u00a7eteam ha winto.");
                d.removeItems();
                for (UUID u : d.getAllPlayers()) {
                    if (d.isSpectating(u)) continue;
                    this.spectate(u);
                }
                this.plugin.pman.combine(a);
                Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, new Runnable(){

                    @Override
                    public void run() {
                        for (UUID current : d.getAllPlayers()) {
                            ArenaManager.this.reset(current);
                        }
                        ArenaManager.this.use.remove(a);
                        ArenaManager.access$2((ArenaManager)ArenaManager.this).mm.match();
                        ArenaManager.access$2((ArenaManager)ArenaManager.this).visible.update();
                    }
                }, 60);
            }
        }
    }

    public void msg(String m) {
        if (this.me != null) {
            this.me.sendMessage(m);
        }
    }

    public void spectate(UUID join, UUID ingame) {
        if (this.getArena(ingame) != null) {
            this.plugin.lobby.remove(join);
            this.plugin.board.remove(join);
            String a = this.getArena(ingame);
            Duel d = this.use.get(a);
            d.remove(join);
            d.addS(join);
            this.use.put(a, d);
            API.clearInv(join);
            Player pl = Bukkit.getPlayer((UUID)join);
            pl.teleport(d.getSpawnA());
            this.msg("updating visibility");
            Player p = Bukkit.getPlayer((UUID)ingame);
            if (p == null) {
                this.msg("ingame player is null");
            }
            this.updateVisible(ingame);
            pl.setFoodLevel(20);
            pl.setHealth(20.0);
            pl.setAllowFlight(true);
            pl.setFlying(true);
            API.applyPotion(join, PotionEffectType.BLINDNESS, 40, 5);
            pl.sendMessage("\u00a7eYou have been put into spectator mode.");
        }
    }

    public void spectate(UUID id) {
        if (this.getArena(id) != null) {
            String a = this.getArena(id);
            Duel d = this.use.get(a);
            d.remove(id);
            d.addS(id);
            this.use.put(a, d);
            API.clearInv(id);
            Player pl = Bukkit.getPlayer((UUID)id);
            this.updateVisible(id);
            pl.setFoodLevel(20);
            pl.setHealth(20.0);
            pl.setAllowFlight(true);
            pl.setFlying(true);
            API.applyPotion(id, PotionEffectType.BLINDNESS, 40, 5);
        }
    }

    public void itemDrop(UUID id, Item item) {
        if (this.getArena(id) != null) {
            String a = this.getArena(id);
            Duel d = this.use.get(a);
            d.addItem(item);
            this.use.put(a, d);
        }
    }

    public void block(UUID id, Location loc) {
        if (this.getArena(id) != null) {
            String a = this.getArena(id);
            Duel d = this.use.get(a);
            d.addBlock(loc);
            this.use.put(a, d);
        }
    }

    private void reset(UUID id) {
        Player p = Bukkit.getPlayer((UUID)id);
        p.setFlying(false);
        if (p.getGameMode() != GameMode.CREATIVE) {
            p.setAllowFlight(false);
        }
        this.plugin.lobby.add(id);
        this.plugin.lobby.tp(p);
        if (!this.plugin.pman.inParty(id)) {
            this.plugin.board.show(id);
            p.getInventory().setContents(Invs.main(p.getName()));
        } else {
            this.plugin.pman.showBoard(id);
            p.getInventory().setContents(Invs.party(this.plugin.pman.twoPlayer(id), this.plugin.pman.openParty(id)));
        }
        API.clearArmor(p);
        API.removeEffects(id);
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.setSaturation(20.0f);
        p.setFireTicks(0);
    }

    public void end(String name) {
        if (this.use.containsKey(name)) {
            Duel d = this.use.get(name);
            d.msgAll("\u00a7eArena has been disabled.");
            for (UUID id : d.getAllPlayers()) {
                this.reset(id);
            }
            this.use.remove(name);
        }
    }

    public void endGame(String name) {
        if (this.use.containsKey(name)) {
            Duel d = this.use.get(name);
            d.msgAll("\u00a7eGame has ended.");
            for (UUID id : d.getAllPlayers()) {
                this.reset(id);
            }
            this.use.remove(name);
        }
    }

    private void create(String name) {
        this.config.set(String.valueOf(name) + ".a", "null");
        this.config.set(String.valueOf(name) + ".b", "null");
        this.config.set(String.valueOf(name) + ".disabled", false);
    }

    private void remove(String name) {
        if (this.config.getConfig().contains(name)) {
            this.config.removeSection(name);
        }
    }

    private void setA(String name, Location loc) {
        this.config.set(String.valueOf(name) + ".a", API.locToString(loc));
    }

    private void setB(String name, Location loc) {
        this.config.set(String.valueOf(name) + ".b", API.locToString(loc));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.isOp()) {
                player.sendMessage("\u00a7c\u00a7lYou don't have permission.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage("\u00a7eNo arguments.");
            } else if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1) {
                    player.sendMessage("\u00a7b/arena create <name>");
                } else if (args.length == 2) {
                    if (!this.arenas.containsKey(args[1])) {
                        this.arenas.put(args[1], new AbstractMap.SimpleEntry<Location, Location>(null, null));
                        this.create(args[1]);
                        player.sendMessage("\u00a7aArena \u00a7b" + args[1] + " \u00a7acreated.");
                    } else {
                        player.sendMessage("\u00a7eArena already exists.");
                    }
                } else {
                    player.sendMessage("\u00a7b/arena create <name>");
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 1) {
                    player.sendMessage("\u00a7b/arena remove <name>");
                } else if (args.length == 2) {
                    if (this.arenas.containsKey(args[1])) {
                        if (this.disabled.contains(args[1])) {
                            this.arenas.remove(args[1]);
                            this.disabled.remove(args[1]);
                            this.remove(args[1]);
                            player.sendMessage("\u00a7aArena \u00a7b" + args[1] + " \u00a7aremoved.");
                        } else {
                            player.sendMessage("\u00a7eArena must be disabled first.");
                        }
                    } else {
                        player.sendMessage("\u00a7eArena doesn't exist.");
                    }
                } else {
                    player.sendMessage("\u00a7b/arena remove <name>");
                }
            } else if (args[0].equalsIgnoreCase("disable")) {
                if (args.length == 1) {
                    player.sendMessage("\u00a7b/arena disable <name>");
                } else if (args.length == 2) {
                    if (this.arenas.containsKey(args[1])) {
                        if (!this.disabled.contains(args[1])) {
                            if (this.use.containsKey(args[1])) {
                                this.end(args[1]);
                            }
                            this.disabled.add(args[1]);
                            player.sendMessage("\u00a7aArena \u00a7b" + args[1] + " \u00a7adisabled.");
                        } else {
                            player.sendMessage("\u00a7eArena is already disabled.");
                        }
                    } else {
                        player.sendMessage("\u00a7eArena doesn't exist.");
                    }
                } else {
                    player.sendMessage("\u00a7b/arena disable <name>");
                }
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (args.length == 1) {
                    player.sendMessage("\u00a7b/arena enable <name>");
                } else if (args.length == 2) {
                    if (this.arenas.containsKey(args[1])) {
                        if (this.disabled.contains(args[1])) {
                            this.disabled.remove(args[1]);
                            player.sendMessage("\u00a7aArena \u00a7b" + args[1] + " \u00a7aenabled.");
                        } else {
                            player.sendMessage("\u00a7eArena is already enabled.");
                        }
                    } else {
                        player.sendMessage("\u00a7eArena doesn't exist.");
                    }
                } else {
                    player.sendMessage("\u00a7b/arena enable <name>");
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (args.length <= 2) {
                    player.sendMessage("\u00a7b/arena set <name> a/b");
                } else if (args.length == 3) {
                    if (this.arenas.containsKey(args[1])) {
                        if (args[2].equalsIgnoreCase("a")) {
                            Location loc = player.getLocation();
                            this.arenas.put(args[1], new AbstractMap.SimpleEntry<Location, Location>(loc, this.arenas.get(args[1]).getValue()));
                            this.setA(args[1], loc);
                            player.sendMessage("\u00a7aSet location a for \u00a7b" + args[1]);
                        } else if (args[2].equalsIgnoreCase("b")) {
                            Location loc = player.getLocation();
                            this.arenas.put(args[1], new AbstractMap.SimpleEntry<Location, Location>(this.arenas.get(args[1]).getKey(), loc));
                            this.setB(args[1], loc);
                            player.sendMessage("\u00a7aSet location b for \u00a7b" + args[1]);
                        } else {
                            player.sendMessage("\u00a7eSet location a or b. \u00a7b/arenas set <name> a/b");
                        }
                    } else {
                        player.sendMessage("\u00a7eArena doesn't exist");
                    }
                } else {
                    player.sendMessage("\u00a7b/arena set <name> a/b");
                }
            } else if (args[0].equalsIgnoreCase("closest")) {
                if (this.arenas.size() > 0) {
                    Location loc = player.getLocation();
                    double d = 0.0;
                    String a = null;
                    for (Map.Entry<String, Map.Entry<Location, Location>> entry : this.arenas.entrySet()) {
                        if (!this.spawnsSet(entry.getKey()) || !entry.getValue().getKey().getWorld().getName().equals(loc.getWorld().getName())) continue;
                        if (d == 0.0) {
                            d = loc.distance(entry.getValue().getKey());
                            a = entry.getKey();
                        }
                        if (loc.distance(entry.getValue().getKey()) < d) {
                            d = loc.distance(entry.getValue().getKey());
                            a = entry.getKey();
                        }
                        if (loc.distance(entry.getValue().getValue()) >= d) continue;
                        d = loc.distance(entry.getValue().getValue());
                        a = entry.getKey();
                    }
                    player.sendMessage("\u00a7aYou are closest to \u00a7b" + a);
                } else {
                    player.sendMessage("\u00a7eThere are no arenas.");
                }
            } else {
                player.sendMessage("\u00a7eUnknown argument.");
            }
        } else {
            sender.sendMessage("That is player only.");
        }
        return true;
    }

    public boolean sameWorld(String arena, Location loc, boolean a) {
        if (this.arenas.containsKey(this.arenas)) {
            if (a) {
                return this.arenas.get(arena).getKey().getWorld().getName().equals(loc.getWorld().getName());
            }
            return this.arenas.get(arena).getValue().getWorld().getName().equals(loc.getWorld().getName());
        }
        return false;
    }

    static /* synthetic */ Main access$2(ArenaManager arenaManager) {
        return arenaManager.plugin;
    }

}

