/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package net.propvp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Cuboid
implements Iterable<Block>,
Cloneable,
ConfigurationSerializable {
    protected String worldName;
    protected int x1;
    protected int y1;
    protected int z1;
    protected int x2;
    protected int y2;
    protected int z2;
    private static /* synthetic */ int[] $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection;

    public Cuboid(Location location, Location location2) {
        if (!location.getWorld().equals((Object)location2.getWorld())) {
            Logger.getLogger("The cuboid's two locations must have the same world.");
        }
        this.worldName = location.getWorld().getName();
        this.x1 = Math.min(location.getBlockX(), location2.getBlockX());
        this.y1 = Math.min(location.getBlockY(), location2.getBlockY());
        this.z1 = Math.min(location.getBlockZ(), location2.getBlockZ());
        this.x2 = Math.max(location.getBlockX(), location2.getBlockX());
        this.y2 = Math.max(location.getBlockY(), location2.getBlockY());
        this.z2 = Math.max(location.getBlockZ(), location2.getBlockZ());
    }

    public Cuboid(Location location) {
        this(location, location);
    }

    public Cuboid(Cuboid cuboid) {
        this(cuboid.getWorld().getName(), cuboid.x1, cuboid.y1, cuboid.z1, cuboid.x2, cuboid.y2, cuboid.z2);
    }

    private Cuboid(String string, int n, int n2, int n3, int n4, int n5, int n6) {
        this.worldName = string;
        this.x1 = Math.min(n, n4);
        this.x2 = Math.max(n, n4);
        this.y1 = Math.min(n2, n5);
        this.y2 = Math.max(n2, n5);
        this.z1 = Math.min(n3, n6);
        this.z2 = Math.max(n3, n6);
    }

    public Cuboid(Map<String, Object> map) {
        this.worldName = (String)map.get("worldName");
        this.x1 = (Integer)map.get("x1");
        this.x2 = (Integer)map.get("x2");
        this.y1 = (Integer)map.get("y1");
        this.y2 = (Integer)map.get("y2");
        this.z1 = (Integer)map.get("z1");
        this.z2 = (Integer)map.get("z2");
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("worldName", this.worldName);
        hashMap.put("x1", this.x1);
        hashMap.put("y1", this.y1);
        hashMap.put("z1", this.z1);
        hashMap.put("x2", this.x2);
        hashMap.put("y2", this.y2);
        hashMap.put("z2", this.z2);
        return hashMap;
    }

    public Location getLowerNE() {
        return new Location(this.getWorld(), (double)this.x1, (double)this.y1, (double)this.z1);
    }

    public Location getUpperSW() {
        return new Location(this.getWorld(), (double)this.x2, (double)this.y2, (double)this.z2);
    }

    public List<Block> getBlocks() {
        Iterator<Block> iterator = this.iterator();
        ArrayList<Block> arrayList = new ArrayList<Block>();
        while (iterator.hasNext()) {
            arrayList.add(iterator.next());
        }
        return arrayList;
    }

    public Location getCenter() {
        int n = this.getUpperX() + 1;
        int n2 = this.getUpperY() + 1;
        int n3 = this.getUpperZ() + 1;
        return new Location(this.getWorld(), (double)this.getLowerX() + (double)(n - this.getLowerX()) / 2.0, (double)this.getLowerY() + (double)(n2 - this.getLowerY()) / 2.0, (double)this.getLowerZ() + (double)(n3 - this.getLowerZ()) / 2.0);
    }

    public World getWorld() {
        World world = Bukkit.getWorld((String)this.worldName);
        if (world == null) {
            Logger.getLogger("The world has not been loaded (null).");
        }
        return world;
    }

    public int getSizeX() {
        return this.x2 - this.x1 + 1;
    }

    public int getSizeY() {
        return this.y2 - this.y1 + 1;
    }

    public int getSizeZ() {
        return this.z2 - this.z1 + 1;
    }

    public int getLowerX() {
        return this.x1;
    }

    public int getLowerY() {
        return this.y1;
    }

    public int getLowerZ() {
        return this.z1;
    }

    public int getUpperX() {
        return this.x2;
    }

    public int getUpperY() {
        return this.y2;
    }

    public int getUpperZ() {
        return this.z2;
    }

    public Block[] corners() {
        Block[] arrblock = new Block[8];
        World world = this.getWorld();
        arrblock[0] = world.getBlockAt(this.x1, this.y1, this.z1);
        arrblock[1] = world.getBlockAt(this.x1, this.y1, this.z2);
        arrblock[2] = world.getBlockAt(this.x1, this.y2, this.z1);
        arrblock[3] = world.getBlockAt(this.x1, this.y2, this.z2);
        arrblock[4] = world.getBlockAt(this.x2, this.y1, this.z1);
        arrblock[5] = world.getBlockAt(this.x2, this.y1, this.z2);
        arrblock[6] = world.getBlockAt(this.x2, this.y2, this.z1);
        arrblock[7] = world.getBlockAt(this.x2, this.y2, this.z2);
        return arrblock;
    }

    public Cuboid expand(CuboidDirection cuboidDirection, int n) {
        switch (Cuboid.$SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection()[cuboidDirection.ordinal()]) {
            case 9: {
                return new Cuboid(this.worldName, this.x1 - n, this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case 2: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2 + n, this.y2, this.z2);
            }
            case 6: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1 - n, this.x2, this.y2, this.z2);
            }
            case 7: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + n);
            }
            case 3: {
                return new Cuboid(this.worldName, this.x1, this.y1 - n, this.z1, this.x2, this.y2, this.z2);
            }
            case 1: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + n, this.z2);
            }
        }
        throw new IllegalArgumentException("Invalid direction " + (Object)((Object)cuboidDirection));
    }

    public Cuboid shift(CuboidDirection cuboidDirection, int n) {
        return this.expand(cuboidDirection, n).expand(cuboidDirection.opposite(), - n);
    }

    public Cuboid outset(CuboidDirection cuboidDirection, int n) {
        Cuboid cuboid = null;
        switch (Cuboid.$SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection()[cuboidDirection.ordinal()]) {
            case 10: {
                cuboid = this.expand(CuboidDirection.North, n).expand(CuboidDirection.South, n).expand(CuboidDirection.East, n).expand(CuboidDirection.West, n);
                break;
            }
            case 5: {
                cuboid = this.expand(CuboidDirection.Down, n).expand(CuboidDirection.Up, n);
                break;
            }
            case 8: {
                cuboid = this.outset(CuboidDirection.Horizontal, n).outset(CuboidDirection.Vertical, n);
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid direction " + (Object)((Object)cuboidDirection));
            }
        }
        return cuboid;
    }

    public Cuboid inset(CuboidDirection cuboidDirection, int n) {
        return this.outset(cuboidDirection, - n);
    }

    public boolean contains(int n, int n2, int n3) {
        if (n >= this.x1 && n <= this.x2 && n2 >= this.y1 && n2 <= this.y2 && n3 >= this.z1 && n3 <= this.z2) {
            return true;
        }
        return false;
    }

    public boolean contains(Block block) {
        return this.contains(block.getLocation());
    }

    public boolean contains(Location location) {
        if (this.worldName.equals(location.getWorld().getName()) && this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ())) {
            return true;
        }
        return false;
    }

    public int getVolume() {
        return this.getSizeX() * this.getSizeY() * this.getSizeZ();
    }

    public byte getAverageLightLevel() {
        long l = 0;
        int n = 0;
        for (Block block : this) {
            if (!block.isEmpty()) continue;
            l += (long)block.getLightLevel();
            ++n;
        }
        return n > 0 ? (byte)(l / (long)n) : 0;
    }

    public Cuboid contract() {
        return this.contract(CuboidDirection.Down).contract(CuboidDirection.South).contract(CuboidDirection.East).contract(CuboidDirection.Up).contract(CuboidDirection.North).contract(CuboidDirection.West);
    }

    public Cuboid contract(CuboidDirection cuboidDirection) {
        Cuboid cuboid = this.getFace(cuboidDirection.opposite());
        switch (Cuboid.$SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection()[cuboidDirection.ordinal()]) {
            case 3: {
                while (cuboid.containsOnly(0) && cuboid.getLowerY() > this.getLowerY()) {
                    cuboid = cuboid.shift(CuboidDirection.Down, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, cuboid.getUpperY(), this.z2);
            }
            case 1: {
                while (cuboid.containsOnly(0) && cuboid.getUpperY() < this.getUpperY()) {
                    cuboid = cuboid.shift(CuboidDirection.Up, 1);
                }
                return new Cuboid(this.worldName, this.x1, cuboid.getLowerY(), this.z1, this.x2, this.y2, this.z2);
            }
            case 9: {
                while (cuboid.containsOnly(0) && cuboid.getLowerX() > this.getLowerX()) {
                    cuboid = cuboid.shift(CuboidDirection.North, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, cuboid.getUpperX(), this.y2, this.z2);
            }
            case 2: {
                while (cuboid.containsOnly(0) && cuboid.getUpperX() < this.getUpperX()) {
                    cuboid = cuboid.shift(CuboidDirection.South, 1);
                }
                return new Cuboid(this.worldName, cuboid.getLowerX(), this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case 6: {
                while (cuboid.containsOnly(0) && cuboid.getLowerZ() > this.getLowerZ()) {
                    cuboid = cuboid.shift(CuboidDirection.East, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, cuboid.getUpperZ());
            }
            case 7: {
                while (cuboid.containsOnly(0) && cuboid.getUpperZ() < this.getUpperZ()) {
                    cuboid = cuboid.shift(CuboidDirection.West, 1);
                }
                return new Cuboid(this.worldName, this.x1, this.y1, cuboid.getLowerZ(), this.x2, this.y2, this.z2);
            }
        }
        throw new IllegalArgumentException("Invalid direction " + (Object)((Object)cuboidDirection));
    }

    public Cuboid getFace(CuboidDirection cuboidDirection) {
        switch (Cuboid.$SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection()[cuboidDirection.ordinal()]) {
            case 3: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
            }
            case 1: {
                return new Cuboid(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
            }
            case 9: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
            }
            case 2: {
                return new Cuboid(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case 6: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
            }
            case 7: {
                return new Cuboid(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
            }
        }
        throw new IllegalArgumentException("Invalid direction " + (Object)((Object)cuboidDirection));
    }

    public boolean containsOnly(int n) {
        for (Block block : this) {
            if (block.getTypeId() == n) continue;
            return false;
        }
        return true;
    }

    public Cuboid getBoundingCuboid(Cuboid cuboid) {
        if (cuboid == null) {
            return this;
        }
        int n = Math.min(this.getLowerX(), cuboid.getLowerX());
        int n2 = Math.min(this.getLowerY(), cuboid.getLowerY());
        int n3 = Math.min(this.getLowerZ(), cuboid.getLowerZ());
        int n4 = Math.max(this.getUpperX(), cuboid.getUpperX());
        int n5 = Math.max(this.getUpperY(), cuboid.getUpperY());
        int n6 = Math.max(this.getUpperZ(), cuboid.getUpperZ());
        return new Cuboid(this.worldName, n, n2, n3, n4, n5, n6);
    }

    public Block getRelativeBlock(int n, int n2, int n3) {
        return this.getWorld().getBlockAt(this.x1 + n, this.y1 + n2, this.z1 + n3);
    }

    public Block getRelativeBlock(World world, int n, int n2, int n3) {
        return world.getBlockAt(this.x1 + n, this.y1 + n2, this.z1 + n3);
    }

    public List<Chunk> getChunks() {
        ArrayList<Chunk> arrayList = new ArrayList<Chunk>();
        World world = this.getWorld();
        int n = this.getLowerX() & -16;
        int n2 = this.getUpperX() & -16;
        int n3 = this.getLowerZ() & -16;
        int n4 = this.getUpperZ() & -16;
        int n5 = n;
        while (n5 <= n2) {
            int n6 = n3;
            while (n6 <= n4) {
                arrayList.add(world.getChunkAt(n5 >> 4, n6 >> 4));
                n6 += 16;
            }
            n5 += 16;
        }
        return arrayList;
    }

    @Override
    public Iterator<Block> iterator() {
        return new CuboidIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    public Cuboid clone() {
        return new Cuboid(this);
    }

    public String toString() {
        return new String("Cuboid: " + this.worldName + "," + this.x1 + "," + this.y1 + "," + this.z1 + "=>" + this.x2 + "," + this.y2 + "," + this.z2);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[CuboidDirection.values().length];
        try {
            arrn[CuboidDirection.Both.ordinal()] = 9;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[CuboidDirection.Down.ordinal()] = 6;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[CuboidDirection.East.ordinal()] = 2;
        }
        catch (NoSuchFieldError v3) {}
        try {
            arrn[CuboidDirection.Horizontal.ordinal()] = 7;
        }
        catch (NoSuchFieldError v4) {}
        try {
            arrn[CuboidDirection.North.ordinal()] = 1;
        }
        catch (NoSuchFieldError v5) {}
        try {
            arrn[CuboidDirection.South.ordinal()] = 3;
        }
        catch (NoSuchFieldError v6) {}
        try {
            arrn[CuboidDirection.Unknown.ordinal()] = 10;
        }
        catch (NoSuchFieldError v7) {}
        try {
            arrn[CuboidDirection.Up.ordinal()] = 5;
        }
        catch (NoSuchFieldError v8) {}
        try {
            arrn[CuboidDirection.Vertical.ordinal()] = 8;
        }
        catch (NoSuchFieldError v9) {}
        try {
            arrn[CuboidDirection.West.ordinal()] = 4;
        }
        catch (NoSuchFieldError v10) {}
        $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection = arrn;
        return $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection;
    }

    public static enum CuboidDirection {
        North("North", 0),
        East("East", 1),
        South("South", 2),
        West("West", 3),
        Up("Up", 4),
        Down("Down", 5),
        Horizontal("Horizontal", 6),
        Vertical("Vertical", 7),
        Both("Both", 8),
        Unknown("Unknown", 9);
        
        private static /* synthetic */ int[] $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection;

        private CuboidDirection(String string2, int n2, String string3, int n3) {
        }

        public CuboidDirection opposite() {
            switch (CuboidDirection.$SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection()[this.ordinal()]) {
                case 9: {
                    return South;
                }
                case 6: {
                    return West;
                }
                case 2: {
                    return North;
                }
                case 7: {
                    return East;
                }
                case 10: {
                    return Vertical;
                }
                case 5: {
                    return Horizontal;
                }
                case 1: {
                    return Down;
                }
                case 3: {
                    return Up;
                }
                case 8: {
                    return Both;
                }
            }
            return Unknown;
        }

        static /* synthetic */ int[] $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection() {
            int[] arrn;
            int[] arrn2 = $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection;
            if (arrn2 != null) {
                return arrn2;
            }
            arrn = new int[CuboidDirection.values().length];
            try {
                arrn[CuboidDirection.Both.ordinal()] = 9;
            }
            catch (NoSuchFieldError v1) {}
            try {
                arrn[CuboidDirection.Down.ordinal()] = 6;
            }
            catch (NoSuchFieldError v2) {}
            try {
                arrn[CuboidDirection.East.ordinal()] = 2;
            }
            catch (NoSuchFieldError v3) {}
            try {
                arrn[CuboidDirection.Horizontal.ordinal()] = 7;
            }
            catch (NoSuchFieldError v4) {}
            try {
                arrn[CuboidDirection.North.ordinal()] = 1;
            }
            catch (NoSuchFieldError v5) {}
            try {
                arrn[CuboidDirection.South.ordinal()] = 3;
            }
            catch (NoSuchFieldError v6) {}
            try {
                arrn[CuboidDirection.Unknown.ordinal()] = 10;
            }
            catch (NoSuchFieldError v7) {}
            try {
                arrn[CuboidDirection.Up.ordinal()] = 5;
            }
            catch (NoSuchFieldError v8) {}
            try {
                arrn[CuboidDirection.Vertical.ordinal()] = 8;
            }
            catch (NoSuchFieldError v9) {}
            try {
                arrn[CuboidDirection.West.ordinal()] = 4;
            }
            catch (NoSuchFieldError v10) {}
            $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection = arrn;
            return $SWITCH_TABLE$net$propvp$util$Cuboid$CuboidDirection;
        }
    }

    public class CuboidIterator
    implements Iterator<Block> {
        private World w;
        private int baseX;
        private int baseY;
        private int baseZ;
        private int x;
        private int y;
        private int z;
        private int sizeX;
        private int sizeY;
        private int sizeZ;

        public CuboidIterator(World world, int n, int n2, int n3, int n4, int n5, int n6) {
            this.w = world;
            this.baseX = n;
            this.baseY = n2;
            this.baseZ = n3;
            this.sizeX = Math.abs(n4 - n) + 1;
            this.sizeY = Math.abs(n5 - n2) + 1;
            this.sizeZ = Math.abs(n6 - n3) + 1;
            boolean bl = false;
            this.z = bl ? 1 : 0;
            this.y = bl ? 1 : 0;
            this.x = bl ? 1 : 0;
        }

        @Override
        public boolean hasNext() {
            if (this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ) {
                return true;
            }
            return false;
        }

        @Override
        public Block next() {
            Block block = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
            if (++this.x >= this.sizeX) {
                this.x = 0;
                if (++this.y >= this.sizeY) {
                    this.y = 0;
                    ++this.z;
                }
            }
            return block;
        }

        @Override
        public void remove() {
        }
    }

}

