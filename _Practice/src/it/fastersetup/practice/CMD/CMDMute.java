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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.API;
import it.fastersetup.practice.Api.ConfigLoader;

public class CMDMute
implements CommandExecutor {
    private ConfigLoader config;

    public CMDMute(Main pl) {
        this.config = new ConfigLoader(pl, "mutes.yml");
        this.config.generateFile();
    }

    private List<String> getMutes() {
        if (this.config.getConfig().contains("mute")) {
            ArrayList<String> mutes = new ArrayList<String>();
            for (String id : this.config.getConfig().getConfigurationSection("mute").getKeys(false)) {
                mutes.add(id);
            }
            return mutes;
        }
        return new ArrayList<String>();
    }

    private List<String> getTempMutes() {
        if (this.config.getConfig().contains("tempmute")) {
            ArrayList<String> mutes = new ArrayList<String>();
            long t = System.currentTimeMillis();
            for (String id : this.config.getConfig().getConfigurationSection("tempmute").getKeys(false)) {
                long l = this.config.getConfig().getLong("tempmute." + id + ".time");
                if (t - l < 0) {
                    mutes.add(id);
                    continue;
                }
                this.config.removeSection("tempmute." + id);
            }
            return mutes;
        }
        return new ArrayList<String>();
    }

    public boolean isMuted(UUID id) {
        if (!this.getMutes().contains(id.toString()) && !this.getTempMutes().contains(id.toString())) {
            return false;
        }
        return true;
    }

    public String getReason(UUID id) {
        if (this.config.getConfig().contains("mute." + id.toString())) {
            return this.config.getConfig().getString("mute." + id.toString());
        }
        if (this.config.getConfig().contains("tempmute." + id.toString() + ".reason")) {
            return this.config.getConfig().getString("tempmute." + id.toString() + ".reason");
        }
        return null;
    }

    public void setMute(UUID id, boolean mute, String reason) {
        if (mute) {
            if (reason == null || reason.isEmpty()) {
                reason = "N/A";
            }
            this.config.set("mute." + id.toString(), reason);
        } else {
            this.config.removeSection("mute." + id.toString());
            this.config.removeSection("tempmute." + id.toString());
        }
    }

    public void setTempMute(UUID id, int tick, String reason) {
        if (tick < 1) {
            tick = 1;
        }
        long ban = System.currentTimeMillis() + (long)(tick * 50);
        if (reason == null || reason.isEmpty()) {
            reason = "N/A";
        }
        this.config.set("tempmute." + id.toString() + ".time", ban);
        this.config.set("tempmute." + id.toString() + ".reason", reason);
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
            if (cmd.getName().equalsIgnoreCase("mute")) {
                if (player.hasPermission("pvpcoin.cmd.mute") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                    this.mute(sender, args);
                } else {
                    player.sendMessage("\u00a7cYou don't have permission.");
                }
            } else if (cmd.getName().equalsIgnoreCase("tempmute")) {
                if (player.hasPermission("pvpcoin.cmd.tempmute") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                    this.tempmute(sender, args);
                } else {
                    player.sendMessage("\u00a7cYou don't have permission.");
                }
            } else if (cmd.getName().equalsIgnoreCase("unmute")) {
                if (player.hasPermission("pvpcoin.cmd.unmute") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                    this.unmute(sender, args);
                } else {
                    player.sendMessage("\u00a7cYou don't have permission.");
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("mute")) {
            this.mute(sender, args);
        } else if (cmd.getName().equalsIgnoreCase("tempban")) {
            this.tempmute(sender, args);
        } else if (cmd.getName().equalsIgnoreCase("unmute")) {
            this.unmute(sender, args);
        }
        return true;
    }

    private void mute(CommandSender sender, String[] args) {
        String use = "\u00a7b/mute <player> [reason]";
        if (args.length == 0) {
            sender.sendMessage(use);
        } else if (args.length == 1) {
            Player p = Bukkit.getPlayer((String)args[0]);
            if (p != null) {
                this.setMute(p.getUniqueId(), true, "\u00a7eN/A");
                p.sendMessage("\u00a7cYou have been muted for: \u00a7eN/A");
                sender.sendMessage("\u00a7cMuted \u00a7e" + p.getName() + " \u00a7cfor: \u00a7eN/A");
                Bukkit.broadcastMessage((String)("\u00a74" + sender.getName() + " \u00a7chas muted \u00a74" + p.getName() + " \u00a7cfor: \u00a7eN/A"));
            } else {
                sender.sendMessage("\u00a7ePlayer not found.");
            }
        } else if (args.length >= 2) {
            Player p = Bukkit.getPlayer((String)args[0]);
            if (p != null) {
                String r = ChatColor.translateAlternateColorCodes((char)'&', (String)CMDMute.combine(1, args));
                this.setMute(p.getUniqueId(), true, r);
                p.sendMessage("\u00a7cYou have been muted for: \u00a7e" + r);
                sender.sendMessage("\u00a7cMuted \u00a7e" + p.getName() + " \u00a7cfor: \u00a7e" + r);
                Bukkit.broadcastMessage((String)("\u00a74" + sender.getName() + " \u00a7chas muted \u00a74" + p.getName() + " \u00a7cfor: \u00a7e" + r));
            } else {
                sender.sendMessage("\u00a7ePlayer not found.");
            }
        }
    }

    private void tempmute(CommandSender sender, String[] args) {
        String use = "\u00a7b/tempmute <player> <time> [reason]";
        if (args.length < 2) {
            sender.sendMessage(use);
        } else if (args.length == 2) {
            Player p = Bukkit.getPlayer((String)args[0]);
            if (p != null) {
                int t = API.stringToTicks(args[1]);
                if (t > 0) {
                    String r = "\u00a7eN/A";
                    String time = API.timeUntil(t * 50);
                    this.setTempMute(p.getUniqueId(), t, r);
                    p.sendMessage("\u00a7cYou have been muted for: \u00a7e" + r + " \u00a7cfor \u00a7a" + time);
                    sender.sendMessage("\u00a7cMuted \u00a7e" + p.getName() + " \u00a7cfor: \u00a7e" + r + " \u00a7cfor \u00a7a" + time);
                    Bukkit.broadcastMessage((String)("\u00a74" + sender.getName() + " \u00a7chas muted \u00a74" + p.getName() + " \u00a7cfor: \u00a7e" + r + " \u00a7cfor \u00a7a" + time));
                } else {
                    sender.sendMessage("\u00a7eTime must be greater than 0.");
                }
            } else {
                sender.sendMessage("\u00a7ePlayer not found.");
            }
        } else if (args.length >= 3) {
            Player p = Bukkit.getPlayer((String)args[0]);
            if (p != null) {
                int t = API.stringToTicks(args[1]);
                if (t > 0) {
                    String r = ChatColor.translateAlternateColorCodes((char)'&', (String)CMDMute.combine(2, args));
                    String time = API.timeUntil(t * 50);
                    this.setTempMute(p.getUniqueId(), t, r);
                    p.sendMessage("\u00a7cYou have been muted for: \u00a7e" + r + " \u00a7cfor \u00a7a" + time);
                    sender.sendMessage("\u00a7cMuted \u00a7e" + p.getName() + " \u00a7cfor: \u00a7e" + r + " \u00a7cfor \u00a7a" + time);
                    Bukkit.broadcastMessage((String)("\u00a74" + sender.getName() + " \u00a7chas muted \u00a74" + p.getName() + " \u00a7cfor: \u00a7e" + r + " \u00a7cfor \u00a7a" + time));
                } else {
                    sender.sendMessage("\u00a7eTime must be greater than 0.");
                }
            } else {
                sender.sendMessage("\u00a7ePlayer not found.");
            }
        }
    }

    private void unmute(CommandSender sender, String[] args) {
        String use = "\u00a7b/unmute <player>";
        if (args.length == 0) {
            sender.sendMessage(use);
        } else if (args.length >= 1) {
            Player p = Bukkit.getPlayer((String)args[0]);
            if (p != null) {
                if (this.isMuted(p.getUniqueId())) {
                    this.setMute(p.getUniqueId(), false, null);
                    p.sendMessage("\u00a7eYou have been unmuted by \u00a7c" + sender.getName());
                    sender.sendMessage("\u00a7eUnmuted \u00a7a" + p.getName() + "\u00a7e.");
                } else {
                    sender.sendMessage("\u00a7ePlayer is not muted.");
                }
            } else {
                sender.sendMessage("\u00a7ePlayer not found.");
            }
        }
    }
}

