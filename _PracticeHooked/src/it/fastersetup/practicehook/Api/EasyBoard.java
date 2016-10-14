/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Score
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.Team
 */
package it.fastersetup.practicehook.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class EasyBoard {
    private String title;
    private Scoreboard board;
    private HashMap<UUID, Integer> scores = new HashMap<UUID, Integer>();

    public EasyBoard(String name) {
        this.title = name;
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public void add(UUID id, int score) {
        this.scores.put(id, score);
    }

    public void remove(UUID id) {
        if (this.scores.containsKey(id)) {
            this.scores.remove(id);
        }
    }

    public void setTitle(String name) {
        this.title = name;
    }

    @SuppressWarnings("deprecation")
	public void build() {
        Objective obj = this.board.registerNewObjective(this.title, "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(this.title);
        int amount = 0;
        for (Map.Entry<UUID, Integer> entry : this.scores.entrySet()) {
            String name;
            OfflinePlayer player;
            Team team;
            if (amount <= 14) {
                name = Bukkit.getPlayer((UUID)entry.getKey()).getName();
                team = this.board.registerNewTeam(name);
                team.setPrefix("\u00a7a");
                player = Bukkit.getOfflinePlayer((UUID)entry.getKey());
                team.addPlayer(player);
                obj.getScore(player.getName()).setScore(entry.getValue().intValue());
                ++amount;
                continue;
            }
            name = String.valueOf(this.scores.size() - 14);
            team = this.board.registerNewTeam(name);
            team.setPrefix("\u00a7bAnd \u00a7a");
            team.setSuffix(" \u00a7bmore...");
            player = Bukkit.getOfflinePlayer((String)name);
            team.addPlayer(player);
            obj.getScore(name).setScore(0);
            break;
        }
    }

    public Scoreboard getBoard() {
        return this.board;
    }

    public void update(ArrayList<UUID> players) {
        this.clearBoard();
        this.build();
        for (UUID id : players) {
            Bukkit.getPlayer((UUID)id).setScoreboard(this.board);
        }
    }

    public void update(UUID id) {
        this.clearBoard();
        this.build();
        Bukkit.getPlayer((UUID)id).setScoreboard(this.board);
    }

    public void eject(ArrayList<UUID> players) {
        for (UUID id : players) {
            Bukkit.getPlayer((UUID)id).setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    public void eject(UUID id) {
        Bukkit.getPlayer((UUID)id).setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    private void clearBoard() {
        for (String score : this.board.getEntries()) {
            this.board.resetScores(score);
        }
        for (Team team : this.board.getTeams()) {
            team.unregister();
        }
        if (this.board.getObjective(DisplaySlot.SIDEBAR) != null) {
            this.board.getObjective(DisplaySlot.SIDEBAR).unregister();
        }
    }
}

