/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package net.propvp.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.propvp.Practice;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Party
implements Listener {
    private Player leader;
    private List<Player> members;
    private List<UUID> invites;
    private String prefix;

    public Party(Player player) {
        this.leader = player;
        this.members = new ArrayList<Player>();
        this.invites = new ArrayList<UUID>();
        this.prefix = (Object)ChatColor.GRAY + "[" + (Object)ChatColor.DARK_PURPLE + "Party" + (Object)ChatColor.GRAY + "] ";
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Practice.getInstance());
    }

    public Player getLeader() {
        return this.leader;
    }

    public List<Player> getMembers() {
        return this.members;
    }

    public void addInvite(Player player) {
        if (this.invites.contains(player.getUniqueId())) {
            return;
        }
        this.invites.add(player.getUniqueId());
    }

    public void removeInvite(Player player) {
        if (!this.invites.contains((Object)player)) {
            return;
        }
        this.invites.remove(player.getUniqueId());
    }

    public boolean hasInvite(Player player) {
        return this.invites.contains(player.getUniqueId());
    }

    public void addMember(Player player) {
        if (this.members.contains((Object)player)) {
            return;
        }
        this.members.add(player);
    }

    public void removeMember(Player player) {
        if (!this.members.contains((Object)player)) {
            return;
        }
        this.members.remove((Object)player);
    }

    public void disband() {
        this.members.clear();
        this.invites.clear();
        this.members = null;
        this.invites = null;
        this.leader = null;
    }

    public void clean() {
        HandlerList.unregisterAll((Listener)this);
    }

    public void sendMessage(String string) {
        this.leader.sendMessage(String.valueOf(this.prefix) + (Object)ChatColor.GRAY + string);
        if (this.members != null) {
            for (Player player : this.members) {
                player.sendMessage(String.valueOf(this.prefix) + (Object)ChatColor.GRAY + string);
            }
        }
    }
}

