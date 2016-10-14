/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package net.propvp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.propvp.Practice;
import net.propvp.util.HiddenStringUtil;
import net.propvp.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class IconMenu
implements Listener {
    private String name;
    private int size;
    private OptionClickEventHandler handler;
    private String[] optionNames;
    private ItemStack[] optionIcons;
    private UUID[] optionUUIDs;
    private Player owner;
    List<Player> open;

    public IconMenu(String string, int n, OptionClickEventHandler optionClickEventHandler, Player player) {
        this.name = string;
        this.size = n;
        this.handler = optionClickEventHandler;
        this.optionNames = new String[n];
        this.optionIcons = new ItemStack[n];
        this.optionUUIDs = new UUID[n];
        this.open = new ArrayList<Player>();
        this.owner = player;
        Practice.getInstance().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)Practice.getInstance());
    }

    public /* varargs */ IconMenu setOption(int n, Material material, String string, String ... arrstring) {
        ItemStack itemStack = this.setItemNameAndLore(material, string, arrstring);
        if (this.optionIcons[n] != null && this.optionIcons[n].getType() == material) {
            this.updateItem(n, itemStack.getItemMeta());
        }
        this.optionIcons[n] = itemStack;
        this.optionNames[n] = string;
        if (arrstring[0] != null) {
            this.optionUUIDs[n] = arrstring[0] == "" ? null : (this.isUUID(HiddenStringUtil.extractHiddenString(arrstring[0])) ? UUID.fromString(HiddenStringUtil.extractHiddenString(arrstring[0])) : null);
        }
        return this;
    }

    public /* varargs */ IconMenu setOption(int n, ItemStack itemStack, String string, String ... arrstring) {
        ItemStack itemStack2 = this.setItemNameAndLore(itemStack, string, arrstring);
        if (this.optionIcons[n] != null && this.optionIcons[n] == itemStack) {
            this.updateItem(n, itemStack2.getItemMeta());
        }
        this.optionIcons[n] = itemStack2;
        this.optionNames[n] = string;
        return this;
    }

    public IconMenu setOption(int n, ItemBuilder itemBuilder) {
        ItemStack itemStack = this.setItemNameAndLore(itemBuilder.getItem(), itemBuilder.getItem().getItemMeta().getDisplayName(), null);
        if (this.optionIcons[n] != null && this.optionIcons[n] == itemBuilder.getItem()) {
            this.updateItem(n, itemStack.getItemMeta());
        }
        this.optionIcons[n] = itemStack;
        this.optionNames[n] = this.name;
        return this;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder)player, (int)this.size, (String)this.name);
        int n = 0;
        while (n < this.optionIcons.length) {
            if (this.optionIcons[n] != null) {
                inventory.setItem(n, this.optionIcons[n]);
            }
            ++n;
        }
        this.open.add(player);
        player.openInventory(inventory);
    }

    public void destroy() {
        HandlerList.unregisterAll((Listener)this);
        this.handler = null;
        this.optionNames = null;
        this.optionIcons = null;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getInventory().getTitle().equals(this.name)) {
            inventoryClickEvent.setCancelled(true);
            int n = inventoryClickEvent.getRawSlot();
            if (n >= 0 && n < this.size && this.optionNames[n] != null) {
                OptionClickEvent optionClickEvent = new OptionClickEvent(inventoryClickEvent.getCurrentItem(), (Player)inventoryClickEvent.getWhoClicked(), n, this.optionNames[n], this.optionUUIDs[n], this.owner);
                this.handler.onOptionClick(optionClickEvent);
                if (optionClickEvent.willClose()) {
                    final Player player = (Player)inventoryClickEvent.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)Practice.getInstance(), new Runnable(){

                        @Override
                        public void run() {
                            player.closeInventory();
                        }
                    }, 1);
                }
                if (optionClickEvent.willDestroy()) {
                    this.destroy();
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {
        if (this.open.contains((Object)inventoryCloseEvent.getPlayer())) {
            this.open.remove((Object)inventoryCloseEvent.getPlayer());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent playerQuitEvent) {
        if (this.open.contains((Object)playerQuitEvent.getPlayer())) {
            this.open.remove((Object)playerQuitEvent.getPlayer());
        }
    }

    private ItemStack setItemNameAndLore(Material material, String string, String[] arrstring) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)string));
        itemMeta.setLore(Arrays.asList(arrstring));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack setItemNameAndLore(ItemStack itemStack, String string, String[] arrstring) {
        ItemStack itemStack2 = itemStack;
        ItemMeta itemMeta = itemStack2.getItemMeta();
        itemMeta.setDisplayName(string);
        itemMeta.setLore(Arrays.asList(arrstring));
        itemStack2.setItemMeta(itemMeta);
        return itemStack2;
    }

    public void updateItem(int n, ItemMeta itemMeta) {
        for (Player player : this.open) {
            player.getOpenInventory().getItem(n).setItemMeta(itemMeta);
            player.updateInventory();
        }
    }

    public ItemStack getItem(int n) {
        return this.optionIcons[n];
    }

    public String getInventoryName() {
        return this.name;
    }

    public void resetOptions() {
        this.optionNames = new String[this.size];
        this.optionIcons = new ItemStack[this.size];
    }

    public void updateName(String string) {
        this.name = string;
    }

    public String getName() {
        return this.name;
    }

    public void updateSize(int n) {
        this.size = n;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isUUID(String string) {
        try {
            UUID.fromString(string);
            return true;
        }
        catch (Exception var2_2) {
            return false;
        }
    }

    public Player getOwner() {
        return this.owner;
    }

    public class OptionClickEvent {
        private Player player;
        private int position;
        private String name;
        private UUID uuid;
        private Player owner;
        private boolean close;
        private boolean destroy;
        private ItemStack icon;

        public OptionClickEvent(ItemStack itemStack, Player player, int n, String string, UUID uUID, Player player2) {
            this.icon = itemStack;
            this.player = player;
            this.position = n;
            this.name = string;
            this.uuid = uUID;
            this.owner = player2;
            this.close = true;
            this.destroy = false;
        }

        public ItemStack getItemStack() {
            return this.icon;
        }

        public Player getPlayer() {
            return this.player;
        }

        public int getPosition() {
            return this.position;
        }

        public String getName() {
            return this.name;
        }

        public UUID getUUID() {
            return this.uuid;
        }

        public Player getOwner() {
            return this.owner;
        }

        public boolean willClose() {
            return this.close;
        }

        public boolean willDestroy() {
            return this.destroy;
        }

        public void setWillClose(boolean bl) {
            this.close = bl;
        }

        public void setWillDestroy(boolean bl) {
            this.destroy = bl;
        }
    }

    public static interface OptionClickEventHandler {
        public void onOptionClick(OptionClickEvent var1);
    }

}

