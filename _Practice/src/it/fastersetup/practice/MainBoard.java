/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.scoreboard.Scoreboard
 */
package it.fastersetup.practice;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.Rank;
import it.fastersetup.practice.Api.SBoard;

public class MainBoard {
    private HashMap<UUID, SBoard> map = new HashMap<UUID, SBoard>();
    private Main plugin;

    public MainBoard(Main pl) {
        this.plugin = pl;
    }

    public void show(Player player) {
        if (this.map.containsKey(player.getUniqueId())) {
            SBoard board = this.map.get(player.getUniqueId());
            boolean c = false;
            if (!board.equals(14, player.getName())) {
                c = true;
                board.replace(14, player.getName());
            }
            if (!board.equals(11, String.valueOf(this.plugin.coins.get(player.getUniqueId())))) {
                c = true;
                board.replace(11, String.valueOf(this.plugin.coins.get(player.getUniqueId())));
            }
            if (this.plugin.ranks.atLeast(player.getUniqueId(), Rank.DONATOR)) {
                if (!board.equals(8, "Infinite")) {
                    c = true;
                    board.replace(8, "Infinite");
                }
            } else if (!board.equals(8, String.valueOf(this.plugin.match.get(player.getUniqueId())))) {
                c = true;
                board.replace(8, String.valueOf(this.plugin.match.get(player.getUniqueId())));
            }
            if (!board.equals(5, this.getRank(player.getUniqueId()))) {
                c = true;
                board.replace(5, this.getRank(player.getUniqueId()));
            }
            this.map.put(player.getUniqueId(), board);
            if (c) {
                player.setScoreboard(board.getBoard());
            } else {
                player.setScoreboard(board.getWithoutUpdate());
            }
            return;
        }
        SBoard board = new SBoard("\u00a7b\u00a7lPractice");
        board.add("\u00a7a\u00a7lNome:", 15);
        board.add(player.getName(), 14);
        board.blank(13);
        board.add("\u00a7e\u00a7lCoins:", 12);
        board.add(String.valueOf(this.plugin.coins.get(player.getUniqueId())), 11);
        board.blank(10);
        board.add("\u00a76\u00a7lRanked Disponibili:", 9);
        if (this.plugin.ranks.getRank(player.getUniqueId()) == Rank.DEFAULT) {
            board.add(String.valueOf(this.plugin.match.get(player.getUniqueId())), 8);
        } else {
            board.add("Infinite", 8);
        }
        board.blank(7);
        board.add("\u00a7c\u00a7lRank:", 6);
        board.add(this.getRank(player.getUniqueId()), 5);
        board.blank(4);
        board.add("\u00a77\u00a7lTs3:", 3);
        board.add("cfserv.com", 2);
        board.add("\u00a7m--------------------", 1);
        this.map.put(player.getUniqueId(), board);
        player.setScoreboard(board.getBoard());
    }

    public void show(UUID id) {
        this.show(Bukkit.getPlayer((UUID)id));
    }

    public void remove(UUID id) {
        this.remove(Bukkit.getPlayer((UUID)id));
    }

    public void remove(Player player) {
        Scoreboard b = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(b);
    }

    private String getRank(UUID id) {
        return this.plugin.ranks.getRank(id).toString();
    }
}

