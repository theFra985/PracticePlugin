/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.propvp.player;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInv {
    private ItemStack[] contents;
    private ItemStack[] armorContents;

    public PlayerInv() {
    }

    public PlayerInv(ItemStack[] arritemStack, ItemStack[] arritemStack2) {
        this.contents = arritemStack;
        this.armorContents = arritemStack2;
    }

    public static PlayerInv fromPlayerInventory(PlayerInventory playerInventory) {
        return new PlayerInv(playerInventory.getContents(), playerInventory.getArmorContents());
    }

    public ItemStack[] getContents() {
        return this.contents;
    }

    public void setContents(ItemStack[] arritemStack) {
        this.contents = arritemStack;
    }

    public ItemStack[] getArmorContents() {
        return this.armorContents;
    }

    public void setArmorContents(ItemStack[] arritemStack) {
        this.armorContents = arritemStack;
    }
}

