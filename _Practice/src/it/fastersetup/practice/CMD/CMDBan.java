/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practice.CMD;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.API;
import it.fastersetup.practice.Api.ConfigLoader;

public class CMDBan
implements CommandExecutor {
    private ConfigLoader config;
    private String msg;
    private String banip;
    private String tempban;
    private Main plugin;

    public CMDBan(Main pl) {
        this.plugin = pl;
        this.config = new ConfigLoader(pl, "bans.yml");
        this.config.generateFile();
        this.config.setDefault("banmsg", "&cBanned: &e<reason>");
        this.config.setDefault("banipmsg", "&cYour IP is banned.");
        this.config.setDefault("tempbanmsg", "&cBanned for &e<reason> &cfor &e<time>");
        this.msg = ChatColor.translateAlternateColorCodes((char)'&', (String)this.config.getConfig().getString("banmsg"));
        this.banip = ChatColor.translateAlternateColorCodes((char)'&', (String)this.config.getConfig().getString("banipmsg"));
        this.tempban = ChatColor.translateAlternateColorCodes((char)'&', (String)this.config.getConfig().getString("tempbanmsg"));
    }

    public String getBanMsg(String reason) {
        return CMDBan.replace(this.msg, "reason", ChatColor.translateAlternateColorCodes((char)'&', (String)reason));
    }

    public String getTempBan(String reason, String time) {
        return CMDBan.replace(CMDBan.replace(this.tempban, "reason", reason), "time", time);
    }

    public String getBanIp() {
        return this.banip;
    }

    public String getReason(UUID id) {
        if (this.config.getConfig().contains("banned." + id.toString())) {
            return this.config.getConfig().getString("banned." + id.toString());
        }
        if (this.config.getConfig().contains("tempban." + id.toString() + ".reason")) {
            return this.config.getConfig().getString("tempban." + id.toString() + ".reason");
        }
        return null;
    }

    private List<String> getBanned() {
        ArrayList<String> bans = new ArrayList<String>();
        if (this.config.getConfig().contains("banned")) {
            for (String id : this.config.getConfig().getConfigurationSection("banned").getKeys(false)) {
                bans.add(id);
            }
            return bans;
        }
        return new ArrayList<String>();
    }

    private List<String> getTempBanned() {
        if (this.config.getConfig().contains("tempban")) {
            ArrayList<String> temp = new ArrayList<String>();
            long t = System.currentTimeMillis();
            for (String id : this.config.getConfig().getConfigurationSection("tempban").getKeys(false)) {
                long l = this.config.getConfig().getLong("tempban." + id + ".time");
                if (t - l < 0) {
                    temp.add(id);
                    continue;
                }
                this.config.removeSection("tempban." + id);
            }
            return temp;
        }
        return new ArrayList<String>();
    }

    private List<String> getBannedIP() {
        if (this.config.getConfig().contains("bannedip")) {
            return this.config.getConfig().getStringList("bannedip");
        }
        return new ArrayList<String>();
    }

    public boolean isBanned(UUID id) {
        if (!this.getBanned().contains(id.toString()) && !this.getTempBanned().contains(id.toString())) {
            return false;
        }
        return true;
    }

    public boolean ipBanned(Player player) {
        return this.getBannedIP().contains(player.getAddress().getAddress().getHostAddress());
    }

    public boolean ipBanned(String ip) {
        return this.getBannedIP().contains(ip);
    }

    public void setBanned(UUID id, boolean ban, String reason) {
        if (ban) {
            if (reason == null || reason.isEmpty()) {
                reason = "Banned";
            }
            this.config.set("banned." + id.toString(), reason);
        } else {
            this.config.removeSection("banned." + id.toString());
            this.config.removeSection("tempban." + id.toString());
        }
    }

    public void setTempban(UUID id, int tick, String reason) {
        if (tick < 1) {
            tick = 1;
        }
        long ban = System.currentTimeMillis() + (long)(tick * 50);
        this.config.set("tempban." + id.toString() + ".time", ban);
        if (reason == null || reason.isEmpty()) {
            reason = "Banned";
        }
        this.config.set("tempban." + id.toString() + ".reason", reason);
    }

    public void setBanIP(String ip, boolean ban) {
        List<String> bans = this.getBannedIP();
        if (ban) {
            if (!bans.contains(ip)) {
                bans.add(ip);
            }
        } else if (bans.contains(ip)) {
            bans.remove(ip);
        }
        if (bans.size() > 0) {
            this.config.set("bannedip", bans);
        } else {
            this.config.set("bannedip", null);
        }
    }

    private static String replace(String input, String ch, String replace) {
        return input.replaceAll(Pattern.quote("<" + ch + ">"), Matcher.quoteReplacement(replace));
    }

    private static String combine(int index, String[] args) {
        String f = "";
        int i = index;
        while (i < args.length) {
            f = String.valueOf(f) + args[i] + " ";
            ++i;
        }
        return f.substring(0, f.length() - 1);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("ban")) {
                if (player.hasPermission("pvpcoin.cmd.ban") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                    this.ban(sender, args);
                } else {
                    player.sendMessage("\u00a7cYou don't have permission.");
                }
            } else if (cmd.getName().equalsIgnoreCase("banip")) {
                if (player.hasPermission("pvpcoin.cmd.banip") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                    this.banip(sender, args);
                } else {
                    player.sendMessage("\u00a7cYou don't have permission.");
                }
            } else if (cmd.getName().equalsIgnoreCase("tempban")) {
                if (player.hasPermission("pvpcoin.cmd.tempban") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                    this.tempban(sender, args);
                } else {
                    player.sendMessage("\u00a7cYou don't have permission.");
                }
            } else if (cmd.getName().equalsIgnoreCase("unban")) {
                if (player.hasPermission("pvpcoin.cmd.unban") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                    this.unban(sender, args);
                } else {
                    player.sendMessage("\u00a7cYou don't have permission.");
                }
            } else if (cmd.getName().equalsIgnoreCase("unbanip")) {
                if (player.hasPermission("pvpcoin.cmd.unbanip") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                    this.unbanip(sender, args);
                } else {
                    player.sendMessage("\u00a7cYou don't have permission.");
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("ban")) {
            this.ban(sender, args);
        } else if (cmd.getName().equalsIgnoreCase("banip")) {
            this.banip(sender, args);
        } else if (cmd.getName().equalsIgnoreCase("tempban")) {
            this.tempban(sender, args);
        } else if (cmd.getName().equalsIgnoreCase("unban")) {
            this.unban(sender, args);
        } else if (cmd.getName().equalsIgnoreCase("unbanip")) {
            this.unbanip(sender, args);
        }
        return true;
    }

    private void ban(CommandSender sender, String[] args) {
        String use = "\u00a7b/ban <player> [reason]";
        if (args.length == 0) {
            sender.sendMessage(use);
        } else if (args.length == 1) {
            UUID p = this.plugin.names.getId(args[0]);
            if (p != null) {
                Player pl = Bukkit.getPlayer((UUID)p);
                this.setBanned(p, true, null);
                if (pl != null) {
                    pl.kickPlayer(this.getBanMsg("\u00a7cBanned"));
                }
                sender.sendMessage("\u00a7eBanned \u00a7c" + this.plugin.names.properName(args[0]) + " \u00a7efor: \u00a7cBanned");
                Bukkit.broadcastMessage((String)("\u00a74" + sender.getName() + " \u00a7chas banned \u00a74" + this.plugin.names.properName(args[0]) + " \u00a7cfor: Banned"));
            } else {
                sender.sendMessage("Name not found.");
            }
        } else if (args.length >= 2) {
            UUID p = this.plugin.names.getId(args[0]);
            if (p != null) {
                Player pl = Bukkit.getPlayer((UUID)p);
                String r = ChatColor.translateAlternateColorCodes((char)'&', (String)CMDBan.combine(1, args));
                this.setBanned(p, true, r);
                if (pl != null) {
                    pl.kickPlayer(this.getBanMsg(r));
                }
                sender.sendMessage("\u00a7eBanned \u00a7c" + this.plugin.names.properName(args[0]) + " \u00a7efor: " + r);
                Bukkit.broadcastMessage((String)("\u00a74" + sender.getName() + " \u00a7chas banned \u00a74" + this.plugin.names.properName(args[0]) + " \u00a7cfor: " + r));
            } else {
                sender.sendMessage("Name not found.");
            }
        }
    }

    private void banip(CommandSender sender, String[] args) {
        String use = "\u00a7b/banip <player>";
        if (args.length == 0) {
            sender.sendMessage(use);
        } else if (args.length == 1) {
            Player p = Bukkit.getPlayer((String)args[0]);
            if (p != null) {
                String ip = p.getAddress().getHostString();
                this.setBanIP(ip, true);
                @SuppressWarnings("deprecation")
				Player[] arrplayer = Bukkit.getOnlinePlayers();
                int n = arrplayer.length;
                int n2 = 0;
                while (n2 < n) {
                    Player pl = arrplayer[n2];
                    if (pl.getAddress().getHostString().equals(ip)) {
                        pl.kickPlayer(this.banip);
                    }
                    ++n2;
                }
                sender.sendMessage("\u00a74You banned " + p.getName() + "'s IP: " + ip);
            } else {
                sender.sendMessage("\u00a7ePlayer not found.");
            }
        } else {
            sender.sendMessage(use);
        }
    }

    private void tempban(CommandSender sender, String[] args) {
        String use = "\u00a7b/tempban <player> <time> [reason]";
        if (args.length < 2) {
            sender.sendMessage(use);
        } else if (args.length == 2) {
            UUID p = this.plugin.names.getId(args[0]);
            if (p != null) {
                Player pl = Bukkit.getPlayer((UUID)p);
                int t = API.stringToTicks(args[1]);
                if (t > 0) {
                    String time = API.timeUntil(t * 50);
                    this.setTempban(p, t, null);
                    if (pl != null) {
                        pl.kickPlayer(this.getTempBan("\u00a7cBanned", time));
                    }
                    sender.sendMessage("\u00a7eBanned \u00a7c" + this.plugin.names.properName(args[0]) + " \u00a7efor \u00a7cBanned \u00a7efor \u00a7b" + time);
                    Bukkit.broadcastMessage((String)("\u00a74" + sender.getName() + " \u00a7chas banned \u00a74" + this.plugin.names.properName(args[0]) + " \u00a7cfor " + "Banned" + " \u00a7cfor \u00a7e" + time));
                } else {
                    sender.sendMessage("\u00a7eTime must be greater than 0.");
                }
            } else {
                sender.sendMessage("\u00a7eName not found.");
            }
        } else if (args.length >= 3) {
            UUID p = this.plugin.names.getId(args[0]);
            if (p != null) {
                Player pl = Bukkit.getPlayer((UUID)p);
                int t = API.stringToTicks(args[1]);
                if (t > 0) {
                    String r = ChatColor.translateAlternateColorCodes((char)'&', (String)CMDBan.combine(2, args));
                    String time = API.timeUntil(t * 50);
                    this.setTempban(p, t, r);
                    if (pl != null) {
                        pl.kickPlayer(this.getTempBan(r, time));
                    }
                    sender.sendMessage("\u00a7eBanned \u00a7c" + this.plugin.names.properName(args[0]) + " \u00a7efor " + r + " \u00a7efor \u00a7b" + time);
                    Bukkit.broadcastMessage((String)("\u00a74" + sender.getName() + " \u00a7chas banned \u00a74" + this.plugin.names.properName(args[0]) + " \u00a7cfor " + r + " \u00a7cfor \u00a7e" + time));
                } else {
                    sender.sendMessage("\u00a7eTime must be greater than 0.");
                }
            } else {
                sender.sendMessage("\u00a7eName not found.");
            }
        }
    }

    private void unban(CommandSender sender, String[] args) {
        String use = "\u00a7b/unban <player>";
        if (args.length == 0) {
            sender.sendMessage(use);
        } else if (args.length == 1) {
            if (this.plugin.names.getId(args[0]) != null) {
                UUID b = this.plugin.names.getId(args[0]);
                if (this.isBanned(b)) {
                    this.setBanned(b, false, null);
                    sender.sendMessage("\u00a7e" + this.plugin.names.properName(args[0]) + " \u00a7ahas been unbanned.");
                } else {
                    sender.sendMessage("\u00a7ePlayer is not banned.");
                }
            } else {
                sender.sendMessage("\u00a7eThat name could not be found.");
            }
        } else {
            sender.sendMessage(use);
        }
    }

    private void unbanip(CommandSender sender, String[] args) {
        String use = "\u00a7b/unbanip <ip>";
        if (args.length == 0) {
            sender.sendMessage(use);
        } else if (args.length == 1) {
            if (this.getBannedIP().contains(args[0])) {
                this.setBanIP(args[0], false);
                sender.sendMessage("\u00a7eIP \u00a7b" + args[0] + " \u00a7ehas been unbanned.");
            } else {
                sender.sendMessage("\u00a7eThat IP is not banned.");
            }
        } else {
            sender.sendMessage(use);
        }
    }
}

