/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package it.fastersetup.practice.CMD;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDSpy
implements CommandExecutor {
    private ArrayList<UUID> list = new ArrayList<UUID>();

    private ArrayList<UUID> online(ArrayList<UUID> list) {
        for (UUID id : list) {
            if (Bukkit.getPlayer((UUID)id) != null) continue;
            list.remove(id);
        }
        return list;
    }

    public void spy(String from, String to, String message) {
        if (from.equals(to)) {
            return;
        }
        for (UUID id : this.online(this.list)) {
            Bukkit.getPlayer((UUID)id).sendMessage("\u00a78[\u00a77" + from + " \u00a78-> \u00a77" + to + "\u00a78] \u00a77" + message);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (player.hasPermission("pvpcoin.cmd.spy") || player.hasPermission("pvpcoin.cmd.*") || player.isOp()) {
                if (!this.list.contains(player.getUniqueId())) {
                    this.list.add(player.getUniqueId());
                    player.sendMessage("\u00a7bSpy mode \u00a7aEnabled");
                } else {
                    this.list.remove(player.getUniqueId());
                    player.sendMessage("\u00a7bSpy mode \u00a7cDisabled");
                }
            } else {
                player.sendMessage("\u00a7cYou don't have permission.");
            }
        } else {
            sender.sendMessage("Player only");
        }
        return true;
    }
}

