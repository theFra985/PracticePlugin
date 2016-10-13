/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package it.fastersetup.practice.CMD;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.fastersetup.practice.Main;

public class CMDPC
implements CommandExecutor {
    private Main plugin;

    public CMDPC(Main pl) {
        this.plugin = pl;
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
            if (this.plugin.pman.inParty(player.getUniqueId())) {
                String use = "\u00a7b/pc <msg>";
                if (args.length == 0) {
                    player.sendMessage(use);
                } else if (args.length >= 1) {
                    String msg = ChatColor.stripColor((String)ChatColor.translateAlternateColorCodes((char)'&', (String)CMDPC.combine(0, args)));
                    this.plugin.pman.getParty(this.plugin.pman.getLeader(player.getUniqueId())).msgAll("\u00a75[\u00a7fParty\u00a75] " + player.getName() + " \u00a7d> \u00a7f" + msg);
                }
            } else {
                player.sendMessage("\u00a7eNon sei in un party.");
            }
        } else {
            sender.sendMessage("Player only");
        }
        return true;
    }
}

