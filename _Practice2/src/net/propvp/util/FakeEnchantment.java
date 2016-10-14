/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.enchantments.EnchantmentTarget
 *  org.bukkit.inventory.ItemStack
 */
package net.propvp.util;

import java.lang.reflect.Field;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class FakeEnchantment
extends Enchantment {
    static {
        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(null, true);
        }
        catch (Exception var0_1) {
            throw new RuntimeException("Can't register enchantment", var0_1);
        }
        if (Enchantment.getByName((String)"CA_FAKE") == null) {
            Enchantment.registerEnchantment((Enchantment)new FakeEnchantment());
        }
    }

    public FakeEnchantment() {
        super(100);
    }

    public String getName() {
        return "CA_FAKE";
    }

    public int getMaxLevel() {
        return 0;
    }

    public int getStartLevel() {
        return 0;
    }

    public EnchantmentTarget getItemTarget() {
        return null;
    }

    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    public boolean canEnchantItem(ItemStack itemStack) {
        return false;
    }
}

