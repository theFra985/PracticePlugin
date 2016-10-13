/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practice.CMD;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.ConfigLoader;

public class CMDIgnore
implements CommandExecutor {
    private ConfigLoader config;
    private Main plugin;

    public CMDIgnore(Main pl) {
        this.plugin = pl;
        this.config = new ConfigLoader(pl, "ignore.yml");
        this.config.generateFile();
    }

    public boolean isIgnoring(UUID id, UUID ignore) {
        return this.getIgnore(id).contains(ignore.toString());
    }

    public List<String> getIgnore(UUID id) {
        if (this.config.getConfig().contains(id.toString())) {
            return this.config.getConfig().getStringList(id.toString());
        }
        return new ArrayList<String>();
    }

    public boolean ignore(UUID id, UUID ignore) {
        List<String> list = this.getIgnore(id);
        boolean i = false;
        if (!list.contains(ignore.toString())) {
            list.add(ignore.toString());
            i = true;
        } else {
            list.remove(ignore.toString());
            i = false;
        }
        if (list.size() > 0) {
            this.config.set(id.toString(), list);
        } else {
            this.config.removeSection(id.toString());
        }
        return i;
    }

    public boolean unignore(UUID id, UUID ignore) {
        List<String> list = this.getIgnore(id);
        boolean s = false;
        if (list.contains(ignore.toString())) {
            list.remove(ignore.toString());
            s = true;
        }
        if (list.size() > 0) {
            this.config.set(id.toString(), list);
        } else {
            this.config.removeSection(id.toString());
        }
        return s;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("ignore")) {
                if (this.plugin.cooldown.inCooldown(player.getUniqueId())) {
                    player.sendMessage("\u00a7eDevi aspettare 2 secondi per poter scrivere.");
                    return true;
                }
                this.plugin.cooldown.addCooldown(player.getUniqueId(), 40, null);
                String use = "\u00a7b/ignore <player>";
                if (args.length == 0) {
                    player.sendMessage(use);
                } else if (args.length >= 1) {
                    Player p = Bukkit.getPlayer((String)args[0]);
                    if (p != null) {
                        if (p.getUniqueId() == player.getUniqueId()) {
                            player.sendMessage("\u00a7eNon puoi ignorarti da solo.");
                            return true;
                        }
                        if (this.ignore(player.getUniqueId(), p.getUniqueId())) {
                            player.sendMessage("\u00a7eOra stai ignorando \u00a7c" + p.getName());
                        } else {
                            player.sendMessage("\u00a7eHai disignorato \u00a7b" + p.getName());
                        }
                    } else {
                        player.sendMessage("\u00a7ePlayer non trovato.");
                    }
                }
            } else if (cmd.getName().equalsIgnoreCase("unignore")) {
                if (this.plugin.cooldown.inCooldown(player.getUniqueId())) {
                    player.sendMessage("\u00a7eDevi aspettare 2 secondi prima di poter scrivere.");
                    return true;
                }
                this.plugin.cooldown.addCooldown(player.getUniqueId(), 40, null);
                String use = "\u00a7b/unignore <player>";
                if (args.length == 0) {
                    player.sendMessage(use);
                } else if (args.length >= 1) {
                    Player p = Bukkit.getPlayer((String)args[0]);
                    if (p != null) {
                        if (p.getUniqueId() == player.getUniqueId()) {
                            player.sendMessage("\u00a7eNon puoi disignorarti da solo.");
                            return true;
                        }
                        if (this.unignore(player.getUniqueId(), p.getUniqueId())) {
                            player.sendMessage("\u00a7eHai disignorato \u00a7b" + p.getName());
                        } else {
                            player.sendMessage("\u00a7eNon stai ignorando il player.");
                        }
                    } else {
                        player.sendMessage("\u00a7ePlayer not found.");
                    }
                }
            }
        } else {
            sender.sendMessage("Player only");
        }
        return true;
    }
}

