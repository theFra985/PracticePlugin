/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.WorldCreator
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.generator.ChunkGenerator
 */
package net.propvp.command;

import java.io.File;
import net.propvp.Practice;
import net.propvp.world.EmptyChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

public class WorldManagementCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player)commandSender;
        if (arrstring.length == 0) {
            player.sendMessage((Object)ChatColor.RED + "World Management Commands");
            player.sendMessage((Object)ChatColor.GRAY + "/wm tp <player> <world-name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm create <world-name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm delete <world-name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm load <name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm unload <name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm clearcloned");
            player.sendMessage((Object)ChatColor.GRAY + "/wm list");
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("help")) {
            player.sendMessage((Object)ChatColor.RED + "World Management Commands");
            player.sendMessage((Object)ChatColor.GRAY + "/wm tp <player> <world-name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm create <world-name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm delete <world-name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm load <name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm unload <name>");
            player.sendMessage((Object)ChatColor.GRAY + "/wm clearcloned");
            player.sendMessage((Object)ChatColor.GRAY + "/wm list");
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("teleport") || arrstring[0].equalsIgnoreCase("tp")) {
            if (arrstring.length == 1) {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Correct usage: /wm tp <player> <world-name>");
                return true;
            }
            if (arrstring.length == 2) {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Correct usage: /wm tp <player> <world-name>");
                return true;
            }
            if (Bukkit.getPlayer((String)arrstring[1]) == null) {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "That player is not online.");
                return true;
            }
            if (Bukkit.getWorld((String)arrstring[2]) == null) {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "That world doesn't exist or is not loaded.");
                return true;
            }
            Bukkit.getPlayer((String)arrstring[1]).teleport(Bukkit.getWorld((String)arrstring[2]).getSpawnLocation());
            Bukkit.getPlayer((String)arrstring[1]).sendMessage((Object)ChatColor.GREEN + "You have been teleported to world " + Bukkit.getWorld((String)arrstring[2]).getName());
            player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GREEN + "You have teleported player " + Bukkit.getPlayer((String)arrstring[1]).getName() + " to world " + Bukkit.getWorld((String)arrstring[2]).getName());
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("create")) {
            World world2;
            if (arrstring.length == 1) {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Correct usage: /wm create <world-name>");
                return true;
            }
            for (World world2 : Bukkit.getWorlds()) {
                if (!world2.getName().equalsIgnoreCase(arrstring[1])) continue;
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "That world already exists.");
                return true;
            }
            world2 = WorldCreator.name((String)arrstring[1]).generator((ChunkGenerator)new EmptyChunkGenerator()).createWorld();
            player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GREEN + "The world " + world2.getName() + " has been created.");
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("delete")) {
            if (arrstring.length == 1) {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Correct usage: /wm delete <world-name>");
                return true;
            }
            if (Bukkit.getWorld((String)arrstring[1]) != null) {
                Practice.getWorldManager().deleteWorld(Bukkit.getWorld((String)arrstring[1]).getWorldFolder());
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GREEN + "World '" + arrstring[1] + "' has been deleted.");
            } else {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "That world cannot be found. Try loading it first.");
            }
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("load")) {
            if (arrstring.length == 1) {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Correct usage: /wm load <world-name>");
                return true;
            }
            Practice.getWorldManager().loadWorld(arrstring[1]);
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("unload")) {
            if (arrstring.length == 1) {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Correct usage: /wm unload <world-name>");
                return true;
            }
            if (Bukkit.getWorld((String)arrstring[1]) != null) {
                Practice.getWorldManager().unloadWorld(Bukkit.getWorld((String)arrstring[1]));
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GREEN + "World '" + arrstring[1] + "' has been unloaded.");
            } else {
                player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "That world doesn't exist or has not been loaded.");
            }
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("clearcloned")) {
            Practice.getWorldManager().clearClonedWorlds();
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("list")) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((Object)ChatColor.GOLD + "Worlds: ");
            for (World world : Bukkit.getWorlds()) {
                stringBuilder.append((Object)ChatColor.GRAY + world.getName() + ", ");
            }
            player.sendMessage(stringBuilder.toString());
            return true;
        }
        player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Could not find that sub-command.");
        return true;
    }
}

