/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package net.propvp.command;

import net.propvp.util.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand
implements CommandExecutor {
    private Player player;

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players may execute this command.");
            return true;
        }
        this.player = (Player)commandSender;
        if (arrstring.length == 0) {
            this.ping();
        } else {
            this.ping(arrstring[0]);
        }
        return true;
    }

    public void ping() {
        this.player.sendMessage((Object)ChatColor.GOLD + "Ping: " + (Object)ChatColor.GRAY + PlayerUtils.getPing(this.player) + "ms");
    }

    public void ping(String string) {
        if (Bukkit.getPlayer((String)string) == null) {
            this.player.sendMessage((Object)ChatColor.RED + "That player is not online.");
            return;
        }
        this.player.sendMessage((Object)ChatColor.GOLD + string + "'s Ping: " + (Object)ChatColor.GRAY + PlayerUtils.getPing(Bukkit.getPlayer((String)string)));
    }
}

