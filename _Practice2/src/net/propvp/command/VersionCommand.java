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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VersionCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players may execute this command.");
            return true;
        }
        Player player = (Player)commandSender;
        player.sendMessage((Object)ChatColor.DARK_GRAY + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
        player.sendMessage((Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588\u2588\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588\u2588\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588");
        player.sendMessage((Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588   ProPractice Version 2.2.3");
        player.sendMessage((Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588\u2588\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588\u2588\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588   Made by deaL_ / skruffys / joeleoli");
        player.sendMessage((Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588\u2588\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588\u2588\u2588   In-game: joeleoli");
        player.sendMessage((Object)ChatColor.DARK_GRAY + "\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588\u2588\u2588" + (Object)ChatColor.DARK_RED + "\u2588" + (Object)ChatColor.DARK_GRAY + "\u2588\u2588\u2588");
        player.sendMessage((Object)ChatColor.DARK_GRAY + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
        return true;
    }
}

