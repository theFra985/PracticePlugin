/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package net.propvp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.propvp.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CuboidManager {
    public static Map<UUID, Location> regionSelect1 = new HashMap<UUID, Location>();
    public static Map<UUID, Location> regionSelect2 = new HashMap<UUID, Location>();

    public static boolean bothPointsSet(Player player) {
        if (regionSelect1.containsKey(player.getUniqueId()) && regionSelect2.containsKey(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    public static void setRegionSelect1(UUID uUID, Location location) {
        regionSelect1.put(uUID, location);
    }

    public static void setRegionSelect2(UUID uUID, Location location) {
        regionSelect2.put(uUID, location);
    }

    public static Cuboid getCuboid(UUID uUID) {
        return new Cuboid(regionSelect1.get(uUID), regionSelect2.get(uUID));
    }
}

