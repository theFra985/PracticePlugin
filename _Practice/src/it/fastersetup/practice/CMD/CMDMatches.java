/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
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
import it.fastersetup.practice.Api.ConfigLoader;
import it.fastersetup.practice.Api.Rank;

public class CMDMatches
implements CommandExecutor {
    private ConfigLoader config;
    private HashMap<UUID, Integer> map = new HashMap<UUID, Integer>();
    private Main plugin;

    public CMDMatches(Main pl) {
        this.plugin = pl;
        this.config = new ConfigLoader(pl, "giftmatches.yml");
        this.config.generateFile();
        for (String s : this.config.getConfig().getConfigurationSection("").getKeys(false)) {
            if (s.isEmpty()) continue;
            UUID id = UUID.fromString(s);
            int a = this.config.getConfig().getInt(s);
            this.map.put(id, a);
        }
    }

    public void set(UUID id, int a) {
        this.map.put(id, a);
        this.config.set(id.toString(), a);
    }

    public int get(UUID id) {
        if (this.map.containsKey(id)) {
            return this.map.get(id);
        }
        return 0;
    }

    public void remove(UUID id, int a) {
        if (this.map.containsKey(id)) {
            int i = this.map.get(id);
            if ((i -= a) < 0) {
                i = 0;
            }
            this.map.put(id, i);
            this.config.set(id.toString(), i);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String use = "\u00a7b/matches give <player> <amount>";
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage(use);
                player.sendMessage("\u00a7eMatches to give: \u00a7a" + this.get(player.getUniqueId()));
            } else if (args[0].equalsIgnoreCase("give")) {
                if (args.length <= 2) {
                    player.sendMessage(use);
                } else if (args.length == 3) {
                    Player p = Bukkit.getPlayer((String)args[1]);
                    if (p != null) {
                        String n = args[2].replaceAll("[^0-9]", "");
                        if (!n.isEmpty()) {
                            int a = Integer.parseInt(n);
                            if (a > 0) {
                                if (this.get(player.getUniqueId()) >= a) {
                                    if (this.plugin.ranks.getRank(p.getUniqueId()) != Rank.DEFAULT) {
                                        this.remove(player.getUniqueId(), a);
                                        this.plugin.match.add(p.getUniqueId(), a);
                                        if (a == 1) {
                                            player.sendMessage("\u00a7eHai inviato \u00a7b1 \u00a7ematch a \u00a7a" + p.getName() + "\u00a7e.");
                                            p.sendMessage("\u00a7eHai ricevuto \u00a7b1 \u00a7ematch da \u00a7a" + player.getName() + "\u00a7e.");
                                        } else {
                                            player.sendMessage("\u00a7eHai inviato \u00a7b" + a + " \u00a7ematches to \u00a7a" + p.getName() + "\u00a7e.");
                                            p.sendMessage("\u00a7eHai rivevuto \u00a7b" + a + " \u00a7ematches da \u00a7a" + player.getName() + "\u00a7e.");
                                        }
                                    } else {
                                        player.sendMessage("\u00a7a" + p.getName() + " \u00a7eha i match infiniti.");
                                    }
                                } else {
                                    player.sendMessage("\u00a7eNon hai abbastanza match disponibil.");
                                }
                            } else {
                                player.sendMessage("\u00a7eIl valore deve essere maggiore di 0.");
                            }
                        } else {
                            player.sendMessage("\u00a7eDevi inserire un numero.");
                        }
                    } else {
                        player.sendMessage("\u00a7ePlayer non trovato.");
                    }
                } else {
                    player.sendMessage(use);
                }
            } else {
                player.sendMessage(use);
            }
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
                            if (this.plugin.ranks.getRank(p.getUniqueId()) != Rank.DEFAULT) {
                                this.plugin.match.add(p.getUniqueId(), a);
                                if (a == 1) {
                                    sender.sendMessage("Givvato 1 match a " + p.getName() + ".");
                                    p.sendMessage("\u00a7eHai ricevuto \u00a7b1 \u00a7ematch per \u00a7avoting\u00a7e.");
                                } else {
                                    sender.sendMessage("Given " + a + " matches to " + p.getName() + ".");
                                    p.sendMessage("\u00a7eHai ricevuto \u00a7b" + a + " \u00a7ematches per \u00a7avoting\u00a7e.");
                                }
                            } else {
                                sender.sendMessage("\u00a7a" + p.getName() + " \u00a7eha i match infiti.");
                            }
                        } else {
                            sender.sendMessage("\u00a7eIl valore deve essere maggiore di 0.");
                        }
                    } else {
                        sender.sendMessage("\u00a7eDevi inserire un numero.");
                    }
                } else {
                    sender.sendMessage("Player non trovato.");
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

