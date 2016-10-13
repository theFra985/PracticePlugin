/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package it.fastersetup.practice.CMD;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.fastersetup.practice.Main;

public class CMDMsg
implements CommandExecutor {
    private Main plugin;

    public CMDMsg(Main pl) {
        this.plugin = pl;
    }

    public void msg(Player from, Player to, String message) {
        from.sendMessage("\u00a7a[\u00a7bYou \u00a7a-> \u00a7b" + to.getName() + "\u00a7a] \u00a7e" + message);
        to.sendMessage("\u00a7a[\u00a7b" + from.getName() + " \u00a7a-> \u00a7bYou\u00a7a] \u00a7e" + message);
    }

    public void msg(CommandSender from, Player to, String message) {
        from.sendMessage("You -> " + to.getName() + " >> " + message);
        to.sendMessage("\u00a7a[\u00a7bConsole \u00a7a-> \u00a7bYou\u00a7a] \u00a7e" + message);
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

    public void tryMsg(Player player, Player to, String message) {
        if (!this.plugin.mutes.isMuted(player.getUniqueId()) || this.plugin.over.contains(player.getUniqueId())) {
            if (!this.plugin.cooldown.inCooldown(player.getUniqueId())) {
                this.plugin.cooldown.addCooldown(player.getUniqueId(), 40, null);
                if (to != null) {
                    if (this.plugin.data.getSettings(to.getUniqueId()).msgEnabled()) {
                        if (!this.plugin.ignore.isIgnoring(to.getUniqueId(), player.getUniqueId())) {
                            String m = ChatColor.stripColor((String)ChatColor.translateAlternateColorCodes((char)'&', (String)message));
                            this.msg(player, to, m);
                            this.plugin.spy.spy(player.getName(), to.getName(), m);
                        } else {
                            player.sendMessage("\u00a7eNon puoi mandare msg a questo player.");
                        }
                    } else {
                        player.sendMessage("\u00a7eQuesto Player ha gli msg disabilitati.");
                    }
                } else {
                    player.sendMessage("\u00a7ePlayer non trovato.");
                }
            } else {
                player.sendMessage("\u00a7eDevi aspettare 2 secondi prima di poter scrivere.");
            }
        } else {
            player.sendMessage("\u00a7cSei mutato per: \u00a7e" + this.plugin.mutes.getReason(player.getUniqueId()));
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!this.plugin.mutes.isMuted(player.getUniqueId()) || this.plugin.over.contains(player.getUniqueId())) {
                if (!this.plugin.cooldown.inCooldown(player.getUniqueId())) {
                    this.plugin.cooldown.addCooldown(player.getUniqueId(), 40, null);
                    String use = "\u00a7b/msg <player> <msg>";
                    if (args.length < 2) {
                        sender.sendMessage(use);
                    } else if (args.length >= 2) {
                        Player p = Bukkit.getPlayer((String)args[0]);
                        if (p != null) {
                            if (this.plugin.data.getSettings(p.getUniqueId()).msgEnabled()) {
                                if (!this.plugin.ignore.isIgnoring(p.getUniqueId(), player.getUniqueId())) {
                                    String m = ChatColor.stripColor((String)ChatColor.translateAlternateColorCodes((char)'&', (String)CMDMsg.combine(1, args)));
                                    this.msg(player, p, m);
                                    this.plugin.spy.spy(player.getName(), p.getName(), m);
                                    this.plugin.reply.setReply(p.getUniqueId(), player.getUniqueId());
                                    this.plugin.reply.setReply(player.getUniqueId(), p.getUniqueId());
                                } else {
                                    player.sendMessage("\u00a7eNon puoi mandare msg a questo player.");
                                }
                            } else {
                                player.sendMessage("\u00a7eIl Player ha i messaggi disabilitati.");
                            }
                        } else {
                            player.sendMessage("\u00a7ePlayer non trovato.");
                        }
                    }
                } else {
                    player.sendMessage("\u00a7eDevi aspettare 2 secondi prima di poter scrivere.");
                }
            } else {
                player.sendMessage("\u00a7cSei mutato per: \u00a7e" + this.plugin.mutes.getReason(player.getUniqueId()));
            }
        } else {
            String use = "\u00a7b/msg <player> <msg>";
            if (args.length < 2) {
                sender.sendMessage(use);
            } else if (args.length >= 2) {
                Player p = Bukkit.getPlayer((String)args[0]);
                if (p != null) {
                    String m = ChatColor.stripColor((String)ChatColor.translateAlternateColorCodes((char)'&', (String)CMDMsg.combine(1, args)));
                    this.msg(sender, p, m);
                    this.plugin.reply.setReply(p.getUniqueId(), null);
                } else {
                    sender.sendMessage("\u00a7ePlayer non trovato.");
                }
            }
        }
        return true;
    }
}

