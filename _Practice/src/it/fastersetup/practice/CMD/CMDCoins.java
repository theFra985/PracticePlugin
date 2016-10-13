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

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import it.fastersetup.practice.Main;

public class CMDCoins
implements CommandExecutor {
    private Main plugin;

    public CMDCoins(Main pl) {
        this.plugin = pl;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String use = "\u00a7b/coins give <player> <amount>";
        if (sender instanceof Player) {
            sender.sendMessage("Console only");
        } else if (args[0].equalsIgnoreCase("give")) {
            if (args.length <= 2) {
                sender.sendMessage(use);
            } else if (args.length == 3) {
                Player p = Bukkit.getPlayer((String)args[1]);
                if (p != null) {
                    String n = args[2].replaceAll("[^0-9]", "");
                    if (!n.isEmpty()) {
                        int a = Integer.parseInt(n);
                        if (a > 0) {
                            this.plugin.coins.add(p.getUniqueId(), a);
                            if (a == 1) {
                                sender.sendMessage("Given 1 match to " + p.getName() + ".");
                                p.sendMessage("\u00a7eYou received \u00a7b1 \u00a7ecoin for \u00a7avoting\u00a7e.");
                            } else {
                                sender.sendMessage("Given " + a + " matches to " + p.getName() + ".");
                                p.sendMessage("\u00a7eYou received \u00a7b" + a + " \u00a7ecoins for \u00a7avoting\u00a7e.");
                            }
                        } else {
                            sender.sendMessage("\u00a7eValue must be greater than 0.");
                        }
                    } else {
                        sender.sendMessage("\u00a7eYou must enter a number.");
                    }
                } else {
                    sender.sendMessage("Player not found.");
                }
            } else {
                sender.sendMessage(use);
            }
        } else {
            sender.sendMessage(use);
        }
        return true;
    }
}

