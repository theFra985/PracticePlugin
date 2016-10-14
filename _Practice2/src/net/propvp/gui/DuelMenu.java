/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.propvp.gui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import mkremins.fanciful.FancyMessage;
import net.propvp.Practice;
import net.propvp.game.GameManager;
import net.propvp.game.GameMode;
import net.propvp.game.arena.Arena;
import net.propvp.game.arena.ArenaManager;
import net.propvp.game.duel.DuelInfo;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import net.propvp.util.IconMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DuelMenu {
    private static Map<Player, GameMode> pickedGame = new HashMap<Player, GameMode>();
    private static Map<Player, Arena> pickedArena = new HashMap<Player, Arena>();

    public static void openPickGame(Player player, final Player player2) {
        IconMenu iconMenu = new IconMenu((Object)ChatColor.GOLD + "Dueling", 18, optionClickEvent -> {
            if (optionClickEvent.getPlayer() != player) {
                return;
            }
            GameMode gameMode = Practice.getBackend().getGameManager().getGameMode(optionClickEvent.getName());
            pickedGame.put(player, gameMode);
            optionClickEvent.setWillClose(true);
            optionClickEvent.setWillDestroy(true);
            new BukkitRunnable(){

                public void run() {
                    DuelMenu.openPickArena(Player.this, player2);
                }
            }.runTaskLater((Plugin)Practice.getInstance(), 5);
        }
        , player);
        int n = 0;
        for (GameMode gameMode : Practice.getBackend().getGameManager().getGameModes().values()) {
            iconMenu.setOption(n, gameMode.getDisplay(), gameMode.getDisplayName(), (Object)ChatColor.GRAY + "Click to select this game-mode.");
            ++n;
        }
        iconMenu.open(player);
    }

    public static void openPickArena(Player player, Player player2) {
        if (Practice.getBackend().getArenaManager().getArenas().isEmpty()) {
            player.sendMessage((Object)ChatColor.RED + "Could not continue duel invintation because there are no arenas.");
            return;
        }
        HashMap<Integer, Arena> hashMap = new HashMap<Integer, Arena>();
        IconMenu iconMenu = new IconMenu((Object)ChatColor.GOLD + "Dueling", 18, optionClickEvent -> {
            if (optionClickEvent.getPlayer() != player) {
                return;
            }
            Arena arena = (Arena)hashMap.get(optionClickEvent.getPosition());
            pickedArena.put(player, arena);
            PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
            DuelInfo duelInfo = new DuelInfo(pickedGame.get((Object)player), pickedArena.get((Object)player), player, player2);
            playerData.addDuelInvite(player2, duelInfo);
            if (duelInfo.isParty()) {
                new FancyMessage("Duel").color(ChatColor.RED).then(" \u00bb ").color(ChatColor.DARK_GRAY).then("Party duel invitation from " + player.getName() + "'s party! ").color(ChatColor.GRAY).then("ACCEPT").style(ChatColor.BOLD).color(ChatColor.GREEN).command("/duel accept " + player.getName()).send(player2);
            } else {
                new FancyMessage("Duel").color(ChatColor.RED).then(" \u00bb ").color(ChatColor.DARK_GRAY).then("You have been invited to a duel by " + player.getName() + "! ").color(ChatColor.GRAY).then("ACCEPT").style(ChatColor.BOLD).color(ChatColor.GREEN).command("/duel accept " + player.getName()).send(player2);
            }
            player.sendMessage((Object)ChatColor.RED + "Duel" + (Object)ChatColor.DARK_GRAY + " \u00bb " + (Object)ChatColor.GRAY + "Sent invite to " + player2.getName());
            optionClickEvent.getPlayer().closeInventory();
            optionClickEvent.setWillClose(true);
            optionClickEvent.setWillDestroy(true);
        }
        , player);
        int n = 0;
        for (Arena arena : Practice.getBackend().getArenaManager().getArenas().values()) {
            if (arena.inUse() || !arena.isSetup()) continue;
            if (n == 19) break;
            hashMap.put(n, arena);
            iconMenu.setOption(n, new ItemStack(Material.STONE), (Object)ChatColor.GREEN + arena.getName(), (Object)ChatColor.GRAY + "Click to select this arena.");
            ++n;
        }
        iconMenu.open(player);
    }

}

