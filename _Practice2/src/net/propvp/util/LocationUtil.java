/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package net.propvp.util;

import net.propvp.Practice;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LocationUtil {
    public static void teleportToSpawn(Player player) {
        if (Practice.getBackend().getSpawn() == null) {
            Bukkit.getLogger().severe("The spawn is not yet set and the server's attempting to teleport them.");
        } else {
            player.teleport(Practice.getBackend().getSpawn());
        }
    }

    public static String getFakeString(String string, double d, double d2, double d3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(d).append("|");
        stringBuilder.append(d2).append("|");
        stringBuilder.append(d3).append("|");
        stringBuilder.append(string).append("|");
        stringBuilder.append(0.0).append("|");
        stringBuilder.append(0.0);
        return stringBuilder.toString();
    }

    public static String getString(Location location) {
        StringBuilder stringBuilder = new StringBuilder();
        if (location == null) {
            return null;
        }
        stringBuilder.append(location.getBlockX()).append("|");
        stringBuilder.append(location.getBlockY()).append("|");
        stringBuilder.append(location.getBlockZ()).append("|");
        stringBuilder.append(location.getWorld().getName()).append("|");
        stringBuilder.append(location.getYaw()).append("|");
        stringBuilder.append(location.getPitch());
        return stringBuilder.toString();
    }

    public static Location getLocation(String string) {
        String[] arrstring = string.split("\\|");
        int n = Integer.parseInt(arrstring[0]);
        int n2 = Integer.parseInt(arrstring[1]);
        int n3 = Integer.parseInt(arrstring[2]);
        World world = Bukkit.getWorld((String)arrstring[3]);
        Float f = Float.valueOf(Float.parseFloat(arrstring[4]));
        Float f2 = Float.valueOf(Float.parseFloat(arrstring[5]));
        return new Location(world, (double)n, (double)n2, (double)n3, f.floatValue(), f2.floatValue());
    }
}

