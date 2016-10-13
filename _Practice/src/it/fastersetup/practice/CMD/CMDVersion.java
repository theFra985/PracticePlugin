/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.PluginDescriptionFile
 */
package it.fastersetup.practice.CMD;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import it.fastersetup.practice.Main;

public class CMDVersion
implements CommandExecutor {
    private Main plugin;

    public CMDVersion(Main pl) {
        this.plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (!player.isOp()) {
                player.sendMessage("\u00a7cYou don't have permission.");
                return true;
            }
            player.sendMessage("\u00a7ePvPCoin: \u00a7b" + this.plugin.getDescription().getVersion());
        } else {
            sender.sendMessage("PvPCoin: " + this.plugin.getDescription().getVersion());
        }
        return true;
    }
}

