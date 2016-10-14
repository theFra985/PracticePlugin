/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 */
package net.propvp.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.propvp.player.PlayerInv;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtil {
    public static String playerInventoryToString(PlayerInv playerInv) {
        StringBuilder stringBuilder = new StringBuilder();
        ItemStack[] arritemStack = playerInv.getArmorContents();
        int n = 0;
        while (n < arritemStack.length) {
            if (n == 3) {
                if (arritemStack[n] == null) {
                    stringBuilder.append(InventoryUtil.itemStackToString(new ItemStack(Material.AIR)));
                } else {
                    stringBuilder.append(InventoryUtil.itemStackToString(arritemStack[3]));
                }
            } else if (arritemStack[n] == null) {
                stringBuilder.append(InventoryUtil.itemStackToString(new ItemStack(Material.AIR))).append(";");
            } else {
                stringBuilder.append(InventoryUtil.itemStackToString(arritemStack[n])).append(";");
            }
            ++n;
        }
        stringBuilder.append("|");
        n = 0;
        while (n < playerInv.getContents().length) {
            stringBuilder.append(n).append("#").append(InventoryUtil.itemStackToString(playerInv.getContents()[n])).append(n == playerInv.getContents().length - 1 ? "" : ";");
            ++n;
        }
        return stringBuilder.toString();
    }

    public static String playerInventoryToString(PlayerInventory playerInventory) {
        StringBuilder stringBuilder = new StringBuilder();
        ItemStack[] arritemStack = playerInventory.getArmorContents();
        int n = 0;
        while (n < arritemStack.length) {
            if (n == 3) {
                if (arritemStack[n] == null) {
                    stringBuilder.append(InventoryUtil.itemStackToString(new ItemStack(Material.AIR)));
                } else {
                    stringBuilder.append(InventoryUtil.itemStackToString(arritemStack[3]));
                }
            } else if (arritemStack[n] == null) {
                stringBuilder.append(InventoryUtil.itemStackToString(new ItemStack(Material.AIR))).append(";");
            } else {
                stringBuilder.append(InventoryUtil.itemStackToString(arritemStack[n])).append(";");
            }
            ++n;
        }
        stringBuilder.append("|");
        n = 0;
        while (n < playerInventory.getContents().length) {
            stringBuilder.append(n).append("#").append(InventoryUtil.itemStackToString(playerInventory.getContents()[n])).append(n == playerInventory.getContents().length - 1 ? "" : ";");
            ++n;
        }
        return stringBuilder.toString();
    }

    public static PlayerInv playerInventoryFromString(String string) {
        PlayerInv playerInv = new PlayerInv();
        String[] arrstring = string.split("\\|");
        ItemStack[] arritemStack = new ItemStack[arrstring[0].split(";").length];
        int n = 0;
        while (n < arrstring[0].split(";").length) {
            arritemStack[n] = InventoryUtil.itemStackFromString(arrstring[0].split(";")[n]);
            ++n;
        }
        playerInv.setArmorContents(arritemStack);
        ItemStack[] arritemStack2 = new ItemStack[arrstring[1].split(";").length];
        String[] arrstring2 = arrstring[1].split(";");
        int n2 = arrstring2.length;
        int n3 = 0;
        while (n3 < n2) {
            String string2 = arrstring2[n3];
            int n4 = Integer.parseInt(string2.split("#")[0]);
            arritemStack2[n4] = string2.split("#").length == 1 ? null : InventoryUtil.itemStackFromString(string2.split("#")[1]);
            ++n3;
        }
        playerInv.setContents(arritemStack2);
        return playerInv;
    }

    public static String itemStackToString(ItemStack itemStack) {
        StringBuilder stringBuilder = new StringBuilder();
        if (itemStack != null) {
            Object object;
            String string = String.valueOf(itemStack.getType().getId());
            stringBuilder.append("t@").append(string);
            if (itemStack.getDurability() != 0) {
                object = String.valueOf(itemStack.getDurability());
                stringBuilder.append(":d@").append((String)object);
            }
            if (itemStack.getAmount() != 1) {
                object = String.valueOf(itemStack.getAmount());
                stringBuilder.append(":a@").append((String)object);
            }
            if ((object = itemStack.getEnchantments()).size() > 0) {
                for (Map.Entry object2 : object.entrySet()) {
                    stringBuilder.append(":e@").append(((Enchantment)object2.getKey()).getId()).append("@").append(object2.getValue());
                }
            }
            if (itemStack.hasItemMeta()) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta.hasDisplayName()) {
                    stringBuilder.append(":dn@").append(itemMeta.getDisplayName());
                }
                if (itemMeta.hasLore()) {
                    stringBuilder.append(":l@").append(itemMeta.getLore());
                }
            }
        }
        return stringBuilder.toString();
    }

    public static String inventoryToString(Inventory inventory) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < inventory.getContents().length) {
            stringBuilder.append(n).append("#").append(InventoryUtil.itemStackToString(inventory.getContents()[n]));
            if (n != inventory.getContents().length - 1) {
                stringBuilder.append(";");
            }
            ++n;
        }
        return stringBuilder.toString();
    }

    public static Inventory inventoryFromString(String string) {
        String[] arrstring;
        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)54);
        String[] arrstring2 = arrstring = string.split(";");
        String[] arrstring3 = arrstring;
        int n = arrstring3.length;
        int n2 = 0;
        while (n2 < n) {
            String[] arrstring4;
            String string2;
            inventory.setItem(Integer.parseInt(arrstring4[0]), (arrstring4 = (string2 = arrstring3[n2]).split("#")).length > 1 ? InventoryUtil.itemStackFromString(arrstring4[1]) : null);
            ++n2;
        }
        return inventory;
    }

    /*
     * Exception decompiling
     */
    public static ItemStack itemStackFromString(String var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:355)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:768)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:700)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    public static String formatSeconds(int n) {
        int n2 = n / 60;
        if (n2 == 0) {
            return String.valueOf(n) + " seconds";
        }
        return String.valueOf(n2) + " minutes and " + (n %= 60) + " seconds";
    }

    public static void listFields() {
        try {
            Method method;
            String string = Bukkit.getServer().getClass().getPackage().getName();
            Class class_ = Class.forName(String.valueOf(string) + ".inventory.CraftItemStack");
            Method[] arrmethod = class_.getDeclaredMethods();
            int n = arrmethod.length;
            int n2 = 0;
            while (n2 < n) {
                method = arrmethod[n2];
                Bukkit.getLogger().severe(method.getName());
                Parameter[] arrparameter = method.getParameters();
                int n3 = arrparameter.length;
                int n4 = 0;
                while (n4 < n3) {
                    Parameter parameter = arrparameter[n4];
                    Bukkit.getLogger().severe(String.valueOf(method.getName()) + " " + parameter.getName() + " " + parameter.getType().getName());
                    ++n4;
                }
                ++n2;
            }
            arrmethod = class_.getDeclaredFields();
            n = arrmethod.length;
            n2 = 0;
            while (n2 < n) {
                method = arrmethod[n2];
                Bukkit.getLogger().severe(String.valueOf(method.getType().getCanonicalName()) + " " + method.getName());
                ++n2;
            }
        }
        catch (ClassNotFoundException var0_1) {
            var0_1.printStackTrace();
        }
    }

    public static String getFriendlyItemName(ItemStack itemStack) {
        String string = Bukkit.getServer().getClass().getPackage().getName();
        try {
            Class class_ = Class.forName(String.valueOf(string) + ".inventory.CraftItemStack");
            Method method = class_.getDeclaredMethod("asNMSCopy", ItemStack.class);
            Object object = method.invoke(class_, new Object[]{itemStack});
            Method method2 = object.getClass().getDeclaredMethod("getName", new Class[0]);
            Object object2 = method2.invoke(object, null);
            return (String)object2;
        }
        catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException var2_3) {
            var2_3.printStackTrace();
            return null;
        }
    }
}

