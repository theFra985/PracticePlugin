/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
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

public class CMDSpectate
implements CommandExecutor {
    private Main plugin;

    public CMDSpectate(Main pl) {
        this.plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage("\u00a7b/spectate <player>");
            } else if (args.length == 1) {
                if (this.plugin.arenas.isInArena(player.getUniqueId())) {
                    player.sendMessage("\u00a7eNon disponibile in game.");
                    return true;
                }
                if (this.plugin.pman.inParty(player.getUniqueId())) {
                    player.sendMessage("\u00a7eDevi prima uscire dal partyt.");
                    return true;
                }
                Player p = Bukkit.getPlayer((String)args[0]);
                if (p != null) {
                    if (player.getUniqueId() == p.getUniqueId()) {
                        player.sendMessage("\u00a7eNon puoi spectarti da solo.");
                        return true;
                    }
                    if (this.plugin.arenas.isInArena(p.getUniqueId())) {
                        this.plugin.arenas.spectate(player.getUniqueId(), p.getUniqueId());
                        player.sendMessage("\u00a7aStai ora spectando \u00a7b" + p.getName() + "\u00a7a.");
                    } else {
                        player.sendMessage("\u00a7b" + p.getName() + " \u00a7enon \u00e8 in arena.");
                    }
                }
            } else {
                player.sendMessage("\u00a7b/spectate <player>");
            }
        } else {
            sender.sendMessage("Player only command.");
        }
        return true;
    }
}

