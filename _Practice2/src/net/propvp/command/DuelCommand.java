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
 */
package net.propvp.command;

import net.propvp.Practice;
import net.propvp.game.GameMode;
import net.propvp.game.arena.Arena;
import net.propvp.game.duel.Duel;
import net.propvp.game.duel.DuelInfo;
import net.propvp.gui.DuelMenu;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand
implements CommandExecutor {
    private Player player;
    private PlayerData data;
    private String[] args;

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players may execute this command.");
            return true;
        }
        this.player = (Player)commandSender;
        this.data = Practice.getBackend().getDataManager().getData(this.player);
        this.args = arrstring;
        if (this.data.hasMatch()) {
            this.player.sendMessage((Object)ChatColor.RED + "This command cannot be used while in a match.");
            return true;
        }
        if (arrstring.length == 0) {
            this.player.sendMessage((Object)ChatColor.RED + "Correct use: /duel <player|party leader>");
            return true;
        }
        if (arrstring[0].equalsIgnoreCase("accept")) {
            this.accept();
            return true;
        }
        this.invite();
        return true;
    }

    public void invite() {
        if (this.player.getName().equalsIgnoreCase(this.args[0])) {
            this.player.sendMessage((Object)ChatColor.RED + "You cannot duel yourself.");
            return;
        }
        if (Bukkit.getPlayer((String)this.args[0]) == null) {
            this.player.sendMessage((Object)ChatColor.RED + "That player is not online or cannot be found.");
            return;
        }
        if (this.data.isEditing() || this.data.isSpectating() || this.data.hasMatch() || this.data.hasQueue() || this.data.hasParty()) {
            this.player.sendMessage((Object)ChatColor.RED + "You cannot duel someone if you are editing, spectating, have a match, a queue, or a party.");
            return;
        }
        Player player = Bukkit.getPlayer((String)this.args[0]);
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (this.data.hasDuelInvite(player)) {
            this.player.sendMessage((Object)ChatColor.RED + "That player has already been invited by you.");
            return;
        }
        if (playerData.isEditing() || this.data.isSpectating() || this.data.hasMatch() || this.data.hasQueue()) {
            this.player.sendMessage((Object)ChatColor.RED + "That player is currently busy.");
            return;
        }
        DuelMenu.openPickGame(this.player, player);
    }

    public void accept() {
        if (this.data.isEditing() || this.data.isSpectating() || this.data.hasMatch() || this.data.hasQueue() || this.data.hasParty()) {
            this.player.sendMessage((Object)ChatColor.RED + "You cannot duel someone if you are editing, spectating, have a match, a queue, or a party.");
            return;
        }
        if (this.args.length == 1) {
            this.player.sendMessage((Object)ChatColor.RED + "You didn't specify which duel invitation to accept.");
            return;
        }
        if (Bukkit.getPlayer((String)this.args[1]) == null) {
            this.player.sendMessage((Object)ChatColor.RED + "That player is not online or cannot be found.");
            return;
        }
        Player player = Bukkit.getPlayer((String)this.args[1]);
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (playerData.isEditing() || playerData.isSpectating() || playerData.hasMatch() || playerData.hasQueue() || playerData.hasParty()) {
            this.player.sendMessage((Object)ChatColor.RED + "That player is currently busy.");
            return;
        }
        if (!playerData.hasDuelInvite(this.player)) {
            this.player.sendMessage((Object)ChatColor.RED + "This player did not invite you to a duel.");
            return;
        }
        DuelInfo duelInfo = playerData.getDuelInfo(this.player);
        Duel duel = new Duel(duelInfo.getArena(), duelInfo.getGameMode(), (Object)duelInfo.getPlayer1(), (Object)duelInfo.getPlayer2());
        duel.startMatch();
        playerData.removeDuelInvite(this.player);
    }
}

