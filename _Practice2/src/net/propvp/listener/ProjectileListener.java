/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.projectiles.ProjectileSource
 */
package net.propvp.listener;

import java.util.List;
import net.propvp.Practice;
import net.propvp.game.Game;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import net.propvp.util.EntityHider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ProjectileListener
implements Listener {
    @EventHandler
    public void onLaunch(ProjectileLaunchEvent projectileLaunchEvent) {
        if (!(projectileLaunchEvent.getEntity().getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)projectileLaunchEvent.getEntity().getShooter();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (projectileLaunchEvent.getEntityType() == EntityType.SPLASH_POTION || projectileLaunchEvent.getEntityType() == EntityType.ENDER_PEARL || projectileLaunchEvent.getEntityType() == EntityType.ARROW) {
            EntityHider entityHider = Practice.getBackend().getEntityHider();
            if (playerData.getMatch() != null) {
                Game game = playerData.getMatch();
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (game.getPlayers().contains((Object)player2)) continue;
                    entityHider.hideEntity(player2, (Entity)projectileLaunchEvent.getEntity());
                }
            }
        }
    }
}

