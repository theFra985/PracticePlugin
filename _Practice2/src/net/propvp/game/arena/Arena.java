/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Difficulty
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.propvp.game.arena;

import java.io.File;
import java.util.List;
import net.propvp.Practice;
import net.propvp.file.Config;
import net.propvp.util.Cuboid;
import net.propvp.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Arena {
    private String name;
    private Cuboid originalRegion;
    private Cuboid clonedRegion;
    private Location spawn1;
    private Location spawn2;
    private World originalWorld;
    private World clonedWorld;
    private boolean used = false;

    public Arena(String string) {
        this.name = string;
    }

    public Arena(String string, String string2, String string3, String string4, String string5) {
        this.name = string;
        Practice.getWorldManager().loadWorld(string);
        if (Bukkit.getWorld((String)string) == null) {
            Practice.getLog().log("Tried loading arena '" + string + "' but could not find a world to load with it.", true);
            return;
        }
        this.originalWorld = Bukkit.getWorld((String)string);
        Practice.getWorldManager().copyWorld(this.originalWorld.getWorldFolder(), new File(String.valueOf(string) + "-clone"));
        Practice.getWorldManager().loadWorld(String.valueOf(string) + "-clone");
        this.clonedWorld = Bukkit.getWorld((String)(String.valueOf(string) + "-clone"));
        this.clonedWorld.setDifficulty(Difficulty.PEACEFUL);
        this.clonedWorld.setTime(0);
        this.clonedWorld.setPVP(true);
        this.clonedWorld.setAnimalSpawnLimit(1);
        this.clonedWorld.setMonsterSpawnLimit(1);
        this.clonedWorld.setThundering(false);
        for (Entity entity : this.clonedWorld.getEntities()) {
            if (entity instanceof Player) continue;
            entity.remove();
        }
        this.clonedWorld.save();
        try {
            this.spawn1 = LocationUtil.getLocation(string4);
            this.spawn2 = LocationUtil.getLocation(string5);
            this.originalRegion = new Cuboid(LocationUtil.getLocation(string2), LocationUtil.getLocation(string3));
            this.clonedRegion = new Cuboid(new Location(this.clonedWorld, this.spawn1.getX(), this.spawn1.getY(), this.spawn1.getZ()), new Location(this.clonedWorld, this.spawn2.getX(), this.spawn2.getY(), this.spawn2.getZ()));
        }
        catch (NullPointerException var6_8) {
            Practice.getLog().log("Tried loading arena '" + string + "' but one of the values is null.", true);
        }
        catch (NumberFormatException var6_9) {
            Practice.getLog().log("Tried loading arena '" + string + "' but one of the values is misconfigured.", true);
        }
    }

    public String getName() {
        return this.name;
    }

    public Cuboid getRegion() {
        return this.originalRegion;
    }

    public Cuboid getClonedRegion() {
        return this.clonedRegion;
    }

    public Location getSpawn1() {
        return this.spawn1;
    }

    public Location getSpawn2() {
        return this.spawn2;
    }

    public Location getClonedSpawn1() {
        if (this.clonedWorld != null) {
            return new Location(this.clonedWorld, this.spawn1.getX(), this.spawn1.getY(), this.spawn1.getZ());
        }
        return null;
    }

    public Location getClonedSpawn2() {
        if (this.clonedWorld != null) {
            return new Location(this.clonedWorld, this.spawn2.getX(), this.spawn2.getY(), this.spawn2.getZ());
        }
        return null;
    }

    public World getOriginalWorld() {
        return this.originalWorld;
    }

    public World getClonedWorld() {
        return this.clonedWorld;
    }

    public boolean inUse() {
        return this.used;
    }

    public void setInUse(boolean bl) {
        this.used = bl;
    }

    public void setSpawn1(Location location) {
        this.spawn1 = location;
    }

    public void setSpawn2(Location location) {
        this.spawn2 = location;
    }

    public void setCuboid(Cuboid cuboid) {
        this.originalRegion = cuboid;
    }

    public void regenerate() {
        Practice.getWorldManager().unloadWorld(this.clonedWorld);
        Practice.getWorldManager().deleteWorld(new File(String.valueOf(this.originalWorld.getWorldFolder().getPath()) + "-clone"));
        Practice.getWorldManager().copyWorld(this.originalWorld.getWorldFolder(), new File(String.valueOf(this.name) + "-clone"));
        Practice.getWorldManager().loadWorld(String.valueOf(this.name) + "-clone");
        this.clonedWorld = Bukkit.getWorld((String)(String.valueOf(this.name) + "-clone"));
        this.clonedWorld.setAutoSave(false);
        this.clonedWorld.setDifficulty(Difficulty.PEACEFUL);
        this.clonedWorld.setTime(0);
        this.clonedWorld.setPVP(true);
        this.clonedWorld.setAnimalSpawnLimit(1);
        this.clonedWorld.setMonsterSpawnLimit(1);
        this.clonedWorld.setThundering(false);
        this.used = false;
    }

    public boolean isSetup() {
        if (this.spawn1 == null || this.spawn2 == null || this.originalRegion == null || this.clonedRegion == null || this.originalWorld == null || this.clonedWorld == null) {
            return false;
        }
        return true;
    }

    public void save() {
        Config config = Practice.getBackend().getArenasConfig();
        if (!config.getConfig().isConfigurationSection("arena")) {
            config.getConfig().createSection("arena");
        }
        if (this.spawn1 != null) {
            config.getConfig().set("arena." + this.name + ".spawn1", (Object)LocationUtil.getString(this.spawn1));
        }
        if (this.spawn2 != null) {
            config.getConfig().set("arena." + this.name + ".spawn2", (Object)LocationUtil.getString(this.spawn2));
        }
        if (this.originalRegion != null) {
            String string = LocationUtil.getString(new Location(this.originalRegion.getWorld(), (double)this.originalRegion.getLowerX(), (double)this.originalRegion.getLowerY(), (double)this.originalRegion.getLowerZ()));
            String string2 = LocationUtil.getString(new Location(this.originalRegion.getWorld(), (double)this.originalRegion.getUpperX(), (double)this.originalRegion.getUpperY(), (double)this.originalRegion.getUpperZ()));
            config.getConfig().set("arena." + this.name + ".region1", (Object)string);
            config.getConfig().set("arena." + this.name + ".region2", (Object)string2);
        }
        config.save();
    }
}

