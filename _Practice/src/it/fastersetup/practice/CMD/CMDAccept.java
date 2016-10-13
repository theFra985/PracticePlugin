/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package it.fastersetup.practice.CMD;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.fastersetup.practice.Main;

public class CMDAccept
implements CommandExecutor {
    private Main plugin;

    public CMDAccept(Main pl) {
        this.plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.plugin.arenas.isInArena(player.getUniqueId())) {
                player.sendMessage("\u00a7eDisabled while ingame.");
                return true;
            }
            if (this.plugin.duels.isAccepting(player.getUniqueId())) {
                this.plugin.duels.accept(player.getUniqueId());
            } else {
                player.sendMessage("\u00a7eYou don't have a duel request.");
            }
        } else {
            sender.sendMessage("That is player only.");
        }
        return true;
    }
}

