/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package net.propvp.command;

import net.propvp.Practice;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreboardCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players may execute this command.");
            return true;
        }
        Player player = (Player)commandSender;
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (playerData.isHidingScoreboard()) {
            player.sendMessage((Object)ChatColor.LIGHT_PURPLE + "You are now seeing the scoreboard again.");
            playerData.setHidingScoreboard(false);
        } else {
            player.sendMessage((Object)ChatColor.LIGHT_PURPLE + "You are now hiding the scoreboard.");
            playerData.setHidingScoreboard(true);
        }
        return true;
    }
}

