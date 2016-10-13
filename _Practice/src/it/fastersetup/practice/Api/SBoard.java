/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Score
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.Team
 */
package it.fastersetup.practice.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SBoard {
    private String title;
    private Scoreboard board;
    private HashMap<String, Integer> map = new HashMap<String, Integer>();

    public SBoard(String name) {
        this.title = name;
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    private String fix(String index) throws IllegalArgumentException {
        String o = new String(index);
        while (this.map.keySet().contains(index)) {
            index = String.valueOf(index) + "\u00a7r";
        }
        if (index.length() > 48) {
            index = index.substring(0, 48);
        }
        if (this.map.keySet().contains(index)) {
            throw new IllegalArgumentException("Duplicate string. Could not be fixed: " + o);
        }
        return index;
    }

    public void add(String name, int score) {
        name = this.fix(name);
        this.map.put(name, score);
    }

    public void blank(int score) {
        this.add(" ", score);
    }

    public boolean equals(int slot, String index) {
        for (Map.Entry<String, Integer> entry : this.map.entrySet()) {
            if (entry.getValue() != slot || !ChatColor.stripColor((String)entry.getKey()).equals(ChatColor.stripColor((String)index))) continue;
            return true;
        }
        return false;
    }

    public void replace(int score, String name) {
        ArrayList<String> keys = new ArrayList<String>();
        for (String s2 : this.map.keySet()) {
            keys.add(s2);
        }
        for (String s2 : keys) {
            if (this.map.get(s2) != score) continue;
            this.map.remove(s2);
            this.add(name, score);
        }
    }

    public void delete(int score) {
        ArrayList<String> keys = new ArrayList<String>();
        for (String s2 : this.map.keySet()) {
            keys.add(s2);
        }
        for (String s2 : keys) {
            if (this.map.get(s2) != score) continue;
            this.map.remove(s2);
        }
    }

    public void reset() {
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        this.map.clear();
    }

    public void setTitle(String t) {
        this.title = t;
    }

    @SuppressWarnings("deprecation")
	public void build() {
        this.clearBoard();
        Objective o = this.board.registerNewObjective(this.title, "dummy");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName(this.title);
        int index = 1;
        for (Map.Entry<String, Integer> entry : this.map.entrySet()) {
            OfflinePlayer player;
            Team team;
            String name = entry.getKey();
            if (name.length() <= 16) {
                o.getScore(name).setScore(entry.getValue().intValue());
            } else if (name.length() <= 32) {
                team = this.board.registerNewTeam(this.teamName(name, index));
                team.setPrefix(name.substring(0, 16));
                name = name.substring(16, name.length());
                player = Bukkit.getOfflinePlayer((String)name);
                team.addPlayer(player);
                o.getScore(player).setScore(entry.getValue().intValue());
            } else if (name.length() <= 48) {
                team = this.board.registerNewTeam(this.teamName(name, index));
                team.setPrefix(name.substring(0, 16));
                team.setSuffix(name.substring(32, name.length()));
                name = name.substring(16, 32);
                player = Bukkit.getOfflinePlayer((String)name);
                team.addPlayer(player);
                o.getScore(player).setScore(entry.getValue().intValue());
            }
            ++index;
        }
    }

    public String teamName(String name, int index) {
        if (index > 16) {
            index = 16;
        }
        return (name = ChatColor.stripColor((String)name)).length() >= index ? name.substring(0, index) : (name.length() <= 16 ? name : name.substring(0, 16));
    }

    public Scoreboard getBoard() {
        this.build();
        return this.board;
    }

    public Scoreboard getWithoutUpdate() {
        return this.board;
    }

    public void display(Player p) {
        p.setScoreboard(this.getBoard());
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

