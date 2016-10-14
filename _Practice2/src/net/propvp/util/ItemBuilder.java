/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.ItemMeta$Spigot
 *  org.bukkit.material.MaterialData
 */
package net.propvp.util;

import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ItemBuilder {
    private Material type;
    private String name;
    private String lore;
    private int amount;
    private MaterialData data;
    private boolean unbreakable;

    public ItemBuilder(Material material, String string, String string2) {
        this.type = material;
        this.name = string;
        this.lore = string2;
        this.amount = 1;
        this.data = new MaterialData(material);
    }

    public ItemBuilder(Material material, String string, String string2, boolean bl) {
        this.type = material;
        this.name = string;
        this.lore = string2;
        this.amount = 1;
        this.unbreakable = bl;
        this.data = new MaterialData(material);
    }

    public ItemBuilder(Material material, String string, String string2, int n) {
        this.type = material;
        this.name = string;
        this.lore = string2;
        this.amount = n;
        this.data = new MaterialData(material);
    }

    public ItemBuilder(Material material, String string, String string2, int n, MaterialData materialData) {
        this.type = material;
        this.name = string;
        this.lore = string2;
        this.amount = n;
        this.data = materialData;
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(this.type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)this.name));
        itemMeta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes((char)'&', (String)this.lore)));
        itemMeta.spigot().setUnbreakable(this.unbreakable);
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(this.amount);
        itemStack.setData(this.data);
        return itemStack;
    }
}

