/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.BookMeta
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.propvp.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.propvp.Practice;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import net.propvp.util.InventoryUtil;
import net.propvp.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class InventoryManager {
    public static Map<String, Inventory> invs = new HashMap<String, Inventory>();
    public static List<UUID> opened = new ArrayList<UUID>();

    public static ItemStack[] getSoloInventory(Player player) {
        ItemStack[] arritemStack = new ItemStack[9];
        arritemStack[0] = InventoryManager.getRankedItem();
        arritemStack[1] = InventoryManager.getUnrankedItem();
        arritemStack[7] = InventoryManager.getEditorItem();
        arritemStack[8] = InventoryManager.getHiderItem(player);
        return arritemStack;
    }

    public static ItemStack[] getPartyLeaderInventory(Player player) {
        ItemStack[] arritemStack = new ItemStack[9];
        arritemStack[0] = InventoryManager.getPartyItem();
        arritemStack[7] = InventoryManager.getPartyInfoItem();
        arritemStack[8] = InventoryManager.getPartyLeaveItem();
        return arritemStack;
    }

    public static ItemStack[] getPartyMemberInventory(Player player) {
        ItemStack[] arritemStack = new ItemStack[9];
        arritemStack[7] = InventoryManager.getPartyInfoItem();
        arritemStack[8] = InventoryManager.getPartyLeaveItem();
        return arritemStack;
    }

    public static ItemStack getRankedItem() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((Object)ChatColor.RED + "Ranked Matches");
        itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Right-click to view all of the ranked queues."));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getUnrankedItem() {
        ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((Object)ChatColor.YELLOW + "Unranked Matches");
        itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Right-click to view all of the unranked queues."));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getEditorItem() {
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((Object)ChatColor.GOLD + "Kit Editor");
        itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Right-click to edit your kits."));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getPartyItem() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((Object)ChatColor.BLUE + "Party Matches");
        itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Right-click to view all of the party queues."));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getPartyLeaveItem() {
        ItemStack itemStack = new ItemStack(Material.INK_SACK, 1, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((Object)ChatColor.RED + "Leave Party");
        itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Right-click to leave your party."));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getPartyInfoItem() {
        ItemStack itemStack = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((Object)ChatColor.GRAY + "Party Information");
        itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Right-click to show information about your party."));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getQueueLeaveItem() {
        ItemStack itemStack = new ItemStack(Material.INK_SACK, 1, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((Object)ChatColor.RED + "Leave queue");
        itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Right-click to leave the queue."));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getHiderItem(Player player) {
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        ItemStack itemStack = playerData.isHidingPlayers() ? new ItemStack(Material.INK_SACK, 1, 8) : new ItemStack(Material.INK_SACK, 1, 10);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (playerData.isHidingPlayers()) {
            itemMeta.setDisplayName((Object)ChatColor.RED + "Hiding Players" + (Object)ChatColor.GRAY + " (Right-click)");
            itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Right-click to show players again."));
        } else {
            itemMeta.setDisplayName((Object)ChatColor.GREEN + "Showing Players" + (Object)ChatColor.GRAY + " (Right-click)");
            itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Right-click to hide players."));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getHandBook() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add((Object)ChatColor.BLACK + "ProPractice Handbook\n" + (Object)ChatColor.GRAY + "This handbook shows you the ways of ProPractice and useful utilities.\n\n" + (Object)ChatColor.BLACK + "Chapter 1: Game-types\n" + (Object)ChatColor.GRAY + "Game-types (game-modes) consist of variables and rules that tell a match how to function.\nContinue on page 2.");
        arrayList.add((Object)ChatColor.BLACK + "List of game-type variables.\n" + (Object)ChatColor.GRAY + "build <true/false>\nhunger<true/false>\nregen<true/false>\nhitdelay<number>\n\nYou can set game-type varibales by using the command: '/practice setvar (var) (value)'");
        arrayList.add((Object)ChatColor.BLACK + "Chapter 2: Arenas\n" + (Object)ChatColor.GRAY + "Creating arenas is pretty simple. Use the following command to create one: '/arena create (name)'\n\nOnce you run that command you need to set the regions and the spawn points.");
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta)itemStack.getItemMeta();
        bookMeta.setTitle((Object)ChatColor.GOLD + "ProPractice Handbook");
        bookMeta.setAuthor("ProPractice");
        bookMeta.setPages(arrayList);
        itemStack.setItemMeta((ItemMeta)bookMeta);
        return itemStack;
    }

    public static ItemStack getRegionSelector() {
        ItemStack itemStack = new ItemStack(Material.GOLD_AXE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName((Object)ChatColor.GOLD + "Region Selector");
        itemMeta.setLore(Arrays.asList((Object)ChatColor.GRAY + "Left click to select location 1.", "Right-click to select location 2."));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void storeInv(Player player, boolean bl) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)54, (String)player.getName());
        PlayerInventory playerInventory = player.getInventory();
        int n = 9;
        while (n <= 35) {
            inventory.setItem(n - 9, playerInventory.getContents()[n]);
            ++n;
        }
        n = 0;
        while (n <= 8) {
            inventory.setItem(n + 27, playerInventory.getContents()[n]);
            ++n;
        }
        inventory.setItem(36, playerInventory.getHelmet());
        inventory.setItem(37, playerInventory.getChestplate());
        inventory.setItem(38, playerInventory.getLeggings());
        inventory.setItem(39, playerInventory.getBoots());
        if (bl) {
            inventory.setItem(48, new ItemBuilder(Material.SKULL_ITEM, (Object)ChatColor.RED + "Player Died", "", 1).getItem());
        } else {
            inventory.setItem(48, new ItemBuilder(Material.SPECKLED_MELON, (Object)ChatColor.GREEN + "Player Health", "", (int)player.getHealthScale()).getItem());
        }
        inventory.setItem(49, new ItemBuilder(Material.COOKED_BEEF, (Object)ChatColor.GREEN + "Player Food", "", player.getFoodLevel()).getItem());
        ItemStack itemStack = new ItemBuilder(Material.POTION, (Object)ChatColor.BLUE + "Potion Effects", "", player.getActivePotionEffects().size()).getItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        List list = itemMeta.getLore();
        list.addAll(player.getActivePotionEffects().stream().map(potionEffect -> String.valueOf(potionEffect.getType().getName()) + " " + (potionEffect.getAmplifier() + 1) + " for " + InventoryUtil.formatSeconds(potionEffect.getDuration() / 20) + "!").collect(Collectors.toList()));
        itemMeta.setLore(list);
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(50, itemStack);
        invs.put(player.getName(), inventory);
        new BukkitRunnable(){

            public void run() {
                InventoryManager.invs.remove(Player.this.getName());
            }
        }.runTaskLater((Plugin)Practice.getInstance(), 2400);
    }

}

