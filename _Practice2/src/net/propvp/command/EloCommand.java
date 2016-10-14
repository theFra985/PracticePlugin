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

import java.util.Map;
import java.util.Set;
import net.propvp.Practice;
import net.propvp.game.GameMode;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import net.propvp.util.Elo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EloCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players may execute this command.");
            return true;
        }
        Player player = (Player)commandSender;
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        player.sendMessage((Object)ChatColor.GOLD + "Your Ratings");
        for (Map.Entry<GameMode, Elo> entry : playerData.getRatings().entrySet()) {
            player.sendMessage((Object)ChatColor.GRAY + " \u00bb " + (Object)ChatColor.YELLOW + entry.getKey().getName() + (Object)ChatColor.RED + " " + entry.getValue().getRating());
        }
        return true;
    }
}

