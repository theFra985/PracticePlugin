/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.propvp.player;

import java.util.HashMap;
import java.util.Map;
import net.propvp.Practice;
import net.propvp.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DataManager
implements Listener {
    private Map<Player, PlayerData> data = new HashMap<Player, PlayerData>();

    public DataManager() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Practice.getInstance());
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData playerData = new PlayerData(player);
            playerData.load();
            this.data.put(player, playerData);
        }
    }

    public boolean hasData(Player player) {
        return this.data.containsKey((Object)player);
    }

    public PlayerData getData(Player player) {
        return this.data.get((Object)player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        if (this.data.containsKey((Object)player)) {
            this.data.remove((Object)player);
        }
        PlayerData playerData = new PlayerData(player);
        this.data.put(player, playerData);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        final Player player = playerQuitEvent.getPlayer();
        PlayerData playerData = this.data.get((Object)player);
        playerData.save();
        new BukkitRunnable(){

            public void run() {
                if (DataManager.this.data.containsKey((Object)player)) {
                    DataManager.this.data.remove((Object)player);
                }
            }
        }.runTaskLater((Plugin)Practice.getInstance(), 60);
    }

}

