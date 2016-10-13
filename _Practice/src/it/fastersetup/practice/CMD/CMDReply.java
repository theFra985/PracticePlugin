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

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.fastersetup.practice.Main;

public class CMDReply
implements CommandExecutor {
    private HashMap<UUID, UUID> map = new HashMap<UUID, UUID>();
    private Main plugin;

    public CMDReply(Main pl) {
        this.plugin = pl;
    }

    public void setReply(UUID id, UUID r) {
        if (r != null) {
            this.map.put(id, r);
        } else if (this.map.containsKey(id)) {
            this.map.remove(id);
        }
    }

    private static String combine(int index, String[] args) {
        String f = "";
        int i = index;
        while (i < args.length) {
            f = String.valueOf(f) + args[i] + " ";
            ++i;
        }
        return f.substring(0, f.length() - 1);
    }

    public void remove(UUID id) {
        if (this.map.containsKey(id)) {
            this.map.remove(id);
        }
        for (UUID rem : this.map.keySet()) {
            if (this.map.get(rem) != id) continue;
            this.map.remove(rem);
            break;
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.map.containsKey(player.getUniqueId())) {
                Player to = Bukkit.getPlayer((UUID)this.map.get(player.getUniqueId()));
                if (to != null) {
                    this.plugin.msg.tryMsg(player, to, CMDReply.combine(0, args));
                } else {
                    player.sendMessage("\u00a7ePlayer non trovato.");
                }
            } else {
                player.sendMessage("\u00a7eNon hai messaggi a cui replicare.");
            }
        } else {
            sender.sendMessage("Player only.");
        }
        return true;
    }
}

