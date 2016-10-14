/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.PluginManager
 */
package net.propvp.command;

import java.util.List;
import net.propvp.Practice;
import net.propvp.event.PartyCreateEvent;
import net.propvp.event.PartyDisbandEvent;
import net.propvp.event.PartyInviteEvent;
import net.propvp.event.PartyJoinEvent;
import net.propvp.event.PartyKickEvent;
import net.propvp.event.PartyLeaveEvent;
import net.propvp.party.Party;
import net.propvp.party.PartyManager;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

public class PartyCommand
implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player)commandSender;
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (playerData.hasMatch()) {
            player.sendMessage((Object)ChatColor.RED + "This command cannot be used while in a match.");
            return true;
        }
        if (arrstring.length == 0) {
            player.sendMessage((Object)ChatColor.RED + " Party Commands");
            player.sendMessage((Object)ChatColor.GRAY + "/party create");
            player.sendMessage((Object)ChatColor.GRAY + "/party invite <player>");
            player.sendMessage((Object)ChatColor.GRAY + "/party kick <player>");
            player.sendMessage((Object)ChatColor.GRAY + "/party join <player invited by>");
            player.sendMessage((Object)ChatColor.GRAY + "/party leave");
            player.sendMessage((Object)ChatColor.GRAY + "/party info");
            player.sendMessage((Object)ChatColor.GRAY + "/party disband");
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("help")) {
            player.sendMessage((Object)ChatColor.RED + " Party Commands");
            player.sendMessage((Object)ChatColor.GRAY + "/party create");
            player.sendMessage((Object)ChatColor.GRAY + "/party invite <player>");
            player.sendMessage((Object)ChatColor.GRAY + "/party kick <player>");
            player.sendMessage((Object)ChatColor.GRAY + "/party join <player invited by>");
            player.sendMessage((Object)ChatColor.GRAY + "/party leave");
            player.sendMessage((Object)ChatColor.GRAY + "/party info");
            player.sendMessage((Object)ChatColor.GRAY + "/party disband");
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("create")) {
            if (playerData.hasParty()) {
                player.sendMessage((Object)ChatColor.RED + "You already have a party.");
                return true;
            }
            PartyCreateEvent partyCreateEvent = new PartyCreateEvent(player);
            Bukkit.getServer().getPluginManager().callEvent((Event)partyCreateEvent);
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("invite")) {
            if (!playerData.hasParty()) {
                player.sendMessage((Object)ChatColor.RED + "You do not have a party.");
                return true;
            }
            if (arrstring.length == 1) {
                player.sendMessage((Object)ChatColor.RED + "You did not specify a player to invite.");
                return true;
            }
            if (Bukkit.getPlayer((String)arrstring[1]) == null) {
                player.sendMessage((Object)ChatColor.RED + "That player is not online.");
                return true;
            }
            if (playerData.getParty().getLeader() != player) {
                player.sendMessage((Object)ChatColor.RED + "You must be the leader to invite a player.");
                return true;
            }
            PartyInviteEvent partyInviteEvent = new PartyInviteEvent(Bukkit.getPlayer((String)arrstring[1]), playerData.getParty());
            Bukkit.getServer().getPluginManager().callEvent((Event)partyInviteEvent);
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("join")) {
            if (playerData.hasParty()) {
                player.sendMessage((Object)ChatColor.RED + "You already have a party.");
                return true;
            }
            if (playerData.hasMatch() || playerData.hasQueue() || playerData.isEditing() || playerData.isSpectating()) {
                player.sendMessage((Object)ChatColor.RED + "You are busy and cannot do that right now.");
                return true;
            }
            if (arrstring.length == 1) {
                player.sendMessage((Object)ChatColor.RED + "You did not specify a party to join.");
                return true;
            }
            if (!Practice.getBackend().getPartyManager().isParty(Bukkit.getPlayer((String)arrstring[1]))) {
                player.sendMessage((Object)ChatColor.RED + "That party is invalid.");
                return true;
            }
            if (!Practice.getBackend().getPartyManager().getParty(Bukkit.getPlayer((String)arrstring[1])).hasInvite(player)) {
                player.sendMessage((Object)ChatColor.RED + "You have not been invited to that party.");
                return true;
            }
            PartyJoinEvent partyJoinEvent = new PartyJoinEvent(player, Practice.getBackend().getPartyManager().getParty(Bukkit.getPlayer((String)arrstring[1])));
            Bukkit.getServer().getPluginManager().callEvent((Event)partyJoinEvent);
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("kick")) {
            if (!playerData.hasParty()) {
                player.sendMessage((Object)ChatColor.RED + "You do not have a party.");
                return true;
            }
            if (arrstring.length == 1) {
                player.sendMessage((Object)ChatColor.RED + "You did not specify a player to kick.");
                return true;
            }
            if (Bukkit.getPlayer((String)arrstring[1]) == null) {
                player.sendMessage((Object)ChatColor.RED + "That player is not online.");
                return true;
            }
            if (playerData.getParty().getLeader() != player) {
                player.sendMessage((Object)ChatColor.RED + "You must be the leader to kick a player.");
                return true;
            }
            PartyKickEvent partyKickEvent = new PartyKickEvent(player, Bukkit.getPlayer((String)arrstring[1]), playerData.getParty());
            Bukkit.getServer().getPluginManager().callEvent((Event)partyKickEvent);
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("leave")) {
            if (!playerData.hasParty()) {
                player.sendMessage((Object)ChatColor.RED + "You do not have a party.");
                return true;
            }
            Party party = playerData.getParty();
            if (party.getLeader().equals((Object)player)) {
                player.sendMessage((Object)ChatColor.RED + "You have to disband your party to leave.");
                return true;
            }
            PartyLeaveEvent partyLeaveEvent = new PartyLeaveEvent(player, playerData.getParty());
            Bukkit.getServer().getPluginManager().callEvent((Event)partyLeaveEvent);
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("info")) {
            if (!playerData.hasParty()) {
                player.sendMessage((Object)ChatColor.RED + "You do not have a party.");
                return true;
            }
            Party party = playerData.getParty();
            if (party == null) {
                player.sendMessage((Object)ChatColor.RED + "An error occured while trying to fecth party information.");
                Practice.getLog().log("A player tried checking their party information and has a party but the party is null. This might be caused by a server reload.", true);
                return true;
            }
            player.sendMessage((Object)ChatColor.GOLD + "Party Information:");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((Object)ChatColor.GRAY + " \u00bb Members: ");
            if (party.getMembers().isEmpty()) {
                stringBuilder.append((Object)ChatColor.GRAY + "None");
            } else {
                int n = party.getMembers().size();
                int n2 = 0;
                for (Player player2 : party.getMembers()) {
                    if (n != n2) {
                        stringBuilder.append((Object)ChatColor.GRAY + player2.getName() + ", ");
                        continue;
                    }
                    stringBuilder.append((Object)ChatColor.GRAY + player2.getName());
                }
            }
            player.sendMessage(stringBuilder.toString());
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("disband")) {
            if (!playerData.hasParty()) {
                player.sendMessage((Object)ChatColor.RED + "You do not have a party.");
                return true;
            }
            if (playerData.getParty().getLeader() != player) {
                player.sendMessage((Object)ChatColor.RED + "You must be the leader to disband the party.");
                return true;
            }
            PartyDisbandEvent partyDisbandEvent = new PartyDisbandEvent(player, playerData.getParty());
            Bukkit.getServer().getPluginManager().callEvent((Event)partyDisbandEvent);
            return true;
        }
        player.sendMessage((Object)ChatColor.RED + "Unknown sub-command.");
        return true;
    }
}

