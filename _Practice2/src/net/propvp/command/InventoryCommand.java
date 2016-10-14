/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 */
package net.propvp.command;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.propvp.player.InventoryManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class InventoryCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players may execute this command.");
            return true;
        }
        Player player = (Player)commandSender;
        if (arrstring.length == 0) {
            player.sendMessage((Object)ChatColor.RED + "You need to specify a player to check their inventory.");
            return true;
        }
        if (!InventoryManager.invs.containsKey(arrstring[0])) {
            player.sendMessage((Object)ChatColor.RED + "The player's inventory has not been stored recently.");
            return true;
        }
        player.openInventory(InventoryManager.invs.get(arrstring[0]));
        InventoryManager.opened.add(player.getUniqueId());
        return true;
    }
}

