/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.scoreboard.Objective
 *  org.bukkit.scoreboard.Score
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.Team
 */
package net.propvp.scoreboard;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.propvp.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardHelper {
    private List<ScoreboardText> list = new ArrayList<ScoreboardText>();
    private Scoreboard scoreBoard;
    private Objective objective;
    private String tag = "PlaceHolder";
    private int lastSentCount = -1;

    public ScoreboardHelper(Scoreboard scoreboard) {
        this.scoreBoard = scoreboard;
        this.objective = this.getOrCreateObjective(this.tag);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public ScoreboardHelper(Scoreboard scoreboard, String string) {
        Preconditions.checkState((boolean)(string.length() <= 32), (Object)"title can not be more than 32");
        this.tag = ChatColor.translateAlternateColorCodes((char)'&', (String)string);
        this.scoreBoard = scoreboard;
        this.objective = this.getOrCreateObjective(this.tag);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void add(String string, String string2) {
        Preconditions.checkState((boolean)(string.length() <= 16), (Object)"left can not be more than 16");
        Preconditions.checkState((boolean)(string2.length() <= 16), (Object)"right can not be more than 16");
        this.list.add(new ScoreboardText(Utils.color(string), Utils.color(string2)));
    }

    public void set(int n, String string, String string2) {
        Preconditions.checkState((boolean)(string.length() <= 16), (Object)"left can not be more than 16");
        Preconditions.checkState((boolean)(string2.length() <= 16), (Object)"right can not be more than 16");
        this.list.set(n, new ScoreboardText(Utils.color(string), Utils.color(string2)));
    }

    public void clear() {
        this.list.clear();
    }

    public void remove(int n) {
        String string = this.getNameForIndex(n);
        this.scoreBoard.resetScores(string);
        Team team = this.getOrCreateTeam(String.valueOf(String.valueOf(String.valueOf(ChatColor.stripColor((String)this.tag)))) + n, n);
        team.unregister();
    }

    public void update(Player player) {
        int n;
        player.setScoreboard(this.scoreBoard);
        if (this.lastSentCount != -1) {
            n = this.list.size();
            int n2 = 0;
            while (n2 < this.lastSentCount - n) {
                this.remove(n + n2);
                ++n2;
            }
        }
        n = 0;
        while (n < this.list.size()) {
            Team team = this.getOrCreateTeam(String.valueOf(String.valueOf(String.valueOf(ChatColor.stripColor((String)this.tag)))) + n, n);
            ScoreboardText scoreboardText = this.list.get(this.list.size() - n - 1);
            team.setPrefix(scoreboardText.getLeft());
            team.setSuffix(scoreboardText.getRight());
            this.objective.getScore(this.getNameForIndex(n)).setScore(n + 1);
            ++n;
        }
        this.lastSentCount = this.list.size();
    }

    public Team getOrCreateTeam(String string, int n) {
        Team team = this.scoreBoard.getTeam(string);
        if (team == null) {
            team = this.scoreBoard.registerNewTeam(string);
            team.addEntry(this.getNameForIndex(n));
        }
        return team;
    }

    public Objective getOrCreateObjective(String string) {
        Objective objective = this.scoreBoard.getObjective("dummyhubobj");
        if (objective == null) {
            objective = this.scoreBoard.registerNewObjective("dummyhubobj", "dummy");
        }
        objective.setDisplayName(string);
        return objective;
    }

    public String getNameForIndex(int n) {
        return String.valueOf(String.valueOf(String.valueOf(ChatColor.values()[n].toString()))) + (Object)ChatColor.RESET;
    }

    public static class ScoreboardText {
        private String left;
        private String right;

        public ScoreboardText(String string, String string2) {
            this.left = string;
            this.right = string2;
        }

        public String getLeft() {
            return this.left;
        }

        public void setLeft(String string) {
            this.left = string;
        }

        public String getRight() {
            return this.right;
        }

        public void setRight(String string) {
            this.right = string;
        }
    }

}

