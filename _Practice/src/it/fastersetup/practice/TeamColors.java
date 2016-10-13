/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.Team
 */
package it.fastersetup.practice;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamColors {
    public TeamColors(ArrayList<UUID> t1, ArrayList<UUID> t2) {
        Scoreboard b1 = this.getBoard(t1, t2);
        Scoreboard b2 = this.getBoard(t2, t1);
        for (UUID id2 : t1) {
            Bukkit.getPlayer((UUID)id2).setScoreboard(b1);
        }
        for (UUID id2 : t2) {
            Bukkit.getPlayer((UUID)id2).setScoreboard(b2);
        }
    }

    private Scoreboard getBoard(ArrayList<UUID> t1, ArrayList<UUID> t2) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Team team1 = board.registerNewTeam("t1");
        Team team2 = board.registerNewTeam("t2");
        team1.setPrefix("\u00a7a");
        team2.setPrefix("\u00a7c");
        for (UUID id2 : t1) {
            team1.addPlayer((OfflinePlayer)Bukkit.getPlayer((UUID)id2));
        }
        for (UUID id2 : t2) {
            team2.addPlayer((OfflinePlayer)Bukkit.getPlayer((UUID)id2));
        }
        return board;
    }
}

