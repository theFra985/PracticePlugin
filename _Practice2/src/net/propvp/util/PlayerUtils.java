/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.propvp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import net.propvp.Practice;
import net.propvp.game.Game;
import net.propvp.player.DataManager;
import net.propvp.player.InventoryManager;
import net.propvp.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerUtils {
    private static Method getHandleMethod;
    private static Field pingField;

    public static int getPing(Player player) {
        try {
            int n;
            if (getHandleMethod == null) {
                getHandleMethod = player.getClass().getDeclaredMethod("getHandle", new Class[0]);
                getHandleMethod.setAccessible(true);
            }
            Object object = getHandleMethod.invoke((Object)player, new Object[0]);
            if (pingField == null) {
                pingField = object.getClass().getDeclaredField("ping");
                pingField.setAccessible(true);
            }
            return (n = pingField.getInt(object)) > 0 ? n : 0;
        }
        catch (Exception var1_2) {
            return 1;
        }
    }

    public static void announceToStaff(String string) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission("propractice.admin")) continue;
            player.sendMessage((Object)ChatColor.DARK_RED + "Important" + (Object)ChatColor.DARK_GRAY + " \u00bb " + (Object)ChatColor.GRAY + string);
        }
    }

    public static void prepareForMatch(Player player) {
        player.getActivePotionEffects().clear();
        player.setHealth(20.0);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setCanPickupItems(true);
        player.updateInventory();
        player.setMaximumNoDamageTicks(20);
    }

    public static void prepareForSpawn(Player player) {
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        playerData.setMatchSpectating(null);
        playerData.setSpectating(false);
        playerData.setMatch(null);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setCanPickupItems(true);
        Practice.getBackend().getInventoryManager();
        player.getInventory().setContents(InventoryManager.getSoloInventory(player));
        player.teleport(Practice.getBackend().getSpawn());
    }
}

