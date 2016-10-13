/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package it.fastersetup.practice.CMD;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.fastersetup.practice.Main;

public class CMDSettings
implements CommandExecutor {
    private Main plugin;

    public CMDSettings(Main pl) {
        this.plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.isOp()) {
                player.sendMessage("\u00a7cYou don't have permission.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage("\u00a7eNo arguments.");
            } else if (args[0].equalsIgnoreCase("override")) {
                if (args.length == 1) {
                    if (!this.plugin.over.contains(player.getUniqueId())) {
                        this.plugin.over.add(player.getUniqueId());
                        player.sendMessage("\u00a7aOverride enabled.");
                    } else {
                        this.plugin.over.remove(player.getUniqueId());
                        player.sendMessage("\u00a7aOverride disabled.");
                    }
                } else if (args.length == 2) {
                    Player temp = Bukkit.getPlayer((String)args[1]);
                    if (temp != null) {
                        if (!this.plugin.over.contains(temp.getUniqueId())) {
                            this.plugin.over.add(temp.getUniqueId());
                            player.sendMessage("\u00a7aEnabled override for \u00a7e" + temp.getName());
                        } else {
                            this.plugin.over.remove(temp.getUniqueId());
                            player.sendMessage("\u00a7aDisabled override for \u00a7e" + temp.getName());
                        }
                    } else {
                        player.sendMessage("\u00a7ePlayer not found.");
                    }
                } else {
                    player.sendMessage("\u00a7eToo many arguments.");
                }
            } else if (args[0].equalsIgnoreCase("lobby")) {
                if (args.length == 1) {
                    this.plugin.lobby.tp(player);
                    player.sendMessage("\u00a7aTeleported to lobby");
                } else if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("set")) {
                        this.plugin.lobby.set(player.getLocation());
                        player.sendMessage("\u00a7aLobby set");
                    } else {
                        player.sendMessage("\u00a7eUnknown argument.");
                    }
                } else {
                    player.sendMessage("\u00a7eToo many arguments.");
                }
            } else {
                player.sendMessage("\u00a7eUnknown argument");
            }
        } else {
            sender.sendMessage("Command is player only.");
        }
        return true;
    }
}

