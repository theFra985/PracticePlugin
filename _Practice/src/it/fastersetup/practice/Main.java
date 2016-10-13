package it.fastersetup.practice;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import it.fastersetup.practice.AdminPass;
import it.fastersetup.practice.Coins;
import it.fastersetup.practice.Cooldown;
import it.fastersetup.practice.DuelRequests;
import it.fastersetup.practice.ELO;
import it.fastersetup.practice.Events;
import it.fastersetup.practice.LiveUpdate;
import it.fastersetup.practice.Lobby;
import it.fastersetup.practice.MainBoard;
import it.fastersetup.practice.MatchMaker;
import it.fastersetup.practice.Matches;
import it.fastersetup.practice.Ranks;
import it.fastersetup.practice.TempHold;
import it.fastersetup.practice.UserData;
import it.fastersetup.practice.VisibleManager;
import it.fastersetup.practice.Api.API;
import it.fastersetup.practice.Api.ConfigLoader;
import it.fastersetup.practice.Api.EPerm;
import it.fastersetup.practice.CMD.CMDAccept;
import it.fastersetup.practice.CMD.CMDBan;
import it.fastersetup.practice.CMD.CMDCoins;
import it.fastersetup.practice.CMD.CMDDebug;
import it.fastersetup.practice.CMD.CMDDeny;
import it.fastersetup.practice.CMD.CMDDuel;
import it.fastersetup.practice.CMD.CMDIgnore;
import it.fastersetup.practice.CMD.CMDKick;
import it.fastersetup.practice.CMD.CMDMatches;
import it.fastersetup.practice.CMD.CMDMsg;
import it.fastersetup.practice.CMD.CMDMute;
import it.fastersetup.practice.CMD.CMDPC;
import it.fastersetup.practice.CMD.CMDParty;
import it.fastersetup.practice.CMD.CMDReply;
import it.fastersetup.practice.CMD.CMDSettings;
import it.fastersetup.practice.CMD.CMDSpawn;
import it.fastersetup.practice.CMD.CMDSpectate;
import it.fastersetup.practice.CMD.CMDSpy;
import it.fastersetup.practice.CMD.CMDTP;
import it.fastersetup.practice.CMD.CMDVersion;
import it.fastersetup.practice.Manager.ArenaManager;
import it.fastersetup.practice.Manager.DayManager;
import it.fastersetup.practice.Manager.NameManager;
import it.fastersetup.practice.Manager.PartyManager;

public class Main
extends JavaPlugin {
    private ConfigLoader config;
    public Lobby lobby;
    public ELO elo;
    public AdminPass over;
    public Matches match;
    public Coins coins;
    public PartyManager pman;
    public LiveUpdate live;
    public ArenaManager arenas;
    public DuelRequests duels;
    public MainBoard board;
    public MatchMaker mm;
    public Ranks ranks;
    public Permissions perm;
    public UserData data;
    public DayManager day;
    public CMDMatches gift;
    public NameManager names;
    public CMDBan bans;
    public CMDMute mutes;
    public CMDMsg msg;
    public CMDReply reply;
    public TempHold temp;
    public Cooldown cooldown;
    public Cooldown ender;
    public Cooldown grace;
    public CMDSpy spy;
    public CMDIgnore ignore;
    public EPerm patt;
    public VisibleManager visible;

    public void onEnable() {
        this.config = new ConfigLoader(this, "config.yml");
        this.config.generateFile();
        this.patt = new EPerm(this);
        this.permissions();
        this.ranks = new Ranks(this);
        this.ranks.load();
        this.lobby = new Lobby(this);
        this.lobby.load();
        this.over = new AdminPass();
        this.elo = new ELO(this);
        this.elo.load();
        this.match = new Matches(this);
        this.match.load();
        this.coins = new Coins(this);
        this.coins.load();
        this.pman = new PartyManager(this);
        this.live = new LiveUpdate(this);
        this.arenas = new ArenaManager(this);
        this.arenas.load();
        this.duels = new DuelRequests(this);
        this.board = new MainBoard(this);
        this.mm = new MatchMaker(this);
        this.perm = new Permissions(this);
        this.data = new UserData(this);
        this.day = new DayManager(this);
        this.gift = new CMDMatches(this);
        this.names = new NameManager(this);
        this.bans = new CMDBan(this);
        this.mutes = new CMDMute(this);
        this.msg = new CMDMsg(this);
        this.reply = new CMDReply(this);
        this.temp = new TempHold();
        this.cooldown = new Cooldown(this);
        this.ender = new Cooldown(this);
        this.grace = new Cooldown(this);
        this.spy = new CMDSpy();
        this.ignore = new CMDIgnore(this);
        this.visible = new VisibleManager(this);
        Events events = new Events(this);
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents((Listener)events, (Plugin)this);
        API.resetAll();
        if (this.lobby.isSet()) {
            API.lobbyAll(this.lobby.getLoc());
        }
        @SuppressWarnings("deprecation")
		Player[] arrplayer = this.getServer().getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            this.board.show(p);
            this.perm.check(p.getUniqueId());
            ++n2;
        }
        this.getCommand("debug").setExecutor((CommandExecutor)new CMDDebug(this));
        this.getCommand("settings").setExecutor((CommandExecutor)new CMDSettings(this));
        this.getCommand("party").setExecutor((CommandExecutor)new CMDParty(this));
        this.getCommand("pc").setExecutor((CommandExecutor)new CMDPC(this));
        this.getCommand("arena").setExecutor((CommandExecutor)this.arenas);
        this.getCommand("duel").setExecutor((CommandExecutor)new CMDDuel(this));
        this.getCommand("accept").setExecutor((CommandExecutor)new CMDAccept(this));
        this.getCommand("deny").setExecutor((CommandExecutor)new CMDDeny(this));
        this.getCommand("spawn").setExecutor((CommandExecutor)new CMDSpawn(this));
        this.getCommand("spectate").setExecutor((CommandExecutor)new CMDSpectate(this));
        this.getCommand("ranks").setExecutor((CommandExecutor)this.ranks);
        this.getCommand("matches").setExecutor((CommandExecutor)this.gift);
        this.getCommand("ban").setExecutor((CommandExecutor)this.bans);
        this.getCommand("banip").setExecutor((CommandExecutor)this.bans);
        this.getCommand("tempban").setExecutor((CommandExecutor)this.bans);
        this.getCommand("unban").setExecutor((CommandExecutor)this.bans);
        this.getCommand("unbanip").setExecutor((CommandExecutor)this.bans);
        this.getCommand("mute").setExecutor((CommandExecutor)this.mutes);
        this.getCommand("tempmute").setExecutor((CommandExecutor)this.mutes);
        this.getCommand("unmute").setExecutor((CommandExecutor)this.mutes);
        this.getCommand("kick").setExecutor((CommandExecutor)new CMDKick());
        this.getCommand("msg").setExecutor((CommandExecutor)this.msg);
        this.getCommand("r").setExecutor((CommandExecutor)this.reply);
        this.getCommand("spy").setExecutor((CommandExecutor)this.spy);
        this.getCommand("ignore").setExecutor((CommandExecutor)this.ignore);
        this.getCommand("unignore").setExecutor((CommandExecutor)this.ignore);
        this.getCommand("tp").setExecutor((CommandExecutor)new CMDTP());
        this.getCommand("pvpv").setExecutor((CommandExecutor)new CMDVersion(this));
        this.getCommand("coins").setExecutor((CommandExecutor)new CMDCoins(this));
    }

    public void onDisable() {
        this.remPerms();
        this.lobby.save();
        this.elo.save();
        this.match.save();
        this.coins.save();
        this.ranks.save();
        this.patt.clear();
        this.config = null;
        this.lobby = null;
        this.elo = null;
        this.over = null;
        this.match = null;
        this.coins = null;
        this.pman = null;
        this.live = null;
        this.arenas = null;
        this.duels = null;
        this.board = null;
        this.mm = null;
        this.ranks = null;
        this.perm = null;
        this.data = null;
        this.day = null;
        this.gift = null;
        this.names = null;
        this.bans = null;
        this.mutes = null;
        this.msg = null;
        this.reply = null;
        this.temp = null;
        this.cooldown = null;
        this.ender = null;
        this.grace = null;
        this.spy = null;
        this.patt = null;
        this.visible = null;
    }

    public FileConfiguration getMainConfig() {
        return this.config.getConfig();
    }

    public ConfigLoader getConfigLoader() {
        return this.config;
    }

    public void saveMainConfig(FileConfiguration data) {
        this.config.saveConfig(data);
    }

    public File getPluginFile() {
        return this.getFile();
    }

    private void remPerms() {
        Permission main = new Permission("pvpcoin.*");
        Permission cmd = new Permission("pvpcoin.cmd.*");
        Permission ban = new Permission("pvpcoin.cmd.ban");
        Permission banip = new Permission("pvpcoin.cmd.banip");
        Permission tempban = new Permission("pvpcoin.cmd.tempban");
        Permission unban = new Permission("pvpcoin.cmd.unban");
        Permission unbanip = new Permission("pvpcoin.cmd.unbanip");
        Permission mute = new Permission("pvpcoin.cmd.mute");
        Permission tempmute = new Permission("pvpcoin.cmd.tempmute");
        Permission unmute = new Permission("pvpcoin.cmd.unmute");
        Permission kick = new Permission("pvpcoin.cmd.kick");
        Permission tp = new Permission("pvpcoin.cmd.tp");
        Bukkit.getPluginManager().removePermission(main);
        Bukkit.getPluginManager().removePermission(cmd);
        Bukkit.getPluginManager().removePermission(ban);
        Bukkit.getPluginManager().removePermission(banip);
        Bukkit.getPluginManager().removePermission(tempban);
        Bukkit.getPluginManager().removePermission(unban);
        Bukkit.getPluginManager().removePermission(unbanip);
        Bukkit.getPluginManager().removePermission(mute);
        Bukkit.getPluginManager().removePermission(tempmute);
        Bukkit.getPluginManager().removePermission(unmute);
        Bukkit.getPluginManager().removePermission(kick);
        Bukkit.getPluginManager().removePermission(tp);
    }

    private void permissions() {
        Permission main = new Permission("pvpcoin.*");
        Permission cmd = new Permission("pvpcoin.cmd.*");
        cmd.addParent(main, true);
        Permission ban = new Permission("pvpcoin.cmd.ban");
        Permission banip = new Permission("pvpcoin.cmd.banip");
        Permission tempban = new Permission("pvpcoin.cmd.tempban");
        Permission unban = new Permission("pvpcoin.cmd.unban");
        Permission unbanip = new Permission("pvpcoin.cmd.unbanip");
        Permission mute = new Permission("pvpcoin.cmd.mute");
        Permission tempmute = new Permission("pvpcoin.cmd.tempmute");
        Permission unmute = new Permission("pvpcoin.cmd.unmute");
        Permission kick = new Permission("pvpcoin.cmd.kick");
        Permission tp = new Permission("pvpcion.cmd.tp");
        ban.addParent(cmd, true);
        banip.addParent(cmd, true);
        tempban.addParent(cmd, true);
        unban.addParent(cmd, true);
        unbanip.addParent(cmd, true);
        mute.addParent(cmd, true);
        tempmute.addParent(cmd, true);
        unmute.addParent(cmd, true);
        kick.addParent(cmd, true);
        tp.addParent(cmd, true);
        this.checkPerm(main);
        this.checkPerm(cmd);
        this.checkPerm(ban);
        this.checkPerm(banip);
        this.checkPerm(tempban);
        this.checkPerm(unban);
        this.checkPerm(unbanip);
        this.checkPerm(mute);
        this.checkPerm(tempmute);
        this.checkPerm(unmute);
        this.checkPerm(kick);
    }

    private void checkPerm(Permission perm) {
        Bukkit.getPluginManager().removePermission(perm.getName());
        Bukkit.getPluginManager().addPermission(perm);
    }
}

