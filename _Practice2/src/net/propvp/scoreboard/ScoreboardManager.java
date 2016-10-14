/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.propvp.scoreboard;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.propvp.Practice;
import net.propvp.file.Config;
import net.propvp.game.Game;
import net.propvp.game.GameMode;
import net.propvp.game.Team;
import net.propvp.game.ladder.Ladder;
import net.propvp.game.ladder.OvO;
import net.propvp.party.Party;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import net.propvp.scoreboard.ScoreboardHelper;
import net.propvp.scoreboard.ScoreboardUser;
import net.propvp.timing.ElapsedTimer;
import net.propvp.timing.ManualTimer;
import net.propvp.util.Elo;
import net.propvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardManager
extends BukkitRunnable {
    private Config config = Practice.getBackend().getMainConfig();
    private String lines;
    private String primary;
    private String secondary;
    private String alternative;
    private String websiteURL;
    private boolean showRatings;
    private boolean showWebsite;
    private boolean showParty;
    private ConfigurationSection section = this.config.getConfig().getConfigurationSection("scoreboard");

    public ScoreboardManager() {
        this.lines = Utils.color(this.section.getString("lines.decorator"));
        this.primary = Utils.color(this.section.getString("colors.primary"));
        this.secondary = Utils.color(this.section.getString("colors.secondary"));
        this.alternative = Utils.color(this.section.getString("colors.alternative"));
        this.websiteURL = this.section.getString("lines.website");
        this.showRatings = this.section.getBoolean("options.show-ratings");
        this.showWebsite = this.section.getBoolean("options.show-website");
        this.showParty = this.section.getBoolean("options.show-party");
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Object object;
            Player player2;
            Object object2;
            PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
            ScoreboardHelper scoreboardHelper = playerData.getScoreboardHelper();
            ScoreboardUser scoreboardUser = playerData.getScoreboardUser();
            scoreboardHelper.clear();
            if (playerData.isHidingScoreboard()) {
                scoreboardHelper.update(player);
                return;
            }
            if (this.showWebsite) {
                scoreboardHelper.add(this.lines, this.lines);
                object2 = this.websiteURL.substring(0, 2).contains("&") ? Utils.color(this.websiteURL).substring(0, 2) : this.primary;
                player2 = this.websiteURL.length() >= 14 ? this.websiteURL.substring(0, this.websiteURL.length() - (this.websiteURL.length() - 14)) : this.websiteURL;
                object = this.websiteURL.length() >= 14 ? this.websiteURL.subSequence(14, this.websiteURL.length()) : "";
                scoreboardHelper.add(player2.contains("&") ? Utils.color((String)player2) : String.valueOf(object2) + (String)player2, String.valueOf(object2) + (String)object);
            }
            if (playerData.getQueue() == null && playerData.getMatch() == null) {
                scoreboardHelper.add(this.lines, this.lines);
                if (this.showRatings) {
                    if (playerData.getRatings().isEmpty()) {
                        scoreboardHelper.add(String.valueOf(this.secondary) + "No data found", "");
                    } else {
                        player2 = playerData.getRatings().entrySet().iterator();
                        while (player2.hasNext()) {
                            object2 = (Map.Entry)player2.next();
                            scoreboardHelper.add(String.valueOf(this.primary) + ((GameMode)object2.getKey()).getName() + ": ", String.valueOf(this.secondary) + ((Elo)object2.getValue()).getRating());
                        }
                    }
                }
                if (this.showParty && playerData.hasParty()) {
                    scoreboardHelper.add(this.lines, this.lines);
                    scoreboardHelper.add(String.valueOf(this.primary) + "Party", "");
                    scoreboardHelper.add((Object)ChatColor.GRAY + " \u00bb ", String.valueOf(this.secondary) + (playerData.getParty().getLeader().getName().length() >= 15 ? playerData.getParty().getLeader().getName().substring(0, playerData.getParty().getLeader().getName().length() - (playerData.getParty().getLeader().getName().length() - 15)) : playerData.getParty().getLeader().getName()));
                    if (!playerData.getParty().getMembers().isEmpty()) {
                        player2 = playerData.getParty().getMembers().iterator();
                        while (player2.hasNext()) {
                            scoreboardHelper.add((Object)ChatColor.GRAY + " \u00bb ", String.valueOf(this.secondary) + ((object2 = player2.next()).getName().length() >= 15 ? object2.getName().substring(0, object2.getName().length() - (object2.getName().length() - 15)) : object2.getName()));
                        }
                    }
                }
                scoreboardHelper.add(this.lines, this.lines);
            }
            if (playerData.getQueue() != null) {
                scoreboardHelper.add(this.lines, this.lines);
                object2 = playerData.getQueue();
                scoreboardHelper.add(String.valueOf(this.primary) + "Queue:", "");
                scoreboardHelper.add((Object)ChatColor.GRAY + " \u00bb ", String.valueOf(this.secondary) + object2.getGame().getName());
                if (object2 instanceof OvO) {
                    scoreboardHelper.add((Object)ChatColor.GRAY + " \u00bb ", String.valueOf(this.secondary) + "1vs1");
                } else {
                    scoreboardHelper.add((Object)ChatColor.GRAY + " \u00bb ", String.valueOf(this.secondary) + "2vs2");
                }
                if (object2 instanceof OvO && ((OvO)object2).isRanked()) {
                    int n = ((OvO)object2).getRange(player);
                    scoreboardHelper.add(String.valueOf(this.primary) + "Range: ", String.valueOf(this.alternative) + (playerData.getRating(object2.getGame()).getRating() - n) + " -> " + this.alternative + (playerData.getRating(object2.getGame()).getRating() + n));
                }
                if (object2 instanceof OvO) {
                    scoreboardHelper.add(String.valueOf(this.primary) + "Waiting: ", String.valueOf(this.secondary) + object2.getTimer((Object)player).toHoursMinutesSeconds());
                } else {
                    scoreboardHelper.add(String.valueOf(this.primary) + "Waiting: ", String.valueOf(this.secondary) + object2.getTimer(playerData.getParty()).toHoursMinutesSeconds());
                }
                if (this.showParty && playerData.hasParty()) {
                    scoreboardHelper.add(this.lines, this.lines);
                    scoreboardHelper.add(String.valueOf(this.primary) + "Party", "");
                    scoreboardHelper.add((Object)ChatColor.GRAY + " \u00bb ", String.valueOf(this.secondary) + (playerData.getParty().getLeader().getName().length() >= 15 ? playerData.getParty().getLeader().getName().substring(0, playerData.getParty().getLeader().getName().length() - (playerData.getParty().getLeader().getName().length() - 15)) : playerData.getParty().getLeader().getName()));
                    if (!playerData.getParty().getMembers().isEmpty()) {
                        object = playerData.getParty().getMembers().iterator();
                        while (object.hasNext()) {
                            scoreboardHelper.add((Object)ChatColor.GRAY + " \u00bb ", String.valueOf(this.secondary) + ((player2 = (Player)object.next()).getName().length() >= 15 ? player2.getName().substring(0, player2.getName().length() - (player2.getName().length() - 15)) : player2.getName()));
                        }
                    }
                }
                scoreboardHelper.add(this.lines, this.lines);
            }
            if (playerData.getMatch() != null) {
                scoreboardHelper.add(this.lines, this.lines);
                object2 = playerData.getMatch();
                player2 = object2.getRival(object2.getTeamOfPlayer(player));
                object = object2.hasStarted() ? object2.getTimer().toHoursMinutesSeconds() : " Starting...";
                scoreboardHelper.add(String.valueOf(this.primary) + "Match Time: ", "");
                scoreboardHelper.add(" " + this.secondary + (String)object, "");
                scoreboardHelper.add(String.valueOf(this.primary) + "Rival: ", "");
                scoreboardHelper.add(" " + this.secondary + (player2.getName().length() >= 15 ? player2.getName().substring(0, player2.getName().length() - (player2.getName().length() - 15)) : player2.getName()), "");
                scoreboardHelper.add(this.lines, this.lines);
            }
            if (scoreboardUser.hasAnyActiveTimers()) {
                if (scoreboardUser.getEnderpearlTimer().isActive()) {
                    object2 = new DecimalFormat("##.#");
                    scoreboardHelper.add(String.valueOf(this.primary) + "Enderpearl: ", String.valueOf(this.secondary) + object2.format((double)scoreboardUser.getEnderpearlTimer().getLeftTime() / 1000.0));
                }
                scoreboardHelper.add(this.lines, this.lines);
            }
            scoreboardHelper.update(player);
        }
    }
}

