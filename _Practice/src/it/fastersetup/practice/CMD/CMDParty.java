/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package it.fastersetup.practice.CMD;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.API;
import it.fastersetup.practice.Api.Invs;
import it.fastersetup.practice.Api.Party;

public class CMDParty
implements CommandExecutor {
    private Main plugin;

    public CMDParty(Main pl) {
        this.plugin = pl;
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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.plugin.arenas.isInArena(player.getUniqueId())) {
                player.sendMessage("\u00a7eDisabilitato in game.");
                return true;
            }
            if (args.length == 0) {
                String[] msg = new String[]{"\u00a7b/party create", "\u00a7b/party leave", "\u00a7b/party invite <player>", "\u00a7b/party accept", "\u00a7b/party deny", "\u00a7b/party chat/c <msg>", "\u00a7b/party join <name>", "\u00a7b/party kick <player>"};
                API.sendAll(msg, player);
            } else if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 1) {
                    if (!this.plugin.mm.inQueue(player.getUniqueId())) {
                        if (!this.plugin.pman.inParty(player.getUniqueId())) {
                            if (!this.plugin.pman.waiting(player.getUniqueId())) {
                                this.plugin.pman.newParty(player.getUniqueId());
                            } else {
                                player.sendMessage("\u00a7eDevi aspettare 5 secondi dalla creazione di un party all'altro.");
                            }
                        } else {
                            player.sendMessage("\u00a7c\u00a7lSei gi\u00e0 in un party.");
                        }
                    } else {
                        player.sendMessage("\u00a7eSei in coda per il PvP.");
                    }
                } else {
                    player.sendMessage("\u00a7eUsa. \u00a7b/party create");
                }
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (args.length == 1) {
                    if (this.plugin.pman.inParty(player.getUniqueId())) {
                        if (this.plugin.arenas.isInArena(player.getUniqueId())) {
                            player.sendMessage("\u00a7eNon puoi uscire mentre sei in una arena.");
                        } else {
                            this.plugin.pman.eject(player.getUniqueId());
                            player.sendMessage("\u00a7eSei uscito dal party.");
                        }
                    } else {
                        player.sendMessage("\u00a7eNon sei in nessun party.");
                        player.getInventory().setContents(Invs.main(player.getName()));
                    }
                } else {
                    player.sendMessage("\u00a7eusa. \u00a7b/party leave");
                }
            } else if (args[0].equalsIgnoreCase("invite")) {
                if (args.length == 1) {
                    player.sendMessage("\u00a7eusa. \u00a7b/party invite <player>");
                } else if (args.length == 2) {
                    if (this.plugin.pman.hasParty(player.getUniqueId())) {
                        Player p = Bukkit.getPlayer((String)args[1]);
                        if (p != null) {
                            if (this.plugin.data.getSettings(p.getUniqueId()).partyEnabled()) {
                                if (this.plugin.mm.inQueue(p.getUniqueId())) {
                                    player.sendMessage("\u00a7eQuesto Player \u00e8 in coda.");
                                    return true;
                                }
                                if (!this.plugin.duels.hasRequest(player.getUniqueId())) {
                                    if (!this.plugin.ignore.isIgnoring(p.getUniqueId(), player.getUniqueId())) {
                                        if (!this.plugin.pman.inParty(p.getUniqueId())) {
                                            if (!this.plugin.pman.isRequesting(p.getUniqueId())) {
                                                if (!this.plugin.arenas.isInArena(p.getUniqueId())) {
                                                    this.plugin.pman.sendRequest(p.getUniqueId(), player.getUniqueId());
                                                    player.sendMessage("\u00a7bRichiesta inviata.");
                                                } else {
                                                    player.sendMessage("\u00a7eQuesto Player \u00e8 in arena.");
                                                }
                                            } else {
                                                player.sendMessage("\u00a7eQuesto player ha gi\u00e0 una richiesta party.");
                                            }
                                        } else {
                                            player.sendMessage("\u00a7ePlayer is already in a party.");
                                        }
                                    } else {
                                        player.sendMessage("\u00a7eNon puoi invitare questo player.");
                                    }
                                } else {
                                    player.sendMessage("\u00a7eQuesto player ha una richiesta di duello.");
                                }
                            } else {
                                player.sendMessage("\u00a7eQuesto Player ha i party disabilitati.");
                            }
                        } else {
                            player.sendMessage("\u00a7ePlayer non trovato.");
                        }
                    } else {
                        player.sendMessage("\u00a7eNon hai un party.");
                    }
                } else {
                    player.sendMessage("\u00a7eusa. \u00a7b/party invite <player>");
                }
            } else if (args[0].equalsIgnoreCase("accept")) {
                if (!this.plugin.pman.inParty(player.getUniqueId())) {
                    if (this.plugin.pman.isRequesting(player.getUniqueId())) {
                        if (!this.plugin.pman.accept(player.getUniqueId())) {
                            player.sendMessage("\u00a7eParty non trovato.");
                        }
                    } else {
                        player.sendMessage("\u00a7eNon hai richieste di party.");
                    }
                } else {
                    player.sendMessage("\u00a7eSei gi\u00e0 in un party.");
                }
            } else if (args[0].equalsIgnoreCase("deny")) {
                if (!this.plugin.pman.inParty(player.getUniqueId())) {
                    if (this.plugin.pman.isRequesting(player.getUniqueId())) {
                        if (!this.plugin.pman.deny(player.getUniqueId())) {
                            player.sendMessage("\u00a7eParty non trovato.");
                        } else {
                            player.sendMessage("\u00a7bRichiesta rifiutata.");
                        }
                    } else {
                        player.sendMessage("\u00a7eNon hai richieste party.");
                    }
                } else {
                    player.sendMessage("\u00a7eSei gi\u00e0 in un party.");
                }
            } else if (args[0].equalsIgnoreCase("chat") || args[0].equalsIgnoreCase("c")) {
                if (this.plugin.pman.inParty(player.getUniqueId())) {
                    String use = "\u00a7b/party chat/c <msg>";
                    if (args.length == 1) {
                        player.sendMessage(use);
                    } else if (args.length >= 2) {
                        if (!this.plugin.arenas.isInArena(player.getUniqueId())) {
                            String msg = ChatColor.stripColor((String)ChatColor.translateAlternateColorCodes((char)'&', (String)CMDParty.combine(1, args)));
                            this.plugin.pman.getParty(this.plugin.pman.getLeader(player.getUniqueId())).msgAll("\u00a75[\u00a7fParty\u00a75] " + player.getName() + " \u00a7d> \u00a7f" + msg);
                        } else {
                            player.sendMessage("\u00a7eDisabilitato in game.");
                        }
                    }
                } else {
                    player.sendMessage("\u00a7eYou are not in a party.");
                }
            } else if (args[0].equalsIgnoreCase("join")) {
                if (!this.plugin.pman.inParty(player.getUniqueId())) {
                    String use = "\u00a7b/party join <name>";
                    if (args.length == 1) {
                        player.sendMessage(use);
                    } else if (args.length > 1) {
                        Player p = Bukkit.getPlayer((String)args[1]);
                        if (p != null) {
                            if (this.plugin.mm.inQueue(player.getUniqueId())) {
                                player.sendMessage("\u00a7eSei in coda.");
                                return true;
                            }
                            if (!this.plugin.ignore.isIgnoring(p.getUniqueId(), player.getUniqueId())) {
                                if (this.plugin.pman.inParty(p.getUniqueId())) {
                                    Party party = this.plugin.pman.getParty(this.plugin.pman.getLeader(p.getUniqueId()));
                                    if (party.isOpen()) {
                                        this.plugin.pman.add(party, player.getUniqueId());
                                    } else {
                                        player.sendMessage("\u00a7eQuesto Party non \u00e8 pubblico.");
                                    }
                                } else {
                                    player.sendMessage("\u00a7eQuesto Player non \u00e8 nel party.");
                                }
                            } else {
                                player.sendMessage("\u00a7eNon puoi entrare nel party di questo player.");
                            }
                        } else {
                            player.sendMessage("\u00a7ePlayer non trovato.");
                        }
                    }
                } else {
                    player.sendMessage("\u00a7eSei gi\u00e0 in un party.");
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (args.length == 1) {
                    player.sendMessage("\u00a7b/party kick <player>");
                } else if (args.length > 1) {
                    if (this.plugin.pman.inParty(player.getUniqueId())) {
                        if (this.plugin.pman.hasParty(player.getUniqueId())) {
                            Player p = Bukkit.getPlayer((String)args[1]);
                            if (p != null) {
                                if (this.plugin.pman.getParty(player.getUniqueId()).contains(p.getUniqueId())) {
                                    this.plugin.pman.eject(p.getUniqueId());
                                    player.sendMessage("\u00a7bHai kickato\u00a7a" + p.getName() + " \u00a7bdal party.");
                                } else {
                                    player.sendMessage("\u00a7eQuesto player non \u00e8 nel tuo party.");
                                }
                            } else {
                                player.sendMessage("\u00a7ePlayer non trovato.");
                            }
                        } else {
                            player.sendMessage("\u00a7eNon hai un party.");
                        }
                    } else {
                        player.sendMessage("\u00a7eNon sei in nessun party.");
                    }
                }
            } else {
                player.sendMessage("\u00a7eUnknown argument.");
            }
        } else {
            sender.sendMessage("That command is player only.");
        }
        return true;
    }
}

