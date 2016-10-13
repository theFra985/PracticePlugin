/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package it.fastersetup.practice;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import it.fastersetup.practice.Main;

public class VisibleManager {
    private Main plugin;

    public VisibleManager(Main pl) {
        this.plugin = pl;
    }

    public void update() {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            this.update(p);
            ++n2;
        }
    }

    public void update(Player player) {
        if (this.plugin.data.getSettings(player.getUniqueId()).playersVisible()) {
            if (!this.plugin.arenas.isInArena(player.getUniqueId())) {
                this.showAll(player);
            }
        } else if (!this.plugin.arenas.isInArena(player.getUniqueId())) {
            this.hideAll(player);
        }
        this.plugin.arenas.updateVisible(player.getUniqueId());
    }

    private void hideAll(Player player) {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            if (p.getUniqueId() != player.getUniqueId()) {
                player.hidePlayer(p);
            }
            ++n2;
        }
    }

    private void showAll(Player player) {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            if (p.getUniqueId() != player.getUniqueId() && !this.plugin.arenas.isSpectating(p.getUniqueId())) {
                player.showPlayer(p);
            }
            ++n2;
        }
    }
}

