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

public class CMDKick
implements CommandExecutor {
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
            if (player.hasPermission("pvpcoin.cmd.kick") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                this.kick(sender, args);
            } else {
                player.sendMessage("\u00a7cNon hai il permesso.");
            }
        } else {
            this.kick(sender, args);
        }
        return true;
    }

    private void kick(CommandSender sender, String[] args) {
        String use = "\u00a7b/kick <player> [reason]";
        if (args.length == 0) {
            sender.sendMessage(use);
        } else if (args.length == 1) {
            Player p = Bukkit.getPlayer((String)args[0]);
            if (p != null) {
                p.kickPlayer("\u00a7eKicked from server.");
                sender.sendMessage("\u00a7eKicked player \u00a7a" + p.getName() + "\u00a7e.");
            } else {
                sender.sendMessage("\u00a7ePlayer not found.");
            }
        } else if (args.length >= 2) {
            Player p = Bukkit.getPlayer((String)args[0]);
            if (p != null) {
                String r = ChatColor.translateAlternateColorCodes((char)'&', (String)CMDKick.combine(1, args));
                p.kickPlayer(r);
                sender.sendMessage("\u00a7eKicked player \u00a7a" + p.getName() + " \u00a7efor: " + r + "\u00a7e.");
            } else {
                sender.sendMessage("\u00a7ePlayer not found.");
            }
        }
    }
}

