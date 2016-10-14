/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.generator.ChunkGenerator
 */
package net.propvp.world;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class EmptyChunkGenerator
extends ChunkGenerator {
    public byte[] generate(World world, Random random, int n, int n2) {
        byte[] arrby = new byte[32768];
        return arrby;
    }

    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0.0, 64.0, 0.0);
    }

    public boolean canSpawn(World world, int n, int n2) {
        return true;
    }
}

