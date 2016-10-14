/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 */
package net.propvp.listener;

import net.propvp.Practice;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener
implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        Player player = blockPlaceEvent.getPlayer();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (!playerData.hasMatch()) {
            if (player.hasPermission("propractice.bypass") || player.isOp()) {
                return;
            }
            blockPlaceEvent.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        if (blockBreakEvent.isCancelled()) {
            return;
        }
        Player player = blockBreakEvent.getPlayer();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (!playerData.hasMatch()) {
            if (player.hasPermission("propractice.bypass") || player.isOp()) {
                return;
            }
            blockBreakEvent.setCancelled(true);
        }
    }
}

