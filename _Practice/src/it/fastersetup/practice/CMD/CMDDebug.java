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

public class CMDDebug
implements CommandExecutor {
    private Main plugin;

    public CMDDebug(Main pl) {
        this.plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.getName().equals("StarShadow")) {
                player.sendMessage("No Permission");
                return true;
            }
            this.plugin.arenas.debug(player);
        } else {
            sender.sendMessage("Command for StarShadow only.");
        }
        return true;
    }
}

