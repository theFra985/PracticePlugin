/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 */
package it.fastersetup.practice.CMD;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.Invs;

public class CMDDuel
implements CommandExecutor {
    private Main plugin;

    public CMDDuel(Main pl) {
        this.plugin = pl;
    }

    private void chooseGame(UUID id, UUID opp, boolean party) {
        this.plugin.duels.prepare(id, opp, party);
        Bukkit.getPlayer((UUID)id).openInventory(Invs.pickGame());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.plugin.arenas.isInArena(player.getUniqueId())) {
                player.sendMessage("\u00a7eDisabilitato in game.");
                return true;
            }
            if (args.length == 0) {
                player.sendMessage("\u00a7b/duel <player>");
            } else if (args.length >= 1) {
                if (this.plugin.pman.inParty(player.getUniqueId()) && !this.plugin.pman.hasParty(player.getUniqueId())) {
                    player.sendMessage("\u00a7cSolo il leader pu\u00f2 inviare richieste.");
                    return true;
                }
                Player p = Bukkit.getPlayer((String)args[0]);
                if (p != null) {
                    if (player.getUniqueId() == p.getUniqueId()) {
                        player.sendMessage("\u00a7eNon puoi duellarti da solo.");
                        return true;
                    }
                    if (this.plugin.mm.inQueue(player.getUniqueId())) {
                        player.sendMessage("\u00a7eNon puoi duellare mentre sei in coda.");
                        return true;
                    }
                    if (this.plugin.mm.inQueue(p.getUniqueId())) {
                        player.sendMessage("\u00a7eIl player \u00e8 in coda.");
                        return true;
                    }
                    if (this.plugin.duels.hasRequest(player.getUniqueId())) {
                        player.sendMessage("\u00a7eHai gi\u00e0 una richiesta attiva.");
                        return true;
                    }
                    if (this.plugin.duels.hasRequest(p.getUniqueId())) {
                        player.sendMessage("\u00a7eil Player ha una richiesta attiva");
                    }
                    if (this.plugin.data.getSettings(p.getUniqueId()).duelEnabled()) {
                        if (!this.plugin.ignore.isIgnoring(p.getUniqueId(), player.getUniqueId())) {
                            if (!this.plugin.mm.inQueue(p.getUniqueId())) {
                                if (!this.plugin.arenas.isInArena(p.getUniqueId())) {
                                    if (!this.plugin.duels.hasRequest(p.getUniqueId())) {
                                        if (!this.plugin.pman.isRequesting(p.getUniqueId())) {
                                            if (this.plugin.pman.inParty(player.getUniqueId())) {
                                                if (this.plugin.pman.inParty(p.getUniqueId())) {
                                                    int d = Math.abs(this.plugin.pman.size(player.getUniqueId()) - this.plugin.pman.size(p.getUniqueId()));
                                                    if (d < 1) {
                                                        this.chooseGame(player.getUniqueId(), this.plugin.pman.getLeader(p.getUniqueId()), true);
                                                    } else {
                                                        player.sendMessage("\u00a7eIl Party deve contenere almeno 2 player.");
                                                    }
                                                } else {
                                                    player.sendMessage("\u00a7eBoth players must be in a party.");
                                                }
                                            } else if (!this.plugin.pman.inParty(p.getUniqueId())) {
                                                this.chooseGame(player.getUniqueId(), p.getUniqueId(), false);
                                            } else {
                                                player.sendMessage("\u00a7eL'altro player \u00e8 in un party,Per duellarlo devi essere anche tu in un party");
                                            }
                                        } else {
                                            player.sendMessage("\u00a7eQuesto Player ha un party.");
                                        }
                                    } else {
                                        player.sendMessage("\u00a7eQuesto player ha gi\u00e0 una richiesta di duello.");
                                    }
                                } else {
                                    player.sendMessage("\u00a7eQueasto Player \u00e8 in un duello.");
                                }
                            } else {
                                player.sendMessage("\u00a7eQuesto Player \u00e8 in coda");
                            }
                        } else {
                            player.sendMessage("\u00a7eNon puoi inviare richieste a questo player.");
                        }
                    } else {
                        player.sendMessage("\u00a7eQuesto Player ha i duelli disabilitati.");
                    }
                } else {
                    player.sendMessage("\u00a7ePlayer non trovato.");
                }
            } else {
                player.sendMessage("\u00a7b/duel <name>");
            }
        } else {
            sender.sendMessage("Command is player only.");
        }
        return true;
    }
}

