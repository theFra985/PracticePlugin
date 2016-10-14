/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.PluginManager
 */
package net.propvp.listener;

import java.util.List;
import mkremins.fanciful.FancyMessage;
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
import net.propvp.player.InventoryManager;
import net.propvp.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;

public class PartyListener
implements Listener {
    @EventHandler
    public void onPartyItem(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_AIR && playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        if (player.getItemInHand().equals((Object)InventoryManager.getPartyInfoItem())) {
            player.performCommand("party info");
            playerInteractEvent.setCancelled(true);
        } else if (player.getItemInHand().equals((Object)InventoryManager.getPartyLeaveItem())) {
            player.performCommand("party leave");
            playerInteractEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (playerData.hasParty()) {
            if (playerData.getParty() == null) {
                return;
            }
            if (playerData.getParty().getLeader() == player) {
                PartyDisbandEvent partyDisbandEvent = new PartyDisbandEvent(player, playerData.getParty());
                Bukkit.getServer().getPluginManager().callEvent((Event)partyDisbandEvent);
            } else {
                PartyLeaveEvent partyLeaveEvent = new PartyLeaveEvent(playerQuitEvent.getPlayer(), playerData.getParty());
                Bukkit.getServer().getPluginManager().callEvent((Event)partyLeaveEvent);
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent playerKickEvent) {
        Player player = playerKickEvent.getPlayer();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (playerData.hasParty()) {
            if (playerData.getParty().getLeader() == player) {
                PartyDisbandEvent partyDisbandEvent = new PartyDisbandEvent(player, playerData.getParty());
                Bukkit.getServer().getPluginManager().callEvent((Event)partyDisbandEvent);
            } else {
                PartyLeaveEvent partyLeaveEvent = new PartyLeaveEvent(playerKickEvent.getPlayer(), playerData.getParty());
                Bukkit.getServer().getPluginManager().callEvent((Event)partyLeaveEvent);
            }
        }
    }

    @EventHandler
    public void onPartyCreate(PartyCreateEvent partyCreateEvent) {
        partyCreateEvent.getPlayer().sendMessage((Object)ChatColor.GRAY + "You have created a party.");
        partyCreateEvent.getPlayer().getInventory().setContents(InventoryManager.getPartyLeaderInventory(partyCreateEvent.getPlayer()));
        partyCreateEvent.getPlayer().updateInventory();
        Party party = new Party(partyCreateEvent.getPlayer());
        Practice.getBackend().getDataManager().getData(partyCreateEvent.getPlayer()).setParty(party);
        Practice.getBackend().getPartyManager().addSet(partyCreateEvent.getPlayer(), party);
    }

    @EventHandler
    public void onPartyInvite(PartyInviteEvent partyInviteEvent) {
        partyInviteEvent.getParty().addInvite(partyInviteEvent.getPlayer());
        partyInviteEvent.getParty().sendMessage((Object)ChatColor.LIGHT_PURPLE + partyInviteEvent.getPlayer().getName() + (Object)ChatColor.GRAY + " has been invited to join the party.");
        partyInviteEvent.getPlayer().sendMessage((Object)ChatColor.GRAY + "[" + (Object)ChatColor.DARK_PURPLE + "Party" + (Object)ChatColor.GRAY + "] " + (Object)ChatColor.GRAY + "You've been invited to join " + (Object)ChatColor.LIGHT_PURPLE + partyInviteEvent.getParty().getLeader().getName() + (Object)ChatColor.GRAY + "'s party.");
        new FancyMessage("[").color(ChatColor.GRAY).then("Party").color(ChatColor.DARK_PURPLE).then("] ").color(ChatColor.GRAY).then("Click HERE to join the party.").command("/party join " + partyInviteEvent.getParty().getLeader().getName()).color(ChatColor.GREEN).send(partyInviteEvent.getPlayer());
    }

    @EventHandler
    public void onPartyJoin(PartyJoinEvent partyJoinEvent) {
        Practice.getBackend().getDataManager().getData(partyJoinEvent.getPlayer()).setParty(partyJoinEvent.getParty());
        partyJoinEvent.getPlayer().getInventory().setContents(InventoryManager.getPartyMemberInventory(partyJoinEvent.getPlayer()));
        partyJoinEvent.getPlayer().updateInventory();
        partyJoinEvent.getParty().addMember(partyJoinEvent.getPlayer());
        partyJoinEvent.getParty().removeInvite(partyJoinEvent.getPlayer());
        partyJoinEvent.getParty().sendMessage((Object)ChatColor.LIGHT_PURPLE + partyJoinEvent.getPlayer().getName() + (Object)ChatColor.GRAY + " has joined the party.");
    }

    @EventHandler
    public void onPartyLeave(PartyLeaveEvent partyLeaveEvent) {
        Practice.getBackend().getDataManager().getData(partyLeaveEvent.getPlayer()).setParty(null);
        partyLeaveEvent.getPlayer().getInventory().setContents(InventoryManager.getSoloInventory(partyLeaveEvent.getPlayer()));
        partyLeaveEvent.getPlayer().updateInventory();
        partyLeaveEvent.getParty().removeMember(partyLeaveEvent.getPlayer());
        partyLeaveEvent.getParty().sendMessage((Object)ChatColor.LIGHT_PURPLE + partyLeaveEvent.getPlayer().getName() + (Object)ChatColor.GRAY + " has left the party.");
    }

    @EventHandler
    public void onPartyKick(PartyKickEvent partyKickEvent) {
        Practice.getBackend().getDataManager().getData(partyKickEvent.getKicked()).setParty(null);
        partyKickEvent.getKicked().getInventory().setContents(InventoryManager.getSoloInventory(partyKickEvent.getKicked()));
        partyKickEvent.getKicked().updateInventory();
        partyKickEvent.getParty().removeMember(partyKickEvent.getKicked());
        partyKickEvent.getParty().sendMessage((Object)ChatColor.LIGHT_PURPLE + partyKickEvent.getKicked().getName() + (Object)ChatColor.GRAY + " has been kicked from the party by " + (Object)ChatColor.LIGHT_PURPLE + partyKickEvent.getParty().getLeader().getName() + (Object)ChatColor.GRAY + ".");
    }

    @EventHandler
    public void onPartyDisband(PartyDisbandEvent partyDisbandEvent) {
        for (Player player : partyDisbandEvent.getParty().getMembers()) {
            Practice.getBackend().getDataManager().getData(player).setParty(null);
            player.getInventory().setContents(InventoryManager.getSoloInventory(player));
            player.updateInventory();
        }
        Practice.getBackend().getDataManager().getData(partyDisbandEvent.getPlayer()).setParty(null);
        partyDisbandEvent.getPlayer().getInventory().setContents(InventoryManager.getSoloInventory(partyDisbandEvent.getPlayer()));
        partyDisbandEvent.getPlayer().updateInventory();
        partyDisbandEvent.getParty().sendMessage((Object)ChatColor.LIGHT_PURPLE + partyDisbandEvent.getParty().getLeader().getName() + (Object)ChatColor.GRAY + " has disbanded the party.");
        partyDisbandEvent.getParty().disband();
        partyDisbandEvent.getParty().clean();
    }
}

