/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Player$Spigot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.propvp.command;

import java.util.List;
import net.propvp.Practice;
import net.propvp.game.Game;
import net.propvp.game.ladder.Ladder;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import net.propvp.util.EntityHider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SpectateCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players may execute this command.");
            return true;
        }
        Player player = (Player)commandSender;
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (playerData.hasMatch()) {
            player.sendMessage((Object)ChatColor.RED + "This command cannot be used while in a match.");
            return true;
        }
        if (arrstring.length == 0) {
            player.sendMessage((Object)ChatColor.RED + "Specify a player to spectate or '/spectate leave' to stop spectating.");
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("leave")) {
            if (!playerData.isSpectating()) {
                player.sendMessage((Object)ChatColor.RED + "You are not spectating any match.");
                return true;
            }
            playerData.getMatchSpectating().removeSpectator(player);
            playerData.setMatchSpectating(null);
            playerData.setSpectating(false);
            player.teleport(Practice.getBackend().getSpawn());
            player.sendMessage((Object)ChatColor.RED + "You have stopped spectating the match.");
            return true;
        }
        if (playerData.getMatch() != null || playerData.getQueue() != null || playerData.isEditing() || playerData.isSpectating()) {
            player.sendMessage((Object)ChatColor.RED + "You cannot spectate if you're editing, spectating, in a queue, or in a match.");
            return true;
        }
        if (player.getName().equals(arrstring[0])) {
            player.sendMessage((Object)ChatColor.RED + "You cannot spectate yourself.");
            return true;
        }
        if (Bukkit.getPlayer((String)arrstring[0]) == null) {
            player.sendMessage((Object)ChatColor.RED + "That player is offline.");
            return true;
        }
        Player player2 = Bukkit.getPlayer((String)arrstring[0]);
        PlayerData playerData2 = Practice.getBackend().getDataManager().getData(player2);
        if (!playerData2.hasMatch()) {
            player.sendMessage((Object)ChatColor.RED + "That player is not in a match.");
            return true;
        }
        playerData.setMatchSpectating(playerData2.getMatch());
        playerData.setSpectating(true);
        player.spigot().setCollidesWithEntities(false);
        player.teleport(player2.getLocation());
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().clear();
        player.updateInventory();
        if (Bukkit.getVersion().contains("1.7")) {
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(true);
            player.setCanPickupItems(false);
        } else {
            player.setGameMode(GameMode.SPECTATOR);
        }
        player.sendMessage((Object)ChatColor.GREEN + "You are now spectating " + player2.getName() + "'s match.");
        EntityHider entityHider = Practice.getBackend().getEntityHider();
        if (playerData2.getMatch().getPlayers() != null) {
            for (Player player3 : playerData2.getMatch().getPlayers()) {
                entityHider.hideEntity(player3, (Entity)player);
                entityHider.showEntity(player, (Entity)player3);
            }
        }
        if (playerData2.getMatch().getSpectators() != null) {
            for (Player player3 : playerData2.getMatch().getSpectators()) {
                entityHider.hideEntity(player3, (Entity)player);
                entityHider.hideEntity(player, (Entity)player3);
            }
        }
        playerData2.getMatch().addSpectator(player);
        return true;
    }
}

