/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 */
package net.propvp.game;

import net.propvp.Practice;
import net.propvp.file.Config;
import net.propvp.player.PlayerInv;
import net.propvp.util.InventoryUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class GameMode {
    private String name;
    private String displayName;
    private Material display;
    private boolean editable;
    private boolean regeneration;
    private boolean hunger;
    private boolean build;
    private int hitdelay;
    private PlayerInv inventory;

    public GameMode(String string) {
        this.name = string;
        this.inventory = null;
        this.display = Material.ANVIL;
        this.editable = false;
        this.regeneration = true;
        this.hunger = true;
        this.build = false;
        this.hitdelay = 20;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public PlayerInv getInventory() {
        return this.inventory;
    }

    public Material getDisplay() {
        return this.display;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public boolean isRegeneration() {
        return this.regeneration;
    }

    public boolean isHunger() {
        return this.hunger;
    }

    public boolean isBuild() {
        return this.build;
    }

    public int getHitDelay() {
        return this.hitdelay;
    }

    public void setDisplayName(String string) {
        this.displayName = string;
    }

    public void setStartingInventory(PlayerInv playerInv) {
        this.inventory = playerInv;
    }

    public void setDisplay(Material material) {
        this.display = material;
    }

    public void setEditable(boolean bl) {
        this.editable = bl;
    }

    public void setRegeneration(boolean bl) {
        this.regeneration = bl;
    }

    public void setHunger(boolean bl) {
        this.hunger = bl;
    }

    public void setBuild(boolean bl) {
        this.build = bl;
    }

    public void setHitDelay(Integer n) {
        this.hitdelay = n;
    }

    public void save() {
        Config config = Practice.getBackend().getGameModesConfig();
        if (!config.getConfig().isConfigurationSection("gamemode")) {
            config.getConfig().createSection("gamemode");
        }
        if (this.inventory == null) {
            config.getConfig().set("gamemode." + this.name + ".items", (Object)null);
        } else {
            config.getConfig().set("gamemode." + this.name + ".items", (Object)InventoryUtil.playerInventoryToString(this.inventory));
        }
        config.getConfig().set("gamemode." + this.name + ".display-name", (Object)this.displayName);
        config.getConfig().set("gamemode." + this.name + ".display", (Object)this.display.name());
        config.getConfig().set("gamemode." + this.name + ".editable", (Object)this.editable);
        config.getConfig().set("gamemode." + this.name + ".regeneration", (Object)this.regeneration);
        config.getConfig().set("gamemode." + this.name + ".hunger", (Object)this.hunger);
        config.getConfig().set("gamemode." + this.name + ".build", (Object)this.build);
        config.getConfig().set("gamemode." + this.name + ".hitdelay", (Object)this.hitdelay);
        config.save();
    }

    public void remove() {
        Config config = Practice.getBackend().getGameModesConfig();
        config.getConfig().set("gamemode." + this.name, (Object)null);
        config.save();
    }
}

