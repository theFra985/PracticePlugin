/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package it.fastersetup.practice.CMD;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CMDTP
implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!(player.hasPermission("pvpcoin.cmd.tp") || player.hasPermission("pvpcoin.cmd.*") || player.isOp())) {
                player.sendMessage("\u00a7eYou don't have permission.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage("\u00a7b/tp <player>");
            } else {
                Player p = Bukkit.getPlayer((String)args[0]);
                if (p != null) {
                    player.teleport((Entity)p);
                } else {
                    player.sendMessage("\u00a7ePlayer not found.");
                }
            }
        } else {
            sender.sendMessage("Player only");
        }
        return true;
    }
}

